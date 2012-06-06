package com.bouncingdata.plfdemo.datastore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.jdo.support.JdoDaoSupport;

import com.bouncingdata.plfdemo.datastore.pojo.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.ExecutionLog;
import com.bouncingdata.plfdemo.datastore.pojo.model.Group;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;

@SuppressWarnings("unchecked")
public class JdoDataStorage_ extends JdoDaoSupport implements DataStorage_ {

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
    q.setFilter("appId==" + appId);
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
}
