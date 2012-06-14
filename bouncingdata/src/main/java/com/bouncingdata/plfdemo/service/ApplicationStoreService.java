package com.bouncingdata.plfdemo.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

import com.bouncingdata.plfdemo.datastore.pojo.ApplicationDetail;
import com.bouncingdata.plfdemo.utils.Utils;
import com.bouncingdata.plfdemo.utils.VisualizationSource;
import com.bouncingdata.plfdemo.utils.VisualizationType;

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
    File dir = new File(storeAbsPath + Utils.FILE_SEPARATOR + guid);
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
  
  public ApplicationDetail getApplicationDetail(String guid, String language) throws IOException {
    String storeAbsPath = storePath; //servletContext.getRealPath(Utils.FILE_SEPARATOR + storePath);
    String filename = Utils.getApplicationFilename(language);
    String code = FileUtils.readFileToString(new File(storeAbsPath + Utils.FILE_SEPARATOR + guid 
        + Utils.FILE_SEPARATOR + filename));
    
    // read visualizations
   
    return new ApplicationDetail(code, null, getVisualizationMap(guid));
    
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
  
  public Map<String, VisualizationSource> getVisualizationMap(String guid) {
    String storeAbsPath = storePath;
    File appDir = new File(storeAbsPath + Utils.FILE_SEPARATOR + guid);
    File[] files = appDir.listFiles(new FileFilter() {
      
      @Override
      public boolean accept(File pathname) {
        String fname = pathname.getName();
        if (fname.endsWith(".png") || fname.endsWith(".html")) return true;
        return false;
      }
    });
    if (files == null) return null;
    
    Map<String, VisualizationSource> visuals = new HashMap<String, VisualizationSource>();
    for (File f : files) {
      String filename = f.getName();
      String name = filename.substring(0, filename.lastIndexOf("."));
      String extension = filename.substring(filename.lastIndexOf(".") + 1);
      VisualizationType type = null;
      if ("png".equals(extension)) type = VisualizationType.PNG;
      else if ("html".equals(extension)) type = VisualizationType.HTML;
      
      int length = (int) f.length();
      byte[] bytes = new byte[length];
      try {
        InputStream is = new BufferedInputStream(new FileInputStream(f));
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
          offset += numRead;
        }
        
        if (offset < bytes.length) {
          throw new IOException("Could not completely read file " + f.getAbsolutePath());
        }
        
        if (type == VisualizationType.PNG) visuals.put(name, new VisualizationSource(new String(Base64.encodeBase64(bytes)), type));
        else if (type == VisualizationType.HTML) visuals.put(name, new VisualizationSource(new String(bytes), type));
        
      } catch (IOException e) {
        logger.debug("Failed to read visualization at {}", f.getAbsoluteFile());
      }
    }
    return visuals;
  }
}
