package com.smm.ctrm.bo.impl.Report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Report.DailyReportService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.LotSign;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.Pricing;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.apiClient.DailyReportParams;
import com.smm.ctrm.dto.res.ReceiptShipDailyReport.DailyReport;
import com.smm.ctrm.dto.res.ReceiptShipDailyReport.FundReport;
import com.smm.ctrm.dto.res.ReceiptShipDailyReport.InvoiceReport;
import com.smm.ctrm.dto.res.ReceiptShipDailyReport.LotReport;
import com.smm.ctrm.dto.res.ReceiptShipDailyReport.PositionReport;
import com.smm.ctrm.dto.res.ReceiptShipDailyReport.PricingReport;
import com.smm.ctrm.dto.res.ReceiptShipDailyReport.ReceiptShipReport;
import com.smm.ctrm.hibernate.DataSource.CTRMOrg;
import com.smm.ctrm.hibernate.DataSource.DataSourceContextHolder;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.JSONUtil;

@Service
public class DailyReportServiceImpl implements DailyReportService {
	@Resource
	HibernateRepository<Lot> lotRepo;

	@Resource
	HibernateRepository<Pricing> pricingRepo;

	@Resource
	HibernateRepository<ReceiptShip> receiptShipRepo;

	@Resource
	HibernateRepository<Position> positionRepo;

	@Resource
	HibernateRepository<LotReport> lotReportRepo;

	@Resource
	HibernateRepository<PricingReport> pricingReportRepo;

	@Resource
	HibernateRepository<ReceiptShipReport> receiptShipReportRepo;

	@Resource
	HibernateRepository<PositionReport> positionReportRepo;

	@Resource
	HibernateRepository<InvoiceReport> invoiceReportRepo;

	@Resource
	HibernateRepository<FundReport> fundReportRepo;

	@Resource
	HibernateRepository<LotSign> lotSignRepo;

	@Value("#{ctrm['folder.dailyReport']}")
	private String jsonFolder;

	private final String isShow = "Y";

	@Override
	public DailyReport getDailyReport(DailyReportParams dailyReportParams) throws IOException {

		String beginDate = DateFormatUtils.format(dailyReportParams.getBeginDate(), "yyyy-MM-dd");
		String todayDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		if (beginDate.equalsIgnoreCase(todayDate)) {
			return getDailyReportByDB(dailyReportParams);
		} else {
			String Flag = dailyReportParams.getFlag();
			String jsonFolder = getJsonFolder(dailyReportParams.getBeginDate());
			String JsonPath = getJsonFile(jsonFolder, Flag.substring(0, 1), dailyReportParams.getBeginDate());
			File file = new File(JsonPath);
			JsonPath = getJsonFileSaas(jsonFolder, Flag.substring(0, 1), dailyReportParams.getBeginDate());
			// 此方法用于过度，SAAS模式
			if (!file.exists()) {
				file = new File(JsonPath);
			}
			if (file.exists() && file.isFile()) {
				return getDailyReportByJson(dailyReportParams, file.getAbsolutePath());
			} else {
				return new DailyReport();
			}
		}

	}

	@Override
	public void clearSession() {
		lotRepo.Clear();
	}

