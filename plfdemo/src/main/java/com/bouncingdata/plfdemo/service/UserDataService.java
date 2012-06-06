package com.bouncingdata.plfdemo.service;

import java.util.List;

import com.bouncingdata.plfdemo.datastore.DataStorage;
import com.bouncingdata.plfdemo.datastore.pojo.old.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Datastore;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Visualization;


public class UserDataService implements DatastoreService {
  
  DataStorage dataStorage;
  
  public void setDataStorage(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }
  
  @Override
  public List<Datastore> getDatastoreList() {
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
  public Datastore getDatastore(String appname) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int createApplication(String appname, String description,
      String language) {
    // TODO Auto-generated method stub
    return dataStorage.createApplication(appname, description, language);
  }

  @Override
  public void createDatastore(String name, String description) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<Dataset> getDatasetList(String datastoreName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getDatasetData(String appname, String datasetName) {
    return dataStorage.getDatasetDataInJson(appname, datasetName);
  }

  public void deleteDataset(String appname, String tableName) {
    dataStorage.deleteDataset(appname, tableName);
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

  @Override
  public List<Application> searchApplication(String query) {
    return dataStorage.searchApplication(query);
  }

  @Override
  public List<Datastore> searchDatastore(String query) {
    return dataStorage.searchDatastore(query);
  }

  @Override
  public SearchResult search(String query) {
    return dataStorage.search(query);
  }

}
