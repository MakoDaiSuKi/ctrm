package com.smm.ctrm.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.ResourceBo;
import com.smm.ctrm.dao.ResourceDao;
import com.smm.ctrm.domain.Basis.Resource;

@Service("resourceBo")
public class ResourceBoImpl implements ResourceBo {

	@Autowired
	private ResourceDao resourceDao;

	@Override
	public Resource load(String id) {
		return resourceDao.load(id);
	}

	@Override
	public Resource get(String id) {
		return resourceDao.get(id);
	}

	@Override
	public List<Resource> findAll() {
		return resourceDao.findAll();
	}

	@Override
	public void persist(Resource entity) {
		resourceDao.persist(entity);
	}

	@Override
	public String save(Resource entity) {
		return resourceDao.save(entity);
	}

	@Override
	public void saveOrUpdate(Resource entity) {
		resourceDao.saveOrUpdate(entity);
	}

	@Override
	public void delete(String id) {
		resourceDao.delete(id);
	}

	@Override
	public void flush() {
		resourceDao.flush();
	}

}
