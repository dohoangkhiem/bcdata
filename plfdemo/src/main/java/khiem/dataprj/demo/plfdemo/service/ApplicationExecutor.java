package khiem.dataprj.demo.plfdemo.service;

public interface ApplicationExecutor {
  
  public String executePython(String appname, String code);
  
  public String executeR(String appname, String code);
}
