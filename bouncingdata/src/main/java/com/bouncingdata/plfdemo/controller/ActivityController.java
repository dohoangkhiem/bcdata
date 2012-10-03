package com.bouncingdata.plfdemo.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bouncingdata.plfdemo.datastore.pojo.model.Activity;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.service.DatastoreService;

@Controller
public class ActivityController {
  
  private Logger logger = LoggerFactory.getLogger(ActivityController.class);
  
  @Autowired
  private DatastoreService datastoreService;
  
  @RequestMapping(value={"/", "/stream", "/home"}, method=RequestMethod.GET)
  public String getActivityStream(ModelMap model, Principal principal) {
    try {
      User user = (User) ((Authentication)principal).getPrincipal();
      List<Activity> activities = datastoreService.getRecentFeed(user.getId());
      model.addAttribute("activities", activities);
    } catch (Exception e) {
      logger.debug("Failed to load activity stream", e);
      model.addAttribute("errorMsg", "Failed to load the activity stream");
    }
    return "stream";
  }
  
  @RequestMapping(value="/a/more/{lastId}", method=RequestMethod.GET)
  public @ResponseBody List<Activity> getMoreActivities(@PathVariable int lastId, ModelMap model, Principal principal) {
    try {
      User user = (User) ((Authentication)principal).getPrincipal();
      List<Activity> activities = datastoreService.getMoreFeed(user.getId(), lastId);
      return activities;
    } catch (Exception e) {
      logger.debug("Failed to load more activity", e);
      return null;
    }
  }
}
