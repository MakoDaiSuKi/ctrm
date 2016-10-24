package com.smm.ctrm.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.LegalBank;
import com.smm.ctrm.domain.Basis.Origin;
import com.smm.ctrm.domain.Basis.Product;
import com.smm.ctrm.domain.Basis.Spec;
import com.smm.ctrm.domain.Physical.BankReceipt;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.FinishedProduct;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.InvoiceStorage;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.Pricing;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.Physical.Storage;

public class TableNameConst {
	
	private final static Map<String, String> map = new HashMap<>();
	public static String getTableNameCustomer(String tableName, String column, String value, Session session) {
		if(map.size() == 0) {
			buildMyMap();
		}
		if(tableName.equals(TableNameConst.ReceiptShip)) {
			List<ReceiptShip> receiptShipList = session.createQuery("select t from ReceiptShip t where " + column + " = :columnValue", com.smm.ctrm.domain.Physical.ReceiptShip.class)
					.setParameter("columnValue", value).setFirstResult(0).setMaxResults(1).getResultList();
			if(receiptShipList.size() > 0) {
				if(receiptShipList.get(0).getFlag().equals(com.smm.ctrm.domain.Physical.ReceiptShip.RECEIPT)) {
					return "收货单";
				} else if(receiptShipList.get(0).getFlag().equals(com.smm.ctrm.domain.Physical.ReceiptShip.SHIP)){
					return "发货单";
				}
			}
		} else if (tableName.equals(TableNameConst.Fund)) {
			List<Fund> fundList = session.createQuery("select t from Fund t where " + column + " = :columnValue", com.smm.ctrm.domain.Physical.Fund.class)
					.setParameter("columnValue", value).setFirstResult(0).setMaxResults(1).getResultList();
			if(fundList.size() > 0) {
				if(fundList.get(0).getDC().equals(com.smm.ctrm.domain.Physical.Fund.PAYMENT)) {
					return "付款";
				} else if(fundList.get(0).getDC().equals(com.smm.ctrm.domain.Physical.Fund.RECEIPT)){
					return "收款";
				}
			}
		}
		return map.get(tableName);
	}
	
	private static void buildMyMap(){
		map.put(Lot, "批次");
		map.put(LotBrand, "批次关联的品牌");
		map.put(Storage, "交付明细");
		map.put(FinishedProduct, "产成品");
		map.put(ReceiptShip, "收发货单");
		map.put(Contract, "订单");
		map.put(Legal, "抬头");
		map.put(BankReceipt, "收款水单");
		map.put(Product, "商品名称");
		map.put(Commodity, "品种");
		map.put(Position, "头寸");
		map.put(Spec, "规格");
		map.put(Origin, "原产地");
		map.put(Brand, "品牌");
		map.put(Pricing, "点价");
		map.put(Invoice, "发票");
		map.put(Fund, "收付款");
		map.put(InvoiceStorage, "发票关联的商品明细");
		map.put(CustomerBank, "客户银行");
		map.put(LegalBank, "抬头银行");
	}

	/**
	 * 批次
	 */
	public static String Lot = HTools.getTableName(Lot.class);

	/**
	 * 
	 */
	public static String LotBrand = "Physical.LotBrand";

	/**
	 * 交付明细
	 */
	public static String Storage = HTools.getTableName(Storage.class);

	/**
	 * 产成品
	 */
	public static String FinishedProduct = HTools.getTableName(FinishedProduct.class);

	/**
	 * 收发货单
	 */
	public static String ReceiptShip = HTools.getTableName(ReceiptShip.class);

	/**
	 * 订单
	 */
	public static String Contract = HTools.getTableName(Contract.class);

	/**
	 * 抬头
	 */
	public static String Legal = HTools.getTableName(Legal.class);

	/**
	 * 收款水单
	 */
	public static String BankReceipt = HTools.getTableName(BankReceipt.class);

	/**
	 * 商品名称
	 */
	public static String Product = HTools.getTableName(Product.class);
	
	/**
	 * 品种
	 */
	public static String Commodity = HTools.getTableName(Commodity.class);
	
	/**
	 * 头寸
	 */
	public static String Position = HTools.getTableName(Position.class);
	
	/**
	 * 规格
	 */
	public static String Spec = HTools.getTableName(Spec.class);
	
	/**
	 * 原产地
	 */
	public static String Origin = HTools.getTableName(Origin.class);
	
	/**
	 * 品牌
	 */
	public static String Brand = HTools.getTableName(Brand.class);
	
	/**
	 * 点价
	 */
	public static String Pricing = HTools.getTableName(Pricing.class);
	
	/**
	 * 发票
	 */
	public static String Invoice = HTools.getTableName(Invoice.class);
	
	/**
	 * 收付款
	 */
	public static String Fund = HTools.getTableName(Fund.class);
	
	/**
	 * 发票关联的商品明细
	 */
	public static String InvoiceStorage = HTools.getTableName(InvoiceStorage.class);

	/**
	 * 客户银行
	 */
	public static String CustomerBank = HTools.getTableName(CustomerBank.class);
	
	/**
	 * 抬头银行
	 */
	public static String LegalBank = HTools.getTableName(LegalBank.class);
	
}
