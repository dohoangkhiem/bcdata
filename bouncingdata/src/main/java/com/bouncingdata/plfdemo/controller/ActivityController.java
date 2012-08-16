package com.bouncingdata.plfdemo.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bouncingdata.plfdemo.datastore.pojo.model.Activity;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.service.DatastoreService;

@Controller
public class ActivityController {
  
  private Logger logger = LoggerFactory.getLogger(ActivityController.class);
  
  @Autowired
  private DatastoreService datastoreService;
  
  @RequestMapping(value={"/a", "/home"}, method=RequestMethod.GET)
  public String getActivityStream(ModelMap model, Principal principal) {
    try {
      User user = (User) ((Authentication)principal).getPrincipal();
      List<Activity> activities = datastoreService.getRecentFeed(user.getId());
      model.addAttribute("activities", activities);
    } catch (Exception e) {
      logger.error("", e);
      model.addAttribute("errorMsg", "Failed to load the Activity Stream");
    }
    return "home";
  }
}
