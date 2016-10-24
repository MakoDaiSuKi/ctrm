package com.smm.ctrm.domain.Physical;

public class FileInfoUpload {

	private String FileName;

	private String FileSize;

	private String UploadDate;

	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public String getFileSize() {
		return FileSize;
	}

	public void setFileSize(String fileSize) {
		FileSize = fileSize;
	}

	public String getUploadDate() {
		return UploadDate;
	}

	public void setUploadDate(String uploadDate) {
		UploadDate = uploadDate;
	}
}
