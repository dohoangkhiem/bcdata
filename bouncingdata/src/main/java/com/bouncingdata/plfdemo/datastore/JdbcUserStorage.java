package com.bouncingdata.plfdemo.datastore;

import java.sql.Connection;
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

public class JdbcUserStorage extends JdbcDaoSupport {
  
  private Logger logger = LoggerFactory.getLogger(JdbcUserStorage.class);

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
}
