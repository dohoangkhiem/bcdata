package khiem.dataprj.demo.plfdemo.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Utils {
  
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
  
  public static String getApplicationFilename(String language) {
    String filename = null;
    if (ApplicationLanguage.PYTHON.getLanguage().equals(language)) filename = "appcode.py";
    else if (ApplicationLanguage.R.getLanguage().equals(language)) filename = "appcode.R";
    return filename;
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
