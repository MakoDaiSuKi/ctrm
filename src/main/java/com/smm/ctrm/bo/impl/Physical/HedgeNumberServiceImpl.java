package com.smm.ctrm.bo.impl.Physical;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Physical.HedgeNumberService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.LoginInfoToken;
import com.smm.ctrm.domain.Physical.HedgeNumber;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.apiClient.HedgeNumberParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.DecimalUtil;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.StringUtil;
import com.smm.ctrm.util.Result.LS;
import com.smm.ctrm.util.Result.MajorType;
import com.smm.ctrm.util.Result.SpotType;

/**
 * 
 * 保值编号管理
 * 
 * @author zengshihua
 *
 */
@Service
public class HedgeNumberServiceImpl implements HedgeNumberService {

	@Autowired
	private HibernateRepository<HedgeNumber> hedgeNumberRepo;

	@Override
	public Criteria GetCriteria() {
		return this.hedgeNumberRepo.CreateCriteria(HedgeNumber.class);

	}

	@Override
	public ActionResult<HedgeNumber> Save(HedgeNumber hedgeNumber) {
		/**
		 * 保存关系表
		 */
		if(hedgeNumber.getId()!=null&&!hedgeNumber.getFlag()){
			/**
			 * 不存在，返回
			 */
			if(verify(hedgeNumber)){
				return new ActionResult<>(false, hedgeNumber.getNo()+"该保值编号不存在.");
			}
			
			/**
			 * 检查保值方案名称
			 */
			if(!verifyName(hedgeNumber)){
				return new ActionResult<>(false, "方案名称已经存在.");
			}
			
			/**
			 * 存在，直接追加
			 */
			
			List<Lot> lots=hedgeNumber.getLots();
			List<Position> positions=hedgeNumber.getPositions();
			hedgeNumber.setLots(null);
			hedgeNumber.setPositions(null);
			//保存修改信息
			this.hedgeNumberRepo.SaveOrUpdate(hedgeNumber);
			//保存关联关系
			this.saveFutures(hedgeNumber,lots);
			this.saveSpot(hedgeNumber,positions);
			
			
		}else{
			
			/**
			 * 检查编号是否存在
			 */
			if(!verify(hedgeNumber)){
				return new ActionResult<>(false, "保值编号已经存在.");
			}
			/**
			 * 检查保值方案名称
			 */
			if(!verifyName(hedgeNumber)){
				return new ActionResult<>(false, "方案名称已经存在.");
			}
			
			/**
			 * 保存保值编号表
			 * 
			 */
			List<Lot> lots=hedgeNumber.getLots();
			List<Position> positions=hedgeNumber.getPositions();
			hedgeNumber.setLots(null);
			hedgeNumber.setPositions(null);
			String hNId = this.hedgeNumberRepo.SaveOrUpdateRetrunId(hedgeNumber);
			hedgeNumber.setId(hNId);
			/**
			 * 保存保值编号与批次
			 */
			this.saveFutures(hedgeNumber,lots);
			/**
			 * 保存保值编号与头寸
			 */
			this.saveSpot(hedgeNumber,positions);
		}
		return new ActionResult<>(true, MessageCtrm.SaveSuccess,hedgeNumber,"");
	}

	@Override
	public ActionResult<String> GetNo(String type) {
		/**
		 * 本年第一天
		 */
		if (DateUtil.getCurrYearFirst(new Date())) {
			String first = DateUtil.doFormatDate(new Date(), "yyyyMMdd");
			return new ActionResult<>(true, "保值编号生成成功", first + "0001");
		}
		/**
		 * 数据最大记录+1
		 */
		DetachedCriteria dc = DetachedCriteria.forClass(HedgeNumber.class);
		dc.setProjection(Projections.max("SerialNo"));
		List<String> serialNos=(List<String>) this.hedgeNumberRepo.getHibernateTemplate().findByCriteria(dc);
		
		String first = DateUtil.doFormatDate(new Date(), "yyyyMMdd");
		if (serialNos==null||serialNos.size() == 0||serialNos.get(0)==null) {
			return new ActionResult<>(true, "保值编号生成成功", first + "0001","");
		} else {
			return new ActionResult<>(true, "保值编号生成成功", first+StringUtil.padLeft((Integer.valueOf(serialNos.get(0)) + 1)+"",4,'0'),"");
		}
	}
	
