package com.bouncingdata.plfdemo.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

import com.bouncingdata.plfdemo.datastore.pojo.ApplicationDetail;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;
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
    
  public void saveApplicationCode(String guid, String language, String code) throws IOException {
    String storeAbsPath = storePath;
    String filename = Utils.getApplicationFilename(language);
    FileUtils.write(new File(storeAbsPath + Utils.FILE_SEPARATOR + guid + Utils.FILE_SEPARATOR + filename), code);
  }
  
  public String getVisualization(String guid, String vGuid, String type) throws IOException {
    File f = new File(storePath + Utils.FILE_SEPARATOR + guid + Utils.FILE_SEPARATOR + "/v/" + vGuid + "." + type.toLowerCase());
    if (!f.isFile()) {
      // 
      if (logger.isDebugEnabled()) {
        logger.debug("Visualization file {} does not existed.", f.getAbsolutePath());
      }
      return null;
    }
    if ("png".equals(type)) {
      byte[] bytes = FileUtils.readFileToByteArray(f);
      return new String(Base64.encodeBase64(bytes));
    } else {
      String content = FileUtils.readFileToString(f);
      return content;
    }
  }
   
  /**
   * @param guid
   * @param visualizations
   * @return
   */
  public Map<String, VisualizationSource> getVisualizationMap(String guid, List<Visualization> visualizations) {
    if (visualizations == null) return null;
    
    String storeAbsPath = storePath;
    File appDir = new File(storeAbsPath + Utils.FILE_SEPARATOR + guid);
    
    Map<String, VisualizationSource> visualsMap = new HashMap<String, VisualizationSource>();
    
    for (Visualization v : visualizations) {
      String vGuid = v.getGuid();
      String vType = v.getType().toLowerCase();
      String fname = vGuid + "." + vType;
      File f = new File(appDir + Utils.FILE_SEPARATOR + "v" + Utils.FILE_SEPARATOR + fname);
      if (!f.isFile()) {
        logger.debug("Cannot read file " + f.getAbsolutePath());
        continue;
      }
      
      VisualizationType type = null;
      if ("png".equals(vType)) type = VisualizationType.PNG;
      else if ("html".equals(vType)) type = VisualizationType.HTML;
      
      int length = (int) f.length();
      byte[] bytes = new byte[length];
      try {
        /*InputStream is = new BufferedInputStream(new FileInputStream(f));
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
          offset += numRead;
        }
        
        if (offset < bytes.length) {
          throw new IOException("Could not completely read file " + f.getAbsolutePath());
        }*/
        bytes = FileUtils.readFileToByteArray(f);
        
        if (type == VisualizationType.PNG) {
          visualsMap.put(v.getName(), new VisualizationSource(new String(Base64.encodeBase64(bytes)), type)); 
        }
        else if (type == VisualizationType.HTML) {
          visualsMap.put(v.getName(), new VisualizationSource(new String(bytes), type));
        }
        
      } catch (IOException e) {
        logger.debug("Failed to read visualization at {}", f.getAbsoluteFile());
      }
    }
    return visualsMap;
  }
}
