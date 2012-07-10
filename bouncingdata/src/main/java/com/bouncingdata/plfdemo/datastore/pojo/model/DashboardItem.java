package com.bouncingdata.plfdemo.datastore.pojo.model;

public class DashboardItem {
  private int appId;
  private int vizId;
  private String appGuid;
  private String vizGuid;
  private int x;
  private int y;
  private int w;
  private int h;
  
  public int getAppId() {
    return appId;
  }
  public void setAppId(int appId) {
    this.appId = appId;
  }
  public int getVizId() {
    return vizId;
  }
  public void setVizId(int vizId) {
    this.vizId = vizId;
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
  public String getAppGuid() {
    return appGuid;
  }
  public void setAppGuid(String appGuid) {
    this.appGuid = appGuid;
  }
  public String getVizGuid() {
    return vizGuid;
  }
  public void setVizGuid(String vizGuid) {
    this.vizGuid = vizGuid;
  }
}
