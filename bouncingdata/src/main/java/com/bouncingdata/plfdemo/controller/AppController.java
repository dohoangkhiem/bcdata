package com.bouncingdata.plfdemo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
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
import com.bouncingdata.plfdemo.datastore.pojo.dto.Attachment;
import com.bouncingdata.plfdemo.datastore.pojo.dto.DashboardDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.DashboardPosition;
import com.bouncingdata.plfdemo.datastore.pojo.dto.ExecutionResult;
import com.bouncingdata.plfdemo.datastore.pojo.dto.ScraperDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.VisualizationDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.VisualizationType;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.AnalysisDataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.BcDataScript;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Scraper;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;
import com.bouncingdata.plfdemo.service.ApplicationExecutor;
import com.bouncingdata.plfdemo.service.ApplicationStoreService;
import com.bouncingdata.plfdemo.service.DatastoreService;
import com.bouncingdata.plfdemo.util.ScriptType;
import com.bouncingdata.plfdemo.util.Utils;

@Controller
@RequestMapping("/app")
public class AppController {
  
  private Logger logger = LoggerFactory.getLogger(AppController.class);
  
  @Autowired private DatastoreService datastoreService;
  @Autowired private ApplicationExecutor appExecutor;
  @Autowired private ApplicationStoreService appStoreService;
  //@Autowired private BcDatastoreService bcDatastoreService;
  
  @RequestMapping(value="/a/{guid}", method = RequestMethod.GET)
  public @ResponseBody AnalysisDetail getApplication(@PathVariable String guid) {
    try {
      Analysis anls = datastoreService.getAnalysisByGuid(guid);
      if (anls == null) return null;
      
      List<Visualization> visuals = datastoreService.getAnalysisVisualizations(anls.getId());
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
      
      Map<String, DashboardPosition> dashboard = Utils.parseDashboard(anls);
      
      String code = appStoreService.getScriptCode(guid, null);
      List<AnalysisDataset> relations = datastoreService.getAnalysisDatasets(anls.getId());
      List<Dataset> datasets = null;
      if (relations != null && relations.size() > 0) {
        datasets = new ArrayList<Dataset>();
        for (AnalysisDataset anlsDts : relations) {
          datasets.add(anlsDts.getDataset());
        }
      }
      
      List<Attachment> attachments = appStoreService.getAttachmentData(guid);
      
      AnalysisDetail detail = new AnalysisDetail(code, datasets, attachments, visualsMap, dashboard);
      return detail;
    } catch (Exception e) {
      logger.error("Failed to get application", e);
      return null;
    }
  }
  
  @RequestMapping(value="/v/{guid}", method = RequestMethod.GET)
  public @ResponseBody DashboardDetail getVisualizationMap(@PathVariable String guid) {
    try {
      Analysis anls = datastoreService.getAnalysisByGuid(guid);
      if (anls == null) return null;
    
      List<Visualization> visuals = datastoreService.getAnalysisVisualizations(anls.getId());
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
      logger.error("Failed to get visualization map", e);
      return null;
    }
  }
  
