package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.NullValue;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.bouncingdata.plfdemo.util.Action;

@PersistenceCapable
public class Activity {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  private int id;
  @Persistent(defaultFetchGroup="true", nullValue=NullValue.EXCEPTION)
  private User user;
  private String action;
  private int objectId;
  private Date time;
  private boolean isPublic;
  private String message;
  
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
  
  public String getDescription() {
    StringBuilder desc = new StringBuilder("<strong>" + user.getUsername() + "</strong>");
    String guid = ((Analysis)object).getGuid();
    if (Action.PUBLISH.getAction().equals(action)) {
      desc.append(" has published analysis ");
      desc.append("<a href='#'>" + guid  + "<a>");
    } else if (Action.UPDATE.getAction().equals(action)) {
      desc.append(" has updated analysis ");
      desc.append("<a href='#'>" + guid  + "<a>");
    }
    desc.append("\n");
    desc.append(time.toString());
    return desc.toString();
  }
  public boolean isPublic() {
    return isPublic;
  }
  public void setPublic(boolean isPublic) {
    this.isPublic = isPublic;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  } 
}
