package com.bouncingdata.plfdemo.datastore.pojo.dto;

import java.util.Date;

public class UserInfo {
  private int id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private Date joinedDate;
  private Date lastLogin;
  private boolean isFriend;
  
  public UserInfo(int id, String username, String firstName, String lastName,
      String email, Date joinedDate, Date lastLogin) {
    super();
    this.id = id;
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.joinedDate = joinedDate;
    this.lastLogin = lastLogin;
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

  public boolean isFriend() {
    return isFriend;
  }

  public void setFriend(boolean isFriend) {
    this.isFriend = isFriend;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
  
  
  
}
