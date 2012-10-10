package com.bouncingdata.plfdemo.datastore.pojo.dto;

import java.util.List;
import java.util.Map;

import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;


public class AnalysisDetail {
  
  String code;
  List<Dataset> datasets;
  Map<String, VisualizationDetail> visualizations;
  Map<String, DashboardPosition> dashboard;
  
  List<Attachment> attachments;
  
  public AnalysisDetail(String code, List<Dataset> datasets, List<Attachment> attachments,
      Map<String, VisualizationDetail> visualizations, Map<String, DashboardPosition> dashboard) {
    super();
    this.code = code;
    this.datasets = datasets;
    this.visualizations = visualizations;
    this.dashboard = dashboard;
    this.attachments = attachments;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public List<Dataset> getDatasets() {
    return datasets;
  }

  public void setDatasets(List<Dataset> datasets) {
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

  public List<Attachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<Attachment> attachments) {
    this.attachments = attachments;
  }
  
  
}
