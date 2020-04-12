package com.jiachen.elasticsearch.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Version 1.0
 * @ClassName IndexServiceImpl
 * @Author jiachenXu
 * @Date 2020/4/12 21:14
 * @Description
 */
@Slf4j
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public Boolean createIndex() throws IOException {
        // 设置索引类型（ES 7.0 将不存在索引类型）和 mapping 与 index 配置
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("user").settings(createSettings( ));
        createIndexRequest.mapping("doc", createMapping( ));
        // RestHighLevelClient 执行创建索引
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices( ).create(createIndexRequest, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged( );
    }

    @Override
    public Boolean deleteIndex() throws IOException {
        DeleteIndexRequest indexRequest = new DeleteIndexRequest("user");
        AcknowledgedResponse response = restHighLevelClient.indices( ).delete(indexRequest, RequestOptions.DEFAULT);
        return response.isAcknowledged( );
    }

    @Override
    public String getIndex(String index) throws IOException {
        GetIndexRequest getRequest = new GetIndexRequest(index);
        GetIndexResponse getIndexResponse = restHighLevelClient.indices( ).get(getRequest, RequestOptions.DEFAULT);
        return JSON.toJSONString(getIndexResponse.getMappings( ));
    }

    /**
     * user索引
     *
     * @return
     * @throws IOException
     */
    private XContentBuilder createMapping() throws IOException {
        return XContentFactory.jsonBuilder( )
                .startObject( )
                .field("dynamic", true)
                .startObject("properties")

                .startObject("name")
                .field("type", "text")
                .startObject("fields")
                .startObject("keyword")
                .field("type", "keyword")
                .endObject( )
                .endObject( )
                .endObject( )

                .startObject("address")
                .field("type", "text")
                .startObject("fields")
                .startObject("keyword")
                .field("type", "keyword")
                .endObject( )
                .endObject( )
                .endObject( )

                .startObject("remark")
                .field("type", "text")
                .startObject("fields")
                .startObject("keyword")
                .field("type", "keyword")
                .endObject( )
                .endObject( )
                .endObject( )

                .startObject("age")
                .field("type", "integer")
                .endObject( )

                .startObject("salary")
                .field("type", "float")
                .endObject( )

                .startObject("birthDate")
                .field("type", "date")
                .field("format", "yyyy-MM-dd")
                .endObject( )

                .startObject("createTime")
                .field("type", "date")
                .endObject( )

                .endObject( )
                .endObject( );
    }

    /**
     * 创建索引配置
     *
     * @return
     */
    private Settings createSettings() {
        return Settings.builder( )
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
                .build( );
    }

}
