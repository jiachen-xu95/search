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

    @GetMapping("/exists")
    public Boolean exists(String indexName) throws IOException {
        return indexService.exists(indexName);
    }

    @GetMapping("/delete")
    public Boolean delete(String indexName) throws IOException {
        return indexService.deleteIndex(indexName);
    }

    @GetMapping("/clear")
    public Boolean clear(String indexName) throws IOException {
        return indexService.clearIndex(indexName);
    }
}
