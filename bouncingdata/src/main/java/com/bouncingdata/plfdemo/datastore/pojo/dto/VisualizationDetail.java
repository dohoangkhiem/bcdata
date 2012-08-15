package com.bouncingdata.plfdemo.datastore.pojo.dto;

import java.io.Serializable;


public class VisualizationDetail implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 5769531432619867514L;
  
  private String source;
  private VisualizationType type;
  private String guid;
  
  public VisualizationDetail(String guid, String source, VisualizationType type) {
    super();
    this.source = source;
    this.type = type;
    this.guid = guid;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public VisualizationType getType() {
    return type;
  }

  public void setType(VisualizationType type) {
    this.type = type;
  }
} 
