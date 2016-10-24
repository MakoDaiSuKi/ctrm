package com.smm.ctrm.bo.impl.Maintain;

import com.smm.ctrm.bo.Maintain.FetchCalendarService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class FetchCalendarServiceImpl implements FetchCalendarService {

    private static final Logger logger=Logger.getLogger(FetchCalendarServiceImpl.class);

    @Autowired
    private HibernateRepository<FetchCalendar> repository;


    @Autowired
    private HibernateRepository<Market> marketHibernateRepository;


    @Autowired
    private HibernateRepository<LMB> lmbHibernateRepository;


    @Autowired
    private HibernateRepository<FetchLME> fetchLMEHibernateRepository;


    @Autowired
    private HibernateRepository<FetchSFE> fetchSFEHibernateRepository;


    @Autowired
    private HibernateRepository<FetchCalendar> fetchCalendarHibernateRepository;


    @Autowired
    private HibernateRepository<FetchDSME> fetchDSMEHibernateRepository;



    @Override
    public ActionResult<String> GenerateLmbCalendar(Date dtDateStartTime, Date dtDateEndTime) {

        try{

            DetachedCriteria where=DetachedCriteria.forClass(Market.class);
            where.add(Restrictions.eq("Code","lmb"));

            //检查市场代码
            List<Market> lmeMarkets=this.marketHibernateRepository.GetQueryable(Market.class).where(where).toList();

            if(lmeMarkets==null || lmeMarkets.size()==0) throw new Exception("市场基础代码设置错误，请检查");


            where=DetachedCriteria.forClass(LMB.class);

            Date now=new Date();

            //开始日期
            if (dtDateStartTime != null && dtDateStartTime != now)
            {
                where.add(Restrictions.ge("TradeDate", dtDateStartTime));
            }
            //结束日期
            if (dtDateEndTime != null && dtDateEndTime != now)
            {
                where.add(Restrictions.le("TradeDate", dtDateEndTime));
            }


            List<LMB> lmbs=this.lmbHibernateRepository.GetQueryable(LMB.class).where(where).toList();

            if(lmbs==null || lmbs.size()==0) throw new Exception("lmb is null");


            //分组
            Map<Date,List<LMB>> temp= lmbs.stream().collect(Collectors.groupingBy(LMB::getTradeDate));

            //构建新集合
            List<FetchCalendar> calendars=new ArrayList<>();

            for (Date key : temp.keySet()) {

                FetchCalendar fetchCalendar = new FetchCalendar();
                fetchCalendar.setTradeDate(key);
                fetchCalendar.setIsTrade(true);
                fetchCalendar.setMarketId(lmeMarkets.get(0).getId());

                calendars.add(fetchCalendar);
            }

            //组合查询条件
            calendars.forEach(c->{

                //检查是否已存在
                DetachedCriteria where_1=DetachedCriteria.forClass(FetchCalendar.class);
                where_1.add(Restrictions.eq("MarketId",c.getMarketId()));
                where_1.add(Restrictions.eq("TradeDate",c.getTradeDate()));

                List<FetchCalendar> list=this.fetchCalendarHibernateRepository.GetQueryable(FetchCalendar.class).where(where_1).toList();

                if(list==null || list.size()==0) this.fetchCalendarHibernateRepository.SaveOrUpdate(c);
            });


            return new ActionResult<>(true,"成功生成LMB交易日历");

        }catch (Exception e){

            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }

    }

    @Override
    public ActionResult<String> GenerateLmeCalendar(Date dtDateStartTime, Date dtDateEndTime) {


        try{

            DetachedCriteria where=DetachedCriteria.forClass(Market.class);
            where.add(Restrictions.eq("Code","lme"));

            //检查市场代码
            List<Market> lmeMarkets=this.marketHibernateRepository.GetQueryable(Market.class).where(where).toList();

            if(lmeMarkets==null || lmeMarkets.size()==0) throw new Exception("市场基础代码设置错误，请检查");


            where=DetachedCriteria.forClass(FetchLME.class);

            Date now=new Date();

            //开始日期
            if (dtDateStartTime != null && dtDateStartTime != now)
            {
                where.add(Restrictions.ge("TradeDate", dtDateStartTime));
            }
            //结束日期
            if (dtDateEndTime != null && dtDateEndTime != now)
            {
                where.add(Restrictions.le("TradeDate", dtDateEndTime));
            }


            List<FetchLME> lmes=this.fetchLMEHibernateRepository.GetQueryable(FetchLME.class).where(where).toList();

            if(lmes==null || lmes.size()==0) throw new Exception("LME is null");


            //分组
            Map<Date,List<FetchLME>> temp= lmes.stream().collect(Collectors.groupingBy(FetchLME::getTradeDate));

            //构建新集合
            List<FetchCalendar> calendars=new ArrayList<>();

            for (Date key : temp.keySet()) {

                FetchCalendar fetchCalendar = new FetchCalendar();
                fetchCalendar.setTradeDate(key);
                fetchCalendar.setIsTrade(true);
                fetchCalendar.setMarketId(lmeMarkets.get(0).getId());

                calendars.add(fetchCalendar);
            }

            //组合查询条件
            calendars.forEach(c->{

                //检查是否已存在
                DetachedCriteria where_1=DetachedCriteria.forClass(FetchCalendar.class);
                where_1.add(Restrictions.eq("MarketId",c.getMarketId()));
                where_1.add(Restrictions.eq("TradeDate",c.getTradeDate()));

                List<FetchCalendar> list=this.fetchCalendarHibernateRepository.GetQueryable(FetchCalendar.class).where(where_1).toList();

                if(list==null || list.size()==0) this.fetchCalendarHibernateRepository.SaveOrUpdate(c);
            });

            return new ActionResult<>(true,"成功生成LME交易日历");

        }catch (Exception e){

            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }

    }

    @Override
    public ActionResult<String> GenerateSfeCalendar(Date dtDateStartTime, Date dtDateEndTime) {

        try{

            DetachedCriteria where=DetachedCriteria.forClass(FetchSFE.class);

            //开始日期
            if (dtDateStartTime != null)
            {
                where.add(Restrictions.ge("TradeDate",dtDateStartTime));
            }
            //结束日期
            if (dtDateEndTime != null)
            {
                where.add(Restrictions.le("TradeDate", dtDateEndTime));
            }

            where.add(Restrictions.eq("Code","SFE"));

            List<Market> lmeMarkets = marketHibernateRepository.GetQueryable(Market.class).where(where).toList();

            if (lmeMarkets == null || lmeMarkets.size()==0) return new ActionResult<>(false,"市场基础代码设置错误，请检查");

            List<FetchSFE> sfes = fetchSFEHibernateRepository.GetList(FetchSFE.class);


            Map<Date,List<FetchSFE>> temp_map=sfes.stream().collect(Collectors.groupingBy(FetchSFE::getTradeDate));

            List<FetchCalendar>  calendars=new ArrayList<>();

            for(Date key:temp_map.keySet()){

                FetchCalendar f=new FetchCalendar();

                f.setTradeDate(key);
                f.setIsTrade(true);
                f.setMarketId(lmeMarkets.get(0).getId());

            }


            //------
            for (FetchCalendar c : calendars)
            {

                where=DetachedCriteria.forClass(FetchCalendar.class);
                where.add(Restrictions.eq("MarketId", c.getMarketId()));
                where.add(Restrictions.eq("TradeDate", c.getTradeDate()));

                List<FetchCalendar> exist = fetchCalendarHibernateRepository.GetQueryable(FetchCalendar.class).where(where).toList();

                if (exist == null)
                    fetchCalendarHibernateRepository.SaveOrUpdate(c);
            }

            return new ActionResult<>(true,"成功生成SFE交易日历");

        }catch (Exception e){

            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }

    }

    @Override
    public ActionResult<String> GenerateDsmeCalendar(Date dtDateStartTime, Date dtDateEndTime) {

        try{

            DetachedCriteria where=DetachedCriteria.forClass(FetchDSME.class);

            //开始日期
            if (dtDateStartTime != null)
            {
                where.add(Restrictions.ge("TradeDate",dtDateStartTime));
            }
            //结束日期
            if (dtDateEndTime != null)
            {
                where.add(Restrictions.le("TradeDate", dtDateEndTime));
            }

            ProjectionList pList=Projections.projectionList();
            pList.add(Projections.groupProperty("TradeDate"));
            pList.add(Projections.groupProperty("MarketId"));
            where.setProjection(pList);

            List<Map<String,Object>> mapList= (List<Map<String, Object>>) repository.getHibernateTemplate().findByCriteria(where);

            List<FetchCalendar> calendars = new ArrayList<>();

            mapList.forEach(map->{

                FetchCalendar f=new FetchCalendar();
                f.setTradeDate((Date) map.get("TradeDate"));
                f.setIsTrade(true);
                f.setMarketId((String) map.get("MarketId"));
            });

            for (FetchCalendar c : calendars)
            {
                where=DetachedCriteria.forClass(FetchCalendar.class);
                where.add(Restrictions.eq("MarketId", c.getMarketId()));
                where.add(Restrictions.eq("TradeDate", c.getTradeDate()));

                List<FetchCalendar> exist = fetchCalendarHibernateRepository.GetQueryable(FetchCalendar.class).where(where).toList();

                if (exist == null)
                    fetchCalendarHibernateRepository.SaveOrUpdate(c);
            }

            return new ActionResult<>(true,"成功生成DSME交易日历");

        }catch (Exception e){

            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }

    }

    @Override
    public ActionResult<String> SaveVm(VmFetchCalendar vm) {

        try{

            DetachedCriteria where=DetachedCriteria.forClass(FetchCalendar.class);
            where.add(Restrictions.eq("TradeDate", vm.getTradeDate()));

            List<FetchCalendar> olds = fetchCalendarHibernateRepository.GetQueryable(FetchCalendar.class).where(where).toList();

            for (FetchCalendar fetchCalendar : olds)
            {
                fetchCalendar.setIsTrade(false);
                fetchCalendarHibernateRepository.SaveOrUpdate(fetchCalendar);
            }

            if (vm.getMarkets() != null && vm.getMarkets().size() > 0)
            {
                for (Market market : vm.getMarkets())
                {
                    where=DetachedCriteria.forClass(FetchCalendar.class);
                    where.add(Restrictions.eq("MarketId", market.getId()));
                    where.add(Restrictions.eq("TradeDate", vm.getTradeDate()));
                    List<FetchCalendar> temp_list= fetchCalendarHibernateRepository.GetQueryable(FetchCalendar.class).where(where).toList();

                    FetchCalendar exist=new FetchCalendar();

                    if(temp_list!=null && temp_list.size()>0) exist=temp_list.get(0);

                    if (!StringUtils.isEmpty(exist.getId()))
                        exist.setUpdatedAt(new Date());
                    else
                        exist.setCreatedAt(new Date());

                    exist.setTradeDate(vm.getTradeDate());
                    exist.setIsTrade(true);
                    exist.setMarketId(market.getId());
                    fetchCalendarHibernateRepository.SaveOrUpdate(exist);
                }
            }

            return new ActionResult<>(true,MessageCtrm.SaveSuccess);

        }catch (Exception e){

            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }

    }

    @Override
    public ActionResult<String> Save(FetchCalendar fetchCalendar) {

        ActionResult<String> result= new ActionResult<>();

        try{

            //检查重复
            DetachedCriteria where=DetachedCriteria.forClass(FetchCalendar.class);
            where.add(Restrictions.eq("MarketId", fetchCalendar.getMarketId()));
            where.add(Restrictions.eq("TradeDate", fetchCalendar.getTradeDate()));
            where.add(Restrictions.ne("Id", fetchCalendar.getId()));

            List<FetchCalendar> list=this.repository.GetQueryable(FetchCalendar.class).where(where).toList();

            if(list!=null && list.size()>0) throw new Exception(MessageCtrm.DuplicatedName);

            this.repository.SaveOrUpdate(fetchCalendar);

            result.setSuccess(true);
            result.setMessage(MessageCtrm.SaveSuccess);

        }catch (Exception e){

            logger.error(e);

            result.setSuccess(false);
            result.setMessage(e.getMessage());

        }

        return result;
    }

    @Override
    public ActionResult<String> Delete(String id) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(id,FetchCalendar.class);

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
    public ActionResult<VmFetchCalendar> GetVmById(Date tradeDate) {

        if (tradeDate == null || tradeDate == new Date()){

            return new ActionResult<>(false,"参数日期非法");
        }


        VmFetchCalendar vm = new VmFetchCalendar();
        vm.setTradeDate(tradeDate);


        DetachedCriteria where=DetachedCriteria.forClass(FetchCalendar.class);
        where.add(Restrictions.eq("TradeDate", tradeDate));
        List<FetchCalendar> fetchCalendars=this.fetchCalendarHibernateRepository.GetQueryable(FetchCalendar.class).where(where).toList();

        if(fetchCalendars!=null && fetchCalendars.size()>0){

            vm.setMarkets(fetchCalendars.stream().map(FetchCalendar::getMarket).collect(Collectors.toList()));
        }

        return new ActionResult<>(true,null,vm);
    }

    @Override
    public ActionResult<FetchCalendar> GetById(String id) {

        ActionResult<FetchCalendar> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,FetchCalendar.class));

        return result;
    }

    @Override
    public Criteria GetCriteria() {

        return this.repository.CreateCriteria(FetchCalendar.class);

    }

    @Override
    public List<FetchCalendar> FetchCalendars(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort) {

        return this.repository.GetPage(param,pageSize,pageIndex,orderBy,orderSort,total).getData();
    }
}
