package com.bouncingdata.plfdemo.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bouncingdata.plfdemo.datastore.pojo.dto.RegisterResult;
import com.bouncingdata.plfdemo.datastore.pojo.model.User;
import com.bouncingdata.plfdemo.service.DatastoreService;

@Controller
public class LoginController {
  
  private Logger logger = LoggerFactory.getLogger(LoginController.class);
  
  @Autowired
  private DatastoreService datastoreService;
        
  @RequestMapping(value={"/new", "/test", "/profile"}, method = RequestMethod.GET)
  public String newVersion(ModelMap model, Principal principal) {
    return "test";
  }
  
  @RequestMapping(value="/create", method = RequestMethod.GET)
  public String openCreate() {
    return "create";
  }
  
  @RequestMapping(value="/help", method = RequestMethod.GET)
  public String openHelp() {
    return "help";
  }
    
  @RequestMapping(value="/auth", method=RequestMethod.GET)
  public String gologin(ModelMap model) {
    model.addAttribute("mode", "login");
    return "redirect:/auth/login";
  }
  
  @RequestMapping(value="/auth/login", method=RequestMethod.GET)
  public String login(ModelMap model) {
    model.addAttribute("mode", "login");
    return "login";
  }
  
  @RequestMapping(value="/auth/failed", method=RequestMethod.GET)
  public String failed(ModelMap model) {
    model.addAttribute("mode", "login");
    model.addAttribute("error", "true");
    return "login";
  }
  
  @RequestMapping(value="/auth/logout", method = RequestMethod.GET)
  public String logout(ModelMap model) {
    model.addAttribute("mode", "login");
    return "login";
  }
  
  @RequestMapping(value="/auth/register", method = RequestMethod.GET)
  public String getRegisterPage(ModelMap model) {
    model.addAttribute("mode", "register");
    model.addAttribute("regResult", null);
    return "login";
  }
  
  @RequestMapping(value="/auth/register", method = RequestMethod.POST)
  public String register(@RequestParam(value="username", required=true) String username, 
      @RequestParam(value="password", required=true) String password, 
      @RequestParam(value="email", required=true) String email, 
      @RequestParam(value="firstName", required=false) String firstName, 
      @RequestParam(value="lastName", required=false) String lastName, ModelMap model) {
    
    RegisterResult result = new RegisterResult();
    result.setUsername(username);
    result.setEmail(email);
    model.addAttribute("mode", "register");
    // validate 
    if (username == null || username.length() < 4 
        || password == null || password.length() < 6
        || email == null) {
      result.setMessage("Your input data is invalid. Please check and try again.");
      result.setStatusCode(-1);     
      model.addAttribute("regResult", result);
      return "login";
    }
    
    // do business logic to create account
    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    
    try {
      datastoreService.createUser(user);
      result.setStatusCode(0);
      result.setMessage("Successfully create user " + user.getUsername());
      model.addAttribute("regResult", result);
    } catch (Exception e) {
      if (logger.isDebugEnabled()) logger.debug("Failed to create new user " + username, e);
      result.setStatusCode(-2);
      result.setMessage(e.getMessage());
      model.addAttribute("regResult", result);
    }
    
    return "login";
  }
}
