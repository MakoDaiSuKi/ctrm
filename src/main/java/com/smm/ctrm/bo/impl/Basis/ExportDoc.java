package com.smm.ctrm.bo.impl.Basis;

public class ExportDoc {
	
	/**
	 * 模板文件名
	 */
	private String templateName;
	
	/**
	 * 生效前是否可生成
	 */
	
	private Boolean IsCreatableBE;
	
	/**
	 * 生成模式（覆盖/增加）
	 */
	private Boolean IsOverwrite;

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Boolean getIsCreatableBE() {
		return IsCreatableBE == null ? false : IsCreatableBE;
	}

	public void setIsCreatableBE(Boolean isCreatableBE) {
		IsCreatableBE = isCreatableBE;
	}

	public Boolean getIsOverwrite() {
		return IsOverwrite == null ? false : IsOverwrite;
	}

	public void setIsOverwrite(Boolean isOverwrite) {
		IsOverwrite = isOverwrite;
	}
	
	
}
