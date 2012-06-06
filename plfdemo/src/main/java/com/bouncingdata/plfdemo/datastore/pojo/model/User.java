package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class User {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
  private int id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private boolean enabled;
  private int groupId;
  private Date joinedDate;
  private Date lastLogin;
  
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public Date getJoinedDate() {
    return joinedDate;
  }
  public void setJoinedDate(Date joinedDate) {
    this.joinedDate = joinedDate;
  }
  public Date getLastLogin() {
    return lastLogin;
  }
  public void setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
  }
  public int getGroupId() {
    return groupId;
  }
  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }
  public boolean isEnabled() {
    return enabled;
  }
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
