package com.bouncingdata.plfdemo.datastore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;


import org.springframework.dao.DataAccessException;

import com.bouncingdata.plfdemo.datastore.pojo.old.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Datastore;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Visualization;
import com.bouncingdata.plfdemo.utils.Utils;

public class JdbcDataStorage implements DataStorage {
  
  private DataSource dataSource;
  
  public void setDataSource(DataSource ds) {
    this.dataSource = ds;
  }

  @Override
  public List<Datastore> getDatastoreList() throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void createDatastore(String name, String description)
      throws DataAccessException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<Dataset> getDatasetList(String datastoreName)
      throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void createDataset(String datastoreName, String datasetName, String fieldList)
      throws DataAccessException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void importJsonData(String datasetName, String tableName,
      String jsonData) throws DataAccessException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void executeSql(String sql) throws Exception {
    Connection conn = null;
    Statement st = null;
    try {
      conn = dataSource.getConnection();
      st = conn.createStatement();
      st.executeUpdate(sql);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      if (conn != null) {
        try { st.close(); conn.close(); } catch (Exception e) {}
      }
    }
  }

  @Override
  public String executeSqlWithResult(String sql) throws Exception {
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
      conn = dataSource.getConnection();
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      String result =  Utils.resultSetToJson(rs);
      return result;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      if (conn != null) {
        try { rs.close(); st.close(); conn.close(); } catch (Exception e) {}
      }
    }
  }

  @Override
  public List<Application> getApplicationList() throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Application getApplication(String appname) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int createApplication(String name, String description, String language) {
    // TODO Auto-generated method stub
    return -1;
  }

  @Override
  public List<Visualization> getVisualization(String appname) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getDatasetDataInJson(String appname, String datasetName) {
    String sql = "select * from userdata_" + appname + "_" + datasetName;
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(sql);
      String result =  Utils.resultSetToJson(rs);
      rs.close();
      st.close();
      return result;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      if (conn != null) {
        try { conn.close(); } catch (Exception e) {}
      }
    }
  }

  @Override
  public Datastore getDatastore(String datastore) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteVisualization(String appname, String visualizationName)
      throws DataAccessException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void deleteDataset(String appname, String datasetName) throws DataAccessException {
    String sql = "drop table userdata_" + appname + "_" + datasetName;
    Connection conn = null;
    Statement st = null;
    try {
      conn = dataSource.getConnection();
      st = conn.createStatement();
      int rs = st.executeUpdate(sql);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if (st != null) st.close();
        if (conn != null) conn.close();
      } catch (Exception e) {}
    }
  }

  @Override
  public List<Application> searchApplication(String query)
      throws DataAccessException {
    String sql = "select * from Applications where name like '%" + query + "%' or description like '%" + query + "%'";
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(sql);
      List<Application> apps = new ArrayList<Application>();
      while (rs.next()) {
        Application app = new Application(rs.getString("name"), rs.getString("description"), rs.getString("language"));
        apps.add(app);
      }
      rs.close();
      st.close();
      return apps;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      if (conn != null) {
        try { conn.close(); } catch (Exception e) {}
      }
    }
  }

  @Override
  public List<Datastore> searchDatastore(String query) throws DataAccessException {
    String sql = "select * from Datastores where name like '%" + query + "%' or description like '%" + query + "%'";
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(sql);
      List<Datastore> datasets = new ArrayList<Datastore>();
      while (rs.next()) {
        Datastore dataset = new Datastore(rs.getString("name"), rs.getString("description"));
        datasets.add(dataset);
      }
      rs.close();
      st.close();
      return datasets;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      if (conn != null) {
        try { conn.close(); } catch (Exception e) {}
      }
    }
  }

  @Override
  public SearchResult search(String query) throws DataAccessException {
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    SearchResult result = new SearchResult();
    try {
      conn = dataSource.getConnection();
      st = conn.createStatement();
      String sql = "select * from ? where name like '%"  + query + "%' or description like '%" + query + "%'";
      rs = st.executeQuery(sql.replace("?", "Tables"));
      while (rs.next()) {
        Dataset dataset = new Dataset(rs.getString("name"), rs.getString("description"), rs.getString("datastore"), rs.getString("field_list"));
        result.getDatasets().add(dataset);
      }
      
      rs = st.executeQuery(sql.replace("?", "Datastores"));
      while (rs.next()) {
        Datastore datastore = new Datastore(rs.getString("name"), rs.getString("description"));
        result.getDatastores().add(datastore);
      }
      
      rs = st.executeQuery(sql.replace("?", "Applications"));
      while (rs.next()) {
        Application app = new Application(rs.getString("name"), rs.getString("description"), rs.getString("language"));
        result.getApplications().add(app);
      }
      
      return result;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      if (rs != null) {
        try { rs.close(); } catch (Exception e) {}
      }
      if (st != null) {
        try { st.close(); } catch (Exception e) {}
      }
      if (conn != null) {
        try { conn.close(); } catch (Exception e) {}
      }
    }
  }

}
