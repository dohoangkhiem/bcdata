package com.bouncingdata.plfdemo.datastore;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface BcDatastore {

  /**
   * @param dataset
   * @return
   */
  String getDataset(String dataset);

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

}
