package com.bouncingdata.plfdemo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.bouncingdata.plfdemo.datastore.DataStorage;
import com.bouncingdata.plfdemo.datastore.pojo.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.DashboardItem;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;
import com.bouncingdata.plfdemo.utils.Utils;

@Transactional
public class DatastoreServiceImpl implements DatastoreService {
  
  private Logger logger = LoggerFactory.getLogger(DatastoreServiceImpl.class);
  
  private DataStorage dataStorage;
  
  public void setDataStorage(DataStorage ds) {
    this.dataStorage = ds;
  }

  @Override
  public List<Dataset> getDatasetList(int userId) throws Exception {
    return dataStorage.getDatasetList(userId);
  }

  @Override
  public List<Application> getApplicationList(int userId) throws Exception {
    return dataStorage.getApplicationList(userId);
  }

  @Override
  public String createApplication(String name, String description, String language, int author, String authorName, int lineCount, boolean isPublished, String tags) throws Exception {
    Application application = new Application();
    application.setName(name);
    application.setDescription(description);
    application.setLanguage(language);
    application.setAuthor(author);
    application.setLineCount(lineCount);
    application.setPublished(isPublished);
    application.setTags(tags);
    // generate guid
    application.setGuid(Utils.generateGuid());
    Date date = Utils.getCurrentDate();
    application.setCreateAt(date);
    application.setLastUpdate(date);
    application.setAuthorName(authorName);
    dataStorage.createApplication(application);
    return application.getGuid();
  }

  @Override
  public void updateApplication(Application application) throws Exception {
    
  }

  @Override
  public SearchResult search(String query) throws Exception {
    return dataStorage.search(query);
  }

  @Override
  public Application getApplication(String guid) throws Exception {
    return dataStorage.getApplicationByGuid(guid);
  }

  @Override
  public void createUser(User user) throws Exception {
    // 
    // check if user was existed
    User us = dataStorage.findUserByUsername(user.getUsername());
    if (us != null) {
      // loging
      logger.debug("The username {} was existed. Skip user creation.", us.getUsername());
      // throw custom exception
      throw new IllegalArgumentException("The username was existed.");
    }
    // check email
    
    user.setEnabled(true);
    user.setJoinedDate(new Date());
    
    // hash password
    // persist data
    dataStorage.createUser(user);
  }

  @Override
  public List<Dataset> getApplicationDataset(int appId) throws Exception {
    return dataStorage.getApplicationDataset(appId);
  }

  @Override
  public List<Visualization> getApplicationVisualization(int appId) throws Exception {
    return dataStorage.getApplicationVisualization(appId);
  }

  @Override
  public String readDataset(String dataset) throws Exception {
    /*List<Object> objects = dataStorage.readDataset(dataset);
    System.out.println (objects);*/
    return "";
  }

  @Override
  public Map<String, String> getDataSetMap(int appId) throws Exception {
    List<Dataset> datasets = getApplicationDataset(appId);
    if (datasets != null) {
      Map<String, String> result = new HashMap<String, String>();
      for (Dataset ds : datasets) {
        String s = readDataset(ds.getName());
        result.put(ds.getName(), s);
      }
      return result;
    }
    return null;
  }

  @Override
  public void createVisualization(Visualization visualization) throws Exception {
    dataStorage.createVisualization(visualization);
  }

  @Override
  public Dataset getDataset(String guid) throws Exception {
    return dataStorage.getDatasetByGuid(guid);
  }

  @Override
  public List<DashboardItem> getDashboard(int appId) throws Exception {
    return dataStorage.getDashboard(appId);
  }

  @Override
  public void invalidateViz(Application application) throws Exception {
    dataStorage.invalidateViz(application);    
  }

}
