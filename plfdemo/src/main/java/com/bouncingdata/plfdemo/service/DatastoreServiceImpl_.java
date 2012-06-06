package com.bouncingdata.plfdemo.service;

import java.util.List;

import com.bouncingdata.plfdemo.datastore.DataStorage_;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;

public class DatastoreServiceImpl_ implements DatastoreService_ {
  
  private DataStorage_ dataStorage;
  
  public void setDataStorage(DataStorage_ ds) {
    this.dataStorage = ds;
  }

  @Override
  public List<Dataset> getDatasetList(int userId) throws Exception {
    return dataStorage.getDatasetList(userId);
  }

  @Override
  public List<Application> getApplicationList(int userId) throws Exception {
    return dataStorage.getApplicationList(userId);
  }

  @Override
  public void createApplication(Application application) throws Exception {
    dataStorage.createApplication(application);
  }
  
  

}
