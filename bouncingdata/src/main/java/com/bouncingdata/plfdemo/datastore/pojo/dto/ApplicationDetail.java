package com.bouncingdata.plfdemo.datastore.pojo.dto;

import java.util.Map;
import java.util.Properties;


public class ApplicationDetail {
  
  String code;
  Map<String, String> datasets;
  Map<String, VisualizationDetail> visualizations;
  Map<String, DashboardPosition> dashboard;
  
  public ApplicationDetail(String code, Map<String, String> datasets,
      Map<String, VisualizationDetail> visualizations, Map<String, DashboardPosition> dashboard) {
    super();
    this.code = code;
    this.datasets = datasets;
    this.visualizations = visualizations;
    this.dashboard = dashboard;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Map<String, String> getDatasets() {
    return datasets;
  }

  public void setDatasets(Map<String, String> datasets) {
    this.datasets = datasets;
  }

  public Map<String, VisualizationDetail> getVisualizations() {
    return visualizations;
  }

  public void setVisualizations(Map<String, VisualizationDetail> visualizations) {
    this.visualizations = visualizations;
  }

  public Map<String, DashboardPosition> getDashboard() {
    return dashboard;
  }

  public void setDashboard(Map<String, DashboardPosition> dashboard) {
    this.dashboard = dashboard;
  }
  
  
}
