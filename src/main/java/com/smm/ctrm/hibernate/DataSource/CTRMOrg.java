package com.smm.ctrm.hibernate.DataSource;

/**
 * @author zhaoyutao
 *
 */
public class CTRMOrg {
	
	public static CTRMOrg currentOrg(){
		return DataSourceConfig.getCtrmOrgMap().get(DataSourceContextHolder.getDataSourceName());
	}
	
	/**
	 * 机构名称
	 */
	private String orgName;
	
	/**
	 * 机构模板文件路径
	 */
	private String templateFilePath;

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getTemplateFilePath() {
		return templateFilePath;
	}

	public void setTemplateFilePath(String templateFilePath) {
		this.templateFilePath = templateFilePath;
	}
	
	
}
