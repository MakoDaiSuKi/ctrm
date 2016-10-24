package com.smm.ctrm.hibernate.Interceptor;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;

public class StaticDelegateInterceptor extends EmptyInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6634627253858739982L;

	private static Interceptor interceptor;

	public static void setInterceptor(Interceptor interceptor) {
		StaticDelegateInterceptor.interceptor = interceptor;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		return StaticDelegateInterceptor.interceptor.onSave(entity, id, state, propertyNames, types);
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		return StaticDelegateInterceptor.interceptor.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}
	
	
}
