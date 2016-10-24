
package com.smm.ctrm.domain.apiClient;

import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AttachCombined {
	@JsonProperty(value = "FileName")
	private String FileName;

	
	public String getFileName() {
		return FileName;
	}

	public void setFileName(String value) {
		FileName = value;
	}

	@JsonProperty(value = "FileUrl")
	private String FileUrl;

	
	public String getFileUrl() {
		return FileUrl;
	}
	public void setFileUrl(String value) {
		FileUrl = value;
	}

	@JsonProperty(value = "Stream")
	private Stream Stream;

	
	public Stream getStream() {
		return Stream;
	}

	public void setStream(Stream value) {
		Stream = value;
	}
}