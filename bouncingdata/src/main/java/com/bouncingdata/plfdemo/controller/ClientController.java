package com.bouncingdata.plfdemo.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bouncingdata.plfdemo.datastore.pojo.model.User;

/**
 * This controller is about to handle all operations from client library.
 * It exposes REST for retrieve analysis info, code, dataset and also allow to upload analysis 
 *
 */
@Controller
@RequestMapping(value="/client")
public class ClientController {
  
  /**
   * Gets list of analyses, datasets of given user
   * @throws IOException 
   */
  public @ResponseBody Map getList(@RequestParam(value="type", required=false) String type, Principal principal, HttpServletResponse res) throws IOException {
    User user = (User) ((Authentication)principal).getPrincipal();
    if (user == null) {
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized request.");
      return null;
    }
    
    String[] types = new String[] {"all", "analyses", "datasets"};
    if (type == null) type = "all";
    type = type.toLowerCase();
    if (type != null && !Arrays.asList(types).contains(type)) {
      res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown type.");
      return null;
    }
    
    Map results = new LinkedHashMap<String, Object>();
    
    
    return results;
  }
  
  public @ResponseBody void getAnalysisInfo(String guid, Principal principal) {
    
  }
  
  public @ResponseBody void getAnalysisCode(String guid, Principal principal) {
    
  }
  
  public @ResponseBody void getDataset(String guid, Principal principal) {
    
  }
}

