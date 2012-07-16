package com.bouncingdata.plfdemo.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

public class ApplicationConfigurationAware {
  
  protected Properties appStoreConfiguration;
  
  public void setAppStoreConfiguration(Properties props) {
    this.appStoreConfiguration = props;
  }
}
