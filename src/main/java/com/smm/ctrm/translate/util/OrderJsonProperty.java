package com.smm.ctrm.translate.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderJsonProperty {

	private static ObjectMapper om = new ObjectMapper();

	public static String asc(String jsonStr) {
		String rlt = jsonStr;
		try {
			JsonNode root = om.readTree(jsonStr);
			rlt = asc(root, "");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rlt;
	}

	private static String asc(JsonNode root, String rootName) {
		StringBuilder sb = new StringBuilder();

		if (root.isValueNode()) {
			if(rootName.equalsIgnoreCase("\"Id\"")) {
				return rootName + ":" + root.toString().toLowerCase() + ",";
			}
			return rootName + ":" + root.toString() + ",";
		} else if (root.isContainerNode()) {

			if (root.isArray()) {
				sb.append(surroundedByQuotationMarks("Data") + ":[");
				Iterator<JsonNode> arr = root.elements();
				List<String> list = new ArrayList<>();
				while (arr.hasNext()) {
					list.add(arr.next().findValue("Id").toString().toLowerCase());
				}
				list = list.stream().sorted().collect(Collectors.toList());
				List<JsonNode> arrNode = new ArrayList<>();
				for(String id : list) {
					arr = root.elements();
					while(arr.hasNext()) {
						JsonNode node = arr.next();
						if(node.findValue("Id").toString().equalsIgnoreCase(id)){
							arrNode.add(node);
						}
					}
				}
				for(JsonNode node : arrNode) {
					sb.append(asc(node, ""));
				}
				sb.append("]");
			} else if (root.isObject()) {
				if (StringUtils.isNotBlank(rootName)) {
					sb.append(rootName + ":");
				}
				sb.append("{");
				Iterator<String> fieldNames = root.fieldNames();
				List<String> list = new ArrayList<>();
				while (fieldNames.hasNext()) {
					list.add(fieldNames.next());
				}
				list = list.stream().sorted().collect(Collectors.toList());
				for (String fieldName : list) {
					sb.append(asc(root.get(fieldName), surroundedByQuotationMarks(fieldName)));
				}
				return sb.substring(0, sb.length() - 1) + "}";
			}

		} else if (root.isMissingNode()) {
			sb.append(root.toString());
		}

		return sb.toString();
	}

	private static String surroundedByQuotationMarks(String fieldName) {
		return "\"" + fieldName + "\"";
	}
	
	
	public static void main(String[] args) {
		String jsonStr = "{\"FullNo\":null,\"CustomerName\":null,\"CustomerTitleName\":null,\"LegalName\":\"SB\",\"CommodityName\":\"铜\",\"CommodityCode\":\"CU\",\"Unit\":\"吨\",\"GrossProfitAmount\":0,\"GrossProfitRate\":0,\"Exposure\":0,\"InvoiceGrades\":null,\"Provisionals\":null,\"Positions\":null,\"Quantity\":3,\"Comments\":\"\",\"CreatedId\":\"98D251F4-9CDB-4231-9F8B-A43A00F6D73E\",\"UpdatedId\":\"4AECCDBE-6F80-42F7-891C-A4EB011698F7\",\"CommodityId\":\"217036A2-86F7-4D94-8D4D-A3840083D06F\",\"Commodity\":null,\"CustomerId\":\"8C95FA86-FC2D-420C-A6D3-A41F00B12260\",\"Customer\":null,\"LegalId\":\"8C706B1C-B4D1-420A-9EA8-A3EE00CF40AA\",\"Legal\":null,\"CustomerTitleId\":\"3ACB4FE9-14C2-4168-8F21-A41F00B12260\",\"CustomerTitle\":null,\"SpotDirection\":null,\"Price\":5740,\"ContractId\":\"C92722B7-F2BD-4BC3-8464-A4E300F5F7F8\",\"Contract\":null,\"Amount\":2870000,\"LegalBankId\":null,\"LegalBank\":null,\"Storages\":null,\"SerialNo\":\"0001\",\"DocumentNo\":\"SB1504\",\"DueDate\":\"2015-04-30T16:05:56\",\"FeeCode\":\"9\",\"LotId\":\"73C70537-E1B0-44EA-96CF-A4E301084BAE\",\"Lot\":null,\"InvoiceNo\":\"PI150001P\",\"IsExecuted\":true,\"CustomerBankId\":null,\"CustomerBank\":null,\"UInvoiceCode\":\"类别一\",\"PFA\":\"P\",\"DeliveryOrderNo\":null,\"MT\":\"T\",\"IsWareHouseQuantity\":false,\"IsUserChangedQuantity\":true,\"Major\":6140,\"Premium\":-400,\"Discount4Transportation\":0,\"DiscountMisc\":0,\"AmountNotional\":2870000,\"DueRate\":95,\"DueAmount\":2726500,\"AmountDrafted\":2726500,\"QuantityDrafted\":3,\"PnL\":0,\"IsSettled\":false,\"IsAccounted\":false,\"Is4MultiLots\":false,\"InvoiceType\":0,\"InvalidId\":null,\"InvalidBy\":null,\"InvalidAt\":null,\"Notices\":null,\"LcId\":null,\"LC\":null,\"AdjustId\":null,\"Suffix\":\"P\",\"Currency\":\"USD\",\"Prefix\":\"PI15\",\"TradeDate\":\"2015-03-24T00:00:00\",\"Instance\":null,\"Id\":\"26EAE233-A750-46D3-9772-A4E90109C085\",\"IsDeleted\":false,\"IsHidden\":false,\"CreatedBy\":\"陈振忠\",\"CreatedAt\":\"2015-08-03T16:07:34\",\"UpdatedBy\":\"王小可\",\"UpdatedAt\":\"2015-12-16T16:13:35\",\"DeletedBy\":null,\"DeletedAt\":null,\"Version\":8}";
		String jsonStr2 = "{\"Storages\":null,\"Notices\":null,\"InvoiceGrades\":null,\"Provisionals\":null,\"LcId\":null,\"LC\":null,\"LegalId\":\"8c706b1c-b4d1-420a-9ea8-a3ee00cf40aa\",\"Legal\":null,\"LegalBankId\":null,\"LegalBank\":null,\"ContractId\":\"c92722b7-f2bd-4bc3-8464-a4e300f5f7f8\",\"Contract\":null,\"LotId\":\"73c70537-e1b0-44ea-96cf-a4e301084bae\",\"Lot\":null,\"CommodityId\":\"217036a2-86f7-4d94-8d4d-a3840083d06f\",\"Commodity\":null,\"CustomerId\":\"8c95fa86-fc2d-420c-a6d3-a41f00b12260\",\"Customer\":null,\"CustomerTitleId\":\"3acb4fe9-14c2-4168-8f21-a41f00b12260\",\"CustomerTitle\":null,\"CustomerBankId\":null,\"CustomerBank\":null,\"Positions\":null,\"AdjustId\":null,\"CreatedId\":\"98d251f4-9cdb-4231-9f8b-a43a00f6d73e\",\"UpdatedId\":\"4aeccdbe-6f80-42f7-891c-a4eb011698f7\",\"FullNo\":null,\"CustomerName\":null,\"CustomerTitleName\":null,\"LegalName\":\"SB\",\"CommodityName\":\"铜\",\"CommodityCode\":\"CU\",\"Unit\":\"吨\",\"GrossProfitAmount\":null,\"GrossProfitRate\":null,\"Exposure\":null,\"IsExecuted\":true,\"UInvoiceCode\":\"类别一\",\"FeeCode\":\"9\",\"Currency\":\"USD\",\"PFA\":\"P\",\"DocumentNo\":\"SB1504\",\"DeliveryOrderNo\":null,\"InvoiceNo\":\"PI150001P\",\"Prefix\":\"PI15\",\"SerialNo\":\"0001\",\"Suffix\":\"P\",\"TradeDate\":\"2015-03-24T00:00:00\",\"MT\":\"T\",\"Quantity\":500,\"IsWareHouseQuantity\":false,\"IsUserChangedQuantity\":true,\"Price\":5740,\"Major\":6140,\"Premium\":-400,\"Discount4Transportation\":0,\"DiscountMisc\":0,\"AmountNotional\":2870000,\"Amount\":2870000,\"DueRate\":95,\"DueAmount\":2726500,\"AmountDrafted\":2726500,\"QuantityDrafted\":500,\"PnL\":null,\"DueDate\":\"2015-04-30T16:05:56\",\"Comments\":\"\",\"IsSettled\":false,\"IsAccounted\":false,\"Is4MultiLots\":false,\"SpotDirection\":null,\"InvoiceType\":0,\"InvalidId\":null,\"InvalidBy\":null,\"InvalidAt\":null,\"Id\":\"26eae233-a750-46d3-9772-a4e90109c085\",\"Version\":8,\"IsHidden\":false,\"CreatedBy\":\"陈振忠\",\"CreatedAt\":\"2015-08-03T16:07:34\",\"UpdatedBy\":\"王小可\",\"UpdatedAt\":\"2015-12-16T16:13:35\",\"DeletedBy\":null,\"DeletedAt\":null}";
		
		
		System.out.println(asc(jsonStr));
		System.out.println(asc(jsonStr2));
	}
}
