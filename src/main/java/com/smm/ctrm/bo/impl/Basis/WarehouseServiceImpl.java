package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.WarehouseService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Warehouse;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class WarehouseServiceImpl implements WarehouseService {

	private static final Logger logger = Logger.getLogger(WarehouseServiceImpl.class);

	@Autowired
	private HibernateRepository<Warehouse> repository;

	@Override
	public ActionResult<List<Warehouse>> Warehouses() {

		DetachedCriteria where = DetachedCriteria.forClass(Warehouse.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Warehouse> list = this.repository.GetQueryable(Warehouse.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toCacheList();

		ActionResult<List<Warehouse>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	public ActionResult<List<Warehouse>> BackWarehouses() {

		List<Warehouse> list = this.repository.GetQueryable(Warehouse.class).OrderBy(Order.desc("OrderIndex")).toList();

		ActionResult<List<Warehouse>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	public ActionResult<Warehouse> Save(Warehouse warehouse) {

		ActionResult<Warehouse> result = new ActionResult<>();

		try {

			// 检查重复
			DetachedCriteria where = DetachedCriteria.forClass(Warehouse.class);
			where.add(Restrictions.eq("Name", warehouse.getName()));
			if (warehouse.getId() != null) {
				where.add(Restrictions.ne("Id", warehouse.getId()));
			}
			List<Warehouse> list = this.repository.GetQueryable(Warehouse.class).where(where).toList();

			if (list != null && list.size() > 0)
				throw new Exception(MessageCtrm.DuplicatedName);

			this.repository.SaveOrUpdate(warehouse);

			result.setSuccess(true);
			result.setMessage(MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			result.setSuccess(false);
			result.setMessage(e.getMessage());

		}

		return result;
	}

	@Override
	public ActionResult<String> Delete(String id) {

		this.repository.PhysicsDelete(id, Warehouse.class);
		return new ActionResult<>(true, MessageCtrm.DeleteSuccess);
	}

	@Override
	public ActionResult<Warehouse> GetById(String id) {

		ActionResult<Warehouse> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(this.repository.getOneById(id, Warehouse.class));

		return result;
	}

	@Override
	public void MoveUp(String id) {

	}

	@Override
	public void MoveDown(String id) {

	}

}