  @RequestMapping(value="/v/e/{guid}", method = RequestMethod.GET)
  public @ResponseBody DashboardDetail updateDashboardForExecution(@PathVariable String guid) {
    try {
      Analysis anls = datastoreService.getAnalysisByGuid(guid);
      if (anls == null) return null;
    
      List<Visualization> visuals = datastoreService.getAnalysisVisualizations(anls.getId());
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
  public @ResponseBody ExecutionResult executeApp(@PathVariable String appGuid, @RequestParam(value="code", required=true) String code,
      @RequestParam(value="type", required=true) String type, ModelMap model, Principal principal) {
    User user = (User) ((Authentication)principal).getPrincipal();
    if (user == null) return new ExecutionResult(null, null, null, -1, "User not found.");
    BcDataScript script = null;
    try {
      if (ScriptType.SCRAPER.getType().equals(type)) {
        script = datastoreService.getScraperByGuid(appGuid);
      } else if (ScriptType.ANALYSIS.getType().equals(type)) {
        script = datastoreService.getAnalysisByGuid(appGuid);
      }
      
      if (script == null) return new ExecutionResult(null, null, null, -1, "Application not found.");
      
      if (script.getUser().getId() != user.getId()) {
        return new ExecutionResult(null, null, null, -1, "No permission to run this app.");
      }
      
      try {
        appStoreService.saveApplicationCode(appGuid, script.getLanguage(), code);
      } catch (IOException e) {
        logger.error("", e);
      }
      
      // if analysis is public and this is the first execution
      if (!script.isExecuted() && script.isPublished() && script instanceof Analysis) {
        script.setExecuted(true);
        datastoreService.updateBcDataScript(script);
        datastoreService.doPublishAction(user, script);
      }
      
      //datastoreService.doExecuteAction(user, script);
      
      if ("python".equals(script.getLanguage())) {
        return appExecutor.executePython(script, code, user);
      } else if ("r".equals(script.getLanguage())) {
        return appExecutor.executeR(script, code, user);
      } else {
        return new ExecutionResult(null, null, null, -1, "Not supported language.");
      }
      
    } catch (Exception e) {
      logger.error("Execution error", e);
      return new ExecutionResult(null, null, null, -3, "Unknown error");
    }
    
  }
  
  @RequestMapping(value="/s/{guid}", method = RequestMethod.POST)
  public @ResponseBody String saveApp(@PathVariable String guid, @RequestParam(value="code", required=true) String code, 
      @RequestParam(value="type", required=true) String type, @RequestParam(value="language", required=false) String language, 
      ModelMap model, Principal principal) {
    
    User user = (User) ((Authentication)principal).getPrincipal();
    if (user == null) return null;
    
    try {
      BcDataScript script;
      if ("analysis".equals(type)) {
        script = datastoreService.getAnalysisByGuid(guid);
      } else if ("scraper".equals(type)) {
        script = datastoreService.getScraperByGuid(guid);
      } else {
        return "Unknown application type.";
      }
      if (script == null) {
        return "Cannot find application";
      }
      
      if (script.getUser().getId() != user.getId()) {
        return "No permission";
      }
      
      appStoreService.saveApplicationCode(guid, script.getLanguage(), code);
      
      int lines = Utils.countLines(code);
      script.setLineCount(lines);
      datastoreService.updateBcDataScript(script);
      
    } catch (Exception e) {
      logger.error("Failed to update application.", e);
    }
    
    return "OK";
  }
  
  @RequestMapping(value="/v/d/update/{guid}", method = RequestMethod.POST)
  public @ResponseBody String updateDashboard(@PathVariable String guid, @RequestParam(value="status", required = true) String status, 
      @RequestParam(value="cause", required=false) String cause, Principal principal) {
    logger.debug("Receive update dashboard request {}, {}", guid, status);
    User user = (User) ((Authentication)principal).getPrincipal();
    if (user == null) return "KO";
    try {
      Analysis app = datastoreService.getAnalysisByGuid(guid);
      if (app == null) return "KO";
      
      if (app.getUser().getId() != user.getId()) {
        return "KO";
      }
      
      if (app.isPublished() && "execute".equals(cause)) {
        // create activity
        datastoreService.doUpdateAction(user, app);
      }
      datastoreService.updateDashboard(guid, status);
    } catch (Exception e) {
      logger.error("Failed to update dashboard.", e);
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
      logger.error("Failed to create dashboard.", e);
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
      logger.error("Failed to publish analysis", e);
    }
  }
  
  /**
   * 
   */
  @RequestMapping(value="/scr/{guid}", method=RequestMethod.GET)
  public @ResponseBody ScraperDetail getScraper(@PathVariable String guid) {
    try {
      Scraper scr = datastoreService.getScraperByGuid(guid);
      if (scr == null) return null;
      
      // get scraper's code
      String code = appStoreService.getScriptCode(guid, null);
      
      // get scraper datasets 
      List<Dataset> dsList = datastoreService.getScraperDataset(scr.getId());
      return new ScraperDetail(code, scr, dsList);
      
    } catch (Exception e) {
      logger.error("Failed to get scraper", e);
      return null;
    }
  }
  
}
