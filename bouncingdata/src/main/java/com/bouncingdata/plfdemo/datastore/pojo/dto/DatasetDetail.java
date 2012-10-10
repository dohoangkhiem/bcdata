package com.bouncingdata.plfdemo.datastore.pojo.dto;


public class DatasetDetail {
  private String guid;
  private String name;
  private int size;
  private String[] columns;
  private String data;
  
  public DatasetDetail(String guid, String name, int size, String[] columns, String data) {
    super();
    this.guid = guid;
    this.name = name;
    this.size = size;
    this.data = data;
    this.columns = columns;
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

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String[] getColumns() {
    return columns;
  }

  public void setColumns(String[] columns) {
    this.columns = columns;
  }
  
  
}
