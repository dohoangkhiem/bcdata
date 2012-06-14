package com.bouncingdata.plfdemo.utils;

import java.io.Serializable;

public class VisualizationSource implements Serializable {
  
  /**
   * 
   */
  private static final long serialVersionUID = 5769531432619867514L;
  
  private String source;
  private VisualizationType type;
  
  public VisualizationSource(String source, VisualizationType type) {
    super();
    this.source = source;
    this.type = type;
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
