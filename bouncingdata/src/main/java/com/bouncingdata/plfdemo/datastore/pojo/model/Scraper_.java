package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.NullValue;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

public class Scraper_ {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  private int id;
  private String name;
  private String description;
  private String language;
  @Index(name="scraper_guid_idx", unique="true")
  private @Unique String guid;
  @Persistent(defaultFetchGroup="true", nullValue=NullValue.EXCEPTION)
  private User user;
  private int lineCount;
  private boolean published;
  private Date createAt;
  private Date lastUpdate;
  @Persistent(mappedBy="scraper")
  List<Dataset> datasets;
  
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
  public String getLanguage() {
    return language;
  }
  public void setLanguage(String language) {
    this.language = language;
  }
  public String getGuid() {
    return guid;
  }
  public void setGuid(String guid) {
    this.guid = guid;
  }
  public User getUser() {
    return user;
  }
  public void setUser(User user) {
    this.user = user;
  }
  public int getLineCount() {
    return lineCount;
  }
  public void setLineCount(int lineCount) {
    this.lineCount = lineCount;
  }
  public boolean isPublished() {
    return published;
  }
  public void setPublished(boolean published) {
    this.published = published;
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
  public List<Dataset> getDatasets() {
    return datasets;
  }
  public void setDatasets(List<Dataset> datasets) {
    this.datasets = datasets;
  }
  
    
}
