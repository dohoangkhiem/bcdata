package com.bouncingdata.plfdemo.datastore.pojo.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class ExecutionLog {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private int id;
  private String executionId;
  private int appId;
  private Date startedTime;
  private Date endedTime;
  private int persistedRowCount;
  private int visualizationCount;
  private String output;
  
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getExecutionId() {
    return executionId;
  }
  public void setExecutionId(String executionId) {
    this.executionId = executionId;
  }
  public int getAppId() {
    return appId;
  }
  public void setAppId(int appId) {
    this.appId = appId;
  }
  public Date getStartedTime() {
    return startedTime;
  }
  public void setStartedTime(Date startedTime) {
    this.startedTime = startedTime;
  }
  public Date getEndedTime() {
    return endedTime;
  }
  public void setEndedTime(Date endedTime) {
    this.endedTime = endedTime;
  }
  public int getPersistedRowCount() {
    return persistedRowCount;
  }
  public void setPersistedRowCount(int persistedRowCount) {
    this.persistedRowCount = persistedRowCount;
  }
  public int getVisualizationCount() {
    return visualizationCount;
  }
  public void setVisualizationCount(int visualizationCount) {
    this.visualizationCount = visualizationCount;
  }
  public String getOutput() {
    return output;
  }
  public void setOutput(String output) {
    this.output = output;
  }
}
