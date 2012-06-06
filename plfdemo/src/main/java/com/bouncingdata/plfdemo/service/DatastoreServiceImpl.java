package com.bouncingdata.plfdemo.service;

import java.util.List;


import org.springframework.transaction.annotation.Transactional;

import com.bouncingdata.plfdemo.datastore.DataStorage;
import com.bouncingdata.plfdemo.datastore.pojo.old.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Datastore;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Visualization;

@Transactional
public class DatastoreServiceImpl implements DatastoreService {
  
  DataStorage dataStorage;
  
  public void setDataStorage(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }

  @Override
  public List<Datastore> getDatastoreList() {
    return dataStorage.getDatastoreList();
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
  public Datastore getDatastore(String datastore) {
    return dataStorage.getDatastore(datastore);
  }

  @Override
  public int createApplication(String appname, String description, String language) {
    return dataStorage.createApplication(appname, description, language);

  }

  @Override
  public void createDatastore(String name, String description) {
    dataStorage.createDatastore(name, description);
  }

  @Override
  public List<Dataset> getDatasetList(String datastoreName) {
    return dataStorage.getDatasetList(datastoreName);
  }

  @Override
  public String getDatasetData(String appname, String tablename) {
    return dataStorage.getDatasetDataInJson(appname, tablename);
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
  public void deleteDataset(String appname, String datasetName) {
    dataStorage.deleteDataset(appname, datasetName);
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
