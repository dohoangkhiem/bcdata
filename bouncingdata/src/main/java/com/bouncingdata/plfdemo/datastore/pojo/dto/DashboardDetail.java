package com.bouncingdata.plfdemo.datastore.pojo.dto;

import java.util.Map;

public class DashboardDetail {
  Map<String, VisualizationDetail> visualizations;
  Map<String, DashboardPosition> dashboard;
  
  public DashboardDetail(Map<String, VisualizationDetail> visualizations,
      Map<String, DashboardPosition> dashboard) {
    super();
    this.visualizations = visualizations;
    this.dashboard = dashboard;
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
