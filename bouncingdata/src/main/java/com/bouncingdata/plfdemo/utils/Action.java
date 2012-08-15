package com.bouncingdata.plfdemo.utils;

public enum Action {
  PUBLISH("publish"), LIKE("like"), COMMENT("comment"), UPDATE("update");
  
  private String action; 
  private Action(String action) {
    this.action = action;
  }
  
  public String getAction() {
    return action;
  }
}
