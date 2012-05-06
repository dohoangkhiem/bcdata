package khiem.dataprj.demo.plfdemo.controller;

import java.io.IOException;

import khiem.dataprj.demo.plfdemo.service.ApplicationStoreService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/visualize")
public class VisualizeController {
  
  private ApplicationStoreService appStoreService;
  
  public void setAppStoreService(ApplicationStoreService appStoreService) {
    this.appStoreService = appStoreService;
  }
  
  @RequestMapping(value="/{appname}/{visualization}", method = RequestMethod.GET)
  public String get(@PathVariable String appname, @PathVariable String visualization, ModelMap model) {
    try { 
      String content = appStoreService.getVisualizationContent(appname, visualization);
      model.addAttribute("content", content);
    } catch (IOException e) {
      model.addAttribute("content", "Not found!");
    }
    return "visualize";
  }
}
