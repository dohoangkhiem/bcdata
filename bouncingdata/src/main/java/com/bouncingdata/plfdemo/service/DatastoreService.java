package com.bouncingdata.plfdemo.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.bouncingdata.plfdemo.datastore.pojo.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;

public interface DatastoreService {
  
  /**
   * 
   * @param userId the user id
   * @return <code>List</code> of <code>Dataset</code>s
   * @throws DataAccessException
   */
  public List<Dataset> getDatasetList(int userId) throws Exception;
  
  /**
   * @param userId the user id
   * @return <code>List</code> of <code>Application</code>s
   * @throws DataAccessException
   */
  public List<Application> getApplicationList(int userId) throws Exception;
  
  /**
   * @param guid
   * @return
   * @throws Exception
   */
  public Application getApplication(String guid) throws Exception;
  
  /**
   * Stores new application
   * @param application the <code>Application</code> to save
   * @throws Exception
   */
  public String createApplication(String name, String description, String language, int author, String authorName, int lineCount, boolean isPublished, String tags) throws Exception;
  
  /**
   * @param application
   * @throws Exception
   */
  public void updateApplication(Application application) throws Exception;
    
  /**
   * @param query
   * @return
   * @throws Exception
   */
  public SearchResult search(String query) throws Exception;
  
  public void createUser(User user) throws Exception;
}
