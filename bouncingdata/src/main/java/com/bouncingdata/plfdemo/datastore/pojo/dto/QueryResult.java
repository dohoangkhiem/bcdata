package com.bouncingdata.plfdemo.datastore.pojo.dto;

public class QueryResult {
  private Object data;
  private int statusCode;
  private String message;
  
  public QueryResult(Object data, int statusCode, String message) {
    this.data = data;
    this.statusCode = statusCode;
    this.message = message;
  }
  
  public Object getData() {
    return data;
  }
  public void setData(Object data) {
    this.data = data;
  }
  public int getStatusCode() {
    return statusCode;
  }
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
}
