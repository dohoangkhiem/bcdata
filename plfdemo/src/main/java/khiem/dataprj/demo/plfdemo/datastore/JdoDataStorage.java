package khiem.dataprj.demo.plfdemo.datastore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.transaction.NotSupportedException;

import khiem.dataprj.demo.plfdemo.datastore.pojo.Application;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Table;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Visualization;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.jdo.support.JdoDaoSupport;

public class JdoDataStorage extends JdoDaoSupport implements DataStorage {

  public JdoDataStorage() {
  }

  @Override
  public List<Dataset> getDatasetList() {
    Query q = getPersistenceManager().newQuery(Dataset.class);
    q.compile();
    List<Dataset> datasets = (List) q.execute();
    return datasets;
  }

  @Override
  public void createDataset(String name, String description) {
    Dataset dataset = new Dataset(name, description);
    List<Dataset> datasets = new ArrayList<Dataset>();
    datasets.add(dataset);
    persistData(datasets);
  }

  @Override
  public List<Table> getTableList(String datasetName) {
    Query q = getPersistenceManager().newQuery(Table.class);
    q.setFilter("dataset==\"" + datasetName + "\"");
    List<Table> tables = (List<Table>) q.execute();
    return tables;
  }

  @Override
  public void createTable(String datasetName, String tableName, String fieldList) {
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
  public void createApplication(String name, String description, String language) throws DataAccessException {
    Application app = new Application(name, description, language);
    List<Application> apps = new ArrayList<Application>();
    apps.add(app);
    persistData(apps);
  }

  @Override
  public List<Visualization> getVisualization(String appname) {
    Query q = getPersistenceManager().newQuery(Visualization.class);
    q.setFilter("appname==\"" + appname + "\"");
    List<Visualization> visuals = (List<Visualization>) q.execute();
    return visuals;
  }

  @Override
  public String getTableDataInJson(String appname, String tablename) {
    throw new RuntimeException("Not support!");
  }

  @Override
  public Dataset getDataset(String dataset) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Dataset.class);
    q.setFilter("name==\"" + dataset + "\"");
    List<Dataset> dss = (List<Dataset>) q.execute();
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
  public void deleteTable(String appname, String tableName) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Table.class);
    q.setFilter("dataset == \"" + appname + "\" && name == \"" + tableName + "\"");
    q.deletePersistentAll();
  }

  @Override
  public List<Application> searchApplication(String query)
      throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Application.class); 
    q.setFilter("this.name.matches(\"*" + query + "*\") || " + 
      "this.description.matches(\"*" + query + "*\")");
    List<Application> apps = (List<Application>) q.execute();
    return apps;
  }

  @Override
  public List<Dataset> searchDataset(String query) throws DataAccessException {
    Query q = getPersistenceManager().newQuery(Dataset.class); 
    q.setFilter("this.name.matches(\"*" + query + "*\") || " + 
        "this.description.matches(\"*" + query + "*\")");
      List<Dataset> datasets = (List<Dataset>) q.execute();
      return datasets;
  }

}

