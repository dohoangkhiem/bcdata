package com.bouncingdata.plfdemo.datastore.pojo.model.old;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Datastore {
  
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
  private int id;
  private String name;
  private String description;
  @NotPersistent private List<Dataset> datasets;

  public Datastore(String name, String description) {
    this.name = name;
    this.description = description;
  }
  
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
  public List<Dataset> getDatasets() {
    return datasets;
  }
  public void setDatasets(List<Dataset> datasets) {
    this.datasets = datasets;
  }
}
