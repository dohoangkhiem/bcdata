package khiem.dataprj;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.jruby.RubyHash;
import org.jruby.RubyObject;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;
import org.jruby.exceptions.RaiseException;

public class XBRLDatastore {
  private MySQLAccess mySQLAccess;
  private String xbrlDirectory;
  private static ScriptingContainer container = new ScriptingContainer(LocalContextScope.CONCURRENT);
  private static Object receiver;
  
  public XBRLDatastore(String xbrlDir) throws Exception {
    this.xbrlDirectory = xbrlDir;
    mySQLAccess = new MySQLAccess();
    String basepath = System.getProperty("user.dir");
    container.runScriptlet("ENV['GEM_PATH'] = '" + basepath + "/lib/jruby'");
    receiver = container.runScriptlet(PathType.ABSOLUTE, basepath + "/xbrldemo.rb");
  }
  
  public void closeConnection() {
    try {
      mySQLAccess.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public static void main(String args[]) throws Exception {
    XBRLDatastore xbrlDatastore = new XBRLDatastore("/media/MUSIC/xbrl1");
    try {
      xbrlDatastore.process();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      xbrlDatastore.closeConnection();
    }
  }
  
  public void process() throws Exception {
    File root = new File(xbrlDirectory);
    if (!root.isDirectory()) {
      System.err.println("The XBRL directory is invalid!");
      return;
    }
    
    File[] companies = root.listFiles(new FilenameFilter() {
      
      @Override
      public boolean accept(File dir, String name) {
        return name.matches("(\\d)+");
      }
    });
    
    if (companies == null || companies.length == 0) {
      System.err.println("The XBRL directory is empty!");
      return;
    }
    
    int count = 0;
    for (File company : companies) {
      String cik = company.getName();
      System.out.println("Processing the company with CIK " + cik);
      
      File[] reports = company.listFiles();
      if (reports == null || reports.length == 0) {
        System.err.println("Company with CIK " + cik + " has no report!");
        return;
      }
      
      // push data to 'companies' table
      // get the ticker
      String ticker = reports[0].listFiles()[0].getName().split("-")[0];
      int id = mySQLAccess.storeCompany(cik, ticker, "");
      
      for (File report : reports) {
        
        /*if (count == 1) {
          System.out.println("FINISHED!");
          return;
        }*/
        
        String accessionNo = report.getName();      
        
        File[] reportFiles = report.listFiles();
        if (reportFiles == null || reportFiles.length == 0) {
          System.err.println("Report " + accessionNo + " of company " + cik + " is empty!") ;
          return;
        }
        
        int reportId = mySQLAccess.storeReport(id, accessionNo, null);
        
        count++;
        
        // pass the report dir to ruby to parse, return the list of items
        try {
          System.out.println("Parse the report " + report.getAbsolutePath());
          RubyObject[] items = container.callMethod(receiver, "parse_xbrl",
              report.getAbsolutePath(), RubyObject[].class);
          
          // store the item to 'facts' table
          for (RubyObject item : items) {
            RubyHash hash = (RubyHash) item;
            String conceptName = hash.get("name").toString();
            String value = hash.get("value")==null?null:hash.get("value").toString();
            if (value != null) {
              value = value.substring(0,(value.length() > 1000 ? 999 : value.length()));
            }
            String startDate = hash.get("start_date")==null?null:hash.get("start_date").toString();
            String endDate = hash.get("end_date")==null?null:hash.get("end_date").toString();
            
            String unit = hash.get("unit")==null?null:hash.get("unit").toString();
            
            String type = hash.get("type").toString();
            
            mySQLAccess.storeFact(reportId, conceptName, value, startDate, endDate, unit, type);
          }
        } catch (RaiseException e) {
          e.printStackTrace();
        } catch (SQLException e) {
          System.err.println(e.getMessage());
        }
      }
      
    }
  }
  
  static class MySQLAccess {
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    
    public MySQLAccess() throws Exception {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/xbrldemo", "root", "a");
    } 
    
    public void close() throws SQLException {
      if (preparedStatement != null) preparedStatement.close();
      if (connection != null) connection.close();
    }
    
    /**
     * @param cik
     * @param ticker
     * @param name
     * @return
     * @throws SQLException
     */
    public int storeCompany(String cik, String ticker, String name) throws SQLException {
      try {
        preparedStatement = connection.prepareStatement("insert into companies(cik, ticker, name) values(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, cik);
        preparedStatement.setString(2, ticker);
        preparedStatement.setString(3, name);
        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows == 0) {
          throw new SQLException("Failed to insert new company " + cik + ", no row affected");
        }
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
          return Integer.valueOf(generatedKeys.getString(1));
        } else return -1;
      } finally {
        preparedStatement.close();
      }
    }
    
    /**
     * @param companyId
     * @param accessionNo
     * @param reportDate
     * @return
     * @throws SQLException
     */
    public int storeReport(int companyId, String accessionNo, Date reportDate) throws SQLException {
      try {
        preparedStatement = connection.prepareStatement("insert into reports(accessionNo, companyId) values(?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, accessionNo);
        preparedStatement.setInt(2, companyId);
        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows == 0) {
          throw new SQLException("Failed to insert new report " + accessionNo + ", no row affected");
        }
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
          return Integer.valueOf(generatedKeys.getString(1));
        } else return -1;
      } finally {
        preparedStatement.close();
      }
    }
    
    public int storeFact(int reportId, String concept, String value, String startDate, String endDate, String unit, String type) throws SQLException {
      try {    
        if ("monetary".equals(type) || "dei".equals(type)) {
          preparedStatement = connection.prepareStatement("insert into facts_monetary(concept, value, start_date, end_date, unit, report_id) values (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
          preparedStatement.setString(5, unit);
          preparedStatement.setInt(6, reportId);
        } else if ("text".equals(type)) {
          preparedStatement = connection.prepareStatement("insert into facts_text(concept, value, start_date, end_date, report_id) values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
          preparedStatement.setInt(5, reportId);
        } else {
          System.err.println("Skip store fact " + concept + ". Unknown type of fact '" + type + "'.");
          return -1;
        }
        preparedStatement.setString(1, concept);
        preparedStatement.setString(2, value);
        preparedStatement.setString(3, startDate);
        preparedStatement.setString(4, endDate);
        
        int affectedRows = preparedStatement.executeUpdate();
        if (affectedRows == 0) {
          throw new SQLException("Failed to insert new fact, no row affected");
        }
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
          return Integer.valueOf(generatedKeys.getString(1));
        } else return -1;
        
      } finally {
        preparedStatement.close();
      }
    }
  }
}
