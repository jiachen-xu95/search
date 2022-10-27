package com.jiachen.elasticsearch.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiachen.elasticsearch.dao.GoodDAO;
import com.jiachen.elasticsearch.dao.object.GoodDO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private GoodDAO goodDAO;

    @Override
    public List<GoodDO> searchName(String name) throws IOException {
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.multiMatchQuery(name, "title", "title.pinyin"));
        searchSourceBuilder.query(boolQueryBuilder);
        SearchRequest searchRequest = new SearchRequest("good");
        searchRequest.source(searchSourceBuilder);
        searchRequest.types("doc");
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return Arrays.stream(searchResponse.getHits().getHits())
                .map(x -> JSON.parseObject(x.getSourceAsString(), GoodDO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void init() {
        int limit = 500;
        long startId = 0L;
        try {
            Integer count = goodDAO.count();
            for (int pageNo = 0; pageNo <= (count / limit); pageNo++) {
                GoodDO goodDO = new GoodDO();
                goodDO.setId(startId);
                goodDO.setLimit(limit);
                List<GoodDO> goodDOList = goodDAO.query(goodDO);
                if (CollectionUtils.isEmpty(goodDOList)) {
                    log.info("init data break");
                    break;
                }
                BulkRequest bulkRequest = new BulkRequest();
                for (GoodDO good : goodDOList) {
                    IndexRequest indexRequest = new IndexRequest("good");
                    indexRequest.id(good.getId() + "").type("doc").source(JSONObject.toJSONString(good), XContentType.JSON);
                    bulkRequest.add(indexRequest);
                }
                BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
                log.info(bulk.status().toString());
            }

        } catch (Exception e) {

        }
    }

    private <T> List<T> convertResult(SearchHit[] searchHits, Class<T> tClass) {
        return Arrays.stream(searchHits)
                .filter(Objects::nonNull)
                .map(hit -> JSONObject.parseObject(hit.getSourceAsString(), tClass))
                .collect(Collectors.toList());
    }
}
