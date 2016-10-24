package com.smm.ctrm.translate.element;

import java.util.ArrayList;
import java.util.List;

public class ServiceInterface {
	
	List<ServiceMethod> list = new ArrayList<>();
	
	String folderName;
	
	String serviceName;

	public List<ServiceMethod> getList() {
		return list;
	}

	public void setList(List<ServiceMethod> list) {
		this.list = list;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return "ServiceInterface [list=" + list + ", folderName=" + folderName + ", serviceName=" + serviceName + "]";
	}
	
	
}
