package com.bouncingdata.plfdemo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.bouncingdata.plfdemo.datastore.pojo.dto.DashboardPosition;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;

public class Utils {
  
  // Suppress default constructor for noninstantiability
  private Utils() {
    throw new AssertionError();
  }
  
  public static final String FILE_SEPARATOR;
  
  static {
    FILE_SEPARATOR = System.getProperty("file.separator");
  }
  
  public static List<Map> resultSetToList(ResultSet rs) throws SQLException {
    java.sql.ResultSetMetaData rsmd = rs.getMetaData();
    List<Map> result = new ArrayList<Map>();
    while (rs.next()) {
      Map<String, Object> row = new HashMap<String, Object>();
      
      int numColumns = rsmd.getColumnCount();
      for (int i = 1; i < numColumns + 1; i++) {
        String column_name = rsmd.getColumnName(i);
        Object value = rs.getObject(column_name);
        row.put(column_name, value);
      }
      result.add(row);
    }
    return result;
  }
  
  public static List<Object[]> resultSetToListOfArray(ResultSet rs) throws SQLException {
    java.sql.ResultSetMetaData rsmd = rs.getMetaData();
    List<Object[]> result = new ArrayList<Object[]>();
    while (rs.next()) {
      //Map<String, Object> row = new HashMap<String, Object>();
      int numColumns = rsmd.getColumnCount();
      Object[] row = new Object[numColumns];
        
      for (int i = 1; i < numColumns + 1; i++) {
        String column_name = rsmd.getColumnName(i);
        Object value = rs.getObject(column_name);
        row[i-1] = value;
      }
      result.add(row);
    }
    return result;
  }
  
