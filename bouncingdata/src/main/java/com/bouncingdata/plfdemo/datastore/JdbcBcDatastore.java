package com.bouncingdata.plfdemo.datastore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.bouncingdata.plfdemo.utils.Utils;

public class JdbcBcDatastore extends JdbcDaoSupport implements BcDatastore {
  
  private Logger logger = LoggerFactory.getLogger(JdbcBcDatastore.class);

  @Override
  public String getDataset(String dataset) throws DataAccessException {
    String sql = "select * from `" + dataset + "`";
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      return Utils.resultSetToJson(rs);
      
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when retrieved dataset {}", dataset);
        logger.debug("Exception detail: ", e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }  
  }
  
  public String getDataset(String dataset, int begin, int maxNumber) throws DataAccessException {
    String sql = "select * from `" + dataset + "` limit " + begin + "," + maxNumber;
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      return Utils.resultSetToJson(rs);
      
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when retrieved dataset {}", dataset);
        logger.debug("Exception detail: ", e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }
  }
  
  public List<Map> getDatasetToList(String dataset, int begin, int maxNumber) throws DataAccessException {
    String sql = "select * from `" + dataset + "` limit " + begin + "," + maxNumber;
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      return Utils.resultSetToList(rs);
      
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when retrieved dataset {}", dataset);
        logger.debug("Exception detail: ", e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }  
  }
  
  public List<Object[]> getDatasetToListOfArray(String dataset, int begin, int maxNumber) throws DataAccessException {
    String sql = "select * from `" + dataset + "` limit " + begin + "," + maxNumber;
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      return Utils.resultSetToListOfArray(rs);
      
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when retrieved dataset {}", dataset);
        logger.debug("Exception detail: ", e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }  
  }
  
  @Override
  public List<Map> getDatasetToList(String dataset) throws DataAccessException {
    String sql = "select * from `" + dataset + "`";
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      return Utils.resultSetToList(rs);
      
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when retrieved dataset {}", dataset);
        logger.debug("Exception detail: ", e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }  
  }
  
  @Override
  public List<Map> query(String sql) throws DataAccessException {
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      return Utils.resultSetToList(rs);
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when execute query: " + sql, e);
      }
      return null;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }  
  }
  
  /**
   * 
   */
  @Override
  public void persistDataset(String tableName, String[] headers, List<String[]> data) {
    Connection conn = null;
    Statement st = null;
    PreparedStatement pst = null;
    int columnNum = headers.length;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      String sql = "drop table if exists `" + tableName + "`";
      st.executeUpdate(sql);
      
      StringBuilder createSql = new StringBuilder("create table `" + tableName + "` (");
      for (String header : headers) {
        createSql.append("`" + header + "`");
        createSql.append(" text,");
      }
      
      sql = createSql.substring(0, createSql.length() -1) + ")";
      logger.debug("Create table: {}.", sql);
      st.execute(sql);
      
      StringBuilder insertSql = new StringBuilder("insert into `" + tableName + "`(");
      for (int i = 0; i < columnNum; i++) {
        insertSql.append("`" + headers[i] + "`,");
      }
      insertSql = new StringBuilder(insertSql.substring(0, insertSql.length() - 1));
      insertSql.append(") values ");
      
      for (int i = 0; i < data.size(); i++) {      
        StringBuilder row = new StringBuilder("(");
        for (int j = 0; j < columnNum; j++) {
          row.append("?,");
        }
        String rowStr = row.substring(0, row.length() - 1);
        rowStr += "),";
        insertSql.append(rowStr);
      }
      
      sql = insertSql.substring(0, insertSql.length() - 1);
      pst = conn.prepareStatement(sql);
      for (int i = 0; i < data.size(); i++) {
        String[] rowData = data.get(i);
        for (int j = 0; j < columnNum; j++) {
          pst.setString(i*columnNum + j + 1, rowData[j]);
        }
      }
      //int result = st.executeUpdate(sql);
      int result = pst.executeUpdate();
      logger.debug("Insert complete: {} rows.", result);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (pst != null) try { pst.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }
  }

  @Override
  public void dropDataset(String datasetName) {
    Connection conn = null;
    Statement st = null;
    try {
      conn = getConnection();
      st = conn.createStatement();
      String sql = "drop table `" + datasetName + "`";
      st.executeUpdate(sql);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      if (st != null) try { st.close(); } catch(Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }
  }
  
  public int getDatasetSize(String dataset) {
    String sql = "select id from `" + dataset + "`";
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = getDataSource().getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      
      int rowCount = 0;
      while (rs.next()) {
        rowCount++;
       }
      return rowCount;
    } catch (SQLException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Error when retrieved dataset {}", dataset);
        logger.debug("Exception detail: ", e);
      }
      return -1;
    } finally {
      if (rs != null) try { rs.close(); } catch (Exception e) {}
      if (st != null) try { st.close(); } catch (Exception e) {}
      if (conn != null) try { conn.close(); } catch (Exception e) {}
    }
  }
}
