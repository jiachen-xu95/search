package com.jiachen.elasticsearch.service;

import com.alibaba.fastjson.JSON;
import com.jiachen.elasticsearch.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * @Version 1.0
 * @ClassName DocumentServiceImpl
 * @Author jiachenXu
 * @Date 2020/4/12 21:46
 * @Description 文档业务
 */
@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void addDocument() throws IOException {
        // id 1
        IndexRequest indexRequest = new IndexRequest("user", "doc", "1");
        UserModel userInfo = new UserModel();
        userInfo.setName("张三");
        userInfo.setAge(29);
        userInfo.setSalary(100.00f);
        userInfo.setAddress("北京市");
        userInfo.setRemark("来自北京市的张先生");
        userInfo.setCreateDate(new Date());
        userInfo.setBirthDate("1990-01-10");
        byte[] json = JSON.toJSONBytes(userInfo);
        // 设置文档内容
        indexRequest.source(json, XContentType.JSON);
        // 执行增加文档
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

}


