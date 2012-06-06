package com.bouncingdata.plfdemo.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;

public interface DatastoreService_ {
  
  /**
   * 
   * @param userId the user id
   * @return <code>List</code> of <code>Dataset</code>s
   * @throws DataAccessException
   */
  public List<Dataset> getDatasetList(int userId) throws Exception;
  
  /**
   * @param userId the user id
   * @return
   * @throws DataAccessException
   */
  public List<Application> getApplicationList(int userId) throws Exception;
  
  /**
   * @throws Exception
   */
  public void createApplication(Application application) throws Exception;
}
