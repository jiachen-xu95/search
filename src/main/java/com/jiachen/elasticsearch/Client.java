package com.jiachen.elasticsearch;

import com.jiachen.elasticsearch.service.DocumentService;
import com.jiachen.elasticsearch.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Version 1.0
 * @ClassName Client
 * @Author jiachenXu
 * @Date 2020/4/12 21:58
 * @Description
 */
@Slf4j
@RequestMapping("/client")
@RestController
public class Client {

    @Autowired
    private IndexService indexService;

    @Autowired
    private DocumentService documentService;

    @RequestMapping("/createIndex")
    public Boolean createIndex() {
        try {
            return indexService.createIndex();
        } catch (Exception e) {
            log.error("createIndex error", e);
        }
        return false;
    }

    @RequestMapping("/getIndex")
    public String getIndex() {
        try {
            return indexService.getIndex("user");
        } catch (Exception e) {
            log.error("getIndex error", e);
        }
        return null;
    }

    @RequestMapping("/deleteIndex")
    public Boolean deleteIndex() {
        try {
            return indexService.deleteIndex("user");
        } catch (Exception e) {
            log.error("deleteIndex error", e);
        }
        return false;
    }


}
