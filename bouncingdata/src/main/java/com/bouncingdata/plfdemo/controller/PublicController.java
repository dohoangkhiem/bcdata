package com.bouncingdata.plfdemo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
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
import org.springframework.web.context.request.WebRequest;

import com.bouncingdata.plfdemo.datastore.pojo.dto.Attachment;
import com.bouncingdata.plfdemo.datastore.pojo.dto.DashboardDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.DashboardPosition;
import com.bouncingdata.plfdemo.datastore.pojo.dto.DatasetDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.VisualizationDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.VisualizationType;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.AnalysisDataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;
import com.bouncingdata.plfdemo.service.ApplicationStoreService;
import com.bouncingdata.plfdemo.service.BcDatastoreService;
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
  
  @Autowired
  private BcDatastoreService userDataService;
  
  @RequestMapping(value="/embed/{guid}")
  public String embedAnalysis(@PathVariable String guid, ModelMap model, HttpServletRequest request) throws JsonGenerationException, JsonMappingException, IOException {
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
      
      Map paramMap = request.getParameterMap();
      String mode = paramMap.containsKey("mode")?((String[]) paramMap.get("mode"))[0]:"default";
      List<String> tabList = Arrays.asList(paramMap.containsKey("tab")?(String[]) paramMap.get("tab"):new String[] {"v"});
      
      final List<String> allModes = new ArrayList<String>() {{add("default"); add("tabs"); add("single");}};
      if (!allModes.contains(mode)) {
        model.addAttribute("errorMsg", "Unknown mode");
        return "embed";
      }
      
      model.addAttribute("guid", guid);
      model.addAttribute("anls", anls);
      
      List<String> tabs = new ArrayList<String>();
      if (tabList.contains("v")) {
        tabs.add("v");
      }
      
      if (tabList.contains("c")) {
        tabs.add("c");
      }
        
      if (tabList.contains("d")) {
        tabs.add("d");
      }
      
      if (tabs.size() == 0) {
        tabs.add("v");
        mode = "default";
      } else if (tabs.size() == 1) {
        mode = "single";
      } else {
        mode = "tabs";
      }
        
      model.addAttribute("mode", mode);
      model.addAttribute("tabs", tabs.toArray());
      
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
      
      try {
        List<AnalysisDataset> relations = datastoreService.getAnalysisDatasets(anls.getId());
        if (relations != null) {
          // key: dataset guid, value: dataset name
          Map<String, String> datasetList = new HashMap<String, String>();
          for (AnalysisDataset relation : relations) {
            Dataset ds = relation.getDataset();
            datasetList.put(ds.getGuid(), ds.getName());
          }
          model.addAttribute("datasetList", datasetList);
        }
      } catch (Exception e) {
        logger.debug("Error when trying to get relation datasets", e);
      }
      
      List<Attachment> attachments = appStoreService.getAttachmentData(guid);
      model.addAttribute("attachments", attachments);
      
      String code = StringEscapeUtils.escapeJavaScript(appStoreService.getScriptCode(guid, anls.getLanguage()));
      model.addAttribute("code", code);
      
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
  
  @RequestMapping(value="/data/m/{guids}", method = RequestMethod.GET)
  public @ResponseBody Map<String, DatasetDetail> getDataMap(@PathVariable String guids) {
    Map<String, DatasetDetail> results = new HashMap<String, DatasetDetail>();
    String[] guidArr = guids.split(",");
    for (String guid : guidArr) {
      guid = guid.trim();
      try {
        Dataset ds = datastoreService.getDatasetByGuid(guid);
        if (ds == null) {
          logger.debug("Can't find the dataset {}", guid);
          continue;
        }
        String data = null;
        String[] columns = null;
        if (ds.getRowCount() < 500) {
          data = userDataService.getDatasetToString(ds.getName());
        } else {
          Map row = userDataService.getDatasetToList(ds.getName(), 0, 1).get(0);
          columns = new String[row.keySet().size()];
          int i = 0;
          for (Object s : row.keySet()) {
            columns[i++] = (String) s;
          }
          
        }
        DatasetDetail detail = new DatasetDetail(guid, ds.getName(), ds.getRowCount(), columns, data);
        //DatasetDetail detail = new DatasetDetail(guid, ds.getName());
        results.put(ds.getGuid(), detail);
      } catch (Exception e) {
        logger.debug("Exception occurs when retrieving dataset " + guid, e);
      } 
    }        
    return results;
  }
  
  @RequestMapping(value="/data/ajax/{guid}", method = RequestMethod.GET)
  public @ResponseBody Map<String, Object> loadDatatable(@PathVariable String guid, WebRequest request) {
    try {
      Dataset ds = datastoreService.getDatasetByGuid(guid);
      if (ds == null) {
        logger.debug("Can't find the dataset {}", guid);
        return null;
      }
    
      Map<String, String[]> params = request.getParameterMap();
      int displayStart = Integer.valueOf(params.get("iDisplayStart")[0]);
      int displayLength = Integer.valueOf(params.get("iDisplayLength")[0]);
      int sEcho = Integer.valueOf(params.get("sEcho")[0]);
      
      Map<String, Object> result = new HashMap<String, Object>();
      result.put("sEcho", sEcho);
      
      List<Map> data = userDataService.getDatasetToList(ds.getName(), displayStart, displayLength);
      int totalRecords = ds.getRowCount();
      result.put("iTotalRecords", totalRecords);
      result.put("iTotalDisplayRecords", totalRecords);
      result.put("aaData", data);
      return result;
    } catch (Exception e) {
      logger.debug("", e);
      return null;
    }
    
  }
}
