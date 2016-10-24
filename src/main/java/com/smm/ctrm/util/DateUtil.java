package com.smm.ctrm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.w3c.dom.Node;

/**
 * 
 * @author zengshihua
 *
 */
public class DateUtil {

	private final static Calendar calendar = Calendar.getInstance();

	private final static SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy/MM/dd");

	/**
	 * 判断当天是否是某年第一天
	 * @param dateStart
	 * @return
	 * @throws ParseException
	 */
	public static boolean  getCurrYearFirst(Date nowDate){
		Calendar currCal=Calendar.getInstance();  
        int currentYear = currCal.get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        Date currYearFirst = calendar.getTime();
        
        return   nowDate.compareTo(currYearFirst)==0?true:false;
	}
	
	// 格式化当前系统日期
	public static String getDateWidthFormat(String format) {
		if (format == null || format.equals("")) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		SimpleDateFormat dateFm = new SimpleDateFormat(format);
		String dateTime = dateFm.format(new java.util.Date());
		return dateTime;
	}

	// 格式化日期
	public static String doFormatDate(Date date, String format) {
		if (format == null || format.equals("")) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		if (date == null) {
			date = new Date();
		}

		SimpleDateFormat dateFm = new SimpleDateFormat(format);
		String dateTime = dateFm.format(date);
		return dateTime;
	}

	// 格式化日期
	public static String getFormatDate() {
		String format = "yyyy-MM-dd HH:mm:ss";
		Date date = new Date();
		SimpleDateFormat dateFm = new SimpleDateFormat(format);
		String dateTime = dateFm.format(date);
		return dateTime;
	}

	/**
	 * 格式化日期 yyyyMM
	 * 
	 * @return
	 */
	public static String getFormatDateToYearMonth(Date date) {
		String format = "yyyyMM";
		SimpleDateFormat dateFm = new SimpleDateFormat(format);
		String dateTime = dateFm.format(date);
		return dateTime;
	}

	/**
	 * 格式化日期 yyyyMMdd
	 * 
	 * @return
	 */
	public static String getFormatDateToShort(Date date) {
		String format = "yyyyMMdd";
		SimpleDateFormat dateFm = new SimpleDateFormat(format);
		String dateTime = dateFm.format(date);
		return dateTime;
	}

