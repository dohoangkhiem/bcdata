package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Activity {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  private int id;
  @Persistent(defaultFetchGroup="true")
  private User user;
  @Persistent(defaultFetchGroup="true")
  private String action;
  private int objectId;
  private Date time;
  
  @NotPersistent
  private Object object;
  
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public User getUser() {
    return user;
  }
  public void setUser(User user) {
    this.user = user;
  }
  public String getAction() {
    return action;
  }
  public void setAction(String verb) {
    this.action = verb;
  }
  public int getObjectId() {
    return objectId;
  }
  public void setObjectId(int objectId) {
    this.objectId = objectId;
  }
  public Date getTime() {
    return time;
  }
  public void setTime(Date time) {
    this.time = time;
  }
  public Object getObject() {
    return object;
  }
  public void setObject(Object object) {
    this.object = object;
  }
  
}
