package com.dah.camel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(path = "/hello/{me}", produces = "application/json")
    public String hello(@PathVariable(name = "me") String me){
        return "hello " + me + "!";
    }

    @GetMapping(path = "/start", produces = "application/json")
    public String start(){
        return "hello";
    }
}
