package khiem.dataprj.demo.plfdemo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import khiem.dataprj.demo.plfdemo.datastore.pojo.Application;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Dataset;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Table;
import khiem.dataprj.demo.plfdemo.datastore.pojo.Visualization;
import khiem.dataprj.demo.plfdemo.service.ApplicationExecutor;
import khiem.dataprj.demo.plfdemo.service.ApplicationStoreService;
import khiem.dataprj.demo.plfdemo.service.DatastoreService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
  private ApplicationStoreService appStoreService;
  private DatastoreService userDataService;
  
  public void setDatastoreService(DatastoreService dsService) {
    this.datastoreService = dsService;
  }
  
  public void setAppExecutor(ApplicationExecutor appExecutor) {
    this.appExecutor = appExecutor;
  }
  
  public void setAppStoreService(ApplicationStoreService appStoreService) {
    this.appStoreService = appStoreService;
  }
  
  public void setUserDataService(DatastoreService userDataService) {
    this.userDataService = userDataService;
  }
  
  @RequestMapping(value="/{appname}", method = RequestMethod.GET)
  public String getApp(@PathVariable String appname, ModelMap model) {    
    // get application name, code
    Application application = datastoreService.getApplication(appname);
    if (application == null) return "app";
    
    model.addAttribute("app", application);
    
    // get application dataset, visualization
    Dataset dataset = datastoreService.getDataset(appname);
    model.addAttribute("dataset", dataset);
    
    // get list of tables inside dataset
    List<Table> tables = datastoreService.getTableList(appname);
    // get data from (at least) the first table
    model.addAttribute("tables", tables);
    
    // list of visualizations
    List<Visualization> visualizations = datastoreService.getVisualizationList(appname);
    model.addAttribute("visualizations", visualizations);
    
    try {
      String code = appStoreService.getAppliationCode(appname, "python");
      model.addAttribute("appcode", code);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "app";
  }
  
  @RequestMapping(value="/{appname}/execute",  method = RequestMethod.POST)
  public @ResponseBody String executeApp(@PathVariable String appname, @RequestParam(value="code", required=true) String code, ModelMap model) {
    // invoke executor to execute code, pass the appname as parameter
    String output = appExecutor.executePython(appname, code);
    
    // return the output console
    return output;
  }
  
  @RequestMapping(value="/{appname}/save", method = RequestMethod.POST)
  public @ResponseBody String saveApp(@PathVariable String appname, @RequestParam(value="code", required=true) String code, ModelMap model) {
    try {
      appStoreService.saveApplicationCode(appname, "python", code);
      return "OK";
    } catch (IOException e) {
      return "Failed";
    }
  }
  
  @RequestMapping(value="/{appname}/data/{tablename}", method = RequestMethod.GET)
  public @ResponseBody String getData(@PathVariable String appname, @PathVariable String tablename, ModelMap model) {
    return userDataService.getTableData(appname, tablename);
  }
  
  @RequestMapping(value="/{appname}/data/{tablename}/delete", method = RequestMethod.POST)
  public @ResponseBody String deleteTable(@PathVariable String appname, @PathVariable String tablename) {
    try {
      datastoreService.deleteTable(appname, tablename);
      userDataService.deleteTable(appname, tablename);
      return "OK";
    } catch (Exception e) {
      e.printStackTrace();
      return "Failed";
    }  
  }
  
  @RequestMapping(value="/{appname}/visualize", method = RequestMethod.GET) 
  public @ResponseBody List<String> getVisualization(@PathVariable String appname) {
    List<Visualization> visuals = datastoreService.getVisualizationList(appname);
    List<String> results = new ArrayList<String>();
    if (visuals != null) {
      for (Visualization v : visuals) {
        results.add(v.getName());
      }
    }
    return results;
  }
  
  @RequestMapping(value="/{appname}/visualize/{visualizationName}", method = RequestMethod.GET)
  public @ResponseBody String getVisualizationContent(@PathVariable String appname, @PathVariable String visualizationName, ModelMap model) {
    try {
      return appStoreService.getVisualizationContent(appname, visualizationName);
    } catch(IOException e) {
      return "Failed to load this visualization";
    }
  }
  
  @RequestMapping(value="/{appname}/visualize/{visualName}/delete", method = RequestMethod.POST)
  public @ResponseBody String deleteVisualization(@PathVariable String appname, @PathVariable String visualName) {
    try {
      datastoreService.deleteVisualization(appname, visualName);
      return "OK";
    } catch (Exception e) {
      e.printStackTrace();
      return "Error";
    }
  }
  
  @RequestMapping(value="/{appname}/visualize/{visualName}/save", method = RequestMethod.POST)
  public @ResponseBody String saveVisualizationCode(@PathVariable String appname, @PathVariable String visualName, @RequestParam(value="code", required=true) String code) {
    try {
      appStoreService.saveVisualizationCode(appname, visualName, code);
      return "OK";
    } catch (IOException e) {
      e.printStackTrace();
      return "Error";
    }
  }
  
  @RequestMapping(value="/{appname}/data")
  public @ResponseBody String getApplicationData(@PathVariable String appname) {
    List<Table> tableList = datastoreService.getTableList(appname);
    JSONArray tableListJson = new JSONArray();
    if (tableList != null) {
      for (Table t : tableList) {
        String tdata = userDataService.getTableData(appname, t.getName());
        JSONObject js = new JSONObject();
        js.element("name", t.getName());
        js.element("data", tdata);
        tableListJson.add(js);
      }
    }
    
    JSONArray visualizationListJson = new JSONArray();
    List<Visualization> visualizations = datastoreService.getVisualizationList(appname);
    if (visualizations != null) {
      for (Visualization v : visualizations) {
        visualizationListJson.add(v.getName());      
      }
    }
    JSONObject result = new JSONObject();
    result.element("tables", tableListJson.toString());
    result.element("visualizations", visualizationListJson.toString());
    return result.toString();
  }
  
}
