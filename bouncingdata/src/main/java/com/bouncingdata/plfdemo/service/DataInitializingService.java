package com.bouncingdata.plfdemo.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.bouncingdata.plfdemo.datastore.DataStorage;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;

@Transactional
public class DataInitializingService {
  
  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private DataStorage dataStorage;
  
  public void setDataStorage(DataStorage ds) {
    this.dataStorage = ds;
  }
  
  public void init() {
    logger.info("Initializing CustomUserDetailService...");
    User demo = dataStorage.findUserByUsername("demo");
    if (demo == null) {
      demo = new User();
      demo.setEmail("demo@bouncingdata.com");
      demo.setUsername("demo");
      demo.setEnabled(true);
      demo.setPassword("demo");
      demo.setJoinedDate(new Date());
      dataStorage.createUser(demo);
    }
    logger.info("Finished CustomUserDetailService.");
  }
  
  public void destroy() {
    
  }
}
