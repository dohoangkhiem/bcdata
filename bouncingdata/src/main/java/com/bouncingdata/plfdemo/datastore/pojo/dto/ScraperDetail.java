package com.bouncingdata.plfdemo.datastore.pojo.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Scraper;

public class ScraperDetail {
  
  private String code;
  private Scraper scraper;
  private List<Dataset> datasets;
  
  public ScraperDetail(String code, Scraper scraper, List<Dataset> datasets) {
    this.code = code;
    this.scraper = scraper;
    this.datasets = datasets;
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
  @JsonIgnore
  public Scraper getScraper() {
    return scraper;
  }

  public void setScraper(Scraper scraper) {
    this.scraper = scraper;
  }
}
