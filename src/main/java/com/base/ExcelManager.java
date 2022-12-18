package com.base;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestResult;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelManager {
    private static final Logger LOGGER = LogManager.getLogger(ExcelManager.class);
    private static List<Map<String, String>> excelRow = null;

    public ExcelManager() {
    }

    public static synchronized List<Map<String, String>> getControllerRowsList() throws Exception {
        return excelRow;
    }

    private static synchronized List<Map<String, String>> getExcelRow() throws Exception {
        try {
            FileInputStream fileInputStream = new FileInputStream(Constants.RUN_MANAGER_WORKBOOK.toFile());
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet("Controller");
            List<String> headerList = new LinkedList();

            int i;
            for(i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); ++i) {
                headerList.add(sheet.getRow(0).getCell(i).getStringCellValue());
            }

            LOGGER.debug("Headers list [{}]", headerList);
            List<Map<String, String>> rowMapList = new LinkedList();

            for(i = 1; i < sheet.getPhysicalNumberOfRows(); ++i) {
                Row row = sheet.getRow(i);
                Map<String, String> rowMap = new LinkedHashMap();

                for(int j = 0; j < sheet.getRow(0).getPhysicalNumberOfCells(); ++j) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        rowMap.put(headerList.get(j), getCellValue(cell));
                        LOGGER.debug("Added Key : [{}] | Value [{}] to row map", headerList.get(j), getCellValue(cell));
                    } else {
                        rowMap.put(headerList.get(j), "");
                        LOGGER.debug("Added Key : [{}] | Value [{}] to row map", headerList.get(j), "");
                    }
                }

                rowMapList.add(rowMap);
                LOGGER.debug("Row : " + i);
            }

            fileInputStream.close();
            return (List)rowMapList.stream().filter((map) -> {
                return map.size() > 0;
            }).collect(Collectors.toList());
        } catch (IOException var10) {
            LOGGER.error(var10);
            throw new RuntimeException(var10);
        }
    }

    public static synchronized List<Map<String, String>> getExcelRowsAsListOfMap(String excelWorkbookName, String excelSheetName, String testMethodName) throws Exception {
        LinkedList rowMapList = new LinkedList();

        try {
            FileInputStream fileInputStream = new FileInputStream(excelWorkbookName);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(excelSheetName);
            List<String> headerList = new LinkedList();

            int i;
            for(i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); ++i) {
                headerList.add(sheet.getRow(0).getCell(i).getStringCellValue());
            }

            LOGGER.debug("Headers list [{}]", headerList);

            for(i = 0; i < sheet.getPhysicalNumberOfRows(); ++i) {
                Row row = sheet.getRow(i);
                Map<String, String> rowMap = new LinkedHashMap();

                for(int j = 0; j < sheet.getRow(0).getPhysicalNumberOfCells(); ++j) {
                    Cell cell = row.getCell(j);
                    if (!getCellValue(sheet.getRow(i).getCell(1)).equals(testMethodName)) {
                        break;
                    }

                    if (cell != null) {
                        rowMap.put(headerList.get(j), getCellValue(cell));
                        LOGGER.debug("Added Key : [{}] | Value [{}] to row map", headerList.get(j), getCellValue(cell));
                    } else {
                        rowMap.put(headerList.get(j), "");
                        LOGGER.debug("Added Key : [{}] | Value [{}] to row map", headerList.get(j), "");
                    }
                }

                if (rowMap.size() > 0) {
                    rowMapList.add(rowMap);
                }
            }

            fileInputStream.close();
            return rowMapList;
        } catch (IOException var13) {
            LOGGER.error(var13);
            throw new RuntimeException(var13);
        }
    }

    protected static synchronized void writeTestStatusToExcel(ITestResult result) {
        try {
            FileInputStream fileInputStream = new FileInputStream(Constants.RUN_MANAGER_WORKBOOK.toFile());
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet("Controller");
            Map<String, Integer> headersMap = new LinkedHashMap();

            int columnIndex;
            for(columnIndex = 0; columnIndex < sheet.getRow(0).getPhysicalNumberOfCells(); ++columnIndex) {
                headersMap.put(sheet.getRow(0).getCell(columnIndex).getStringCellValue(), columnIndex);
            }

            columnIndex = 1;

            while(true) {
                if (columnIndex >= sheet.getPhysicalNumberOfRows()) {
                    fileInputStream.close();
                    FileOutputStream fileOutputStream = new FileOutputStream(Constants.RUN_MANAGER_WORKBOOK.toFile());
                    workbook.write(fileOutputStream);
                    fileOutputStream.close();
                    break;
                }

                if (StringUtils.isNotBlank(sheet.getRow(columnIndex).getCell((Integer)headersMap.get("TestMethodName")).getStringCellValue())) {
                    String testMethodName = sheet.getRow(columnIndex).getCell((Integer)headersMap.get("TestMethodName")).getStringCellValue();
                    String executeFlag = sheet.getRow(columnIndex).getCell((Integer)headersMap.get("Execute")).getStringCellValue();
                    if (executeFlag.equalsIgnoreCase("yes") && result.getMethod().getMethodName().equals(testMethodName)) {
                        String status = result.getStatus() == 2 ? "Failed" : "Passed";
                        sheet.getRow(columnIndex).getCell((Integer)headersMap.get("Status")).setCellValue(status);
                    }
                }

                ++columnIndex;
            }
        } catch (Exception var9) {
            LOGGER.error(var9);
            throw new RuntimeException(var9);
        }

        LOGGER.debug("Successfully wrote back test result to TestRunner sheet");
    }

    public static synchronized void writeToExcelColumn(Map<String, String> data, String sheetName, String columnName, String columnValueToSet) {
        writeToExcelColumn(Constants.RUN_MANAGER_WORKBOOK.toString(), sheetName, (String)data.get("TestMethodName"), columnName, columnValueToSet);
    }

    public static synchronized void writeToExcelColumn(String workBookPath, String sheetName, String testMethodName, String columnName, String columnValueToSet) {
        try {
            FileInputStream fileInputStream = new FileInputStream(workBookPath);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            Map<String, Integer> headersMap = new LinkedHashMap();
            int columnIndex = 0;

            while(true) {
                if (columnIndex >= sheet.getRow(0).getPhysicalNumberOfCells()) {
                    for(columnIndex = 1; columnIndex < sheet.getPhysicalNumberOfRows(); ++columnIndex) {
                        String runTimePKey = sheet.getRow(columnIndex).getCell((Integer)headersMap.get("TestMethodName")).getStringCellValue();
                        if (runTimePKey.equals(testMethodName)) {
                            sheet.getRow(columnIndex).getCell((Integer)headersMap.get(columnName)).setCellValue(columnValueToSet);
                        }
                    }

                    fileInputStream.close();
                    FileOutputStream fileOutputStream = new FileOutputStream(workBookPath);
                    workbook.write(fileOutputStream);
                    fileOutputStream.close();
                    break;
                }

                headersMap.put(sheet.getRow(0).getCell(columnIndex).getStringCellValue(), columnIndex);
                ++columnIndex;
            }
        } catch (Exception var11) {
            LOGGER.error(var11);
            throw new RuntimeException(var11);
        }

        LOGGER.debug("Successfully wrote to excel");
    }

    public static synchronized void writeToExcelColumn(String workBookPath, String sheetName, String testMethodName, String columnName, String columnValueToSet, String P_key) {
        try {
            FileInputStream fileInputStream = new FileInputStream(workBookPath);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            Map<String, Integer> headersMap = new LinkedHashMap();
            int columnIndex = 0;

            label33:
            while(true) {
                if (columnIndex >= sheet.getRow(0).getPhysicalNumberOfCells()) {
                    columnIndex = 1;

                    while(true) {
                        if (columnIndex < sheet.getPhysicalNumberOfRows()) {
                            String runTimePKey = sheet.getRow(columnIndex).getCell((Integer)headersMap.get("TestMethodName")).getStringCellValue();
                            String runP_key = getCellValue(sheet.getRow(columnIndex).getCell((Integer)headersMap.get("P_Key")));
                            if (!runTimePKey.equals(testMethodName) || !runP_key.equals(P_key)) {
                                ++columnIndex;
                                continue;
                            }

                            sheet.getRow(columnIndex).getCell((Integer)headersMap.get(columnName)).setCellValue(columnValueToSet);
                        }

                        fileInputStream.close();
                        FileOutputStream fileOutputStream = new FileOutputStream(workBookPath);
                        workbook.write(fileOutputStream);
                        fileOutputStream.close();
                        break label33;
                    }
                }

                headersMap.put(sheet.getRow(0).getCell(columnIndex).getStringCellValue(), columnIndex);
                ++columnIndex;
            }
        } catch (Exception var13) {
            LOGGER.error(var13);
            throw new RuntimeException(var13);
        }

        LOGGER.debug("Successfully wrote to excel");
    }

    protected static Map<String, String> getControllerRowMapByTestMethodName(String testMethodName) {
        return (Map)((List)excelRow.stream().filter((map) -> {
            return ((String)map.get("TestMethodName")).equals(testMethodName);
        }).collect(Collectors.toList())).get(0);
    }

    private static String getCellValue(Cell cell) throws Exception {
        if (cell.getCellType().equals(CellType.NUMERIC)) {
            return cell.getNumericCellValue() + "";
        } else if (cell.getCellType().equals(CellType.STRING)) {
            return cell.getStringCellValue();
        } else if (cell.getCellType().equals(CellType.BLANK)) {
            return "";
        } else if (cell.getCellType().equals(CellType.BOOLEAN)) {
            return cell.getBooleanCellValue() + "";
        } else if (cell.getCellType().equals(CellType._NONE)) {
            return "";
        } else if (cell.getCellType().equals(CellType.FORMULA)) {
            return cell.getStringCellValue() + "";
        } else {
            throw new RuntimeException("Cell Type not Supported");
        }
    }

    public static Sheet getSheet(Path excelPath, String sheetName) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(excelPath.toFile());
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        return workbook.getSheet(sheetName);
    }

    public static int getRowIndex(String reference, Path excelPath, String sheetName, int rowNumber) throws Exception {
        Iterator rows = getSheet(excelPath, sheetName).rowIterator();

        XSSFRow row;
        do {
            if (!rows.hasNext()) {
                LOGGER.error("No Such Reference Found. Reference -> " + reference);
                throw new Exception("No Such Reference Found. Reference -> " + reference);
            }

            row = (XSSFRow)rows.next();
        } while(!row.getCell(rowNumber).toString().trim().equals(reference.trim()));

        return row.getRowNum();
    }

    public static String getCellValue(XSSFCell cell) {
        if (cell != null && cell.getCellType() != CellType.BLANK) {
            if (cell.getCellType() == CellType.NUMERIC) {
                double value = cell.getNumericCellValue();
                return value + "";
            } else if (cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCellType() != CellType.BOOLEAN && cell.getCellType() != CellType.ERROR && cell.getCellType() != CellType.FORMULA) {
                return "";
            } else {
                throw new RuntimeException("Cell Type is not supported ");
            }
        } else {
            return "";
        }
    }

    public static Map<String, String> getRowValue(int rowNumber, Path excelPath, String sheetName) throws Exception {
        Map<String, String> rowValue = new HashMap();
        Sheet sheet = getSheet(excelPath, sheetName);
        Iterator<Cell> keyCells = sheet.getRow(0).cellIterator();
        XSSFRow valueRow = (XSSFRow)sheet.getRow(rowNumber);
        int i = 0;

        while(keyCells.hasNext()) {
            String key = ((Cell)keyCells.next()).toString().trim();

            String value;
            try {
                value = getCellValue(valueRow.getCell(i)).trim();
            } catch (NoSuchElementException var11) {
                value = "";
            }

            ++i;
            rowValue.put(key, value);
        }

        return rowValue;
    }

    public static Map<String, String> getRowValue(int rowNumber, int headerRowNumber, Path excelPath, String sheetName) throws Exception {
        Map<String, String> rowValue = new HashMap();
        Sheet sheet = getSheet(excelPath, sheetName);
        Iterator<Cell> keyCells = sheet.getRow(headerRowNumber).cellIterator();
        XSSFRow valueRow = (XSSFRow)sheet.getRow(rowNumber);
        int i = 0;

        while(keyCells.hasNext()) {
            String key = ((Cell)keyCells.next()).toString().trim();

            String value;
            try {
                value = getCellValue(valueRow.getCell(i)).trim();
            } catch (NoSuchElementException var12) {
                value = "";
            }

            ++i;
            rowValue.put(key, value);
        }

        return rowValue;
    }

    public static Map<String, String> getRowValue(String reference, Path excelPath, String sheetName) throws Exception {
        return getRowValue(getRowIndex(reference, excelPath, sheetName, 0), excelPath, sheetName);
    }

    private static String getMobileSettingsReference() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(Constants.RUN_MANAGER_WORKBOOK.toFile());
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheet("Settings");
        return sheet.getRow(2).getCell(1).toString();
    }

    private static String getWebSettingsReference() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(Constants.RUN_MANAGER_WORKBOOK.toFile());
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheet("Settings");
        return sheet.getRow(1).getCell(1).toString();
    }

    private static Map<String, String> getWebSettingsDetailsAsMap() throws Exception {
        return getRowValue(getRowIndex(getMobileSettingsReference(), Constants.RUN_MANAGER_WORKBOOK, "Settings", 0), getRowIndex("WebConfiguration", Constants.RUN_MANAGER_WORKBOOK, "Settings", 0), Constants.RUN_MANAGER_WORKBOOK, "Settings");
    }

    public static Map<String, String> getMobileSettingsDetailsAsMap() throws Exception {
        return getRowValue(getRowIndex(getMobileSettingsReference(), Constants.RUN_MANAGER_WORKBOOK, "Settings", 0), getRowIndex("MobileConfiguration", Constants.RUN_MANAGER_WORKBOOK, "Settings", 0), Constants.RUN_MANAGER_WORKBOOK, "Settings");
    }

    public static Map<String, String> getMobileSettingsDetailsAsMap(String reference) throws Exception {
        return getRowValue(getRowIndex(reference, Constants.RUN_MANAGER_WORKBOOK, "Settings", 0), getRowIndex("MobileConfiguration", Constants.RUN_MANAGER_WORKBOOK, "Settings", 0), Constants.RUN_MANAGER_WORKBOOK, "Settings");
    }

    public static List<String> getExcelColumnAsList(String excelPath, String sheetName, int columnNumber) {
        ArrayList columnValues = new ArrayList();

        try {
            FileInputStream fileInputStream = new FileInputStream(excelPath);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            Iterator var7 = sheet.iterator();

            while(var7.hasNext()) {
                Row row = (Row)var7.next();
                Cell cell = row.getCell(columnNumber);
                cell.setCellType(CellType.STRING);
                columnValues.add(getCellValue(cell));
            }

            return columnValues;
        } catch (Exception var10) {
            LOGGER.error(var10);
            throw new RuntimeException(var10);
        }
    }

    public static synchronized void writeToExcelCell(String workBookPath, String sheetName, int rowNumber, int columnNumber, String columnValueToSet) {
        try {
            FileInputStream fileInputStream = new FileInputStream(workBookPath);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            sheet.getRow(rowNumber).getCell(columnNumber).setCellValue(columnValueToSet);
            fileInputStream.close();
            FileOutputStream fileOutputStream = new FileOutputStream(workBookPath);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (Exception var9) {
            LOGGER.error(var9);
            throw new RuntimeException(var9);
        }

        LOGGER.debug("Successfully wrote to excel");
    }

    static {
        try {
            excelRow = getExcelRow();
        } catch (Exception var1) {
            var1.printStackTrace();
        }

    }
}
