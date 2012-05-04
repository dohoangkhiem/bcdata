package khiem.dataprj.demo.plfdemo.service;

import java.util.List;

import khiem.dataprj.demo.plfdemo.datastore.pojo.Application;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;

public interface DatastoreService {

  public List<Dataset> getDatasetList();
  
  public List<Application>  getApplicationList();
  
  public Application getApplication(String appname);
  
  public Dataset getDataset(String appname);
  
}
