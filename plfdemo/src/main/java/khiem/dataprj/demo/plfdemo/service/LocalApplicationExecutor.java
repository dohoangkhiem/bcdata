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
    
    // invokes Python
    File temp = new File("/tmp/" + appname + ".py");
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
    
      writer.write(code);
      writer.close();
    } catch (Exception e) { e.printStackTrace(); }
    
    //ProcessBuilder pb = new ProcessBuilder("appexecute.sh", appname, code);
    ProcessBuilder pb = new ProcessBuilder("python", "/tmp/" + appname + ".py");
    pb.redirectErrorStream(true);
    
    //return ticket;
    
    //Map<String, String> env = pb.environment();
    //env.put("VAR1", "myValue");
    //env.remove("OTHERVAR");
    //env.put("VAR2", env.get("VAR1") + "suffix");
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
    // TODO Auto-generated method stub
    return null;
  }

}
