package com.smm.ctrm.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.proxy.HibernateProxy;

import com.smm.ctrm.domain.HibernateEntity;

public class BeanUtils {

	private static final String GET = "get";
	private static final String SET = "set";

	private static final String SYSTEM_VARIABLE_PATH = "java.lang|java.util.Date|java.math.BigDecimal";
	private static final Pattern pattern = Pattern.compile(SYSTEM_VARIABLE_PATH);

	/**
	 * 复制entity中的非domain属性到新的实体
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T extends HibernateEntity> T copy(T entity) {
		if (entity instanceof HibernateProxy) {
			entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		}

		T result;
		Class<?> entityClass = entity.getClass();
		try {
			result = (T) entity.getClass().newInstance();

			Method[] superMethods = entityClass.getSuperclass().getDeclaredMethods();
			Method[] methods = entityClass.getDeclaredMethods();
			methods = ArrayUtils.addAll(superMethods, methods);
			Field[] superFields = entityClass.getSuperclass().getDeclaredFields();
			Field[] fields = entityClass.getDeclaredFields();
			fields = ArrayUtils.addAll(superFields, fields);
			Map<String, Method> methodMap = new HashMap<>();
			for (Method m : methods) {
				if (m.getName().startsWith(GET) || m.getName().startsWith(SET)) {
					methodMap.put(m.getName(), m);
				}
			}
			for (Field field : fields) {
				if (!pattern.matcher(field.getType().getName()).find()) {
					continue;
				}
				Method getMethod = methodMap.get(GET + field.getName());
				Method setMethod = methodMap.get(SET + field.getName());
				if (getMethod != null && setMethod != null) {
					Object value = getMethod.invoke(entity);
					if (value != null) {
						setMethod.invoke(result, value);
					}
				}
			}
			return result;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return entity;
	}

	/**
	 * 消除entity中的非domain属性
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T extends HibernateEntity> void simple(T entity) {

		if (entity instanceof HibernateProxy) {
			entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		}
		Class<?> entityClass = entity.getClass();
		try {
			Method[] superMethods = entityClass.getSuperclass().getDeclaredMethods();
			Method[] methods = entityClass.getDeclaredMethods();
			methods = ArrayUtils.addAll(superMethods, methods);
			Field[] superFields = entityClass.getSuperclass().getDeclaredFields();
			Field[] fields = entityClass.getDeclaredFields();
			fields = ArrayUtils.addAll(superFields, fields);
			Map<String, Method> methodMap = new HashMap<>();
			for (Method m : methods) {
				if (m.getName().startsWith(SET)) {
					methodMap.put(m.getName(), m);
				}
			}
			for (Field field : fields) {
				if (!pattern.matcher(field.getType().getName()).find()) {
					continue;
				}
				Method setMethod = methodMap.get(SET + field.getName());
				if (setMethod != null) {
					setMethod.invoke(entity, (Object) null);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
