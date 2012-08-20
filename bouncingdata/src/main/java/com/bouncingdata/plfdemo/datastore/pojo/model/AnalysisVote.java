package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NullValue;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class AnalysisVote {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  private int id;
  @Persistent(defaultFetchGroup="true", nullValue=NullValue.EXCEPTION)
  private User user;
  @Persistent(defaultFetchGroup="true", nullValue=NullValue.EXCEPTION)
  private Analysis analysis;
  private int vote;
  private Date voteAt;
  private boolean isActive;
  
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
  public Analysis getAnalysis() {
    return analysis;
  }
  public void setAnalysis(Analysis analysis) {
    this.analysis = analysis;
  }
  public Date getVoteAt() {
    return voteAt;
  }
  public void setVoteAt(Date voteAt) {
    this.voteAt = voteAt;
  }
  public int getVote() {
    return vote;
  }

  public void setVote(int vote) {
    this.vote = vote;
  }
  public boolean isActive() {
    return isActive;
  }
  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }
}
