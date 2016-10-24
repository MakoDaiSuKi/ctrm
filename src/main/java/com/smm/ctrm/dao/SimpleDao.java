package com.smm.ctrm.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.smm.ctrm.domain.SimpleEntity;

public interface SimpleDao<T extends SimpleEntity> {
	
    /**
     * 更新或者新增
     * @param obj
     * @throws Exception 
     */
    void Save(T obj) throws RuntimeException;

	List<T> getList(DetachedCriteria dc);

	void PhysicsDelete(T t);

	void PhysicsDelete(String id, Class<T> clazz);
	
}
