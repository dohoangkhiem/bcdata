package com.bouncingdata.plfdemo.datastore.pojo.dto;

import java.util.ArrayList;
import java.util.List;

import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;


public class SearchResult {
  private List<Dataset>     datasets;
  private List<Analysis> analyses;
  
  public SearchResult() {
    datasets = new ArrayList<Dataset>();
    analyses = new ArrayList<Analysis>();
  }
  
  public List<Dataset> getDatasets() {
    return datasets;
  }
  public void setDatasets(List<Dataset> tables) {
    this.datasets = tables;
  }
  public List<Analysis> getAnalyses() {
    return analyses;
  }
  public void setAnalyses(List<Analysis> applications) {
    this.analyses = applications;
  }
  
  
}
