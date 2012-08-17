package com.bouncingdata.plfdemo.datastore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.jdo.support.JdoDaoSupport;

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
import com.bouncingdata.plfdemo.datastore.pojo.model.GroupAuthority;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;

@SuppressWarnings("unchecked")
public class JdoDataStorage extends JdoDaoSupport implements DataStorage {
  
  private Logger logger = LoggerFactory.getLogger(JdoDataStorage.class); 

  @Override
  public List<Dataset> getDatasetList(int userId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Dataset.class);
    q.setFilter("user.id==" + userId);
    List<Dataset> results = null;
    try {
      results = (List<Dataset>) q.execute();
      results = (List<Dataset>) pm.detachCopyAll(results);
      return results;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public List<Analysis> getAnalysisList(int userId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Analysis.class);
    q.setFilter("user.id==" + userId);
    List<Analysis> results = null;
    try {
      results = (List<Analysis>) q.execute();
      results = (List<Analysis>) pm.detachCopyAll(results);
      return results;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public List<Analysis> getPrivateAnalyses(int userId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Analysis.class);
    q.setFilter("user.id==" + userId + " && isPublised==false");
    List<Analysis> results = null;
    try {
      results = (List<Analysis>) q.execute();
      results = (List<Analysis>) pm.detachCopyAll(results);
      return results;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public List<Analysis> getPublicAnalyses(int userId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Analysis.class);
    q.setFilter("user.id==" + userId + " && isPublised==true");
    List<Analysis> results = null;
    try {
      results = (List<Analysis>) q.execute();
      results = (List<Analysis>) pm.detachCopyAll(results);
      return results;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public User findUserByUsername(String username) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(User.class);
    User user = null;
    q.setFilter("username == '" + username + "'");
    try {
      List<User> results = (List<User>) q.execute();
      if (results.size() > 0) user = ((List<User>)pm.detachCopyAll(results)).get(0);
      return user;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public ExecutionLog getExecutionLog(String executionId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(ExecutionLog.class);
    q.setFilter("executionId==\"" + executionId + "\"");
    List<ExecutionLog> results = null;
    try {
      results = (List<ExecutionLog>) q.execute();
      results = (List<ExecutionLog>) pm.detachCopyAll(results);
      return (results.size()>0?results.get(0):null);
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public List<Dataset> getAnalysisDataset(int appId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Dataset.class);
    q.setFilter("appId==" + appId);
    List<Dataset> results = null;
    try {
      results = (List<Dataset>) q.execute();
      results = (List<Dataset>) pm.detachCopyAll(results);
      return results;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public List<Visualization> getAnalysisVisualization(int appId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Visualization.class);
    q.setFilter("appId==" + appId + " && isActive==true");
    List<Visualization> results = null;
    try {
      results = (List<Visualization>) q.execute();
      results = (List<Visualization>) pm.detachCopyAll(results);
      return results;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public SearchResult search(String query) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    SearchResult sr = new SearchResult();
    Query q = pm.newQuery(Analysis.class);
    try {
      q.setFilter("this.name.matches(\".*" + query + ".*\") || this.description.matches(\".*" + query + ".*\")");
      List<Analysis> apps = (List<Analysis>) pm.detachCopyAll((List<Analysis>) q.execute());
      sr.setAnalyses(apps);
      
      q = getPersistenceManager().newQuery(Dataset.class);
      q.setFilter("this.name.matches(\".*" + query + ".*\") || this.description.matches(\".*" + query + ".*\")");
      List<Dataset> datasets = (List<Dataset>) pm.detachCopyAll((List<Dataset>) q.execute());
      sr.setDatasets(datasets);
      return sr;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public void createUser(User user) throws DataAccessException {
    List<User> users = new ArrayList<User>();
    users.add(user);
    persistData(users);
  }

  @Override
  public void createGroup(Group group) throws DataAccessException {
    List<Group> groups = new ArrayList<Group>();
    groups.add(group);
    persistData(groups);
  }

  @Override
  public void createAnalysis(Analysis Analysis) throws DataAccessException {
    if (Analysis.getUser() == null) {
      return;
    }
    PersistenceManager pm = getPersistenceManager();
    User user = pm.getObjectById(User.class, Analysis.getUser().getId());
    if (user == null) {
      return;
    }
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Analysis.setUser(user);
      pm.makePersistent(Analysis);
      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
      pm.close();
    }
  }

  private <T> void persistData(Collection<T> collection) {
    if (collection != null && collection.size() > 0) {
      PersistenceManager pm = getPersistenceManager();
      Transaction tx = pm.currentTransaction();
      try {
        tx.begin();
        pm.makePersistentAll(collection);
        tx.commit();
      } finally {
        if (tx.isActive()) tx.rollback();
        pm.close();
      }      
    }
  }

  @Override
  public Collection<String> getUserAuthorities(int userId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    /*Query q = pm.newQuery(Membership.class);
    q.execute();
    q = pm.newQuery(GroupAuthority.class);
    q.execute();
    q = pm.newQuery(Group.class);
    q.execute();*/
    /**
     * "select ga.authority from users u, memberships m, group_authorities ga 
     *  where u.user_id = m.user_id and m.group_id = ga.group_id and u.user_id = " + userId     
     */
    // temporarily use JPQL
    Query q = pm.newQuery("javax.jdo.query.JPQL", 
        "SELECT ga FROM com.bouncingdata.plfdemo.datastore.pojo.model.User u, " +
        "com.bouncingdata.plfdemo.datastore.pojo.model.Membership m, " +
        "com.bouncingdata.plfdemo.datastore.pojo.model.GroupAuthority ga " +
        "WHERE u.id = m.userId AND m.groupId = ga.groupId AND u.id = " + userId);
    try {
      List<GroupAuthority> gas = (List<GroupAuthority>)q.execute();
      if (gas != null) {
        List<String> authorities = new ArrayList<String>();
        for (GroupAuthority ga : gas) {
          authorities.add(ga.getAuthority());
        }
        return authorities;
      }
      return null;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public Analysis getAnalysisByGuid(String guid) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Analysis.class);
    q.setFilter("guid == \"" + guid + "\"");
    Analysis anls = null;
    try {
      List<Analysis> anlses = (List<Analysis>) q.execute();
      anls = anlses.size()>0?anlses.get(0):null;
      if (anls != null) {
        anls.setCommentCount(anls.getComments()!=null?anls.getComments().size():0);
        anls = pm.detachCopy(anls);
      }
      return anls;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public void updateAnalysis(Analysis analysis) throws DataAccessException {
    if (analysis.getUser() == null) return;
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    User user = pm.getObjectById(User.class, analysis.getUser().getId());
    if (user == null) {
      return;
    }
    try {
      tx.begin();
      Analysis anls = pm.getObjectById(Analysis.class, analysis.getId());
      anls.setName(analysis.getName());
      anls.setDescription(analysis.getDescription());
      anls.setLanguage(analysis.getLanguage());
      anls.setUser(user);
      anls.setLastUpdate(new Date());
      anls.setPublished(analysis.isPublished());
      anls.setTags(analysis.getTags());
      anls.setLineCount(anls.getLineCount());
      //
      tx.commit();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
      pm.close();
    }
  }

  /*@Override
  public List<Object> readDataset(String dataset) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    
    Query q = getPersistenceManager().newQuery("javax.jdo.query.SQL", "select * from `" + dataset + "`");
    
    List<Object> result = (List<Object>) q.execute();
    return result;
    
    Collection results = (Collection) q.execute();
    
    for (Iterator itr = results.iterator (); itr.hasNext ();) {
      Object[] data = (Object[]) itr.next ();  
      
    }

  }*/

  @Override
  public void createVisualization(Visualization visualization) throws DataAccessException {
    List<Visualization> visuals = new ArrayList<Visualization>();
    visuals.add(visualization);
    persistData(visuals);    
  }

  @Override
  public Dataset getDatasetByGuid(String guid) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Dataset.class);
    q.setFilter("guid == \"" + guid + "\"");
    Dataset ds = null;
    try {
      List<Dataset> results = (List<Dataset>) q.execute();
      results = (List<Dataset>) pm.detachCopyAll(results);
      ds = results.size()>0?results.get(0):null;
      return ds;
    } finally {
      q.closeAll();
      pm.close();
    }

  }

  @Override
  public void updateDashboard(String guid, String status) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    Query q = pm.newQuery(Analysis.class);
    q.setFilter("guid == '" + guid + "'");
    try {
      tx.begin();
      List<Analysis> db = (List<Analysis>) q.execute();
      if (db != null && db.size() > 0) {
        Analysis analysis = db.get(0);
        analysis.setStatus(status);
        tx.commit();
      } else {
        if (logger.isDebugEnabled()) {
          logger.debug("Analysis {} not found.", guid);
        }
        return;
      } 
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public void invalidateViz(Analysis app) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Visualization.class);
    q.setFilter("appId == " + app.getId() + " && isActive == true");
    List<Visualization> vis = (List<Visualization>) q.execute();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      for (Visualization v : vis) {
        v.setActive(false);
      }
      tx.commit();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public List<Comment> getComments(int analysisId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Comment.class);
    q.setFilter("analysis.id == " + analysisId);
    q.setOrdering("createAt ascending");
    try {
      List<Comment> results = (List<Comment>) q.execute();
      results = (List<Comment>) pm.detachCopyAll(results);
      return results;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public void addComment(int userId, int analysisId, Comment comment) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      User user = pm.getObjectById(User.class, userId);
      Analysis analysis = pm.getObjectById(Analysis.class, analysisId);
      comment.setUser(user);
      comment.setAnalysis(analysis);
      pm.makePersistent(comment);
      tx.commit();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
      pm.close();
    }
  }

  @Override
  public void removeComment(int commentId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    Comment comment = pm.getObjectById(Comment.class, commentId);
    if (comment != null) {
      try {
        tx.begin();
        pm.deletePersistent(comment);
        tx.commit();
      } finally {
        if (tx.isActive()) tx.rollback();
        pm.close();
      }
    } else {
      throw new DataRetrievalFailureException("Comment id" + commentId + " does not exist.");
    }
  }

  @Override
  public void updateComment(Comment comment) throws DataAccessException {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Comment getComment(int commentId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    try {
      return pm.getObjectById(Comment.class, commentId);
    } finally {
      pm.close();
    }
  }

  @Override
  public Analysis getAnalysis(int analysisId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    try {
      Analysis anls = pm.getObjectById(Analysis.class, analysisId);
      anls.setCommentCount(anls.getComments()!=null?anls.getComments().size():0);
      return anls;
    } finally {
      pm.close();
    }
  }

  @Override
  public User getUser(int userId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    try {
      return pm.getObjectById(User.class, userId);
    } finally {
      pm.close();
    }
  }

  @Override
  public CommentVote getCommentVote(int userId, int commentId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(CommentVote.class);
    q.setFilter("user.id == " + userId + " && comment.id == " + commentId + " && isActive == true");
    try {
      List<CommentVote> results = (List<CommentVote>) q.execute();
      results = (List<CommentVote>) pm.detachCopyAll(results);
      if (results.size() > 0) {
        CommentVote vote = results.get(0);
        return vote;
      } else return null;
    } finally {
      pm.close();
    }
  }

  @Override
  public void addCommentVote(int userId, int commentId, CommentVote commentVote) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    User user = pm.getObjectById(User.class, userId);
    Comment comment = pm.getObjectById(Comment.class, commentId);
    if (user == null || comment == null) {
      throw new DataRetrievalFailureException("User or Comment object not found, userId " + userId + ", commentId " + commentId);
    }
    try {
      tx.begin();
      commentVote.setUser(user);
      commentVote.setComment(comment);
      int vote = commentVote.getVote();
      commentVote.setVote(vote>=0?1:-1);
      pm.makePersistent(commentVote);
      
      if (vote >= 0) {
        comment.setUpVote(comment.getUpVote()+1);
      } else {
        comment.setDownVote(comment.getDownVote()+1);
      }      
      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
      pm.close();
    }
  }

  @Override
  public void removeCommentVote(int userId, int commentId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    Query q = pm.newQuery(CommentVote.class);
    q.setFilter("user.id == " + userId + " && comment.id == " + commentId + " && isActive == true");
    List<CommentVote> results = (List<CommentVote>) q.execute();
    try {
      if (results.size() > 0) {
        tx.begin();
        CommentVote cv = results.get(0);
        cv.setActive(false);
        int vote = cv.getVote();
        Comment c = cv.getComment();
        // concurrent concern here? 
        if (vote >= 0) {
          c.setUpVote(c.getUpVote()-1);
        } else {
          c.setDownVote(c.getDownVote()-1);
        }
        tx.commit();
      }
    } finally {
      if (tx.isActive()) tx.rollback();
      q.closeAll();
      pm.close();
    }    
  }

  @Override
  public AnalysisVote getAnalysisVote(int userId, int analysisId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(AnalysisVote.class);
    q.setFilter("user.id == " + userId + " && analysis.id == " + analysisId + " && isActive == true");
    try {
      List<AnalysisVote> results = (List<AnalysisVote>) q.execute();
      results = (List<AnalysisVote>) pm.detachCopyAll(results);
      if (results.size() > 0) {
        AnalysisVote vote = results.get(0);
        return vote;
      } else return null;
    } finally {
      pm.close();
    }
  }

  @Override
  public void addAnalysisVote(int userId, int analysisId, AnalysisVote analysisVote) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    User user = pm.getObjectById(User.class, userId);
    Analysis anls = pm.getObjectById(Analysis.class, analysisId);
    if (user == null || anls == null) {
      throw new DataRetrievalFailureException("User or Analysis object not found, userId " + userId + ", analysisId " + analysisId);
    }
    try {
      tx.begin();
      analysisVote.setUser(user);
      analysisVote.setAnalysis(anls);
      int vote = analysisVote.getVote();
      analysisVote.setVote(vote>=0?1:-1);
      pm.makePersistent(analysisVote);
      
      if (vote >= 0) {
        anls.setScore(anls.getScore()+1);
      } else {
        anls.setScore(anls.getScore()-1);
      }      
      
      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
      pm.close();
    }
  }

  @Override
  public void removeAnalysisVote(int userId, int analysisId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    Query q = pm.newQuery(AnalysisVote.class);
    q.setFilter("user.id == " + userId + " && analysis.id == " + analysisId + " && isActive == true");
    List<AnalysisVote> results = (List<AnalysisVote>) q.execute();
    try {
      if (results.size() > 0) {
        tx.begin();
        AnalysisVote av = results.get(0);
        av.setActive(false);
        int vote = av.getVote();
        Analysis anls = av.getAnalysis();
        // concurrent concern here? 
        if (vote >= 0) {
          anls.setScore(anls.getScore()-1);
        } else {
          anls.setScore(anls.getScore()+1);
        }
        tx.commit();
      }
    } finally {
      if (tx.isActive()) tx.rollback();
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public Activity getActivity(int activityId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    try {
      return pm.getObjectById(Activity.class, activityId);
    } finally {
      pm.close();
    }
    
  }

  @Override
  public void createActivity(Activity activity) throws DataAccessException {
    if (activity.getUser() == null) {
      return;
    }
    PersistenceManager pm = getPersistenceManager();
    User user = pm.getObjectById(User.class, activity.getUser().getId());
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      activity.setUser(user);
      pm.makePersistent(activity);
      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
      pm.close();
    }
  }

  @Override
  public void removeActivity(int activityId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Activity activity = pm.getObjectById(Activity.class, activityId);
      pm.deletePersistent(activity);
      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
      pm.close();
    }
    
  }

  @Override
  public void updateActivity(Activity activity) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Activity pstObj = pm.getObjectById(Activity.class, activity.getId());
    if (pstObj == null) {
      throw new DataRetrievalFailureException("Can't find activity with id " + activity.getId());
    }
    
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      pstObj.setObjectId(activity.getObjectId());
      pstObj.setAction(activity.getAction());
      pstObj.setTime(activity.getTime());
      pstObj.setPublic(activity.isPublic());
      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
      pm.close();
    }
    
  }

  @Override
  public List<Activity> getUserActitity(int userId, Date cutPoint) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Activity.class);
    q.setFilter("user.id == " + userId + " && time >= cut_point");
    q.declareImports("java.util.Date");
    q.declareParameters("Date cut_point");
    q.setOrdering("time DESC");
    try {
      List<Activity> activities = (List<Activity>) q.execute(cutPoint);
      activities = (List<Activity>) pm.detachCopyAll(activities);
      return activities;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public List<User> getFollowers(int userId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Following.class);
    q.setFilter("user.id == " + userId);
    try {
      List<Following> followings = (List<Following>) q.execute();
      followings = (List<Following>) pm.detachCopyAll(followings);
      List<User> results = new ArrayList<User>();
      for(Following f : followings) {
        results.add(f.getFollower());
      }
      return results;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public List<User> getFollowingUsers(int userId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Following.class);
    q.setFilter("follower.id == " + userId);
    try {
      List<Following> followings = (List<Following>) q.execute();
      followings = (List<Following>) pm.detachCopyAll(followings);
      List<User> results = new ArrayList<User>();
      for (Following f : followings) {
        results.add(f.getUser());
      }
      return results;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public List<Activity> getFeed(int userId, Date cutPoint) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    // get list of feed (now, is list of following users)
    Query q = pm.newQuery(Following.class);
    q.setFilter("follower.id == " + userId);
    try {
      List<Following> followings =  (List<Following>) q.execute();
  
      // query from the 'activities' table, with condition: actor is in the above list
      q = pm.newQuery(Activity.class);
      StringBuilder filter = new StringBuilder();
      filter.append("(");
      for (Following f : followings) {
        filter.append("user.id == " + f.getUser().getId() + " ||");
      }
      
      filter.append(" user.id == " + userId + ")");
      q.setFilter(filter.toString() + " && (time >= cut_point) && isPublic == true");
      q.declareImports("import java.util.Date");
      q.declareParameters("Date cut_point");
      q.setOrdering("time DESC");
      List<Activity> activities = (List<Activity>) q.execute(cutPoint);
      activities = (List<Activity>) pm.detachCopyAll(activities);
      
      // set the target object
      for (Activity ac : activities) {
        Analysis anls = pm.getObjectById(Analysis.class, ac.getObjectId());
        List<Comment> comments = getComments(ac.getObjectId());
        anls.setCommentCount(comments!=null?comments.size():0);
        ac.setObject(anls);
      }
      return activities;
    } finally {
      q.closeAll();
      pm.close();
    } 
  }

  @Override
  public List<Following> getFollowingList(int userId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Following.class);
    q.setFilter("user.id == " + userId);
    try {
      List<Following> followings = (List<Following>) q.execute();
      followings = (List<Following>) pm.detachCopyAll(followings);
      return followings;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public List<User> findFriends(User finder, String query) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(User.class);
    q.setFilter("(this.username !=\"" + finder.getUsername() + "\") && (this.username.matches(\".*" + query + ".*\") || this.firstName.matches(\".*" + query + ".*\")"
      + " || this.lastName.matches(\".*" + query + ".*\") || this.email.matches(\".*" + query + ".*\"))");
    try {
      List<User> results = (List<User>) q.execute();
      results = (List<User>) pm.detachCopyAll(results);
      return results;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public void createFollowing(int follower, int target) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    User followerUser = pm.getObjectById(User.class, follower);
    User targetUser = pm.getObjectById(User.class, target);
    if (followerUser == null || targetUser == null) return;
    Following f = new Following(targetUser, followerUser, new Date());
    try {
      tx.begin();
      pm.makePersistent(f);
      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
      pm.close();
    }
  }
  
  @Override
  public void removeFollowing(int follower, int target) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    User followerUser = pm.getObjectById(User.class, follower);
    User targetUser = pm.getObjectById(User.class, target);
    if (followerUser == null || targetUser == null) return;
    Query q = pm.newQuery(Following.class);
    q.setFilter("user.id==" + target + " && follower.id==" + follower);
    try {
      tx.begin();
      q.deletePersistentAll();
      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
      pm.close();
    }
  }

  @Override
  public boolean isFollowing(int follower, int target) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Following.class);
    q.setFilter("user.id==" + target + " && follower.id==" + follower);
    try {
      List<Following> results = (List<Following>) q.execute();
      return (results != null && results.size() > 0);
    } finally {
      q.closeAll();
      pm.close();
    }
  }
}
