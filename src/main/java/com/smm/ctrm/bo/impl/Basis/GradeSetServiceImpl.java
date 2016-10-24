package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.GradeSetService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.GradeSet;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

@Service
public class GradeSetServiceImpl implements GradeSetService {

	private static final Logger logger = Logger.getLogger(AccountServiceImpl.class);

	@Autowired
	private HibernateRepository<GradeSet> gradeSetRepo;
	
	public ActionResult<List<GradeSet>> GradeSetsByCommodityId(String commodityId) {

		DetachedCriteria where = DetachedCriteria.forClass(GradeSet.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("CommodityId", commodityId));

		List<GradeSet> list = gradeSetRepo.GetQueryable(GradeSet.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	public ActionResult<List<GradeSet>> BackGradeSetsByCommodityId(String commodityId){
		DetachedCriteria where = DetachedCriteria.forClass(GradeSet.class);
		where.add(Restrictions.eq("CommodityId", commodityId));

		List<GradeSet> list = gradeSetRepo.GetQueryable(GradeSet.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}
	
	public ActionResult<List<GradeSet>> GradeSets() {

		DetachedCriteria where = DetachedCriteria.forClass(GradeSet.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<GradeSet> list = gradeSetRepo.GetQueryable(GradeSet.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toCacheList();

		return new ActionResult<>(true, null, list);
	}

	public ActionResult<List<GradeSet>> BackGradeSets() {

		List<GradeSet> list = gradeSetRepo.GetQueryable(GradeSet.class).OrderBy(Order.desc("OrderIndex"))
				.toList();

		return new ActionResult<>(true, null, list);
	}

	public ActionResult<GradeSet> Save(GradeSet gradeSet) {

		try {
			// 检查重复
			DetachedCriteria where = DetachedCriteria.forClass(GradeSet.class);
			where.add(Restrictions.eq("IsHidden", false));
			where.add(Restrictions.eq("CommodityId", gradeSet.getCommodityId()));
			if (gradeSet.getId() != null) {
				where.add(Restrictions.ne("Id", gradeSet.getId()));
			}
			where.add(Restrictions.or(Restrictions.eq("Name", gradeSet.getName()),
					Restrictions.eq("Code", gradeSet.getCode())));

			List<GradeSet> list = gradeSetRepo.GetQueryable(GradeSet.class).where(where)
				 	.OrderBy(Order.desc("OrderIndex")).toList();

			if (list != null && list.size() > 0)
				throw new Exception(MessageCtrm.DuplicatedName);

			gradeSetRepo.SaveOrUpdate(gradeSet);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}

	}

	public ActionResult<String> Delete(String id) {
		try {
			gradeSetRepo.PhysicsDelete(id, GradeSet.class);
			return new ActionResult<>(true, MessageCtrm.DeleteSuccess);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}
	}

	public ActionResult<GradeSet> GetById(String id) {
		GradeSet gradeSet = gradeSetRepo.getOneById(id, GradeSet.class);
		return new ActionResult<>(true, null, gradeSet);
	}
}
