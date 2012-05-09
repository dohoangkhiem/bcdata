package khiem.dataprj.demo.plfdemo.datastore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import khiem.dataprj.demo.plfdemo.datastore.pojo.Application;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Table;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Visualization;
import khiem.dataprj.demo.plfdemo.utils.Utils;

import org.springframework.dao.DataAccessException;

public class JdbcDataStorage implements DataStorage {
  
  private DataSource dataSource;
  
  public void setDataSource(DataSource ds) {
    this.dataSource = ds;
  }

  @Override
  public List<Dataset> getDatasetList() throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void createDataset(String name, String description)
      throws DataAccessException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<Table> getTableList(String datasetName)
      throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void createTable(String datasetName, String tableName, String fieldList)
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
  public void createApplication(String name, String description, String language) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<Visualization> getVisualization(String appname) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getTableDataInJson(String appname, String tablename) {
    String sql = "select * from userdata_" + appname + "_" + tablename;
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
  public Dataset getDataset(String dataset) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteVisualization(String appname, String visualizationName)
      throws DataAccessException {
    // TODO Auto-generated method stub
    
  }

}
