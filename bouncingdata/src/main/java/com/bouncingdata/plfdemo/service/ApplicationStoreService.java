package com.bouncingdata.plfdemo.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bouncingdata.plfdemo.datastore.pojo.dto.Attachment;
import com.bouncingdata.plfdemo.util.Utils;

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
  
  public List<Attachment> getAttachmentData(String guid) {
    File dir = new File(storePath + Utils.FILE_SEPARATOR + guid);
    if (!dir.isDirectory()) {
      logger.debug("The application {} does not exist.", guid);
      return null;
    }
    
    File[] attachedFiles = dir.listFiles(new FileFilter() {
      
      @Override
      public boolean accept(File pathname) {
        return pathname.isFile() && pathname.getName().endsWith(".att");
      }
    });
    
    ObjectMapper mapper = new ObjectMapper();
    List<Attachment> results = null;
    if (attachedFiles != null) {
      results = new ArrayList<Attachment>();
      for (File f : attachedFiles) {
        String s;
        try {
          s = FileUtils.readFileToString(f);
        } catch (IOException e) {
          logger.debug("Can't read attachment file {}", f.getAbsolutePath());
          continue;
        }
        if (s == null || s.isEmpty()) continue;
        
        try {
          JsonNode root = mapper.readTree(s);
          String name = root.get("name").getTextValue();
          String description = null;
          if (root.has("description")) {
            description = root.get("description").getTextValue();
          }
          String data = root.get("data").toString();
          Attachment attachment = new Attachment(-1, name, description, data);
          results.add(attachment);
        } catch (IOException e) {
          logger.debug("Cannot parse attachment file {}", f.getAbsoluteFile());
          continue;
        }
        
      }
    }
    
    return results;
  }
  
}
