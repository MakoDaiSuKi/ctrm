package com.smm.ctrm.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.smm.ctrm.dao.ResourceDao;
import com.smm.ctrm.domain.Basis.Resource;

@Repository("resourceDao")
public class ResourceDaoImpl implements ResourceDao{

	@javax.annotation.Resource
	private SessionFactory sessionFactory;
	
	private Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	@Override
	public Resource load(String id) {
		return (Resource) this.getCurrentSession().get(Resource.class, id);
	}

	@Override
	public Resource get(String id) {
		return (Resource) this.getCurrentSession().get(Resource.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Resource> findAll() {
		List<Resource> resource = this.getCurrentSession().createQuery("from Resource").setCacheable(true).list();
		return resource;
	}

	@Override
	public void persist(Resource entity) {
		
	}

	@Override
	public String save(Resource entity) {
		return null;
	}

	@Override
	public void saveOrUpdate(Resource entity) {
		
	}

	@Override
	public void delete(String id) {
		
	}

	@Override
	public void flush() {
		
	}

}
