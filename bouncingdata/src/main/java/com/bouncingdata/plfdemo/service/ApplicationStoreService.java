package com.bouncingdata.plfdemo.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bouncingdata.plfdemo.datastore.pojo.dto.VisualizationDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.VisualizationType;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;
import com.bouncingdata.plfdemo.utils.Utils;

public class ApplicationStoreService {
  
  private Logger logger = LoggerFactory.getLogger(ApplicationStoreService.class);
    
  private String storePath;
  private String logDir;
  
  public void setStorePath(String storePath) {
    this.storePath = storePath;
  }

  public void setLogDir(String logDir) {
    this.logDir = logDir;
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
  
  public String getScriptCode(String guid, String language) throws IOException {
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
  
  public String getTemporaryVisualization(String executionId, String name, String type) throws IOException {
    File f = new File(logDir + Utils.FILE_SEPARATOR + executionId + Utils.FILE_SEPARATOR + name + "." + type.toLowerCase());
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
  
}
