package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.GlobalSetService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.domain.Basis.GlobalSet;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class GlobalSetServiceImpl implements GlobalSetService {


    private static final Logger logger=Logger.getLogger(GlobalSetServiceImpl.class);

    @Autowired
    private HibernateRepository<GlobalSet> repository;

    @Autowired
    private HibernateRepository<Customer> customerRepository;
    @Autowired
    private HibernateRepository<CustomerTitle> customerTitleRepository;


    @Override
    public ActionResult<GlobalSet> MyGlobalSet(String orgId) {

        DetachedCriteria where=DetachedCriteria.forClass(GlobalSet.class);
        where.add(Restrictions.eq("OrgId", orgId));

        List<GlobalSet> list=this.repository.GetQueryable(GlobalSet.class).where(where).toCacheList();

        GlobalSet globalSet=null;

        if(list!=null && list.size()>0) globalSet=list.get(0);

        ActionResult<GlobalSet> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(globalSet);

        return result;


    }

    @Override
    public ActionResult<GlobalSet> SaveGlobalSet(GlobalSet globalSet) {

        ActionResult<GlobalSet> result= new ActionResult<>();

        try{

            this.repository.SaveOrUpdate(globalSet);

            result.setSuccess(true);
            result.setMessage(MessageCtrm.SaveSuccess);
//            result.setData(globalSet);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

    @Override
    public ActionResult<String> InitDatabase() {

        //清空报表数据
        cleanUp();

        //清空客户抬头
        List<Customer> customerList=this.customerRepository.GetList(Customer.class);

        List<CustomerTitle> titleList=this.customerTitleRepository.GetList(CustomerTitle.class);

        for(CustomerTitle title:titleList){

            String customerId=title.getCustomerId();

            //如果存在就删除
            if(checkExist(customerId,customerList)){

                this.customerTitleRepository.PhysicsDelete(title.getId(), CustomerTitle.class);
            }
        }

        ActionResult<String> result=new ActionResult<>();

        result.setSuccess(true);
        result.setMessage("初始化成功");

        return result;
    }

    private boolean checkExist(String customerId, List<Customer> customerList) {

        boolean exists=false;

        if(customerId==null || customerId.trim().equals("")) return false;

        for(Customer customer:customerList){

            if(customer.getId().equals(customerId)){

                exists=true;

                break;
            }
        }

        return exists;
    }

    private void cleanUp() {

        //报表有关
        String sql = "Delete from [Report].FundBalanceDaily";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].FundBalanceMonthly";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].Inventory";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].ModelPnLSummary";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].ModelLotPnLHedge";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].ModelLotPnLSpotOneMany";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].ModelLotPnLSpotOneOne";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].ModelPnLSummary";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].ModelStorage";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].Reconciliation";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].RptHedgeDetail";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].RptLotSettleModel";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].RptPhysicalDetail";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].SettleDaily";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].SettleMonthly";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].SettleYearly";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].ZrSettleMonthly";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].VmPnL";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].VmPnL4Hedge";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Report].VmPnL4Spot";
        repository.ExecuteNonQuery(sql);

        //MISC
        sql = "Delete from [Physical].Tip";
        repository.ExecuteNonQuery(sql);
        //上传的附件
        sql = "Delete from [Physical].Attachment";
        repository.ExecuteNonQuery(sql);

        //审批有关
        sql = "Delete from [Physical].Approve";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Physical].Pending";
        repository.ExecuteNonQuery(sql);

        //财务有关
        sql = "Delete from [Physical].InvoicePosition";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Physical].LC";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Physical].Fund";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Physical].InvoiceStorage";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Physical].Invoice";
        repository.ExecuteNonQuery(sql);

        //期货有关
        sql = "Delete from [Physical].PositionPnL";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Physical].Position";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Physical].PorfolioLot";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Physical].Porfolio";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Physical].Pricing";
        repository.ExecuteNonQuery(sql);

        //现货有关
        sql = "Delete from [Physical].Storage";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Physical].Invoice";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Physical].LotBrand";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Physical].Lot";
        repository.ExecuteNonQuery(sql);
        sql = "Delete from [Physical].Contract";
        repository.ExecuteNonQuery(sql);

        //删除客户和相关的台头和银行信息（内部客户除外）
        sql = "Delete from [Basis].[CustomerBank]";
        repository.ExecuteNonQuery(sql);

        sql = "Delete from [Basis].[Customer] where (IsInternalCustomer = 0)";
        repository.ExecuteNonQuery(sql);



    }
}
