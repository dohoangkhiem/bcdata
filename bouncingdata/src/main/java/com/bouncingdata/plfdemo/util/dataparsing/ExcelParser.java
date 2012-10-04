package com.bouncingdata.plfdemo.util.dataparsing;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ExcelParser implements DataParser {
  
  private Logger logger = LoggerFactory.getLogger(ExcelParser.class);
  
  ExcelParser() {}

  @Override
  public List<String[]> parse(InputStream is) throws Exception {
    // read excel file
    Workbook wb = WorkbookFactory.create(is);
    Sheet sheet = wb.getSheetAt(0);

    // from the first row, determine the schema
    int firstRowNum = sheet.getFirstRowNum();
    int lastRowNum = sheet.getLastRowNum();
    Row firstRow = sheet.getRow(firstRowNum);
    int firstCellNum = firstRow.getFirstCellNum();
    int lastCellNum = firstRow.getLastCellNum() - 1;

    int columnNum = lastCellNum - firstCellNum + 1;
    String[] headers = new String[columnNum];
    List<String[]> result = null;
    for (int i = firstCellNum; i <= lastCellNum; i++) {
      Cell headerCell = firstRow.getCell(i);
      headers[i - firstCellNum] = getCellStringValue(headerCell);
    }

    result = new ArrayList<String[]>();
    result.add(headers);

    // now the data range is from [firstRow+1, firstCell] -> [lastRow, lastCell]
    logger.debug("Data range is from [{}, {}] to [{}, {}]", new String[] { String.valueOf(firstRowNum + 1), String.valueOf(firstCellNum), String.valueOf(lastRowNum), String.valueOf(lastCellNum) });
    for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
      String[] rowValues = new String[columnNum];
      Row row = sheet.getRow(i);
      for (int j = firstCellNum; j <= lastCellNum; j++) {
        Cell cell = row.getCell(j);
        if (cell != null) {
          String value = getCellStringValue(cell);
          rowValues[j - firstCellNum] = value;
        } else
          rowValues[j - firstCellNum] = null;
      }
      result.add(rowValues);
    }
    return result;
  }

  /**
   * Get string value from excel cell
   * 
   * @param cell
   *          the <code>Cell</code> object to read
   * @return the <code>String</code> value of <i>cell</i>
   */
  public static String getCellStringValue(Cell cell) {
    try {
      if (cell == null)
        return null;
      return cell.getStringCellValue();
    } catch (IllegalStateException e) {
      int type = cell.getCellType();
      switch (type) {
      case Cell.CELL_TYPE_BLANK:
        return null;
      case Cell.CELL_TYPE_BOOLEAN:
        return String.valueOf(cell.getBooleanCellValue());
      case Cell.CELL_TYPE_ERROR:
        return null;
      case Cell.CELL_TYPE_FORMULA:
        return cell.getCellFormula();
      case Cell.CELL_TYPE_NUMERIC:
        return String.valueOf(cell.getNumericCellValue());
      default:
        return cell.toString();
      }
    }
  }
}