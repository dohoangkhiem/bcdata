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
import org.springframework.orm.jdo.support.JdoDaoSupport;

import com.bouncingdata.plfdemo.datastore.pojo.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.AnalysisVote;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Comment;
import com.bouncingdata.plfdemo.datastore.pojo.model.CommentVote;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.ExecutionLog;
import com.bouncingdata.plfdemo.datastore.pojo.model.Group;
import com.bouncingdata.plfdemo.datastore.pojo.model.GroupAuthority;
import com.bouncingdata.plfdemo.datastore.pojo.model.Membership;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;

@SuppressWarnings("unchecked")
public class JdoDataStorage extends JdoDaoSupport implements DataStorage {
  
  private Logger logger = LoggerFactory.getLogger(JdoDataStorage.class); 

  @Override
  public List<Dataset> getDatasetList(int userId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Dataset.class);
    q.setFilter("author==" + userId);
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
  public List<Application> getApplicationList(int userId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Application.class);
    q.setFilter("author==" + userId);
    List<Application> results = null;
    try {
      results = (List<Application>) q.execute();
      results = (List<Application>) pm.detachCopyAll(results);
      return results;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public List<Application> getPrivateApplication(int userId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Application.class);
    q.setFilter("author==" + userId + " && isPublised==false");
    List<Application> results = null;
    try {
      results = (List<Application>) q.execute();
      results = (List<Application>) pm.detachCopyAll(results);
      return results;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public List<Application> getPublicApplication(int userId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Application.class);
    q.setFilter("author==" + userId + " && isPublised==true");
    List<Application> results = null;
    try {
      results = (List<Application>) q.execute();
      results = (List<Application>) pm.detachCopyAll(results);
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
  public List<Dataset> getApplicationDataset(int appId) throws DataAccessException {
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
  public List<Visualization> getApplicationVisualization(int appId) throws DataAccessException {
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
    Query q = pm.newQuery(Application.class);
    try {
      q.setFilter("this.name.matches(\".*" + query + ".*\") || this.description.matches(\".*" + query + ".*\")");
      List<Application> apps = (List<Application>) pm.detachCopyAll((List<Application>) q.execute());
      sr.setApplications(apps);
      
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
  public void createApplication(Application application) throws DataAccessException {
    List<Application> apps = new ArrayList<Application>();
    apps.add(application);
    persistData(apps);
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
  public Application getApplicationByGuid(String guid) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Application.class);
    q.setFilter("guid == \"" + guid + "\"");
    Application app = null;
    try {
      List<Application> apps = (List<Application>) q.execute();
      apps = (List<Application>) pm.detachCopyAll(apps);
      app = apps.size()>0?apps.get(0):null;
      return app;
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public void updateApplication(Application application) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Application app = getApplicationByGuid(application.getGuid());
      app.setName(application.getName());
      app.setDescription(application.getDescription());
      app.setLanguage(app.getLanguage());
      app.setAuthorName(app.getAuthorName());
      app.setLastUpdate(new Date());
      app.setPublished(application.isPublished());
      app.setTags(application.getTags());
      app.setLineCount(app.getLineCount());
      app.setAuthor(application.getAuthor());
      tx.commit();
    } catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Exception occurs when update application " + application.getGuid(), e);
      }
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
  public Analysis getAnalysisByGuid(String guid) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = null;
    try {
      q = pm.newQuery(Analysis.class);
      q.setFilter("guid == '" + guid + "'");
      List<Analysis> db = (List<Analysis>) q.execute();
      db = (List<Analysis>) pm.detachCopyAll(db);
      return db.size() > 0 ? db.get(0):null;
    } finally {
      if (q != null) q.closeAll();
      pm.close();
    }
  }

  @Override
  public void saveDashboard(String guid, String status, boolean isNew) throws DataAccessException {
    /*if (isNew) {
      Dashboard db = new Dashboard(guid, status);
      List<Dashboard> ldb = new ArrayList<Dashboard>();
      persistData(ldb);
      return;
    }*/
    PersistenceManager pm = getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    Query q = pm.newQuery(Analysis.class);
    q.setFilter("guid == '" + guid + "'");
    try {
      tx.begin();
      List<Analysis> db = (List<Analysis>) q.execute();
      if (db != null && db.size() > 0) {
        Analysis dashboard = db.get(0);
        dashboard.setStatus(status);
        tx.commit();
      } else {
        if (logger.isDebugEnabled()) {
          logger.debug("Dashboard {} not found.", guid);
        }
        Analysis d = new Analysis(guid, status);
        List<Analysis> ld = new ArrayList<Analysis>();
        ld.add(d);
        persistData(ld);
      } 
    } catch (Exception e) {
      if (logger.isDebugEnabled()) logger.debug("", e);
      if (tx.isActive()) {
        tx.rollback();
      }
    } finally {
      q.closeAll();
      pm.close();
    }
  }

  @Override
  public void invalidateViz(Application app) throws DataAccessException {
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
    } catch (Exception e) {
      if (tx.isActive()) {
        tx.rollback();
      }
    } finally {
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
    } catch (Exception e) {
      if (logger.isDebugEnabled()) logger.debug("", e);
      if (tx.isActive()) {
        tx.rollback();
      }
    } finally {
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
      } catch (Exception e) {
        if (tx.isActive()) tx.rollback();
      } finally {
        pm.close();
      }
    } else {
      if (logger.isDebugEnabled()) {
        logger.debug("Comment id {} does not exist.", commentId);
      }
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
      return pm.getObjectById(Analysis.class, analysisId);
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
      if (logger.isDebugEnabled()) {
        logger.debug("Try to add comment vote, userId = {}, commentId = {}. User or Comment object is null.", userId, commentId);
      }
      return;
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
    } catch (Exception e) {
      logger.debug("Exception when persist comment vote {}", e);
      if (tx.isActive()) tx.rollback();
    } finally {
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
    } catch (Exception e) {
      logger.debug("Exception occurs when removing comment vote {}", e);
      if (tx.isActive()) tx.rollback();
    } finally {
      q.closeAll();
      pm.close();
    }
    //pm.deletePersistentAll(results);
    
  }

  @Override
  public AnalysisVote getAnalysisVote(int userId, int analysisId) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addAnalysisVote(int userId, int analysisId, AnalysisVote analysisVote) throws DataAccessException {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeAnalysisVote(int userId, int analysisId) throws DataAccessException {
    // TODO Auto-generated method stub

  }
}
