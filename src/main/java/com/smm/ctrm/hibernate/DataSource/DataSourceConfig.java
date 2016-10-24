package com.smm.ctrm.hibernate.DataSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataSourceConfig {

	private Logger logger = Logger.getLogger(this.getClass());

	private final static List<String> dataSourceNameList = new CopyOnWriteArrayList<>();
	
	private Object defaultDataSource;

	private final static Map<String, CTRMOrg> ctrmOrgMap = new ConcurrentHashMap<>();

	/**
	 * 动态数据源配置文件
	 */
	private String dataSourceXmlPath = "";

	/**
	 * 构建多个数据源
	 */
	public Map<Object, Object> builder() {
		Map<Object, Object> dataSourceMap = new HashMap<>();
		try {
			init();
			List<DataSourceXmlProperty> propertyList = readDataSourceConfigXml();

			for (int i = 0; i < propertyList.size(); i++) {
				DataSourceXmlProperty property = propertyList.get(i);
				String url = getPropertyUrlByDriverName(property.getDatabaseName(), property.getIp(), property.getPort(),
						property.getDb());
				DataSource dataSource = getDataSource(property.getDatabaseName(), url, property.getUid(),
						property.getPassword());
				dataSourceNameList.add(property.getName());
				dataSourceMap.put(property.getName(), dataSource);
				if(i == 0) {
					defaultDataSource = dataSource;
				}
				CTRMOrg org = new CTRMOrg();
				org.setOrgName(property.getName());
				org.setTemplateFilePath(property.getTemplateFilePath());
				ctrmOrgMap.put(property.getName(), org);
				
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return dataSourceMap;
	}

	/**
	 * 初始化 dataSourceXmlPath、defaultDriverClassName
	 * 
	 * @throws IOException
	 */
	private void init() throws IOException {
		Properties conf = new Properties();
		FileInputStream fis = null;
		String configPath = System.getProperty("smm.ctrm") + "/WEB-INF/classes/conf.properties";
		try {
			File file = new File(configPath);
			fis = new FileInputStream(file);
			conf.load(fis);
			dataSourceXmlPath = conf.getProperty("dataSourceXmlPath");
			if(!new File(dataSourceXmlPath).exists()) {
				dataSourceXmlPath = conf.getProperty("dataSourceXmlPathForMac");
			}
			if (StringUtils.isBlank(dataSourceXmlPath)) {
				RuntimeException ex = new RuntimeException("\nconf.properties缺少dataSourceXmlPath[动态数据源配置文件路径]");
				logger.error(ex.getMessage(), ex);
			}
			dataSourceNameList.clear();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 构建数据源
	 * 
	 * @param driverClassName
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	private DataSource getDataSource(String dataBaseName, String url, String username, String password) {
		BasicDataSource dataSource = new BasicDataSource();
		String driver = "";
		if(dataBaseName.equalsIgnoreCase(Constants.Oracle)) {
			driver = Constants.OracleDriver;
		} else if(dataBaseName.equalsIgnoreCase(Constants.SQLServer)) {
			driver = Constants.SQLServerDriver;
		}

		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setMaxActive(200);
		dataSource.setMaxIdle(50);
		dataSource.setMaxWait(60000);
		dataSource.setRemoveAbandoned(Boolean.TRUE);
		dataSource.setRemoveAbandonedTimeout(30);
		dataSource.setValidationQuery("select 1");
		dataSource.setTestOnBorrow(Boolean.TRUE);

		return dataSource;
	}

	/**
	 * 读取xml中的动态数据源配置
	 * 
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private List<DataSourceXmlProperty> readDataSourceConfigXml()
			throws IOException, ParserConfigurationException, SAXException {

		List<DataSourceXmlProperty> propertyList = new ArrayList<>();
		File fXmlFile = new File(dataSourceXmlPath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		// optional, but recommended
		// read this -
		// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("org");
		for (int i = 0; i < nodeList.getLength(); i++) {
			NamedNodeMap map = nodeList.item(i).getAttributes();
			DataSourceXmlProperty property = new DataSourceXmlProperty();
			property.setName(map.getNamedItem("name").getNodeValue());
			property.setIp(map.getNamedItem("ip").getNodeValue());
			property.setPort(map.getNamedItem("port").getNodeValue());
			property.setDatabaseName(map.getNamedItem("databaseName").getNodeValue());
			property.setDb(map.getNamedItem("db").getNodeValue());
			property.setUid(map.getNamedItem("uid").getNodeValue());
			property.setPassword(map.getNamedItem("password").getNodeValue());
			property.setTimeout(map.getNamedItem("timeout").getNodeValue());
			property.setTemplateFilePath(map.getNamedItem("templateFilePath").getNodeValue());
			if (checkProperty(property)) {
				propertyList.add(property);
			}
		}
		return propertyList;
	}

	/**
	 * 构造jdbc连接url
	 * 
	 * @param driverClassName
	 * @param url
	 * @param db
	 * @return
	 */
	private String getPropertyUrlByDriverName(String databaseName, String ip, String port, String db) {
		
		if (databaseName.equalsIgnoreCase("Oracle")) {
			// jdbc:oracle:thin:@//192.168.2.1:1521/XE
			return "jdbc:oracle:thin:@//" + ip +":" + port + "/" + db;
		} else if (databaseName.equalsIgnoreCase("SQLServer")) {
			// jdbc:sqlserver://172.16.5.106:1433; DatabaseName=hedgestudio.baiyin
			return "jdbc:sqlserver://"  + ip +":" + port + "; DatabaseName=" + db;
		}
		return "jdbc:sqlserver://"  + ip +":" + port + "; DatabaseName=" + db;
	}

	/**
	 * 检查数据源配置合法性
	 * 
	 * @param property
	 * @return
	 */
	private boolean checkProperty(DataSourceXmlProperty property) {
		if (StringUtils.isBlank(property.getName()) || StringUtils.isBlank(property.getIp()) || StringUtils.isBlank(property.getDatabaseName())
				|| StringUtils.isBlank(property.getDb()) || StringUtils.isBlank(property.getUid())
				|| StringUtils.isBlank(property.getPassword()) || StringUtils.isBlank(property.getPort()) 
				|| StringUtils.isBlank(property.getTemplateFilePath())) {
			logger.warn("\n配置项有问题，" + property);
			return false;
		}
		if(property.getDatabaseName().equalsIgnoreCase(Constants.Oracle)) {
//			logger.info("使用的是Oracle数据库");
		} else if (property.getDatabaseName().equalsIgnoreCase(Constants.SQLServer)) {
//			logger.info("使用的是SQLServier数据库");
		} else {
			logger.error("\n未知的数据库类型," + property.getDatabaseName());
			return false;
		}
		logger.info("已加载机构，" + property.getName() + "。");
		return true;
	}

	public static List<String> getDatasourcenamelist() {
		return dataSourceNameList;
	}

	public Object getDefaultDataSource() {
		return defaultDataSource;
	}

	public static Map<String, CTRMOrg> getCtrmOrgMap() {
		return ctrmOrgMap;
	}
}
