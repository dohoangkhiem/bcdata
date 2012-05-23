package khiem.dataprj.demo.plfdemo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Table;
import khiem.dataprj.demo.plfdemo.service.DatastoreService;
import khiem.dataprj.demo.plfdemo.service.UserDataService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    List<Dataset> datasets = datastoreService.getDatasetList();
    model.addAttribute("datasets", datasets);
    
    Map<String, List<Table>> datamap = new HashMap<String, List<Table>>();
    for (Dataset ds : datasets) {
      datamap.put(ds.getName(), datastoreService.getTableList(ds.getName()));
    }
    model.addAttribute("datamap", datamap);
    return "dataset";
  }
  
  @RequestMapping(value="/gui/{dataset}", method = RequestMethod.GET)
  public String getDatasetView(@PathVariable String dataset, ModelMap model) {
    Dataset ds = datastoreService.getDataset(dataset);
    if (ds == null) return "redirect:/dataset/gui";
    model.addAttribute("currentDataset", ds);
    List<Table> tables = datastoreService.getTableList(ds.getName());
    model.addAttribute("tables", tables);
    return "dataset";
  }

  @RequestMapping(value="/rest/{dataset}/{tablename}", method = RequestMethod.GET)
  public @ResponseBody String getTableData(@PathVariable String dataset, @PathVariable String tablename) {
    try {
      return userDataService.getTableData(dataset, tablename);
    } catch (Exception e) {
      return "Not found!";
    }
  }
  
  @RequestMapping(value="/query", method = RequestMethod.GET)
  public @ResponseBody String query(String query) {   
    return userDataService.executeQueryWithResult(query);
  }
  
}
