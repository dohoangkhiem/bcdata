package com.bouncingdata.plfdemo.datastore.pojo.dto;

import java.util.ArrayList;
import java.util.List;

import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.Scraper;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;


public class SearchResult {
  private List<Dataset>     datasets;
  private List<Analysis> analyses;
  private List<Scraper> scrapers;
  private List<User> users;
  
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

  public List<Scraper> getScrapers() {
    return scrapers;
  }

  public void setScrapers(List<Scraper> scrapers) {
    this.scrapers = scrapers;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }
  
  
}
