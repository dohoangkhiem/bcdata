package khiem.dataprj.demo.plfdemo.service;

import java.util.List;

import khiem.dataprj.demo.plfdemo.datastore.DataStorage;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Application;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Table;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Visualization;

public class UserDataService implements DatastoreService {
  
  DataStorage dataStorage;
  
  public void setDataStorage(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }
  
  @Override
  public List<Dataset> getDatasetList() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Application> getApplicationList() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Application getApplication(String appname) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Dataset getDataset(String appname) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void createApplication(String appname, String description,
      String language) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void createDataset(String name, String description) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<Table> getTableList(String datasetName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getTableData(String appname, String tablename) {
    return dataStorage.getTableDataInJson(appname, tablename);
  }

  public void deleteTable(String appname, String tableName) {
    dataStorage.deleteTable(appname, tableName);
  }
  
  @Override
  public List<Visualization> getVisualizationList(String appname) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void executeQuery(String query) {
    try {
      dataStorage.executeSql(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public String executeQueryWithResult(String query) {
    try {
      return dataStorage.executeSqlWithResult(query);
    }  catch (Exception e) {
      return null;
    }
  }

  @Override
  public void deleteVisualization(String appname, String visualizationName) {
    
    
  }

}
