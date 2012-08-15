package com.bouncingdata.plfdemo.datastore.pojo.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * This class represents user session on our workspace, it keeps such information like opening tabs, application output, 
 * application variables, application visualizations which was saved from last working time.
 */
public class UserSession implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3305814864052190701L;
  
  private String sessionId;
  private String username;
  private List<Object> openingTabs;
  private Map<String, Object> sessionInfo;
  
  
}
