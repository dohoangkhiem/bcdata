package com.bouncingdata.plfdemo.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
  
  @RequestMapping(value="/", method = RequestMethod.GET)
  public String main(ModelMap model, Principal principal) {
    String username = principal.getName();
    model.addAttribute("username", username);
    return "main";
  }
  
  @RequestMapping(value="/auth/login", method=RequestMethod.GET)
  public String login(ModelMap model) {
    return "login";
  }
  
  @RequestMapping(value="/auth/failed", method=RequestMethod.GET)
  public String failed(ModelMap model) {
    model.addAttribute("error", "true");
    return "login";
  }
  
  @RequestMapping(value="/auth/logout", method = RequestMethod.GET)
  public String logout(ModelMap model) {
    return "login";
  }
}
