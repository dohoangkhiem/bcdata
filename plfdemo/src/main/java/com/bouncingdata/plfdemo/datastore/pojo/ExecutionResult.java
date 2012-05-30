package com.bouncingdata.plfdemo.datastore.pojo;

import java.util.List;

public class ExecutionResult {
  private String output;
  private List<String> visualizations;
  
  public ExecutionResult(String output, List<String> visualizations) {
    this.output = output;
    this.visualizations = visualizations;
  }
  public String getOutput() {
    return output;
  }
  public List<String> getVisualizations() {
    return visualizations;
  }
  
}
