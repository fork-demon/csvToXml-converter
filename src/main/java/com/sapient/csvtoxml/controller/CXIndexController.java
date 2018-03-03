package com.sapient.csvtoxml.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
//@Controller
//@RequestMapping("/")
public class CXIndexController {
 
      @RequestMapping(method = RequestMethod.GET)
        public String getIndexPage() {
    	     System.out.println("inside index");
            return "test";
        }
 
}