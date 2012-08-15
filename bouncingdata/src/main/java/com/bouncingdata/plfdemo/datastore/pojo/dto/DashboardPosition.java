package com.bouncingdata.plfdemo.datastore.pojo.dto;

import java.io.Serializable;

public class DashboardPosition implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -6893319914166424373L;
  
  String guid;
  int x;
  int y;
  int w;
  int h;
  
  public DashboardPosition(String guid, int x, int y, int w, int h) {
    this.guid = guid;
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getW() {
    return w;
  }

  public void setW(int w) {
    this.w = w;
  }

  public int getH() {
    return h;
  }

  public void setH(int h) {
    this.h = h;
  }
}
