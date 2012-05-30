package com.bouncingdata.plfdemo.service;

import com.bouncingdata.plfdemo.datastore.pojo.ExecutionResult;

public interface ApplicationExecutor {
  
  public ExecutionResult executePython(String appname, String code);
  
  public ExecutionResult executeR(String appname, String code);
}
