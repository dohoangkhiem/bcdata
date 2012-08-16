package com.bouncingdata.plfdemo.service;

import com.bouncingdata.plfdemo.datastore.pojo.dto.ExecutionResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;

public interface ApplicationExecutor {
  
  public ExecutionResult executePython(Analysis analysis, String code, User user);
  
  public ExecutionResult executeR(Analysis analysis, String code, User user);
}
