package com.smm.ctrm.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

public class EntityUtil {

	public static void main(String[] args)
			throws SecurityException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		// Basis Maintain Physical Report
		File file = new File("D:\\git\\ctrm\\src\\main\\java\\com\\smm\\ctrm\\domain\\Basis");
		File[] list = file.listFiles();
		for (int f = 0; f < list.length; f++) {
			String fileNmae = list[f].getName().replaceAll("[.][^.]+$", "");
			Field[] fds = Class.forName("com.smm.ctrm.domain.Basis." + fileNmae).getDeclaredFields();
			FileWriter writer;
			try {
				writer = new FileWriter("D:\\property\\Basis\\" + fileNmae + ".java");
				for (int i = 0; i < fds.length; i++) {

					switch (fds[i].getType().getName()) {
					case "java.lang.String":
						writer.write(
								"@JsonProperty(value = " + "\"" + toUpperCaseFirstOne(fds[i].getName()) + "\"" + ")");
						writer.write("\r\n");
						writer.write("private String " + fds[i].getName() + ";");
						writer.write("\r\n");
						break;
					case "java.util.Date":
						writer.write(
								"@JsonProperty(value = " + "\"" + toUpperCaseFirstOne(fds[i].getName()) + "\"" + ")");
						writer.write("\r\n");
						writer.write("private Date " + fds[i].getName() + ";");
						writer.write("\r\n");
						break;
					case "java.math.BigDecimal":
						writer.write(
								"@JsonProperty(value = " + "\"" + toUpperCaseFirstOne(fds[i].getName()) + "\"" + ")");
						writer.write("\r\n");
						writer.write("private BigDecimal " + fds[i].getName() + ";");
						writer.write("\r\n");
						break;
					case "java.lang.Boolean":
						writer.write(
								"@JsonProperty(value = " + "\"" + toUpperCaseFirstOne(fds[i].getName()) + "\"" + ")");
						writer.write("\r\n");
						writer.write("private Boolean " + fds[i].getName() + ";");
						writer.write("\r\n");
						break;
					case "java.io.Serializable":
						writer.write(
								"@JsonProperty(value = " + "\"" + toUpperCaseFirstOne(fds[i].getName()) + "\"" + ")");
						writer.write("\r\n");
						writer.write("private Serializable " + fds[i].getName() + ";");
						writer.write("\r\n");
						break;
					case "int":
						writer.write(
								"@JsonProperty(value = " + "\"" + toUpperCaseFirstOne(fds[i].getName()) + "\"" + ")");
						writer.write("\r\n");
						writer.write("private int " + fds[i].getName() + ";");
						writer.write("\r\n");
						break;
					case "java.lang.Integer":
						writer.write(
								"@JsonProperty(value = " + "\"" + toUpperCaseFirstOne(fds[i].getName()) + "\"" + ")");
						writer.write("\r\n");
						writer.write("private Integer " + fds[i].getName() + ";");
						writer.write("\r\n");
						break;
					default:
						break;
					}

				}
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// 首字母转大写
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}
}
