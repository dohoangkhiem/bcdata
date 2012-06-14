package com.bouncingdata.plfdemo.datastore.pojo;

import java.util.Map;

import com.bouncingdata.plfdemo.utils.VisualizationSource;

public class ApplicationDetail {
  
  String code;
  Map<String, String> datasets;
  Map<String, VisualizationSource> visualizations;
  
  public ApplicationDetail(String code, Map<String, String> datasets,
      Map<String, VisualizationSource> visualizations) {
    super();
    this.code = code;
    this.datasets = datasets;
    this.visualizations = visualizations;
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

  public Map<String, VisualizationSource> getVisualizations() {
    return visualizations;
  }

  public void setVisualizations(Map<String, VisualizationSource> visualizations) {
    this.visualizations = visualizations;
  }
  
  
}
