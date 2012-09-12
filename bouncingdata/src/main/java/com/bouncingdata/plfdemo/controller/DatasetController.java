package com.bouncingdata.plfdemo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
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
import org.springframework.web.multipart.MultipartFile;

import com.bouncingdata.plfdemo.datastore.pojo.dto.DatasetDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.QueryResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.AnalysisDataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.service.BcDatastoreService;
import com.bouncingdata.plfdemo.service.DatastoreService;
import com.bouncingdata.plfdemo.utils.Utils;

@Controller
@RequestMapping("/dataset")
public class DatasetController {
  
  private Logger logger = LoggerFactory.getLogger(DatasetController.class);
  
  @Autowired
  private DatastoreService datastoreService;
  
  @Autowired
  private BcDatastoreService userDataService;
    
  @SuppressWarnings("rawtypes")
  @RequestMapping(value="/{guid}", method = RequestMethod.GET)
  public @ResponseBody List<Map> getData(@PathVariable String guid) {
    try {
      Dataset ds = datastoreService.getDatasetByGuid(guid);
      if (ds == null) {
        logger.debug("Can't find the dataset {}", guid);
        return null;
      }
      
      return userDataService.getDatasetToList(ds.getName());
    } catch (Exception e) {
      logger.debug("Exception occurs when retrieving dataset " + guid, e);
    }   
    return null;
  }
  
  @RequestMapping(value="/query", method = RequestMethod.POST)
  public @ResponseBody QueryResult queryDataset(@RequestParam(value="guid", required=true) String guid, 
      @RequestParam(value="query", required=true) String query) {
    try {
      Dataset ds = datastoreService.getDatasetByGuid(guid);
      if (ds == null) {
        logger.debug("Can't find the dataset {}", guid);
        return null;
      }
      
      return new QueryResult(userDataService.query(query), 1, "OK");
    } catch (Exception e) {
      logger.debug("Exception occurs when querying dataset " + guid, e);
      return new QueryResult(null, -1, e.getMessage());
    }
  }
  
  @RequestMapping(value="/m/{guids}", method = RequestMethod.GET)
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
        DatasetDetail detail = new DatasetDetail(guid, ds.getName(), userDataService.getDatasetToString(ds.getName()));
        results.put(ds.getGuid(), detail);
      } catch (Exception e) {
        logger.debug("Exception occurs when retrieving dataset " + guid, e);
      } 
    }        
    return results;
  }
  
  @RequestMapping(value="/up", method = RequestMethod.POST)
  public @ResponseBody long processUploadDataset(@RequestParam(value="file", required=true) MultipartFile file, ModelMap model,
      Principal principal) {
    User user = (User) ((Authentication)principal).getPrincipal();
    String filename = file.getOriginalFilename();    
    filename = filename.substring(0, filename.lastIndexOf("."));
    long size = file.getSize();
    if (size <= 0) return -1;
    logger.debug("UPLOAD FILE: Received {} file. Size {}", filename, size);
    try {
      List<String[]> data = Utils.parseExcel(file.getInputStream());
      Dataset ds = new Dataset();
      ds.setUser(user);
      ds.setActive(true);
      ds.setCreateAt(new Date());
      ds.setLastUpdate(new Date());
      ds.setDescription("Uploaded from " + file.getOriginalFilename());
      String dsName = user.getUsername() + "." + filename + "_uploaded"; 
      //Dataset oldDs = datastoreService.getDatasetByName(user.getUsername() + "." + datasetName);
      //if (oldDs != null) tableName = tableName + "_1";
      //ds.setName(tableName);
      ds.setName(dsName);
      ds.setScraper(null);
      ds.setRowCount(data.size() - 1);
      ds.setGuid(Utils.generateGuid());
      
      String[] headers = data.get(0);
      StringBuilder schema = new StringBuilder("CREATE TABLE `");
      schema.append(dsName).append("` (");
      for (String h : headers) {
        schema.append("`").append(h).append("` text,");
      }
      String schemaStr = schema.substring(0, schema.length() - 1) + ")";
      ds.setSchema(schemaStr);
      datastoreService.createDataset(ds);
      
      userDataService.storeData(dsName, headers, data.subList(1, data.size()));
    } catch (Exception e) {
      logger.debug("Exception when trying to import data", e);
      return -1;
    }
    return size;
  }
  
  @SuppressWarnings("rawtypes")
  @RequestMapping(value="/view/{guid}", method = RequestMethod.GET)
  public String viewDataPage(@PathVariable String guid, ModelMap model, Principal principal) {
    try {
      Dataset ds = datastoreService.getDatasetByGuid(guid);
      if (ds == null) {
        logger.debug("Can't find the dataset {}", guid);
        return null;
      }
      List<Map> data = userDataService.getDatasetToList(ds.getName());
      ObjectMapper mapper = new ObjectMapper();
      model.addAttribute("dataset", ds);
      model.addAttribute("data", mapper.writeValueAsString(data));
      
      List<AnalysisDataset> relations = datastoreService.getRelatedAnalysis(ds.getId());
      if (relations != null) {
        List<Analysis> relatedAnls = new ArrayList<Analysis>();
        for (AnalysisDataset ad : relations) {
          if (ad.isActive()) {
            Analysis anls = ad.getAnalysis();
            relatedAnls.add(anls);
          }
        }
        model.addAttribute("relatedAnls", relatedAnls);
      }
      
    } catch (Exception e) {
      logger.debug("", e);
      model.addAttribute("errorMsg", e.getMessage());
    }
    return "datapage";
  }
  
  
  
}
