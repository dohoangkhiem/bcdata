package com.bouncingdata.plfdemo.datastore.pojo;

import java.util.List;

public class ExecutionResult {
  private String output;
  private List<String> visualizations;
  private int statusCode;
  private String message;
  
  public ExecutionResult(String output, List<String> visualizations) {
    this.output = output;
    this.visualizations = visualizations;
  }
  public ExecutionResult(String output, List<String> visualizations, int statusCode, String msg) {
    this.output = output;
    this.visualizations = visualizations;
    this.statusCode = statusCode;
    this.message = msg;
  }
  
  public String getOutput() {
    return output;
  }
  public List<String> getVisualizations() {
    return visualizations;
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
