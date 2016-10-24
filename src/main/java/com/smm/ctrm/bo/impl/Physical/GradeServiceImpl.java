package com.smm.ctrm.bo.impl.Physical;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Finance.InvoiceService;
import com.smm.ctrm.bo.Physical.GradeService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.Grade;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.Result.InvoiceType;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class GradeServiceImpl implements GradeService {

	private static final Logger logger = Logger.getLogger(GradeServiceImpl.class);

	@Autowired
	private HibernateRepository<Grade> repository;

	@Autowired
	private HibernateRepository<Lot> lotRepository;

	@Autowired
	private HibernateRepository<Invoice> invoiceRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	private InvoiceService invoiceService;

	@Override
	public void GetCriteria() {

		this.repository.CreateCriteria(Grade.class);

	}

	@Override
	public ActionResult<Grade> Save(Grade grade) {

		// 检查重复
		DetachedCriteria where = DetachedCriteria.forClass(Grade.class);
		where.add(Restrictions.eqOrIsNull("SpecId", grade.getSpecId()));
		where.add(Restrictions.eqOrIsNull("Discharging", grade.getDischarging()));
		where.add(Restrictions.eqOrIsNull("ContractId", grade.getContractId()));
		if (StringUtils.isNotBlank(grade.getId())) {
			where.add(Restrictions.neOrIsNotNull("Id", grade.getId()));
		}
		Grade exist = this.repository.GetQueryable(Grade.class).where(where).firstOrDefault();
		if (exist != null)
			return new ActionResult<>(Boolean.FALSE, "该品级和到达地点已经存在");
		// 批量更新
		where = DetachedCriteria.forClass(Lot.class);
		where.add(Restrictions.eq("ContractId", grade.getContractId()));
		List<Lot> lotList = this.lotRepository.GetQueryable(Lot.class).where(where).toList();
		if (StringUtils.isNotBlank(grade.getId())) {
			for (Lot lot : lotList) {
				// 获取 Invoice
				where = DetachedCriteria.forClass(Invoice.class);
				where.add(Restrictions.eq("LotId", lot.getId()));
				where.add(Restrictions.or(Restrictions.eq("PFA", ActionStatus.InvoiceType_Adjust),
						Restrictions.eq("PFA", ActionStatus.InvoiceType_Final)));
				List<Invoice> invoiceList = this.invoiceRepository.GetQueryable(Invoice.class).where(where).toList();
				for (Invoice i : invoiceList) {
					i.setStorages(invoiceService.getStorageListByInvoiceId(i.getId()));
					if (i.getStorages() == null || i.getStorages().size() == 0)
						continue;
					List<Storage> storageList = i.getStorages().stream()
							.filter(storage -> storage.getSpecId()!=null&&storage.getSpecId().equalsIgnoreCase(grade.getSpecId()))
							.collect(Collectors.toList());
					if (storageList != null && storageList.size() > 0) {
						return new ActionResult<>(Boolean.FALSE, "该品级的交付明细已关联调整或正式发票，不允许修改！");
					}
				}
			}
		}
		this.repository.SaveOrUpdate(grade);
		lotList.forEach(lot -> this.commonService.UpdateLotPriceByLotId(lot.getId()));
		return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess, grade);
	}

	@Override
	public ActionResult<String> Delete(String id) {
			if (id == null) {
				ActionResult<String> tempVar = new ActionResult<String>();
				tempVar.setSuccess(false);
				tempVar.setMessage("参数错误");
				return tempVar;
			}
			Grade grade = this.repository.getOneById(id, Grade.class);

			DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
			where.add(Restrictions.eq("ContractId", grade.getContractId()));
			List<Lot> lots = this.lotRepository.GetQueryable(Lot.class).where(where).toList();

			if (grade.getId() != null) {

				for (Lot lot : lots) {
					DetachedCriteria where2 = DetachedCriteria.forClass(Invoice.class);
					where2.add(Restrictions.eq("LotId", lot.getId()));
					where2.add(Restrictions.or(Restrictions.eq("PFA", InvoiceType.Adjust),
							Restrictions.eq("PFA", InvoiceType.Final)));

					List<Invoice> invoices = this.invoiceRepository.GetQueryable(Invoice.class).where(where2).toList();
					if (invoices.size() == 0) {
						continue;
					}
					for (Invoice i : invoices) {
						if (i.getStorages() == null) {
							continue;
						}

						/**
						 * i.getStorages()是关联属性.如果属性改为懒加载，应该相应改成直接查数据库
						 */
						
						
						Long exist1 = i.getStorages().stream().filter(s ->s.getSpecId()!=null&&s.getSpecId().equalsIgnoreCase(grade.getSpecId()))
								.count();
//						boolean exist1=false;
//							for (int j = 0; j < i.getStorages().size(); j++) {
//								Storage s=i.getStorages().get(j);
//								if(s.getSpecId()!=null&&s.getSpecId().equalsIgnoreCase(grade.getSpecId())){
//									exist1=true;
//									break;
//								}
//							}
						
						if (exist1>0) {
							ActionResult<String> tempVar2 = new ActionResult<String>();
							tempVar2.setSuccess(false);
							tempVar2.setMessage("该品级的交付明细已关联调整或正式发票，不允许删除！");
							return tempVar2;
						}
					}

				}
			}
			this.repository.PhysicsDelete(id, Grade.class);
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(true);
			tempVar3.setMessage(MessageCtrm.DeleteSuccess);
			return tempVar3;
		
	}

	@Override
	public ActionResult<Grade> GetById(String id) {

		ActionResult<Grade> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(this.commonService.SimplifyData(this.repository.getOneById(id, Grade.class)));

		return result;
	}

	@Override
	public ActionResult<List<Grade>> GradesByContractId(String contractId) {

		DetachedCriteria where = DetachedCriteria.forClass(Grade.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("ContractId", contractId));

		List<Grade> list = this.repository.GetQueryable(Grade.class).where(where).toList();

		list = this.commonService.SimplifyDataGradeList(list);

		ActionResult<List<Grade>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	public ActionResult<List<Grade>> GradesByInvoiceId(String invoiceId) {

		ActionResult<List<Grade>> result = new ActionResult<>();

		// get Invoice
		Invoice invoice = this.invoiceRepository.getOneById(invoiceId, Invoice.class);

		if (invoice == null) {

			result.setSuccess(false);
			result.setData(null);
			result.setMessage("Invoice is null");

			return result;
		}

		// get Grade
		DetachedCriteria where = DetachedCriteria.forClass(Grade.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("ContractId", invoice.getContractId()));

		List<Grade> list = this.repository.GetQueryable(Grade.class).where(where).toList();

		list = this.commonService.SimplifyDataGradeList(list);

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	public List<Grade> Grades() {

		return this.repository.GetList(Grade.class);
	}
}
