package com.bouncingdata.plfdemo.service;

import com.bouncingdata.plfdemo.datastore.pojo.ExecutionResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Application;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;

public interface ApplicationExecutor {
  
  public ExecutionResult executePython(Application app, String code, User user);
  
  public ExecutionResult executeR(Application app, String code, User user);
}
