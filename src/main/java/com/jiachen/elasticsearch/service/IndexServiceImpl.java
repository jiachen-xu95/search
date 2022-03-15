package com.jiachen.elasticsearch.service;

import com.alibaba.fastjson.JSON;
import com.jiachen.elasticsearch.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Version 1.0
 * @ClassName IndexServiceImpl
 * @Author jiachenXu
 * @Date 2020/4/12
 * @Description
 */
@Slf4j
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public Boolean createIndex(String indexName) throws IOException {
        // 设置索引类型（ES 7.0 将不存在索引类型）和 mapping 与 index 配置
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName).settings(createSettings());
        createIndexRequest.mapping("doc", createMapping());
        // RestHighLevelClient 执行创建索引
        return restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT).isAcknowledged();
    }

    @Override
    public Boolean createIndex(String indexName, String settings, String mapping) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        if (null != settings && !"".equals(settings)) {
            request.settings(settings, XContentType.JSON);
        }
        if (null != mapping && !"".equals(mapping)) {
            request.mapping(mapping, XContentType.JSON);
        }
        return restHighLevelClient.indices().create(request, RequestOptions.DEFAULT).isAcknowledged();
    }


    @Override
    public Boolean deleteIndex(String indexName) throws IOException {
        DeleteIndexRequest indexRequest = new DeleteIndexRequest(indexName);
        return restHighLevelClient.indices().delete(indexRequest, RequestOptions.DEFAULT).isAcknowledged();
    }

    @Override
    public Boolean exists(String indexName) throws IOException {
        return restHighLevelClient.exists(new GetRequest(indexName), RequestOptions.DEFAULT);
    }

    @Override
    public String getIndex(String index) throws IOException {
        GetIndexRequest getRequest = new GetIndexRequest(index);
        GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(getRequest, RequestOptions.DEFAULT);
        return JSON.toJSONString(getIndexResponse.getMappings());
    }

    @Override
    public List<UserModel> search(String indexName, String field, String key, String[] keys,
                                  int page, int pageSize, SortOrder sortOrder) throws IOException {
        List<UserModel> result = new ArrayList<>();
        result.addAll(termQuery(indexName, field, key, keys, page, pageSize, sortOrder));
        result.addAll(matchQuery(indexName, field, key, keys, page, pageSize, sortOrder));
        result.addAll(fuzziness(indexName, field, key, keys, page, pageSize, sortOrder));
        return result.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<UserModel> termQuery(String indexName, String field, String key, String[] keys,
                                     int page, int pageSize, SortOrder sortOrder) throws IOException {
        List<UserModel> result = new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        builderQueryConfig(searchSourceBuilder, page, pageSize, sortOrder);

        if (key != null) {
            searchSourceBuilder.query(QueryBuilders.termQuery(field, key));
        } else {
            if (keys.length <= 7) {
                searchSourceBuilder.query(QueryBuilders.termsQuery(field, keys[0], keys[1], keys[2], keys[3], keys[4], keys[5], keys[6]));
            }
        }

        SearchRequest searchRequest = builderSearchRequest(searchSourceBuilder, indexName);

        builderQueryResult(restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT), result);
        return result;
    }

    @Override
    public List<UserModel> matchQuery(String indexName, String field, String key, String[] keys,
                                      int page, int pageSize, SortOrder sortOrder) throws IOException {
        List<UserModel> result = new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        builderQueryConfig(searchSourceBuilder, page, pageSize, sortOrder);
        searchSourceBuilder.query(QueryBuilders.matchPhraseQuery(field, key));

        SearchRequest searchRequest = builderSearchRequest(searchSourceBuilder, indexName);
        SearchResponse searchResponse = clientSearch(searchRequest, RequestOptions.DEFAULT);
        builderQueryResult(searchResponse, result);
        if (CollectionUtils.isEmpty(result)) {
            builderMatchQuery(searchSourceBuilder, indexName, field, key, result);
        } else {
            // Todo 关键词在多个索引词进行匹配待完善
            // builderMultiMatchQuery
        }

        return result;
    }

    @Override
    public List<UserModel> fuzziness(String indexName, String field, String key, String[] keys,
                                     int page, int pageSize, SortOrder sortOrder) throws IOException {
        List<UserModel> result = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        builderQueryConfig(searchSourceBuilder, page, pageSize, sortOrder);
        searchSourceBuilder.query(QueryBuilders.fuzzyQuery(field, key).fuzziness(Fuzziness.AUTO));
        SearchRequest searchRequest = builderSearchRequest(searchSourceBuilder, indexName);
        SearchResponse searchResponse = clientSearch(searchRequest, RequestOptions.DEFAULT);
        builderQueryResult(searchResponse, result);
        return result;
    }

    /**
     * 关键词在多个索引词进行匹配
     *
     * @param searchSourceBuilder 构建器
     * @param indexName           索引名称
     * @param field               索引词
     * @param key                 关键词
     * @return List<UserModel>
     */
