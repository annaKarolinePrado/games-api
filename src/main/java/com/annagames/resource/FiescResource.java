package com.annagames.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class FiescResource {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String get(){
        return "teste";
    }
}
