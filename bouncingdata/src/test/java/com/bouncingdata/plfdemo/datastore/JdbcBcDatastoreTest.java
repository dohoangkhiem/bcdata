package com.bouncingdata.plfdemo.datastore;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

import com.bouncingdata.plfdemo.util.dataparsing.DataParser;
import com.bouncingdata.plfdemo.util.dataparsing.DataParserFactory;
import com.bouncingdata.plfdemo.util.dataparsing.DataParserFactory.FileType;

@ContextConfiguration
public class JdbcBcDatastoreTest extends AbstractJUnit38SpringContextTests {

  @Autowired
  private JdbcBcDatastore jdbcBcDatastore;
  
  public void setUp() {
  
  }
  
  public void tearDown() {
  
  }
  
  public void testOwner() {
    assertNotNull(jdbcBcDatastore);
  }
  
  public void testPersistData() throws Exception {
    String tableName = "demo.testTable";
    String[] headers = {"username", "age", "job"};
    List<String[]> data = new ArrayList<String[]>();
    data.add(new String[] {"khi'em", "25", "developer"});
    data.add(new String[] {"adriano", "30", "striker"});
    data.add(new String[] {"sneijder", "28", "attacking midfielder"});
    jdbcBcDatastore.persistDataset(tableName, headers, data);
    
    String dataset = jdbcBcDatastore.getDataset(tableName);
    assertNotNull(dataset);
    assertTrue(dataset.length() > 0);
    
    jdbcBcDatastore.dropDataset(tableName);
  }
  
  public void testParseExcel() throws Exception {
    String tableName = "demo.from_excel";
    InputStream is = new FileInputStream("/home/khiem/workbook.xls");
    DataParser parser = DataParserFactory.getDataParser(FileType.EXCEL);
    List<String[]> data = parser.parse(is);
    try {
      jdbcBcDatastore.persistDataset(tableName, data.get(0), data.subList(1, data.size()));
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    String dataset = jdbcBcDatastore.getDataset(tableName);
    assertNotNull(dataset);
    assertTrue(dataset.length() > 0);
    
    jdbcBcDatastore.dropDataset(tableName);
  }
}
