package com.smm.ctrm.domain.apiClient;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutDocumentFile {
	
	public OutDocumentFile() {
		super();
	}
	
	public OutDocumentFile(boolean success, String message) {
		super();
		Success = success;
		Message = message;
	}

	@JsonProperty("Success")
	private boolean Success;
	
	@JsonProperty("Message")
    private String Message;

    /**
     * 模板文件夹路径（绝对路径）
     */
	@JsonProperty("TempFolder")
    private String TempFolder;
    /**
     * 模板文件名称+扩展名
     */
	@JsonProperty("TempFileFullName")
    private String TempFileFullName;
    /**
     * 模板文件名称(不包含扩展名)
     */
	@JsonProperty("TempFileName")
    private String TempFileName;
    /**
     * 模板文件（绝对路径）
     */
	@JsonProperty("TempPath")
    private String TempPath;
    /**
     * 保存文件名称(不包含扩展名)
     */
	@JsonProperty("FileName")
    private String FileName;

    /**
     * 保存文件名称+扩展名
     */
	@JsonProperty("FileFullName")
    private String FileFullName;
    /**
     * 保存文件绝对文件夹路径
     */
	@JsonProperty("FileFolder")
    private String FileFolder;
    /**
     * 保存文件相对文件夹路径
     */
	@JsonProperty("FileRelFolder")
    private String FileRelFolder;
    /**
     * 文件路径
     */
	@JsonProperty("FilePath")
    private String FilePath;
    /**
     * PDF 文件名称+扩展名
     */
	@JsonProperty("FilePDFFullName")
    private String FilePDFFullName;
	
	/**
     * PDF 文件名称+扩展名
     */
	@JsonProperty("TemporarilyFilePath")
    private String TemporarilyFilePath;
	
	public boolean isSuccess() {
		return Success;
	}
	public void setSuccess(boolean success) {
		Success = success;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getTempFolder() {
		return TempFolder;
	}
	public void setTempFolder(String tempFolder) {
		TempFolder = tempFolder;
	}
	public String getTempFileFullName() {
		return TempFileFullName;
	}
	public void setTempFileFullName(String tempFileFullName) {
		TempFileFullName = tempFileFullName;
	}
	public String getTempFileName() {
		return TempFileName;
	}
	public void setTempFileName(String tempFileName) {
		TempFileName = tempFileName;
	}
	public String getTempPath() {
		return TempPath;
	}
	public void setTempPath(String tempPath) {
		TempPath = tempPath;
	}
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public String getFileFullName() {
		return FileFullName;
	}
	public void setFileFullName(String fileFullName) {
		FileFullName = fileFullName;
	}
	public String getFileFolder() {
		return FileFolder;
	}
	public void setFileFolder(String fileFolder) {
		FileFolder = fileFolder;
	}
	public String getFileRelFolder() {
		return FileRelFolder;
	}
	public void setFileRelFolder(String fileRelFolder) {
		FileRelFolder = fileRelFolder;
	}
	public String getFilePath() {
		return FilePath;
	}
	public void setFilePath(String filePath) {
		FilePath = filePath;
	}
	public String getFilePDFFullName() {
		return FilePDFFullName;
	}
	public void setFilePDFFullName(String filePDFFullName) {
		FilePDFFullName = filePDFFullName;
	}

	public String getTemporarilyFilePath() {
		return TemporarilyFilePath;
	}

	public void setTemporarilyFilePath(String temporarilyFilePath) {
		TemporarilyFilePath = temporarilyFilePath;
	}
}
