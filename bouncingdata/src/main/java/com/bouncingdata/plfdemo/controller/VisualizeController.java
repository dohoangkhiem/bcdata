package com.bouncingdata.plfdemo.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bouncingdata.plfdemo.service.ApplicationStoreService;

@Controller
@RequestMapping(value={"/visualize", "/public"})
public class VisualizeController {
  
  private Logger logger = LoggerFactory.getLogger(VisualizeController.class);
   
  @Autowired
  private ApplicationStoreService appStoreService;
    
  @RequestMapping(value="/app/{guid}/{vGuid}/{type}", method = RequestMethod.GET)
  public String get(@PathVariable String guid, @PathVariable String vGuid, @PathVariable String type, ModelMap model) {
    try { 
      String content = appStoreService.getVisualization(guid, vGuid, type);
      model.addAttribute("content", content);
    } catch (IOException e) {
      // log: file not found
      logger.debug("Failed to find visualization file, appGuid: {}, vGuid: {}", guid, vGuid);
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "visualize";
  }
  
  @RequestMapping(value="/temp/{executionId}/{name}/{type}", method = RequestMethod.GET)
  public String temp(@PathVariable String executionId, @PathVariable String name, @PathVariable String type, ModelMap model) {
    try {
      String content = appStoreService.getTemporaryVisualization(executionId, name, type);
      model.addAttribute("content", content);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "visualize";
  }

}
