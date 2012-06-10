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
import java.util.List;


import org.apache.commons.codec.binary.Base64;

import com.bouncingdata.plfdemo.datastore.pojo.ExecutionResult;
import com.bouncingdata.plfdemo.utils.Utils;

public class LocalApplicationExecutor implements ApplicationExecutor {
  
  private String logDir;
  
  public void setLogDir(String ld) {
    this.logDir = ld;
  }

  @Override
  public ExecutionResult executePython(String appname, String code) {
    // get execution ticket
    String ticket = Utils.getExecutionId();
    String tempFile = logDir + Utils.FILE_SEPARATOR + ticket + Utils.FILE_SEPARATOR + ticket + ".py";
    // invokes Python
    File temp = new File(tempFile);
    
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
    if (appname == null) appname = "";
    ProcessBuilder pb = new ProcessBuilder("python", tempFile, ticket, appname);
    pb.redirectErrorStream(true);
    
    String output = null;
    try {
      Process p = pb.start();   
      InputStream appOutputStream = new BufferedInputStream(p.getInputStream());
      int c;
      StringBuilder outputBuilder = new StringBuilder();
      byte[] b = new byte[1024];
      while ((c = appOutputStream.read(b)) != -1) {
        String chunk = new String(b, 0, c);
        outputBuilder.append(chunk);
      }
      output = outputBuilder.toString();
    } catch (IOException e) {
      e.printStackTrace();
    }
    List<String> encodedSnapshots = getEncodedVisualizations(ticket);
    return new ExecutionResult(output, encodedSnapshots, 0, "OK");
  }
  
  public static void main(String args[]) {
    String code = "library(\"R.utils\") \n x <- c(1,2,3,4,5,6) \n y <- x^2 \n lm_1 <- lm(y~x) \n print(lm_1) \n plot(lm_1)";
    LocalApplicationExecutor executor =  new LocalApplicationExecutor();
    executor.setLogDir("/tmp/R");
    ExecutionResult result = executor.executeR(null, code);
    System.out.println("Output: " + result.getOutput());
    System.out.println("Visualizations: " + result.getVisualizations().get(0));
  }
  
  @Override
  public ExecutionResult executeR(String appname, String code) {
    String ticket = Utils.getExecutionId();
    String tempFile = logDir + Utils.FILE_SEPARATOR + ticket + Utils.FILE_SEPARATOR + ticket + ".R";
    File temp = new File(tempFile);
    String updatedCode = "options(device=png)\n" + code;
    try {
      if (!temp.getParentFile().isDirectory()) {
        temp.getParentFile().mkdirs();
      }
      
      BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
      writer.write(updatedCode);
      writer.close();
    } catch (Exception e) { 
      e.printStackTrace(); 
    }
    
    if (appname == null) appname = "";
    ProcessBuilder pb = new ProcessBuilder("Rscript", tempFile, ticket, appname);
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
    
    /* read the logs */
    List<String> encodedSnapshots = getEncodedVisualizations(ticket);
    return new ExecutionResult(output, encodedSnapshots, 0, "OK");
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
