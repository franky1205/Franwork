package org.franwork.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Frankie
 * 
 */
public final class PoiExcelUtil {

	public static final DataFormatter DEFAULT_DATA_FORMATTER = new DataFormatter();
	
	public static final int ADDING_LEVEL = 26;
	
	private static Logger logger = LoggerFactory.getLogger(PoiExcelUtil.class);

	private PoiExcelUtil() {
		super();
	}
	
	public static int getColumnIndex(String columnName) {
		if (StringUtils.isBlank(columnName)) {
			throw new IllegalArgumentException("The given ColumnName can not be Blank.");
		}
		columnName = columnName.toUpperCase();
		int columnIndex = 0;
		int[] columnNameIndexes = new int[columnName.length()];
		for (int i = 0 ; i < columnNameIndexes.length ; i++) {
			char columnNameChar = columnName.charAt(i);
			columnNameIndexes[columnNameIndexes.length - i - 1] = (int)columnNameChar - (int)('A') + 1;
		}
		for (int i = 0 ; i < columnNameIndexes.length ; i++) {
			columnIndex += columnNameIndexes[i] * ((int)Math.pow(ADDING_LEVEL, i));
		}
		return columnIndex - 1;
	}
	
	public static Workbook getWorkbook(String filePath) throws FileNotFoundException, IOException {
		if (StringUtils.isBlank(filePath)) {
			logger.error("The filePath is Blank.");
			return null;
		}
		return PoiExcelUtil.getWorkbook(new File(filePath));
	}

	public static Workbook getWorkbook(File file) throws FileNotFoundException, IOException {
		if (file == null || !file.exists()) {
			throw new IllegalArgumentException("The given File object is NULL or not Exists. " + file);
		}
		FileInputStream inputStream = new FileInputStream(file);
		Workbook workbook = null;
		if (file.getName().endsWith(".xls")) {
			workbook = PoiExcelUtil.getHSSFWorkbook(inputStream);
		}
		if (file.getName().endsWith(".xlsx")) {
			workbook = PoiExcelUtil.getXSSFWorkbook(inputStream);
		}
		IOUtils.closeQuietly(inputStream);
		if (workbook == null) {
			throw new IllegalArgumentException("The given File object does not have " 
					+ "valid Excel file extension " + file);
		}
		return workbook;
	}
	
	public static void writeWorkbookToFile(Workbook workbook, String filePath) throws IOException {
		if (workbook == null) {
			logger.error("The given Workbook instance is NULL");
			return ;
		}
		if (StringUtils.isBlank(filePath)) {
			logger.error("The filePath is Blank. No content write to Workbook");
			return ;
		}
		PoiExcelUtil.writeWorkbookToFile(workbook, new File(filePath));
	}
	
	public static void writeWorkbookToFile(Workbook workbook, File file) throws IOException {
		if (workbook == null) {
			logger.error("The given Workbook instance is NULL");
			return ;
		}
		if (file == null) {
			logger.error("The File object is NULL");
			return ;
		}
		if (!file.getName().endsWith(".xls") && !file.getName().endsWith(".xlsx")) {
			logger.error("The given file object is not a valid Excel File");
			return ;
		}
		FileOutputStream outputStream = new FileOutputStream(file);
		workbook.write(outputStream);
		IOUtils.closeQuietly(outputStream);
	}
	
	private static Workbook getHSSFWorkbook(FileInputStream fileInputStream) throws IOException {
		if (fileInputStream == null) {
			logger.error("The given fileInputStream is NULL.");
			return null;
		}
		return new HSSFWorkbook(fileInputStream);
	}

	private static Workbook getXSSFWorkbook(FileInputStream fileInputStream) throws IOException {
		if (fileInputStream == null) {
			logger.error("The given fileInputStream is NULL.");
			return null;
		}
		return new XSSFWorkbook(fileInputStream);
	}

	public static String getCellValueString(Cell cell) {
		if (cell == null) {
			logger.info("The given cell is NULL.");
			return StringUtils.EMPTY;
		}
		return DEFAULT_DATA_FORMATTER.formatCellValue(cell);
	}
}
