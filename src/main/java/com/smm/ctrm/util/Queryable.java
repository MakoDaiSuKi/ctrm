package com.smm.ctrm.util;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.HibernateEntity;

/**
 * Created by hao.zheng on 2016/4/26. 查询辅助工具
 */
public class Queryable<T extends HibernateEntity> {

	private int firstResult = 0;

	private int maxResults = Integer.MAX_VALUE;

	private DetachedCriteria where;

	private HibernateRepository<T> repository;

	private final String StandardQueryCache = "StandardQueryCache";

	Class<T> clazz;

	public Queryable(HibernateRepository<T> repository, Class<T> clazz) {
		this.clazz = clazz;
		this.repository = repository;
	}

	public Queryable<T> where(DetachedCriteria where) {
		this.where = where;
		return this;
	}

	public Queryable<T> OrderBy(Order orderBy) {
		where.addOrder(orderBy);
		return this;
	}

	@SuppressWarnings("unchecked")
	public List<T> toList() {
		where.add(Property.forName("IsDeleted").eq(Boolean.FALSE));
		return (List<T>) repository.getHibernateTemplate().findByCriteria(where, firstResult, maxResults);
	}

	@SuppressWarnings("unchecked")
	public List<T> toCacheList() {
		HibernateTemplate template = repository.getHibernateTemplate();
		template.setCacheQueries(true);
		template.setQueryCacheRegion(StandardQueryCache);
		where.add(Property.forName("IsDeleted").eq(Boolean.FALSE));
		return (List<T>) template.findByCriteria(where, firstResult, maxResults);
	}

	@SuppressWarnings("unchecked")
	public List<Double> toDouble() {
		where.add(Property.forName("IsDeleted").eq(Boolean.FALSE));
		return (List<Double>) repository.getHibernateTemplate().findByCriteria(where, firstResult, maxResults);
	}

	@SuppressWarnings("unchecked")
	public List<String> toStr() {
		where.add(Property.forName("IsDeleted").eq(Boolean.FALSE));
		return (List<String>) repository.getHibernateTemplate().findByCriteria(where, firstResult, maxResults);
	}

	public T firstOrDefault() {
		where.add(Property.forName("IsDeleted").eq(Boolean.FALSE));
		HibernateTemplate template = repository.getHibernateTemplate();
		template.setCacheQueries(true);
		template.setQueryCacheRegion(StandardQueryCache);
		@SuppressWarnings("unchecked")
		List<T> list = (List<T>) template.findByCriteria(where, 0, 1);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public DetachedCriteria getWhere() {
		return where;
	}

	public void setWhere(DetachedCriteria where) {
		this.where = where;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public void setMaxResult(int maxResults) {
		this.maxResults = maxResults;
	}

}
