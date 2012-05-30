package com.bouncingdata.plfdemo.service;

import java.util.List;

import com.bouncingdata.plfdemo.datastore.pojo.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Datastore;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;


public interface DatastoreService {

  /**
   * Gets list of all datastores
   * @return
   */
  public List<Datastore> getDatastoreList();
  
  /**
   * Gets list of all applications
   * @return
   */
  public List<Application> getApplicationList();
  
  public Application getApplication(String appname);
  
  /**
   * @param appname
   * @return
   */
  public Datastore getDatastore(String datastore);
  
  /**
   * @param appname
   * @param description
   * @param language
   * 
   * @return application id, or -1 if failed
   */
  public int createApplication(String appname, String description, String language);
  
  /**
   * @param name
   * @param description
   */
  public void createDatastore(String name, String description);
  
  public List<Dataset> getDatasetList(String datasetName);
  
  public String getDatasetData(String appname, String datasetName);
  
  public List<Visualization> getVisualizationList(String appname);
  
  public void executeQuery(String query);
  
  public String executeQueryWithResult(String query);
  
  public void deleteVisualization(String appname, String visualizationName);
  
  public void deleteDataset(String appname, String datasetName);
  
  public List<Application> searchApplication(String query);
  
  public List<Datastore> searchDatastore(String query);
  
  public SearchResult search(String query);
}
