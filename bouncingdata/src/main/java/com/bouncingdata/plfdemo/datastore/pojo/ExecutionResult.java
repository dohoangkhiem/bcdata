package com.bouncingdata.plfdemo.datastore.pojo;

import java.util.Map;


public class ExecutionResult {
  private String output;
  private Map<String, VisualizationDetail> visualizations;
  private Map<String, String> datasets;
  private int statusCode;
  private String message;
  
  public ExecutionResult(String output, Map<String, VisualizationDetail> visualizations, Map<String, String> datasets, int statusCode, String msg) {
    this.output = output;
    this.visualizations = visualizations;
    this.statusCode = statusCode;
    this.message = msg;
    this.datasets = datasets;
  }
  
  public String getOutput() {
    return output;
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

  public Map<String, VisualizationDetail> getVisualizations() {
    return visualizations;
  }

  public void setVisualizations(Map<String, VisualizationDetail> visualizations) {
    this.visualizations = visualizations;
  }

  public Map<String, String> getDatasets() {
    return datasets;
  }

  public void setDatasets(Map<String, String> datasets) {
    this.datasets = datasets;
  }
  
}
