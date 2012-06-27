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
  
  @RequestMapping(value="/{guid}/{vGuid}/{type}", method = RequestMethod.GET)
  public String get(@PathVariable String guid, @PathVariable String vGuid, @PathVariable String type, ModelMap model) {
    try { 
      String content = appStoreService.getVisualization(guid, vGuid, type);
      model.addAttribute("content", content);
    } catch (IOException e) {
      // log: file not found
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "visualize";
  }
}