	/**
	 * 数据库读取日报
	 * 
	 * @param dailyReportParams
	 * @return
	 */
	private DailyReport getDailyReportByDB(DailyReportParams dailyReportParams) {
		String Flag = dailyReportParams.getFlag();
		DailyReport report = new DailyReport();
		// List<LotSign> lotSignFromSignList = getTagChangedLotInToday(Flag);
		LotChangedToday changed = getLotChangedToday(Flag);
		if (Flag.substring(0, 1).equalsIgnoreCase(DailyReport.SELL)) {
			if (Flag.substring(1, 2).equalsIgnoreCase(isShow)) {
				report.setLotReportList(getTodayLotReports(DailyReport.SELL, dailyReportParams.getBeginDate(),
						dailyReportParams.getEndDate(), changed));
			}
			if (Flag.substring(2, 3).equalsIgnoreCase(isShow)) {
				report.setPricingReportList(getTodayPriceReport(DailyReport.SELL, dailyReportParams.getBeginDate(),
						dailyReportParams.getEndDate()));
			}
			if (Flag.substring(3, 4).equalsIgnoreCase(isShow)) {
				report.setReceiptShipReportList(getReceiptShipReports(DailyReport.SELL,
						dailyReportParams.getBeginDate(), dailyReportParams.getEndDate()));
			}
			if (Flag.substring(4, 5).equalsIgnoreCase(isShow)) {
				// report.setPositionReportList(getPositionReports(DailyReport.SELL,
				// dailyReportParams.getBeginDate(),
				// dailyReportParams.getEndDate()));
			}
			if (Flag.substring(5, 6).equalsIgnoreCase(isShow)) {
				report.setInvoioceReportList(getInvoiceReports(DailyReport.SELL, dailyReportParams.getBeginDate(),
						dailyReportParams.getEndDate()));
			}
			if (Flag.substring(6, 7).equalsIgnoreCase(isShow)) {
				report.setFundReportList(getFundReports(DailyReport.SELL, dailyReportParams.getBeginDate(),
						dailyReportParams.getEndDate()));
			}
		} else if (Flag.substring(0, 1).equalsIgnoreCase(DailyReport.BUY)) {
			if (Flag.substring(1, 2).equalsIgnoreCase(isShow)) {
				report.setLotReportList(getTodayLotReports(DailyReport.BUY, dailyReportParams.getBeginDate(),
						dailyReportParams.getEndDate(), changed));
			}
			if (Flag.substring(2, 3).equalsIgnoreCase(isShow)) {
				report.setPricingReportList(getTodayPriceReport(DailyReport.BUY, dailyReportParams.getBeginDate(),
						dailyReportParams.getEndDate()));
			}
			if (Flag.substring(3, 4).equalsIgnoreCase(isShow)) {
				report.setReceiptShipReportList(getReceiptShipReports(DailyReport.BUY, dailyReportParams.getBeginDate(),
						dailyReportParams.getEndDate()));
			}
			if (Flag.substring(4, 5).equalsIgnoreCase(isShow)) {
				// report.setPositionReportList(getPositionReports(DailyReport.BUY,
				// dailyReportParams.getBeginDate(),
				// dailyReportParams.getEndDate()));
			}
			if (Flag.substring(5, 6).equalsIgnoreCase(isShow)) {
				report.setInvoioceReportList(getInvoiceReports(DailyReport.BUY, dailyReportParams.getBeginDate(),
						dailyReportParams.getEndDate()));
			}
			if (Flag.substring(6, 7).equalsIgnoreCase(isShow)) {
				report.setFundReportList(getFundReports(DailyReport.BUY, dailyReportParams.getBeginDate(),
						dailyReportParams.getEndDate()));
			}
		}
		return report;
	}

