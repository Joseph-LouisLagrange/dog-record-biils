package com.darwin.dog.controller;

import com.darwin.dog.exception.BaseExceptionType;
import com.darwin.dog.exception.CommonException;
import com.google.common.collect.Sets;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class PigController {
    @GetMapping("/hello")
    // @PreAuthorize("isAuthenticated()")
    public String hello(HttpSession session){
        return "你好";
    }
}
