package com.bouncingdata.plfdemo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bouncingdata.plfdemo.datastore.pojo.dto.DashboardDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.DashboardPosition;
import com.bouncingdata.plfdemo.datastore.pojo.dto.VisualizationDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.VisualizationType;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;
import com.bouncingdata.plfdemo.service.ApplicationStoreService;
import com.bouncingdata.plfdemo.service.DatastoreService;
import com.bouncingdata.plfdemo.util.Utils;

@Controller
@RequestMapping(value = "/public")
public class PublicController {
  
  private Logger logger = LoggerFactory.getLogger(PublicController.class);
  
  @Autowired
  DatastoreService datastoreService;
  
  @Autowired
  private ApplicationStoreService appStoreService;
  
  @RequestMapping(value="/embed/{guid}")
  public String embedAnalysis(@PathVariable String guid, ModelMap model) throws JsonGenerationException, JsonMappingException, IOException {
    try {
      Analysis anls = null;
      anls = datastoreService.getAnalysisByGuid(guid);
            
      if (anls == null) {
        model.addAttribute("errorMsg", "Analysis " + guid + " not found!"); 
        return "embed";
      }
      
      /*if (!anls.isPublished() && !anls.getUser().getUsername().equals(user.getUsername())) {
        model.addAttribute("errorMsg", "You have no permission to view this analysis.");
        return "embed";
      }*/
      
      if (!anls.isPublished()) {
        model.addAttribute("errorMsg", "This analysis was set to private.");
        return "embed";
      }
      
      List<Visualization> visuals = null;
      try {
        visuals = datastoreService.getAnalysisVisualizations(anls.getId());
      } catch (Exception e) {
        if (logger.isDebugEnabled()) {
          logger.debug("Failed to get visualization from analysis {}", guid);
        }
        model.addAttribute("errorMsg", "Failed to get analysis visualizations.");
        return "embed";
      }
      
      Map<String, VisualizationDetail> visualsMap = null;
      if (visuals != null) {
        visualsMap = new HashMap<String, VisualizationDetail>();
        for (Visualization v : visuals) {
          if ("html".equals(v.getType())) {
            visualsMap.put(v.getName(), new VisualizationDetail(v.getGuid(), "public/app/" + guid + "/" + v.getGuid() + "/html", VisualizationType.getVisualType(v.getType())));
          } else if ("png".equals(v.getType())) {
            try {
              String source = appStoreService.getVisualization(guid, v.getGuid(), v.getType());
              visualsMap.put(v.getName(), new VisualizationDetail(v.getGuid(), source, VisualizationType.getVisualType(v.getType())));
            } catch (Exception e) {
              if (logger.isDebugEnabled()) {
                logger.debug("Error occurs when retrieving visualizations {} from analysis {}", v.getGuid(), guid);
                logger.debug("Exception detail", e);
              }
              continue;
            }
          }
        }
      }
  
      Map<String, DashboardPosition> dashboard = Utils.parseDashboard(anls);
  
      DashboardDetail dbDetail = new DashboardDetail(visualsMap, dashboard);
      ObjectMapper mapper = new ObjectMapper();
      model.addAttribute("dashboardDetail", mapper.writeValueAsString(dbDetail));
    } catch (Exception e) {
      model.addAttribute("errorMsg", "Failed to load analysis: Unknown error.");
    }
    return "embed";
  }
  
  @RequestMapping(value="/source/{guid}", method = RequestMethod.GET)
  public @ResponseBody String getSourceCode(@PathVariable String guid) {
    Analysis anls = null;
    try {
      anls = datastoreService.getAnalysisByGuid(guid);
    } catch (Exception e) {
      return "Analysis not found!";
    }
    
    if (anls == null) {
      return "Analysis not found!";
    }
    
    try {
      return appStoreService.getScriptCode(anls.getGuid(), anls.getLanguage());
    } catch (Exception e) {
      logger.debug("Failed to retrive source code for analysis " + anls.getGuid(), e);
      return "Failed to retrieve analysis source code. This file maybe corrupted or deleted.";
    }
  }
}
