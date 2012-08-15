package com.bouncingdata.plfdemo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.bouncingdata.plfdemo.datastore.DataStorage;
import com.bouncingdata.plfdemo.datastore.pojo.dto.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Activity;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Comment;
import com.bouncingdata.plfdemo.datastore.pojo.model.CommentVote;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;
import com.bouncingdata.plfdemo.utils.Action;
import com.bouncingdata.plfdemo.utils.Utils;

@Transactional
public class DatastoreServiceImpl implements DatastoreService {
  
  private Logger logger = LoggerFactory.getLogger(DatastoreServiceImpl.class);
  
  @Autowired
  private DataStorage dataStorage;
    
  @Override
  public List<Dataset> getDatasetList(int userId) throws Exception {
    return dataStorage.getDatasetList(userId);
  }

  @Override
  public List<Application> getApplicationList(int userId) throws Exception {
    return dataStorage.getApplicationList(userId);
  }

  @Override
  public String createApplication(String name, String description, String language, int userId, String authorName, int lineCount, boolean isPublished, String tags) throws Exception {
    Application application = new Application();
    application.setName(name);
    application.setDescription(description);
    application.setLanguage(language);
    application.setLineCount(lineCount);
    application.setPublished(isPublished);
    application.setTags(tags);
    // generate guid
    String guid = Utils.generateGuid();
    application.setGuid(guid);
    Date date = Utils.getCurrentDate();
    application.setCreateAt(date);
    application.setLastUpdate(date);
    try {
      User user = dataStorage.getUser(userId);
      application.setUser(user);
      dataStorage.createApplication(application);
      return guid;
    } catch (DataAccessException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to create application {}. UserId", name, userId);
        logger.debug("Root cause: ", e);
      }
      return null;
    }
  }

  @Override
  public void updateApplication(Application application) throws Exception {
    dataStorage.updateApplication(application);
  }

  @Override
  public SearchResult search(String query) throws Exception {
    return dataStorage.search(query);
  }
  
  @Override
  public Application getApplication(int appId) throws Exception {
    return dataStorage.getApplication(appId);
  }

  @Override
  public Application getApplicationByGuid(String guid) throws Exception {
    return dataStorage.getApplicationByGuid(guid);
  }

  @Override
  public void createUser(User user) throws Exception {
    // check if user was existed
    User us = dataStorage.findUserByUsername(user.getUsername());
    if (us != null) {
      // loging
      logger.debug("The username {} was existed. Skip user creation.", us.getUsername());
      // throw custom exception
      throw new IllegalArgumentException("The username was existed.");
    }
    // check email
    
    user.setEnabled(true);
    user.setJoinedDate(new Date());
    
    // hash password
    // persist data
    dataStorage.createUser(user);
  }

  @Override
  public List<Dataset> getApplicationDataset(int appId) throws Exception {
    return dataStorage.getApplicationDataset(appId);
  }

  @Override
  public List<Visualization> getApplicationVisualization(int appId) throws Exception {
    return dataStorage.getApplicationVisualization(appId);
  }

  @Override
  public String readDataset(String dataset) throws Exception {
    /*List<Object> objects = dataStorage.readDataset(dataset);
    System.out.println (objects);*/
    return "";
  }

  @Override
  public Map<String, String> getDataSetMap(int appId) throws Exception {
    List<Dataset> datasets = getApplicationDataset(appId);
    if (datasets != null) {
      Map<String, String> result = new HashMap<String, String>();
      for (Dataset ds : datasets) {
        String s = readDataset(ds.getName());
        result.put(ds.getName(), s);
      }
      return result;
    }
    return null;
  }

  @Override
  public void createVisualization(Visualization visualization) throws Exception {
    dataStorage.createVisualization(visualization);
  }

  @Override
  public Dataset getDataset(String guid) throws Exception {
    return dataStorage.getDatasetByGuid(guid);
  }

  @Override
  public Analysis getDashboard(String guid) throws Exception {
    return dataStorage.getAnalysisByGuid(guid);
  }

  @Override
  public void invalidateViz(Application application) throws Exception {
    dataStorage.invalidateViz(application);    
  }
  
  @Override
  public void updateDashboard(String guid, String status) throws Exception {
    try {
      dataStorage.saveDashboard(guid, status, false);
      
      Analysis analysis = dataStorage.getAnalysisByGuid(guid);
      Application application = dataStorage.getApplicationByGuid(guid);
      Activity activity = new Activity();
      activity.setAction(Action.UPDATE.getAction());
      activity.setObjectId(analysis.getId());
      activity.setTime(new Date());
      activity.setUser(application.getUser());
      dataStorage.createActivity(activity);
    } catch (DataAccessException e) {
      logger.error("Failed to update dashboard or create new update activity for analysis with guid {}", guid);
      logger.error("Exceptio detail", e);
    }
  }

  @Override
  public void createDashboard(String guid, String status) throws Exception {
    dataStorage.saveDashboard(guid, status, true);
  }

  @Override
  public List<Comment> getComments(int analysisId) throws Exception {
    Analysis analysis  = dataStorage.getAnalysis(analysisId);
    if (analysis == null) {
      // logging
      return null;
    }
    
    return dataStorage.getComments(analysisId);

  }
  
  @Override
  public void addComment(int userId, int analysisId, Comment comment) throws Exception {
    dataStorage.addComment(userId, analysisId, comment);
  }

  @Override
  public Comment getComment(int commentId) throws Exception {
    return dataStorage.getComment(commentId);
  }

  @Override
  public CommentVote getCommentVote(int userId, int commentId) throws Exception {
    // check user, comment
    return dataStorage.getCommentVote(userId, commentId);
  }

  @Override
  public void addCommentVote(int userId, int commentId, CommentVote commentVote) throws Exception {
    // check logic here
    Comment comment = dataStorage.getComment(commentId);
    if (comment == null) {
      if (logger.isDebugEnabled()) {
        logger.debug("Comment id {} does not exist.", commentId);
        return;
      }
    }
    
    // check if this user has voted before
    CommentVote oldVote = dataStorage.getCommentVote(userId, commentId);
    if (oldVote == null) {
      dataStorage.addCommentVote(userId, commentId, commentVote);
    } else {
      if (oldVote.getVote() == commentVote.getVote()) {
        return;
      } else {
        dataStorage.removeCommentVote(userId, commentId);
      }
    }
  }

  @Override
  public void removeCommentVote(int userId, int commentId) throws Exception {
    Comment comment = dataStorage.getComment(commentId);
    if (comment == null) {
      if (logger.isDebugEnabled()) {
        logger.debug("Comment id {} does not exist.", commentId);
        return;
      }
    }
    
    dataStorage.removeCommentVote(userId, commentId);
  }
  
  @Override
  public void publishAnalysis(User user, Analysis analysis) throws Exception {
    try {
      // add activity 
      Activity activity = new Activity();
      activity.setAction(Action.PUBLISH.getAction());
      activity.setUser(user);
      activity.setObjectId(analysis.getId());
      activity.setTime(new Date());
      dataStorage.createActivity(activity);
    } catch (DataAccessException e) {
      logger.error("Failed to add publish activity for user {}, analysisId {}", user.getUsername(), analysis.getId());
      logger.error("Exception detail", e);
    }
  }
}