  public static String resultSetToJson(ResultSet rs) throws SQLException {
    
    JSONArray json = new JSONArray();

    java.sql.ResultSetMetaData rsmd = rs.getMetaData();

    while (rs.next()) {
      int numColumns = rsmd.getColumnCount();
      JSONObject obj = new JSONObject();

      for (int i = 1; i < numColumns + 1; i++) {

        String column_name = rsmd.getColumnName(i);

        if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
          obj.put(column_name, rs.getArray(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
          obj.put(column_name, rs.getInt(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
          obj.put(column_name, rs.getBoolean(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
          obj.put(column_name, rs.getBlob(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
          obj.put(column_name, rs.getDouble(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
          obj.put(column_name, rs.getFloat(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
          obj.put(column_name, rs.getInt(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
          obj.put(column_name, rs.getNString(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
          obj.put(column_name, rs.getString(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
          obj.put(column_name, rs.getInt(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
          obj.put(column_name, rs.getInt(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
          obj.put(column_name, rs.getDate(column_name));
        } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
          obj.put(column_name, rs.getTimestamp(column_name));
        } else {
          obj.put(column_name, rs.getObject(column_name));
        }

      }// end foreach
      json.add(obj);

    }// end while
    return json.toString();
  }
  
  public static String getExecutionId() {
    return UUID.randomUUID().toString();
  }
  
  public static String generateGuid() {
    return UUID.randomUUID().toString();
  }
  
  public static Date getCurrentDate() {
    Calendar cal = Calendar.getInstance();
    return cal.getTime();
  }
    
  public static String getApplicationFilename(String language) {
    /*String filename = null;
    if (ApplicationLanguage.PYTHON.getLanguage().equals(language)) filename = "appcode.py";
    else if (ApplicationLanguage.R.getLanguage().equals(language)) filename = "appcode.R";*/
    //return filename;
    return "appcode";
  }
  
  public static int countLines(String str) {
    if (str == null || str.isEmpty()) return 0;
    String[] lines = str.split("\n");
    return lines.length;
  }
  
  public static Map<String, DashboardPosition> parseDashboard(Analysis db) {
    if (db == null || db.getStatus() == null || db.getStatus().isEmpty()) return null;
    String status = db.getStatus();
    String[] list = status.split(",");
    int i = 0;
    Map<String, DashboardPosition> dashboard = new HashMap<String, DashboardPosition>();
    try {
      while (i < list.length) {
        String guid = list[i];
        int x = Integer.parseInt(list[i+1]);
        int y = Integer.parseInt(list[i+2]);
        int w = Integer.parseInt(list[i+3]);
        int h = Integer.parseInt(list[i+4]);
        dashboard.put(guid, new DashboardPosition(guid, x, y, w, h));
        i+=5;
      }
      return dashboard;
    } catch(Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public static String buildDashboardStatus(Map<String, DashboardPosition> dpMap) {
    Iterator<Entry<String,DashboardPosition>> iter = dpMap.entrySet().iterator();
    StringBuilder status = new StringBuilder();
    while (iter.hasNext()) {
      Entry<String, DashboardPosition> entry = iter.next();
      DashboardPosition dp = entry.getValue();
      status.append(entry.getKey() + "," + dp.getX() + "," + dp.getY() + "," + dp.getW() + "," + dp.getH() + ",");
    }
    
    String st = status.substring(0, status.length() - 1);
    return st;
  }
  
  /**
   * Parses the Excel file (*.xls) to get the column headers and data rows  
   * @param is the excel input stream
   * @return <code>List</code> of <code>String</code> array, the first element is the column headers, the rest is data
   * @throws Exception
   */
  public static List<String[]> parseExcel(InputStream is) throws Exception {
    // read excel file
    Workbook wb = WorkbookFactory.create(is);
    Sheet sheet = wb.getSheetAt(0);
    
    System.out.println("First row: " + sheet.getFirstRowNum());
    System.out.println("Last row: " + sheet.getLastRowNum());
    
    System.out.println(sheet.getTopRow());
    
    // from the first row, determine the schema
    int firstRowNum = sheet.getFirstRowNum();
    int lastRowNum = sheet.getLastRowNum();
    Row firstRow = sheet.getRow(firstRowNum);
    int firstCellNum = firstRow.getFirstCellNum();
    int lastCellNum = firstRow.getLastCellNum() - 1;
    
    System.out.println("First column: " + firstCellNum);
    System.out.println("Last column: " + lastCellNum);
    
    int columnNum = lastCellNum - firstCellNum + 1;
    String[] headers = new String[columnNum];
    List<String[]> result = null;
    for (int i = firstCellNum; i <= lastCellNum; i++) {
      Cell headerCell = firstRow.getCell(i);
      System.out.println("header at column " + i + " " + headerCell.toString());
      headers[i - firstCellNum] = getCellStringValue(headerCell);
    }
    
    result = new ArrayList<String[]>();
    result.add(headers);
    
    // now the data range is from [firstRow+1, firstCell] -> [lastRow, lastCell]
    System.out.format("Data range is from [%d, %d] to [%d, %d]%n", firstRowNum + 1, firstCellNum, lastRowNum, lastCellNum);
    
    for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
      String[] rowValues = new String[columnNum];
      Row row = sheet.getRow(i);
      for (int j = firstCellNum; j <= lastCellNum; j++) {
        Cell cell = row.getCell(j);
        if (cell != null) {
          String value = getCellStringValue(cell);
          rowValues[j - firstCellNum] = value;
        } else rowValues[j - firstCellNum] = null;
      }
      result.add(rowValues);
    }    
    return result;
  }
  
  /**
   * Get string value from excel cell
   * @param cell the <code>Cell</code> object to read
   * @return the <code>String</code> value of <i>cell</i>
   */
  public static String getCellStringValue(Cell cell) {
    try {
      if (cell == null) return null;
      return cell.getStringCellValue();
    } catch (IllegalStateException e) {
      int type = cell.getCellType();
      switch(type) {
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
  
  
  
  
  
    
  public static void main(String args[]) {
    try {
      InputStream is = new FileInputStream("/home/khiem/workbook.xls");
      List<String[]> result = parseExcel(is);
      System.out.println(result.size());
    } catch (Exception e) {
      e.printStackTrace();
    }
    /*System.out.println("MySQL Connect Example.");
    Connection conn = null;
    String url = "jdbc:mysql://localhost:3306/";
    String dbName = "plfdemo";
    String driver = "com.mysql.jdbc.Driver";
    String userName = "root"; 
    String password = "a";
    try {
      Class.forName(driver).newInstance();
      conn = DriverManager.getConnection(url+dbName,userName,password);
      System.out.println("Connected to the database");
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("select * from userdata_worldbank_wb_br_gdp");
      System.out.println(resultSetToJson(rs));
      rs.close();
      st.close();
      conn.close();
      System.out.println("Disconnected from database");
    } catch (Exception e) {
      e.printStackTrace();
    }*/
    
    
  }
}
