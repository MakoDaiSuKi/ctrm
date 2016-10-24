package com.smm.ctrm.util;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.smm.ctrm.domain.apiClient.DotNetToJavaStringHelper;

public final class StringHelper {
	private static final Pattern RegexBr = Pattern.compile("\r|\n");

	/**
	 * 首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String ToTitleCase(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * 返回字符串真实长度, 1个汉字长度为2
	 * 
	 * @return
	 */
	public static int GetStringLength(String str) {
		return str.getBytes().length;
	}

	/**
	 * 判断指定字符串在指定字符串数组中的位置
	 * 
	 * @param strSearch
	 *            字符串
	 * @param stringArray
	 *            字符串数组
	 * @param caseInsensetive
	 *            是否不区分大小写, true为不区分, false为区分
	 * @return 字符串在指定字符串数组中的位置, 如不存在则返回-1
	 */
	public static int GetInArrayId(String strSearch, String[] stringArray, boolean caseInsensetive) {
		for (int i = 0; i < stringArray.length; i++) {
			if (caseInsensetive) {
				if (strSearch.toLowerCase().equals(stringArray[i].toLowerCase())) {
					return i;
				}
			} else {
				if (strSearch.equals(stringArray[i])) {
					return i;
				}
			}

		}
		return -1;
	}

	/**
	 * 清除给定字符串中的回车及换行符
	 * 
	 * @param str
	 *            要清除的字符串
	 * @return 清除后返回的字符串
	 */
	public static String ClearBR(String str) {
		Matcher m = RegexBr.matcher(str);
		return m.replaceAll("");
	}

	/**
	 * 字符串如果操过指定长度则将超出的部分用指定字符串代替
	 * 
	 * @param p_SrcString
	 *            要检查的字符串
	 * @param p_Length
	 *            指定长度
	 * @param p_TailString
	 *            用于替换的字符串
	 * @return 截取后的字符串
	 */
	public static String GetSubString(String p_SrcString, int p_Length, String p_TailString) {
		return GetSubString(p_SrcString, 0, p_Length, p_TailString);
	}