	// 转化时间字符串为日期
	public static Date doSFormatDate(String dateStr, String format) {
		if (dateStr.equals("")) {
			return null;
		}
		if (format == null || format.equals("")) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		SimpleDateFormat dateFm = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = dateFm.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	// 转化时间字符串为日期
	public static Date doSFormatDate(Date date, String format) {
		if (format == null || format.equals("")) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		if (date == null) {
			date = new Date();
		}

		SimpleDateFormat dateFm = new SimpleDateFormat(format);
		String dateTime = dateFm.format(date);

		Date rdate = null;
		try {
			rdate = dateFm.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return rdate;
	}

	// 转化时间字符串为日期
	public static String doSFormatDate2(String dateStr, String format) {
		if (dateStr.equals("")) {
			return null;
		}
		if (format == null || format.equals("")) {
			format = "yyyy-MM-dd HH:mm:ss";
		}

		SimpleDateFormat dateFm = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = dateFm.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String dateTime = dateFm.format(date);
		return dateTime;
	}

	public static String doFormatDate(Date oldDate, Date newDate) {
		Calendar oldCal = Calendar.getInstance();
		Calendar newCal = Calendar.getInstance();
		oldCal.setTime(oldDate);
		newCal.setTime(newDate);
		if (oldCal.equals(newCal)) {
			return "";
		} else if (oldCal.before(newCal)) {
			return "";
		} else if (oldCal.after(newCal)) {
			return "";
		} else {
			return "";
		}

	}

	public static int getMinute() {
		Calendar calendar = Calendar.getInstance();
		int minute = calendar.get(Calendar.MINUTE);
		return minute;
	}

	/**
	 * 一天的开始时间 00:00:00
	 * 
	 * @discription
	 * @author Nancy/张楠
	 * @created 2015年9月14日 上午11:09:11
	 * @return
	 */
	public static Date startOfTodDay(String date) {
		Date startDate = DateUtil.doSFormatDate(date, "yyyy-MM-dd");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * 一天的结束时间 23:59:59
	 * 
	 * @discription
	 * @author Nancy/张楠
	 * @created 2015年9月14日 上午11:09:26
	 * @return
	 */
	public static Date endOfTodDay(String date) {
		Date endDate = DateUtil.doSFormatDate(date, "yyyy-MM-dd");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		return calendar.getTime();
	}
	
	/**
	 * 
	 * @param startDate
	 * @return
	 */
	public static Date startOfTodDay(Date startDate) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * 
	 * @param endDate
	 * @return
	 */
	public static Date endOfTodDay(Date endDate) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		return calendar.getTime();
	}

	/**
	 * 获取与指定日期相隔 I 天的日期。 i 可以是正负整数
	 * 
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date getDiffDate(Date date, int i) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		calendar.add(Calendar.DATE, i);

		return calendar.getTime();
	}

	/**
	 * 比较两个日期相差的天数
	 * 
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int getIntervalDays(Date fDate, Date oDate) {

		if (null == fDate || null == oDate) {
			return -1;
		}
		long intervalMilli = oDate.getTime() - fDate.getTime();

		return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	}

	public static Date FormatDateAsYymmdd000000(Date dateTime) {
		if (dateTime == null) {
			return null;
		}
		calendar.setTime(dateTime);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static String shortDateString(Date dateTime) {
		return shortDateFormat.format(dateTime);
	}

	private final static String[] engMonth = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
			"Nov", "Dec" };

	public static String getChineseMonth(int month) {
		return engMonth[month];
	}
	
	private final static String[] cnWeek = { "一", "二", "三", "四", "五", "六", "日" };

	public static String getChineseWeek(int week) {
		return "星期" + cnWeek[week];
	}
	
	private final static String[] enWeek = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
	
	public static String getEngWeek(int week) {
		return enWeek[week];
	}

	/**
	 * 根据路径文件路径获取所有的书签
	 * 
	 * @param path
	 * @param docx
	 * @return
	 */
	static Map<String, CTBookmark> bookmarkMap = new HashMap<>();

	public static Map<String, CTBookmark> GetBookMarkStarts(XWPFDocument docx) {

		try {
			for (XWPFTable table : docx.getTables()) {
				for (XWPFTableRow row : table.getRows()) {
					for (XWPFTableCell cell : row.getTableCells()) {
						handleXWPFParagraph(cell.getParagraphs());
					}
				}
			}
			handleXWPFParagraph(docx.getParagraphs());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookmarkMap;
	}

	private static void handleXWPFParagraph(List<XWPFParagraph> paras) {
		for (XWPFParagraph para : paras) {
			for (CTBookmark bookmark : para.getCTP().getBookmarkStartList()) {
				bookmarkMap.put(bookmark.getName(), bookmark);
				XWPFRun run = para.createRun();
				run.setText("dddsfaljaljl");
				// The new Run should be inserted between the bookmarkStart
				// and bookmarkEnd nodes, so find the bookmarkEnd node.
				// Note that we are looking for the next sibling of the
				// bookmarkStart node as it does not contain any child nodes
				// as far as I am aware.
				Node nextNode = bookmark.getDomNode().getNextSibling();
				// If the next node is not the bookmarkEnd node, then step
				// along the sibling nodes, until the bookmarkEnd node
				// is found. As the code is here, it will remove anything
				// it finds between the start and end nodes. This, of course
				// comepltely sidesteps the issues surrounding boorkamrks
				// that contain other bookmarks which I understand can happen.
				while (!(nextNode.getNodeName().contains("bookmarkEnd"))) {
					para.getCTP().getDomNode().removeChild(nextNode);
					nextNode = bookmark.getDomNode().getNextSibling();
				}

				// Finally, insert the new Run node into the document
				// between the bookmarkStrat and the bookmarkEnd nodes.

				para.getCTP().getDomNode().insertBefore(run.getCTR().getDomNode(), nextNode);
				System.out.println(para);
			}
		}
	}

	private static void handleTable(XWPFDocument docx) {
		XWPFTable table = docx.getTableArray(1);
		XWPFUtil.CopytTableRow(table.getRow(1), table.insertNewTableRow(2));
		System.out.println(table);
	}

	public static void main(String[] args) {
		System.out.println(DateUtil.doFormatDate(new Date(), "yy"));
		
		/*// 按照模版格式，生成临时文件
		String filePath = "C:/Users/zhaoyutao/Desktop/test.docx";
		String outFilePath = "C:/Users/zhaoyutao/Desktop/test_out.docx";
		XWPFDocument docx = null;
		try {
			docx = new XWPFDocument(OPCPackage.open(filePath));
			handleTable(docx);
			FileOutputStream fos = new FileOutputStream(new File(outFilePath));
			docx.write(fos);
		} catch (Exception ex) {
		}*/
	}
}
