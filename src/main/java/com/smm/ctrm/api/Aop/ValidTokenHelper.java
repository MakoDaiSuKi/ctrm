
package com.smm.ctrm.api.Aop;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.smm.ctrm.domain.LoginInfo;
import com.smm.ctrm.domain.apiClient.Token;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.LoginHelper;

/**
 * 
 * @author shihua.zeng
 *
 */
@Component
@Aspect
public class ValidTokenHelper {

	private Logger log = Logger.getLogger(this.getClass());

	@Pointcut("execution(public * com.smm.ctrm.api.*.*.*(..))")
	public void sleeppoint() {
		System.out.println("Aop");
	}

	// private Pattern idPattern =
	// Pattern.compile("$\"[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}\"^");
	private Pattern idPattern = Pattern.compile("^\".+\"$");

	private final String FAILED_VALID_AUTHORIZATION = "Authorization无效";

	private final String FAILED_VALID_ORG = "orgName无效";

	@Around("sleeppoint()")
	public Object process(ProceedingJoinPoint point) {
		try {
			Object[] args = point.getArgs();
			Object targetObj = point.getTarget();
			String methodName = point.getSignature().getName();
			log.info("##开始调用Service:" + targetObj.getClass().getName() + ",##方法为:" + methodName + "##");
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();

			trimDoubleQuotationMarksForGetIdMethod(args);

			/**
			 * 登录
			 */
			if (request.getRequestURI().contains("/api/Auth/Login/Login4Winform")
					|| request.getRequestURI().contains("/api/Auth/Login/Login5Winform")
					|| request.getRequestURI().contains("/api/Basis/ExceptionLog/Save")) {
				return point.proceed(args);
			}

			String authorization = request.getHeader("Authorization");
			if (authorization == null) {
				return new ActionResult<>(false, FAILED_VALID_AUTHORIZATION,"401");
			}

			String[] accessToken = authorization.replaceFirst("Basic_", "").trim().split(",");

			if (accessToken == null || accessToken.length == 0) {
				return new ActionResult<>(false, FAILED_VALID_AUTHORIZATION,"401");
			}
			String account = accessToken[0].split(":")[1];
			if (StringUtils.isBlank(account)) {
				return new ActionResult<>(false, FAILED_VALID_AUTHORIZATION,"401");
			}

			String authToken = accessToken[1].split(":")[1];
			if (StringUtils.isBlank(authToken)) {
				return new ActionResult<>(false, FAILED_VALID_AUTHORIZATION,"401");
			}
			boolean is_org_name_valid = Boolean.FALSE;
			String orgName = "";
			if (accessToken.length >= 3 ){
				orgName = accessToken[2].split(":")[1];
				if(StringUtils.isNotBlank(orgName) && LoginHelper.setDataSource(orgName)){
					is_org_name_valid = Boolean.TRUE;
				}
			}
			if(!is_org_name_valid) {
				return new ActionResult<>(Boolean.FALSE, FAILED_VALID_ORG);
			}
			
			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setAccount(account);
			loginInfo.setAuthToken(authToken);
			
			Token tokenResult = isTokenValid(loginInfo);
			if (tokenResult.getIsValid()) {
				return point.proceed(args);
			} else {
				loginInfo.setOrgName(orgName);
				String tokenExpire = loginInfo + "; token已过期！";
				log.warn(tokenExpire);
				return new ActionResult<>(false, FAILED_VALID_AUTHORIZATION, String.valueOf(tokenResult.getStatus()));
			}

		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			return new ActionResult<>(false, "程序错误," + e.getMessage());
		}
	}

	/**
	 * 删除请求中单个参数的双引号
	 * 
	 * @param args
	 */
	private void trimDoubleQuotationMarksForGetIdMethod(Object[] args) {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof String) {
					String idStr = (String) args[i];
					Matcher m = idPattern.matcher(idStr);
					if (m.find()) {
						args[i] = idStr.substring(1, idStr.length() - 1);
					}
				}
			}
		}
	}

	@Before("sleeppoint()")
	public void beforeSleep(JoinPoint point) {
		Object targetObj = point.getTarget();
		String methodName = point.getSignature().getName();
		log.info("##即将调用Service:" + targetObj.getClass().getName() + ",##方法为:" + methodName + "##");
	}

	@AfterReturning("sleeppoint()")
	public void afterSleep(JoinPoint point) {
		Object targetObj = point.getTarget();
		String methodName = point.getSignature().getName();
		log.info("##结束调用Service:" + targetObj.getClass().getName() + ",##方法为:" + methodName + "##");
	}

	/**
	 * 验证token是否过期
	 * @throws IOException 
	 */
	public Token isTokenValid(LoginInfo loginInfo) throws IOException {
		return LoginHelper.ValidateToken(loginInfo);
	}
}