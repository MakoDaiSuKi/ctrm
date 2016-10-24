package com.smm.ctrm.bo.impl.Basis;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.CustomerService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = Logger.getLogger(CustomerServiceImpl.class);

	@Autowired
	private HibernateRepository<Customer> repository;
	
	@Autowired
	private HibernateRepository<CustomerTitle> customerTitleHibernateRepository;

	@Autowired
	private HibernateRepository<User> userHibernateRepository;

	@Override
	public ActionResult<Customer> Save(Customer customer) {

		try {

			if (customer == null)
				throw new Exception("customer is null");

			String customerId = customer.getId();

			// 检查重名
			DetachedCriteria where = DetachedCriteria.forClass(Customer.class);
			where.add(Restrictions.eq("Name", customer.getName()));
			if (customer.getId() != null) {
				where.add(Restrictions.ne("Id", customer.getId()));
			} else {
				where.add(Restrictions.isNotNull("Id"));
			}

			List<Customer> customerList = this.repository.GetQueryable(Customer.class).where(where).toList();

			if (customerList != null && customerList.size() > 0)
				throw new Exception("客户名称重复");

			if (StringUtils.isEmpty(customer.getId())) {

				// 如果是新增
				customer.setStatus(ActionStatus.Status_Draft);

				if (customer.getIsIniatiated() != null ? customer.getIsIniatiated() : false) {
					customer.setStatus(ActionStatus.Status_Agreed);
					customer.setIsApproved(true);
				}

				customerId = this.repository.SaveOrUpdateRetrunId(customer);
				CustomerTitle customerTitle = new CustomerTitle();
				customerTitle.setCustomerId(customerId);
				customerTitle.setName(customer.getName());
				customerTitle.setIsDefault(true);
				this.customerTitleHibernateRepository.SaveOrUpdate(customerTitle);

			} else {

				if (customer.getIsIniatiated() != null ? customer.getIsIniatiated() : false) {
					customer.setStatus(ActionStatus.Status_Agreed);
					customer.setIsApproved(true);
				}

				this.repository.SaveOrUpdate(customer);
			}

			customer = this.repository.getOneById(customerId, Customer.class);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess, customer);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false,e.getMessage());
		}

	}

	@Override
	public ActionResult<String> Delete(String customerId, String userId) {

		try {

			Customer customer = this.repository.getOneById(customerId, Customer.class);

			if (customer == null)
				throw new Exception("customer is null");

			if (customer.getIsIniatiated() == false && (customer.getStatus() == ActionStatus.Status_Agreed
					|| customer.getStatus() == ActionStatus.Status_Deny)) {

				throw new Exception("不可以删除已经审核的客户");

			}

			// 先删除关联的数据
			String sql = String.format("delete from [Basis].Liaison where CustomerId = '{0}'", customerId);
			this.repository.ExecuteNonQuery(sql);

			sql = String.format("delete from [Basis].CustomerBalance where CustomerId = '{0}'", customerId);
			this.repository.ExecuteNonQuery(sql);

			sql = String.format("delete from [Basis].CustomerBank where CustomerId = '{0}'", customerId);
			this.repository.ExecuteNonQuery(sql);

			sql = String.format("delete from [Basis].CustomerTitle where CustomerId = '{0}'", customerId);
			this.repository.ExecuteNonQuery(sql);

			this.repository.PhysicsDelete(customerId, Customer.class);

			return new ActionResult<>(false, "删除成功");

		} catch (Exception e) {

			logger.error(e.getMessage(), e);

			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public ActionResult<Customer> GetById(String id) {
		Customer customer = this.repository.getOneById(id, Customer.class);
		return new ActionResult<>(true, "", customer);

	}

	@Override
	public List<Customer> getListByName(String name) {

		DetachedCriteria where = DetachedCriteria.forClass(Customer.class);
		where.add(Restrictions.eq("Name", name));

		return this.repository.GetQueryable(Customer.class).where(where).toList();

	}

	@Override
	public List<Customer> Top10CustomerByProfit() {

		return null;
	}

	@Override
	public List<Customer> GetByCustomerName(String customerName) {

		DetachedCriteria where = DetachedCriteria.forClass(Customer.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("Name", customerName));

		return this.repository.GetQueryable(Customer.class).where(where).toList();
	}

	@Override
	public List<Customer> CustomersCustomersIncInternals() {

		DetachedCriteria where = DetachedCriteria.forClass(Customer.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("Status", ActionStatus.Status_Agreed));

		return this.repository.GetQueryable(Customer.class).where(where).toCacheList();
	}

	@Override
	public List<Customer> CustomersCustomersExcInternals() {

		DetachedCriteria where = DetachedCriteria.forClass(Customer.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("IsInternalCustomer", false));
		where.add(Restrictions.eq("Status", ActionStatus.Status_Agreed));

		return this.repository.GetQueryable(Customer.class).where(where).toCacheList();
	}

	@Override
	public List<Customer> InternalCustomersOnly() {

		DetachedCriteria where = DetachedCriteria.forClass(Customer.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("IsInternalCustomer", true));
		where.add(Restrictions.eq("Status", ActionStatus.Status_Agreed));

		return this.repository.GetQueryable(Customer.class).where(where).toCacheList();
	}

	@Override
	public List<Customer> ImportCustomers() {
		return null;
	}

	@Override
	public List<Customer> Top10CustomerByTurnover() {
		return null;
	}

	@Override
	public List<Customer> Customers(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy) {
		List<Customer> list = this.repository.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();

		return assemblingBeanList(list);
	}

	@Override
	public Criteria GetCriteria() {

		return this.repository.CreateCriteria(Customer.class);

	}

	/**
	 * 以下是打掉关系
	 */
	public List<Customer> assemblingBeanList(List<Customer> ct) {
		if (ct.size() == 0)
			return null;
		for (Customer user : ct) {
			assemblingBean(user);
		}
		return ct;
	}

	public void assemblingBean(Customer ct) {
		if (ct != null) {
			/**
			 * 组装User
			 */
			if (ct.getCreatedId() != null) {
				User user = this.userHibernateRepository.getOneById(ct.getCreatedId(), User.class);
				ct.setCreated(user);
			}
		}
	}
}
