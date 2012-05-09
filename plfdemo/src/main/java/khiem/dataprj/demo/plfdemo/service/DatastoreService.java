package khiem.dataprj.demo.plfdemo.service;

import java.util.List;

import khiem.dataprj.demo.plfdemo.datastore.pojo.Application;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Table;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Visualization;

public interface DatastoreService {

  /**
   * Gets list of all datasets
   * @return
   */
  public List<Dataset> getDatasetList();
  
  /**
   * Gets list of all applications
   * @return
   */
  public List<Application> getApplicationList();
  
  public Application getApplication(String appname);
  
  /**
   * @param appname
   * @return
   */
  public Dataset getDataset(String dataset);
  
  /**
   * @param appname
   * @param description
   * @param language
   */
  public void createApplication(String appname, String description, String language);
  
  /**
   * @param name
   * @param description
   */
  public void createDataset(String name, String description);
  
  public List<Table> getTableList(String datasetName);
  
  public String getTableData(String appname, String tablename);
  
  public List<Visualization> getVisualizationList(String appname);
  
  public void executeQuery(String query);
  
  public String executeQueryWithResult(String query);
  
  public void deleteVisualization(String appname, String visualizationName);
}
