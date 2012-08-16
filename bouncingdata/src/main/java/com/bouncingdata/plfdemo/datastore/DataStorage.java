package com.bouncingdata.plfdemo.datastore;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.bouncingdata.plfdemo.datastore.pojo.dto.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Activity;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.AnalysisVote;
import com.bouncingdata.plfdemo.datastore.pojo.model.Comment;
import com.bouncingdata.plfdemo.datastore.pojo.model.CommentVote;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.ExecutionLog;
import com.bouncingdata.plfdemo.datastore.pojo.model.Following;
import com.bouncingdata.plfdemo.datastore.pojo.model.Group;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;

/**
 * @author khiem
 */
/**
 * @author khiem
 *
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
   * Retrieves all <code>Analysis</code>s owned by an user.
   * @param user the user id to retrieve
   * @return a <code>List</code> of <code>Analysis</code> objects
   * @throws DataAccessException
   */
  public List<Analysis> getAnalysisList(int userId) throws DataAccessException;
  
  /**
   * Retrieves all private <code>Analysis</code>s owned by user.
   * @param user user id to retrieve
   * @return <code>List</code> of <code>Analysis</code>
   * @throws DataAccessException
   */
  public List<Analysis> getPrivateAnalyses(int userId) throws DataAccessException;
  
  /**
   * Retrieves all public <code>Analysis</code>s by user
   * @param user user id
   * @return <code>List</code> of <code>Analysis</code>
   * @throws DataAccessException
   */
  public List<Analysis> getPublicAnalyses(int userId) throws DataAccessException;
    
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
   * Retrieves all <code>Dataset</code>s related to an <code>Analysis</code>
   * @param analysisId the analysis id
   * @return a <code>List</code> of <code>Dataset</code>s
   * @throws DataAccessException
   */
  public List<Dataset> getAnalysisDataset(int analysisId) throws DataAccessException;
  
  /**
   * Retrieves all <code>Visualization</code>s related to an <code>Analysis</code>.
   * @param analysisId the analysis id
   * @return a <code>List</code> of <code>Visualization</code>s
   * @throws DataAccessException
   */
  public List<Visualization> getAnalysisVisualization(int analysisId) throws DataAccessException;
  
  /**
   * Searches Analysiss and datasets by query string.
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
   * @param Analysis the <code>Analysis</code>
   * @throws DataAccessException
   */
  public void createAnalysis(Analysis Analysis) throws DataAccessException;
  
  /**
   * @param Analysis
   * @throws DataAccessException
   */
  public void updateAnalysis(Analysis Analysis) throws DataAccessException;
  
  /**
   * @param appId
   * @return
   * @throws DataAccessException
   */
  /**
   * @param id the id of analysis
   * @return
   * @throws DataAccessException
   */
  public Analysis getAnalysis(int analysisId) throws DataAccessException;
  
  /**
   * @param guid
   * @throws DataAccessException
   */
  public Analysis getAnalysisByGuid(String guid) throws DataAccessException;
  
  /**
   * @param userId
   * @return
   * @throws DataAccessException
   */
  public User getUser(int userId) throws DataAccessException;
    
  /**
   * @param userId
   * @return
   * @throws DataAccessException
   */
  public Collection<String> getUserAuthorities(int userId) throws DataAccessException;
  
  /*public List<Object> readDataset(String dataset) throws DataAccessException;*/
  
  /**
   * @param visualization
   * @throws DataAccessException
   */
  public void createVisualization(Visualization visualization) throws DataAccessException; 
  
  /**
   * @param guid
   * @return
   * @throws DataAccessException
   */
  public Dataset getDatasetByGuid(String guid) throws DataAccessException;
      
  /**
   * Update status of the analysis dashboard
   * @param guid the guid of analysis
   * @param status the dashboard status to save
   * @throws DataAccessException
   */
  public void updateDashboard(String guid, String status) throws DataAccessException;
  
  /**
   * @param app
   * @throws DataAccessException
   */
  public void invalidateViz(Analysis app) throws DataAccessException;
  
  /**
   * @param analysisId
   * @return
   * @throws DataAccessException
   */
  public List<Comment> getComments(int analysisId) throws DataAccessException;
  
  /**
   * @param userId
   * @param analysisId
   * @param comment
   * @throws DataAccessException
   */
  public void addComment(int userId, int analysisId, Comment comment) throws DataAccessException;
  
  /**
   * @param commentId
   * @throws DataAccessException
   */
  public void removeComment(int commentId) throws DataAccessException;
  
  /**
   * @param comment
   * @throws DataAccessException
   */
  public void updateComment(Comment comment) throws DataAccessException;
  
  /**
   * @param commentId
   * @return
   * @throws DataAccessException
   */
  public Comment getComment(int commentId) throws DataAccessException;
  
  /**
   * @param userId
   * @param commentId
   * @return
   * @throws DataAccessException
   */
  public CommentVote getCommentVote(int userId, int commentId) throws DataAccessException;
  
  /**
   * @param userId
   * @param commentId
   * @param commentVote
   * @throws DataAccessException
   */
  public void addCommentVote(int userId, int commentId, CommentVote commentVote) throws DataAccessException;
  
  /**
   * @param userId
   * @param commentId
   * @throws DataAccessException
   */
  public void removeCommentVote(int userId, int commentId) throws DataAccessException;
  
  /**
   * @param userId
   * @param analysisId
   * @return
   * @throws DataAccessException
   */
  public AnalysisVote getAnalysisVote(int userId, int analysisId) throws DataAccessException;
  
  /**
   * @param userId
   * @param analysisId
   * @param analysisVote
   * @throws DataAccessException
   */
  public void addAnalysisVote(int userId, int analysisId, AnalysisVote analysisVote) throws DataAccessException;
  
  /**
   * @param userId
   * @param analysisId
   * @throws DataAccessException
   */
  public void removeAnalysisVote(int userId, int analysisId) throws DataAccessException;
  
  /**
   * @param activityId
   * @return
   * @throws DataAccessException
   */
  public Activity getActivity(int activityId) throws DataAccessException;
  
  /**
   * @param activity
   * @throws DataAccessException
   */
  public void createActivity(Activity activity) throws DataAccessException;
  
  /**
   * @param activityId
   * @throws DataAccessException
   */
  public void removeActivity(int activityId) throws DataAccessException;
  
  /**
   * @param activity
   * @throws DataAccessException
   */
  public void updateActivity(Activity activity) throws DataAccessException;
  
  /**
   * @param userId
   * @param cutPoint
   * @return
   * @throws DataAccessException
   */
  public List<Activity> getUserActitity(int userId, Date cutPoint) throws DataAccessException;
  
  /**
   * @param userId
   * @return
   * @throws DataAccessException
   */
  public List<Following> getFollowingList(int userId)  throws DataAccessException;
  
  /**
   * Retrieves list of users which is following this user
   * @param userId
   * @return <code>List</code> of <code>User</code> objects
   * @throws DataAccessException
   */
  public List<User> getFollowers(int userId) throws DataAccessException;
  
  /**
   * Retrieves list of users which this user is currently following
   * @param userId
   * @return
   * @throws DataAccessException
   */
  public List<User> getFollowingUsers(int userId) throws DataAccessException;
 
  /**
   * @param userId
   * @param cutPoint
   * @return
   * @throws DataAccessException
   */
  public List<Activity> getFeed(int userId, Date cutPoint) throws  DataAccessException;
}
