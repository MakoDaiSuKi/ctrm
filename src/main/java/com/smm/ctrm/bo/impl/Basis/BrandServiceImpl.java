package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.BrandService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Physical.FinishedProduct;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

@Service
public class BrandServiceImpl implements BrandService {

	private static final Logger logger = Logger.getLogger(AccountServiceImpl.class);

	@Autowired
	private HibernateRepository<Brand> brandRepo;
	
	@Autowired
	private HibernateRepository<Lot> lotRepo;
	
	@Autowired
	private HibernateRepository<FinishedProduct> finishedProductRepo;
	
	@Autowired
	private HibernateRepository<ReceiptShip> receiptShipRepo;

	@Override
	public ActionResult<List<Brand>> BrandsByCommodityId(String commodityId) {

		DetachedCriteria where = DetachedCriteria.forClass(Brand.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("CommodityId", commodityId));

		List<Brand> list = brandRepo.GetQueryable(Brand.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Brand>> BackBrandsByCommodityId(String commodityId) {

		DetachedCriteria where = DetachedCriteria.forClass(Brand.class);
		where.add(Restrictions.eq("CommodityId", commodityId));

		List<Brand> list = brandRepo.GetQueryable(Brand.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Brand>> Brands() {

		DetachedCriteria where = DetachedCriteria.forClass(Brand.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Brand> list = brandRepo.GetQueryable(Brand.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toCacheList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Brand>> BackBrands() {

		List<Brand> list = brandRepo.GetQueryable(Brand.class).OrderBy(Order.desc("OrderIndex"))
				.toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<Brand> Save(Brand brand) {

		try {
			// 检查重复
			DetachedCriteria where = DetachedCriteria.forClass(Brand.class);
			where.add(Restrictions.eq("IsHidden", false));
			where.add(Restrictions.eq("CommodityId", brand.getCommodityId()));
			if (brand.getId() != null) {
				where.add(Restrictions.ne("Id", brand.getId()));
			}
			where.add(Restrictions.or(Restrictions.eq("Name", brand.getName()),
					Restrictions.eq("Code", brand.getCode())));

			List<Brand> list = brandRepo.GetQueryable(Brand.class).where(where)
					.OrderBy(Order.desc("OrderIndex")).toList();

			if (list != null && list.size() > 0)
				throw new Exception(MessageCtrm.DuplicatedName);

			brandRepo.SaveOrUpdate(brand);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public ActionResult<String> Delete(String id) {
		try {
			brandRepo.PhysicsDelete(id, Brand.class);
			return new ActionResult<>(true, MessageCtrm.DeleteSuccess);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<Brand> GetById(String id) {
		Brand brand = brandRepo.getOneById(id, Brand.class);
		return new ActionResult<>(true, null, brand);
	}
}
