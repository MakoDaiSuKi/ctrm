package com.smm.ctrm.util;

import java.util.ArrayList;

public class KeyObj {

	ArrayList<Object> keys;

	public KeyObj(Object... objs) {
		keys = new ArrayList<Object>();
		for (int i = 0; i < objs.length; i++) {
			keys.add(objs[i]);
		}
	}

	@Override
	public String toString() {
		return "KeyObj [keys=" + keys + "]";

	}

	public ArrayList<Object> getKeys() {
		return keys;
	}

	public void setKeys(ArrayList<Object> keys) {
		this.keys = keys;
	}

}
