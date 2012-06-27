package com.bouncingdata.plfdemo.datastore;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.bouncingdata.plfdemo.datastore.pojo.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.ExecutionLog;
import com.bouncingdata.plfdemo.datastore.pojo.model.Group;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;

/**
 * @author khiem
 */
public interface DataStorage {

  /**
   * Retrieves all <code>Dataset</code>s owned by an user.
   * @param userId the user id to retrieve
   * @return a <code>List</code> of <code>Dataset</code> objects
   * @throws DataAccessException
   */
  public List<Dataset> getDatasetList(int userId) throws DataAccessException;
  
  /**
   * Retrieves all <code>Application</code>s owned by an user.
   * @param user the user id to retrieve
   * @return a <code>List</code> of <code>Application</code> objects
   * @throws DataAccessException
   */
  public List<Application> getApplicationList(int userId) throws DataAccessException;
  
  /**
   * Retrieves all private <code>Application</code>s owned by user.
   * @param user user id to retrieve
   * @return <code>List</code> of <code>Application</code>
   * @throws DataAccessException
   */
  public List<Application> getPrivateApplication(int userId) throws DataAccessException;
  
  /**
   * Retrieves all public <code>Application</code>s by user
   * @param user user id
   * @return <code>List</code> of <code>Application</code>
   * @throws DataAccessException
   */
  public List<Application> getPublicApplication(int userId) throws DataAccessException;
    
  /**
   * Finds the <code>User</code> by username.
   * @param username the username
   * @return <code>User</code> object, or null if not found
   * @throws DataAccessException
   */
  public User findUserByUsername(String username) throws DataAccessException;
  
  /**
   * Gets the <code>ExecutionLog</code> detail by execution id.
   * @param executionId the execution id
   * @return an <code>ExecutionLog</code> object
   * @throws DataAccessException
   */
  public ExecutionLog getExecutionLog(String executionId) throws DataAccessException;
  
  /**
   * Retrieves all <code>Dataset</code>s related to an <code>Application</code>
   * @param applicationId the application id
   * @return a <code>List</code> of <code>Dataset</code>s
   * @throws DataAccessException
   */
  public List<Dataset> getApplicationDataset(int applicationId) throws DataAccessException;
  
  /**
   * Retrieves all <code>Visualization</code>s related to an <code>Application</code>.
   * @param applicationId the application id
   * @return a <code>List</code> of <code>Visualization</code>s
   * @throws DataAccessException
   */
  public List<Visualization> getApplicationVisualization(int applicationId) throws DataAccessException;
  
  /**
   * Searches applications and datasets by query string.
   * @param query the query string
   * @return <code>SearchResult</code> object
   * @throws DataAccessException
   */
  public SearchResult search(String query) throws DataAccessException;
  
  /**
   * Creates new user
   * @param user the <code>User</code> object
   * @throws DataAccessException
   */
  public void createUser(User user) throws DataAccessException;
  
  /**
   * @param group the <code>Group</code> object
   * @throws DataAccessException
   */
  public void createGroup(Group group) throws DataAccessException;
  
  /**
   * @param application the <code>Application</code>
   * @throws DataAccessException
   */
  public void createApplication(Application application) throws DataAccessException;
  
  /**
   * @param application
   * @throws DataAccessException
   */
  public void updateApplication(Application application) throws DataAccessException;
  
  /**
   * @param guid
   * @throws DataAccessException
   */
  public Application getApplicationByGuid(String guid) throws DataAccessException;
  
  /**
   * @param userId
   * @return
   * @throws DataAccessException
   */
  public Collection<String> getUserAuthorities(int userId) throws DataAccessException;
  
  /*public List<Object> readDataset(String dataset) throws DataAccessException;*/
  
  public void createVisualization(Visualization visualization) throws DataAccessException; 
  
  public Dataset getDatasetByGuid(String guid) throws DataAccessException;
}