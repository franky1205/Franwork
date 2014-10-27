package org.franwork.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.franwork.core.util.file.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FileUtils {

	/**
	 * The private static org.slf4j.Logger object.
	 */
	private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	/**
	 * Constant of Zip File name extenstion.
	 */
	public static final String ZIP_EXTENSION = "zip";
	
	/**
	 * Constant of Zip File Directory separator
	 */
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	private FileUtils() {
		super();
	}
	
	/**
	 * Zip Directory's Files content.
	 * 
	 * @param directory
	 * @param outStream
	 * @throws IOException
	 */
	public static void zipDirectories(File directory, OutputStream outStream) throws IOException {
		if (directory == null || !directory.isDirectory()) {
			logger.error("Compress Directory is null or not valid directory.");
			return ;
		}
		FileUtils.zipFiles(Arrays.asList(directory.listFiles()), outStream);
	}
	
	/**
	 * File Collection zip to OutputStream.
	 * 
	 * @param files
	 * @param outStream
	 * @throws IOException
	 */
	public static void zipFiles(Collection<File> files, OutputStream outStream) throws IOException {
		if (CollectionUtils.isEmpty(files)) {
			logger.error("Compress File Collection is Empty.");
			return ;
		}
		if (outStream == null) {
			logger.error("Compress File Output Stream not given.");
			return ;
		}
		ZipOutputStream zipOut = new ZipOutputStream(outStream);
		for (File file : files) {
			compressFileObj(file, zipOut, file.getName());
		}
		IOUtils.closeQuietly(zipOut);
		logger.debug("Files Zip Successfully.");
	}
	
	/**
	 * ZipFile Collection zip to OutputStream
	 * 
	 * @param zipFiles
	 * @param outStream
	 * @throws IOException
	 */
	public static void zipEntryFiles(Collection<ZipFile> zipFiles, OutputStream outStream) throws IOException {
		if (CollectionUtils.isEmpty(zipFiles)) {
			logger.error("Zip File Entry Collection is Empty.");
			return ;
		}
		if (outStream == null) {
			logger.error("Compress File Output Stream not given.");
			return ;
		}
		ZipOutputStream zipOut = new ZipOutputStream(outStream);
		for (ZipFile zipFile : zipFiles) {
			if (zipFile == null) {
				logger.error("Null Zip File in Collection.");
				continue;
			}
			compressFileObj(zipFile.getZipFile(), zipOut, zipFile.getZipFileName());
		}
		IOUtils.closeQuietly(zipOut);
		logger.debug("Entry Files Zip Successfully.");
	}
	
	private static void compressFileObj(File fileObj, ZipOutputStream zipOut, String zipFileName) throws IOException {
		if (fileObj == null) {
			logger.error("Null File Object Compression.");
			return ;
		}
		if (fileObj.isDirectory()) {
			compressDirectory(fileObj, zipOut, zipFileName);
		} else if (fileObj.isFile()) {
			compressFile(fileObj, zipFileName, zipOut, StringUtils.EMPTY);
		} else { 
			logger.error("File type unrecognized Error : " + fileObj.getAbsolutePath());
		}
	}
	
	private static void compressDirectory(File directory, ZipOutputStream zipOut, String rootDirectory) throws IOException {
		if (directory == null || !directory.isDirectory()) {
			logger.error("Directory is Not exist");
			return ;
		}
		for (File file : Arrays.asList(directory.listFiles())) {
			if (file.isDirectory()) {
				FileUtils.compressDirectory(file, zipOut, rootDirectory + FILE_SEPARATOR + file.getName());
			} else if (file.isFile()) {
				FileUtils.compressFile(file, file.getName(), zipOut, rootDirectory);
			} else { 
				logger.error("File type unrecognized Error : " + file.getAbsolutePath());
			}
		}
	}
	
	private static void compressFile(File file, String zipFileName, 
			ZipOutputStream zipOut, String rootDirectory) throws IOException {
		if (file == null || !file.isFile()) {
			logger.error("The Given File Object is not valid.");
			return ;
		}
		byte[] readArray = new byte[1024];
		String entryPath = (StringUtils.isBlank(rootDirectory) ? 
				StringUtils.EMPTY : rootDirectory + FILE_SEPARATOR) + zipFileName;
		zipOut.putNextEntry(new ZipEntry(entryPath));
		FileInputStream inFile = new FileInputStream(file);
		try {
			int readLength = 0;
			while ((readLength = inFile.read(readArray)) > 0) {
				zipOut.write(readArray, 0, readLength);
			}
		} finally {
			IOUtils.closeQuietly(zipOut);
			IOUtils.closeQuietly(inFile);
		}
		logger.debug("File Zipped : " + file.getAbsolutePath() + " -> " + zipFileName);
	}
}
