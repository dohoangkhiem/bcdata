package com.bouncingdata.plfdemo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.BcDataScript;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.service.ApplicationStoreService;
import com.bouncingdata.plfdemo.service.BcDatastoreService;
import com.bouncingdata.plfdemo.service.DatastoreService;
import com.bouncingdata.plfdemo.util.Utils;

/**
 * This controller is about to handle all operations from client library.
 * It exposes REST for retrieve analysis info, code, dataset and also allow to upload analysis 
 *
 */
@Controller
@RequestMapping(value="/client")
public class ClientController {
  
  private Logger logger = LoggerFactory.getLogger(ClientController.class);
  
  @Autowired
  private DatastoreService datastoreService;
  
  @Autowired
  private ApplicationStoreService appStoreService;
  
  @Autowired
  private BcDatastoreService userDataService;
  
  /**
   * Test client connection and authentication
   * @param principal
   * @return
   */
  @RequestMapping(value="/test")
  public @ResponseBody String test(Principal principal) {
    StringBuilder response = new StringBuilder("Your request has been authenticated.");
    User user = (User) ((Authentication)principal).getPrincipal();
    if (user == null) {
      response.append("ERROR: Cannot find authenticated user.");
    }
    return response.toString();
  }
  
  /**
   * Gets list of analyses, datasets of given user
   * @throws IOException 
   * @throws Exception 
   */
  @RequestMapping(value="/list", method=RequestMethod.GET)
  public @ResponseBody Map getList(@RequestParam(value="type", required=false) String type, Principal principal, HttpServletResponse res) throws IOException {
    User user = (User) ((Authentication)principal).getPrincipal();
    if (user == null) {
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized request.");
      return null;
    }
    
    String[] types = new String[] {"all", "analysis", "dataset"};
    if (type == null) type = "analysis";
    type = type.toLowerCase();
    if (type != null && !Arrays.asList(types).contains(type)) {
      res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown type.");
      return null;
    }
    
    Map<String, Object> results = new LinkedHashMap<String, Object>();
    if (type.equals("all") || type.equals("analysis")) {
      try {
        results.put("analyses", datastoreService.getAnalysisList(user.getId()));
      } catch (Exception e) {
        results.put("analyses", null);
      }
    }
    if (type.equals("all") || type.equals("dataset")) {
      try {
        results.put("datasets", datastoreService.getDatasetList(user.getId()));
      } catch (Exception e) {
        results.put("datasets", null);
      }
    }
    
    return results;
  }
    
  @RequestMapping(value="/anls/info/{guid}", method=RequestMethod.GET)
  public @ResponseBody Analysis getAnalysisInfo(@PathVariable String guid, Principal principal) throws Exception {
    Analysis anls = datastoreService.getAnalysisByGuid(guid);
    return anls;
  }
  
  @RequestMapping(value="/anls/getsource/{guid}", method=RequestMethod.GET)
  public @ResponseBody String getAnalysisSource(@PathVariable String guid, ModelMap model, Principal principal, HttpServletResponse res) throws IOException {
    // to return to client: script code
    User user = (User) ((Authentication)principal).getPrincipal();
    
    // check user
    if (user == null) {
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not authorized!");
      return null;
    }
    
    if (guid == null) {
      res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad guid!");
    }
    
    Analysis anls = null;
    try {
      anls = datastoreService.getAnalysisByGuid(guid);
    } catch (Exception e) {
      return null;
    }
    
    if (anls == null) return null;
    
    String code =  appStoreService.getScriptCode(guid, null);
    return code;
  }
  
  @RequestMapping(value="/anls/up", method = RequestMethod.POST)
  public @ResponseBody String uploadAnalysis(@RequestParam(value="code", required=true) String code, 
      @RequestParam(value="name", required=true) String name, 
      @RequestParam(value="description", required=false) String description,
      @RequestParam(value="type", required=false) String type, 
      HttpServletResponse res, ModelMap model, Principal principal) throws IOException {
    
    User user = (User) ((Authentication)principal).getPrincipal();
    
    // check user
    if (user == null) {
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized user!");
      return null;
    }
        
    if (code == null || code.isEmpty()) {
      res.sendError(HttpServletResponse.SC_NO_CONTENT, "No content!");
      return null;
    }
    
    BcDataScript script = new Analysis();
    script.setName(name);
    script.setUser(user);
    script.setLanguage("r");
    script.setDescription(description);
    script.setLineCount(Utils.countLines(code));
    Date date = Utils.getCurrentDate();
    script.setCreateAt(date);
    script.setLastUpdate(date);
    script.setUser(user);
    script.setExecuted(false);
    script.setCreateSource("client");
     
    String guid = null;
    try { 
      guid = datastoreService.createBcDataScript(script, type);
    } catch (Exception e) {
      logger.debug("Failed to create analysis " + name + " for user " + user.getUsername(), e);
      logger.debug("", e);
    }
    
    if (guid == null) {
      res.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Can't create new analysis.");
      return null;
    }
    
    // store application code
    try {
      appStoreService.createApplicationFile(guid, "r", code);
    } catch (Exception e) {
      logger.error("Error occurs when save application code, guid {}", guid);
    }
    
    return guid;
    
  }
  
  @RequestMapping(value="/data/info/{guid}", method=RequestMethod.GET)
  public @ResponseBody Dataset getDatasetInfo(@PathVariable String guid, Principal principal) throws Exception {
    Dataset ds = datastoreService.getDatasetByGuid(guid);
    return ds;    
  }
  
  @RequestMapping(value="/data/get/{guid}", method=RequestMethod.GET)
  public @ResponseBody Object getDataset(@PathVariable String guid, Principal principal) throws Exception {
    Dataset ds = datastoreService.getDatasetByGuid(guid);
    return userDataService.getDatasetToList(ds.getName());    
  }
  
  @RequestMapping(value="/data/up", method=RequestMethod.POST)
  public @ResponseBody void uploadData(Principal principal) throws Exception {
    return;
  }
}

