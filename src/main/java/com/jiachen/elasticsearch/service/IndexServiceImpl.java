package com.jiachen.elasticsearch.service;

import com.alibaba.fastjson.JSONObject;
import com.jiachen.elasticsearch.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
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
    public Boolean createAccountIndex() throws IOException {
        return createIndex("t_account", getAccountSetting(), getMapping());
    }

    private Map<String, Object> getMapping() {
        Map<String, Object> mappingMap = new HashMap<>(1);
        Map<String, Object> propertiesMap = new HashMap<>();
        Map<String, Object> accountIdMap = new HashMap<>(1);
        accountIdMap.put("type", "keyword");
        propertiesMap.put("accountId", accountIdMap);
        Map<String, Object> displayNameMap = new HashMap<>(3);
        displayNameMap.put("type", "text");
        displayNameMap.put("analyzer", "ik_max_word");
        displayNameMap.put("fields", getFields());
        propertiesMap.put("displayName", displayNameMap);
        Map<String, Object> weightMap = new HashMap<>(1);
        weightMap.put("type", "integer");
        propertiesMap.put("weight", weightMap);
        Map<String, Object> tenantIdMap = new HashMap<>(1);
        tenantIdMap.put("type", "keyword");
        propertiesMap.put("tenantId", tenantIdMap);
        Map<String, Object> accountMap = new HashMap<>(3);
        accountMap.put("type", "text");
        accountMap.put("analyzer", "ik_max_word");
        accountMap.put("fields", getFields());
        propertiesMap.put("account", accountMap);
        Map<String, Object> createAtMap = new HashMap<>(2);
        createAtMap.put("type", "date");
        createAtMap.put("format", "yyyy-MM-dd HH:mm:ss| yyyy-MM-dd || epoch_millis");
        propertiesMap.put("createAt", createAtMap);
        mappingMap.put("properties", propertiesMap);
        log.info("mapping={}", JSONObject.toJSONString(mappingMap));
        return mappingMap;
    }

    private Map<String, Object> getAccountAnalysis() {
        Map<String, Object> analysisMap = new HashMap<>(2);
        Map<String, Object> analyzerMap = new HashMap<>(2);
        Map<String, Object> defaultMap = new HashMap<>(1);
        defaultMap.put("tokenizer", "ik_max_word");
        analyzerMap.put("default", defaultMap);
        Map<String, Object> pinyinAnalyzerMap = new HashMap<>(3);
        pinyinAnalyzerMap.put("type", "custom");
        pinyinAnalyzerMap.put("tokenizer", "my_pinyin");
        pinyinAnalyzerMap.put("filter", new String[]{"word_delimiter"});
        analyzerMap.put("pinyin_analyzer", pinyinAnalyzerMap);
        analysisMap.put("analyzer", analyzerMap);
        Map<String, Object> tokenizerMap = new HashMap<>(1);
        Map<String, Object> myPinyinMap = new HashMap<>(7);
        myPinyinMap.put("type", "pinyin");
        myPinyinMap.put("keep_first_letter", true);
        myPinyinMap.put("keep_separate_first_letter", false);
        myPinyinMap.put("keep_full_pinyin", true);
        myPinyinMap.put("keep_original", false);
        myPinyinMap.put("limit_first_letter_length", 16);
        myPinyinMap.put("lowercase", true);
        tokenizerMap.put("my_pinyin", myPinyinMap);
        analysisMap.put("tokenizer", tokenizerMap);
        return analysisMap;
    }

    private Map<String, Object> getAccountSetting() {
        Map<String, Object> settingMap = new HashMap<>(1);
        settingMap.put("number_of_shards", 1);
        settingMap.put("number_of_replicas", 1);
        settingMap.put("analysis", getAccountAnalysis());
        log.info("settings={}", JSONObject.toJSONString(settingMap));
        return settingMap;
    }

    private Map<String, Object> getFields() {
        Map<String, Object> fieldsMap = new HashMap<>(1);
        Map<String, Object> fieldsPinyinMap = new HashMap<>(1);
        fieldsPinyinMap.put("type", "text");
        fieldsPinyinMap.put("term_vector", "with_positions_offsets");
        fieldsPinyinMap.put("analyzer", "pinyin_analyzer");
        fieldsPinyinMap.put("boost", 10);
        fieldsMap.put("pinyin", fieldsPinyinMap);
        return fieldsMap;
    }

    @Override
    public Boolean createIndex(String indexName, Map<String, Object> settings, Map<String, Object> mapping) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        createIndexRequest.settings(settings);
        createIndexRequest.mapping(mapping);
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }

    @Override
    public Boolean deleteIndex(String indexName) throws IOException {
        AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
        return acknowledgedResponse.isAcknowledged();
    }

    @Override
    public Boolean clearIndex(String indexName) throws IOException {
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(indexName);
        deleteByQueryRequest.setQuery(QueryBuilders.matchAllQuery());
        restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
        return Boolean.TRUE;
    }

    @Override
    public Boolean exists(String indexName) throws IOException {
        return restHighLevelClient.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
    }

    @Override
    public List<UserModel> search(String indexName, String field, String key, String[] keys, int page, int pageSize, SortOrder sortOrder) throws IOException {
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.multiMatchQuery(key, keys));
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.sort("id", sortOrder);
        searchSourceBuilder.from(page).size(pageSize);
        SearchRequest searchRequest = new SearchRequest(indexName).types("_doc").source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        log.info("查询结果：{} ", searchResponse.status().toString());
        log.info("请求语句:{} ", searchRequest.source().toString());
        return convertResult(searchResponse.getHits().getHits(), UserModel.class);
    }

    @Override
    public List<UserModel> termQuery(String indexName, String field, String key, String value, int page, int pageSize, SortOrder sortOrder) throws IOException {
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery(field, value));
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.sort("id", sortOrder);
        searchSourceBuilder.from(page).size(pageSize);
        SearchRequest searchRequest = new SearchRequest(indexName).types("_doc").source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        log.info("查询结果：{} ", searchResponse.status().toString());
        log.info("请求语句:{} ", searchRequest.source().toString());
        return convertResult(searchResponse.getHits().getHits(), UserModel.class);
    }

    @Override
    public List<UserModel> fuzziness(String indexName, String field, String key, String value, int page, int pageSize, SortOrder sortOrder) throws IOException {
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.fuzzyQuery(field, value));
        SearchRequest searchRequest = new SearchRequest(indexName).types("_doc").source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        log.info("查询结果：{} ", searchResponse.status().toString());
        log.info("请求语句:{} ", searchRequest.source().toString());
        return convertResult(searchResponse.getHits().getHits(), UserModel.class);
    }

    private <T> List<T> convertResult(SearchHit[] searchHits, Class<T> tClass) {
        return Arrays.stream(searchHits)
                .filter(Objects::nonNull)
                .map(hit -> JSONObject.parseObject(hit.getSourceAsString(), tClass))
                .collect(Collectors.toList());
    }
}
