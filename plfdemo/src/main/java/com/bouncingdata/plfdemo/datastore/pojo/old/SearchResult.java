package com.bouncingdata.plfdemo.datastore.pojo.old;

import java.util.ArrayList;
import java.util.List;

import com.bouncingdata.plfdemo.datastore.pojo.model.old.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Dataset;
import com.bouncingdata.plfdemo.datastore.pojo.model.old.Datastore;


public class SearchResult {
  private List<Dataset>     datasets;
  private List<Datastore> datastores;
  private List<Application> applications;
  
  public SearchResult() {
    datasets = new ArrayList<Dataset>();
    applications = new ArrayList<Application>();
  }
  
  public List<Dataset> getDatasets() {
    return datasets;
  }
  public void setDatasets(List<Dataset> tables) {
    this.datasets = tables;
  }
  public List<Application> getApplications() {
    return applications;
  }
  public void setApplications(List<Application> applications) {
    this.applications = applications;
  }

  public List<Datastore> getDatastores() {
    return datastores;
  }

  public void setDatastores(List<Datastore> datastores) {
    this.datastores = datastores;
  }
  
  
}
