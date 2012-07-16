package com.bouncingdata.plfdemo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bouncingdata.plfdemo.datastore.pojo.ApplicationDetail;
import com.bouncingdata.plfdemo.datastore.pojo.DashboardDetail;
import com.bouncingdata.plfdemo.datastore.pojo.ExecutionResult;
import com.bouncingdata.plfdemo.datastore.pojo.VisualizationDetail;
import com.bouncingdata.plfdemo.datastore.pojo.VisualizationType;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dashboard;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;
import com.bouncingdata.plfdemo.service.ApplicationExecutor;
import com.bouncingdata.plfdemo.service.ApplicationStoreService;
import com.bouncingdata.plfdemo.service.DatastoreService;
import com.bouncingdata.plfdemo.service.UserDataService;
import com.bouncingdata.plfdemo.utils.Utils;

@Controller
@RequestMapping("/app")
public class AppController {
  
  private DatastoreService datastoreService;
  private ApplicationExecutor appExecutor;
  private ApplicationStoreService appStoreService;
  @Autowired private UserDataService userDataService;
  
  public void setDatastoreService(DatastoreService dsService) {
    this.datastoreService = dsService;
  }
  
  public void setAppExecutor(ApplicationExecutor appExecutor) {
    this.appExecutor = appExecutor;
  }
  
  public void setAppStoreService(ApplicationStoreService appStoreService) {
    this.appStoreService = appStoreService;
  }
  
  public void setUserDataService(UserDataService userDataService) {
    this.userDataService = userDataService;
  }
  
  @RequestMapping(value="/a/{guid}", method = RequestMethod.GET)
  public @ResponseBody ApplicationDetail getApplication(@PathVariable String guid) {
    try {
      Application app = datastoreService.getApplication(guid);
      if (app == null) return null;
      
      List<Visualization> visuals = datastoreService.getApplicationVisualization(app.getId());
      Map<String, VisualizationDetail> visualsMap = null;
      if (visuals != null) {
        visualsMap = new HashMap<String, VisualizationDetail>();
        for (Visualization v : visuals) {
          if ("html".equals(v.getType())) {
            visualsMap.put(v.getName(), new VisualizationDetail(v.getGuid(), "visualize/app/" + guid + "/" + v.getGuid() + "/html", VisualizationType.getVisualType(v.getType())));
          } else if ("png".equals(v.getType())) {
            try {
              String source = appStoreService.getVisualization(guid, v.getGuid(), v.getType());
              visualsMap.put(v.getName(), new VisualizationDetail(v.getGuid(), source, VisualizationType.getVisualType(v.getType())));
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }
      
      String code = appStoreService.getApplicationCode(guid, null);
      Map<String, String> datasets = userDataService.getApplicationDataset(app.getId());
      
      Dashboard d = datastoreService.getDashboard(guid);
      Map<String, DashboardDetail> dashboard = Utils.parseDashboard(d);
      
      ApplicationDetail detail = new ApplicationDetail(code, datasets, visualsMap, dashboard);
      return detail;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  @RequestMapping(value="/v/{guid}", method = RequestMethod.GET)
  public @ResponseBody Map<String, VisualizationDetail> getVisualizationMap(@PathVariable String guid) {
    try {
      Application app = datastoreService.getApplication(guid);
      if (app == null) return null;
    
      List<Visualization> visuals = datastoreService.getApplicationVisualization(app.getId());
      Map<String, VisualizationDetail> visualsMap = null;
      if (visuals != null) {
        visualsMap = new HashMap<String, VisualizationDetail>();
        for (Visualization v : visuals) {
          if ("html".equals(v.getType())) {
            visualsMap.put(v.getName(), new VisualizationDetail(v.getGuid(), "visualize/app/" + guid + "/" + v.getGuid() + "/html", VisualizationType.getVisualType(v.getType())));
          } else if ("png".equals(v.getType())) {
            try {
              String source = appStoreService.getVisualization(guid, v.getGuid(), v.getType());
              visualsMap.put(v.getName(), new VisualizationDetail(v.getGuid(), source, VisualizationType.getVisualType(v.getType())) );
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }
      return visualsMap;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
    
  @RequestMapping(value="/e/{appGuid}", method = RequestMethod.POST)
  public @ResponseBody ExecutionResult executeApp(@PathVariable String appGuid, @RequestParam(value="code", required=true) String code, ModelMap model, Principal principal) {
    User user = (User) ((Authentication)principal).getPrincipal();
    if (user == null) return new ExecutionResult(null, null, null, -1, "User not found.");
    try {
      Application app = datastoreService.getApplication(appGuid);
      if (app == null) return new ExecutionResult(null, null, null, -1, "Application not found.");
      
      if (app.getAuthor() != user.getId()) {
        return new ExecutionResult(null, null, null, -1, "No permission to run this app.");
      }
      
      try {
        appStoreService.saveApplicationCode(appGuid, app.getLanguage(), code);
      } catch (IOException e) {
        e.printStackTrace();
      }
      
      if ("python".equals(app.getLanguage())) {
        return appExecutor.executePython(app, code, user);
      } else if ("r".equals(app.getLanguage())) {
        return appExecutor.executeR(app, code, user);
      } else {
        return new ExecutionResult(null, null, null, -1, "Not supported language.");
      }
      
    } catch (Exception e) {
      e.printStackTrace();
      return new ExecutionResult(null, null, null, -3, "Unknown error");
    }
    
  }
  
  @RequestMapping(value="/s/{appGuid}", method = RequestMethod.POST)
  public @ResponseBody String saveApp(@PathVariable String appGuid, @RequestParam(value="code", required=true) String code, 
      @RequestParam(value="language", required=false) String language, ModelMap model, Principal principal) {
    
    User user = (User) ((Authentication)principal).getPrincipal();
    if (user == null) return null;
    
    try {
      Application app = datastoreService.getApplication(appGuid);
      if (app == null) {
        return "Cannot find application";
      }
      
      if (app.getAuthor() != user.getId()) {
        return "No permission";
      }
      
      try {
        appStoreService.saveApplicationCode(appGuid, app.getLanguage(), code);
        return "OK";
      } catch (IOException e) {
        e.printStackTrace();
      }
      
      int lines = Utils.countLines(code);
      app.setLineCount(lines);
      datastoreService.updateApplication(app);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  @RequestMapping(value="/v/d/update/{guid}", method = RequestMethod.POST)
  public @ResponseBody String updateDashboard(@PathVariable String guid, @RequestParam(value="status", required = true) String status, Principal principal) {
    System.out.format("Receive update dashboard request %s, %s%n", guid, status);
    User user = (User) ((Authentication)principal).getPrincipal();
    if (user == null) return "KO";
    try {
      Application app = datastoreService.getApplication(guid);
      if (app == null) return "KO";
      
      if (app.getAuthor() != user.getId()) {
        return "KO";
      }
      
      datastoreService.updateDashboard(guid, status);
    } catch (Exception e) {
      e.printStackTrace();
      return "KO";
    }
    return "OK";
  }
  
  @RequestMapping(value="/v/d/create/{guid}/{status}", method = RequestMethod.GET)
  public void createDashboard(@PathVariable String guid, @PathVariable String status) {
    System.out.format("Receive create dashboard request %s, %s%n", guid, status);
    try {
      datastoreService.createDashboard(guid, status);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
}
