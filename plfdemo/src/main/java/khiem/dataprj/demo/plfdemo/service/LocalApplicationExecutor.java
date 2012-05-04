package khiem.dataprj.demo.plfdemo.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

public class LocalApplicationExecutor implements ApplicationExecutor {

  @Override
  public String executePython(String appname, String code) {
    // invokes Python
    String random = UUID.randomUUID().toString();
    File temp = new File("/tmp/" + appname + ".py");
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
    
      writer.write(code);
      writer.close();
    } catch (Exception e) { e.printStackTrace(); }
    
    //ProcessBuilder pb = new ProcessBuilder("appexecute.sh", appname, code);
    ProcessBuilder pb = new ProcessBuilder("python", "/tmp/" + appname + ".py");
    Map<String, String> env = pb.environment();
    //env.put("VAR1", "myValue");
    //env.remove("OTHERVAR");
    //env.put("VAR2", env.get("VAR1") + "suffix");
    try {
      Process p = pb.start();
      InputStream is = p.getInputStream();
      int c;
      StringBuilder output = new StringBuilder();
      while ((c = is.read()) != -1) {
        output.append((char)c);
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

}