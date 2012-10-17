package com.bouncingdata.plfdemo.datastore;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface BcDatastore {

  /**
   * @param dataset
   * @return
   */
  String getDataset(String dataset) throws Exception;

  /**
   * @param tableName
   * @param headers
   * @param data
   */
  void persistDataset(String tableName, String[] headers, List<String[]> data);

  /**
   * @param sql
   * @return
   */
  List<Map> query(String sql);

  /**
   * @param dataset
   * @return
   */
  List<Map> getDatasetToList(String dataset);
  
  /**
   * @param datasetName
   */
  void dropDataset(String datasetName);
  
  void getCsvStream(String dsFullname, OutputStream os) throws Exception;

}
