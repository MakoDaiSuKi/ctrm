package com.smm.ctrm.bo.impl.Report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Report.CustomerReportService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.CustomerBalance;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Report.ModelCustomerBalanceL1;
import com.smm.ctrm.domain.Report.ModelCustomerBalanceL2;
import com.smm.ctrm.domain.Report.ModelCustomerBalanceL3;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.KeyObj;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class CustomerReportServiceImpl implements CustomerReportService {


    private static final Logger logger=Logger.getLogger(CustomerReportServiceImpl.class);

    @Autowired
    private HibernateRepository<Position> repository;

    @Autowired
    private HibernateRepository<CustomerBalance> customerBalanceHibernateRepository;

    @Autowired
    private HibernateRepository<Fund> fundHibernateRepository;


    @Autowired
    private HibernateRepository<Lot> lotHibernateRepository;


    @Override
    public ActionResult<List<ModelCustomerBalanceL1>> PagerCustomerBalance4MultiCustomers(List<Customer> customers) {


        if (customers == null || customers.size()==0){

            return new ActionResult<>(false,"param is null: customers");
        }

        List<ModelCustomerBalanceL1> objs = new ArrayList<>();

        customers.forEach(customer -> {

            ActionResult<List<ModelCustomerBalanceL1>> result=this.PagerCustomerBalance4OneCustomer(customer.getId());

            if(result.getData()!=null) objs.addAll(result.getData());
        });

        return new ActionResult<>(true,null,objs);
    }

    @Override
    public ActionResult<List<ModelCustomerBalanceL1>> PagerCustomerBalance4OneCustomer(String customerId) {

        try{

            //取得客户名称List

            DetachedCriteria where=DetachedCriteria.forClass(CustomerBalance.class);
            where.add(Restrictions.eq("CustomerId", customerId));
            List<CustomerBalance> customerBalances=this.customerBalanceHibernateRepository.GetQueryable(CustomerBalance.class).where(where).toList();

            List<ModelCustomerBalanceL3> customerBalanceL3S= new ArrayList<>();

            if(customerBalances!=null && customerBalances.size()>0){


                for(CustomerBalance t:customerBalances){

                    ModelCustomerBalanceL3 model=new ModelCustomerBalanceL3();
                    model.setLegalId(t.getLegalId());
                    model.setLegalName(t.getLegal().getName());
                    model.setSumInitBalance(t.getInitBalance()==null? BigDecimal.ZERO:t.getInitBalance());
                    model.setSumLastBalance(t.getLastBalance()==null? BigDecimal.ZERO:t.getLastBalance());
                    model.setCustomerId(t.getCustomerId());
                    model.setCustomerName(t.getCustomer().getName());
                    customerBalanceL3S.add(model);

                }

            }


            // Funds------------------------

            where=DetachedCriteria.forClass(Fund.class);
            where.add(Restrictions.eq("CustomerId", customerId));
            List<Fund> funds=this.fundHibernateRepository.GetQueryable(Fund.class).where(where).toList();

            if(funds!=null && funds.size()>0){

                for(Fund t:funds){

                    ModelCustomerBalanceL3 model=new ModelCustomerBalanceL3();
                    model.setLotId(t.getLotId());
                    model.setLegalId(t.getLegalId());
                    model.setLegalName(t.getLegal().getName());
                    model.setCommodityName(t.getCommodity().getName());
                    model.setComments(t.getComments());
                    model.setDueBalance(BigDecimal.ZERO);
                    model.setAmount(t.getAmount());
                    model.setFullNo(t.getContract().getHeadNo());
                    model.setCustomerId(t.getCustomerId());
                    model.setCustomerName(t.getCustomer().getName());
                    customerBalanceL3S.add(model);



                }
            }

            //Lots------------------------------
            where=DetachedCriteria.forClass(Lot.class);
            where.add(Restrictions.eq("CustomerId", customerId));
            List<Lot> lots=this.lotHibernateRepository.GetQueryable(Lot.class).where(where).toList();

            if(lots!=null && lots.size()>0){

                for(Lot t:lots){

                    ModelCustomerBalanceL3 model=new ModelCustomerBalanceL3();
                    model.setContractId(t.getContractId());
                    model.setLotId(t.getId());
                    model.setLegalId(t.getLegalId());
                    model.setLegalName(t.getLegal().getName());
                    model.setCommodityName(t.getCommodity().getName());
                    model.setComments(t.getComments());
                    model.setBrandName(t.getBrandNames());
                    model.setQuantity(t.getIsDeleted()?t.getQuantityDelivered():t.getQuantity());
                    model.setPrice(t.getPrice()==null?BigDecimal.ZERO:t.getPrice());
                    model.setAmount(BigDecimal.ZERO) ;
                    model.setDueBalance(t.getDueBalance());
                    model.setFullNo(t.getFullNo());
                    model.setCustomerId(t.getCustomerId());
                    model.setCustomerName(t.getCustomer().getName());
                    model.setTradeDate(t.getContract().getTradeDate());
                    model.setIsInvoiced(t.getIsInvoiced());
                    customerBalanceL3S.add(model);
                }
            }

            List<ModelCustomerBalanceL3> originalList=customerBalanceL3S.stream()
            		.sorted(Comparator.comparing(ModelCustomerBalanceL3::getTradeDate))
            		.collect(Collectors.toList());

            //#region 汇总第1级

            Map<List<Object>,List<ModelCustomerBalanceL3>> groupMap= originalList.stream().collect(Collectors.groupingBy( m -> new KeyObj(m.getCustomerName(),m.getCustomerId()).getKeys()));

            List<ModelCustomerBalanceL1> listLevel1= new ArrayList<>();

            for(List<Object> key:groupMap.keySet()){

                ModelCustomerBalanceL1 model=new ModelCustomerBalanceL1();

                model.setCustomerName((String) key.get(0));
                model.setCustomerId((String) key.get(1));
                model.setSumFund(new BigDecimal(groupMap.get(key).stream().mapToDouble(b -> b.getAmount().doubleValue()).sum()));
                model.setSumLotInvoiced( new BigDecimal( groupMap.get(key).stream().filter(ModelCustomerBalanceL3::getIsInvoiced).mapToDouble(b->b.getDueBalance().doubleValue()).sum() ) );
                model.setSumLotUnInvoiced(new BigDecimal(groupMap.get(key).stream().filter(b -> !b.getIsInvoiced()).mapToDouble(b -> b.getDueBalance().doubleValue()).sum()));
                model.setSumInitBalance(new BigDecimal( groupMap.get(key).stream().mapToDouble(b->b.getSumInitBalance().doubleValue()).sum() ));

                BigDecimal SumInitBalance=new BigDecimal(groupMap.get(key).stream().mapToDouble(b->b.getSumInitBalance().doubleValue()).sum());
                BigDecimal DueBalance=new BigDecimal(groupMap.get(key).stream().mapToDouble(b -> b.getDueBalance().doubleValue()).sum());
                BigDecimal Amount=new BigDecimal(groupMap.get(key).stream().mapToDouble(b -> b.getAmount().doubleValue()).sum());

                model.setVirtualLastBalance(SumInitBalance.add(DueBalance).subtract(Amount));

                model.setSumLastBalance(new BigDecimal(groupMap.get(key).stream().mapToDouble(b->b.getSumLastBalance().doubleValue()).sum()));

                BigDecimal sum_isInvoiced_DueBalance=new BigDecimal( groupMap.get(key).stream().filter(ModelCustomerBalanceL3::getIsInvoiced).mapToDouble(b->b.getDueBalance().doubleValue()).sum() );

                model.setSumLotInvoicedMinusSumFund(sum_isInvoiced_DueBalance.subtract(Amount).add(SumInitBalance));

                listLevel1.add(model);

            }


            //#region 汇总第2级
            Map<List<Object>,List<ModelCustomerBalanceL3>> groupMap_2= originalList.stream()
                    .collect(Collectors.groupingBy(m -> new KeyObj(m.getCustomerId(),m.getLegalId(),m.getCustomerName(),m.getLegalName()).getKeys()));

            List<ModelCustomerBalanceL2> listLevel2= new ArrayList<>();

            for(List<Object> key:groupMap_2.keySet()){

                ModelCustomerBalanceL2 model=new ModelCustomerBalanceL2();

                model.setCustomerId((String) key.get(0));
                model.setLegalId((String) key.get(1));
                model.setCustomerName((String) key.get(2));
                model.setLegalName((String) key.get(3));

                //求和
                BigDecimal sum_Amount=new BigDecimal(groupMap.get(key).stream().mapToDouble(b -> b.getAmount().doubleValue()).sum());
                BigDecimal sum_isInvoiced_DueBalance=new BigDecimal( groupMap.get(key).stream().filter(ModelCustomerBalanceL3::getIsInvoiced).mapToDouble(b->b.getDueBalance().doubleValue()).sum() );
                BigDecimal sum_notInvoiced_DueBalance=new BigDecimal( groupMap.get(key).stream().filter(b -> !b.getIsInvoiced()).mapToDouble(b -> b.getDueBalance().doubleValue()).sum() );
                BigDecimal sum_DueBalance=new BigDecimal(groupMap.get(key).stream().mapToDouble(b -> b.getDueBalance().doubleValue()).sum());

                //赋值
                model.setSumFund(sum_Amount);
                model.setSumLotInvoiced(sum_isInvoiced_DueBalance);
                model.setSumLotUnInvoiced(sum_notInvoiced_DueBalance);
                model.setSumInitBalance(new BigDecimal( groupMap.get(key).stream().mapToDouble(b->b.getSumInitBalance().doubleValue()).sum() ));

                //求和
                BigDecimal sum_init_balance=GetCustomerBalanceSummary((String) key.get(1),(String) key.get(0))[0];
                BigDecimal sum_last_balance=GetCustomerBalanceSummary((String) key.get(1),(String) key.get(0))[1];

                //赋值
                model.setSumInitBalance(sum_init_balance);
                model.setSumLastBalance(sum_last_balance);
                model.setVirtualLastBalance(sum_init_balance.add(sum_DueBalance).subtract(sum_Amount));
                model.setSumLotInvoicedMinusSumFund(sum_isInvoiced_DueBalance.subtract(sum_Amount).add(sum_init_balance));

                listLevel2.add(model);
            }

            listLevel2=listLevel2.stream().sorted(Comparator.comparing(ModelCustomerBalanceL2::getCustomerName)
                    .thenComparing(Comparator.comparing(ModelCustomerBalanceL2::getLegalName)))
                    .collect(Collectors.toList());


            //汇总第3级
            List<ModelCustomerBalanceL3> filterList= originalList.stream().filter(b->b.getTradeDate()!=null).sorted(Comparator.comparing(ModelCustomerBalanceL3::getTradeDate)).collect(Collectors.toList());

            List<ModelCustomerBalanceL3> listLevel3=new ArrayList<>();

            filterList.forEach(x->{
                ModelCustomerBalanceL3 m =new ModelCustomerBalanceL3();
                m.setCustomerId(x.getCustomerId());
                m.setCustomerName(x.getCustomerName());
                m.setLegalId(x.getLegalId());
                m.setLegalName(x.getLegalName());
                m.setContractId(x.getContractId());
                m.setLotId(x.getLotId());
                m.setCommodityName(x.getCommodityName());
                m.setComments(x.getComments());
                m.setBrandName(x.getBrandName());
                m.setQuantity(x.getQuantity());
                m.setPrice(x.getPrice());
                m.setAmount(x.getAmount());
                m.setDueBalance(x.getDueBalance());
                m.setFullNo(x.getFullNo());
                m.setTradeDate(x.getTradeDate());
            });

            //---------------
            for (ModelCustomerBalanceL2 l2 : listLevel2)
            {
                l2.setListOfL3(listLevel3.stream().filter(w -> w.getLegalId().equals(l2.getLegalId()) && w.getCustomerId().equals(l2.getCustomerId())).collect(Collectors.toList()));

                BigDecimal  dct = BigDecimal.ZERO;   //为个LastBalance，是该客户的期初余额

                for (int i = 0; i < l2.getListOfL3().size(); i++)
                {
                    ModelCustomerBalanceL3 model=l2.getListOfL3().get(i);

                    if (i == 0)
                    {
                        model.setLastBalance(l2.getSumLastBalance().add(dct).add(model.getDueBalance()).subtract(model.getAmount()));
                        dct=model.getLastBalance();
                    }
                    else
                    {
                        model.setLastBalance(dct.add(model.getDueBalance()).subtract(model.getAmount()));
                        dct=model.getLastBalance();
                    }
                }
            }

            return new ActionResult<>(true,null,listLevel1);

        }catch (Exception e){

            logger.error(e);

            return  new ActionResult<>(false,e.getMessage());
        }

    }


    private BigDecimal[] GetCustomerBalanceSummary(String legalId, String customerId)
    {
        BigDecimal[] dc = { BigDecimal.ZERO, BigDecimal.ZERO };

        DetachedCriteria where=DetachedCriteria.forClass(CustomerBalance.class);
        where.add(Restrictions.eq("LegalId", legalId));
        where.add(Restrictions.eq("CustomerId", customerId));
        List<CustomerBalance> customerBalances = customerBalanceHibernateRepository.GetQueryable(CustomerBalance.class).where(where).toList();

        dc[0] = new BigDecimal(customerBalances.stream().mapToDouble(x->x.getInitBalance().doubleValue()).sum());
        dc[1] = new BigDecimal(customerBalances.stream().mapToDouble(x->x.getLastBalance().doubleValue()).sum());

        return dc;
    }
}
