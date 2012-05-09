package khiem.dataprj.demo.plfdemo.service;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.springframework.web.context.ServletContextAware;

public class ApplicationStoreService implements ServletContextAware {
  
  private String storePath;
  private ServletContext servletContext;
  
  public ApplicationStoreService(String storePath) {
    this.storePath = storePath;
  }
  
  @Override
  public void setServletContext(ServletContext servletContext) {
    this.servletContext = servletContext;
  }
  
  public void createApplicationFile(String appname, String language, String code) throws IOException {
    String storeAbsPath = storePath; //servletContext.getRealPath(System.getProperty("file.separator") + storePath);
    System.out.println("App store path: " + storeAbsPath);
    File dir = new File(storeAbsPath + System.getProperty("file.separator") + appname);
    if (!dir.isDirectory()) {
      dir.mkdirs(); 
    }
    FileUtils.write(new File(dir.getAbsolutePath() + System.getProperty("file.separator") + "appcode.py"), code);
    //logging
    System.out.println("Successfully write " + appname + ".py");
  }
  
  public String getAppliationCode(String appname, String language) throws IOException {
    String storeAbsPath = storePath; //servletContext.getRealPath(System.getProperty("file.separator") + storePath);
    String code = FileUtils.readFileToString(new File(storeAbsPath + System.getProperty("file.separator") + appname 
        + System.getProperty("file.separator") + "appcode.py"));
    return code;
  }
  
  public void saveApplicationCode(String appname, String language, String code) throws IOException {
    String storeAbsPath = storePath;
    FileUtils.write(new File(storeAbsPath + System.getProperty("file.separator") + appname 
        + System.getProperty("file.separator") + "appcode.py"), code);
  }
  
 public String getVisualizationContent(String appname, String visualizationName) throws IOException {
   String content = FileUtils.readFileToString(new File(storePath + System.getProperty("file.separator") 
       + appname + System.getProperty("file.separator") + "/visualization/" + visualizationName + ".html"));
   return content;
 }
 
 public void saveVisualizationCode(String appname, String visualizationName, String code) throws IOException {
   String file = storePath + System.getProperty("file.separator") + appname + System.getProperty("file.separator")
       + "/visualization/" + visualizationName + ".html";
   FileUtils.writeStringToFile(new File(file), code, "UTF-8");
 }
}
