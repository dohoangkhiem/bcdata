package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

@PersistenceCapable
public class Application {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  private int id;
  private String name;
  private String description;
  private String language;
  @Index(name="app_guid_idx", unique="true")
  @Unique
  private String guid;
  //private int author;
  private int lineCount;
  private boolean isPublished;
  private Date createAt;
  private Date lastUpdate;
  private String tags;
  @NotPersistent
  @Join
  private Set<Tag> _tags;
  
  //private String authorName;
  @Persistent(defaultFetchGroup="true")
  private User user;
  
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
  /*public int getAuthor() {
    return author;
  }
  public void setAuthor(int author) {
    this.author = author;
  }*/
  public int getLineCount() {
    return lineCount;
  }
  public void setLineCount(int lineCount) {
    this.lineCount = lineCount;
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
  public boolean isPublished() {
    return isPublished;
  }
  public void setPublished(boolean isPublished) {
    this.isPublished = isPublished;
  }
  /*public String getAuthorName() {
    return authorName;
  }
  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }*/
  public User getUser() {
    return user;
  }
  public void setUser(User user) {
    this.user = user;
  }
  public String getTags() {
    return tags;
  }
  public void setTags(String tags) {
    this.tags = tags;
  }
  public Set<Tag> get_tags() {
    return _tags;
  }
  public void set_tags(Set<Tag> _tags) {
    this._tags = _tags;
  }
  
  
  
}
