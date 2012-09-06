package com.bouncingdata.plfdemo.datastore.pojo.dto;


public class DatasetDetail {
  private String guid;
  private String name;
  private String data;
  
  public DatasetDetail(String guid, String name, String data) {
    super();
    this.guid = guid;
    this.name = name;
    this.data = data;
  }
  
  public String getGuid() {
    return guid;
  }
  public void setGuid(String guid) {
    this.guid = guid;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getData() {
    return data;
  }
  public void setData(String data) {
    this.data = data;
  }
  
  
}