	/**
	 * 检查编号是否存在
	 * @param hedgeNumber
	 * @return
	 */
	private boolean verify(HedgeNumber hedgeNumber){
		DetachedCriteria dc=DetachedCriteria.forClass(HedgeNumber.class);
		dc.add(Restrictions.eq("No", hedgeNumber.getNo()));
		dc.setProjection(Projections.rowCount());
		List<Long> count= (List<Long>) this.hedgeNumberRepo.getHibernateTemplate().findByCriteria(dc);
		if(count==null||count.size()==0||count.get(0)==null||count.get(0)==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 检查保值方案名称是否存在
	 * @param hedgeNumber
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean verifyName(HedgeNumber hedgeNumber){
		DetachedCriteria dc=DetachedCriteria.forClass(HedgeNumber.class);
		dc.add(Restrictions.eq("Name", hedgeNumber.getName()));
		if(hedgeNumber.getId()!=null){
			dc.add(Restrictions.not(Restrictions.eq("Id", hedgeNumber.getId())));
		}
		dc.setProjection(Projections.rowCount());
		List<Long> count= (List<Long>) this.hedgeNumberRepo.getHibernateTemplate().findByCriteria(dc);
		if(count==null||count.size()==0||count.get(0)==null||count.get(0)==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 检查是否已经存在关系表
	 */
	private boolean verifyMapping(String hnId,String mId,String flag){
		String sqlLot="select count(*) from Physical.HedgeNumberMapping where HegdeNumberId='"+hnId+"' and LotId='"+mId+"'";
		String sqlPosition="select count(*) from Physical.HedgeNumberMapping where HegdeNumberId='"+hnId+"' and PositionId='"+mId+"'";
		Object obj = null;
		if("L".equals(flag)){
			obj = this.hedgeNumberRepo.ExecuteScalarSql(sqlLot);
		}else{
			obj = this.hedgeNumberRepo.ExecuteScalarSql(sqlPosition);
		}
		
		if(obj!=null){
			if(Integer.valueOf(obj.toString())>0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	
	/**
	 * 保存现货
	 */
	private void saveFutures(HedgeNumber hedgeNumber,List<Lot> lots){
		LoginInfoToken info=LoginHelper.GetLoginInfo();
		//List<Lot> lots=hedgeNumber.getLots();
		/**
		 * 先删除原有关系
		 */
		
		String delSql="Delete [Physical].[HedgeNumberMapping] Where HegdeNumberId='"+hedgeNumber.getId()+"' and LotId is not null";
		this.hedgeNumberRepo.ExecuteCorrectlySql3(delSql);
		
		if(lots!=null&&lots.size()>0){
			for (Lot lot : lots) {
				/*if(verifyMapping(hedgeNumber.getId(),lot.getId(),"L")){
					continue;
				}*/
				String sql="INSERT INTO [Physical].[HedgeNumberMapping]"+
						"(Id,Version"+
						",HegdeNumberId"+
						",LotId"+
						",LotNo"+
						",CreatedId"+
						",IsDeleted"+
						",IsHidden"+
						",CreatedBy"+
						",CreatedAt"+
						" )VALUES('"+UUID.randomUUID().toString()+"',1,'"+hedgeNumber.getId()+"','"+lot.getId()+"','"+lot.getLotNo()+"','"+info.getUserId()+"',0,0,'"+info.getName()+"','"+DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"')";
				this.hedgeNumberRepo.ExecuteCorrectlySql3(sql);
			}
		}
	}
	/**
	 * 保存期货
	 */
	private void saveSpot(HedgeNumber hedgeNumber,List<Position> positions){
		LoginInfoToken info=LoginHelper.GetLoginInfo();
		//List<Position> positions=hedgeNumber.getPositions();
		if(positions!=null&&positions.size()>0){
			/**
			 * 先删除原有关系
			 */
			
			String delSql="Delete [Physical].[HedgeNumberMapping] Where HegdeNumberId='"+hedgeNumber.getId()+"' and PositionId is not null ";;
			this.hedgeNumberRepo.ExecuteCorrectlySql3(delSql);
			
			for (Position p : positions) {
				/*if(verifyMapping(hedgeNumber.getId(),p.getId(),"P")){
					continue;
				}*/
				String sql="INSERT INTO [Physical].[HedgeNumberMapping]"+
						"(Id,Version"+
						",HegdeNumberId"+
						",PositionId"+
						",PositionNo"+
						",CreatedId"+
						",IsDeleted"+
						",IsHidden"+
						",CreatedBy"+
						",CreatedAt"+
						" )VALUES('"+UUID.randomUUID().toString()+"',1,'"+hedgeNumber.getId()+"','"+p.getId()+"','"+p.getOurRef()+"','"+info.getUserId()+"',0,0,'"+info.getName()+"','"+DateUtil.doFormatDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"')";
				this.hedgeNumberRepo.ExecuteCorrectlySql3(sql);
			}
		}
		
	}

	@Override
	public ActionResult<HedgeNumber> GetHedgeNumberById(String hNId) {
		if(StringUtils.isBlank(hNId)){
			return new ActionResult<>(false, "id is null");
		}
		HedgeNumber hn=this.hedgeNumberRepo.getOneById(hNId, HedgeNumber.class);
		ActionResult<HedgeNumber> result=new ActionResult<>();
		result.setData(hn);
		result.setSuccess(true);
		result.setMessage("ok");
		return result;
	}

	@Override
	public ActionResult<List<HedgeNumber>> GetHedgeNumbers(HedgeNumberParams hnParams) {
		
		Criteria criteria=this.hedgeNumberRepo.CreateCriteria(HedgeNumber.class);
		//dc.add(Restrictions.eq("Category", hnParams.getContractCategory()));
		RefUtil ref=new RefUtil();
		List<HedgeNumber> hns=this.hedgeNumberRepo.GetPage(criteria, hnParams.getPageSize(), hnParams.getPageIndex(), null, null, ref).getData();
		
		if(hns==null||hns.size()==0)return new ActionResult<List<HedgeNumber>>(true, "没有数据", Collections.emptyList());
		
		for (HedgeNumber hedgeNumber : hns) {
			//现货
			List<Lot> lots=hedgeNumber.getLots();
			//采购数量汇总
			BigDecimal summaryBuyQuantity=BigDecimal.ZERO;
			//销售数量汇总
			BigDecimal summarySaleQuantity=BigDecimal.ZERO;
			//sum现货采购均价
			BigDecimal sumSpotBuyAvgPrice=BigDecimal.ZERO;
			//sum现货销售均价
			BigDecimal sumSpotSaleAvgPrice=BigDecimal.ZERO;
			
			
			for (Lot lot : lots) {
				//批次计算数量
				BigDecimal compute=BigDecimal.ZERO;
				//汇总数量
				if(lot.getIsDeleted()){
					//收发货已经完成，按交付数量计算
					if(lot.getSpotDirection().equals(SpotType.Purchase)){
						summaryBuyQuantity=summaryBuyQuantity.add(lot.getQuantityDelivered());//采购数量汇总
						compute=lot.getQuantityDelivered();
					}else{
						summarySaleQuantity=summarySaleQuantity.add(lot.getQuantityDelivered());
						compute=lot.getQuantityDelivered();
					}
				}else{
					//收发货还没有完成，按批次数量计算
					if(lot.getSpotDirection().equals(SpotType.Purchase)){
						summaryBuyQuantity=summaryBuyQuantity.add(lot.getQuantity());
						compute=lot.getQuantity();
					}else{
						summarySaleQuantity=summarySaleQuantity.add(lot.getQuantity());
						compute=lot.getQuantity();
					}
				}
				//计算均价
				if(MajorType.Pricing.equals(lot.getMajorType())){
					//点价
					if(lot.getIsPriced()){
						//点价完成
						if(lot.getSpotDirection().equals(SpotType.Purchase)){
							sumSpotBuyAvgPrice=sumSpotBuyAvgPrice.add(compute.multiply(lot.getPrice()));
						}else{
							sumSpotSaleAvgPrice=sumSpotSaleAvgPrice.add(compute.multiply(lot.getPrice()));
						}
					}else if(!lot.getIsPriced()&&lot.getQuantityPriced().compareTo(BigDecimal.ZERO)>0){
						//部分点价，按已点价数量计算
						if(lot.getSpotDirection().equals(SpotType.Purchase)){
							sumSpotBuyAvgPrice=sumSpotBuyAvgPrice.add(lot.getQuantityPriced().multiply(lot.getPrice()));
						}else{
							sumSpotSaleAvgPrice=sumSpotSaleAvgPrice.add(lot.getQuantityPriced().multiply(lot.getPrice()));
						}
					}
				}else if(MajorType.Average.equals(lot.getMajorType())||MajorType.Fix.equals(lot.getMajorType())){
					//均价(已点价)或者固定价
					if(lot.getIsPriced()){
						if(lot.getSpotDirection().equals(SpotType.Purchase)){
							sumSpotBuyAvgPrice=sumSpotBuyAvgPrice.add(compute.multiply(lot.getPrice()));
						}else{
							sumSpotSaleAvgPrice=sumSpotSaleAvgPrice.add(compute.multiply(lot.getPrice()));
						}
					}
				}
				compute=BigDecimal.ZERO;
			}
			
			//现货采购均价
			BigDecimal spotBuyAvgPrice=BigDecimal.ZERO;
			//现货销售均价
			BigDecimal spotSaleAvgPrice=BigDecimal.ZERO;
			
			//现货盈亏
			BigDecimal spotProfitAndLoss=BigDecimal.ZERO;
			
			//现货采购均价 = sum(采购数量*采购价格） / 采购数量汇总
			spotBuyAvgPrice=DecimalUtil.divideForPrice(sumSpotBuyAvgPrice, summaryBuyQuantity);
			
			//现货销售均价= sum(销售数量*销售价格) / 销售数量汇总
			spotSaleAvgPrice=DecimalUtil.divideForPrice(sumSpotSaleAvgPrice, summarySaleQuantity);
			
			//现货盈亏
			spotProfitAndLoss=(spotSaleAvgPrice.subtract(spotBuyAvgPrice)).multiply(summaryBuyQuantity.compareTo(summarySaleQuantity)<=0?summaryBuyQuantity:summarySaleQuantity);
			
			//~~~~~~~~~~~~~~~~~~~~期货计算~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
			//期货买数量（汇总）
			BigDecimal summaryFuturesBuyQuantity=BigDecimal.ZERO;
			//期货卖数量（汇总）
			BigDecimal summaryyFuturesSaleQuantity=BigDecimal.ZERO;
			//期货买均价
			BigDecimal sumfuturesBuyAvgPrice=BigDecimal.ZERO;
			BigDecimal futuresBuyAvgPrice=BigDecimal.ZERO;
			//期货卖均价
			BigDecimal sumfuturesSaleAvgPrice=BigDecimal.ZERO;
			BigDecimal futuresSaleAvgPrice=BigDecimal.ZERO;
			List<Position> positions=hedgeNumber.getPositions();
			for (Position position : positions) {
				if(LS.LONG.equals(position.getLS())){
					summaryFuturesBuyQuantity=summaryFuturesBuyQuantity.add(position.getQuantity());
					sumfuturesBuyAvgPrice=sumfuturesBuyAvgPrice.add(position.getQuantity().multiply(position.getOurPrice()));
				}else{
					summaryyFuturesSaleQuantity=summaryyFuturesSaleQuantity.add(position.getQuantity().abs());
					sumfuturesSaleAvgPrice=sumfuturesSaleAvgPrice.add(position.getQuantity().abs().multiply(position.getOurPrice()));
				}
			}
			futuresBuyAvgPrice=DecimalUtil.divideForPrice(sumfuturesBuyAvgPrice, summaryFuturesBuyQuantity);
			futuresSaleAvgPrice=DecimalUtil.divideForPrice(sumfuturesSaleAvgPrice, summaryyFuturesSaleQuantity);
			
			//期货盈亏
			BigDecimal futuresProfitAndLoss=BigDecimal.ZERO;
			
			futuresProfitAndLoss=futuresSaleAvgPrice.subtract(futuresBuyAvgPrice).multiply(summaryFuturesBuyQuantity.compareTo(summaryyFuturesSaleQuantity)<=0?summaryFuturesBuyQuantity:summaryyFuturesSaleQuantity);
			
			//总盈亏
			BigDecimal summaryProfitAndLoss=BigDecimal.ZERO;
			summaryProfitAndLoss=futuresProfitAndLoss.add(spotProfitAndLoss);
			
			//~~~~~~~~~~~~~~~~~~~~敞口数量计算~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
			
			//期货买数量（汇总）
			BigDecimal exposureBuyQuantity=BigDecimal.ZERO;
			//期货卖数量（汇总）
			BigDecimal exposureSaleQuantity=BigDecimal.ZERO;
			
			exposureBuyQuantity=summaryBuyQuantity.subtract(summaryyFuturesSaleQuantity);
			
			exposureSaleQuantity=summarySaleQuantity.subtract(summaryFuturesBuyQuantity);
			
			hedgeNumber.setSpotProfitAndLoss(spotProfitAndLoss);
			hedgeNumber.setFuturesProfitAndLoss(futuresProfitAndLoss);
			hedgeNumber.setSummaryProfitAndLoss(summaryProfitAndLoss);
			hedgeNumber.setSummaryBuyQuantity(summaryBuyQuantity);
			hedgeNumber.setSummarySaleQuantity(summarySaleQuantity);
			hedgeNumber.setSpotBuyAvgPrice(spotBuyAvgPrice);
			hedgeNumber.setSpotSaleAvgPrice(spotSaleAvgPrice);
			hedgeNumber.setSummaryFuturesBuyQuantity(summaryFuturesBuyQuantity);
			hedgeNumber.setSummaryyFuturesSaleQuantity(summaryyFuturesSaleQuantity);
			hedgeNumber.setFuturesBuyAvgPrice(futuresBuyAvgPrice);
			hedgeNumber.setFuturesSaleAvgPrice(futuresSaleAvgPrice);
			hedgeNumber.setExposureBuyQuantity(exposureBuyQuantity);
			hedgeNumber.setExposureSaleQuantity(exposureSaleQuantity);
		}
		
		ActionResult<List<HedgeNumber>> result=new ActionResult<>();
		result.setData(hns);
		result.setSuccess(true);
		result.setMessage("ok");
		result.setTotal(ref.getTotal());
		return result;
	}

	public ActionResult<String> Delete(String hnId) {
		ActionResult<String> result=new ActionResult<>();
		try {
			this.hedgeNumberRepo.PhysicsDelete(hnId, HedgeNumber.class);
			result.setSuccess(true);
			result.setMessage(MessageCtrm.DeleteSuccess);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMessage(MessageCtrm.DeleteFaile);
		}
		
		return result;
	}
}
