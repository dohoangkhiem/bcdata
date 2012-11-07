package com.bouncingdata.plfdemo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.bouncingdata.plfdemo.datastore.DataStorage;
import com.bouncingdata.plfdemo.datastore.pojo.dto.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.dto.UserInfo;
import com.bouncingdata.plfdemo.datastore.pojo.model.Activity;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.AnalysisDataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.AnalysisVote;
import com.bouncingdata.plfdemo.datastore.pojo.model.BcDataScript;
import com.bouncingdata.plfdemo.datastore.pojo.model.Comment;
import com.bouncingdata.plfdemo.datastore.pojo.model.CommentVote;
import com.bouncingdata.plfdemo.datastore.pojo.model.DataCollection;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Following;
import com.bouncingdata.plfdemo.datastore.pojo.model.Scraper;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;
import com.bouncingdata.plfdemo.util.Action;
import com.bouncingdata.plfdemo.util.Utils;

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
  public String createBcDataScript(BcDataScript script, String type) throws Exception {
    // generate guid
    String guid = Utils.generateGuid();
    script.setGuid(guid);
     
    try {      
      dataStorage.createBcDataScript(script);
      return guid;
    } catch (DataAccessException e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to create Analysis {}. Username {}", script.getName(), script.getUser().getUsername());
        logger.debug("Root cause: ", e);
      }
      return null;
    }
  }

  @Override
  public void updateAnalysis(Analysis Analysis) throws Exception {
    dataStorage.updateAnalysis(Analysis);
  }
  
  public void deleteAnalysis(int analysisId) throws Exception {
    Analysis anls = dataStorage.getAnalysis(analysisId);
    if (anls == null) {
      logger.debug("Analysis Id {} does not exist.", analysisId);
      return;
    }
    dataStorage.deleteAnalysis(analysisId);
  }

  @Override
  public SearchResult search(String query) throws Exception {
    return dataStorage.search(query);
  }
  
  @Override
  public SearchResult search(String query, int ownerId) throws Exception {
    return dataStorage.search(query, ownerId);
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
  public User getUser(int userId) throws Exception {
    return dataStorage.getUser(userId);
  }

  @Override
  public List<AnalysisDataset> getAnalysisDatasets(int appId) throws Exception {
    return dataStorage.getAnalysisDatasets(appId);
  }
  
  @Override
  public List<AnalysisDataset> getRelatedAnalysis(int datasetId) throws Exception {
    return dataStorage.getRelatedAnalysis(datasetId);
  }
  
  @Override
  public List<Dataset> getScraperDataset(int scraperId) throws Exception {
    return dataStorage.getScraperDatasets(scraperId);
  }

  @Override
  public List<Visualization> getAnalysisVisualizations(int appId) throws Exception {
    return dataStorage.getAnalysisVisualizations(appId);
  }

  @Override
  public String readDataset(String dataset) throws Exception {
    /*List<Object> objects = dataStorage.readDataset(dataset);
    System.out.println (objects);*/
    return "";
  }

  @Override
  public Map<String, String> getDataSetMap(int appId) throws Exception {
    /*List<Dataset> datasets = getAnalysisDatasets(appId);
    if (datasets != null) {
      Map<String, String> result = new HashMap<String, String>();
      for (Dataset ds : datasets) {
        String s = readDataset(ds.getName());
        result.put(ds.getName(), s);
      }
      return result;
    }*/
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
      activity.setAction(value?Action.PUBLISH.getAction():Action.UNPUBLISH.getAction());
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
    //Calendar calendar = Calendar.getInstance();
    //calendar.add(Calendar.DATE, -5);
    return dataStorage.getFeed(userId, 20);
  }
  
  @Override
  public List<Activity> getMoreFeed(int userId, int lastId) throws Exception {
    List<Following> followings = dataStorage.getFollowingList(userId);
    return dataStorage.getMoreFeed(userId, followings, lastId , 20);
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
  public void doPublishAction(User user, BcDataScript script) throws Exception {
    // add activity 
    Activity activity = new Activity();
    activity.setAction(Action.PUBLISH.getAction());
    activity.setUser(user);
    activity.setObjectId(script.getId());
    activity.setTime(new Date());
    activity.setPublic(script.isPublished());
    try {
      dataStorage.createActivity(activity);
    } catch (DataAccessException e) {
      logger.debug("", e);
    }
  }
  
  @Override
  public void doUpdateAction(User user, BcDataScript script) throws Exception {
    // add activity 
    Activity activity = new Activity();
    activity.setAction(Action.UPDATE.getAction());
    activity.setUser(user);
    activity.setObjectId(script.getId());
    activity.setTime(new Date());
    activity.setPublic(script.isPublished());
    try {
      dataStorage.createActivity(activity);
    } catch (DataAccessException e) {
      logger.debug("", e);
    }
  }

  @Override
  public List<UserInfo> getFollowers(int userId) throws Exception {
    List<User> users = dataStorage.getFollowers(userId);
    List<UserInfo> userInfos = new ArrayList<UserInfo>();
    for (User u : users) {
      UserInfo ui = new UserInfo(u.getId(), u.getUsername(), u.getFirstName(), u.getLastName(), u.getEmail(), null, null);
      ui.setFriend(dataStorage.isFollowing(userId, ui.getId()));
      userInfos.add(ui);
    }
    return userInfos;
  }

  @Override
  public List<UserInfo> getFollowingUsers(int userId) throws Exception {
    List<User> users = dataStorage.getFollowingUsers(userId);
    List<UserInfo> userInfos = new ArrayList<UserInfo>();
    for (User u : users) {
      UserInfo ui = new UserInfo(u.getId(), u.getUsername(), u.getFirstName(), u.getLastName(), u.getEmail(), null, null);
      ui.setFriend(true);
      userInfos.add(ui);
    }
    return userInfos;
  }

  @Override
  public List<UserInfo> findFriends(User finder, String query) throws Exception {
    List<User> users = dataStorage.findFriends(finder, query);
    List<UserInfo> userInfos = new ArrayList<UserInfo>();
    for (User u : users) {
      UserInfo ui = new UserInfo(u.getId(), u.getUsername(), u.getFirstName(), u.getLastName(), u.getEmail(), null, null);
      ui.setFriend(dataStorage.isFollowing(finder.getId(), ui.getId()));
      userInfos.add(ui);
    }
    return userInfos;
  }

  @Override
  public void createFollowing(int followerId, int targetId) throws Exception {
    if (dataStorage.isFollowing(followerId, targetId)) return;
    dataStorage.createFollowing(followerId, targetId);
  }

  @Override
  public void removeFollowing(int followerId, int targetId) throws Exception {
    dataStorage.removeFollowing(followerId, targetId);
    
  }

  @Override
  public void createDataset(Dataset dataset) throws Exception {
    dataStorage.createDataset(dataset);
  }

  @Override
  public void createDatasets(List<Dataset> datasets) throws Exception {
    dataStorage.createDatasets(datasets);
  }

  @Override
  public void invalidateDataset(Scraper scraper) throws Exception {
    dataStorage.invalidateDataset(scraper);
  }
  
  @Override
  public Scraper getScraperByGuid(String guid) throws Exception {
    return dataStorage.getScraperByGuid(guid);
  }

  @Override
  public List<Scraper> getPublicScrapers(int userId) throws Exception {
    return dataStorage.getPublicScrapers(userId);
  }

  @Override
  public List<Scraper> getScraperList(int userId) {
    return dataStorage.getScraperList(userId);
  }

  @Override
  public Dataset getDatasetByName(String identifier) throws Exception {
    /*int firstUnderlineIdx = identifier.indexOf("_");
    int secondUnderlineIdx = identifier.indexOf(ch, fromIndex)
    int userId = Integer.parseInt(identifier.substring(0, identifier.indexOf("_") + 1));
    String scraper = identifier.substring(identifier.indexOf("_") + 1, identifier.indexOf("_", iden) + 1)
    return dataStorage.getDatasetByName(userId, scraperId, dsName);*/
    return dataStorage.getDatasetByName(identifier);
  }

  @Override
  public void invalidateDatasets(Analysis analysis) {
    dataStorage.invalidateDatasets(analysis);
  }

  @Override
  public void createAnalysisDatasets(List<AnalysisDataset> anlsDts) {
    dataStorage.createAnalysisDatasets(anlsDts);  
  }
  
  @Override
  public void updateBcDataScript(BcDataScript script) {
    dataStorage.updateBcDataScript(script);
  }

  @Override
  public void createDataCollection(DataCollection collection) {
    dataStorage.createDataCollection(collection);
    
  }

  @Override
  public void deleteDataCollection(int collectionId) {
    dataStorage.deleteDataCollection(collectionId);
    
  }

  @Override
  public void updateDataCollection(DataCollection collection) {
    dataStorage.updateDataCollection(collection);
    
  }

  @Override
  public DataCollection getDataCollection(int collectionId) {
    return dataStorage.getDataCollection(collectionId);
  }

  @Override
  public List<DataCollection> getUserCollections(int userId) {
    return dataStorage.getUserCollections(userId);
  }

  @Override
  public void deleteUserCollections(int userId) {
    dataStorage.deleteUserCollections(userId);
    
  }

  @Override
  public void addDatasetToCollection(int datasetId, int collectionId) {
    dataStorage.addDatasetToCollection(datasetId, collectionId);
  }

}
