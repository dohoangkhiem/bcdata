package com.bouncingdata.plfdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bouncingdata.plfdemo.service.DatastoreService;

@Controller
public class ActivityController {
  
  private Logger logger = LoggerFactory.getLogger(ActivityController.class);
  
  @Autowired
  private DatastoreService datastoreService;
  
  public void getActivityStream() {
    
  }
}
