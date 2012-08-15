package com.bouncingdata.plfdemo.datastore;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractJUnit38SpringContextTests;

import com.bouncingdata.plfdemo.datastore.pojo.model.Activity;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.Comment;
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
    List<Application> apps = jdoDataStorage.getApplicationList(demo.getId());
    assertNotNull(apps);
    System.out.println("Number of application by demo: " + apps.size());
  }
  
  public void _testTransactional() {
    User test = new User();
    test.setUsername("testUser");
    test.setPassword("testPassword");
    test.setEmail("test@bouncingdata.com");
    jdoDataStorage.createUser(test);
    User test1 = jdoDataStorage.findUserByUsername("testUser");
    assertNotNull(test1);
    System.out.println(test1.getEmail());
  }
  
  public void _testUpdateApplication() {
    User demo = jdoDataStorage.findUserByUsername("demo");
    List<Application> apps = jdoDataStorage.getApplicationList(demo.getId());
    assertTrue(apps.size() > 0);
    Application app = apps.get(0);
    app.setName("zzztestApplicationzzz");
    jdoDataStorage.updateApplication(app);
    app = jdoDataStorage.getApplication(app.getId());
    assertNotNull(app);
    assertTrue(app.getName().equals("zzztestApplicationzzz"));
  }
  
  public void testGetFeed() {
    User demo = jdoDataStorage.findUserByUsername("demo");
    assertNotNull(demo);
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, -1);
    List<Activity> activities = jdoDataStorage.getFeed(demo.getId(), calendar.getTime());
    assertNotNull(activities);
    assertTrue(activities.size() > 0);
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
