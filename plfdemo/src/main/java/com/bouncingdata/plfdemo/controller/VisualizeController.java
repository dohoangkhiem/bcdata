package com.bouncingdata.plfdemo.controller;

import java.io.IOException;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.service.ApplicationStoreService;
import com.bouncingdata.plfdemo.service.DatastoreService;

@Controller
@RequestMapping("/visualize")
public class VisualizeController {
  
  private DatastoreService datastoreService;
  private ApplicationStoreService appStoreService;
  
  public void setAppStoreService(ApplicationStoreService appStoreService) {
    this.appStoreService = appStoreService;
  }
  
  public void setDatastoreService(DatastoreService dsService) {
    this.datastoreService = dsService;
  }
  
  @RequestMapping(value="/{appname}/{visualization}", method = RequestMethod.GET)
  public String get(@PathVariable String appname, @PathVariable String visualization, ModelMap model) {
    try { 
      Application app = datastoreService.getApplication(appname);
      if (app == null) {
        model.addAttribute("content", "Not found!");
      } else {
        String content = appStoreService.getVisualizationContent(app.getId(), visualization);
        model.addAttribute("content", content);
      }
    } catch (IOException e) {
      model.addAttribute("content", "Not found!");
    }
    return "visualize";
  }
}
