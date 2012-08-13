package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Index;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

@PersistenceCapable
public class User implements UserDetails {
  
  private static final long serialVersionUID = -1319577184342896023L;
  
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private int id;
  @Index(name="username_idx", unique="true")
  @Unique
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private boolean enabled;
  private int groupId;
  private Date joinedDate;
  private Date lastLogin;
  
  @NotPersistent private Set<GrantedAuthority> authorities;
  @JsonIgnore
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
  @JsonIgnore
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
  @JsonIgnore
  public int getGroupId() {
    return groupId;
  }
  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }
  
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  
  @Override 
  public boolean isEnabled() {
    return enabled;
  }
  
  @JsonIgnore
  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return authorities;
  }
  
  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }
  
  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }
  
  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
  
  public void setAuthorities(Collection<String> authorities) {
    this.authorities = new HashSet<GrantedAuthority>();
    for (String s : authorities) {
      this.authorities.add(new GrantedAuthorityImpl(s));
    }
  }
}
