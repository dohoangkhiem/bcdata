package com.bouncingdata.plfdemo.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bouncingdata.plfdemo.datastore.DataStorage;
import com.bouncingdata.plfdemo.datastore.JdbcBcDatastore;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.utils.Utils;

@Transactional
public class BcDatastoreService {

  private Logger logger = LoggerFactory.getLogger(BcDatastoreService.class);
  
  @Autowired
  private DataStorage dataStorage;
  
  @Autowired
  private JdbcBcDatastore jdbcBcDatastore;
      
  public Map<String, String> getApplicationDataset(int appId) throws Exception {
    
    List<Dataset> datasets = dataStorage.getAnalysisDataset(appId);
    if (datasets == null) return null;
    
    Map<String, String> dsMap = new HashMap<String, String>();
    for (Dataset ds : datasets) {
      String json = jdbcBcDatastore.getDataset(ds.getName());
      dsMap.put(ds.getName(), json);
    }
    return dsMap;
  }
  
  public List<Map> getDatasetToList(String dataset) throws Exception {
    return jdbcBcDatastore.getDatasetToList(dataset);
  }
  
  public List<Map> query(String query) throws Exception {
    return jdbcBcDatastore.query(query);
  }
  
  public void storeData(String username, String datasetName, String[] headers, List<String[]> data) throws Exception {
    jdbcBcDatastore.persistDataset(username + "." + datasetName , headers, data);
  }
  
  public void storeExcel(String username, String datasetName, InputStream excelIs) throws Exception {
    List<String[]> excelData = Utils.parseExcel(excelIs);
    jdbcBcDatastore.persistDataset(username + "." + datasetName , excelData.get(0), excelData.subList(1, excelData.size()));
  }
    
}
