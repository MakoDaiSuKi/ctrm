package com.smm.ctrm.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.smm.ctrm.domain.LoginInfo;
import com.smm.ctrm.domain.LoginInfoToken;
import com.smm.ctrm.domain.apiClient.Token;
import com.smm.ctrm.hibernate.DataSource.DataSourceConfig;
import com.smm.ctrm.hibernate.DataSource.DataSourceContextHolder;

public class LoginHelper {

	private static Logger logger = Logger.getLogger(LoginHelper.class);

	/**
	 * 获取用户信息
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static LoginInfoToken GetLoginInfo(HttpServletRequest request) {

		LoginInfoToken loginInfo = null;
		if (request != null) {
			String authorization = request.getHeader("Authorization");
			if (authorization == null)
				return null;

			String account = authorization.replaceFirst("Basic_", "").trim().split(",")[0].split(":")[1];
			String cacheKey = GetCacheKey(account);
			/**
			 * 从缓存获取用户信息
			 */
//			String token = RedisUtil.get(cacheKey);

			String token = null;
			if (token != null && StringUtils.isNotBlank(token)) {
				try {
					loginInfo = (LoginInfoToken) JSONUtil.doConvertStringToBean(token, LoginInfoToken.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return loginInfo;
	}

	/**
	 * 根据当前线程获取绑定request 获取用户信息
	 * 
	 * @return
	 * @throws IOException
	 */
	public static LoginInfoToken GetLoginInfo() {
		ServletRequestAttributes sra = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
		if(sra != null) {
			return GetLoginInfo(sra.getRequest());
		}
		return null;
	}

	/**
	 * 登录
	 * 
	 * @throws Exception
	 */
	public static void SignIn(LoginInfo loginInfo, int expireMin) throws Exception {
		try {
			String token = GetToken(loginInfo.getAccount());
			loginInfo.setAuthToken(token);
			LoginInfoToken loginInfoToken = new LoginInfoToken();
			BeanUtils.copyProperties(loginInfo, loginInfoToken);
			String json = JSONUtil.doConvertBeanToString(loginInfoToken);
			RedisUtil.set(GetCacheKey(loginInfo.getAccount()), json, expireMin);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 暂时过渡登录
	 * 
	 * @throws Exception
	 */
	public static void SignIn2(LoginInfo loginInfo, int expireMin) throws Exception {
		try {
			LoginInfoToken loginInfoToken = new LoginInfoToken();
			BeanUtils.copyProperties(loginInfo, loginInfoToken);
			String json = JSONUtil.doConvertBeanToString(loginInfoToken);
			logger.info("准备存入redis.");
//			RedisUtil.set(GetCacheKey(loginInfo.getAccount()), json, expireMin);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SignIn2方法异常:", e);
		}

	}

	/**
	 * 注销
	 * 
	 * @param loginInfo
	 */
	public static void SingOut(LoginInfoToken loginInfo) {
		String cacheKey = GetCacheKey(loginInfo.getAccount());
//		RedisUtil.delete(cacheKey);
	}

	/**
	 * 缓存key转换为大写
	 * 
	 * @param loginName
	 * @return
	 */
	public static String GetCacheKey(String loginName) {
		return String.format("_AUTH_{%s}_{%s}_", DataSourceContextHolder.getDataSourceName(), loginName.toUpperCase());
	}

	/**
	 * 服务器端生成用户Token Token由Uid + Sid构成，Uid经过用域名进行MD5加密
	 * 
	 * @param loginName
	 * @return
	 * @throws Exception
	 */
	public static String GetToken(String loginName) throws Exception {
		// 生成用户验证token
		final String key = "www.hedgestudio.com";
		String uid = EncryptHelper.AESEncrypt(loginName, key);
		String sid = EncryptHelper.DESEncrypt(GetUniqueId());
		String md5 = MD5Util.MD5(uid + sid);
		String token = md5.substring(Math.min(loginName.length(), 10), 16);
		return token;
	}

	/**
	 * 获取唯一的Token ID
	 * 
	 * @return
	 */
	private static String GetUniqueId() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String r = request.getHeader("user-agent") + request.getRemoteAddr() + request.getRemoteHost()
				+ request.getRemotePort();
		return r;
	}

	/**
	 * 验证客户端Token是否正确
	 * 
	 * @param clientLoginInfo
	 * @return
	 * @throws IOException
	 */
	public static Token ValidateToken(LoginInfo clientLoginInfo) throws IOException {
		Token tokenResult = new Token();
		// 用户是否登录
		String cacheKey = GetCacheKey(clientLoginInfo.getAccount());
//		String token = RedisUtil.get(cacheKey);
		String token = null;
		if (token == null || StringUtils.isBlank(token)) {
			tokenResult.setIsValid(false);
			tokenResult.setStatus(Token.UNAUTHORIZED);
			return tokenResult;
		}

		LoginInfoToken loginInfo = new LoginInfoToken();
		loginInfo = (LoginInfoToken) JSONUtil.doConvertStringToBean(token, LoginInfoToken.class);
		boolean isEqual = loginInfo.getAuthToken().equals(clientLoginInfo.getAuthToken());
		tokenResult.setIsValid(isEqual);
		if (isEqual) {
			tokenResult.setStatus(Token.NORMAL);
		} else {
			tokenResult.setStatus(Token.FORBIDDEN);
		}
		return tokenResult;
	}

	/**
	 * 根据request中header头的org变量确定数据源
	 * 
	 * @param request
	 */
	public static boolean setDataSource(String orgName) {
		String decodeOrgName = orgName;
		try {
			decodeOrgName = URLDecoder.decode(orgName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(orgName + "：转码失败，" + e.getMessage(), e);
		}
		if (StringUtils.isBlank(decodeOrgName) || !DataSourceConfig.getDatasourcenamelist().contains(decodeOrgName)) {
			logger.warn(orgName + ", orgName:" + decodeOrgName + "无效");
			return false;
		}
		DataSourceContextHolder.setDataSourceType(decodeOrgName);
		return true;
	}
}
