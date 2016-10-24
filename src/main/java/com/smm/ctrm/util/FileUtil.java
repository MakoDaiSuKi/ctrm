package com.smm.ctrm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

public class FileUtil {

	private static Logger logger = Logger.getLogger(FileUtil.class);

	public static String read() {

		String fullFileName = Thread.currentThread().getContextClassLoader().getResource("javaApiForCSharp.json")
				.getPath();
		File file = new File(fullFileName);
		Scanner scanner = null;
		StringBuilder buffer = new StringBuilder();
		try {
			scanner = new Scanner(file, "utf-8");
			while (scanner.hasNextLine()) {
				buffer.append(scanner.nextLine());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return buffer.toString();
	}

	public static void main(String[] args) {
		wordConvertToPdf("D:\\123.docx", "D:\\12345.pdf");
	}

	public static int wordConvertToPdf(String sourceFile, String destFile) {
		try {
			File inputFile = new File(sourceFile);
			if (!inputFile.exists()) {
				return -1;// 找不到源文件, 则返回-1
			}
			// OpenOffice的安装目录
			// String OpenOffice_HOME =
			// PropertiesUtil.getString("OpenOffice.address");
			// 如果从文件中读取的URL地址最后一个字符不是 '\'，则添加'\'
			/*
			 * if (OpenOffice_HOME.charAt(OpenOffice_HOME.length() - 1) != '\\')
			 * { OpenOffice_HOME += "\\"; }
			 */
			// 启动OpenOffice的服务
			/*
			 * String command = OpenOffice_HOME +
			 * "program\\soffice.exe -headless -accept=\"socket,host="
			 * +PropertiesUtil.getString("OpenOffice.host")+
			 * ",port="+PropertiesUtil.getString("OpenOffice.port")+";urp;\"";
			 */
			// Process pro = Runtime.getRuntime().exec(command);

			OpenOfficeConnection connection = new SocketOpenOfficeConnection(
					PropertiesUtil.getString("OpenOffice.host"), PropertiesUtil.getInteger("OpenOffice.port"));
			connection.connect();

			// 如果目标路径不存在, 则新建该路径
			File outputFile = new File(destFile);
			if (!outputFile.getParentFile().exists()) {
				outputFile.getParentFile().mkdirs();
			}

			// convert
			DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
			converter.convert(inputFile, outputFile);

			// close the connection
			connection.disconnect();

			// 关闭OpenOffice服务的进程
			// pro.destroy();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(sourceFile + "转" + destFile + "失败", e);
		}
		return 1;
	}
}
