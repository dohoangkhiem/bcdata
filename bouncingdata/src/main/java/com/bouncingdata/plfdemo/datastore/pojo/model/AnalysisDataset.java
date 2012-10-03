package com.bouncingdata.plfdemo.datastore.pojo.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NullValue;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class AnalysisDataset {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
  private int id;
  @Persistent(defaultFetchGroup="true", nullValue=NullValue.NONE, dependent="true")
  private Analysis analysis;
  @Persistent(defaultFetchGroup="true", nullValue=NullValue.NONE, dependent="true")
  private Dataset dataset;
  private boolean isActive;
  
  public AnalysisDataset(Analysis analysis, Dataset dataset, boolean isActive) {
    this.analysis = analysis;
    this.dataset = dataset;
    this.isActive = isActive;
  }
  
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public Analysis getAnalysis() {
    return analysis;
  }
  public void setAnalysis(Analysis analysis) {
    this.analysis = analysis;
  }
  public Dataset getDataset() {
    return dataset;
  }
  public void setDataset(Dataset dataset) {
    this.dataset = dataset;
  }
  public boolean isActive() {
    return isActive;
  }
  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }
}
