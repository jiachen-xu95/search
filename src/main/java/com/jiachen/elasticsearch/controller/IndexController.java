package com.jiachen.elasticsearch.controller;

import com.jiachen.elasticsearch.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping("/createAccount")
    public Boolean createAccount() throws IOException {
        return indexService.createAccountIndex();
    }
}
