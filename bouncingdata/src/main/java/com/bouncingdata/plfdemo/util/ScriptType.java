package com.bouncingdata.plfdemo.util;

public enum ScriptType {
  ANALYSIS("analysis"), SCRAPER("scraper");
  
  private String type;
  private ScriptType(String type) {
    this.type = type;
  }
  
  public String getType() {
    return type;
  }
}
