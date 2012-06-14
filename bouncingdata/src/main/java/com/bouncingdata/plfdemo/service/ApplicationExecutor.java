package com.bouncingdata.plfdemo.service;

import com.bouncingdata.plfdemo.datastore.pojo.ExecutionResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;

public interface ApplicationExecutor {
  
  public ExecutionResult executePython(Application app, String code, String username);
  
  public ExecutionResult executeR(Application app, String code, String username);
}
