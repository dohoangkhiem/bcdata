package khiem.dataprj.demo.plfdemo.service;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import khiem.dataprj.demo.plfdemo.utils.Utils;

public class LocalApplicationExecutor implements ApplicationExecutor {

  @Override
  public String executePython(String appname, String code) {
    // get execution ticket
    String ticket = Utils.getExecutionId();
    String tempFile = "/tmp/" + ticket + ".py";
    // invokes Python
    File temp = new File(tempFile);
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
    
      writer.write(code);
      writer.close();
    } catch (Exception e) { e.printStackTrace(); }
    
    //ProcessBuilder pb = new ProcessBuilder("appexecute.sh", appname, code);
    ProcessBuilder pb = new ProcessBuilder("python", tempFile);
    pb.redirectErrorStream(true);
    
    try {
      Process p = pb.start();   
      InputStream appOutputStream = new BufferedInputStream(p.getInputStream());
      int c;
      StringBuilder output = new StringBuilder();
      byte[] b = new byte[1024];
      while ((c = appOutputStream.read(b)) != -1) {
        String chunk = new String(b, 0, c);
        output.append(chunk);
      }
      return output.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public static void main(String[] args) {
    String code = "import httplib \n"
        + "import json \n"
+ "conn = httplib.HTTPConnection(\"api.worldbank.org\") \n"
+ "conn.request(\"GET\", \"/countries/br/indicators/NY.GDP.MKTP.CD?format=json&date=2000:2010\") \n"
+ "res = conn.getresponse() \n"
+ "print res.status \n" 
+ "data = res.read() \n"
+ "print data \n"
+ "obj = json.loads(data) \n"
+ "page = obj[0] \n"
+ "datalist = obj[1]";
    String output = new LocalApplicationExecutor().executePython("khiem", code);
    System.out.println (output);
  }

  @Override
  public String executeR(String appname, String code) {
    String ticket = Utils.getExecutionId();
    String tempFile = "/tmp/" + ticket + ".R";
    File temp = new File(tempFile);
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
    
      writer.write(code);
      writer.close();
    } catch (Exception e) { e.printStackTrace(); }
    
    ProcessBuilder pb = new ProcessBuilder("Rscript", tempFile);
    pb.redirectErrorStream(true);
    
    String output;
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
      return null;
    }
    
    /* read the logs */
    
    return output;
  }

}
