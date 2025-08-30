package com.evanadev.freelancherbd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

    @GetMapping("/")
    public String Home(){
        return "Hello World";
    }
}
