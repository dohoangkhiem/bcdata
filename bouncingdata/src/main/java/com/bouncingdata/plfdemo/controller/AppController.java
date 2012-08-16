package com.bouncingdata.plfdemo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.bouncingdata.plfdemo.datastore.pojo.dto.AnalysisDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.DashboardDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.DashboardPosition;
import com.bouncingdata.plfdemo.datastore.pojo.dto.ExecutionResult;
import com.bouncingdata.plfdemo.datastore.pojo.dto.VisualizationDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.VisualizationType;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
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
  
  private Logger logger = LoggerFactory.getLogger(AppController.class);
  
  @Autowired private DatastoreService datastoreService;
  @Autowired private ApplicationExecutor appExecutor;
  @Autowired private ApplicationStoreService appStoreService;
  @Autowired private UserDataService userDataService;
  
  @RequestMapping(value="/a/{guid}", method = RequestMethod.GET)
  public @ResponseBody AnalysisDetail getApplication(@PathVariable String guid) {
    try {
      Analysis anls = datastoreService.getAnalysisByGuid(guid);
      if (anls == null) return null;
      
      List<Visualization> visuals = datastoreService.getAnalysisVisualization(anls.getId());
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
      Map<String, String> datasets = userDataService.getApplicationDataset(anls.getId());
      
      Map<String, DashboardPosition> dashboard = Utils.parseDashboard(anls);
      
      AnalysisDetail detail = new AnalysisDetail(code, datasets, visualsMap, dashboard);
      return detail;
    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
  }
  
  @RequestMapping(value="/v/{guid}", method = RequestMethod.GET)
  public @ResponseBody DashboardDetail getVisualizationMap(@PathVariable String guid) {
    try {
      Analysis anls = datastoreService.getAnalysisByGuid(guid);
      if (anls == null) return null;
    
      List<Visualization> visuals = datastoreService.getAnalysisVisualization(anls.getId());
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
      
      Map<String, DashboardPosition> dashboard = Utils.parseDashboard(anls);
      
      return new DashboardDetail(visualsMap, dashboard);
    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
  }
  
  @RequestMapping(value="/v/e/{guid}", method = RequestMethod.GET)
  public @ResponseBody DashboardDetail updateDashboardForExecution(@PathVariable String guid) {
    try {
      Analysis anls = datastoreService.getAnalysisByGuid(guid);
      if (anls == null) return null;
    
      List<Visualization> visuals = datastoreService.getAnalysisVisualization(anls.getId());
      Map<String, VisualizationDetail> visualsMap = null;
      Map<String, String> visualNames = null;
      if (visuals != null) {
        visualsMap = new HashMap<String, VisualizationDetail>();
        visualNames = new HashMap<String, String>();
        for (Visualization v : visuals) {
          visualNames.put(v.getGuid(), v.getName());
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
      
      Map<String, DashboardPosition> dashboard = Utils.parseDashboard(anls);
      Map<String, DashboardPosition> dashboardPos = new HashMap<String, DashboardPosition>();
      
      for (DashboardPosition dp : dashboard.values()) {
        dashboardPos.put(visualNames.get(dp.getGuid()), dp);
      }
      
      return new DashboardDetail(visualsMap, dashboardPos);
    } catch (Exception e) {
      logger.error("", e);
      return null;
    }
  }
    
  @RequestMapping(value="/e/{appGuid}", method = RequestMethod.POST)
  public @ResponseBody ExecutionResult executeApp(@PathVariable String appGuid, @RequestParam(value="code", required=true) String code, ModelMap model, Principal principal) {
    User user = (User) ((Authentication)principal).getPrincipal();
    if (user == null) return new ExecutionResult(null, null, null, -1, "User not found.");
    try {
      Analysis anls = datastoreService.getAnalysisByGuid(appGuid);
      if (anls == null) return new ExecutionResult(null, null, null, -1, "Application not found.");
      
      if (anls.getUser().getId() != user.getId()) {
        return new ExecutionResult(null, null, null, -1, "No permission to run this app.");
      }
      
      try {
        appStoreService.saveApplicationCode(appGuid, anls.getLanguage(), code);
      } catch (IOException e) {
        logger.error("", e);
      }
      
      datastoreService.executeAnalysis(user, anls);
      
      if ("python".equals(anls.getLanguage())) {
        return appExecutor.executePython(anls, code, user);
      } else if ("r".equals(anls.getLanguage())) {
        return appExecutor.executeR(anls, code, user);
      } else {
        return new ExecutionResult(null, null, null, -1, "Not supported language.");
      }
      
    } catch (Exception e) {
      logger.error("", e);
      return new ExecutionResult(null, null, null, -3, "Unknown error");
    }
    
  }
  
  @RequestMapping(value="/s/{appGuid}", method = RequestMethod.POST)
  public @ResponseBody String saveApp(@PathVariable String appGuid, @RequestParam(value="code", required=true) String code, 
      @RequestParam(value="language", required=false) String language, ModelMap model, Principal principal) {
    
    User user = (User) ((Authentication)principal).getPrincipal();
    if (user == null) return null;
    
    try {
      Analysis anls = datastoreService.getAnalysisByGuid(appGuid);
      if (anls == null) {
        return "Cannot find application";
      }
      
      if (anls.getUser().getId() != user.getId()) {
        return "No permission";
      }
      
      try {
        appStoreService.saveApplicationCode(appGuid, anls.getLanguage(), code);
        //return "OK";
      } catch (IOException e) {
        e.printStackTrace();
      }
      
      int lines = Utils.countLines(code);
      anls.setLineCount(lines);
      datastoreService.updateAnalysis(anls);
      
    } catch (Exception e) {
      logger.error("", e);
    }
    
    return "OK";
  }
  
  @RequestMapping(value="/v/d/update/{guid}", method = RequestMethod.POST)
  public @ResponseBody String updateDashboard(@PathVariable String guid, @RequestParam(value="status", required = true) String status, Principal principal) {
    logger.debug("Receive update dashboard request {}, {}", guid, status);
    User user = (User) ((Authentication)principal).getPrincipal();
    if (user == null) return "KO";
    try {
      Analysis app = datastoreService.getAnalysisByGuid(guid);
      if (app == null) return "KO";
      
      if (app.getUser().getId() != user.getId()) {
        return "KO";
      }
      
      datastoreService.updateDashboard(guid, status);
    } catch (Exception e) {
      logger.error("", e);
      return "KO";
    }
    return "OK";
  }
  
  @RequestMapping(value="/v/d/create/{guid}/{status}", method = RequestMethod.GET)
  public @ResponseBody void createDashboard(@PathVariable String guid, @PathVariable String status) {
    logger.debug("Receive create dashboard request {}, {}", guid, status);
    try {
      //datastoreService.createDashboard(guid, status);
      datastoreService.updateDashboard(guid, status);
    } catch (Exception e) {
      logger.error("", e);
    }
  }
  
  @RequestMapping(value="/a/publish/{guid}", method=RequestMethod.POST)
  public @ResponseBody void publishAnalysis(@RequestParam(value="value", required=true) boolean value, @PathVariable String guid, ModelMap model, Principal principal) {
    logger.debug("Publishing analysis with guid {}", guid);
    User user = (User) ((Authentication)principal).getPrincipal();
    if (user == null) return;
    try {
      Analysis analysis = datastoreService.getAnalysisByGuid(guid);
      if (analysis == null) return;
      if (analysis.isPublished() == value) {
        return;
      }
      if (user.getId() != analysis.getUser().getId()) {
        return;
      }
      datastoreService.publishAnalysis(user, analysis, value);
    } catch (Exception e) {
      logger.error("", e);
    }
  }
  
}
