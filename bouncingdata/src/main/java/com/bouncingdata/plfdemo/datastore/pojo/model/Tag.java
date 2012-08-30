package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class Tag {
  private int id;
  private String tag;
  private int creator;
  private int popularity;
  private Date createAt;
  
  public Tag() {
    super();
  }
  
  public Tag(String tag) {
    super();
    this.tag = tag;
  }
  
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getTag() {
    return tag;
  }
  public void setTag(String tag) {
    this.tag = tag;
  }
  public int getCreator() {
    return creator;
  }
  public void setCreator(int creator) {
    this.creator = creator;
  }

  public int getPopularity() {
    return popularity;
  }

  public void setPopularity(int popularity) {
    this.popularity = popularity;
  }

  public Date getCreateAt() {
    return createAt;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }
}
