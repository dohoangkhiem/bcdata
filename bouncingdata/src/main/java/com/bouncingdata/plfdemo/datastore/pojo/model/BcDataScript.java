package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.NullValue;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

@PersistenceCapable
public abstract class BcDataScript {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  private int id;
  private String name;
  private String description;
  private String language;
  @Index
  private @Unique String guid;
  @Persistent(defaultFetchGroup="true", nullValue=NullValue.EXCEPTION)
  private User user;
  private int lineCount;
  private boolean published;
  private boolean publicCode;
  private Date createAt;
  private Date lastUpdate;
  private boolean isExecuted;
  private String type;
  private String createSource;
   
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
  
  public abstract Set<Tag> getTags();
  
  public abstract void setTags(Set<Tag> tags);
  
  public boolean isPublicCode() {
    return publicCode;
  }
  public void setPublicCode(boolean publicCode) {
    this.publicCode = publicCode;
  }
  public boolean isExecuted() {
    return isExecuted;
  }
  public void setExecuted(boolean isExecuted) {
    this.isExecuted = isExecuted;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getCreateSource() {
    return createSource;
  }
  public void setCreateSource(String createSource) {
    this.createSource = createSource;
  }
  
}