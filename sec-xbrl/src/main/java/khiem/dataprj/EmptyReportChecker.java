package khiem.dataprj;

import java.beans.FeatureDescriptor;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.jruby.embed.LocalContextScope;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

public class EmptyReportChecker {
  private String basePath;
  StringBuilder log;
  
  int totalCompany = 0;
  int totalReport = 0;
  
  List<String> inCompleteCIK = new ArrayList<String>();
  static ScriptingContainer container = new ScriptingContainer(LocalContextScope.CONCURRENT);
  static Object receiver;
  
  public EmptyReportChecker(String basePath) {
    this.basePath = basePath;
    log = new StringBuilder();
    
  }
  
  public void check() {
    File baseFolder = new File(basePath);
    if (!baseFolder.isDirectory()) {
      System.err.println("The base directory " + basePath + " is not a directory or not existed!");
      log.append("The base directory " + basePath + " is not a directory or not existed!\n");
      return;
    }
    
    File[] children = baseFolder.listFiles(new FilenameFilter() {
      
      @Override
      public boolean accept(File dir, String name) {
        if (!name.matches("(\\d)+")) return false;
        return true;
      }
    });
    
    if (children == null || children.length ==0) {
      System.err.println("The base directory " + basePath + " is empty!");
      log.append("The base directory " + basePath + " is empty!\n");
      return;
    }
    
    for (int i = 0; i < children.length; i++) {
      totalCompany++;
      System.out.println("Checker goes to " + children[i].getName());
      File[] childrenOfChildren = children[i].listFiles(new FilenameFilter() {
        
        @Override
        public boolean accept(File dir, String name) {
          if (!name.matches("(\\d)+")) return false;
          return true;
        }
      });
      
      if (childrenOfChildren == null || childrenOfChildren.length == 0) {
        System.err.println("The directory of commpany with CIK " + children[i].getName() + " is empty!");
        log.append("The directory of commpany with CIK " + children[i].getName() + " is empty!\n");
        inCompleteCIK.add(children[i].getName());
        continue;
      } else {
        for (int j = 0; j < childrenOfChildren.length; j++) {
          if (childrenOfChildren[j].isFile()) {
            System.err.println("The directory of company with CIK " + children[i].getName() + " contains unknown file " + childrenOfChildren[j].getName());
            log.append("The directory of company with CIK " + children[i].getName() + " contains unknown file " + childrenOfChildren[j].getName() + "\n");
            continue;
          }
          
          System.out.println("Checker goes to " + children[i].getName() + "/" + childrenOfChildren[j].getName());
          totalReport++;
          File[] reportFile = childrenOfChildren[j].listFiles(new FileFilter() {
            
            @Override
            public boolean accept(File pathname) {
              return (pathname.isFile() && (pathname.getName().endsWith(".xml") || (pathname.getName().endsWith(".xsd"))));
            }
          });
          
          if (reportFile == null || reportFile.length == 0) {
            System.err.println("The directory of report " + children[i].getName() + "/" + childrenOfChildren[j].getName() + " is empty!");
            log.append("The directory of report " + children[i].getName() + "/" + childrenOfChildren[j].getName() + " is empty!\n");
            if (!inCompleteCIK.contains(children[i].getName())) inCompleteCIK.add(children[i].getName());
            continue;
          }
          
          File def, cal, lab, schema, ref, pre, ins;
          def = cal = lab = schema = ref = pre = ins = null;
          for (File file : reportFile) {
            String name = file.getName();
            if (name.endsWith("_def.xml")) {
              def = file;
              continue;
            }
            if (name.endsWith("_cal.xml")) {
              cal = file;
              continue;
            }
            if (name.endsWith("_lab.xml")) {
              lab = file;
              continue;
            }
            if (name.endsWith("_pre.xml")) {
              pre = file;
              continue;
            }
            if (name.endsWith("_ref.xml")) {
              ref = file;
              continue;
            }
            if (name.endsWith(".xsd")) {
              schema = file;
              continue;
            }
            if (name.endsWith(".xml")) {
              ins = file;
            }
          }
          
          if (ins == null) {
            System.err.println("The report " + children[i].getName() + "/" + childrenOfChildren[j].getName() + " is missing instance file!");
            log.append("The directory of report " + children[i].getName() + "/" + childrenOfChildren[j].getName() + " missing instance file!\n");
            if (!inCompleteCIK.contains(children[i].getName())) inCompleteCIK.add(children[i].getName());
            continue;
          }
          /*if (reportFile.length < 6) {
            System.err.println("The directory of report " + children[i].getName() + "/" + childrenOfChildren[j].getName() + " is not complete!");
            log.append("The directory of report " + children[i].getName() + "/" + childrenOfChildren[j].getName() + " is not complete!\n");
            String[] endings = new String[] {"_ref.xml", "_cal.xml", "_lab.xml", "_pre.xml", ".xsd", ".xml"};
            for (int k = 0; k < endings.length; k++) {
              boolean exist = false;
              for (int l = 0; l < reportFile.length; l++) {
                if (reportFile[l].getName().endsWith(endings[k])) {
                  exist = true;
                  break;
                }
              }
              if (!exist) {
                System.out.println("The " + endings[k] + " is missing");
                log.append("The " + endings[k] + " is missing\n");
              }
            }
            
            //if (!inCompleteCIK.contains(children[i].getName())) inCompleteCIK.add(children[i].getName());
          }*/
        }
      }
    }
    System.out.println("FINISHED!");
    
  }
  
  
  /**
   * @param args
   */
  public static void main(String args[]) {
    String path = "/media/MUSIC/xbrl";
    EmptyReportChecker checker = new EmptyReportChecker(path);
    checker.check();
    System.out.println("\nSUMMARY\n--------------------------------------------------------\n");
    System.out.println("Total company: " + checker.totalCompany + "\nTotal report: " + checker.totalReport + "\n");
    System.out.println(checker.log.toString());
    
    String basepath = System.getProperty("user.dir");
    container.runScriptlet("ENV['GEM_PATH'] = '" + basepath + "/lib/jruby'");
    receiver = container.runScriptlet(PathType.ABSOLUTE, basepath + "/xbrldemo.rb");
    
    String url = "http://www.sec.gov/cgi-bin/browse-edgar?action=getcompany&CIK=?cik&type=?type&dateb=&owner=exclude&count=100";
    // now, re-download all incomplete company report
    for (String cik : checker.inCompleteCIK) {
      checker.fetchData(url.replace("?cik", cik).replace("?type", "10-K"));
    }
  }
  
  public void fetchData(String url) {
    container.callMethod(receiver, "fetch_data_override", url);
  }
}
