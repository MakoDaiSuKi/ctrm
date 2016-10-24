package com.smm.ctrm.domain.Physical;

import java.io.Serializable;
import java.util.List;

public class MaterieCostAccount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5951823312534450748L;
	/**
	 * 合同号
	 **/
	private String DocumentNo;
	/**
	 * 汇率
	 **/
	private String Rate;
	/**
	 * 开证金额
	 **/
	private String OpenAmount;
	/**
	 * 总金额
	 **/
	private String TotalAmount;
	/**
	 * 临时付款金额
	 **/
	private String TempPayAmount;
	/**
	 * 尾款
	 **/
	private String FinalPayment;
	private List<CostComponent> CostComponents;
	private List<CostTax> CostTaxs;
	private List<ItemPrice> ItemPrices;

	public String getDocumentNo() {
		return DocumentNo;
	}

	public void setDocumentNo(String documentNo) {
		DocumentNo = documentNo;
	}

	public String getRate() {
		return Rate;
	}

	public void setRate(String rate) {
		Rate = rate;
	}

	public String getOpenAmount() {
		return OpenAmount;
	}

	public void setOpenAmount(String openAmount) {
		OpenAmount = openAmount;
	}

	public String getTotalAmount() {
		return TotalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		TotalAmount = totalAmount;
	}

	public String getTempPayAmount() {
		return TempPayAmount;
	}

	public void setTempPayAmount(String tempPayAmount) {
		TempPayAmount = tempPayAmount;
	}

	public String getFinalPayment() {
		return FinalPayment;
	}

	public void setFinalPayment(String finalPayment) {
		FinalPayment = finalPayment;
	}

	public List<CostComponent> getCostComponents() {
		return CostComponents;
	}

	public void setCostComponents(List<CostComponent> costComponents) {
		CostComponents = costComponents;
	}

	public List<CostTax> getCostTaxs() {
		return CostTaxs;
	}

	public void setCostTaxs(List<CostTax> costTaxs) {
		CostTaxs = costTaxs;
	}

	public List<ItemPrice> getItemPrices() {
		return ItemPrices;
	}

	public void setItemPrices(List<ItemPrice> itemPrices) {
		ItemPrices = itemPrices;
	}

}
