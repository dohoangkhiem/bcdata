package khiem.dataprj.demo.plfdemo.datastore;

import java.util.List;

import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Table;

public interface DatastoreDao {
  
  public List<Dataset> getDatasetList();
  
  public void createDataset(String name, String description);
  
  public List<Table> getTableList(String datasetName);
  
  public void createTable(String datasetName, String tableName, String fieldList);
  
  public void importJsonData(String datasetName, String tableName, String jsonData);
  
  public void executeSql(String sql);
  
  public <T> List<T> executeSqlWithResult(String sql);
}
