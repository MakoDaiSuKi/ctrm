package com.smm.ctrm.bo.impl.Maintain;

import com.alibaba.fastjson.JSON;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Maintain.SFEService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.LoginInfo;
import com.smm.ctrm.domain.Mainmeta;
import com.smm.ctrm.domain.MainmetaResult;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Maintain.LMB;
import com.smm.ctrm.domain.Maintain.LME;
import com.smm.ctrm.domain.Maintain.SFE;
import com.smm.ctrm.domain.apiClient.QuotationParams;
import com.smm.ctrm.domain.apiClient.WebApiClient;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.hibernate.DataSource.DataSourceConfig;
import com.smm.ctrm.hibernate.DataSource.DataSourceContextHolder;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.HttpClientUtil;
import com.smm.ctrm.util.JSONUtil;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.PropertiesUtil;
import com.smm.ctrm.util.RefUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class SFEServiceImpl implements SFEService {

    private static final Logger logger=Logger.getLogger(SFEServiceImpl.class);

    @Autowired
    private HibernateRepository<SFE> repository;
    
    @Autowired
    private HibernateRepository<Commodity> commodityRepository;
    
    private final static String CODE="0";


    @Autowired
    private CommonService commonService;

    @Override
    public ActionResult<String> Sync() {

        try{

            List<SFE> sfes = GetSFEs4Sync();


            return new ActionResult<>(true,"同步成功");

        }catch (Exception e){

            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }

    }

    private List<SFE> GetSFEs4Sync() {

        LoginInfo syncLoginInfo = this.commonService.GetSyncLoginInfo();
        if (syncLoginInfo == null) return null;

        List<SFE> sfes = null;

        Date targetDate= DateUtil.getDiffDate(new Date(),-7);

        HashMap<String,String> param=new HashMap<>();

        param.put("StartDate",targetDate.toString());


        WebApiClient webApiClient=new WebApiClient(CommonService.SyncUrl, syncLoginInfo.toString());

        String result=webApiClient.Post("api/Maintain/SFE/Pager",param);

        if(!StringUtils.isEmpty(result)){

            ActionResult<List<SFE>> resultObj= JSON.parseObject(result, ActionResult.class);

            if(resultObj.isSuccess()){

                sfes=resultObj.getData();
            }
        }

        return sfes;
    }

    @Override
    public ActionResult<SFE> Save(SFE sfe) {

        ActionResult<SFE> result= new ActionResult<>();

  
            //检查重复
            DetachedCriteria where=DetachedCriteria.forClass(SFE.class);
            where.add(Restrictions.eq("TradeDate", sfe.getTradeDate()));
            where.add(Restrictions.eq("CommodityId", sfe.getCommodityId()));
            where.add(Restrictions.neOrIsNotNull("Id", sfe.getId()));

            List<SFE> list=this.repository.GetQueryable(SFE.class).where(where).toList();

            if(list!=null && list.size()>0) {
            	result.setSuccess(false);
            	result.setMessage(MessageCtrm.DuplicatedName);
            	return result;
            } 

            this.repository.SaveOrUpdate(sfe);

            result.setSuccess(true);
            result.setMessage(MessageCtrm.SaveSuccess);
      
      

        return result;
    }

    @Override
    public ActionResult<String> Delete(String id) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(id,SFE.class);

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
    public ActionResult<SFE> GetById(String id) {

        ActionResult<SFE> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,SFE.class));

        return result;
    }

    @Override
    public Criteria GetCriteria() {


        return this.repository.CreateCriteria(SFE.class);

    }

    @Override
    public List<SFE> SFEs() {

        DetachedCriteria where=DetachedCriteria.forClass(SFE.class);
        where.add(Restrictions.eq("IsHidden", false));
        return  this.repository.GetQueryable(SFE.class).where(where).toList();
    }

    @Override
    public List<SFE> SFEs(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort) {

        return this.repository.GetPage(param,pageSize,pageIndex,orderBy,orderSort,total).getData();
    }

	@Override
	public void fetchQuote() {
		List<String> dataSourceKey=DataSourceConfig.getDatasourcenamelist();
		if(dataSourceKey.size()>0){
			dataSourceKey.forEach(p->{
				//设置数据源
				DataSourceContextHolder.setDataSourceType(p);
				//------------------获取所有金属-------------------------------------
				logger.info("获取所有金属");
				DetachedCriteria dc=DetachedCriteria.forClass(Commodity.class);
				List<Commodity> cpmmList=this.commodityRepository.GetQueryable(Commodity.class).where(dc).toList();
				List<String> mainmetas=new ArrayList<>();
				Map<String, String> commodityIds=new HashMap<>();
				//------------------获取金属主力ID-------------------------------------
				logger.info("获取金属主力ID");
				for (Commodity commodity : cpmmList) {
					String mainmetalUrl=PropertiesUtil.getString("futures.quotation.mainmetal").replace("{0}",commodity.getCode().toLowerCase());
					String mJson=HttpClientUtil.requestByGetMethod(mainmetalUrl);
					if(StringUtils.isNotBlank(mJson)){
						Map<String, Object> map=JSONUtil.doConvertJson2Map(mJson);
						if(map.containsKey("code")&&String.valueOf(map.get("code")).equals(CODE)){
							String mainmeta=(String) map.get("data");
							mainmetas.add(mainmeta);
							commodityIds.put(mainmeta, commodity.getId());
						}else{
							logger.info("["+commodity.getCode()+ "]没有获取到主力金属.");
						}
					}
				}
				
				String ids="";
				for (String id : commodityIds.keySet()) {
					ids=ids+id+",";
				}
				
				//------------------获取盘口数据-------------------------------------
				logger.info("获取盘口数据");
				if(org.apache.commons.lang3.StringUtils.isNoneBlank(ids)){
					ids=ids.substring(0,ids.length()-1);
					String current=PropertiesUtil.getString("futures.quotation.current").replace("{0}", ids);
					String cJson=HttpClientUtil.requestByGetMethod(current);
					if(StringUtils.isNotBlank(cJson)){
						try {
							MainmetaResult mainmetaResult=(MainmetaResult) JSONUtil.doConvertStringToBean(cJson,MainmetaResult.class);
							if(mainmetaResult!=null&&mainmetaResult.getMainmeta().size()>0){
								//保存数据到数据库
								for (Mainmeta mainmeta : mainmetaResult.getMainmeta()) {
									SFE sfe=new SFE();
									sfe.setAverage(mainmeta.getAverage());
									sfe.setPriceSettle(mainmeta.getSettlementPrice());
									sfe.setPriceWeighted(mainmeta.getSettlementPrice());
									sfe.setHigh(mainmeta.getHighestPrice());
									sfe.setLow(mainmeta.getLowestPrice());
									sfe.setCommodityId(commodityIds.get(mainmeta.getInstrumentID()));
									sfe.setTradeDate(DateUtil.doSFormatDate(mainmeta.getTradingDay(), "yyyy-MM-dd"));
									this.repository.SaveForJob(sfe);
								}
								
							}
						} catch (IOException e) {
							logger.error("获取期货行情出错:",e);
						}
					}else{
						logger.info("没有获取到行情数据.");
					}
				}
				repository.Clear(); // session级别缓存未区分多数据源，因此要清空
			});
		}
	}
}
