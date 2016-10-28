package com.smm.ctrm.bo.impl.Report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Report.PositionDailyReportService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.PositionChangeMonth;
import com.smm.ctrm.domain.Physical.PositionDelivery;
import com.smm.ctrm.domain.Physical.PositionUnravel;
import com.smm.ctrm.domain.apiClient.PositionDailyReportParam;
import com.smm.ctrm.dto.res.PositionDailyReport.PositionDailyBase;
import com.smm.ctrm.dto.res.PositionDailyReport.PositionDailyDelivery;
import com.smm.ctrm.dto.res.PositionDailyReport.PositionDailyFuturesInMonth;
import com.smm.ctrm.dto.res.PositionDailyReport.PositionDailyReport;
import com.smm.ctrm.dto.res.PositionDailyReport.PositionDailyUnravel;
import com.smm.ctrm.hibernate.DataSource.CTRMOrg;
import com.smm.ctrm.hibernate.DataSource.DataSourceContextHolder;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.JSONUtil;

@Service
public class PositionDailyReportServiceImpl implements PositionDailyReportService {
	@Resource
	private HibernateRepository<Position> positionRepo;

	@Resource
	private HibernateRepository<Position> position4BrokerRepo;

	@Resource
	private HibernateRepository<PositionUnravel> positionUnravelRepo;

	@Resource
	private HibernateRepository<PositionDelivery> positionDeliveryRepo;

	@Resource
	private HibernateRepository<PositionChangeMonth> positionChangeMonthRepo;

	private final String jsonFolder = "PositionDaily";

	@Override
	public PositionDailyReport getDailyReport(PositionDailyReportParam dailyReportParams) throws IOException {
		String beginDate = DateFormatUtils.format(dailyReportParams.getBeginDate(), "yyyy-MM-dd");
		String todayDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		if (beginDate.equalsIgnoreCase(todayDate)) {
			return getDailyReportByDB(dailyReportParams);
		} else {
			String jsonFolder = getJsonFolder(dailyReportParams.getBeginDate());
			String JsonPath = getJsonFile(jsonFolder, dailyReportParams.getBeginDate());
			File file = new File(JsonPath);
			JsonPath = getJsonFileSaas(jsonFolder, dailyReportParams.getBeginDate());
			// 此方法用于过度，SAAS模式
			if (!file.exists()) {
				file = new File(JsonPath);
			}
			if (file.exists() && file.isFile()) {
				return getDailyReportByJson(dailyReportParams, file.getAbsolutePath());
			} else {
				return new PositionDailyReport();
			}
		}
	}

	/**
	 * 数据库读取日报
	 * 
	 * @param dailyReportParams
	 * @return
	 */
	private PositionDailyReport getDailyReportByDB(PositionDailyReportParam dailyReportParams) {
		PositionDailyReport dailyReport = new PositionDailyReport();
		String commonColumnSql = "pb.Id, pb.Market.Name, pb.ForwardType, pb.Purpose, pb.CommodityId, pb.Commodity.Name, pb.Instrument.Name, pb.LS, pb.OCS, "
				+ "pb.PromptDate, pb.Hands, pb.Quantity, pb.OurPrice, pb.Currency ";
		String baseSql = "select new com.smm.ctrm.dto.res.PositionDailyReport.PositionDailyBase(" + commonColumnSql
				+ ") from Position4Broker pb left join pb.Market left join pb.Commodity left join pb.Instrument where pb.CreatedAt >= :startDate and pb.CreatedAt <= :endDate";

		Date startDate = DateUtil.startOfTodDay(new Date());
		Date endDate = DateUtil.endOfTodDay(new Date());

		List<PositionDailyBase> baseList = position4BrokerRepo.getCurrentSession()
				.createQuery(baseSql, PositionDailyBase.class).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getResultList();
		dailyReport.setAddedTodayList(baseList);

		String unravelSql = "select new com.smm.ctrm.dto.res.PositionDailyReport.PositionDailyUnravel("
				+ commonColumnSql + ", pb.UnravelQuantity, pb.UnravelPrice) "
				+ "from Position4Broker pb left join pb.Market left join pb.Commodity left join pb.Instrument where pb.LastUnravelDate >= :startDate and pb.LastUnravelDate <= :endDate";

		List<PositionDailyUnravel> unravelList = position4BrokerRepo.getCurrentSession()
				.createQuery(unravelSql, PositionDailyUnravel.class).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getResultList();

		addProfitAndLossForUnravel(unravelList, startDate, endDate);

		dailyReport.setUnravelList(unravelList);

		String deliverySql = "select new com.smm.ctrm.dto.res.PositionDailyReport.PositionDailyDelivery("
				+ commonColumnSql + ", pb.DeliveryQuantity, pb.DeliveryPrice) "
				+ "from Position4Broker pb left join pb.Market left join pb.Commodity left join pb.Instrument where pb.LastDeliveryDate >= :startDate and pb.LastDeliveryDate <= :endDate";

		List<PositionDailyDelivery> deliveryList = position4BrokerRepo.getCurrentSession()
				.createQuery(deliverySql, PositionDailyDelivery.class).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getResultList();
		addProfitAndLossForDelivery(deliveryList, startDate, endDate);
		dailyReport.setDeliveryList(deliveryList);

		String futuresInMonthSql = "select new com.smm.ctrm.dto.res.PositionDailyReport.PositionDailyFuturesInMonth("
				+ commonColumnSql + ", pb.ChangeMonthQuantity, pb.ChangeMonthPrice) "
				+ "from Position4Broker pb left join pb.Market left join pb.Commodity left join pb.Instrument where pb.LastDeliveryDate >= :startDate and pb.LastDeliveryDate <= :endDate";

		List<PositionDailyFuturesInMonth> futuresInMonthList = position4BrokerRepo.getCurrentSession()
				.createQuery(futuresInMonthSql, PositionDailyFuturesInMonth.class).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getResultList();
		
		addProfitAndLossForChangeMonth(futuresInMonthList, startDate, endDate);
		dailyReport.setFuturesInMonthList(futuresInMonthList);

		return dailyReport;
	}

