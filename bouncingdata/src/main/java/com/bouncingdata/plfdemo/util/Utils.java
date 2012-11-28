package com.bouncingdata.plfdemo.util;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

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
  
  public static String resultSetToJson(ResultSet rs) throws Exception {
    
    ObjectMapper mapper = new ObjectMapper();
    List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();

    java.sql.ResultSetMetaData rsmd = rs.getMetaData();

    while (rs.next()) {
      int numColumns = rsmd.getColumnCount();
      HashMap<String, Object> obj = new LinkedHashMap<String, Object>();
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
      list.add(obj);

    }// end while
    return mapper.writeValueAsString(list);
  }
  
  public static void resultSetToCSV(ResultSet rs, OutputStream os) throws Exception {
    Writer out = new OutputStreamWriter(os);
    CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
    java.sql.ResultSetMetaData rsmd = rs.getMetaData();
    int numColumns = rsmd.getColumnCount();
    String[] columnNames = new String[numColumns];
    try {
      for (int i = 1; i < numColumns + 1; i++) {
        columnNames[i-1] = rsmd.getColumnName(i);
        if (i == numColumns) {
          printer.print(columnNames[i-1]);
          printer.println();
        } else printer.print(columnNames[i-1]);
      }
      while (rs.next()) {
        for (int i = 1; i < numColumns + 1; i++) {     
          String column_name = columnNames[i-1];
          Object value = null;
          if (rsmd.getColumnType(i) == java.sql.Types.ARRAY) {
            value = rs.getArray(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.BIGINT) {
            value = rs.getInt(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.BOOLEAN) {
            value = rs.getBoolean(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.BLOB) {
            value = rs.getBlob(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.DOUBLE) {
            value = rs.getDouble(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.FLOAT) {
            value = rs.getFloat(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.INTEGER) {
            value = rs.getInt(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.NVARCHAR) {
            value = rs.getNString(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR) {
            value = rs.getString(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.TINYINT) {
            value = rs.getInt(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.SMALLINT) {
            value = rs.getInt(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.DATE) {
            value = rs.getDate(column_name);
          } else if (rsmd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
            value = rs.getTimestamp(column_name);
          } else {
            value = rs.getObject(column_name);
          }
          
          if (i == numColumns) {
            printer.print(value!=null?value.toString():"");
            printer.println();
          } else {
            printer.print(value!=null?value.toString():"");
          }
        }     
      }
    } finally {
      printer.flush();
      out.close();
    }
  }
  
  public static void jsonToCsv(String jsonData, OutputStream os) throws Exception {
    Writer out = new OutputStreamWriter(os);
    CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode data = mapper.readTree(jsonData);
    JsonNode firstEle = data.get(0);
    Iterator<String> it = firstEle.getFieldNames();
    List<String> columns = new ArrayList<String>();
    while(it.hasNext()) {
      String field = it.next();
      columns.add(field);
    }
    try {
      for (int i = 0; i < columns.size(); i++) {
        if (i == columns.size() - 1) {
          printer.print(columns.get(i));
          printer.println();
        } else printer.print(columns.get(i));
      }
      for (int i = 0; i< data.size(); i++) {
        JsonNode element = data.get(i);
        for (int j = 0; j < columns.size(); j++) {
          String col = columns.get(j);
          String val = element.get(col).getValueAsText();
          if (j == columns.size() - 1) {
            printer.print(val);
            printer.println();
          } else printer.print(val);
        }
      }
    } finally {
      out.flush();
      out.close();
    }
    
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
  
}
