package com.bouncingdata.plfdemo.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bouncingdata.plfdemo.datastore.DataStorage;
import com.bouncingdata.plfdemo.datastore.pojo.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.utils.Utils;

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

}
