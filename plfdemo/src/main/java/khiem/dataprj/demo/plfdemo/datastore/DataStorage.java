package khiem.dataprj.demo.plfdemo.datastore;

import java.util.List;

import khiem.dataprj.demo.plfdemo.datastore.pojo.Application;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Table;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Visualization;

import org.springframework.dao.DataAccessException;

public interface DataStorage {
  
  public List<Dataset> getDatasetList() throws DataAccessException;
  
  public void createDataset(String name, String description) throws DataAccessException;
  
  public List<Table> getTableList(String datasetName) throws DataAccessException;
  
  public void createTable(String datasetName, String tableName, String fieldList) throws DataAccessException;
  
  public void importJsonData(String datasetName, String tableName, String jsonData) throws DataAccessException;
  
  public void executeSql(String sql) throws DataAccessException;
  
  public <T> List<T> executeSqlWithResult(String sql) throws DataAccessException;
  
  public List<Application> getApplicationList() throws DataAccessException;
  
  public Application getApplication(String appname) throws DataAccessException;
  
  public void createApplication(String name, String description, String language);
  
  public List<Visualization> getVisualization(String appname);

  public String getTableDataInJson(String appname, String tablename);
  
}

