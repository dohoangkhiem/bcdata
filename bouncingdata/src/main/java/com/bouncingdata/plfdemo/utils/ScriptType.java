package com.bouncingdata.plfdemo.utils;

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
