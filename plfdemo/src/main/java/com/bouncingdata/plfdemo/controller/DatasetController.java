package com.bouncingdata.plfdemo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bouncingdata.plfdemo.datastore.pojo.model.old.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Datastore;
import com.bouncingdata.plfdemo.service.DatastoreService;
import com.bouncingdata.plfdemo.service.UserDataService;

@Controller
@RequestMapping("/dataset")
public class DatasetController {
  private DatastoreService datastoreService;
  private UserDataService userDataService;
  
  public void setDatastoreService(DatastoreService datastoreService) {
    this.datastoreService = datastoreService;
  }

  public void setUserDataService(UserDataService userDataService) {
    this.userDataService = userDataService;
  }
  
  @RequestMapping(method = RequestMethod.GET)
  public String getDefault() {
    return "redirect:/dataset/gui";
  }
  
  @RequestMapping(value="/gui", method = RequestMethod.GET)
  public String getView(ModelMap model) {
    model.addAttribute("currentDataset", null);
    List<Datastore> datasets = datastoreService.getDatastoreList();
    model.addAttribute("datasets", datasets);
    
    Map<String, List<Dataset>> datamap = new HashMap<String, List<Dataset>>();
    for (Datastore ds : datasets) {
      datamap.put(ds.getName(), datastoreService.getDatasetList(ds.getName()));
    }
    model.addAttribute("datamap", datamap);
    return "dataset";
  }
  
  @RequestMapping(value="/gui/{dataset}", method = RequestMethod.GET)
  public String getDatasetView(@PathVariable String dataset, ModelMap model) {
    Datastore ds = datastoreService.getDatastore(dataset);
    if (ds == null) return "redirect:/dataset/gui";
    model.addAttribute("currentDataset", ds);
    List<Dataset> tables = datastoreService.getDatasetList(ds.getName());
    model.addAttribute("tables", tables);
    return "dataset";
  }

  @RequestMapping(value="/rest/{dataset}/{tablename}", method = RequestMethod.GET)
  public @ResponseBody String getTableData(@PathVariable String dataset, @PathVariable String tablename) {
    try {
      return userDataService.getDatasetData(dataset, tablename);
    } catch (Exception e) {
      return "Not found!";
    }
  }
  
  @RequestMapping(value="/query", method = RequestMethod.GET)
  public @ResponseBody String query(String query) {   
    return userDataService.executeQueryWithResult(query);
  }
  
}
