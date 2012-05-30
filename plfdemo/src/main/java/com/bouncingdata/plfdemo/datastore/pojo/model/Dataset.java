package com.bouncingdata.plfdemo.datastore.pojo.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Dataset {
  @PrimaryKey
  @Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
  private int id;
  private String name;
  private String description;
  private String datastore;
  private String fieldList;
  
  public Dataset(String name, String description, String datastore, String fieldList) {
    super();
    this.name = name;
    this.description = description;
    this.datastore = datastore;
    this.fieldList = fieldList;
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
  public String getDatastore() {
    return datastore;
  }
  public void setDatastore(String datastore) {
    this.datastore = datastore;
  }
  public String getFieldList() {
    return fieldList;
  }
  public void setFieldList(String fieldList) {
    this.fieldList = fieldList;
  }
  
}
