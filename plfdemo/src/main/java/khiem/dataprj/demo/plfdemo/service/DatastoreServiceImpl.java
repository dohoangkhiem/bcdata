package khiem.dataprj.demo.plfdemo.service;

import java.util.List;

import khiem.dataprj.demo.plfdemo.datastore.DataStorage;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Application;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;

public class DatastoreServiceImpl implements DatastoreService {
  
  DataStorage dataStorage;
  
  public void setDataStorage(DataStorage dataStorage) {
    this.dataStorage = dataStorage;
  }

  @Override
  public List<Dataset> getDatasetList() {
    return dataStorage.getDatasetList();
  }

  @Override
  public List<Application> getApplicationList() {
    return dataStorage.getApplicationList();
  }

  @Override
  public Application getApplication(String appname) {
    return dataStorage.getApplication(appname);
  }

  @Override
  public Dataset getDataset(String appname) {
    // TODO Auto-generated method stub
    return null;
  }

}
