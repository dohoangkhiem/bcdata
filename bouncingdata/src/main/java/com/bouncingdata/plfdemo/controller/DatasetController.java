package com.bouncingdata.plfdemo.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bouncingdata.plfdemo.datastore.pojo.dto.QueryResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.service.DatastoreService;
import com.bouncingdata.plfdemo.service.UserDataService;

@Controller
@RequestMapping("/dataset")
public class DatasetController {
  
  private Logger logger = LoggerFactory.getLogger(DatasetController.class);
  
  @Autowired
  private DatastoreService datastoreService;
  
  @Autowired
  private UserDataService userDataService;
    
  @RequestMapping(value="/{guid}", method = RequestMethod.GET)
  public @ResponseBody List<Map> getDataset(@PathVariable String guid) {
    try {
      Dataset ds = datastoreService.getDataset(guid);
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
      Dataset ds = datastoreService.getDataset(guid);
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
  
  
}
