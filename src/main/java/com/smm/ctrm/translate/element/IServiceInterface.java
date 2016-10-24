package com.smm.ctrm.translate.element;

public interface IServiceInterface {
	
	String METHOD_WITH_PARAMS = "methodWithParams";
	String METHOD_WITHOUT_PARAMS = "methodWithoutParams";
	String VOID_METHOD_WITH_PARAMS = "voidMethodWithParams";
	String VOID_METHOD_WITHOUT_PARAMS = "voidMethodWithoutParams";
	
	/*String methodWithParamsReg = "\\s*public\\s{0,2}ActionResult<([a-zA-Z0-9<>\\?\\.]+)>\\s{0,2}([a-zA-Z]+)\\(([a-zA-Z0-9<>\\?\\.]+)\\s{0,2}([a-zA-Z]+)\\)";
	
	Pattern methodWithParamsPattern = Pattern.compile(methodWithParamsReg);
	
	String methodWithoutParamsReg = "\\s*public\\s{0,2}ActionResult<([a-zA-Z0-9<>\\?\\.]+)>\\s{0,2}([a-zA-Z]+)\\(\\s*\\)";
	
	Pattern methodWithoutParamsPattern = Pattern.compile(methodWithoutParamsReg);
	
	String voidMethodWithParamsReg = "\\s*public\\s{0,2}void\\s{0,2}([a-zA-Z]+)\\(([a-zA-Z0-9<>\\?\\.]+)\\s{0,2}([a-zA-Z]+)\\)";
	
	Pattern voidMethodWithParamsPattern = Pattern.compile(voidMethodWithParamsReg);
	
	String voidMethodWithoutParamsReg = "\\s*public\\s{0,2}void\\s{0,2}([a-zA-Z]+)\\(\\s*\\)";
	
	Pattern voidMethodWithoutParamsPattern = Pattern.compile(voidMethodWithParamsReg);*/
	
	String PREFIX_PATH = "F:/service";
	
}
