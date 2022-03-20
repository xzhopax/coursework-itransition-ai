package com.dampcave.courseworkitransitionai.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/403")
    public String error() {
        return "errors/error403";
    }
}