	/**
	 * @param sell
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	private List<FundReport> getFundReports(String Flag, Date beginDate, Date endDate) {
		StringBuilder hql = new StringBuilder();
		hql.append(
				"select new com.smm.ctrm.dto.res.ReceiptShipDailyReport.FundReport(fund.Legal.Name, fund.Lot.FullNo, fund.Customer.Name, fund.Amount, "
						+ "fund.Currency, fund.Lot.Quantity, fund.Lot.QuantityDelivered, fund.Lot.Final, fund.Lot.IsDelivered, fund.Lot.AmountFunded, fund.Lot.AmountFunded, fund.TradeDate)")
				.append("from Fund fund ").append("left join fund.Legal ")
				.append("left join fund.Customer ").append("left join fund.Lot ")
				.append("where fund.TradeDate >= :beginDate and fund.TradeDate <= :endDate ")
				.append("and fund.Lot.SpotDirection = :Flag");
		Map<String, Object> params = new HashMap<>();
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("Flag", Flag);

		List<FundReport> fundReportList = fundReportRepo.ExecuteHql(hql.toString(), params, FundReport.class);
		for (FundReport fundReport : fundReportList) {
			fundReport.setLotAmount(
					(fundReport.getIsDelivered() ? fundReport.getLotQuantityDelivered() : fundReport.getLotQuantity())
							.multiply(fundReport.getFinal()));
		}
		return fundReportList;
	}

	/**
	 * @param sell
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	private List<InvoiceReport> getInvoiceReports(String Flag, Date beginDate, Date endDate) {
		StringBuilder hql = new StringBuilder();
		hql.append(
				"select new com.smm.ctrm.dto.res.ReceiptShipDailyReport.InvoiceReport(invoice.InvoiceNo, invoice.Lot.FullNo, invoice.Customer.Name, invoice.Lot.Quantity, invoice.Lot.QuantityDelivered, invoice.Quantity, invoice.Lot.QuantityInvoiced, invoice.Price, invoice.AmountNotional, invoice.DueAmount, invoice.TradeDate)")
				.append("from Invoice invoice ").append("left join invoice.Lot ").append("left join invoice.Customer ")
				.append("where invoice.TradeDate >= :beginDate and invoice.TradeDate <= :endDate ")
				.append("and invoice.Lot.SpotDirection = :Flag");

		Map<String, Object> params = new HashMap<>();
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("Flag", Flag);

		List<InvoiceReport> invoiceReportList = invoiceReportRepo.ExecuteHql(hql.toString(), params,
				InvoiceReport.class);
		return invoiceReportList;
	}

	/**
	 * Json文件读取日报
	 * 
	 * @param dailyReportParams
	 * @param jsonPath
	 * @return
	 * @throws IOException
	 */
	private DailyReport getDailyReportByJson(DailyReportParams dailyReportParams, String jsonPath) throws IOException {
		String jsonString = txt2String(jsonPath);
		String Flag = dailyReportParams.getFlag();
		DailyReport dailyReport = JSONUtil.doConvertStringToT(jsonString, DailyReport.class);

		if (!Flag.substring(1, 2).equalsIgnoreCase(isShow)) {
			dailyReport.setLotReportList(new ArrayList<>());
		}
		if (!Flag.substring(2, 3).equalsIgnoreCase(isShow)) {
			dailyReport.setPricingReportList(new ArrayList<>());
		}
		if (!Flag.substring(3, 4).equalsIgnoreCase(isShow)) {
			dailyReport.setReceiptShipReportList(new ArrayList<>());
		}
		if (!Flag.substring(4).equalsIgnoreCase(isShow)) {
			dailyReport.setPositionReportList(new ArrayList<>());
		}
		return dailyReport;
	}

	/**
	 * @param Flag
	 * @return
	 */
	private List<LotReport> getTodayLotReports(String Flag, Date beginDate, Date endDate, LotChangedToday changed) {
		StringBuilder hql = new StringBuilder();
		hql.append("select new com.smm.ctrm.dto.res.ReceiptShipDailyReport.LotReport(")
				.append("lot.Id, lot.Legal.Name, lot.Commodity.Name, lot.Spec.Name, lot.BrandNames, lot.Contract.RuleWareHouseNames, lot.Customer.Name, lot.FullNo, lot.Final, lot.Quantity, lot.QuantityDelivered, lot.Contract.TransactionType, lot.TradeDate, lot.CreatedAt, '') ")
				.append("from Lot lot ").append("left join lot.Legal ").append("left join lot.Commodity ")
				.append("left join lot.Spec ").append("left join lot.Customer ")
				.append("left join lot.Contract ")
				.append("where lot.CreatedAt >= :beginDate and lot.CreatedAt <= :endDate ")
				.append("and lot.SpotDirection = :Flag ");
		Map<String, Object> params = new HashMap<>();
		List<String> changedLotId = changed.getAllLotId();
		if (changedLotId.size() > 0) {
			hql.append("or lot.Id in :lotIdList");
			params.put("lotIdList", changedLotId);
		}
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("Flag", Flag);
		List<LotReport> lotReportList = lotReportRepo.ExecuteHql(hql.toString(), params, LotReport.class);
		Date todayStart = DateUtil.startOfTodDay(new Date());
		Date todayEnd = DateUtil.endOfTodDay(new Date());
		for (LotReport lotReport : lotReportList) {
			lotReport.setTodayChange("");
			if (inToday(lotReport.getCreatedAt(), todayStart, todayEnd)) {
				lotReport.setTodayChange(" 新");
			}
			if (changed.getPricedList().contains(lotReport.getId().toLowerCase())) {
				lotReport.setTodayChange(lotReport.getTodayChange() + " 点");
			}
			if (changed.getDeliveredList().contains(lotReport.getId().toLowerCase())) {
				lotReport.setTodayChange(lotReport.getTodayChange() + " 货");
			}
			if (changed.getHedgedList().contains(lotReport.getId().toLowerCase())) {
				lotReport.setTodayChange(lotReport.getTodayChange() + " 保");
			}
			if (changed.getInvoicedList().contains(lotReport.getId().toLowerCase())) {
				lotReport.setTodayChange(lotReport.getTodayChange() + " 票");
			}
			if (changed.getFundedList().contains(lotReport.getId().toLowerCase())) {
				lotReport.setTodayChange(lotReport.getTodayChange() + " 资");
			}
			lotReport.setTodayChange(lotReport.getTodayChange().trim());
		}
		return lotReportList;
	}

