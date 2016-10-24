package com.smm.ctrm.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.LoginInfoToken;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.Queryable;
import com.smm.ctrm.util.RefUtil;

@Repository("repository")
@SuppressWarnings("unchecked")
@Transactional
public class HibernateRepositoryImpl<T extends HibernateEntity> extends HibernateDaoSupport
		implements HibernateRepository<T> {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public Session getCurrentSession() {
		return currentSession();
	}

	@Override
	public FlushMode getFlushMode() {
		return currentSession().getHibernateFlushMode();
	}

	@Override
	public void setFlushMode(FlushMode flushMode) {
		currentSession().setHibernateFlushMode(flushMode);
	}

	@Override
	public void Flush() {
		currentSession().flush();
	}

	@Override
	public void Clear() {
		currentSession().clear();
	}

	@Override
	public void Evict(T obj) {
		currentSession().evict(obj);
	}

	@Override
	public void BeginTransaction() {

		currentSession().beginTransaction();
	}

	@Override
	public void BeginTransaction(Long isolationLevel) {
		currentSession().beginTransaction();
	}

	@Override
	public void CommitTransaction() {
		Transaction transaction = currentSession().getTransaction();

		if (transaction != null && !transaction.getStatus().equals(TransactionStatus.COMMITTED)) {
			transaction.commit();
		}
	}

	@Override
	public void RollbackTransaction() {
		Transaction transaction = currentSession().getTransaction();
		if (transaction != null && !transaction.getStatus().equals(TransactionStatus.ROLLED_BACK)) {
			transaction.rollback();
		}
	}

	@Override
	public List<T> GetList(Class<T> clazz) {

		return CreateCriteria(clazz).add(Property.forName("IsDeleted").eq(Boolean.FALSE)).list();
	}

	@Override
	public List<T> GetList(Criteria criteria) {

		return criteria.add(Property.forName("IsDeleted").eq(Boolean.FALSE)).list();
	}

	@Override
	public void Save(T obj) {
		LoginInfoToken loginInfo = LoginHelper.GetLoginInfo();
		String userName = null;
		if (loginInfo != null) {
			userName = loginInfo.getName();
		}
		obj.setIsDeleted(Boolean.FALSE);
		obj.setIsHidden(Boolean.FALSE);
		obj.setCreatedAt(new Date());
		obj.setCreatedBy(userName);
		currentSession().save(obj);
		currentSession().flush();
	}

	@Override
	public void SaveForJob(T obj) {
		obj.setIsDeleted(Boolean.FALSE);
		obj.setIsHidden(Boolean.FALSE);
		obj.setCreatedAt(new Date());
		obj.setCreatedBy("系统定时任务");
		currentSession().saveOrUpdate(obj);
		currentSession().flush();
	}

	@Override
	public String SaveOrUpdateRetrunId(T obj) {
		try {
			addUserAndTimeInfo(obj);
			Clear();
			currentSession().saveOrUpdate(obj);
			currentSession().flush();
			return obj.getId();
		} catch (Exception e) {
			logger.info("数据保存发生异常:" + e.getMessage());
			if (e.getMessage().startsWith("Row was updated or deleted by another transaction")) {
				throw new RuntimeException("数据已过期，请刷新后再继续操作！");
			}
		}

		return null;
	}


	private void addUserAndTimeInfo(T obj) {

		LoginInfoToken loginInfo = LoginHelper.GetLoginInfo();
		String userName = null;
		if (loginInfo != null) {
			userName = loginInfo.getName();
		}
		Date now = new Date();
		if (StringUtils.isBlank(obj.getId())) {
			obj.setCreatedBy(userName);
			obj.setCreatedAt(now);
		} else {
			obj.setUpdatedBy(userName);
			obj.setUpdatedAt(now);
		}
		if (obj.getCreatedAt() == null) {
			obj.setCreatedAt(now);
		}
	}

	@Override
	public void SaveOrUpdate(T obj) {
		try {
			addUserAndTimeInfo(obj);
			// 必须加上此行代码
			Clear();
			currentSession().saveOrUpdate(obj);
			if(obj instanceof Lot) {
				Lot l = (Lot) obj;
				if(l.getIsInvoiced() == null) {
					throw new RuntimeException("竟然出错了！！！！" + l);
				}
			}
			currentSession().flush();
		} catch (Exception e) {
			logger.error("数据保存发生异常:" + e.getMessage(), e);
			if(StringUtils.isNotBlank(e.getMessage())) {
				if (e.getMessage().startsWith("Row was updated or deleted by another transaction")) {
					throw new RuntimeException("数据已过期，请刷新后再继续操作！");
				} else if (e.getMessage().startsWith("Transaction rolled back because it has been marked as rollback-only")) {
					throw new RuntimeException("数据已过期，请刷新后再继续操作！");
				} else if (e.getMessage().startsWith("Batch update returned unexpected row count from update")) {
					throw new RuntimeException("待更新数据已不存在");
				}else if (e.getMessage()
						.startsWith("org.hibernate.exception.LockAcquisitionException: could not execute query")) {
					throw new RuntimeException("其他用户正在操作，请稍后重试");
				} else {
					throw e;
				}
			} else {
				throw e;
			}
		}
	}

	@Override
	public void Merge(T obj) {
		currentSession().merge(obj);
		currentSession().flush();
	}

	@Override
	public Queryable<T> GetQueryable(Class<T> clazz) {
		return new Queryable<>(this, clazz).where(DetachedCriteria.forClass(clazz));
	}

	@Override
	public void PhysicsDelete(String id, Class<T> clazz) {
		Clear();
		PhysicsDelete((T) currentSession().load(clazz, id));
		currentSession().flush();
	}

	@Override
	public T getOneById(String id, Class<T> clazz) {
		return currentSession()
				.createQuery("select t from " + clazz.getSimpleName() + " t where id = '" + id + "'", clazz)
				.setCacheable(true).uniqueResult();
	}
	
	@Override
	public boolean ExecuteNonQuery(String sql) {
		try {
			currentSession().createNativeQuery(sql).executeUpdate();
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	@Override
	public ActionResult<List<T>> GetPage(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy,
			RefUtil total) {
		Clear();
		criteria.add(Property.forName("IsDeleted").eq(Boolean.FALSE));
		criteria.setProjection(Projections.rowCount());
		total.setTotal(((Long) criteria.uniqueResult()).intValue());
		criteria.setProjection(null);

		pageIndex = pageIndex == 0 ? 1 : pageIndex;
		pageSize = pageSize == 0 ? 9999 : pageSize;
		criteria.addOrder(Order.desc("CreatedAt"));// 默认按创建时间倒序
		if (StringUtils.isNotBlank(sortBy) && !sortBy.equals("CreatedAt")) {
			criteria.addOrder(orderBy != null && orderBy.equals("Ascending") ? Order.asc(sortBy) : Order.desc(sortBy));
		}
		List<T> data = criteria.setFirstResult((pageIndex - 1) * pageSize).setMaxResults(pageSize)
				.setResultTransformer(CriteriaSpecification.ROOT_ENTITY).list();
		return new ActionResult<>(true, "", data, total);
	}

	@Override
	public Criteria CreateCriteria(Class<T> clazz) {
		return currentSession().createCriteria(clazz);
	}

	@Override
	public List<T> ExecuteSql(String sql, Class<T> clazz) {

		return currentSession().createQuery(sql).getResultList();
	}
	
	@Override
	public List<T> ExecuteHql(String hql, Map<String, Object> params, Class<T> clazz) {
		Query<T> query = getCurrentSession().createQuery(hql, clazz);
		for(Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		
		return query.getResultList();
	}
	
	public int ExecuteHql2Update(String hql, Map<String, Object> params) {
		Query<T> query = getCurrentSession().createQuery(hql);
		for(Entry<String, Object> entry : params.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		
		return query.executeUpdate();
	}

	@Override
	public List<Object[]> ExecuteProcedureSql(String procName, Map<String, Object> parameters) {
		return getSqlQueryForNameQuery(procName, parameters).getResultList();
	}

	@Override
	public void ExecuteProcedureSql2(String procName, Map<String, Object> parameters) {
		getSqlQueryForNameQuery(procName, parameters).executeUpdate();
	}

	@Override
	public void PhysicsDelete(T t) {
		currentSession().delete(t);
	}

	public List<Object[]> ExecuteProcedureSql(String procName, Map<String, Object> parameters, Class<?> returnClass) {
		return getSqlQueryForNameQuery(procName, parameters).addEntity(returnClass).list();
	}

	private NativeQuery getSqlQueryForNameQuery(String procName, Map<String, Object> parameters) {
		StringBuilder procedureSql = new StringBuilder("{CALL " + procName + "(");
		int index = 0;
		for (String key : parameters.keySet()) {
			if (index > 0) {
				procedureSql.append(",");
			}
			procedureSql.append(":" + key);
			index++;
		}
		procedureSql.append(")}");
		NativeQuery query = currentSession().createNativeQuery(procedureSql.toString());
		for (Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return query;
	}

	public List<Object[]> ExecuteProcedure(String procName, Map<String, Object> parameters) {
		StoredProcedureQuery storedProcedureQuery = currentSession().createStoredProcedureQuery(procName);
		for (Entry<String, Object> entry : parameters.entrySet()) {
			storedProcedureQuery.setParameter("@" + entry.getKey(), entry.getValue());
		}
		return storedProcedureQuery.getResultList();
	}

	@Override
	public Map<String, Object> ExecuteProcedureSqlOutMap(String procName, Map<String, Object> parameters,
			Map<String, String> outParame) {

		try {
			StringBuilder procedureSql = new StringBuilder("{call " + procName + "(");
			int index = 0;
			int size = parameters.keySet().size();
			for (int i = 0; i < size; i++) {
				if (index > 0) {
					procedureSql.append(",");
				}
				procedureSql.append("?");
				index++;
			}

			size = outParame.keySet().size();
			for (int i = 0; i < size; i++) {
				if (index > 0) {
					procedureSql.append(",");
				}
				procedureSql.append("?");
				index++;
			}

			procedureSql.append(")}");

			Connection con = SessionFactoryUtils.getDataSource(getSessionFactory()).getConnection();

			CallableStatement statement = con.prepareCall(procedureSql.toString());
			for (Entry<String, Object> p : parameters.entrySet()) {
				statement.setObject(p.getKey(), p.getValue());
			}
			for (Entry<String, String> o : outParame.entrySet()) {
				switch (o.getValue()) {
				case "DECIMAL":
					statement.registerOutParameter(o.getKey(), Types.DECIMAL);
					break;
				case "VARCHAR":
					statement.registerOutParameter(o.getKey(), Types.VARCHAR);
					break;
				default:
					break;
				}
			}
			statement.executeUpdate();
			Map<String, Object> oMap = new LinkedHashMap<>();
			for (Entry<String, String> o : outParame.entrySet()) {
				switch (o.getValue()) {
				case "DECIMAL":
					oMap.put(o.getKey(), statement.getBigDecimal(o.getKey()));
					break;
				case "VARCHAR":
					oMap.put(o.getKey(), statement.getString(o.getKey()));
					break;
				default:
					break;
				}
			}
			return oMap;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 标准sql
	 */
	@Override
	public List<T> ExecuteCorrectlySql(String sql, Class<T> clazz) {

		return (List<T>) currentSession().createNativeQuery(sql, clazz).getResultList();
	}

	/**
	 * 标准sql
	 */
	@Override
	public List<String> ExecuteCorrectlySql2(String sql) {

		return (List<String>) currentSession().createNativeQuery(sql).getResultList();
	}

	/**
	 * 标准sql
	 */
	@Override
	public void ExecuteCorrectlySql3(String sql) {

		currentSession().createNativeQuery(sql).executeUpdate();
	}

	/**
	 * 返回一条记录
	 */
	@Override
	public Object ExecuteScalarSql(String sql) {
		return currentSession().createNativeQuery(sql).uniqueResult();
	}

	/**
	 * 标准sql
	 */
	@Override
	public List<String> ExecuteCorrectlySql4(String sql, String scalar) {
		return (List<String>) currentSession().createNativeQuery(sql).addScalar(scalar, StringType.INSTANCE)
				.getResultList();
	}

}
