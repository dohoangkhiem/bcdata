package com.bouncingdata.plfdemo.datastore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.transaction.NotSupportedException;


import org.springframework.dao.DataAccessException;
import org.springframework.orm.jdo.support.JdoDaoSupport;

import com.bouncingdata.plfdemo.datastore.pojo.old.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Datastore;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Visualization;

public class JdoDataStorage extends JdoDaoSupport implements DataStorage {

  public JdoDataStorage() {
  }

  @Override
  public List<Datastore> getDatastoreList() {
    Query q = getPersistenceManager().newQuery(Datastore.class);
    q.compile();
    List<Datastore> datasets = (List) q.execute();
    return datasets;
  }

  @Override
  public void createDatastore(String name, String description) {
    Datastore dataset = new Datastore(name, description);
    List<Datastore> datasets = new ArrayList<Datastore>();
    datasets.add(dataset);
    persistData(datasets);
  }

  @Override
  public List<Dataset> getDatasetList(String datastoreName) {
    Query q = getPersistenceManager().newQuery(Dataset.class);
    q.setFilter("datastore==\"" + datastoreName + "\"");
    List<Dataset> datasets = (List<Dataset>) q.execute();
    return datasets;
  }

  @Override
  public void createDataset(String datastoreName, String datasetName, String fieldList) {
    String[] fields = fieldList.split(",");
    
  }

  @Override
  public void importJsonData(String datasetName, String tableName, String jsonData) {
    // TODO Auto-generated method stub
  }
  
  public <T> void persistData(Collection<T> collection) {
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
  public void executeSql(String sql) throws Exception {
    throw new NotSupportedException("");
  }

  @Override
  public String executeSqlWithResult(String sql) throws Exception {
    throw new NotSupportedException("");
  }

  @Override
  public List<Application> getApplicationList() throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Application.class);
    q.compile();
    List<Application> applications = (List) q.execute();
    return applications;
  }

  @Override
  public Application getApplication(String appname) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Application.class);
    q.setFilter("name==\"" + appname + "\"");
    List<Application> results = (List<Application>) q.execute();
    if (results != null && results.size() > 0) {
      return results.get(0);
    } else return null;
  }

  @Override
  public int createApplication(String name, String description, String language) throws DataAccessException {
    Application app = new Application(name, description, language);
    List<Application> apps = new ArrayList<Application>();
    apps.add(app);
    persistData(apps);
    return app.getId();
  }

  @Override
  public List<Visualization> getVisualization(String appname) {
    Query q = getPersistenceManager().newQuery(Visualization.class);
    q.setFilter("appname==\"" + appname + "\"");
    List<Visualization> visuals = (List<Visualization>) q.execute();
    return visuals;
  }

  @Override
  public String getDatasetDataInJson(String appname, String datasetName) {
    throw new RuntimeException("Not support!");
  }

  @Override
  public Datastore getDatastore(String datastore) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Datastore.class);
    q.setFilter("name==\"" + datastore + "\"");
    List<Datastore> dss = (List<Datastore>) q.execute();
    return (dss == null?null:(dss.size()>0?dss.get(0):null));
  }

  @Override
  public void deleteVisualization(String appname, String visualizationName)
      throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Visualization.class);
    q.setFilter("appname == \"" + appname + "\" && name == \"" + visualizationName + "\"" );
    q.deletePersistentAll();
  }

  @Override
  public void deleteDataset(String appname, String datasetName) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Dataset.class);
    q.setFilter("dataset == \"" + appname + "\" && name == \"" + datasetName + "\"");
    q.deletePersistentAll();
  }

  @Override
  public List<Application> searchApplication(String query)
      throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Application.class); 
    q.setFilter("this.name.matches(\".*" + query + ".*\") || " + 
      "this.description.matches(\".*" + query + ".*\")");
    List<Application> apps = (List<Application>) q.execute();
    return apps;
  }

  @Override
  public List<Datastore> searchDatastore(String query) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Datastore.class); 
    q.setFilter("this.name.matches(\".*" + query + ".*\") || " + 
        "this.description.matches(\".*" + query + ".*\")");
    List<Datastore> datastores = (List<Datastore>) q.execute();
    for(Datastore dstore : datastores) {
      dstore.setDatasets(getDatasetList(dstore.getName()));
    }
    return datastores;
  }

  public List<Dataset> searchDataset(String query) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Dataset.class); 
    q.setFilter("this.name.matches(\".*" + query + ".*\") || " + 
        "this.description.matches(\".*" + query + ".*\") || this.fieldList.matches(\".*" + query + ".*\")");
    List<Dataset> datasets = (List<Dataset>) q.execute();
    return datasets;
  }
  
  @Override
  public SearchResult search(String query) throws DataAccessException {
    SearchResult sr = new SearchResult();
    sr.setDatasets(searchDataset(query));
    sr.setApplications(searchApplication(query));
    sr.setDatastores(searchDatastore(query));
    return sr;
  }

}

