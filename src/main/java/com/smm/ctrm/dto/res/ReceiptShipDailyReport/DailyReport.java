package com.smm.ctrm.dto.res.ReceiptShipDailyReport;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DailyReport {
	
	public final static String SELL = "S";
	
	public final static String BUY = "B";
	
	/**
	 * 批次列表
	 */
	@JsonProperty(value = "LotReportList")
	private List<LotReport> LotReportList = new ArrayList<>();
	
	/**
	 * 点价列表
	 */
	@JsonProperty(value = "PricingReportList")
	private List<PricingReport> PricingReportList = new ArrayList<>();
	
	/**
	 * 收发货列表
	 */
	@JsonProperty(value = "ReceiptShipReportList")
	private List<ReceiptShipReport> ReceiptShipReportList = new ArrayList<>();
	
	/**
	 * 保值列表
	 */
	@JsonProperty(value = "PositionReportList")
	private List<PositionReport> PositionReportList = new ArrayList<>();
	
	/**
	 * 发票列表
	 */
	@JsonProperty(value = "InvoioceReportList")
	private List<InvoiceReport> InvoioceReportList = new ArrayList<>();
	
	/**
	 * 资金列表
	 */
	@JsonProperty(value = "FundReportList")
	private List<FundReport> FundReportList = new ArrayList<>();

	public List<LotReport> getLotReportList() {
		return LotReportList;
	}

	public List<FundReport> getFundReportList() {
		return FundReportList;
	}

	public void setFundReportList(List<FundReport> fundReportList) {
		FundReportList = fundReportList;
	}

	public void setLotReportList(List<LotReport> lotReportList) {
		LotReportList = lotReportList;
	}

	public List<PricingReport> getPricingReportList() {
		return PricingReportList;
	}

	public void setPricingReportList(List<PricingReport> pricingReportList) {
		PricingReportList = pricingReportList;
	}

	public List<ReceiptShipReport> getReceiptShipReportList() {
		return ReceiptShipReportList;
	}

	public void setReceiptShipReportList(List<ReceiptShipReport> receiptShipReportList) {
		ReceiptShipReportList = receiptShipReportList;
	}

	public List<PositionReport> getPositionReportList() {
		return PositionReportList;
	}

	public void setPositionReportList(List<PositionReport> positionReportList) {
		PositionReportList = positionReportList;
	}

	public List<InvoiceReport> getInvoioceReportList() {
		return InvoioceReportList;
	}

	public void setInvoioceReportList(List<InvoiceReport> invoioceReportList) {
		InvoioceReportList = invoioceReportList;
	}

}
