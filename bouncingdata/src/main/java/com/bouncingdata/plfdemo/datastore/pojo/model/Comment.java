package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Comment {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private int id;
  private User user;
  private Analysis analysis;
  private int order;
  private String title;
  private String content;
  private Date createAt;
  private Date lastUpdate;
  private int lastUpdateBy;
  private int removeBy;
  private int upVote;
  private int downVote;
  private int parentId;
  private boolean accepted;
  
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
  public Analysis getAnalysis() {
    return analysis;
  }
  public void setAnalysis(Analysis analysis) {
    this.analysis = analysis;
  }
  public int getOrder() {
    return order;
  }
  public void setOrder(int order) {
    this.order = order;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public Date getCreateAt() {
    return createAt;
  }
  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }
  public Date getLastUpdate() {
    return lastUpdate;
  }
  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }
  public int getUpVote() {
    return upVote;
  }
  public void setUpVote(int upVote) {
    this.upVote = upVote;
  }
  public int getDownVote() {
    return downVote;
  }
  public void setDownVote(int downVote) {
    this.downVote = downVote;
  }
  public int getParentId() {
    return parentId;
  }
  public void setParentId(int parentId) {
    this.parentId = parentId;
  }
  public int getLastUpdateBy() {
    return lastUpdateBy;
  }
  public void setLastUpdateBy(int lastUpdateBy) {
    this.lastUpdateBy = lastUpdateBy;
  }
  public int getRemoveBy() {
    return removeBy;
  }
  public void setRemoveBy(int removeBy) {
    this.removeBy = removeBy;
  }
  public boolean isAccepted() {
    return accepted;
  }
  public void setAccepted(boolean accepted) {
    this.accepted = accepted;
  }
  
  
}
