package khiem.dataprj.demo.plfdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/welcome")
public class WelcomeController {
  
  @RequestMapping(method=RequestMethod.GET)
  public String welcome(ModelMap model) {
    model.addAttribute("msg", "Welcome to our page");
    return "welcome";
  }
}
