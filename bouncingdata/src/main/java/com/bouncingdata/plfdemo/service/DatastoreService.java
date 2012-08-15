package com.bouncingdata.plfdemo.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.bouncingdata.plfdemo.datastore.pojo.dto.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Activity;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Comment;
import com.bouncingdata.plfdemo.datastore.pojo.model.CommentVote;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;

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
  
  public Application getApplication(int appId) throws Exception;
  
  /**
   * @param guid
   * @return
   * @throws Exception
   */
  public Application getApplicationByGuid(String guid) throws Exception;
  
  public Dataset getDataset(String guid) throws Exception;
  
  /**
   * @param name
   * @param description
   * @param language
   * @param author
   * @param authorName
   * @param lineCount
   * @param isPublished
   * @param tags
   * @return the id of newly created application
   * @throws Exception
   */
  public String createApplication(String name, String description, String language, int author, String authorName, int lineCount, boolean isPublished, String tags) throws Exception;
  
  /**
   * @param application
   * @throws Exception
   */
  public void updateApplication(Application application) throws Exception;
  
  /**
   * @param appId
   * @return
   * @throws Exception
   */
  public List<Dataset> getApplicationDataset(int appId) throws Exception;
  
  /**
   * @param appId
   * @return
   * @throws Exception
   */
  public List<Visualization> getApplicationVisualization(int appId) throws Exception;
    
  /**
   * @param query
   * @return
   * @throws Exception
   */
  public SearchResult search(String query) throws Exception;
  
  /**
   * Creates new user.
   * @param user the <code>User</code> instance
   * @throws Exception
   */
  public void createUser(User user) throws Exception;
  
  public String readDataset(String dataset) throws Exception;
  
  public Map<String, String> getDataSetMap(int appId) throws Exception;
  
  public void createVisualization(Visualization visualization) throws Exception;
  
  public Analysis getDashboard(String guid)  throws Exception;
  
  public void updateDashboard(String guid, String status) throws Exception;
  
  public void createDashboard(String guid, String status) throws Exception;
  
  public void invalidateViz(Application application) throws Exception;
  
  public List<Comment> getComments(int analysisId) throws Exception;
  
  public void addComment(int userId, int analysisId, Comment comment) throws Exception;
  
  public Comment getComment(int commentId) throws Exception;
  
  public CommentVote getCommentVote(int userId, int commentId) throws Exception;
  
  public void addCommentVote(int userId, int commentId, CommentVote commentVote) throws Exception;
  
  public void removeCommentVote(int userId, int commentId) throws Exception;
  
  public void publishAnalysis(User user, Analysis analysis) throws Exception;
  
  public List<Activity> getRecentFeed(int userId) throws Exception;
  
}