//    private List<UserModel> builderMultiMatchQuery(SearchSourceBuilder searchSourceBuilder, String indexName, String field, String key) throws IOException {
//        String mappings = getIndex(indexName);
//        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(key, , field));
//        return null;
//    }

    /**
     * 匹配查询数据
     *
     * @param searchSourceBuilder 构建器
     * @param indexName           索引名称
     * @param field               索引词
     * @param key                 关键词
     * @param result              查询结果
     */
    private void builderMatchQuery(SearchSourceBuilder searchSourceBuilder, String indexName,
                                   String field, String key, List<UserModel> result) throws IOException {
        searchSourceBuilder.query(QueryBuilders.matchQuery(field, "*" + key));
        SearchRequest searchRequest = builderSearchRequest(searchSourceBuilder, indexName);
        SearchResponse searchResponse = clientSearch(searchRequest, RequestOptions.DEFAULT);
        builderQueryResult(searchResponse, result);
    }

    /**
     * 查询请求
     *
     * @param searchSourceBuilder 构建器
     * @param indexName           索引名称
     * @return SearchRequest
     */
    private SearchRequest builderSearchRequest(SearchSourceBuilder searchSourceBuilder, String indexName) {
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    /**
     * 查询配置
     *
     * @param searchSourceBuilder searchSourceBuilder
     * @param page                当前页
     * @param pageSize            页长度
     * @param sortOrder           倒序\正序
     */
    private void builderQueryConfig(SearchSourceBuilder searchSourceBuilder, int page, int pageSize, SortOrder sortOrder) {
        searchSourceBuilder.from(page);
        searchSourceBuilder.size(pageSize);
        searchSourceBuilder.sort("salary", sortOrder);
    }

    /**
     * 查询封装
     *
     * @param searchRequest  searchRequest
     * @param requestOptions RequestOptions
     * @return SearchResponse
     */
    private SearchResponse clientSearch(SearchRequest searchRequest, RequestOptions requestOptions) throws IOException {
        return restHighLevelClient.search(searchRequest, requestOptions);
    }

    /**
     * 查询结果封装
     *
     * @param searchResponse 封装结果
     * @param result         查询结果
     */
    private void builderQueryResult(SearchResponse searchResponse, List<UserModel> result) {
        if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getTotalShards() > 0) {
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hits1 : hits) {
                result.add(JSON.parseObject(hits1.getSourceAsString(), UserModel.class));
            }
        }
    }

    /**
     * user索引
     *
     * @return
     * @throws IOException
     */
    private XContentBuilder createMapping() throws IOException {
        return XContentFactory.jsonBuilder()
                .startObject()
                .field("dynamic", true)
                .startObject("properties")

                .startObject("name")
                .field("type", "text")
                .startObject("fields")
                .startObject("keyword")
                .field("type", "keyword")
                .endObject()
                .endObject()
                .endObject()

                .startObject("address")
                .field("type", "text")
                .startObject("fields")
                .startObject("keyword")
                .field("type", "keyword")
                .endObject()
                .endObject()
                .endObject()

                .startObject("remark")
                .field("type", "text")
                .startObject("fields")
                .startObject("keyword")
                .field("type", "keyword")
                .endObject()
                .endObject()
                .endObject()

                .startObject("age")
                .field("type", "integer")
                .endObject()

                .startObject("salary")
                .field("type", "float")
                .endObject()

                .startObject("birthDate")
                .field("type", "date")
                .field("format", "yyyy-MM-dd")
                .endObject()

                .startObject("createTime")
                .field("type", "date")
                .endObject()

                .endObject()
                .endObject();
    }

    /**
     * 创建索引配置
     *
     * @return
     */
    private Settings createSettings() {
        return Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
                .build();
    }

}
