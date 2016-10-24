package com.smm.ctrm.translate.util;

public class Statistics {
	private String json;

	public Statistics(String json) {
		this.json = json;
	}

	public int letters() {
		int count = 0;
		int cpCount = json.codePointCount(0, json.length());
		for (int i = 0; i < cpCount; i++) {
			int index = json.offsetByCodePoints(0, i);
			int cp = json.codePointAt(index);
			if((cp >= 0x41 && cp <= 0x5a) || (cp >= 0x61 && cp <= 0x7a)) {
				count++;
			}
		}
		return count;
	}
	public int numbers(){
		int count = 0;
		int cpCount = json.codePointCount(0, json.length());
		for (int i = 0; i < cpCount; i++) {
			int index = json.offsetByCodePoints(0, i);
			int cp = json.codePointAt(index);
			if(cp >= 0x30 && cp <= 0x39) {
				count++;
			}
		}
		return count;
	}
	public int lettersAndNumbers() {
		int count = 0;
		int cpCount = json.codePointCount(0, json.length());
		for (int i = 0; i < cpCount; i++) {
			int index = json.offsetByCodePoints(0, i);
			int cp = json.codePointAt(index);
			if((cp >= 0x30 && cp <= 0x39) || (cp >= 0x41 && cp <= 0x5a) || (cp >= 0x61 && cp <= 0x7a)) {
				count++;
			}
		}
		return count;
	}
	
	
	public int quationMarks() {
		int count = 0;
		int cpCount = json.codePointCount(0, json.length());
		for (int i = 0; i < cpCount; i++) {
			int index = json.offsetByCodePoints(0, i);
			int cp = json.codePointAt(index);
			if(cp == 34) {
				count++;
			}
//			if (!Character.isSupplementaryCodePoint(cp)) {
//				System.out.println((char) cp);
//			} else {
//				System.out.println(cp);
//			}
		}
		return count;
	}

	public static void main(String[] args) {
		Statistics json = new Statistics("aAXy23a3565Z");
		System.out.println(json.lettersAndNumbers());
		
		
	}

}
