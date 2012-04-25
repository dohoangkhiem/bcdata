package khiem.dataprj.demo.plfdemo.datastore;

import java.util.List;

import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Table;

public class JdoDatastoreDao implements DatastoreDao {

  @Override
  public List<Dataset> getDatasetList() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void createDataset(String name, String description) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<Table> getTableList(String datasetName) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void createTable(String datasetName, String tableName, String fieldList) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void importJsonData(String datasetName, String tableName,
      String jsonData) {
    // TODO Auto-generated method stub
    
  }

}
