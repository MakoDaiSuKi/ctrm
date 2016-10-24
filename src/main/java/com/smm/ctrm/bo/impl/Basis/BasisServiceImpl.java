package com.smm.ctrm.bo.impl.Basis;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.BasisService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Account;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Basis.Bvi2Sm;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.GlobalSet;
import com.smm.ctrm.domain.Basis.Instrument;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.Basis.Origin;
import com.smm.ctrm.domain.Basis.Period;
import com.smm.ctrm.domain.Basis.Preset;
import com.smm.ctrm.domain.Basis.PricingShortcut;
import com.smm.ctrm.domain.Basis.Product;
import com.smm.ctrm.domain.Basis.Spec;
import com.smm.ctrm.domain.Basis.StorageTemplate;
import com.smm.ctrm.domain.Basis.Warehouse;
import com.smm.ctrm.domain.Maintain.Calendar;
import com.smm.ctrm.domain.Maintain.Exchange;
import com.smm.ctrm.domain.Maintain.LME;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class BasisServiceImpl implements BasisService {

	private static final Logger logger = Logger.getLogger(BasisServiceImpl.class);

	@Autowired
	private HibernateRepository<GlobalSet> globalSetHibernateRepository;

	@Autowired
	private HibernateRepository<Preset> presetHibernateRepository;

	@Autowired
	private HibernateRepository<Account> accountHibernateRepository;

	@Autowired
	private HibernateRepository<Period> periodHibernateRepository;

	@Autowired
	private HibernateRepository<PricingShortcut> pricingShortcutHibernateRepository;

	@Autowired
	private HibernateRepository<Commodity> commodityHibernateRepository;

	@Autowired
	private HibernateRepository<Brand> brandHibernateRepository;

	@Autowired
	private HibernateRepository<Spec> specHibernateRepository;

	@Autowired
	private HibernateRepository<Origin> originHibernateRepository;

	@Autowired
	private HibernateRepository<Calendar> calendarHibernateRepository;

	@Autowired
	private HibernateRepository<LME> lmeHibernateRepository;

	@Autowired
	private HibernateRepository<Warehouse> warehouseHibernateRepository;

	@Autowired
	private HibernateRepository<Instrument> instrumentHibernateRepository;

	@Autowired
	private HibernateRepository<Market> marketHibernateRepository;

	@Autowired
	private HibernateRepository<Exchange> exchangeHibernateRepository;

	@Autowired
	private HibernateRepository<Product> productHibernateRepository;

	@Autowired
	private HibernateRepository<Bvi2Sm> bvi2SmHibernateRepository;

	@Autowired
	private HibernateRepository<StorageTemplate> storageTemplateHibernateRepository;

	@Override
	public ActionResult<GlobalSet> MyGlobalSet(String orgId) {

		DetachedCriteria where = DetachedCriteria.forClass(GlobalSet.class);
		where.add(Restrictions.eq("OrgId", orgId));

		List<GlobalSet> list = this.globalSetHibernateRepository.GetQueryable(GlobalSet.class).where(where).toList();

		if (list != null && list.size() > 0)
			return new ActionResult<>(true, null, list.get(0));
		return new ActionResult<>(true, null, null);
	}

	@Override
	public ActionResult<GlobalSet> SaveGlobalSet(GlobalSet globalSet) {

		try {

			this.globalSetHibernateRepository.SaveOrUpdate(globalSet);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public ActionResult<List<Preset>> Presets() {

		DetachedCriteria where = DetachedCriteria.forClass(Preset.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Preset> list = this.presetHibernateRepository.GetQueryable(Preset.class).where(where).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Preset>> BackPresets() {

		return new ActionResult<>(true, null, this.presetHibernateRepository.GetList(Preset.class));
	}

	@Override
	public ActionResult<Preset> SavePreset(Preset preset) {

		try {

			this.presetHibernateRepository.SaveOrUpdate(preset);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<Preset> DeletePreset(String id) {

		try {

			this.presetHibernateRepository.PhysicsDelete(id, Preset.class);

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<List<Account>> Accounts() {

		DetachedCriteria where = DetachedCriteria.forClass(Account.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Account> list = this.accountHibernateRepository.GetQueryable(Account.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Account>> BackAccounts() {

		List<Account> list = this.accountHibernateRepository.GetQueryable(Account.class)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<Account> SaveAccount(Account param) {

		try {

			this.accountHibernateRepository.SaveOrUpdate(param);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<String> DeleteAccount(String id) {

		try {

			this.accountHibernateRepository.PhysicsDelete(id, Account.class);

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<List<Period>> Periods() {

		List<Period> periods = this.periodHibernateRepository.GetList(Period.class);

		return new ActionResult<>(true, null, periods);
	}

	@Override
	public ActionResult<List<Period>> BackPeriods() {

		DetachedCriteria where = DetachedCriteria.forClass(Period.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Period> list = this.periodHibernateRepository.GetQueryable(Period.class).where(where).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<Period> SavePeriod(Period param) {

		try {

			this.periodHibernateRepository.SaveOrUpdate(param);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<String> DeletePeriod(String id) {

		try {

			this.periodHibernateRepository.PhysicsDelete(id, Period.class);

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<List<PricingShortcut>> PricingTypes() {

		List<PricingShortcut> list = this.pricingShortcutHibernateRepository.GetQueryable(PricingShortcut.class)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<PricingShortcut>> BackPricingTypes() {

		DetachedCriteria where = DetachedCriteria.forClass(PricingShortcut.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<PricingShortcut> list = this.pricingShortcutHibernateRepository.GetQueryable(PricingShortcut.class)
				.where(where).OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<PricingShortcut> SavePricingType(PricingShortcut pricingType) {

		try {

			this.pricingShortcutHibernateRepository.SaveOrUpdate(pricingType);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<String> DeletePricingType(String id) {

		try {

			this.pricingShortcutHibernateRepository.PhysicsDelete(id, PricingShortcut.class);

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<List<Commodity>> Commoditys() {

		List<Commodity> list = this.commodityHibernateRepository.GetQueryable(Commodity.class)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Commodity>> BackCommoditys() {

		DetachedCriteria where = DetachedCriteria.forClass(Commodity.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Commodity> list = this.commodityHibernateRepository.GetQueryable(Commodity.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<Commodity> SaveCommodity(Commodity commodity) {

		try {

			// 检查重复
			DetachedCriteria where = DetachedCriteria.forClass(Commodity.class);
			where.add(Restrictions.ne("Id", commodity.getId()));
			where.add(Restrictions.or(Restrictions.eq("Name", commodity.getName()),
					Restrictions.eq("Code", commodity.getCode())));

			List<Commodity> list = this.commodityHibernateRepository.GetQueryable(Commodity.class).where(where)
					.toList();

			if (list != null && list.size() > 0)
				throw new Exception(MessageCtrm.DuplicatedName);

			this.commodityHibernateRepository.SaveOrUpdate(commodity);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<String> DeleteCommodity(String id) {

		try {

			this.commodityHibernateRepository.PhysicsDelete(id, Commodity.class);

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<List<Brand>> Brands() {

		List<Brand> list = this.brandHibernateRepository.GetQueryable(Brand.class).OrderBy(Order.desc("OrderIndex"))
				.toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Brand>> BackBrands() {

		DetachedCriteria where = DetachedCriteria.forClass(Brand.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Brand> list = this.brandHibernateRepository.GetQueryable(Brand.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<Brand> SaveBrand(Brand brand) {

		try {

			this.brandHibernateRepository.SaveOrUpdate(brand);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<String> DeleteBrand(String id) {

		try {

			this.brandHibernateRepository.PhysicsDelete(id, Brand.class);

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<List<Spec>> Specs() {

		DetachedCriteria where = DetachedCriteria.forClass(Spec.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Spec> list = this.specHibernateRepository.GetQueryable(Spec.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Spec>> BackSpecs() {

		List<Spec> list = this.specHibernateRepository.GetQueryable(Spec.class).OrderBy(Order.desc("OrderIndex"))
				.toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<Spec> SaveSpec(Spec spec) {

		try {

			this.specHibernateRepository.SaveOrUpdate(spec);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<String> DeleteSpec(String id) {

		try {

			this.specHibernateRepository.PhysicsDelete(id, Spec.class);

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<List<Origin>> Origins() {

		List<Origin> list = this.originHibernateRepository.GetQueryable(Origin.class).OrderBy(Order.desc("OrderIndex"))
				.toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Origin>> BackOrigins() {

		DetachedCriteria where = DetachedCriteria.forClass(Origin.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Origin> list = this.originHibernateRepository.GetQueryable(Origin.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<Origin> SaveOrigin(Origin origin) {

		try {

			this.originHibernateRepository.SaveOrUpdate(origin);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<String> DeleteOrigin(String id) {

		try {

			this.originHibernateRepository.PhysicsDelete(id, Origin.class);

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<List<Warehouse>> Warehouses() {

		DetachedCriteria where = DetachedCriteria.forClass(Warehouse.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Warehouse> list = this.warehouseHibernateRepository.GetQueryable(Warehouse.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Warehouse>> BackWarehouses() {

		List<Warehouse> list = this.warehouseHibernateRepository.GetQueryable(Warehouse.class)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<Warehouse> SaveWarehouse(Warehouse warehouse) {

		try {

			this.warehouseHibernateRepository.SaveOrUpdate(warehouse);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<String> DeleteWarehouse(String id) {

		try {

			this.warehouseHibernateRepository.PhysicsDelete(id, Warehouse.class);

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<List<Instrument>> Instruments() {

		return new ActionResult<>(true, null, this.instrumentHibernateRepository.GetList(Instrument.class));
	}

	@Override
	public ActionResult<List<Instrument>> BackInstruments() {

		DetachedCriteria where = DetachedCriteria.forClass(Instrument.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Instrument> list = this.instrumentHibernateRepository.GetQueryable(Instrument.class).where(where).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<Instrument> SaveInstrument(Instrument instrument) {

		try {

			this.instrumentHibernateRepository.SaveOrUpdate(instrument);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<String> DeleteInstrument(String id) {

		try {

			this.instrumentHibernateRepository.PhysicsDelete(id, Instrument.class);

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<List<Market>> Markets() {

		List<Market> list = this.marketHibernateRepository.GetQueryable(Market.class).OrderBy(Order.desc("OrderIndex"))
				.toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Market>> BackMarkets() {

		DetachedCriteria where = DetachedCriteria.forClass(Market.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Market> list = this.marketHibernateRepository.GetQueryable(Market.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Market>> SpotMarkets() {
		List<Market> list = this.marketHibernateRepository.GetQueryable(Market.class).OrderBy(Order.desc("OrderIndex"))
				.toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Market>> BackSpotMarkets() {
		DetachedCriteria where = DetachedCriteria.forClass(Market.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Market> list = this.marketHibernateRepository.GetQueryable(Market.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Market>> ForwardMarkets() {
		List<Market> list = this.marketHibernateRepository.GetQueryable(Market.class).OrderBy(Order.desc("OrderIndex"))
				.toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Market>> BackForwardMarkets() {
		DetachedCriteria where = DetachedCriteria.forClass(Market.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Market> list = this.marketHibernateRepository.GetQueryable(Market.class).where(where)
				.OrderBy(Order.desc("OrderIndex")).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<Market> SaveMarket(Market market) {

		try {

			this.marketHibernateRepository.SaveOrUpdate(market);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<String> DeleteMarket(String id) {

		try {

			this.marketHibernateRepository.PhysicsDelete(id, Market.class);

			return new ActionResult<>(true, "删除成功");
		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<List<Exchange>> Exchanges() {

		return new ActionResult<>(true, null, this.exchangeHibernateRepository.GetList(Exchange.class));
	}

	@Override
	public ActionResult<List<Exchange>> BackExchanges() {

		DetachedCriteria where = DetachedCriteria.forClass(Exchange.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Exchange> list = this.exchangeHibernateRepository.GetQueryable(Exchange.class).where(where).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<Exchange> SaveExchange(Exchange exchange) {

		try {

			this.exchangeHibernateRepository.SaveOrUpdate(exchange);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<String> DeleteExchange(String id) {

		try {

			this.exchangeHibernateRepository.PhysicsDelete(id, Exchange.class);

			return new ActionResult<>(true, "删除成功");
		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<List<Calendar>> Calendars() {

		return new ActionResult<>(true, null, this.calendarHibernateRepository.GetList(Calendar.class));
	}

	@Override
	public ActionResult<List<Calendar>> BackCalendars() {

		DetachedCriteria where = DetachedCriteria.forClass(Calendar.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Calendar> list = this.calendarHibernateRepository.GetQueryable(Calendar.class).where(where).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<Calendar> SaveCalendar(Calendar calendar) {

		try {

			this.calendarHibernateRepository.SaveOrUpdate(calendar);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<String> DeleteCalendar(String id) {

		try {

			this.calendarHibernateRepository.PhysicsDelete(id, Calendar.class);

			return new ActionResult<>(true, "删除成功");
		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<List<LME>> LMEs() {

		return new ActionResult<>(true, null, this.lmeHibernateRepository.GetList(LME.class));
	}

	@Override
	public ActionResult<List<LME>> BackLMEs() {

		DetachedCriteria where = DetachedCriteria.forClass(LME.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<LME> list = this.lmeHibernateRepository.GetQueryable(LME.class).where(where).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<LME> SaveLME(LME lme) {

		try {

			this.lmeHibernateRepository.SaveOrUpdate(lme);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<String> DeleteLME(String id) {

		try {

			this.lmeHibernateRepository.PhysicsDelete(id, LME.class);

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public void SaveProduct(Product product) {

		this.productHibernateRepository.SaveOrUpdate(product);

	}

	@Override
	public void DeleteProduct(String id) {

		this.productHibernateRepository.PhysicsDelete(id, Product.class);

	}

	@Override
	public ActionResult<Bvi2Sm> Bvi2Sm() {

		return new ActionResult<>(true, null, this.bvi2SmHibernateRepository.GetQueryable(Bvi2Sm.class).firstOrDefault());
	}

	/// 通过字段查询数据ID
	public ActionResult<String> GetObjectByName(Map<String, String> dicts) {
		String sql = MessageFormat.format("select Id from {0} where {1} = {2}", dicts.get("TableName").substring(dicts.get("TableName").indexOf(".")+1),
				dicts.get("ColumnName"), "'"+dicts.get("DataValue")+"'");
		String id = bvi2SmHibernateRepository.getCurrentSession().createQuery(sql, String.class).uniqueResult();
		ActionResult<String> tempVar2 = new ActionResult<String>();
		tempVar2.setSuccess(true);
		tempVar2.setData(id);
		return tempVar2;
	}

	/// <summary>
	/// 获取产品入库字段模板
	/// </summary>
	/// <returns></returns>
	public ActionResult<List<StorageTemplate>> LoadStorageTemplateData() {

		List<StorageTemplate> templates = storageTemplateHibernateRepository.GetList(StorageTemplate.class);

		ActionResult<List<StorageTemplate>> tempVar2 = new ActionResult<List<StorageTemplate>>();
		tempVar2.setSuccess(true);
		tempVar2.setData(templates);
		return tempVar2;

	}

	/**
	 * #region 产品入库模板 创建产品入库模板字段数据
	 * 
	 * @param storage_template_data
	 * @return
	 */

	public ActionResult<String> CrerateStorageTemplateData(List<StorageTemplate> storage_template_data) {

		for (StorageTemplate storageTemplate : storage_template_data) {
			storageTemplateHibernateRepository.SaveOrUpdate(storageTemplate);
		}

		return new ActionResult<>(true, "产品入库模板数据创建成功", null);

	}

}
