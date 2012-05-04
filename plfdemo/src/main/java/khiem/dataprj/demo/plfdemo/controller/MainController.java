package khiem.dataprj.demo.plfdemo.controller;

import java.util.List;

import khiem.dataprj.demo.plfdemo.datastore.pojo.Application;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Table;
import khiem.dataprj.demo.plfdemo.service.DatastoreService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/main")
public class MainController {
  
  private DatastoreService datastoreService;
  
  public void setDatastoreService(DatastoreService ds) {
    this.datastoreService = ds;
  }
  
  @RequestMapping(method = RequestMethod.GET)
  public String main(ModelMap model) {
    model.addAttribute("message", "Hello I am Khiem");
    return "main";
  }
  
  @RequestMapping(value = "/dataset", method = RequestMethod.GET)
  public @ResponseBody List<Dataset> getAllDataset(ModelMap model) {
    // get list of dataset
    try {
      List<Dataset> datasets = datastoreService.getDatasetList();
      if (datasets != null) System.out.println("Number of dataset " + datasets.size());
      else System.out.println("None of dataset");
      return datasets;
    } catch (Exception e) {
      // log
      e.printStackTrace();
      return null;
    }
  }
  
  @RequestMapping(value="/table/{dataset}")
  public @ResponseBody List<Table> getTables(@PathVariable String dataset) {
    return null;
  }
  
  @RequestMapping(value="/application", method = RequestMethod.GET)
  @ResponseBody
  public List<Application> getAllApplication() {
    try {
      List<Application> applications = datastoreService.getApplicationList();
      if (applications != null) System.out.println("Number of application " + applications.size());
      else System.out.println("None of application");
      return applications;
    } catch (Exception e) {
      // log
      e.printStackTrace();
      return null; 
    }
  }
  
}
