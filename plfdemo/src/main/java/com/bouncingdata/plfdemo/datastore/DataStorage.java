package com.bouncingdata.plfdemo.datastore;

import java.util.List;


import org.springframework.dao.DataAccessException;

import com.bouncingdata.plfdemo.datastore.pojo.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Datastore;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;

/**
 *
 */
public interface DataStorage {
  
  public List<Datastore> getDatastoreList() throws DataAccessException;
  
  public void createDatastore(String name, String description) throws DataAccessException;
  
  public Datastore getDatastore(String datastore) throws DataAccessException;
  
  public List<Dataset> getDatasetList(String datastoreName) throws DataAccessException;
  
  public void createDataset(String datastoreName, String datasetName, String fieldList) throws DataAccessException;
  
  public void importJsonData(String datastoreName, String datasetName, String jsonData) throws DataAccessException;
  
  public void executeSql(String sql) throws Exception;
  
  public String executeSqlWithResult(String sql) throws Exception;
  
  public List<Application> getApplicationList() throws DataAccessException;
  
  public Application getApplication(String appname) throws DataAccessException;
  
  public int createApplication(String name, String description, String language);
  
  public List<Visualization> getVisualization(String appname);

  public String getDatasetDataInJson(String appname, String datasetName) throws DataAccessException;
  
  public void deleteVisualization(String appname, String visualizationName) throws DataAccessException;
  
  public void deleteDataset(String appname, String datasetName) throws DataAccessException;
  
  public List<Application> searchApplication(String query) throws DataAccessException;
  
  public List<Datastore> searchDatastore(String query) throws DataAccessException;
  
  public SearchResult search(String query) throws DataAccessException;
}

