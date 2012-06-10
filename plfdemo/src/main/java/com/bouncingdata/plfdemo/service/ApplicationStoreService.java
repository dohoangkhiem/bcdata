package com.bouncingdata.plfdemo.service;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

import com.bouncingdata.plfdemo.utils.Utils;

public class ApplicationStoreService implements ServletContextAware {
  
  private Logger logger = LoggerFactory.getLogger(ApplicationStoreService.class);
  
  private String storePath;
  private ServletContext servletContext;
  
  public ApplicationStoreService(String storePath) {
    this.storePath = storePath;
  }
  
  @Override
  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }
  
  public void createApplicationFile(String guid, String language, String code) throws IOException {
    String storeAbsPath = storePath; //servletContext.getRealPath(Utils.FILE_SEPARATOR + storePath);
    logger.debug("Application store absolute path: {}", storeAbsPath);
    File dir = new File(storeAbsPath + Utils.FILE_SEPARATOR + String.valueOf(guid));
    if (!dir.isDirectory()) {
      dir.mkdirs(); 
    }
    String filename = Utils.getApplicationFilename(language);
    FileUtils.write(new File(dir.getAbsolutePath() + Utils.FILE_SEPARATOR + filename), code);
    //logging
    logger.debug("Successfully write " + guid + "/" + filename);
  }
  
  public String getApplicationCode(String guid, String language) throws IOException {
    String storeAbsPath = storePath; //servletContext.getRealPath(Utils.FILE_SEPARATOR + storePath);
    String filename = Utils.getApplicationFilename(language);
    String code = FileUtils.readFileToString(new File(storeAbsPath + Utils.FILE_SEPARATOR + guid 
        + Utils.FILE_SEPARATOR + filename));
    return code;
  }
  
  public void saveApplicationCode(String guid, String language, String code) throws IOException {
    String storeAbsPath = storePath;
    String filename = Utils.getApplicationFilename(language);
    FileUtils.write(new File(storeAbsPath + Utils.FILE_SEPARATOR + guid + Utils.FILE_SEPARATOR + filename), code);
  }
  
  public String getVisualizationContent(int appId, String visualizationName) throws IOException {
    String content = FileUtils.readFileToString(new File(storePath + Utils.FILE_SEPARATOR 
       + String.valueOf(appId) + Utils.FILE_SEPARATOR + "/visualization/" + visualizationName + ".html"));
    return content;
  }
 
  public void saveVisualizationCode(int appId, String visualizationName, String code) throws IOException {
    String file = storePath + Utils.FILE_SEPARATOR + String.valueOf(appId) + Utils.FILE_SEPARATOR
       + "/visualization/" + visualizationName + ".html";
    FileUtils.writeStringToFile(new File(file), code, "UTF-8");
  }
}
