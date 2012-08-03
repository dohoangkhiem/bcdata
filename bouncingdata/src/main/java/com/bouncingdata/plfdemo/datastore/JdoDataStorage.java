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
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.Comment;
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
    Query q = getPersistenceManager().newQuery(Dataset.class);
    q.setFilter("author==" + userId);
    return (List<Dataset>)q.execute();
  }

  @Override
  public List<Application> getApplicationList(int userId) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Application.class);
    q.setFilter("author==" + userId);
    return (List<Application>)q.execute();
  }

  @Override
  public List<Application> getPrivateApplication(int userId) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Application.class);
    q.setFilter("author==" + userId + " && isPublised==false");
    return (List<Application>)q.execute();
  }

  @Override
  public List<Application> getPublicApplication(int userId) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Application.class);
    q.setFilter("author==" + userId + " && isPublised==true");
    return (List<Application>)q.execute();
  }

  @Override
  public User findUserByUsername(String username) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(User.class);
    q.setFilter("username==\"" + username + "\"");
    List<User> users = (List<User>) q.execute();
    return users != null ? users.size() > 0 ? users.get(0) : null : null;
  }

  @Override
  public ExecutionLog getExecutionLog(String executionId) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(ExecutionLog.class);
    q.setFilter("executionId==\"" + executionId + "\"");
    List<ExecutionLog> results = (List<ExecutionLog>) q.execute();
    return (results != null && results.size() > 0)?results.get(0):null;
  }

  @Override
  public List<Dataset> getApplicationDataset(int appId) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Dataset.class);
    q.setFilter("appId==" + appId);
    return (List<Dataset>) q.execute();
  }

  @Override
  public List<Visualization> getApplicationVisualization(int appId) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Visualization.class);
    q.setFilter("appId==" + appId + " && isActive==true");
    return (List<Visualization>) q.execute();
  }

  @Override
  public SearchResult search(String query) throws DataAccessException {
    SearchResult sr = new SearchResult();
    Query q = getPersistenceManager().newQuery(Application.class);
    q.setFilter("this.name.matches(\".*" + query + ".*\") || this.description.matches(\".*" + query + ".*\")");
    List<Application> apps = (List<Application>) q.execute();
    sr.setApplications(apps);
    
    q = getPersistenceManager().newQuery(Dataset.class);
    q.setFilter("this.name.matches(\".*" + query + ".*\") || this.description.matches(\".*" + query + ".*\")");
    List<Dataset> datasets = (List<Dataset>) q.execute();
    sr.setDatasets(datasets);
    return sr;
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
      }      
    }
  }

  @Override
  public Collection<String> getUserAuthorities(int userId) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Membership.class);
    q.execute();
    q = getPersistenceManager().newQuery(GroupAuthority.class);
    q.execute();
    q = getPersistenceManager().newQuery(Group.class);
    q.execute();
    /**
     * "select ga.authority from users u, memberships m, group_authorities ga 
     *  where u.user_id = m.user_id and m.group_id = ga.group_id and u.user_id = " + userId     
     */
    // temporarily use JPQL
    q = getPersistenceManager().newQuery("javax.jdo.query.JPQL", 
        "SELECT ga FROM com.bouncingdata.plfdemo.datastore.pojo.model.User u, " +
        "com.bouncingdata.plfdemo.datastore.pojo.model.Membership m, " +
        "com.bouncingdata.plfdemo.datastore.pojo.model.GroupAuthority ga " +
        "WHERE u.id = m.userId AND m.groupId = ga.groupId AND u.id = " + userId);
    List<GroupAuthority> gas = (List<GroupAuthority>)q.execute();
    if (gas != null) {
      List<String> authorities = new ArrayList<String>();
      for (GroupAuthority ga : gas) {
        authorities.add(ga.getAuthority());
      }
      return authorities;
    }
    return null;
  }

  @Override
  public Application getApplicationByGuid(String guid) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Application.class);
    q.setFilter("guid == \"" + guid + "\"");
    List<Application> apps = (List<Application>) q.execute();
    return (apps!= null && apps.size() > 0)?apps.get(0):null;
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
      if (tx.isActive()) {
        tx.rollback();
      }
      if (logger.isDebugEnabled()) {
        logger.debug("Exception occurs when update application " + application.getGuid(), e);
      }
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
    Query q = getPersistenceManager().newQuery(Dataset.class);
    q.setFilter("guid == \"" + guid + "\"");
    List<Dataset> ds = (List<Dataset>) q.execute();
    return (ds!= null && ds.size() > 0)?ds.get(0):null;

  }

  @Override
  public Analysis getAnalysisByGuid(String guid) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Analysis.class);
    q.setFilter("guid == '" + guid + "'");
    List<Analysis> db = (List<Analysis>) q.execute();
    return (db!=null && db.size() > 0)?db.get(0):null;
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
    try {
      tx.begin();
      Query q = pm.newQuery(Analysis.class);
      q.setFilter("guid == '" + guid + "'");
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
    }
  }

  @Override
  public List<Comment> getComments(int analysisId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Comment.class);
    q.setFilter("analysis == " + analysisId);
    List<Comment> results = (List<Comment>) q.execute();
    return results;
  }

  @Override
  public void addComment(Comment comment) throws DataAccessException {
    List<Comment> comments = new ArrayList<Comment>();
    comments.add(comment);
    persistData(comments);
  }

  @Override
  public void removeComment(Comment comment) throws DataAccessException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void updateComment(Comment comment) throws DataAccessException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Analysis getAnalysis(int analysisId) throws DataAccessException {
    PersistenceManager pm = getPersistenceManager();
    Query q = pm.newQuery(Analysis.class);
    q.setFilter("id == " + analysisId);
    List<Analysis> results = (List<Analysis>) q.execute();
    return results.size()>0?results.get(0):null;
  }
}
