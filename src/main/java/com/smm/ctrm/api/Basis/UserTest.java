package com.smm.ctrm.api.Basis;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.api.BaseApiController;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Physical.LC;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.dto.res.ActionResult;

@RequestMapping("api/Basis/myUser")
@Controller
public class UserTest extends BaseApiController{
	
	@Resource
	HibernateRepository<User> userRepo;
	@Resource
	HibernateRepository<LC> lcRepo;
	
	@Resource
	HibernateRepository<Storage> storageRepo;
	
	@RequestMapping("getOne")
	@ResponseBody
	public ActionResult<User> getUser(@RequestBody(required=false) String Id){
//		List<LC> lcl = lcRepo.getCurrentSession()
//				.createQuery("select t from LC t "
//						+ "left join fetch t.Invoices "
//						+ "where t.Invoices.LotId=:LotId", LC.class)
//				.setParameter("LotId", "BADC6F33-A690-4D60-B2C0-A4EB00FE32E3")
//				.getResultList();
		List<LC> lcl = lcRepo.getCurrentSession()
				.createQuery("select t from LC t left join fetch t.Invoices", LC.class)
				.getResultList();
		
		DetachedCriteria where = DetachedCriteria.forClass(LC.class);
		where.createCriteria("Invoices", "invoices", JoinType.LEFT_OUTER_JOIN, Restrictions.eqProperty("Id", "LotId"));
		where.add(Restrictions.eq("invoices.LotId", "BADC6F33-A690-4D60-B2C0-A4EB00FE32E3"));
		where.setFetchMode("Invoices", FetchMode.JOIN);
		
		List<LC> lcList = lcRepo.GetQueryable(LC.class).toList();
		return new ActionResult<>(Boolean.TRUE, "");
	}
//	public static void main(String[] args) {	
//		
//		DriverManager.getDrivers();
//	}
}
