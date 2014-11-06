package org.franwork.core.util.file;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Frankie
 *
 */
public final class ZipFile {
	
	private Logger _logger = LoggerFactory.getLogger(this.getClass());
	
	private File fileEntry;
	
	private String fileName;
	
	public ZipFile(File fileEntry) {
		this.fileEntry = fileEntry;
	}
	
	public ZipFile(File fileEntry, String fileName) {
		this.fileEntry = fileEntry;
		this.fileName = fileName;
	}

	public File getFileEntry() {
		return fileEntry;
	}

	public void setFileEntry(File fileEntry) {
		this.fileEntry = fileEntry;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public File getZipFile() {
		if (this.fileEntry == null) {
			_logger.error("ZipFile's File Entry is NULL.");
			return null;
		}
		return this.fileEntry;
	}

	public String getZipFileName() {
		if (this.fileEntry == null) {
			return StringUtils.EMPTY;
		}
		return (StringUtils.isBlank(this.fileName) ? this.fileEntry.getName() : this.fileName);
	}
}
