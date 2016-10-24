package com.smm.ctrm.domain;

/**
 * @author yutao.zhao
 *
 */
public enum ParameterDirection {
	/**
	 * 参数是输入参数。
	 */
	Input,
	/**
	 * 参数是输出参数。
	 */
	Output,
	/**
	 * 参数既能输入，也能输出。
	 */
	InputOutput,
	/**
	 * 参数表示诸如存储过程、内置函数或用户定义函数之类的操作的返回值。
	 */
	ReturnValue
}
