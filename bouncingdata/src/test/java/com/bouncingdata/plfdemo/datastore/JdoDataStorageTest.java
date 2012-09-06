package com.bouncingdata.plfdemo.datastore;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

import com.bouncingdata.plfdemo.datastore.pojo.model.Activity;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;

@ContextConfiguration
public class JdoDataStorageTest extends AbstractJUnit38SpringContextTests {
  
  @Autowired
  private JdoDataStorage jdoDataStorage;
  
  public void setUp() {
  }
  
  public void tearDown() {
  }
  
  public void testJdoDataStorage() {
    assertNotNull(jdoDataStorage);
    
    User demo = jdoDataStorage.findUserByUsername("demo");
    assertNotNull(demo);
    List<Analysis> apps = jdoDataStorage.getAnalysisList(demo.getId());
    assertNotNull(apps);
    System.out.println("Number of application by demo: " + apps.size());
  }
    
  public void testCreateAnalysis() {
    Analysis anls = new Analysis();
    anls.setName("testAnalysis");
    anls.setLanguage("python");
    anls.setDescription("This is just a test analysis");
    //anls.setTags("test");
    String guid="abcd12345ef";
    anls.setGuid(guid);
    User demo = jdoDataStorage.findUserByUsername("demo");
    anls.setUser(demo);
    jdoDataStorage.createAnalysis(anls);
    Analysis anls1 = jdoDataStorage.getAnalysisByGuid(guid);
    assertNotNull(anls1);
    assertTrue(anls1.getName().equals("testAnalysis"));
    jdoDataStorage.deleteAnalysis(anls1.getId());
  }
  
  public void testUpdateApplication() {
    Analysis anls = new Analysis();
    anls.setName("testAnalysis");
    anls.setLanguage("python");
    anls.setDescription("This is just a test analysis");
    //anls.setTags("test");
    String guid="abcd12345ef";
    anls.setGuid(guid);
    User demo = jdoDataStorage.findUserByUsername("demo");
    anls.setUser(demo);
    jdoDataStorage.createAnalysis(anls);
    Analysis anls1 = jdoDataStorage.getAnalysisByGuid(guid);
    anls1.setName("testAnalysis1");
    jdoDataStorage.updateAnalysis(anls1);
    anls1 = jdoDataStorage.getAnalysisByGuid(guid);
    assertNotNull(anls1);
    assertTrue(anls1.getName().equals("testAnalysis1"));
    jdoDataStorage.deleteAnalysis(anls1.getId());
  }
  
  public void testGetFeed() {
    User demo = jdoDataStorage.findUserByUsername("demo");
    assertNotNull(demo);
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, -1);
    List<Activity> activities = jdoDataStorage.getFeed(demo.getId(), 20);
    assertNotNull(activities);
    //assertTrue(activities.size() > 0);
  }
  
  public void _testGetScraperDatasets() {
    int scraperId = 4;
    List<Dataset> datasetList = jdoDataStorage.getScraperDatasets(scraperId);
    System.out.println("Number of dataset for scraper 4: " + datasetList.size());
  }
  
  /*public void testGetComment() {
    User test = new User();
    test.setUsername("testUser");
    test.setPassword("testPassword");
    test.setEmail("test@bouncingdata.com");
    
    Analysis analysis = new Analysis("testGuid", "testStatus");
    Comment c = new Comment();
    c.setAnalysis(analysis);
    c.setUser(test);
    c.setTitle("testComment");
    c.setMessage("This is a test comment");
    c.setAccepted(true);
    c.setOrder(1);
    c.setParentId(-1);
    
    jdoDataStorage.addComment(test.getId(), analysisId, comment)
    
    List<Comment> comments = jdoDataStorage.getComments(analysis.getId());
    assertNotNull(comments);
    Comment c1 = comments.get(0);
    assertTrue(c1.getId() == c.getId());
    assertTrue(c1.getTitle().equals(c.getTitle()));
    assertTrue(c1.getMessage().equals(c.getMessage()));
  }*/
  
}