	private boolean inToday(Date myDate, Date startDate, Date endDate) {
		if (myDate.compareTo(startDate) >= 0 && myDate.compareTo(endDate) <= 0) {
			return true;
		}
		return false;
	}

	private LotChangedToday getLotChangedToday(String Flag) {
		Date startDate = DateUtil.startOfTodDay(new Date());
		Date endDate = DateUtil.endOfTodDay(new Date());
 		LotChangedToday changed = new LotChangedToday();
		String sellOrBuy = Flag.substring(0, 1);
		if (Flag.substring(3, 4).equalsIgnoreCase(isShow)) {
			changed.setDeliveredList(lotRepo.getCurrentSession()
					.createQuery(
							"select lower(t.LotId) from ReceiptShip t left join t.Lot where (t.CreatedAt >= :startDate and t.CreatedAt <= :endDate) and t.Lot.SpotDirection = :flag ",
							String.class)
					.setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("flag", sellOrBuy).getResultList());
		}
		if (Flag.substring(6, 7).equalsIgnoreCase(isShow)) {
			changed.setFundedList(lotRepo.getCurrentSession()
					.createQuery(
							"select lower(t.LotId) from Fund t left join t.Lot where (t.CreatedAt >= :startDate and t.CreatedAt <= :endDate) and t.Lot.SpotDirection = :flag ",
							String.class)
					.setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("flag", sellOrBuy).getResultList());
		}
		// changed.setHedgedList(lotRepo.getCurrentSession()
		// .createQuery("select lower(LotId) from Position t where (CreatedAt >=
		// :startDate and CreatedAt <= :endDate) "
		// , String.class)
		// .setParameter("startDate", startDate).setParameter("endDate",
		// endDate).getResultList());
		if (Flag.substring(5, 6).equalsIgnoreCase(isShow)) {
			changed.setInvoicedList(lotRepo.getCurrentSession()
					.createQuery(
							"select lower(t.LotId) from Invoice t left join t.Lot where (t.CreatedAt >= :startDate and t.CreatedAt <= :endDate) and t.Lot.SpotDirection = :flag ",
							String.class)
					.setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("flag", sellOrBuy).getResultList());
		}
		if (Flag.substring(2, 3).equalsIgnoreCase(isShow)) {
			changed.setPricedList(lotRepo.getCurrentSession()
					.createQuery(
							"select lower(t.LotId) from Pricing t left join t.Lot where (t.CreatedAt >= :startDate and t.CreatedAt <= :endDate) and t.Lot.SpotDirection = :flag ",
							String.class)
					.setParameter("startDate", startDate).setParameter("endDate", endDate).setParameter("flag", sellOrBuy).getResultList());
		}
		return changed;
	}

