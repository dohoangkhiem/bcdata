package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Analysis {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  private int id;
  private String guid;
  private String status;
  @Persistent(mappedBy="analysis") List<Comment> comments;
  
  public Analysis(String guid, String status) {
    this.guid = guid;
    this.status = status;
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getGuid() {
    return guid;
  }
  public void setGuid(String guid) {
    this.guid = guid;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public List<Comment> getComments() {
    return comments;
  }
  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }
  
}