package com.bouncingdata.plfdemo.datastore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import junit.framework.TestCase;

import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.Comment;
import com.bouncingdata.plfdemo.datastore.pojo.model.Scraper;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;

public class AbstractBouncingDataTest extends TestCase {
  
  PersistenceManagerFactory pmf;
  
  public AbstractBouncingDataTest(String name) {
    super(name);
  }
  
  @Override
  public void setUp() {
    pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
  }
  
  @Override
  public void tearDown() {
    pmf.close();
  }
  
  public void testPMF() throws Exception {
    PersistenceManager pm = pmf.getPersistenceManager();
    assertNotNull(pm);
    
    /*Query q = pm.newQuery(Analysis.class);
    List<Analysis> analyses =  (List<Analysis>) q.execute();
    assertNotNull(analyses);
    System.out.println("Number of analyses: " + analyses.size());
    assertTrue(analyses.size() > 0);*/
    pm.close();
  }
    
  public void _testAnalysisComment() throws Exception {
    PersistenceManager pm = pmf.getPersistenceManager();
    
    Query q = pm.newQuery(Analysis.class);
    List<Analysis> analyses =  (List<Analysis>) q.execute();
    
    Analysis analysis = analyses.get(0);
    
    q = pm.newQuery(User.class);
    q.setFilter("username == 'demo'");
    List<User> users = (List<User>) q.execute();
    User user = users.get(0);
    assertNotNull(user);
    
    Comment c = new Comment();
    c.setTitle("Test comment");
    c.setMessage("This is a test comment");
    c.setCreateAt(new Date());
    c.setOrder(1);
    c.setParentId(-1);
    c.setAccepted(true);
    c.setAnalysis(analysis);
    c.setUser(user);
    
    List<Comment> comments = new ArrayList<Comment>();
    comments.add(c);
    
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      pm.makePersistent(c);
      tx.commit();
      
      tx.begin();
      pm.deletePersistent(c);
      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
      pm.close();
    }
  }
  
  public void _testGetComment() {
    PersistenceManager pm = pmf.getPersistenceManager();
    
    Query q = pm.newQuery(Comment.class);
    List<Comment> comments =  (List<Comment>) q.execute();
    Comment comment = comments.get(0);
    //assertNotNull(comment);
    //assertNotNull(comment.getAnalysis());
    //assertNotNull(comment.getUser());
    pm.close();
  }
}
