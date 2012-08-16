package com.bouncingdata.plfdemo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.bouncingdata.plfdemo.datastore.DataStorage;
import com.bouncingdata.plfdemo.datastore.JdbcUserStorage;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;

@Transactional
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
  
  public Map<String, String> getApplicationDataset(int appId) throws Exception {
    
    List<Dataset> datasets = dataStorage.getAnalysisDataset(appId);
    if (datasets == null) return null;
    
    Map<String, String> dsMap = new HashMap<String, String>();
    for (Dataset ds : datasets) {
      String json = jdbcUserStorage.getDataset(ds.getName());
      dsMap.put(ds.getName(), json);
    }
    return dsMap;
  }
  
  public List<Map> getDatasetToList(String dataset) throws Exception {
    return jdbcUserStorage.getDatasetToList(dataset);
  }
  
  public List<Map> query(String query) throws Exception {
    return jdbcUserStorage.query(query);
  }
}
