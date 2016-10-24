package com.smm.ctrm.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;

/**
 * @author zhaoyutao
 *
 */
public class XWPFUtil {
	
	/** 引用 复制
	 * @param source
	 * @param target
	 */
	public static void CopyTable(XWPFTable source, XWPFTable target) {
		target.getCTTbl().setTblGrid(source.getCTTbl().getTblGrid());
		target.getCTTbl().setTblPr(source.getCTTbl().getTblPr());
		target.getCTTbl().setTrArray(source.getCTTbl().getTrArray());
	}
	
	/**
	 * @param source
	 * @param target
	 */
	public static void CloneTable(XWPFTable source, XWPFTable target) {
		for (int i = 0; i < target.getNumberOfRows(); i++) {
			target.removeRow(i);
		}
		
		target.getCTTbl().setTblGrid(source.getCTTbl().getTblGrid());
		target.getCTTbl().setTblPr(source.getCTTbl().getTblPr());
		target.getCTTbl().getTblPr().setTblBorders(source.getCTTbl().getTblPr().getTblBorders());
		int sourceRowNum = source.getRows().size();
		for (int i = 0; i < sourceRowNum; i++) {
			target.createRow();
		}
		// 复制单元格
		for (int i = 0; i < target.getRows().size(); i++) {
			CopytTableRow(source.getRow(i), target.getRow(i));
		}
	}
	
	public static void CopytTableRow(XWPFTableRow source, XWPFTableRow target) {
		int sourceCellNum = source.getTableCells().size();
		for (int i = 0; i < sourceCellNum; i++) {
			target.addNewTableCell();
		}
		// 复制样式
		target.getCtRow().setTrPr(source.getCtRow().getTrPr());
		target.getCtRow().setTblPrEx(source.getCtRow().getTblPrEx());
		// 复制单元格
		for (int i = 0; i < sourceCellNum; i++) {
			copyTableCell(source.getCell(i), target.getCell(i));
		}
	}

	private static void copyTableCell(XWPFTableCell source, XWPFTableCell target) {
		// 列属性
		target.getCTTc().setTcPr(source.getCTTc().getTcPr());
		// 删除目标 targetCell 所有单元格
		for (int pos = 0; pos < target.getParagraphs().size(); pos++) {
			target.removeParagraph(pos);
		}
		// 添加段落
		for (XWPFParagraph sp : source.getParagraphs()) {
			XWPFParagraph targetP = target.addParagraph();
			copyParagraph(sp, targetP);
		}
	}

	public static void copyParagraph(XWPFParagraph source, XWPFParagraph target) {
		// 设置段落样式
		target.getCTP().setPPr(source.getCTP().getPPr());
		// 添加Run标签
		for (int pos = 0; pos < target.getRuns().size(); pos++) {
			target.removeRun(pos);
		}
		List<XWPFRun> runs = source.getRuns();
		for (XWPFRun s : runs) {
			XWPFRun targetrun = target.createRun();
			CopyRun(targetrun, s);
		}
	}

	private static void CopyRun(XWPFRun target, XWPFRun source) {
		try {
			target.getCTR().setRPr(source.getCTR().getRPr());
			for(XWPFPicture pic : source.getEmbeddedPictures()) {
				if(pic.getPictureData() != null) {
					InputStream inputStream = pic.getPictureData().getPackagePart().getInputStream();
					int width = (int) pic.getCTPicture().getSpPr().getXfrm().getExt().getCx();
					int height = (int) pic.getCTPicture().getSpPr().getXfrm().getExt().getCy();
					String picName = pic.getCTPicture().getNvPicPr().getCNvPr().getDescr();
					target.addPicture(inputStream, XWPFDocument.PICTURE_TYPE_JPEG, picName, width, height);
				}
			}
			// 设置文本
			target.setText(source.text());
			CTEmpty[] tabArr = source.getCTR().getTabArray();
			for(int i =0; i < tabArr.length; i++) {
				target.addTab();
			}
		} catch (Exception ex) {

		}
	}
}
