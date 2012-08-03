package com.bouncingdata.plfdemo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.bouncingdata.plfdemo.datastore.pojo.DashboardPosition;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;

public class Utils {
  
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
  
  public static void main(String args[]) {
    System.out.println("MySQL Connect Example.");
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
    }
  }
}
