package com.wx.ptwo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/home")
public class HomeCtrl {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

}
