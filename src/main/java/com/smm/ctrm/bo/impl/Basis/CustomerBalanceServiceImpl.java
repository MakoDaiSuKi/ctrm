package com.smm.ctrm.bo.impl.Basis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.CustomerBalanceService;
import com.smm.ctrm.bo.Basis.CustomerService;
import com.smm.ctrm.bo.Basis.CustomerTitleService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.CustomerBalance;
import com.smm.ctrm.domain.Basis.CustomerBalance_new;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.domain.Basis.Dictionary;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.CustomerBalance2;
import com.smm.ctrm.domain.Physical.CustomerBalanceDetail;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Pricing;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.apiClient.CustomerBalanceDetailParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.JSONUtil;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.MajorType;
import com.smm.ctrm.util.Result.SpotType;
import com.smm.ctrm.util.Result.Status;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class CustomerBalanceServiceImpl implements CustomerBalanceService {


    private static final Logger logger=Logger.getLogger(CustomerBalanceServiceImpl.class);

    @Autowired
    private HibernateRepository<CustomerBalance> repository;


    @Autowired
    private HibernateRepository<Customer> customerHibernateRepository;

    @Autowired
    private HibernateRepository<CustomerTitle> customerTitleHibernateRepository;

    @Autowired
    private HibernateRepository<Invoice> invoiceHibernateRepository;


    @Autowired
    private HibernateRepository<Fund> fundHibernateRepository;


    @Autowired
    private HibernateRepository<Lot> lotHibernateRepository;


    @Autowired
    private HibernateRepository<Dictionary> dictionaryHibernateRepository;

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private CustomerTitleService customerTitleService;
    
    @Autowired
    private HibernateRepository<Legal> legalHibernateRepository;
    
    @Autowired
    private HibernateRepository<Commodity> commodityHibernateRepository;
    
    @Autowired
    private HibernateRepository<Contract> contractHibernateRepository;
    
    @Autowired
    private HibernateRepository<CustomerBalance_new> cusBalanceRepository;
    
    @Autowired
    private HibernateRepository<ReceiptShip> reShipRepository;
    
    @Autowired
    private HibernateRepository<Pricing> pricingRepository;
    
    @Autowired
    private HibernateRepository<Storage> storageRepository;
    
    @Override
    public ActionResult<CustomerBalance_new> Save(CustomerBalance_new customerBalance) {
    	try
        {
            if (customerBalance == null )
                return new ActionResult<CustomerBalance_new>(false, "Param is missing");
            //1、判断客户名称是否存在，如果不存在则创建
            if(customerBalance.getCustomer() != null && customerBalance.getCustomer().getName() != null){
            	DetachedCriteria customerWhere=DetachedCriteria.forClass(Customer.class);
                customerWhere.add(Restrictions.eq("Name", customerBalance.getCustomer().getName()));
                
                List<Customer> customers = this.customerHibernateRepository.GetQueryable(Customer.class).where(customerWhere).toList();
                ActionResult<Customer> retCustomer = new ActionResult<Customer>();
                if(customers == null || customers.size() <= 0){
                	//创建新客户
                	Customer customer = new Customer();
                	customer.setName(customerBalance.getCustomer().getName());
                	customer.setIsIniatiated(true);
                	customer.setCreatedBy("Import");
                	customer.setCreatedAt(new Date());
                	retCustomer = customerService.Save(customer);
                	if(retCustomer.isSuccess() == true && retCustomer.getData() != null){
                		customerBalance.setCustomerId(retCustomer.getData().getId());
                	}
                	else{
                		return new ActionResult<CustomerBalance_new>(false,"创建客户失败");
                	}
                }else{
                	customerBalance.setCustomerId(customers.get(0).getId() );
                }
//                if(customerBalance.getCustomer().getLiaison() != null){
//                	//2、判断抬头是否存在，如果不存在则创建
//                    DetachedCriteria customerTitleWhere=DetachedCriteria.forClass(CustomerTitle.class);
//                    customerTitleWhere.add(Restrictions.eq("Name", customerBalance.getCustomer().getLiaison()));
//                    customerTitleWhere.add(Restrictions.eq("CustomerId", customerBalance.getCustomerId()));
//                    
//                    List<CustomerTitle> customerTitles = this.customerTitleHibernateRepository.GetQueryable(CustomerTitle.class).where(customerTitleWhere).toList();
//                    ActionResult<CustomerTitle> retCustomerTitle = new ActionResult<CustomerTitle>();
//                    if(customerTitles == null || customerTitles.size() <= 0){
//                    	//创建新客户抬头
//                    	CustomerTitle customerTitle = new CustomerTitle();
//                    	customerTitle.setName(customerBalance.getCustomer().getName());
//                    	customerTitle.setCustomerId(customerBalance.getCustomerId());
//                    	customerTitle.setCreatedBy("Import");
//                    	customerTitle.setCreatedAt(new Date());
//                    	retCustomerTitle = customerTitleService.Save(customerTitle);
//                    	if(retCustomerTitle.isSuccess() == true && retCustomerTitle.getData() != null){
//                    		customerBalance.setLegalId(retCustomerTitle.getData().getId() );
//                    	}
//                    	else{
//                    		return new ActionResult<CustomerBalance_new>(false,"创建客户台头失败");
//                    	}
//                    }else{
//                    	customerBalance.setLegalId(customerTitles.get(0).getId() );
//                    }
//                }
            }
            
            //3、根据客户、台头、币种 判断customerBalance是否存在，不存在则创建，存在则更新出事金额
            if(customerBalance.getCustomerId() != null &&customerBalance.getLegalId()!=null && customerBalance.getCurrency() != null){
            	DetachedCriteria customerBalanceWhere=DetachedCriteria.forClass(CustomerBalance_new.class);
                customerBalanceWhere.add(Restrictions.eq("CustomerId", customerBalance.getCustomerId()));
                customerBalanceWhere.add(Restrictions.eq("LegalId", customerBalance.getLegalId()));
                customerBalanceWhere.add(Restrictions.eq("Currency", customerBalance.getCurrency()));
                List<CustomerBalance_new> customerBalances = this.cusBalanceRepository.GetQueryable(CustomerBalance_new.class).where(customerBalanceWhere).toList();
                if(customerBalances == null || customerBalances.size() <= 0){
                	this.cusBalanceRepository.SaveOrUpdate(customerBalance);
                }else{
                	customerBalances.get(0).setInitBalance(customerBalance.getInitBalance());
                	customerBalance = customerBalances.get(0);
                	this.cusBalanceRepository.SaveOrUpdate(customerBalance);
                }
            }else{
            	return new ActionResult<CustomerBalance_new>(true,"信息不全");
            }
            

            return new ActionResult<CustomerBalance_new>(true,MessageCtrm.SaveSuccess,customerBalance);
        }
        catch (Exception ex)
        {
            return new ActionResult<CustomerBalance_new>(false,ex.getMessage());
        }
    	
    }

    @Override
    public ActionResult<String> AmendInitBalance(CustomerBalance customerBalance) {

    	try
        {
            if (customerBalance == null || customerBalance.getId()==null)
                return new ActionResult<String>(false, "Param is missing: CustomerBalance.Id");

            CustomerBalance exist=this.repository.getOneById(customerBalance.getId(), CustomerBalance.class);
            if (exist == null)
                return new ActionResult<String>(false, "CustomerBalance is null, might be deleted already.");

            exist.setLastBalance((exist.getLastBalance()!=null?exist.getLastBalance():new BigDecimal(0))
            					.subtract(exist.getInitBalance()!=null?exist.getInitBalance():new BigDecimal(0))
            					.add(customerBalance.getInitBalance()!=null?customerBalance.getInitBalance(): new BigDecimal(0)));
            exist.setInitBalance(customerBalance.getInitBalance()!=null?customerBalance.getInitBalance(): new BigDecimal(0));
            
            this.repository.SaveOrUpdate(exist);

            return new ActionResult<String>(true,MessageCtrm.SaveSuccess);
        }
        catch (Exception ex)
        {
            return new ActionResult<String>(false,ex.getMessage());
        }
    	
    }

    @Override
    public ActionResult<String> Delete(String id) {
        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(id, CustomerBalance.class);

            result.setSuccess(true);
            result.setMessage(MessageCtrm.DeleteSuccess);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

    @Override
    public ActionResult<String> DeleteCustomerBalances(List<CustomerBalance> customerBalances) {

        ActionResult<String> result= new ActionResult<>();

        try{

            for(CustomerBalance balance:customerBalances){

                this.repository.PhysicsDelete(balance.getId(), CustomerBalance.class);
            }

            result.setSuccess(true);
            result.setMessage(MessageCtrm.DeleteSuccess);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

    @Override
    public ActionResult<List<CustomerBalance>> CustomerBalances() {

        DetachedCriteria where=DetachedCriteria.forClass(CustomerBank.class);
        where.add(Restrictions.eq("IsHidden", false));

        List<CustomerBalance> list=this.repository.GetQueryable(CustomerBalance.class).where(where).toList();

        ActionResult<List<CustomerBalance>> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(list);

        return result;
    }

    @Override
    public ActionResult<CustomerBalance> GetById(String id) {

        ActionResult<CustomerBalance> result= new ActionResult<>();

        try{

            CustomerBalance balance=this.repository.getOneById(id, CustomerBalance.class);

            result.setSuccess(true);
            result.setData(balance);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

    @Override
    public ActionResult<String> ImportInitCustomerBalance(List<CustomerBalance> customerBalances) {


        try{

            if(customerBalances==null || customerBalances.size()==0) throw new Exception("customer Balances is null");


            for(CustomerBalance balance:customerBalances){

                Customer customer=new Customer();

                BeanUtils.copyProperties(customer,balance.getCustomer());

                customer.setCreatedAt(new Date());
                customer.setCreatedBy("Imported");
                customer.setStatus(Customer.Status_Agreed);
                customer.setIsApproved(true);

                List<Customer> cusList=this.customerService.getListByName(customer.getName());

                Customer exist = null;

                if(cusList!=null && cusList.size()>0) exist=cusList.get(0);

                if (exist != null)
                {
                    balance.setCustomerId(exist.getId());
                }
                else
                {
                    balance.setCustomerId(this.customerHibernateRepository.SaveOrUpdateRetrunId(customer));

                    CustomerTitle customerTitle = new CustomerTitle();
                    customerTitle.setCustomerId(balance.getCustomerId());
                    customerTitle.setCreatedAt(new Date());
                    customerTitle.setIsDefault(true);
                    this.customerTitleHibernateRepository.SaveOrUpdate(customerTitle);
                }
            }


            //-----------------------------

            for (CustomerBalance customerBalance : customerBalances)
            {
                //#region 保存各项余额，按 客户 + 内部台头 + 品种 的联合唯一

                CustomerBalance cb = new CustomerBalance();

                cb.setLegalId(customerBalance.getLegalId());
                cb.setCommodityId(customerBalance.getCommodityId());
                cb.setInitBalance(customerBalance.getInitBalance());
                cb.setCustomerId(customerBalance.getCustomerId());
                cb.setCurrency(customerBalance.getCurrency());

                DetachedCriteria where=DetachedCriteria.forClass(CustomerBalance.class);
                where.add(Restrictions.eq("LegalId", cb.getLegalId()));
                where.add(Restrictions.eq("CustomerId", cb.getCustomerId()));
                where.add(Restrictions.eq("CommodityId", cb.getCommodityId()));
                List<CustomerBalance> customerBalanceList=this.repository.GetQueryable(CustomerBalance.class).where(where).toList();

                CustomerBalance exist=null;

                if(customerBalanceList!=null && customerBalanceList.size()>0) exist=customerBalanceList.get(0);

                if (exist != null)
                {
                    exist.setUpdatedAt(new Date());
                    exist.setLastBalance(exist.getLastBalance().subtract(exist.getInitBalance()).add(cb.getInitBalance()));
                    exist.setInitBalance(cb.getInitBalance());
                    this.repository.SaveOrUpdate(exist);
                }
                else
                {
                    cb.setCreatedAt(new Date());
                    this.repository.SaveOrUpdate(cb);
                }
            }

            return new ActionResult<>(true,"导入成功");

        }catch (Exception e){

            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }

    }

    @Override
    public ActionResult<String> Reconciliation() {

        ActionResult<String> result= new ActionResult<>();

        result.setSuccess(false);

        result.setMessage("代码未加入");

        return result;

    }

    @Override
    public CustomerBalance2 PagerDetail2(CustomerBalanceDetailParams param) {

        Customer customer=this.customerHibernateRepository.getOneById(param.getCustomerId(),Customer.class);

        /**
         * 数据权限
         */
        if(param.getPermissionUserId()!=null&&param.getPermissionUserId().size()>0){
        	String permission=param.getPermissionUserId().stream().filter(p->p.equals(customer.getCreatedId())).findFirst().orElse(null);
        	if(StringUtils.isBlank(permission)){
        		return new CustomerBalance2();
        	}
        }
        
        if(customer==null) return new CustomerBalance2();

        List<CustomerBalanceDetail> customerBalanceDetailList=new ArrayList<>();

        BigDecimal sumAmount = BigDecimal.ZERO;
        BigDecimal sumFundAmount = BigDecimal.ZERO;
        BigDecimal sumDiffAmount = BigDecimal.ZERO;

        
        //本年
        DetachedCriteria where=DetachedCriteria.forClass(Invoice.class);
        where.add(Restrictions.eq("LegalId", param.getLegalId()));
        where.add(Restrictions.eq("CustomerId", param.getCustomerId()));
        where.add(Restrictions.eq("FeeCode", ActionStatus.FeeCode_Goods));
        if(param.getEndDate()!=null){
        	where.add(Restrictions.le("TradeDate", param.getEndDate()));
        }
        List<Invoice> yearInvoices = this.invoiceHibernateRepository.GetQueryable(Invoice.class).where(where).toList();
        
        for (Invoice i : yearInvoices){
        	
            BigDecimal tempFundAmount = BigDecimal.ZERO;

            where=DetachedCriteria.forClass(Fund.class);
            where.add(Restrictions.eq("InvoiceId", i.getId()));
            List<Fund> funds = this.fundHibernateRepository.GetQueryable(Fund.class).where(where).OrderBy(Order.desc("TradeDate")).toList();

            if (funds.size() >= 2)
            {
                sumFundAmount = sumFundAmount.add(funds.get(0).getAmount()).add(funds.get(1).getAmount());
                tempFundAmount = funds.get(0).getAmount().add(funds.get(1).getAmount());

            }
            else if (funds.size() == 1)
            {
                sumFundAmount = sumFundAmount.add(funds.get(0).getAmount());
                tempFundAmount = funds.get(0).getAmount();
            }

            sumAmount = sumAmount.add(i.getAmount()) ;
            if (i.getAmount().compareTo(BigDecimal.ZERO)>= 0)
            {
                sumDiffAmount = sumDiffAmount.add(i.getAmount().subtract(tempFundAmount));
            }
            else
            {

                sumDiffAmount = sumDiffAmount.add(i.getAmount().add(tempFundAmount));
            }

        }
        CustomerBalance2 customerBalance = new CustomerBalance2();
        customerBalance.setYearInvoiceAmount(sumAmount);
        customerBalance.setYearFundAmount(sumFundAmount);
        customerBalance.setYearDiffAmount(sumDiffAmount);

        List<String> yearInvoicesOfA = yearInvoices.stream().filter(i -> ActionStatus.InvoiceType_Adjust.equals(i.getPFA())).map(Invoice::getId).collect(Collectors.toList());

        for(Invoice invoice:yearInvoices){

            if(yearInvoicesOfA.contains(invoice.getAdjustId())){

            	yearInvoices.remove(invoice);
            }
        }

        //求和
        BigDecimal _YearInvoiceQutnatiy=BigDecimal.ZERO;
        for (Invoice i : yearInvoices) {
        	_YearInvoiceQutnatiy=_YearInvoiceQutnatiy.add(i.getQuantity());
		}
        customerBalance.setYearInvoiceQutnatiy(_YearInvoiceQutnatiy);

        //----------------

        //期初--------------------
        sumAmount = BigDecimal.ZERO;
        sumFundAmount = BigDecimal.ZERO;
        sumDiffAmount = BigDecimal.ZERO;

        where=DetachedCriteria.forClass(Invoice.class);
        where.add(Restrictions.eq("LegalId", param.getLegalId()));
        where.add(Restrictions.eq("CustomerId", param.getCustomerId()));
        where.add(Restrictions.eq("FeeCode", ActionStatus.FeeCode_Goods));
        if(param.getStartDate()!=null){
        	where.add(Restrictions.lt("TradeDate", param.getStartDate()));
        }

        List<Invoice> initInvoices = this.invoiceHibernateRepository.GetQueryable(Invoice.class).where(where).toList();

        for (Invoice i : initInvoices)
        {
            BigDecimal tempFundAmount = BigDecimal.ZERO;


            where=DetachedCriteria.forClass(Fund.class);
            where.add(Restrictions.eq("InvoiceId", i.getId()));
            List<Fund> funds = this.fundHibernateRepository.GetQueryable(Fund.class).where(where).OrderBy(Order.desc("TradeDate")).toList();

            if (funds.size() >= 2)
            {
                sumFundAmount = sumFundAmount.add(funds.get(0).getAmount()).add(funds.get(1).getAmount());
                tempFundAmount = funds.get(0).getAmount().add(funds.get(1).getAmount());

            }
            else if (funds.size() == 1)
            {
                sumFundAmount = sumFundAmount.add(funds.get(0).getAmount());
                tempFundAmount = funds.get(0).getAmount();
            }

            sumAmount = sumAmount.add(i.getAmount()) ;
            if (i.getAmount().compareTo(BigDecimal.ZERO)>= 0)
            {
                sumDiffAmount = sumDiffAmount.add(i.getAmount().subtract(tempFundAmount));
            }
            else
            {

                sumDiffAmount = sumDiffAmount.add(i.getAmount().add(tempFundAmount));
            }

        }

        customerBalance.setInitInvoiceAmount(sumAmount);
        customerBalance.setInitFundAmount(sumFundAmount);
        customerBalance.setInitDiffAmount(sumDiffAmount);

        List<String> initInvoicesOfA = initInvoices.stream().filter(i -> i.getPFA().equals(ActionStatus.InvoiceType_Adjust)).map(Invoice::getId).collect(Collectors.toList());

        for(Invoice invoice:initInvoices){

            if(initInvoicesOfA.contains(invoice.getAdjustId())){

            	initInvoices.remove(invoice);
            }
        }

        //求和
        BigDecimal _initInvoiceQutnatiy=BigDecimal.ZERO;
        
        for (Invoice invoice : initInvoices) {
        	_initInvoiceQutnatiy=_initInvoiceQutnatiy.add(invoice.getQuantity());
		}
        
        customerBalance.setInitInvoiceQutnatiy(_initInvoiceQutnatiy);

        //本期-----------------------------

        where=DetachedCriteria.forClass(Lot.class);
        where.add(Restrictions.eq("LegalId", param.getLegalId()));
        where.add(Restrictions.eq("CustomerId", customer.getId()));

        //List<Lot> lots = this.lotHibernateRepository.GetQueryable(Lot.class).where(where).toList();


        where=DetachedCriteria.forClass(Invoice.class);
        where.add(Restrictions.eq("LegalId", param.getLegalId()));
        where.add(Restrictions.eq("CustomerId", customer.getId()));
        where.add(Restrictions.eq("FeeCode", ActionStatus.FeeCode_Goods));
        if(param.getStartDate()!=null){
        	where.add(Restrictions.ge("TradeDate", param.getStartDate()));
        }
        if(param.getEndDate()!=null){
        	where.add(Restrictions.le("TradeDate", param.getEndDate()));
        }
        

        List<Invoice> invoices = this.invoiceHibernateRepository.GetQueryable(Invoice.class).where(where).toList();
        invoices=assemblingInvoice(invoices);
        for (Invoice i : invoices)
        {
            CustomerBalanceDetail customerBalanceDetail = new CustomerBalanceDetail();

            where=DetachedCriteria.forClass(Fund.class);
            where.add(Restrictions.eq("InvoiceId", i.getId()));
            List<Fund> funds = this.fundHibernateRepository.GetQueryable(Fund.class).where(where).OrderBy(Order.desc("TradeDate")).toList();
            customerBalanceDetail.setContractNo(i.getContract().getHeadNo());
            customerBalanceDetail.setLotNo(i.getLot().getFullNo());
            customerBalanceDetail.setInvoiceNo(i.getInvoiceNo());
            customerBalanceDetail.setInvoiceDate(i.getTradeDate());
            customerBalanceDetail.setBrand(i.getLot().getBrandNames());
            customerBalanceDetail.setInvoiceQuantity(i.getQuantity());
            customerBalanceDetail.setInvoicePrice(i.getPrice());
            customerBalanceDetail.setInovicePremium(i.getPrice().subtract(i.getMajor()));
            customerBalanceDetail.setInvoiceMajor(i.getMajor());
            customerBalanceDetail.setInvoiceAmount(i.getAmount());

            if (funds.size() >= 2)
            {
                customerBalanceDetail.setFundAmount1(funds.get(0).getAmount());
                customerBalanceDetail.setFundDate1(funds.get(0).getTradeDate());
                customerBalanceDetail.setFundAmount2(funds.get(1).getAmount());
                customerBalanceDetail.setFundDate2(funds.get(1).getTradeDate());
            }
            else if (funds.size() == 1)
            {
                customerBalanceDetail.setFundAmount1(funds.get(0).getAmount());
                customerBalanceDetail.setFundDate1(funds.get(0).getTradeDate());
            }


            customerBalanceDetail.setInvoiceAmount(customerBalanceDetail.getInvoiceAmount());

            if (i.getAmount().compareTo(BigDecimal.ZERO) >= 0)
            {
                customerBalanceDetail.setDiffAmount(i.getAmount().subtract(customerBalanceDetail.getFundAmount1()).subtract(customerBalanceDetail.getFundAmount2()));
            }
            else
            {
                customerBalanceDetail.setDiffAmount(i.getAmount().add(customerBalanceDetail.getFundAmount1()).add(customerBalanceDetail.getFundAmount2()));
            }

            customerBalanceDetailList.add(customerBalanceDetail);

        }

        customerBalance.setCustomerBalanceDetails(customerBalanceDetailList);


        List<String> invoicesOfA = invoices.stream().filter(i -> ActionStatus.InvoiceType_Adjust.equals(i.getPFA())).map(Invoice::getId).collect(Collectors.toList());

        for(Invoice invoice:invoices){

            if(invoicesOfA.contains(invoice.getAdjustId())){

                invoices.remove(invoice);
            }
        }

        List<Invoice> initInvoicesOfP = new ArrayList<>();

        for(Invoice invoice:initInvoices){

            if(invoicesOfA.contains(invoice.getAdjustId())){

                initInvoicesOfP.add(invoice);
            }
        }


        BigDecimal sum_invoices=BigDecimal.ZERO;
        BigDecimal sum_initInvoicesOfP=BigDecimal.ZERO;
        BigDecimal sum_customerBalanceDetailList=BigDecimal.ZERO;
        BigDecimal sum_customerBalanceDetailList_1=BigDecimal.ZERO;
        BigDecimal sum_customerBalanceDetailList_2=BigDecimal.ZERO;

        for (Invoice invoice : invoices) {
        	sum_invoices=sum_invoices.add(invoice.getQuantity());
		}
        for (Invoice i : initInvoicesOfP) {
        	sum_initInvoicesOfP=sum_initInvoicesOfP.add(i.getQuantity());
		}
        for (CustomerBalanceDetail c : customerBalanceDetailList) {
        	sum_customerBalanceDetailList=sum_customerBalanceDetailList.add(c.getInvoiceAmount());
        	sum_customerBalanceDetailList_1=sum_customerBalanceDetailList_1.add(c.getFundAmount1().add(c.getFundAmount2()));
        	sum_customerBalanceDetailList_2=sum_customerBalanceDetailList_2.add(c.getDiffAmount());
		}
        customerBalance.setInvoiceQutnatiy(sum_invoices.subtract(sum_initInvoicesOfP));;
        customerBalance.setInvoiceAmount(sum_customerBalanceDetailList);
        customerBalance.setFundAmount(sum_customerBalanceDetailList_1);
        customerBalance.setDiffAmount(sum_customerBalanceDetailList_2);


        return customerBalance;
    }

    @Override
	public void ScheduledUpdateCustomerBalance() {

        List<Lot> lots = this.lotHibernateRepository.GetList(Lot.class);

        for (Lot lot : lots)
        {
            if (lot.getCustomerId() == null || lot.getLegalId() == null || lot.getCommodityId() == null) continue;

            UpdateCustomerBalance(lot.getCustomerId(), lot.getLegalId(), lot.getCommodityId());
        }


        DetachedCriteria where=DetachedCriteria.forClass(Fund.class);
        where.add(Restrictions.or(
                Restrictions.eq("DC",ActionStatus.DC_Credit),
                Restrictions.and(
                        Restrictions.eq("DC",ActionStatus.DC_Debit),
                        Restrictions.eq("IsExecuted",true)
                )
        ));
        List<Fund> funds =this.fundHibernateRepository.GetQueryable(Fund.class).where(where).toList();

        for (Fund fund : funds)
        {
            if (fund.getCustomerId() == null || fund.getLegalId() == null || fund.getCommodityId() == null) continue;
            UpdateCustomerBalance(fund.getCustomerId(), fund.getLegalId(), fund.getCommodityId());
        }
		
	}

	@Override
	public void UpdateCustomerBalance(String legalId, String customerId, String commodityId) {

        try{

            //因为需要引入多币种，因此，先取得当前在用的币种(Currency)的列表。接着，分别按币种进行统计。

            DetachedCriteria where=DetachedCriteria.forClass(Dictionary.class);
            where.add(Restrictions.eq("Code", "Currency"));
            where.add(Restrictions.eq("IsHidden", false));
            where.add(Restrictions.isNotNull("ParentId"));
            List<Dictionary> currencies=this.dictionaryHibernateRepository.GetQueryable(Dictionary.class).where(where).toList();

            if (currencies==null || currencies.size() == 0) return; //避免初始化时间没有币种、运行出错，加上这个检验。

            for (Dictionary currency : currencies)
            {
                // 1. 获取批次列表，并取得dueBalanceSum = sum(lot.dueBalance)

                where=DetachedCriteria.forClass(Lot.class);
                where.createAlias("Contract", "contract",JoinType.LEFT_OUTER_JOIN);
                where.add(Restrictions.eq("contract.CustomerId", customerId));
                where.add(Restrictions.eq("contract.LegalId", legalId));
                where.add(Restrictions.eq("contract.CommodityId", commodityId));
                where.add(Restrictions.eq("contract.Currency", currency));
                List<Lot> lotList=this.lotHibernateRepository.GetQueryable(Lot.class).where(where).toList();

                BigDecimal dueBalance=BigDecimal.ZERO;

                if(lotList!=null && lotList.size()>0){
                    lotList.forEach(lot -> dueBalance.add(lot.getDueBalance()));
                }



                // 2. 获取资金流水列表，并取得 amountSum = sum(fund.amount)
                where=DetachedCriteria.forClass(Lot.class);
                where.add(Restrictions.eq("CustomerId", customerId));
                where.add(Restrictions.eq("LegalId", legalId));
                where.add(Restrictions.eq("CommodityId", commodityId));
                where.add(Restrictions.eq("Currency", currency));
                where.add(Restrictions.eq("IsExecuted", true));
                List<Fund> lotList_1=this.fundHibernateRepository.GetQueryable(Fund.class).where(where).toList();

                BigDecimal paidBalance=BigDecimal.ZERO;

                if(lotList!=null && lotList.size()>0){
                    lotList_1.forEach(lot -> paidBalance.add(lot.getAmount()));
                }

                // 3. 保存：检查CustomerBalance表，是否已经存在相同的partnerId + legalId + commodityId的记录
                where=DetachedCriteria.forClass(CustomerBalance.class);
                where.add(Restrictions.eq("CustomerId", customerId));
                where.add(Restrictions.eq("LegalId", legalId));
                where.add(Restrictions.eq("CommodityId", commodityId));
                where.add(Restrictions.eq("Currency", currency));
                List<CustomerBalance> customerBalanceList=this.repository.GetQueryable(CustomerBalance.class).where(where).toList();

                CustomerBalance customerBalance=null;

                if(customerBalanceList!=null && customerBalanceList.size()>0) customerBalance=customerBalanceList.get(0);

                if (customerBalance == null)
                {
                    customerBalance = new CustomerBalance();
                    customerBalance.setDueBalance(dueBalance);
                    customerBalance.setPaidBalance(paidBalance);
                    customerBalance.setLastBalance(dueBalance.subtract(paidBalance));
                    customerBalance.setCustomerId(customerId);
                    customerBalance.setLegalId(legalId);
                    customerBalance.setCommodityId(commodityId);
                    customerBalance.setInitBalance(BigDecimal.ZERO);
                    customerBalance.setCurrency(currency.toString());
                    customerBalance.setCreatedAt(new Date());
                    customerBalance.setCreatedBy("Robot");
                }
                else
                {
                    customerBalance.setDueBalance(dueBalance);
                    customerBalance.setPaidBalance(paidBalance);
                    customerBalance.setLastBalance(dueBalance.subtract(paidBalance));

                    //避免潜在的脏数据
                    if (customerBalance.getCreatedAt().equals(new Date())) customerBalance.setCreatedAt(new Date());

                    customerBalance.setUpdatedAt(new Date());
                    customerBalance.setUpdatedBy("Robot");
                }

                this.repository.SaveOrUpdate(customerBalance);
            }

        }catch (Exception e){

            logger.error(e);
        }
		
	}

	@Override
	public Criteria GetCriteria() {

        return this.repository.CreateCriteria(CustomerBalance.class);
		
	}

	@Override
	public List<CustomerBalance> PagerDetail(Criteria criteria, int pageSize, int pageIndex, String sortBy,
			String orderBy, RefUtil total) {
		
		List<CustomerBalance> cbList=this.repository.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();
		//cbList=assembling(cbList);
		return cbList;
	}
	
	public List<CustomerBalance> assembling(List<CustomerBalance> cbList) {
		for (CustomerBalance customerBalance : cbList) {
//			if(customerBalance.getCustomerId()!=null){
//				Customer cb=this.customerHibernateRepository.getOneById(customerBalance.getCustomerId(), Customer.class);
//				customerBalance.setCustomer(cb);
//			}
			if(customerBalance.getLegalId()!=null){
				Legal legal=this.legalHibernateRepository.getOneById(customerBalance.getLegalId(), Legal.class);
				customerBalance.setLegal(legal);
			}
			if(customerBalance.getCommodityId()!=null){
				Commodity commodity=this.commodityHibernateRepository.getOneById(customerBalance.getCommodityId(), Commodity.class);
				customerBalance.setCommodity(commodity);
			}
			
		}
		return cbList;
	}
	public CustomerBalance assemblingBean(CustomerBalance customerBalance) {
//		if(customerBalance.getCustomerId()!=null){
//			Customer cb=this.customerHibernateRepository.getOneById(customerBalance.getCustomerId(), Customer.class);
//			customerBalance.setCustomer(cb);
//		}
		if(customerBalance.getLegalId()!=null){
			Legal legal=this.legalHibernateRepository.getOneById(customerBalance.getLegalId(), Legal.class);
			customerBalance.setLegal(legal);
		}
		if(customerBalance.getCommodityId()!=null){
			Commodity commodity=this.commodityHibernateRepository.getOneById(customerBalance.getCommodityId(), Commodity.class);
			customerBalance.setCommodity(commodity);
		}
		return customerBalance;
	}
	
    public List<String> GetCustomersIdByCreatedId(List<String> createdId){
        List<String> customersId = new ArrayList<String>();
        
        DetachedCriteria dc=DetachedCriteria.forClass(Customer.class);
        dc.add(Restrictions.eq("IsHidden", false));
        dc.add(Restrictions.eq("Status", Status.Agreed));
        
        List<Customer> customerList=this.customerHibernateRepository.GetQueryable(Customer.class).where(dc).toList();
        
        for (Customer customer : customerList) {
			for (String cId : createdId) {
				if(cId.equals(customer.getCreatedId())){
					customersId.add(customer.getId());
				}
			}
		}
        return customersId;
    }
    public List<Invoice> assemblingInvoice(List<Invoice> invoiceList) {
    	for (Invoice invoice : invoiceList) {
    		if(invoice.getLotId()!=null){
    			Lot lot=this.lotHibernateRepository.getOneById(invoice.getLotId(), Lot.class);
    			invoice.setLot(lot);
    		}
    		if(invoice.getContractId()!=null){
    			Contract contract=this.contractHibernateRepository.getOneById(invoice.getContractId(), Contract.class);
    			invoice.setContract(contract);
    		}
    	}
    	return invoiceList;
	}
    
    protected void calculate(Lot lot,List<CustomerBalance_new> cusBalList,
    		Map<String, CustomerBalance_new> maps,BigDecimal receipt,BigDecimal pay){
    	String key=lot.getCustomerId()+lot.getLegalId()+lot.getCurrency();
		CustomerBalance_new cusBal=null;
		if(maps.containsKey(key)){
			cusBal=maps.get(key);
		}else{
			cusBal=cusBalList.stream().filter(c->c.getCustomerId().equals(lot.getCustomerId())
					&&c.getLegalId().equals(lot.getLegalId())
					&&c.getCurrency().equals(lot.getCurrency())).findFirst().orElse(null);
		}
		
		if(cusBal!=null){
			
			this.cusBalanceRepository.getCurrentSession().evict(cusBal);//将实体对象清除游离状态，避免set属性后执行update操作
			cusBal.setReceipt(cusBal.getReceipt().add(receipt));
			cusBal.setPay(cusBal.getPay().add(pay));
			maps.put(key, cusBal);
		}else{
			
			CustomerBalance_new insertCusBal=new CustomerBalance_new();
			insertCusBal.setCustomerId(lot.getCustomerId());
			insertCusBal.setLegalId(lot.getLegalId());
			insertCusBal.setCurrency(lot.getCurrency());
			insertCusBal.setReceipt(receipt);
			insertCusBal.setPay(pay);
			insertCusBal.setRealReceipt(BigDecimal.ZERO);
			insertCusBal.setRealPay(BigDecimal.ZERO);
			insertCusBal.setEndingBalance(BigDecimal.ZERO);
			insertCusBal.setInitBalance(BigDecimal.ZERO);
			maps.put(key, insertCusBal);
		}
    }
    
	@Override
	public synchronized ActionResult<List<CustomerBalance_new>> getCustomerBalance(CustomerBalanceDetailParams param) {
		Date today=DateUtil.doSFormatDate(new Date(), "yyyy-MM-dd");
		List<CustomerBalance_new> resultList=new ArrayList<>();
		RefUtil total=new RefUtil();
		Map<String, CustomerBalance_new> maps=new HashMap<String, CustomerBalance_new>();
		if(param.getStartDate()==null
				||DateUtil.getIntervalDays(today, DateUtil.doSFormatDate(param.getStartDate(), "yyyy-MM-dd"))==0){
			/**
			 * 获取前一天结算记录
			 */
			StringBuffer sql=new StringBuffer();
			sql.append("SELECT * ");
			sql.append("FROM ");
			sql.append("  (SELECT row_number() over(partition BY CustomerId,LegalId,Currency");
			sql.append("                            ORDER BY CreatedAt DESC) AS rownum ,*");
			sql.append("   FROM [Basis].[CustomerBalance_New]) AS T");
			sql.append(" WHERE T.rownum = 1");
			List<CustomerBalance_new> cusBalList=this.cusBalanceRepository.ExecuteCorrectlySql(sql.toString(), CustomerBalance_new.class);
			
			/**
			 * 获取今天发生的订单、并且点价方式是固定价格
			 */
			DetachedCriteria todayLot=DetachedCriteria.forClass(Lot.class);
			todayLot.add(Restrictions.ge("CreatedAt", DateUtil.startOfTodDay(today)));
			todayLot.add(Restrictions.le("CreatedAt",  DateUtil.endOfTodDay(today)));
			todayLot.add(Restrictions.eq("MajorType", MajorType.Fix));
			List<Lot> lots=this.lotHibernateRepository.GetQueryable(Lot.class).where(todayLot).toList();
			
			if(lots!=null&&lots.size()>0){
				for (Lot lot : lots) {
					/**
					 * 应收、应付
					 */
					BigDecimal receipt=BigDecimal.ZERO;
					BigDecimal pay=BigDecimal.ZERO;
					if(lot.getSpotDirection().equals(SpotType.Sell)){
                		receipt=receipt.add(lot.getQuantity().multiply(lot.getPrice()));
                	}else{
                		pay=pay.add(lot.getQuantity().multiply(lot.getPrice()));
                	}
					calculate(lot,cusBalList,maps,receipt,pay);
				}
			}
			
			//应收、应付
			/**
			 * 获取今天发生的点价记录
			 */
			DetachedCriteria todayPricing=DetachedCriteria.forClass(Pricing.class);
			todayPricing.add(Restrictions.ge("CreatedAt", DateUtil.startOfTodDay(today)));
			todayPricing.add(Restrictions.le("CreatedAt",  DateUtil.endOfTodDay(today)));
			List<Pricing> pricings=this.pricingRepository.GetQueryable(Pricing.class).where(todayPricing).toList();
			//找到对应批次
			List<String> lId=new ArrayList<>();
			for (Pricing p : pricings) {
				lId.add(p.getLotId());
			}
			if(lId.size()>0){
				DetachedCriteria pricingLot=DetachedCriteria.forClass(Lot.class);
				pricingLot.add(Restrictions.in("Id", lId));
				List<Lot> pricingLots=this.lotHibernateRepository.GetQueryable(Lot.class).where(pricingLot).toList();
				for (Lot lot : pricingLots) {
					/**
					 * 应收、应付
					 */
					BigDecimal receipt=BigDecimal.ZERO;
					BigDecimal pay=BigDecimal.ZERO;
					if(lot.getSpotDirection().equals(SpotType.Sell)){
                		receipt=receipt.add(lot.getQuantityPriced().multiply(lot.getPrice()));
                	}else{
                		pay=pay.add(lot.getQuantityPriced().multiply(lot.getPrice()));
                	}
					calculate(lot,cusBalList,maps,receipt,pay);
					
					if(lot.getIsDelivered()&&lot.getIsPriced()){
						receipt=BigDecimal.ZERO;
						pay=BigDecimal.ZERO;
						if(lot.getSpotDirection().equals(SpotType.Sell)){
	                		receipt=receipt.add((lot.getQuantityDelivered().subtract(lot.getQuantity())).multiply(lot.getPrice()));
	                	}else{
	                		pay=pay.add((lot.getQuantityDelivered().subtract(lot.getQuantity())).multiply(lot.getPrice()));
	                	}
						calculate(lot,cusBalList,maps,receipt,pay);
					}else{
						
					}
				}
				
			}
			
			
			/**
			 * 获取今天发生收货记录
			 */
			DetachedCriteria todayReShip=DetachedCriteria.forClass(Storage.class);
			todayReShip.add(Restrictions.ge("CreatedAt", DateUtil.startOfTodDay(today)));
			todayReShip.add(Restrictions.le("CreatedAt",  DateUtil.endOfTodDay(today)));
			List<Storage> storages=this.storageRepository.GetQueryable(Storage.class).where(todayReShip).toList();
			List<String> sId=new ArrayList<>();
			for (Storage s : storages) {
				sId.add(s.getLotId());
			}
			if(sId.size()>0){
				DetachedCriteria storageLot=DetachedCriteria.forClass(Lot.class);
				storageLot.add(Restrictions.eq("IsPriced", true));
				storageLot.add(Restrictions.eq("IsDelivered", true));
				storageLot.add(Restrictions.in("Id", sId));
				if(lId.size()>0) storageLot.add(Restrictions.not(Restrictions.in("Id", lId)));
				List<Lot> storageLots=this.lotHibernateRepository.GetQueryable(Lot.class).where(storageLot).toList();
				for (Lot lot : storageLots) {
					/**
					 * 应收、应付
					 */
					BigDecimal receipt=BigDecimal.ZERO;
					BigDecimal pay=BigDecimal.ZERO;
					if(lot.getSpotDirection().equals(SpotType.Sell)){
                		receipt=receipt.add((lot.getQuantityDelivered().subtract(lot.getQuantity())).multiply(lot.getPrice()));
                	}else{
                		pay=pay.add((lot.getQuantityDelivered().subtract(lot.getQuantity())).multiply(lot.getPrice()));
                	}
					calculate(lot,cusBalList,maps,receipt,pay);
				}
			}
			
			//实收、实付
			/**
			 * 获取今天发生金额记录
			 */
			DetachedCriteria todayFund=DetachedCriteria.forClass(Fund.class);
			todayFund.add(Restrictions.ge("CreatedAt", DateUtil.startOfTodDay(today)));
			todayFund.add(Restrictions.le("CreatedAt",  DateUtil.endOfTodDay(today)));
			List<Fund> todayFunds=this.fundHibernateRepository.GetQueryable(Fund.class).where(todayFund).toList();
			for (Entry<String, CustomerBalance_new> map : maps.entrySet()) {
				CustomerBalance_new cusBal=map.getValue();
				
				for(int i=todayFunds.size()-1;i>=0;i--){
					
					Fund f=todayFunds.get(i);
					
					if(cusBal.getCustomerId().equals(f.getCustomerId())&&
						cusBal.getLegalId().equals(f.getLegalId())&&
						cusBal.getCurrency().equals(f.getCurrency())){
						
						if(f.getDC().equals("D")){//付
							cusBal.setRealPay(cusBal.getRealPay().add(f.getAmount()));
						}
						if(f.getDC().equals("C")){//收
							cusBal.setRealReceipt(cusBal.getRealReceipt().add(f.getAmount()));
						}
						todayFunds.remove(i);
					}
				}
				
				//期末余额 = 期初余额 + (应收-实收)  - (应付 -实付）
				cusBal.setEndingBalance(cusBal.getInitBalance().add(
						cusBal.getReceipt().subtract(cusBal.getRealReceipt())).subtract(
						cusBal.getPay().subtract(cusBal.getRealPay())));
				
				//昨天有记录，今天没有记录的数据
				for(int l=cusBalList.size()-1;l>=0;l--){
					if(cusBal.getId()!=null&&cusBalList.get(l).getId().equals(cusBal.getId())){
						cusBalList.remove(l);
					}
				}
			}
			
			resultList=new ArrayList<>(maps.values());
			resultList.addAll(cusBalList);
			
		}else{
			//DetachedCriteria dc=DetachedCriteria.forClass(CustomerBalance_new.class);
			Criteria dc=this.cusBalanceRepository.CreateCriteria(CustomerBalance_new.class);
			dc.add(Restrictions.ge("CreatedAt", DateUtil.startOfTodDay(param.getStartDate())));
			dc.add(Restrictions.le("CreatedAt",  DateUtil.endOfTodDay(param.getStartDate())));
			//resultList=this.cusBalanceRepository.GetQueryable(CustomerBalance_new.class).where(dc).toList();
			resultList=this.cusBalanceRepository.GetPage(dc, param.getPageSize(), param.getPageIndex(), null, null, total).getData();
		}
		ActionResult<List<CustomerBalance_new>> result=new ActionResult<>();
		result.setData(resultList);
		result.setMessage("成功");
		result.setSuccess(true);
		result.setTotal(total.getTotal());
		return result;
	}
	
	@Override
	public void getCustomerBalanceForJob(CustomerBalanceDetailParams param) {
		Date today=DateUtil.doSFormatDate(new Date(), "yyyy-MM-dd");
		Map<String, CustomerBalance_new> maps=new HashMap<String, CustomerBalance_new>();
		/**
		 * 获取前一天结算记录
		 */
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * ");
		sql.append("FROM ");
		sql.append("  (SELECT row_number() over(partition BY CustomerId,LegalId,Currency");
		sql.append("                            ORDER BY CreatedAt DESC) AS rownum ,*");
		sql.append("   FROM [Basis].[CustomerBalance_New]) AS T");
		sql.append(" WHERE T.rownum = 1");
		List<CustomerBalance_new> cusBalList=this.cusBalanceRepository.ExecuteCorrectlySql(sql.toString(), CustomerBalance_new.class);
		
		/**
		 * 获取今天发生的订单、并且点价方式是固定价格
		 */
		DetachedCriteria todayLot=DetachedCriteria.forClass(Lot.class);
		todayLot.add(Restrictions.ge("CreatedAt", DateUtil.startOfTodDay(today)));
		todayLot.add(Restrictions.le("CreatedAt",  DateUtil.endOfTodDay(today)));
		todayLot.add(Restrictions.eq("MajorType", MajorType.Fix));
		List<Lot> lots=this.lotHibernateRepository.GetQueryable(Lot.class).where(todayLot).toList();
		
		if(lots!=null&&lots.size()>0){
			for (Lot lot : lots) {
				/**
				 * 应收、应付
				 */
				BigDecimal receipt=BigDecimal.ZERO;
				BigDecimal pay=BigDecimal.ZERO;
				if(lot.getSpotDirection().equals(SpotType.Sell)){
            		receipt=receipt.add(lot.getQuantity().multiply(lot.getPrice()));
            	}else{
            		pay=pay.add(lot.getQuantity().multiply(lot.getPrice()));
            	}
				calculate(lot,cusBalList,maps,receipt,pay);
			}
		}
		
		//应收、应付
		/**
		 * 获取今天发生的点价记录
		 */
		DetachedCriteria todayPricing=DetachedCriteria.forClass(Pricing.class);
		todayPricing.add(Restrictions.ge("CreatedAt", DateUtil.startOfTodDay(today)));
		todayPricing.add(Restrictions.le("CreatedAt",  DateUtil.endOfTodDay(today)));
		List<Pricing> pricings=this.pricingRepository.GetQueryable(Pricing.class).where(todayPricing).toList();
		//找到对应批次
		List<String> lId=new ArrayList<>();
		for (Pricing p : pricings) {
			lId.add(p.getLotId());
		}
		if(lId.size()>0){
			DetachedCriteria pricingLot=DetachedCriteria.forClass(Lot.class);
			pricingLot.add(Restrictions.in("Id", lId));
			List<Lot> pricingLots=this.lotHibernateRepository.GetQueryable(Lot.class).where(pricingLot).toList();
			for (Lot lot : pricingLots) {
				/**
				 * 应收、应付
				 */
				BigDecimal receipt=BigDecimal.ZERO;
				BigDecimal pay=BigDecimal.ZERO;
				if(lot.getSpotDirection().equals(SpotType.Sell)){
            		receipt=receipt.add(lot.getQuantityPriced().multiply(lot.getPrice()));
            	}else{
            		pay=pay.add(lot.getQuantityPriced().multiply(lot.getPrice()));
            	}
				calculate(lot,cusBalList,maps,receipt,pay);
				
				if(lot.getIsDelivered()&&lot.getIsPriced()){
					receipt=BigDecimal.ZERO;
					pay=BigDecimal.ZERO;
					if(lot.getSpotDirection().equals(SpotType.Sell)){
                		receipt=receipt.add((lot.getQuantityDelivered().subtract(lot.getQuantity())).multiply(lot.getPrice()));
                	}else{
                		pay=pay.add((lot.getQuantityDelivered().subtract(lot.getQuantity())).multiply(lot.getPrice()));
                	}
					calculate(lot,cusBalList,maps,receipt,pay);
				}else{
					
				}
			}
			
		}
		
		
		/**
		 * 获取今天发生收货记录
		 */
		DetachedCriteria todayReShip=DetachedCriteria.forClass(Storage.class);
		todayReShip.add(Restrictions.ge("CreatedAt", DateUtil.startOfTodDay(today)));
		todayReShip.add(Restrictions.le("CreatedAt",  DateUtil.endOfTodDay(today)));
		List<Storage> storages=this.storageRepository.GetQueryable(Storage.class).where(todayReShip).toList();
		List<String> sId=new ArrayList<>();
		for (Storage s : storages) {
			sId.add(s.getLotId());
		}
		if(sId.size()>0){
			DetachedCriteria storageLot=DetachedCriteria.forClass(Lot.class);
			storageLot.add(Restrictions.eq("IsPriced", true));
			storageLot.add(Restrictions.eq("IsDelivered", true));
			storageLot.add(Restrictions.in("Id", sId));
			if(lId.size()>0) storageLot.add(Restrictions.not(Restrictions.in("Id", lId)));
			List<Lot> storageLots=this.lotHibernateRepository.GetQueryable(Lot.class).where(storageLot).toList();
			for (Lot lot : storageLots) {
				/**
				 * 应收、应付
				 */
				BigDecimal receipt=BigDecimal.ZERO;
				BigDecimal pay=BigDecimal.ZERO;
				if(lot.getSpotDirection().equals(SpotType.Sell)){
            		receipt=receipt.add((lot.getQuantityDelivered().subtract(lot.getQuantity())).multiply(lot.getPrice()));
            	}else{
            		pay=pay.add((lot.getQuantityDelivered().subtract(lot.getQuantity())).multiply(lot.getPrice()));
            	}
				calculate(lot,cusBalList,maps,receipt,pay);
			}
		}
		
		//实收、实付
		/**
		 * 获取今天发生金额记录
		 */
		DetachedCriteria todayFund=DetachedCriteria.forClass(Fund.class);
		todayFund.add(Restrictions.ge("CreatedAt", DateUtil.startOfTodDay(today)));
		todayFund.add(Restrictions.le("CreatedAt",  DateUtil.endOfTodDay(today)));
		List<Fund> todayFunds=this.fundHibernateRepository.GetQueryable(Fund.class).where(todayFund).toList();
		for (Entry<String, CustomerBalance_new> map : maps.entrySet()) {
			CustomerBalance_new cusBal=map.getValue();
			for(int i=todayFunds.size()-1;i>=0;i--){
				
				Fund f=todayFunds.get(i);
				
				if(cusBal.getCustomerId().equals(f.getCustomerId())&&
					cusBal.getLegalId().equals(f.getLegalId())&&
					cusBal.getCurrency().equals(f.getCurrency())){
					
					if(f.getDC().equals("D")){//付
						cusBal.setRealPay(cusBal.getRealPay().add(f.getAmount()));
					}
					if(f.getDC().equals("C")){//收
						cusBal.setRealReceipt(cusBal.getRealReceipt().add(f.getAmount()));
					}
				}
				todayFunds.remove(i);
			}
			//期末余额 = 期初余额 + (应收-实收)  - (应付 -实付）
			cusBal.setEndingBalance(cusBal.getInitBalance().add(
					cusBal.getReceipt().subtract(cusBal.getRealReceipt())).subtract(
					cusBal.getPay().subtract(cusBal.getRealPay())));
			this.cusBalanceRepository.SaveForJob(cusBal);
		}
	}
	@Override
	public void clearSession() {
		this.cusBalanceRepository.Clear();
	}
}