	/**
	 * 取指定长度的字符串
	 * 
	 * @param p_SrcString
	 *            要检查的字符串
	 * @param p_StartIndex
	 *            起始位置
	 * @param p_Length
	 *            指定长度
	 * @param p_TailString
	 *            用于替换的字符串
	 * @return 截取后的字符串
	 */
	public static String GetSubString(String p_SrcString, int p_StartIndex, int p_Length, String p_TailString) {
		String myResult = p_SrcString;
		Pattern jap = Pattern.compile("[\u0800-\u4e00]+");
		Pattern kor = Pattern.compile("[\uAC00-\uD7A3]+");
		// 当是日文或韩文时(注:中文的范围:\u4e00 - \u9fa5, 日文在\u0800 - \u4e00,
		// 韩文为\xAC00-\xD7A3)
		if (jap.matcher(p_SrcString).find() || kor.matcher(p_SrcString).find()) {
			// 当截取的起始位置超出字段串长度时
			if (p_StartIndex >= p_SrcString.length()) {
				return "";
			} else {
				return p_SrcString.substring(p_StartIndex,
						((p_Length + p_StartIndex) > p_SrcString.length()) ? (p_SrcString.length()) : p_Length);
			}
		}

		if (p_Length >= 0) {
			byte[] bsSrcString = null;
			try {
				bsSrcString = p_SrcString.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			// 当字符串长度大于起始位置
			if (bsSrcString.length > p_StartIndex) {
				int p_EndIndex = bsSrcString.length;

				// 当要截取的长度在字符串的有效长度范围内
				if (bsSrcString.length > (p_StartIndex + p_Length)) {
					p_EndIndex = p_Length + p_StartIndex;
				} else { // 当不在有效范围内时,只取到字符串的结尾

					p_Length = bsSrcString.length - p_StartIndex;
					p_TailString = "";
				}

				int nRealLength = p_Length;
				int[] anResultFlag = new int[p_Length];
				byte[] bsResult = null;

				int nFlag = 0;
				for (int i = p_StartIndex; i < p_EndIndex; i++) {

					if (bsSrcString[i] > 127) {
						nFlag++;
						if (nFlag == 3) {
							nFlag = 1;
						}
					} else {
						nFlag = 0;
					}

					anResultFlag[i] = nFlag;
				}

				if ((bsSrcString[p_EndIndex - 1] > 127) && (anResultFlag[p_Length - 1] == 1)) {
					nRealLength = p_Length + 1;
				}

				bsResult = new byte[nRealLength];

				System.arraycopy(bsSrcString, p_StartIndex, bsResult, 0, nRealLength);

				try {
					myResult = new String(bsResult, "utf-8");
				} catch (UnsupportedEncodingException e) {
					myResult = new String(bsResult);
					e.printStackTrace();
				}
				myResult = myResult + p_TailString;
			}
		}
		return myResult;
	}

	/**
	 * 自定义的替换字符串函数
	 * 
	 *//*
		 * public static String ReplaceString(String SourceString, String
		 * SearchString, String ReplaceString, boolean IsCaseInsensetive) {
		 * return Regex.Replace(SourceString, Regex.Escape(SearchString),
		 * ReplaceString, IsCaseInsensetive ? RegexOptions.IgnoreCase :
		 * RegexOptions.None); }
		 */

	/**
	 * 分割字符串
	 * 
	 */
	public static String[] SplitString(String strContent, String strSplit) {
		if (strContent.indexOf(strSplit) < 0) {
			String[] tmp = { strContent };
			return tmp;
		}

		return strContent.split(strSplit);
	}

	/**
	 * 分割字符串
	 * 
	 * @param strContent
	 *            待分割字符串
	 * @param strSplit
	 *            分隔符
	 * @param p_3
	 *            指定分割数组大小
	 * @return string[]
	 */
	public static String[] SplitString(String strContent, String strSplit, int p_3) {
		String[] result = new String[p_3];

		String[] splited = SplitString(strContent, strSplit);

		for (int i = 0; i < p_3; i++) {
			if (i < splited.length) {
				result[i] = splited[i];
			} else {
				result[i] = "";
			}
		}

		return result;
	}

	/**
	 * 返回 HTML 字符串的编码结果
	 * 
	 * @param str
	 *            字符串
	 * @return 编码结果
	 */
	public static String HtmlEncode(String str) {
		return StringUtil.doEncoder(str);
	}

	/**
	 * 返回 HTML 字符串的解码结果
	 * 
	 * @param str
	 *            字符串
	 * @return 解码结果
	 */
	public static String HtmlDecode(String str) {
		return StringUtil.doDecoder(str);
	}

	/**
	 * 返回 URL 字符串的编码结果
	 * 
	 * @param str
	 *            字符串
	 * @return 编码结果
	 */
	public static String UrlEncode(String str) {
		return StringUtil.doEncoder(str);
	}

	/**
	 * 返回 URL 字符串的编码结果
	 * 
	 * @param str
	 *            字符串
	 * @return 解码结果
	 */
	public static String UrlDecode(String str) {
		return StringUtil.doDecoder(str);
	}

	/**
	 * 返回指定目录下的非 UTF8 字符集文件
	 * 
	 * @param Path
	 *            路径
	 * @return 文件名的字符串数组
	 */
	public static String[] FindNoUTF8File(String Path) {

		ArrayList<String> filelist = new ArrayList<String>();
		File folder = new File(Path);

		if (!folder.isDirectory()) {
			return new String[] {};
		}
		File[] subFiles = folder.listFiles();
		for (int j = 0; j < subFiles.length; j++) {
			String fileName = subFiles[j].getName();
			if (fileName.substring(fileName.lastIndexOf(".")).equalsIgnoreCase(".htm")) {
				boolean bUtf8 = IsUTF8(subFiles[j]);
				if (!bUtf8) {
					filelist.add(subFiles[j].getName());
				}
			}
		}
		return filelist.toArray(new String[] {});

	}

	// 0000 0000-0000 007F - 0xxxxxxx (ascii converts to 1 octet!)
	// 0000 0080-0000 07FF - 110xxxxx 10xxxxxx ( 2 octet format)
	// 0000 0800-0000 FFFF - 1110xxxx 10xxxxxx 10xxxxxx (3 octet format)

	/**
	 * 判断文件流是否为UTF8字符集
	 * 
	 * @param file
	 * @return 判断结果
	 */
	private static boolean IsUTF8(File file) {
		try {
			return fileCodeString(file).equalsIgnoreCase("UTF-8") ? Boolean.TRUE : Boolean.FALSE;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断文件的编码格式
	 * 
	 * @param file
	 * @return 文件编码格式
	 * @throws Exception
	 */
	public static String fileCodeString(File file) throws Exception {
		if (file == null || !file.exists()) {
			return null;
		}
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
		int p = (bin.read() << 8) + bin.read();
		String code = null;
		// 其中的0xefbb、0xfffe、0xfeff、0x5c75这些都是这个文件的前面两个字节的16进制数
		switch (p) {
		case 0xefbb:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "Unicode";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		case 0x5c75:
			code = "ANSI|ASCII";
			break;
		default:
			code = "GBK";
			break;
		}
		return code;
	}

	/**
	 * 将全角数字转换为数字
	 * 
	 * @param SBCCase
	 * @return
	 */
	public static String SBCCaseToNumberic(String SBCCase) {
		String result = null;
		if (SBCCase != null) {
			StringBuilder sb = new StringBuilder(SBCCase);
			for (int i = 0; i < sb.length(); i++) {
				int c = (int) sb.charAt(i);
				if (c >= 0xFF10 && c <= 0xFF19) {
					sb.setCharAt(i, (char) (c - 0xFEE0));
				}
			}
			result = sb.toString();

		}
		return result;
	}

	// ===================================================================================================
	/**
	 * 将字符串转换为Color
	 * 
	 * @param color
	 * @return
	 */
	public static Color ToColor(String color) {
		int red, green, blue = 0;
		char[] rgb;
		color = DotNetToJavaStringHelper.trimStart(color, '#');
		color = Pattern.compile("[g-zG-Z]").matcher(color.toLowerCase()).replaceAll("");
		switch (color.length()) {
		case 3:
			rgb = color.toCharArray();
			red = Integer.parseInt((new Character(rgb[0])).toString() + (new Character(rgb[0])).toString(), 16);
			green = Integer.parseInt((new Character(rgb[1])).toString() + (new Character(rgb[1])).toString(), 16);
			blue = Integer.parseInt((new Character(rgb[2])).toString() + (new Character(rgb[2])).toString(), 16);

			return new Color(red, green, blue);
		case 6:
			rgb = color.toCharArray();
			red = Integer.parseInt((new Character(rgb[0])).toString() + (new Character(rgb[1])).toString(), 16);
			green = Integer.parseInt((new Character(rgb[2])).toString() + (new Character(rgb[3])).toString(), 16);
			blue = Integer.parseInt((new Character(rgb[4])).toString() + (new Character(rgb[5])).toString(), 16);
			return new Color(red, green, blue);
		default:
			return Color.getColor(color);

		}
	}

	/**
	 * 格式化字节数字符串（用于显示文件大小）
	 * 
	 * @param bytes
	 * @return
	 */
	public static String FormatBytesStr(int bytes) {
		if (bytes > 1073741824) {
			return (new Double(bytes / 1073741824)).toString() + "G";
		}
		if (bytes > 1048576) {
			return (new Double(bytes / 1048576)).toString() + "M";
		}
		if (bytes > 1024) {
			return (new Double(bytes / 1024)).toString() + "K";
		}
		return (new Integer(bytes)).toString() + "Bytes";
	}
}