package com.bouncingdata.plfdemo.service;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bouncingdata.plfdemo.datastore.DataStorage;
import com.bouncingdata.plfdemo.datastore.JdbcUserStorage;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.utils.Utils;

public class UserDataService {

  private Logger logger = LoggerFactory.getLogger(UserDataService.class);
  
  private DataStorage dataStorage;
  
  private JdbcUserStorage jdbcUserStorage;
  
  public void setDataStorage(DataStorage ds) {
    this.dataStorage = ds;
  }
  
  public void setJdbcUserStorage(JdbcUserStorage us) {
    this.jdbcUserStorage = us;
  }
  
  public Map<String, String> getApplicationDataset(String appGuid) throws Exception {
    Application app = dataStorage.getApplicationByGuid(appGuid);
    if (app == null) {
      logger.debug("Cannot find application guid " + appGuid);
      return null;
    }
    
    List<Dataset> datasets = dataStorage.getApplicationDataset(app.getId());
    if (datasets == null) return null;
    
    Map<String, String> dsMap = new HashMap<String, String>();
    for (Dataset ds : datasets) {
      String json = jdbcUserStorage.getDataset(ds.getName());
      dsMap.put(ds.getName(), json);
    }
    return dsMap;
  }
  
  public String getDataset(String dataset) throws Exception {
    return jdbcUserStorage.getDataset(dataset);
  }
}
