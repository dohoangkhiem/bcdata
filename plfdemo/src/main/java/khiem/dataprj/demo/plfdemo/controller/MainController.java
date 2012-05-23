package khiem.dataprj.demo.plfdemo.controller;

import java.io.IOException;
import java.util.List;

import khiem.dataprj.demo.plfdemo.datastore.pojo.Application;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Table;
import khiem.dataprj.demo.plfdemo.service.ApplicationExecutor;
import khiem.dataprj.demo.plfdemo.service.ApplicationStoreService;
import khiem.dataprj.demo.plfdemo.service.DatastoreService;
import khiem.dataprj.demo.plfdemo.utils.Utils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/main")
public class MainController {
  
  private DatastoreService datastoreService;
  private ApplicationStoreService appStoreService;
  private ApplicationExecutor appExecutor;
  
  public void setDatastoreService(DatastoreService ds) {
    this.datastoreService = ds;
  }
  
  public void setAppStoreService(ApplicationStoreService appStoreService) {
    this.appStoreService = appStoreService;
  }
  
  public void setAppExecutor(ApplicationExecutor appExecutor) {
    this.appExecutor = appExecutor;
  }
  
  @RequestMapping(method = RequestMethod.GET)
  public String main(ModelMap model) {
    model.addAttribute("app", null);
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
  
  @RequestMapping(value = "/createApp", method = RequestMethod.POST)
  @ResponseBody
  public String createApplication(@RequestParam(value = "appname", required = true) String appname,
      @RequestParam(value = "language", required = true) String language,
      @RequestParam(value = "description", required = true) String description,
      @RequestParam(value = "code", required = true) String code, ModelMap model) {
    
    // store app info
    datastoreService.createApplication(appname, description, language);
    
    // store app code file
    try {
      appStoreService.createApplicationFile(appname, language, code);
    } catch (IOException e) {
      // loggin
      e.printStackTrace();
    }
    
    return "";
  }
  
  @RequestMapping(value="/execute", method = RequestMethod.POST)
  public @ResponseBody String executeApp(@RequestParam(value="code", required=true) String code, @RequestParam(value="language", required=true) String language, ModelMap model) {
    // generate the id
    String exeId = Utils.getExecutionId();
    // invoke executor to execute code, pass the id as parameter
    String output = null;
    if ("python".equals(language)) {
      output = appExecutor.executePython(exeId, code);
    } else if ("r".equals(language)) {
      output = appExecutor.executeR(exeId, code);
    } else {
      output = "Not support";
    }
    
    // return the output console
    return output;
  }
  
}
