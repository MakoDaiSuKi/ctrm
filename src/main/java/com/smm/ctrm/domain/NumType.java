package com.smm.ctrm.domain;

public class NumType {
	private String _Type;
	private String _Function;

	public String NumTypeStr() {
		return _Type + "_" + _Function + "_";
	}

	public String Type() {
		return _Type;
	}

	public String Function() {
		return _Function;
	}

	public NumType(String Type, String Function) {
		this._Type = Type;
		this._Function = Function;
	}
}
