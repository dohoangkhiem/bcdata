package com.bouncingdata.plfdemo.service;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bouncingdata.plfdemo.datastore.pojo.ExecutionResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.datastore.pojo.model.Visualization;
import com.bouncingdata.plfdemo.utils.Utils;
import com.bouncingdata.plfdemo.utils.VisualizationSource;
import com.bouncingdata.plfdemo.utils.VisualizationType;

public class LocalApplicationExecutor implements ApplicationExecutor {
  
  private Logger logger = LoggerFactory.getLogger(LocalApplicationExecutor.class);
  
  private String logDir;
  
  private String storePath;
  
  private DatastoreService datastoreService;
  
  public void setLogDir(String ld) {
    this.logDir = ld;
  }

  public void setStorePath(String sp) {
    this.storePath = sp;
  }
  
  public void setDatastoreService(DatastoreService ds) {
    this.datastoreService = ds;
  }
  
  @Override
  public ExecutionResult executePython(Application app, String code, User user) {
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
    Map<String, String> datasets = getDatasets(ticket);
    Map<String, VisualizationSource> visuals = getVisualizations(ticket);
    return new ExecutionResult(output, visuals, datasets, 0, "OK");
  }
    
  @Override
  public ExecutionResult executeR(Application app, String code, User user) {
    String ticket = Utils.getExecutionId();
    String tempFile = logDir + Utils.FILE_SEPARATOR + ticket + Utils.FILE_SEPARATOR + ticket + ".R";
    File temp = new File(tempFile);
    //String updatedCode = "options(device=png)\n" + code;
    try {
      if (!temp.getParentFile().isDirectory()) {
        temp.getParentFile().mkdirs();
      }
      
      BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
      writer.write(code);
      writer.close();
    } catch (Exception e) { 
      e.printStackTrace(); 
    }
    
    String mode = "persistent";
    if (app == null) {
      mode = "not-persistent";
    }
    ProcessBuilder pb = new ProcessBuilder("Rscript", tempFile, ticket, app==null?"-1":String.valueOf(app.getId()), String.valueOf(user.getId()), user.getUsername(), mode);
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
    
    Map<String, String> datasets = getDatasets(ticket);
    Map<String, VisualizationSource> visuals = getVisualizations(ticket);
    
    //
    if (mode.equals("persistent")) {
      // copy visuals from log dir to visualizations dir
      try {
        copyVisualizations(ticket, app);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    return new ExecutionResult(output, visuals, datasets, 0, "OK");
  }
  
  private Map<String, String> getDatasets(String executionId) {
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
    if (datasetFiles != null) {
      datasets = new HashMap<String, String>();
      for (File f : datasetFiles) {
        String filename = f.getName();
        String name = filename.substring(0, filename.lastIndexOf("."));
        byte[] b = new byte[1024];
        try {
          InputStream is = new BufferedInputStream(new FileInputStream(f));
          int c;
          StringBuilder sb = new StringBuilder();
          while ((c = is.read(b)) != -1) {
            String s = new String(b, 0, c);
            sb.append(s);
          }
          datasets.put(name, sb.toString());
        } catch (IOException e) {
          logger.debug("Can't read dataset file {}", f.getAbsolutePath());
        }
        
      }
    }
    return datasets;
  }
  
  private void copyVisualizations(String executionId, Application app) throws Exception {
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
    File vDir = new File(storePath + Utils.FILE_SEPARATOR + app.getGuid() + Utils.FILE_SEPARATOR + "v");
    if (!vDir.isDirectory()) {
      vDir.mkdirs(); 
    }
    
    if (vsFiles != null) {
      for (File f : vsFiles) {
        String filename = f.getName();
        Visualization v = new Visualization();
        v.setAppId(app.getId());
        v.setAuthor(app.getAuthor());
        v.setName(filename.substring(0, filename.lastIndexOf(".")));
        v.setType("png");
        String guid = Utils.generateGuid();
        v.setGuid(guid);
        datastoreService.createVisualization(v);
        try {
          FileUtils.copyFile(f, new File(vDir.getAbsoluteFile() + Utils.FILE_SEPARATOR + guid + ".png"));
        } catch (IOException e) {
          logger.debug("Failed to copy visual file " + f.getAbsolutePath() + " to " + vDir.getAbsolutePath());
        }
      }
    }
  }
  
  private Map<String, VisualizationSource> getVisualizations(String executionId) {
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
    
    Map<String, VisualizationSource> visuals = null;
    if (vsFiles != null) {
      visuals = new HashMap<String, VisualizationSource>();
      for (File f : vsFiles) {
        String filename = f.getName();
        String name = filename.substring(0, filename.lastIndexOf("."));
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        VisualizationType type = null;
        if ("png".equals(extension)) type = VisualizationType.PNG;
        else if ("html".equals(extension)) type = VisualizationType.HTML;
        
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
          if (type == VisualizationType.PNG) visuals.put(name, new VisualizationSource(new String(Base64.encodeBase64(bytes)), type));
          else if (type == VisualizationType.HTML) visuals.put(name, new VisualizationSource(new String(bytes), type));
          
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
   */
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
          while (offset < bytes.length
                 && (numRead = is.read(bytes, offset, bytes.length-offset)) >= 0) {
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
}