	/**
	 * @param Flag
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<LotSign> getTagChangedLotInToday(String Flag) {

		List<LotSign> resultList = new ArrayList<>();

		StringBuilder hql = new StringBuilder();
		Date startDate = DateUtil.startOfTodDay(new Date());
		Date endDate = DateUtil.endOfTodDay(new Date());
		hql.append("select t from LotSign t where 1 = 1 and ( ");

		if (Flag.substring(2, 3).equalsIgnoreCase(isShow)) {
			hql.append("(IsPricedDate >= :startDate and IsPricedDate <= :endDate) or ");
		}
		if (Flag.substring(3, 4).equalsIgnoreCase(isShow)) {
			hql.append("(IsDeliveredDate >= :startDate and IsDeliveredDate <= :endDate) or ");
		}
		if (Flag.substring(4, 5).equalsIgnoreCase(isShow)) {
			hql.append("(IsHedgedDate >= :startDate and IsHedgedDate <= :endDate) or ");
		}
		if (Flag.substring(5, 6).equalsIgnoreCase(isShow)) {
			hql.append("(IsInvoicedDate >= :startDate and IsInvoicedDate <= :endDate) or ");
		}
		if (Flag.substring(6, 7).equalsIgnoreCase(isShow)) {
			hql.append("(IsFundedDate >= :startDate and IsFundedDate <= :endDate) or ");
		}
		String hqlStr = hql.toString();
		if (hqlStr.endsWith("where 1 = 1 and ( ")) {
			return resultList;
		} else if (hqlStr.endsWith("or ")) {
			hqlStr = hqlStr.substring(0, hqlStr.lastIndexOf("or"));
		}
		hqlStr += ")";

		List<LotSign> list = lotSignRepo.getCurrentSession().createQuery(hqlStr, LotSign.class)
				.setParameter("startDate", startDate).setParameter("endDate", endDate).getResultList();
		String SpotDirection = Flag.substring(0, 1);
		for (LotSign lotSign : list) {
			if (lotSign.getFullNo().substring(0, 1).equalsIgnoreCase(SpotDirection)) {
				resultList.add(lotSign);
			}
		}
		return resultList;
	}

	/**
	 * @param Flag
	 * @return
	 */
	private List<PricingReport> getTodayPriceReport(String Flag, Date beginDate, Date endDate) {
		StringBuilder hql = new StringBuilder();
		hql.append(
				"select new com.smm.ctrm.dto.res.ReceiptShipDailyReport.PricingReport(pricing.Lot.FullNo, pricing.Quantity, pricing.Lot.QuantityPriced,pricing.Major, pricing.Premium, pricing.MajorMarket.Name, pricing.MajorType, pricing.TradeDate)")
				.append("from Pricing pricing ").append("left join pricing.Lot ")
				.append("left join pricing.MajorMarket ")
				.append("where pricing.TradeDate >= :beginDate and pricing.TradeDate <= :endDate ")
				.append("and pricing.Lot.SpotDirection = :Flag");

		Map<String, Object> params = new HashMap<>();
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("Flag", Flag);
		List<PricingReport> pricingReportList = pricingReportRepo.ExecuteHql(hql.toString(), params,
				PricingReport.class);

		return pricingReportList;
	}

	/**
	 * @param Flag
	 * @return
	 */
	private List<ReceiptShipReport> getReceiptShipReports(String Flag, Date beginDate, Date endDate) {
		StringBuilder hql = new StringBuilder();
		hql.append(
				"select new com.smm.ctrm.dto.res.ReceiptShipDailyReport.ReceiptShipReport(rs.ReceiptShipNo, rs.Lot.FullNo, rs.Weight, rs.Lot.QuantityDelivered, "
				+ "rs.WhName, rs.DeliveryTruckNo, rs.DeliveryUnit, rs.DeliveryMan, rs.DeliveryManIDCard, rs.DeliveryTruckNo, rs.ReceiptShipDate)")
				.append("from ReceiptShip rs ").append("left join rs.Lot ")
				.append("where rs.ReceiptShipDate >= :beginDate and rs.ReceiptShipDate <= :endDate ")
				.append("and rs.Lot.SpotDirection = :Flag");

		Map<String, Object> params = new HashMap<>();
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("Flag", Flag);

		List<ReceiptShipReport> receiptShipReportList = receiptShipReportRepo.ExecuteHql(hql.toString(), params,
				ReceiptShipReport.class);

		return receiptShipReportList;
	}

	/**
	 * @param Flag
	 * @return
	 */
	private List<PositionReport> getPositionReports(String Flag, Date beginDate, Date endDate) {
		StringBuilder hql = new StringBuilder();
		hql.append(
				"select new com.smm.ctrm.dto.res.ReceiptShipDailyReport.PositionReport(position.Lot.FullNo, position.Quantity, position.OurPrice, position.OurRef, position.ForwardType, position.Market.Name, position.Commodity.Name, position.PromptDate, position.TradeDate)")
				.append("from Position position ").append("left join position.Lot ")
				.append("left join position.Market ").append("left join position.Commodity ")
				.append("where position.TradeDate >= :beginDate and position.TradeDate <= :endDate ")
				.append("and position.Lot.SpotDirection = :Flag");

		Map<String, Object> params = new HashMap<>();
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("Flag", Flag);

		List<PositionReport> positionReportList = positionReportRepo.ExecuteHql(hql.toString(), params,
				PositionReport.class);
		return positionReportList;
	}

