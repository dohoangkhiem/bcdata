package com.bouncingdata.plfdemo.service;

import java.util.Calendar;
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
import com.bouncingdata.plfdemo.datastore.pojo.model.AnalysisVote;
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
  public List<Analysis> getAnalysisList(int userId) throws Exception {
    return dataStorage.getAnalysisList(userId);
  }

  @Override
  public String createAnalysis(String name, String description, String language, int userId, String authorName, int lineCount, boolean isPublished, String tags) throws Exception {
    Analysis Analysis = new Analysis();
    Analysis.setName(name);
    Analysis.setDescription(description);
    Analysis.setLanguage(language);
    Analysis.setLineCount(lineCount);
    Analysis.setPublished(isPublished);
    Analysis.setTags(tags);
    // generate guid
    String guid = Utils.generateGuid();
    Analysis.setGuid(guid);
    Date date = Utils.getCurrentDate();
    Analysis.setCreateAt(date);
    Analysis.setLastUpdate(date);
    try {
      User user = dataStorage.getUser(userId);
      Analysis.setUser(user);
      dataStorage.createAnalysis(Analysis);
      return guid;
    } catch (DataAccessException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to create Analysis {}. UserId", name, userId);
        logger.debug("Root cause: ", e);
      }
      return null;
    }
  }

  @Override
  public void updateAnalysis(Analysis Analysis) throws Exception {
    dataStorage.updateAnalysis(Analysis);
  }

  @Override
  public SearchResult search(String query) throws Exception {
    return dataStorage.search(query);
  }
  
  @Override
  public Analysis getAnalysis(int appId) throws Exception {
    return dataStorage.getAnalysis(appId);
  }

  @Override
  public Analysis getAnalysisByGuid(String guid) throws Exception {
    return dataStorage.getAnalysisByGuid(guid);
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
  public List<Dataset> getAnalysisDataset(int appId) throws Exception {
    return dataStorage.getAnalysisDataset(appId);
  }

  @Override
  public List<Visualization> getAnalysisVisualization(int appId) throws Exception {
    return dataStorage.getAnalysisVisualization(appId);
  }

  @Override
  public String readDataset(String dataset) throws Exception {
    /*List<Object> objects = dataStorage.readDataset(dataset);
    System.out.println (objects);*/
    return "";
  }

  @Override
  public Map<String, String> getDataSetMap(int appId) throws Exception {
    List<Dataset> datasets = getAnalysisDataset(appId);
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
  public Dataset getDatasetByGuid(String guid) throws Exception {
    return dataStorage.getDatasetByGuid(guid);
  }

  @Override
  public void invalidateViz(Analysis Analysis) throws Exception {
    dataStorage.invalidateViz(Analysis);    
  }
  
  @Override
  public void updateDashboard(String guid, String status) throws Exception {
    try {
      dataStorage.updateDashboard(guid, status);
    } catch (DataAccessException e) {
      logger.error("Failed to update dashboard of analysis with guid {}", guid);
      logger.error("Exception detail", e);
    }
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
  public void publishAnalysis(User user, Analysis analysis, boolean value) throws Exception {
    try {
      analysis.setPublished(value);
      dataStorage.updateAnalysis(analysis);
      
      // add activity 
      Activity activity = new Activity();
      activity.setAction(Action.PUBLISH.getAction());
      activity.setUser(user);
      activity.setObjectId(analysis.getId());
      activity.setTime(new Date());
      activity.setPublic(value);
      dataStorage.createActivity(activity);
    } catch (DataAccessException e) {
      logger.error("Failed to add publish activity for user {}, analysisId {}", user.getUsername(), analysis.getId());
      logger.error("Exception detail", e);
    }
  }

  @Override
  public List<Activity> getRecentFeed(int userId) throws Exception {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, -5);
    return dataStorage.getFeed(userId, calendar.getTime());
  }

  @Override
  public void addAnalysisVote(int userId, int analysisId, AnalysisVote analysisVote) throws Exception {
    Analysis anls = dataStorage.getAnalysis(analysisId);
    if (anls == null) {
      if (logger.isDebugEnabled()) {
        logger.debug("Analysis id {} does not exist.", analysisId);
        return;
      }
    }
    
    // check if this user has voted before
    AnalysisVote oldVote = dataStorage.getAnalysisVote(userId, analysisId);
    if (oldVote == null) {
      dataStorage.addAnalysisVote(userId, analysisId, analysisVote);
    } else {
      if (oldVote.getVote() == analysisVote.getVote()) {
        return;
      } else {
        dataStorage.removeAnalysisVote(userId, analysisId);
      }
    }
  }

  @Override
  public void executeAnalysis(User user, Analysis analysis) throws Exception {
    // add activity 
    Activity activity = new Activity();
    activity.setAction(Action.UPDATE.getAction());
    activity.setUser(user);
    activity.setObjectId(analysis.getId());
    activity.setTime(new Date());
    activity.setPublic(analysis.isPublished());
    try {
      dataStorage.createActivity(activity);
    } catch (DataAccessException e) {
      logger.debug("", e);
    }
  }
}
