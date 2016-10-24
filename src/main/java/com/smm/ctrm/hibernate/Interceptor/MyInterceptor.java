package com.smm.ctrm.hibernate.Interceptor;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Named;

import org.hibernate.EmptyInterceptor;
import org.hibernate.FlushMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.LotSign;
import com.smm.ctrm.dto.res.LotTag;

@Named
@Transactional
public class MyInterceptor extends EmptyInterceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9043227936042389669L;

	@Resource
	HibernateRepository<LotSign> lotSignRepo;

	@Resource
	HibernateRepository<Lot> lotRepo;

	@PostConstruct
	public void init() {
		StaticDelegateInterceptor.setInterceptor(this);
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (entity != null && entity instanceof Lot) {
			Lot lot = (Lot) entity;
			LotSign lotSign = getLotSignByLot(lot); // 获取lot的打标记时间信息
			updateLotSignForSave(lot, lotSign); // 更新标记时间
		}
		return super.onSave(entity, id, state, propertyNames, types);
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {

		if (entity != null && entity instanceof Lot) {
			Lot lot = (Lot) entity;
			LotSign lotSign = getLotSignByLot(lot); // 获取lot的打标记时间信息
			LotTag lotTag = getDatabaseLotTagInfo(lot.getId()); // 获取数据库中存储的lot标记信息
			if (lot.getUpdatedAt() != null) { // lot 更新
				updateLotSignForUpdate(lotTag, lot, lotSign); // 更新标记时间
			} else { // lot新增
				updateLotSignForSave(lot, lotSign); // 更新标记时间
			}
		}
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}

	/** 获取lot的打标记时间信息
	 * @param lot
	 * @return
	 */
	private LotSign getLotSignByLot(Lot lot) {
		FlushMode flushMode = lotRepo.getFlushMode();
		lotRepo.setFlushMode(FlushMode.MANUAL);
		LotSign lotSign = lotSignRepo.GetQueryable(LotSign.class)
				.where(DetachedCriteria.forClass(LotSign.class).add(Restrictions.eq("LotId", lot.getId())))
				.firstOrDefault();
		lotRepo.setFlushMode(flushMode);
		if (lotSign == null) {
			lotSign = new LotSign();
			lotSign.setLotId(lot.getId());
			lotSign.setFullNo(lot.getFullNo());
			lotSign.setCustomerId(lot.getCustomerId());
			lotSign.setTradeDate(lot.getTradeDate());
		}
		return lotSign;
	}

	/** 获取数据库中存储的lot标机信息
	 * @param lotId
	 * @return
	 */
	private LotTag getDatabaseLotTagInfo(String lotId) {
		FlushMode flushMode = lotRepo.getFlushMode();
		lotRepo.setFlushMode(FlushMode.MANUAL);
		LotTag lotTag = lotRepo.getCurrentSession()
				.createQuery(
						"select new com.smm.ctrm.dto.res.LotTag(IsPriced, IsDelivered, IsHedged, IsFunded, IsInvoiced) from Lot lot where Id=:id",
						LotTag.class)
				.setParameter("id", lotId).getResultList().get(0);
		lotRepo.setFlushMode(flushMode);
		return lotTag;
	}

	/** 更新标记时间
	 * @param lot
	 * @param lotSign
	 */
	private void updateLotSignForSave(Lot lot, LotSign lotSign) {
		Date now = new Date();
		if (lot.getIsPriced()) {
			lotSign.setIsPricedDate(now);
		}
		if (lot.getIsDelivered()) {
			lotSign.setIsDeliveredDate(now);
		}

		if (lot.getIsHedged()) {
			lotSign.setIsHedgedDate(now);
		}

		if (lot.getIsFunded()) {
			lotSign.setIsFundedDate(now);
		}

		if (lot.getIsInvoiced()) {
			lotSign.setIsInvoicedDate(now);
		}
		lotSignRepo.SaveOrUpdate(lotSign);
	}

	/** 更新标记时间
	 * @param lotTag
	 * @param lot
	 * @param lotSign
	 */
	private void updateLotSignForUpdate(LotTag lotTag, Lot lot, LotSign lotSign) {
		Date now = new Date();
		if (!lotTag.getIsPriced().equals(lot.getIsPriced())) {
			if (lot.getIsPriced()) {
				lotSign.setIsPricedDate(now);
			} else {
				lotSign.setIsPricedDate(null);
			}
		}
		if (!lotTag.getIsDelivered().equals(lot.getIsDelivered())) {
			if (lot.getIsDelivered()) {
				lotSign.setIsDeliveredDate(now);
			} else {
				lotSign.setIsDeliveredDate(null);
			} 
		}

		if (!lotTag.getIsHedged().equals(lot.getIsHedged())) {
			if (lot.getIsHedged()) {
				lotSign.setIsHedgedDate(now);
			} else {
				lotSign.setIsHedgedDate(null);
			}
		}

		if (!lotTag.getIsFunded().equals(lot.getIsFunded())) {
			if (lot.getIsFunded()) {
				lotSign.setIsFundedDate(now);
			} else {
				lotSign.setIsFundedDate(null);
			}
		}

		if (!lotTag.getIsInvoiced().equals(lot.getIsInvoiced())) {
			if (lot.getIsInvoiced()) {
				lotSign.setIsInvoicedDate(now);
			} else {
				lotSign.setIsInvoicedDate(null);
			}
		}
		lotSignRepo.getCurrentSession().merge(lotSign);
//		lotSignRepo.Flush();
	}

}
