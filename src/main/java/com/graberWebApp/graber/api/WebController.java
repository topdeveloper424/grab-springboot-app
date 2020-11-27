package com.graberWebApp.graber.api;

import com.graberWebApp.graber.service.GrabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
    private final GrabService grabService;

    @Autowired
    public WebController(GrabService grabService) {
        this.grabService = grabService;
    }


    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("grabs", grabService.getAllGrabData());

        return "home";
    }

}
