package com.bouncingdata.plfdemo.service;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bouncingdata.plfdemo.datastore.pojo.dto.ExecutionResult;
import com.bouncingdata.plfdemo.datastore.pojo.dto.VisualizationDetail;
import com.bouncingdata.plfdemo.datastore.pojo.dto.VisualizationType;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;
import com.bouncingdata.plfdemo.utils.Utils;

public class LocalApplicationExecutor implements ApplicationExecutor {
  
  private Logger logger = LoggerFactory.getLogger(LocalApplicationExecutor.class);
  
  private String logDir;
  private String storePath;
  
  @Autowired
  private DatastoreService datastoreService;
    
  public void setLogDir(String ld) {
    this.logDir = ld;
  }

  public void setStorePath(String sp) {
    this.storePath = sp;
  }
    
  @Override
  public ExecutionResult executePython(Analysis app, String code, User user) throws Exception {
    // get execution ticket
    final String ticket = Utils.getExecutionId();
    
    String mode = "persistent";
    if (app == null) {
      mode = "not-persistent";
    }
    ProcessBuilder pb = new ProcessBuilder("python", "-c",  code, ticket, app==null?"-1":String.valueOf(app.getId()), String.valueOf(user.getId()), user.getUsername(), mode);
    pb.redirectErrorStream(true);
    
    String output = null;
    try {
      logger.info("Starting the execution {}, requested user {}, appId: {}", new Object[] { ticket, user.getUsername(), app==null?"-1":app.getId() });
      final Process p = pb.start();
      Timer t = new Timer();
      t.schedule(new TimerTask() {      
        @Override
        public void run() {
          try {
            p.exitValue();
          } catch (IllegalThreadStateException e) {
            logger.info("Killed the execution {}. Reason: process too long.", ticket);
            p.destroy();
          }
          this.cancel();
        }
      }, 1000*60*5);
      
      InputStream appOutputStream = new BufferedInputStream(p.getInputStream());
      int c;
      StringBuilder outputBuilder = new StringBuilder();
      byte[] b = new byte[1024];
      try {
        while ((c = appOutputStream.read(b)) != -1) {
          String chunk = new String(b, 0, c);
          outputBuilder.append(chunk);
        }
      } catch (IOException e) {
        // the stream maybe closed due to timeout or unknown error
        logger.debug("Exception occurs when reading output stream from execution {}. Maybe the process has been terminated.", ticket);
      }
      output = outputBuilder.toString();
      try {
        p.exitValue();
      } catch (IllegalThreadStateException e) {
        p.destroy();
        t.cancel();
      }
      
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    Map<String, String> datasets = dataPostProcess(ticket, app, user);
    if (app == null) {
      Map<String, VisualizationDetail> visuals = getVisualizations(ticket);
      return new ExecutionResult(output, visuals, datasets, 0, "OK");
    } else {
      return new ExecutionResult(output, null, datasets, 0, "OK");
    }
  }
    
  @Override
  public ExecutionResult executeR(Analysis app, String code, User user) throws Exception {
    String ticket = Utils.getExecutionId();
    String tempFile = logDir + Utils.FILE_SEPARATOR + ticket + Utils.FILE_SEPARATOR + ticket + ".R";
    File temp = new File(tempFile);
    //String updatedCode = "options(device=png)\npng(width=1200)\n" + code;
    try {
      if (!temp.getParentFile().isDirectory()) {
        temp.getParentFile().mkdirs();
      }
      
      BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
      //writer.write(updatedCode);
      writer.write(code);
      writer.close();
    } catch (Exception e) { 
      e.printStackTrace(); 
    }
    
    String mode = "persistent";
    if (app == null) {
      mode = "not-persistent";
    }
    ProcessBuilder pb = new ProcessBuilder("Rscript", tempFile, ticket, app==null?"-1":String.valueOf(app.getId()), app==null?"":app.getGuid(), String.valueOf(user.getId()), user.getUsername(), mode);
    if (!pb.environment().containsKey("R_DEFAULT_DEVICE")) {
      pb.environment().put("R_DEFAULT_DEVICE", "png");
    }
    pb.redirectErrorStream(true);
    pb.directory(temp.getParentFile());
    
    String output = null;
    /* read the console output */ 
    try {
      Process p = pb.start();   
      InputStream appOutputStream = new BufferedInputStream(p.getInputStream());
      int c;
      StringBuilder out = new StringBuilder();
      byte[] b = new byte[1024];
      while ((c = appOutputStream.read(b)) != -1) {
        String chunk = new String(b, 0, c);
        out.append(chunk);
      }
      output = out.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    Map<String, String> datasets = dataPostProcess(ticket, app, user);
    
    //
    if (mode.equals("persistent")) {
      // copy visuals from log dir to visualizations dir
      try {
        processVisualizations(ticket, app);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    if (app == null) {
      Map<String, VisualizationDetail> visuals = getVisualizations(ticket);
      return new ExecutionResult(output, visuals, datasets, 0, "OK");
    } else {
      return new ExecutionResult(output, null, datasets, 0, "OK");
    }
  }
  
  private Map<String, String> dataPostProcess(String executionId, Analysis anls, User user) throws Exception {
    String execLogPath = logDir + Utils.FILE_SEPARATOR + executionId;
    File execLogDir = new File(execLogPath);
    File[] datasetFiles = execLogDir.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        if (pathname.isFile() && pathname.getName().endsWith(".dat")) {
          return true;
        } else return false;
      }
    });
    
    Map<String, String> datasets = null;
    List<Dataset> dsList = new ArrayList<Dataset>();
    ObjectMapper mapper = new ObjectMapper();
    if (datasetFiles != null) {
      datasets = new HashMap<String, String>();
      for (File f : datasetFiles) {
        String filename = f.getName();
        String name = filename.substring(0, filename.lastIndexOf("."));
        try {
          String s = FileUtils.readFileToString(f);
          JsonNode dataObj = mapper.readTree(s);
          JsonNode data = dataObj.get("data");
          datasets.put(name, data.getValueAsText());
          Dataset ds = new Dataset();
          ds.setName(dataObj.get("name").getTextValue());
          ds.setDescription(dataObj.get("description").getTextValue());
          ds.setGuid(Utils.generateGuid());
          ds.setSchema(dataObj.get("schema").getTextValue());
          ds.setUser(user);
          ds.setCreateAt(new Date());
          ds.setLastUpdate(new Date());
          ds.setAnalysis(anls);
          ds.setRowCount(data.size());
          ds.setActive(true);
          dsList.add(ds);
        } catch (IOException e) {
          logger.debug("Can't read dataset file {}", f.getAbsolutePath());
        }        
      }
      
      // invalidate old datasets?
      datastoreService.invalidateDataset(anls);
      
      datastoreService.createDatasets(dsList);
      
    }
    return datasets;
  }
  
  /**
   * 
   * @param executionId
   * @param anls
   * @throws Exception
   */
  private void processVisualizations(String executionId, Analysis anls) throws Exception {
    datastoreService.invalidateViz(anls);
    String execLogPath = logDir + Utils.FILE_SEPARATOR + executionId;
    File execLogDir = new File(execLogPath);
    File[] vsFiles = execLogDir.listFiles(new FileFilter() {
      
      @Override
      public boolean accept(File pathname) {
        if (pathname.isFile() && (pathname.getName().endsWith(".png") || pathname.getName().endsWith(".html"))) {
          return true;
        } else return false;
      }
    });
    File vDir = new File(storePath + Utils.FILE_SEPARATOR + anls.getGuid() + Utils.FILE_SEPARATOR + "v");
    if (!vDir.isDirectory()) {
      vDir.mkdirs(); 
    }
    
    if (vsFiles != null) {
      for (File f : vsFiles) {
        String filename = f.getName();
        Visualization v = new Visualization();
        
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        VisualizationType type = null;
        if ("png".equals(extension)) type = VisualizationType.PNG;
        else if ("html".equals(extension)) type = VisualizationType.HTML;
        
        v.setAnalysis(anls);
        v.setUser(anls.getUser());
        v.setName(filename.substring(0, filename.lastIndexOf(".")));
        v.setType(type.getType());
        String guid = Utils.generateGuid();
        v.setGuid(guid);
        v.setActive(true);
        datastoreService.createVisualization(v);
        try {
          FileUtils.copyFile(f, new File(vDir.getAbsoluteFile() + Utils.FILE_SEPARATOR + guid + "." + type.getType()));
        } catch (IOException e) {
          logger.debug("Failed to copy visual file " + f.getAbsolutePath() + " to " + vDir.getAbsolutePath());
        }
      }
    }
  }
    
  private Map<String, VisualizationDetail> getVisualizations(String executionId) {
    String execLogPath = logDir + Utils.FILE_SEPARATOR + executionId;
    File execLogDir = new File(execLogPath);
    File[] vsFiles = execLogDir.listFiles(new FileFilter() {
      
      @Override
      public boolean accept(File pathname) {
        if (pathname.isFile() && (pathname.getName().endsWith(".png") || pathname.getName().endsWith(".html"))) {
          return true;
        } else return false;
      }
    });
    
    Map<String, VisualizationDetail> visuals = null;
    if (vsFiles != null) {
      visuals = new HashMap<String, VisualizationDetail>();
      for (File f : vsFiles) {
        String filename = f.getName();
        String name = filename.substring(0, filename.lastIndexOf("."));
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        VisualizationType type = null;
        if ("png".equals(extension)) type = VisualizationType.PNG;
        else if ("html".equals(extension)) {
          type = VisualizationType.HTML;
          visuals.put(name, new VisualizationDetail(null, "temp/" + executionId + "/" + name + "/html", type));
          continue;
        }
        
        int length = (int) f.length();
        byte[] bytes = new byte[length];
        try {
          bytes = FileUtils.readFileToByteArray(f);
          visuals.put(name, new VisualizationDetail(null, new String(Base64.encodeBase64(bytes)), type));        
        } catch (IOException e) {
          logger.debug("Failed to read visualization at {}", f.getAbsoluteFile());
        }
        
      }
    }
    return visuals;
  }
  
  /**
   * Get snapshot visualizations for a execution
   * @param executionId
   * @return
   *//*
  private List<String> getEncodedVisualizations(String executionId) {
    // scan visualization dir
    String visualizationPath = logDir + Utils.FILE_SEPARATOR + executionId; // + Utils.FILE_SEPARATOR + "visualizations";
    File visualizationDir = new File(visualizationPath);
    File[] snapshots = visualizationDir.listFiles(new FileFilter() {
      
      @Override
      public boolean accept(File pathname) {
        if (pathname.isFile() && pathname.getName().endsWith(".png")) {
          return true;
        } else return false;
      }
    });
    
    List<String> encodedSnapshots = null;
    if (snapshots != null) {
      encodedSnapshots = new ArrayList<String>();
      for (File snapshot : snapshots) {
        int length = (int) snapshot.length();
        if (length > Integer.MAX_VALUE) {
          // File is too large
        }
        byte[] bytes = new byte[(int)length];
        try {
          InputStream is = new FileInputStream(snapshot);
          // Read in the bytes
          int offset = 0;
          int numRead = 0;
          while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
          }

          // Ensure all the bytes have been read in
          if (offset < bytes.length) {
              throw new IOException("Could not completely read file " + snapshot.getName());
          }
          String base64 = new String(Base64.encodeBase64(bytes)); 
          encodedSnapshots.add(base64);
        } catch (IOException e) {
          e.printStackTrace();
        }            
      }
    }
    
    return encodedSnapshots;
  }
  */
}
