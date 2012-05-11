package khiem.dataprj.demo.plfdemo.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import khiem.dataprj.demo.plfdemo.datastore.DataStorage;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Application;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Table;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Visualization;

@Transactional
public class DatastoreServiceImpl implements DatastoreService {
  
  DataStorage dataStorage;
  
  public void setDataStorage(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }

  @Override
  public List<Dataset> getDatasetList() {
    return dataStorage.getDatasetList();
  }

  @Override
  public List<Application> getApplicationList() {
    return dataStorage.getApplicationList();
  }

  @Override
  public Application getApplication(String appname) {
    return dataStorage.getApplication(appname);
  }

  @Override
  public Dataset getDataset(String dataset) {
    return dataStorage.getDataset(dataset);
  }

  @Override
  public void createApplication(String appname, String description, String language) {
    dataStorage.createApplication(appname, description, language);
    
  }

  @Override
  public void createDataset(String name, String description) {
    dataStorage.createDataset(name, description);
  }

  @Override
  public List<Table> getTableList(String datasetName) {
    return dataStorage.getTableList(datasetName);
  }

  @Override
  public String getTableData(String appname, String tablename) {
    return dataStorage.getTableDataInJson(appname, tablename);
  }

  @Override
  public List<Visualization> getVisualizationList(String appname) {
    return dataStorage.getVisualization(appname);
  }

  @Override
  public void executeQuery(String query) {
  }

  @Override
  public String executeQueryWithResult(String query) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteVisualization(String appname, String visualizationName) {
    dataStorage.deleteVisualization(appname, visualizationName);
    
  }

  @Override
  public void deleteTable(String appname, String tableName) {
    dataStorage.deleteTable(appname, tableName);
  }
}