	/**
	 * 日报数据转出到JSON
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	@Override
	public void DailyToJson(String Flag, Date date) throws IOException {

		DailyReportParams dailyReportParams = new DailyReportParams();
		dailyReportParams.setFlag(Flag + "YYYNYY");
		dailyReportParams.setBeginDate(DateUtil.startOfTodDay(DateUtil.doFormatDate(date, "")));
		dailyReportParams.setEndDate(DateUtil.endOfTodDay(DateUtil.doFormatDate(date, "")));
		DailyReport dailyReport = getDailyReport(dailyReportParams);

		String jsonPath = getJsonFolder(date);

		// 验证指定文件夹是否存在
		File file = new File(jsonPath);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}

		// 验证文件是否存在
		jsonPath = getJsonFileSaas(jsonPath, Flag, date);
		file = new File(jsonPath);
		if (!file.exists() && !file.isFile()) {
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}

		FileOutputStream out = new FileOutputStream(file, false);
		StringBuffer sb = new StringBuffer();
		sb.append(JSONUtil.doConvertObjToString(dailyReport));

		out.write(sb.toString().getBytes("utf-8"));// 注意需要转换对应的字符集]
		out.close();

	}

	/**
	 * 获取Json 文件夹路径
	 * 
	 * @param Flag
	 * @param date
	 * @return
	 */
	private String getJsonFolder(Date date) {
		CTRMOrg ctrmOrg = CTRMOrg.currentOrg();
		String jsonPath = ctrmOrg.getTemplateFilePath();
		jsonPath = jsonPath + "/" + jsonFolder + "/" + DateUtil.getFormatDateToYearMonth(date);
		return jsonPath;
	}

	/**
	 * 获取Json 文件全路径
	 * 
	 * @param path
	 * @param Flag
	 * @param date
	 * @return
	 */
	private String getJsonFile(String path, String Flag, Date date) {
		path = path + "/" + DateUtil.getFormatDateToShort(date) + "_" + Flag + ".json";
		return path;
	}

	private String getJsonFileSaas(String path, String Flag, Date date) {
		path = path + "/" + DataSourceContextHolder.getDataSourceName() + DateUtil.getFormatDateToShort(date) + "_"
				+ Flag + ".json";
		return path;
	}

	/**
	 * 读取txt文件的内容
	 * 
	 * @param file
	 *            想要读取的文件对象
	 * @return 返回文件内容
	 */
	private String txt2String(String fileName) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			// BufferedReader br = new BufferedReader(new
			// FileReader(file));//构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				result.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	class LotChangedToday {

		private List<String> addedList = new ArrayList<>();

		private List<String> pricedList = new ArrayList<>();

		private List<String> deliveredList = new ArrayList<>();

		private List<String> hedgedList = new ArrayList<>();

		private List<String> fundedList = new ArrayList<>();

		private List<String> invoicedList = new ArrayList<>();

		public List<String> getAllLotId() {
			List<String> lotIdList = new ArrayList<>();
			lotIdList.addAll(addedList);
			lotIdList.addAll(deliveredList);
			lotIdList.addAll(pricedList);
			lotIdList.addAll(hedgedList);
			lotIdList.addAll(fundedList);
			lotIdList.addAll(invoicedList);
			return lotIdList;
		}

		public List<String> getAddedList() {
			return addedList;
		}

		public void setAddedList(List<String> addedList) {
			this.addedList = addedList;
		}

		public List<String> getPricedList() {
			return pricedList;
		}

		public void setPricedList(List<String> pricedList) {
			this.pricedList = pricedList;
		}

		public List<String> getDeliveredList() {
			return deliveredList;
		}

		public void setDeliveredList(List<String> deliveredList) {
			this.deliveredList = deliveredList;
		}

		public List<String> getHedgedList() {
			return hedgedList;
		}

		public void setHedgedList(List<String> hedgedList) {
			this.hedgedList = hedgedList;
		}

		public List<String> getFundedList() {
			return fundedList;
		}

		public void setFundedList(List<String> fundedList) {
			this.fundedList = fundedList;
		}

		public List<String> getInvoicedList() {
			return invoicedList;
		}

		public void setInvoicedList(List<String> invoicedList) {
			this.invoicedList = invoicedList;
		}

	}
}
