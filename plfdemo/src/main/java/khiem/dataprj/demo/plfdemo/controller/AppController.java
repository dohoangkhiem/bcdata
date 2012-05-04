package khiem.dataprj.demo.plfdemo.controller;

import khiem.dataprj.demo.plfdemo.datastore.pojo.Application;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;
import khiem.dataprj.demo.plfdemo.service.ApplicationExecutor;
import khiem.dataprj.demo.plfdemo.service.DatastoreService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/app")
public class AppController {
  
  private DatastoreService datastoreService;
  private ApplicationExecutor appExecutor;
  
  public void setDatastoreService(DatastoreService dsService) {
    this.datastoreService = dsService;
  }
  
  public void setAppExecutor(ApplicationExecutor appExecutor) {
    this.appExecutor = appExecutor;
  }
  
  @RequestMapping(value="/{appname}", method = RequestMethod.GET)
  public String getApp(@PathVariable String appname, ModelMap model) {
    model.addAttribute("appname", appname);
    
    // get application name, code
    Application application = datastoreService.getApplication(appname);
    if (application == null) return "app";
    
    // get application dataset, visualization
    Dataset dataset = datastoreService.getDataset(appname);
    
    
    return "app";
  }
  
  @RequestMapping(value="/{appname}/execute",  method = RequestMethod.POST)
  public @ResponseBody String executeApp(@PathVariable String appname, @RequestParam(value="code", required=true) String code, ModelMap model) {
    // invoke executor to execute code, pass the appname as parameter
    String output = appExecutor.executePython(appname, code);
    
    // return the output console
    return output;
  }
  
}
