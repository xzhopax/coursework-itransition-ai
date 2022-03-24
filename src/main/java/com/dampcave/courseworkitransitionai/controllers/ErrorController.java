package com.dampcave.courseworkitransitionai.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
public class ErrorController {

    @GetMapping("/403")
    public String error() {
        return "error/error403";
    }
}
