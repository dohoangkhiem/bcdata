package com.bouncingdata.plfdemo.utils;

public enum VisualizationType {
  PNG("png"), HTML("html");
  
  private String type;
  private VisualizationType(String t) {
    type = t;
  }

  public String getType() {
    return type;
  }
  
  public String toString() {
    return type;
  }
}
