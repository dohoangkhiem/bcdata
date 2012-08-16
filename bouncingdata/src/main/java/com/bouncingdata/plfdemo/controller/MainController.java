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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bouncingdata.plfdemo.datastore.pojo.dto.ExecutionResult;
import com.bouncingdata.plfdemo.datastore.pojo.dto.SearchResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.service.ApplicationExecutor;
import com.bouncingdata.plfdemo.service.ApplicationStoreService;
import com.bouncingdata.plfdemo.service.DatastoreService;
import com.bouncingdata.plfdemo.utils.Utils;

@Controller
@RequestMapping(value = "/main")
public class MainController {
  
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Autowired
  private DatastoreService datastoreService;
  
  @Autowired
  private ApplicationStoreService appStoreService;
  
  @Autowired
  private ApplicationExecutor appExecutor;
      
  @RequestMapping(method = RequestMethod.GET)
  public String main(ModelMap model, Principal principal) {
    String username = principal.getName();
    model.addAttribute("username", username);
    return "create";
  }
  
  @RequestMapping(value = "/dataset", method = RequestMethod.GET)
  public @ResponseBody List<Dataset> getDatasets(ModelMap model, Principal principal) {
    User user = (User) ((Authentication) principal).getPrincipal();
    try {
      if (user == null) return null;
      int userId = user.getId();
      return datastoreService.getDatasetList(userId);
    } catch (Exception e) {
      logger.error("Failed to retrieve dataset list for user " + user.getUsername(), e);
      return null;
    }
    
  }
  
  @RequestMapping(value="/table/{datastore}")
  public @ResponseBody List<Dataset> getTables(@PathVariable String datastore) {
    return null;
  }
  
  @RequestMapping(value="/application", method = RequestMethod.GET)
  @ResponseBody
  public List<Analysis> getApplications(ModelMap model, Principal principal) {
    User user = (User) ((Authentication) principal).getPrincipal();
    try {
      if (user == null) return null;
      int userId = user.getId();
      return datastoreService.getAnalysisList(userId);
    } catch (Exception e) {
      logger.error("Failed to retrieve application list for user " + user.getUsername(), e);
      return null;
    }
  }
  
  @RequestMapping(value = "/createapp", method = RequestMethod.POST)
  @ResponseBody
  public String createApplication(@RequestParam(value = "appname", required = true) String appname,
      @RequestParam(value = "language", required = true) String language,
      @RequestParam(value = "description", required = true) String description,
      @RequestParam(value = "code", required = true) String code, 
      @RequestParam(value = "isPublic", required = true) int isPublic,
      @RequestParam(value = "tags", required = false) String tags, ModelMap model, Principal principal) {
    User user = (User) ((Authentication) principal).getPrincipal();
    if (user == null) {
      logger.debug("Can't get the user. Skip application creation.");
      return null;
    }
    int userId = user.getId();
    String guid = null;
    try {
      guid = datastoreService.createAnalysis(appname, description, language, userId, user.getUsername(), Utils.countLines(code), (isPublic>0), tags);
    } catch (Exception e) {
      logger.error("Failed to create application " + appname + " for user " + user.getUsername(), e);
    }
    
    if (guid == null) {
      logger.error("Can't get the guid of application {} so the code cannot saved", appname);
      return null;
    }
    
    // store application code
    try {
      appStoreService.createApplicationFile(guid, language, code);
    } catch (Exception e) {
      logger.error("Error occurs when save application code, guid {}", guid);
    }
   
    return guid;
  }
  
  @RequestMapping(value="/execute", method = RequestMethod.POST)
  public @ResponseBody ExecutionResult executeApp(@RequestParam(value="code", required=true) String code, @RequestParam(value="language", required=true) String language, ModelMap model, Principal principal) {
    // invoke executor to execute code, pass the id as parameter
    User user = (User) ((Authentication)principal).getPrincipal();
    if (user == null) return new ExecutionResult(null, null, null, -1, "User not found.");
    if ("python".equals(language)) {
      return appExecutor.executePython(null, code, user);
    } else if ("r".equals(language)) {
      return appExecutor.executeR(null, code, user);
    } else {
      return new ExecutionResult("Not support", null, null, -1, "Not support"); 
    }
  }
  
  @RequestMapping(value="/search", method = RequestMethod.GET)
  public @ResponseBody SearchResult search(@RequestParam(value="query", required=true) String query, ModelMap model) {
    SearchResult result = null;
    try {
      result = datastoreService.search(query);
    } catch (Exception e) {
      logger.error("Failed to execute search with query: " + query, e);
    }
    return result;
  }
  
}
