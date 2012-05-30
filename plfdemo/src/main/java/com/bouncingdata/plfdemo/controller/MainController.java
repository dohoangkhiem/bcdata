package com.bouncingdata.plfdemo.controller;

import java.io.IOException;
import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bouncingdata.plfdemo.datastore.pojo.ExecutionResult;
import com.bouncingdata.plfdemo.datastore.pojo.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Datastore;
import com.bouncingdata.plfdemo.service.ApplicationExecutor;
import com.bouncingdata.plfdemo.service.ApplicationStoreService;
import com.bouncingdata.plfdemo.service.DatastoreService;

@Controller
@RequestMapping(value = "/main")
public class MainController {
  
  private DatastoreService datastoreService;
  private ApplicationStoreService appStoreService;
  private ApplicationExecutor appExecutor;
  
  public void setDatastoreService(DatastoreService ds) {
    this.datastoreService = ds;
  }
  
  public void setAppStoreService(ApplicationStoreService appStoreService) {
    this.appStoreService = appStoreService;
  }
  
  public void setAppExecutor(ApplicationExecutor appExecutor) {
    this.appExecutor = appExecutor;
  }
    
  @RequestMapping(method = RequestMethod.GET)
  public String main(ModelMap model) {
    model.addAttribute("app", null);
    return "main";
  }
  
  @RequestMapping(value = "/datastore", method = RequestMethod.GET)
  public @ResponseBody List<Datastore> getAllDatastore(ModelMap model) {
    // get list of dataset
    try {
      List<Datastore> datastores = datastoreService.getDatastoreList();
      if (datastores != null) {
        for (Datastore ds : datastores) {
          ds.setDatasets(datastoreService.getDatasetList(ds.getName()));
        }
      }
      return datastores;
    } catch (Exception e) {
      // log
      e.printStackTrace();
      return null;
    }
  }
  
  @RequestMapping(value="/table/{datastore}")
  public @ResponseBody List<Dataset> getTables(@PathVariable String datastore) {
    return null;
  }
  
  @RequestMapping(value="/application", method = RequestMethod.GET)
  @ResponseBody
  public List<Application> getAllApplication() {
    try {
      List<Application> applications = datastoreService.getApplicationList();
      return applications;
    } catch (Exception e) {
      // log
      e.printStackTrace();
      return null; 
    }
  }
  
  @RequestMapping(value = "/createApp", method = RequestMethod.POST)
  @ResponseBody
  public String createApplication(@RequestParam(value = "appname", required = true) String appname,
      @RequestParam(value = "language", required = true) String language,
      @RequestParam(value = "description", required = true) String description,
      @RequestParam(value = "code", required = true) String code, ModelMap model) {
    
    // store app info
    int appId = datastoreService.createApplication(appname, description, language);
    
    // store app code file
    if (appId >= 0) {
      try {
        appStoreService.createApplicationFile(appId, language, code);
      } catch (IOException e) {
        // loggin
        e.printStackTrace();
      }
    }
    
    return "";
  }
  
  @RequestMapping(value="/execute", method = RequestMethod.POST)
  public @ResponseBody ExecutionResult executeApp(@RequestParam(value="code", required=true) String code, @RequestParam(value="language", required=true) String language, ModelMap model) {
    // invoke executor to execute code, pass the id as parameter
    if ("python".equals(language)) {
      return appExecutor.executePython(null, code);
    } else if ("r".equals(language)) {
      return appExecutor.executeR(null, code);
    } else {
      return new ExecutionResult("Not support", null); 
    }
  }
  
  @RequestMapping(value="/search", method = RequestMethod.GET)
  public @ResponseBody SearchResult search(@RequestParam(value="query", required=true) String query, ModelMap model) {
    SearchResult result = null;
    try {
      result = datastoreService.search(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }
  
}
