package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.CustomerTitleService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class CustomerTitleServiceImpl implements CustomerTitleService {

	@Autowired
	private HibernateRepository<CustomerTitle> repository;
	
	@Autowired
	private HibernateRepository<Contract> contractRepository;
	
	

	@Autowired
	private CommonService commonService;

	@Override
	public ActionResult<CustomerTitle> Save(CustomerTitle customerTitle) {
		/**
		 * 检查重名
		 */
		DetachedCriteria dc = DetachedCriteria.forClass(CustomerTitle.class);
		dc.add(Restrictions.eq("Name", customerTitle.getName()));
		dc.add(Restrictions.eq("CustomerId", customerTitle.getCustomerId()));
		dc.add(Restrictions.neOrIsNotNull("Id", customerTitle.getId()));
		this.repository.SaveOrUpdate(customerTitle);
		return new ActionResult<>(true, MessageCtrm.SaveSuccess, customerTitle);
	}

	@Override
	public ActionResult<String> Delete(String id) {
		
		//先验证有没有关联订单
		DetachedCriteria dc=DetachedCriteria.forClass(Contract.class);
		dc.add(Restrictions.eq("CustomerTitleId", id));
		dc.setProjection(Projections.rowCount());
		long count=(Long) this.contractRepository.getHibernateTemplate().findByCriteria(dc).get(0);
		if(count>0){
			return new ActionResult<>(false, "客户抬头已经关联订单，不能删除。",ActionStatus.ERROR);
		}
		this.repository.PhysicsDelete(id, CustomerTitle.class);
		return new ActionResult<>(true, MessageCtrm.DeleteSuccess,ActionStatus.SUCCESS);
	}

	@Override
	public ActionResult<List<CustomerTitle>> CustomerTitles() {

		DetachedCriteria where = DetachedCriteria.forClass(CustomerTitle.class);
		where.add(Restrictions.eq("IsHidden", false));
		List<CustomerTitle> list = this.repository.GetQueryable(CustomerTitle.class).where(where).toCacheList();
		list = this.commonService.SimplifyDataCustomerTitleList(list);
		return new ActionResult<>(true, "", list);
	}

	@Override
	public ActionResult<CustomerTitle> GetById(String id) {

		CustomerTitle title = this.repository.getOneById(id, CustomerTitle.class);
		return new ActionResult<>(true, "", title);
	}

	@Override
	public ActionResult<List<CustomerTitle>> GetTitlesByCustomerId(String customerId) {

		DetachedCriteria where = DetachedCriteria.forClass(CustomerTitle.class);
		where.add(Restrictions.eq("CustomerId", customerId));
		List<CustomerTitle> list = this.repository.GetQueryable(CustomerTitle.class).where(where).toList();
		return new ActionResult<>(true, "", list);
	}

}
