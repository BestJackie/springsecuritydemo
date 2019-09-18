package com.example.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * FileName: ExampleController
 * Author:   haichaoyang3
 * Date:     2019/9/18 9:54
 * Description:demo
 * History:
 * since: 1.0.0
 */
@RestController
public class ExampleController {

    @GetMapping("helloworld")
    public List<String> helloworld(){
        return Arrays.asList("Spring Security simple demo");
    }
}
