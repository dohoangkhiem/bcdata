package com.bouncingdata.plfdemo.util;

public enum Action {
  PUBLISH("publish"), LIKE("like"), COMMENT("comment"), UPDATE("update"), UNPUBLISH("unpublish");
  
  private String action; 
  private Action(String action) {
    this.action = action;
  }
  
  public String getAction() {
    return action;
  }
}
