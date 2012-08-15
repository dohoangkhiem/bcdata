package com.bouncingdata.plfdemo.datastore.pojo.dto;

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
  
  public static VisualizationType getVisualType(String t) {
    if ("png".equals(t)) return PNG;
    else if ("html".equals(t)) return HTML;
    else return null;
  }
}
