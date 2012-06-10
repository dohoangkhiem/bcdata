package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Dataset {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
  private int id;
  private String name;
  private String description;
  private String schema;
  private String guid;
  private int author;
  private String tags;
  private Date createAt;
  private Date lastUpdate;
  private int rowCount;
  private int appId;
  
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getSchema() {
    return schema;
  }
  public void setSchema(String schema) {
    this.schema = schema;
  }
  public String getGuid() {
    return guid;
  }
  public void setGuid(String guid) {
    this.guid = guid;
  }
  public int getAuthor() {
    return author;
  }
  public void setAuthor(int author) {
    this.author = author;
  }
  public String getTags() {
    return tags;
  }
  public void setTags(String tags) {
    this.tags = tags;
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
  public int getRowCount() {
    return rowCount;
  }
  public void setRowCount(int rowCount) {
    this.rowCount = rowCount;
  }
  public int getAppId() {
    return appId;
  }
  public void setAppId(int appId) {
    this.appId = appId;
  }
    
}