	private void addProfitAndLossForUnravel(List<PositionDailyUnravel> dailyList, Date startDate, Date endDate) {

		List<String> positionBrokerIdList = new ArrayList<>();
		for (PositionDailyUnravel base : dailyList) {
			positionBrokerIdList.add(base.getPositionBrokerId());
		}
		if(positionBrokerIdList.size()==0) {
			return;
		}
		List<PositionUnravel> positionUnravelList = positionUnravelRepo.GetList(positionUnravelRepo
				.CreateCriteria(PositionUnravel.class).add(Restrictions.in("SourcePtId", positionBrokerIdList)));
		for (PositionDailyBase base : dailyList) {
			BigDecimal profitAndLoss = BigDecimal.ZERO;
			for (PositionUnravel pu : positionUnravelList) {
				profitAndLoss = profitAndLoss.add(pu.getProfitAndLoss());
			}
			base.setProfitAndLoss(profitAndLoss);
		}
	}
	
	private void addProfitAndLossForDelivery(List<PositionDailyDelivery> dailyList, Date startDate, Date endDate) {

		List<String> positionBrokerIdList = new ArrayList<>();
		for (PositionDailyDelivery base : dailyList) {
			positionBrokerIdList.add(base.getPositionBrokerId());
		}
		if(positionBrokerIdList.size()==0) {
			return;
		}
		List<PositionDelivery> positionDeliveryList = positionDeliveryRepo.GetList(positionDeliveryRepo
				.CreateCriteria(PositionDelivery.class).add(Restrictions.in("Position4BrokerId", positionBrokerIdList)));
		for (PositionDailyBase base : dailyList) {
			BigDecimal profitAndLoss = BigDecimal.ZERO;
			for (PositionDelivery pu : positionDeliveryList) {
//				profitAndLoss = profitAndLoss.add(pu.getProfitAndLoss());
			}
			base.setProfitAndLoss(profitAndLoss);
		}
	}
	
	private void addProfitAndLossForChangeMonth(List<PositionDailyFuturesInMonth> dailyList, Date startDate, Date endDate) {

		List<String> positionBrokerIdList = new ArrayList<>();
		for (PositionDailyFuturesInMonth base : dailyList) {
			positionBrokerIdList.add(base.getPositionBrokerId());
		}
		if(positionBrokerIdList.size()==0) {
			return;
		}
		List<PositionChangeMonth> positionChangeMonthlList = positionChangeMonthRepo.GetList(positionChangeMonthRepo
				.CreateCriteria(PositionChangeMonth.class).add(Restrictions.in("SourcePtId", positionBrokerIdList)));
		for (PositionDailyBase base : dailyList) {
			BigDecimal profitAndLoss = BigDecimal.ZERO;
			for (PositionChangeMonth pu : positionChangeMonthlList) {
				profitAndLoss = profitAndLoss.add(pu.getProfitAndLoss());
			}
			base.setProfitAndLoss(profitAndLoss);
		}
	}

	@Override
	public void DailyToJson(Date date) throws IOException {
		PositionDailyReportParam dailyReportParams = new PositionDailyReportParam();
		dailyReportParams.setBeginDate(DateUtil.startOfTodDay(DateUtil.doFormatDate(date, "")));
		dailyReportParams.setEndDate(DateUtil.endOfTodDay(DateUtil.doFormatDate(date, "")));
		PositionDailyReport dailyReport = getDailyReport(dailyReportParams);

		String jsonPath = getJsonFolder(date);

		// 验证指定文件夹是否存在
		File file = new File(jsonPath);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}

		// 验证文件是否存在
		jsonPath = getJsonFileSaas(jsonPath, date);
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

	@Override
	public void clearSession() {
		positionRepo.Clear();
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
	private String getJsonFile(String path, Date date) {
		path = path + "/" + DateUtil.getFormatDateToShort(date) + ".json";
		return path;
	}

	private String getJsonFileSaas(String path, Date date) {
		path = path + "/" + DataSourceContextHolder.getDataSourceName() + DateUtil.getFormatDateToShort(date)
				+ ".json";
		return path;
	}

	/**
	 * Json文件读取日报
	 * 
	 * @param dailyReportParams
	 * @param jsonPath
	 * @return
	 * @throws IOException
	 */
	private PositionDailyReport getDailyReportByJson(PositionDailyReportParam dailyReportParams, String jsonPath)
			throws IOException {
		String jsonString = txt2String(jsonPath);
		PositionDailyReport dailyReport = JSONUtil.doConvertStringToT(jsonString, PositionDailyReport.class);

		return dailyReport;
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

}
