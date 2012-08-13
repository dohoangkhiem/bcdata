package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

public class Activity {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  private int id;
  @Persistent(defaultFetchGroup="true")
  private User user;
  @Persistent(defaultFetchGroup="true")
  private String action;
  private String objectId;
  private Date time;
  
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
  public String getObjectId() {
    return objectId;
  }
  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }
  public Date getTime() {
    return time;
  }
  public void setTime(Date time) {
    this.time = time;
  }
  
}
