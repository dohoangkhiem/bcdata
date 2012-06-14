package com.bouncingdata.plfdemo.datastore.pojo;

import java.util.Map;

import com.bouncingdata.plfdemo.utils.VisualizationSource;

public class ExecutionResult {
  private String output;
  private Map<String, VisualizationSource> visualizations;
  private Map<String, String> datasets;
  private int statusCode;
  private String message;
  
  public ExecutionResult(String output, Map<String, VisualizationSource> visualizations, Map<String, String> datasets, int statusCode, String msg) {
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

  public Map<String, VisualizationSource> getVisualizations() {
    return visualizations;
  }

  public void setVisualizations(Map<String, VisualizationSource> visualizations) {
    this.visualizations = visualizations;
  }

  public Map<String, String> getDatasets() {
    return datasets;
  }

  public void setDatasets(Map<String, String> datasets) {
    this.datasets = datasets;
  }
  
}
