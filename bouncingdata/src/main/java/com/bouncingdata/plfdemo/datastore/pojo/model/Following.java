package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Following {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  private int id;
  @Persistent(defaultFetchGroup="true")
  @Index(name="user_source_idx")
  private User user;
  @Persistent(defaultFetchGroup="true")
  @Index(name="user_follower_idx")
  private User follower;
  private Date followFrom;
  
  public Following(User user, User follower, Date from) {
    this.user = user;
    this.follower = follower;
    this.followFrom = from;
  }
  
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
  public User getFollower() {
    return follower;
  }
  public void setFollower(User follower) {
    this.follower = follower;
  }
  public Date getFollowFrom() {
    return followFrom;
  }
  public void setFollowFrom(Date followFrom) {
    this.followFrom = followFrom;
  }
}
