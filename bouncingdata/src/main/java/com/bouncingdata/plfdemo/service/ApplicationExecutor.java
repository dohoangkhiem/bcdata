package com.bouncingdata.plfdemo.service;

import com.bouncingdata.plfdemo.datastore.pojo.dto.ExecutionResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.Analysis;
import com.bouncingdata.plfdemo.datastore.pojo.model.BcDataScript;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;

public interface ApplicationExecutor {
  
  public ExecutionResult executePython(BcDataScript script, String code, User user) throws Exception;
  
  public ExecutionResult executeR(BcDataScript script, String code, User user) throws Exception;
}
