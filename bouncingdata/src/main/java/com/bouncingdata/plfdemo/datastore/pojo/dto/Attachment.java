package com.bouncingdata.plfdemo.datastore.pojo.dto;

public class Attachment {
  private int id;
  private String name;
  private String description;
  private String data;
  
  public Attachment(int id, String name, String description, String data) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.data = data;
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
  public String getData() {
    return data;
  }
  public void setData(String data) {
    this.data = data;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  
}
