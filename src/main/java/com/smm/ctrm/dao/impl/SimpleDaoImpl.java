package com.smm.ctrm.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.dao.SimpleDao;
import com.smm.ctrm.domain.SimpleEntity;

@Repository
public class SimpleDaoImpl<T extends SimpleEntity> extends HibernateDaoSupport implements SimpleDao<T> {

	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public void Save(T obj) {
		try {
			// 必须加上此行代码
			currentSession().clear();
			currentSession().save(obj);
			currentSession().flush();
		} catch (StaleObjectStateException e) {
			if (e.getMessage().startsWith("Row was updated or deleted by another transaction")) {
				throw new RuntimeException("数据已过期，请刷新后再继续操作！");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getList(DetachedCriteria dc) {
		return (List<T>) getHibernateTemplate().findByCriteria(dc);
	}
	
	@Override
	public void PhysicsDelete(T t) {
		currentSession().delete(t);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void PhysicsDelete(String id, Class<T> clazz) {
		currentSession().clear();
		PhysicsDelete((T)currentSession().load(clazz, id));
		currentSession().flush();
	}

}
