package khiem.dataprj.demo.plfdemo.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LxcApplicationExecutor implements ApplicationExecutor {

  @Override
  public String executePython(String appname, String code) {
    
    ProcessBuilder pb = new ProcessBuilder("sudo lxc-execute -n lxc-2 -f /home/khiem/lxc-2.conf echo " + code + " > " + appname + ".py" + "| python");
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
    ProcessBuilder pb = new ProcessBuilder("appexecute.sh", "/root/syspath.py");
    //ProcessBuilder pb = new ProcessBuilder("echo", "hello");
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
      System.out.println(output.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }    
  }

  @Override
  public String executeR(String appname, String code) {
    // TODO Auto-generated method stub
    return null;
  }

}
