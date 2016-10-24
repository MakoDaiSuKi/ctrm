package com.smm.ctrm.bo.impl.Maintain;

import com.alibaba.fastjson.JSON;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Maintain.DSMEService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.LoginInfo;
import com.smm.ctrm.domain.Maintain.Calendar;
import com.smm.ctrm.domain.Maintain.DSME;
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
import com.smm.ctrm.util.QuoteMapping;
import com.smm.ctrm.util.RefUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class DSMEServiceImpl implements DSMEService {

    private static final Logger logger=Logger.getLogger(DSMEServiceImpl.class);

    @Autowired
    private HibernateRepository<DSME> repository;

    @Autowired
    private CommonService commonService;
    
    @Autowired
    private HibernateRepository<Commodity> commodityRepository;
    
    @Autowired
    private HibernateRepository<Market> marketRepository;
    

    private final static String CODE="200";

    @Override
    public ActionResult<String> Sync() {

        try{


            List<DSME> dsmes = GetDsmes4Sync();

            if(dsmes==null || dsmes.size()==0) throw new Exception("dsmes is null");

            dsmes.forEach(dsme -> {

                //检查是否存在
                DetachedCriteria where=DetachedCriteria.forClass(DSME.class);
                where.add(Restrictions.eq("TradeDate", dsme.getTradeDate()));
                where.add(Restrictions.eq("MarketId", dsme.getMarketId()));
                where.add(Restrictions.eq("CommodityId", dsme.getCommodityId()));
                where.add(Restrictions.ne("Id", dsme.getId()));
                List<DSME> list=this.repository.GetQueryable(DSME.class).where(where).toList();

                if(list==null || list.size()==0){

                    dsme.setId(null);
                    dsme.setVersion(0);
                    this.repository.SaveOrUpdate(dsme);
                }

            });

            return new ActionResult<>(true,"同步成功");
        }catch (Exception e){

            logger.error(e);

            return new ActionResult<>(false,e.getMessage());
        }

    }

    private List<DSME> GetDsmes4Sync() {


        LoginInfo syncLoginInfo = this.commonService.GetSyncLoginInfo();

        if (syncLoginInfo == null) return null;

        List<DSME> lmes = null;

        //#region 获取数据

        Date targetDate= DateUtil.getDiffDate(new Date(),-7);

        HashMap<String,String> param=new HashMap<>();

        param.put("StartDate",targetDate.toString());


        WebApiClient webApiClient=new WebApiClient(CommonService.SyncUrl, syncLoginInfo.toString());

        String result=webApiClient.Post("api/Maintain/DSME/Pager",param);

        if(!StringUtils.isEmpty(result)){

            ActionResult<List<DSME>> resultObj= JSON.parseObject(result, ActionResult.class);

            if(resultObj.isSuccess()){

                lmes=resultObj.getData();
            }


        }

        return lmes;

    }

    @Override
    public Criteria GetCriteria() {

        return this.repository.CreateCriteria(DSME.class);

    }

    @Override
    public ActionResult<DSME> Save(DSME dsme) throws Exception {

        ActionResult<DSME> result= new ActionResult<>();


            //检查重复
            DetachedCriteria where=DetachedCriteria.forClass(DSME.class);
            where.add(Restrictions.eq("TradeDate", dsme.getTradeDate()));
            where.add(Restrictions.eq("MarketId", dsme.getMarketId()));
            where.add(Restrictions.eq("CommodityId", dsme.getCommodityId()));
            where.add(Restrictions.eqOrIsNull("SpecId", dsme.getSpecId()));
            where.add(Restrictions.neOrIsNotNull("Id", dsme.getId()));

            List<DSME> list=this.repository.GetQueryable(DSME.class).where(where).toList();

            if(list!=null && list.size()>0) throw new Exception(MessageCtrm.DuplicatedName);

            this.repository.SaveOrUpdate(dsme);

            result.setSuccess(true);
            result.setData(dsme);

        return result;
    }

    @Override
    public ActionResult<String> Delete(String id) {

        ActionResult<String> result= new ActionResult<>();

        try{

            this.repository.PhysicsDelete(id,DSME.class);

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
    public ActionResult<DSME> GetById(String id) {

        ActionResult<DSME> result= new ActionResult<>();

        result.setSuccess(true);
        result.setData(this.repository.getOneById(id,DSME.class));

        return result;
    }

    @Override
    public List<DSME> Dsmes() {

        DetachedCriteria where=DetachedCriteria.forClass(DSME.class);
        where.add(Restrictions.eq("IsHidden", false));

        return this.repository.GetQueryable(DSME.class).where(where).toList();

    }

    @Override
    public List<DSME> Dsmes(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy, String orderBy) {

        return this.repository.GetPage(criteria,pageSize,pageIndex,sortBy,orderBy,total).getData();
    }

    /**
     * 接入SMM现货数据
     */
	@SuppressWarnings("unchecked")
	@Override
	public void fetchQuote() {
		List<String> dataSourceKey=DataSourceConfig.getDatasourcenamelist();
		if(dataSourceKey.size()>0){
			dataSourceKey.forEach(p->{
				//设置数据源
				DataSourceContextHolder.setDataSourceType(p);
				
				List<Commodity> commList=this.commodityRepository.GetQueryable(Commodity.class).toList();
				
				DetachedCriteria dc=DetachedCriteria.forClass(Market.class);
				dc.add(Restrictions.eq("Code", QuoteMapping.MARKET_CODE_SMM));
				List<Market> marketList=this.marketRepository.GetQueryable(Market.class).where(dc).toList();
				
				String typeUrl=PropertiesUtil.getString("spot.quotation.type");
				String tJson=HttpClientUtil.requestByGetMethod(typeUrl);
				if(StringUtils.isNotBlank(tJson)){
					Map<String, Object> map=JSONUtil.doConvertJson2Map(tJson);
					if(map.containsKey("Code")&&String.valueOf(map.get("Code")).equals(CODE)){
						List<Map<String, Object>> lMap=(List<Map<String, Object>>) map.get("Data");
						if(lMap.size()>0){
							for (Commodity commodity : commList) {
								for (Map<String, Object> m : lMap) {
									if(commodity.getName().equals(m.get("ProductTypeName"))){
										/**
										 * 取SMM现货价格
										 */
										String lastDataUrl=PropertiesUtil.getString("spot.quotation.lastData").replace("{0}", String.valueOf(m.get("ProductTypeID")));
										String rJson=HttpClientUtil.requestByGetMethod(lastDataUrl);
										if(StringUtils.isNotBlank(rJson)){
											Map<String, Object> dMap=JSONUtil.doConvertJson2Map(rJson);
											if(dMap.containsKey("Code")&&String.valueOf(dMap.get("Code")).equals(CODE)&&dMap.get("Data")!=null){
												List<Map<String, Object>> resultMap=(List<Map<String, Object>>) dMap.get("Data");
												for (Map<String, Object> m2 : resultMap) {
													if(String.valueOf(m2.get("ProductID")).equals(QuoteMapping.map.get(commodity.getCode()))){
														/**
														 * 取升贴水
														 */
														Map<String, Object> premium=null;
														if(m2.get("PremiumID")!=null&&!m2.get("PremiumID").equals("0")){
															String nowDate=DateUtil.doFormatDate(new Date(), "yyyy-MM-dd");
															String premiumUrl=PropertiesUtil.getString("spot.quotation.Premium")
																	.replace("{0}", String.valueOf(m2.get("PremiumID")))
																	.replace("{1}", nowDate)
																	.replace("{2}", nowDate);
															String pJson=HttpClientUtil.requestByGetMethod(premiumUrl);
															if(StringUtils.isNotBlank(pJson)){
																Map<String, Object> pMap=JSONUtil.doConvertJson2Map(pJson);
																if(pMap.containsKey("Code")&&String.valueOf(pMap.get("Code")).equals(CODE)){
																	if(pMap.get("Data")!=null){
																		List<Map<String, Object>> premiumMap=(List<Map<String, Object>>) pMap.get("Data");
																		premium=premiumMap.get(premiumMap.size()-1);
																	}
																}
															}
														}
														
														/**
														 * 保存数据到数据库
														 */
														DSME dsme=new DSME();
														dsme.setTradeDate(new Date());
														//价格部分
														dsme.setPriceLow(new BigDecimal(String.valueOf(m2.get("Low"))));
														dsme.setPriceHigh(new BigDecimal(String.valueOf(m2.get("Highs"))));
														dsme.setPriceAverage(new BigDecimal(String.valueOf(m2.get("Average"))));
														if(premium!=null){
															//升贴水部分
															dsme.setPremiumLow(new BigDecimal(String.valueOf(premium.get("Low"))));
															dsme.setPremiumHigh(new BigDecimal(String.valueOf(premium.get("Highs"))));
															dsme.setPremiumAverage(new BigDecimal(String.valueOf(premium.get("Average"))));
														}
														dsme.setCommodityId(commodity.getId());
														dsme.setMarketId(marketList.get(0).getId());
														
														this.repository.SaveForJob(dsme);
														
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				repository.Clear(); // session级别缓存未区分多数据源，因此要清空
			});
		}
	}
}
