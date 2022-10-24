package com.jiachen.elasticsearch.controller;

import com.jiachen.elasticsearch.dao.object.GoodDO;
import com.jiachen.elasticsearch.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/good")
public class GoodController {

    @Autowired
    private GoodService goodService;

    @GetMapping(value = "/init")
    public void init() {
        goodService.init();
    }

    @GetMapping(value = "/search")
    public List<GoodDO> search(@RequestParam("title") String title) throws IOException {
        return goodService.searchName(title);
    }
}
