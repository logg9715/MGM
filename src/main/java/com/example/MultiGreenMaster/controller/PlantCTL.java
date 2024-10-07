package com.example.MultiGreenMaster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test-plant")
public class PlantCTL {

    @GetMapping("/input")
    public String inputPage () {
        return "test-plant/input";
    }

    @GetMapping("/admin_plant")
    public String admin_plant() {
        return "test-plant/admin_plant";
    }
}
