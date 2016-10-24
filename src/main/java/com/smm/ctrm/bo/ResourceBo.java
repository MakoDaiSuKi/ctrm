package com.smm.ctrm.bo;

import java.util.List;

import com.smm.ctrm.domain.Basis.Resource;

public interface ResourceBo {
	
	Resource load(String id);

	Resource get(String id);

	List<Resource> findAll();

	void persist(Resource entity);

	String save(Resource entity);

	void saveOrUpdate(Resource entity);

	void delete(String id);

	void flush();
}
