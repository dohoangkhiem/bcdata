package khiem.dataprj.demo.plfdemo.controller;

import java.util.List;

import khiem.dataprj.demo.plfdemo.datastore.pojo.Application;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;
import khiem.dataprj.demo.plfdemo.service.DatastoreService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
public class SearchController {

  private DatastoreService datastoreService;
  private DatastoreService userDataService;
  
  public void setDatastoreService(DatastoreService dsService) {
    this.datastoreService = dsService;
  }
  
  public void setUserDataService(DatastoreService udService) {
    this.userDataService = udService;
  }
  
  @RequestMapping(method = RequestMethod.GET)
  public String getResult(@RequestParam(value="query", required=true) String query, ModelMap model) {
    List<Application> apps = null;
    List<Dataset> datasets = null;
    try {
      apps = userDataService.searchApplication(query);
      datasets = userDataService.searchDataset(query);
    } catch (Exception e) {
      e.printStackTrace();
    }
    model.addAttribute("appList", apps);
    model.addAttribute("datasetList", datasets);
    return "search";
  }
}