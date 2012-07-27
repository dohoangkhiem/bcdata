package com.bouncingdata.plfdemo.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bouncingdata.plfdemo.datastore.pojo.DashboardDetail;
import com.bouncingdata.plfdemo.datastore.pojo.DashboardPosition;
import com.bouncingdata.plfdemo.datastore.pojo.VisualizationDetail;
import com.bouncingdata.plfdemo.datastore.pojo.VisualizationType;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dashboard;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;
import com.bouncingdata.plfdemo.service.ApplicationStoreService;
import com.bouncingdata.plfdemo.service.DatastoreService;
import com.bouncingdata.plfdemo.utils.Utils;

@Controller
@RequestMapping("/anls")
public class AnalysisController {
  
  private Logger logger = LoggerFactory.getLogger(AnalysisController.class);
  
  @Autowired
  private DatastoreService datastoreService;
  
  @Autowired
  private ApplicationStoreService appStoreService;
  
  @RequestMapping(method=RequestMethod.GET)
  public String defaultLayout() {
    return "analysis";
  }
  
  @RequestMapping(value="/{guid}", method=RequestMethod.GET)
  public String viewAnalysis(@PathVariable String guid, ModelMap model, Principal principal) {
    logger.debug("Received request for analysis {}", guid);
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
          
      Dashboard d = datastoreService.getDashboard(guid);
      Map<String, DashboardPosition> dashboard = Utils.parseDashboard(d);
      
      DashboardDetail dbDetail = new DashboardDetail(visualsMap, dashboard);
      ObjectMapper mapper = new ObjectMapper();
      model.addAttribute("dashboardDetail", mapper.writeValueAsString(dbDetail));
      
      return "analysis";
    } catch (Exception e) {
      logger.debug("Failed to load analysis {}", guid);
      return null;
    }
  }
}
