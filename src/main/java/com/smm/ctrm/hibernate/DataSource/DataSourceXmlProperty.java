package com.smm.ctrm.hibernate.DataSource;

/**
 * @author zhaoyutao
 *
 */
public class DataSourceXmlProperty {

	/**
	 * 组织名称
	 */
	private String name;

	/**
	 * dataSource
	 */
	private String ip;
	
	/**
	 * 数据库类型名
	 */
	private String databaseName;
	
	/**
	 * 端口
	 */
	private String port;

	/**
	 * 数据库名称
	 */
	private String db;

	/**
	 * 用户名
	 */
	private String uid;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 超时时间
	 */
	private String timeout;
	
	private String templateFilePath;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getTemplateFilePath() {
		return templateFilePath;
	}

	public void setTemplateFilePath(String templateFilePath) {
		this.templateFilePath = templateFilePath;
	}

	@Override
	public String toString() {
		return String.format(
				"DataSourceXmlProperty [name=%s, ip=%s, databaseName=%s, port=%s, db=%s, uid=%s, password=%s, timeout=%s, templateFilePath=%s]",
				name, ip, databaseName, port, db, uid, password, timeout, templateFilePath);
	}

}
