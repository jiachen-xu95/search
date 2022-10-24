package com.jiachen.elasticsearch.service;

import com.jiachen.elasticsearch.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

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
    @Override
    public Boolean createIndex(String indexName) throws IOException {
        return null;
    }

    @Override
    public Boolean createIndex(String indexName, Settings settings, String mapping) throws IOException {
        return null;
    }

    @Override
    public Boolean deleteIndex(String indexName) throws IOException {
        return null;
    }

    @Override
    public Boolean exists(String indexName) throws IOException {
        return null;
    }

    @Override
    public String getIndex(String index) throws IOException {
        return null;
    }

    @Override
    public List<UserModel> queryByKey(String keyWord) {
        return null;
    }

    @Override
    public List<UserModel> search(String indexName, String field, String key, String[] keys, int page, int pageSize, SortOrder sortOrder) throws IOException {
        return null;
    }

    @Override
    public List<UserModel> termQuery(String indexName, String field, String key, String[] keys, int page, int pageSize, SortOrder sortOrder) throws IOException {
        return null;
    }

    @Override
    public List<UserModel> fuzziness(String indexName, String field, String key, String[] keys, int page, int pageSize, SortOrder sortOrder) throws IOException {
        return null;
    }
//    @Autowired
//    private RestHighLevelClient restHighLevelClient;
//
//    private SearchResponse getSearchResponse(BoolQueryBuilder boolQueryBuilder, Integer pageNo, Integer pageSize, String sortField, String indexName, Boolean distinct) throws IOException {
//        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
//        searchSourceBuilder.query(boolQueryBuilder).sort(sortField, SortOrder.DESC).from(pageNo).size(pageSize);
//        if (distinct) {
//            searchSourceBuilder.collapse(new CollapseBuilder(sortField));
//        }
//        SearchRequest searchRequest = new SearchRequest(indexName).source(searchSourceBuilder);
//        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//    }
//
//    private long distinctCount(BoolQueryBuilder boolQueryBuilder, String indexName, String field) throws IOException {
//        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
//        searchSourceBuilder.query(boolQueryBuilder).aggregation(AggregationBuilders.cardinality(field).field(field));
//        SearchRequest searchRequest = new SearchRequest(indexName).source(searchSourceBuilder);
//        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        ParsedCardinality aggregation = searchResponse.getAggregations().get(field);
//        return aggregation.getValue();
//    }
//
//    private void queryRangeTime(String startTime, String endTime, BoolQueryBuilder boolQueryBuilder, String timeField) {
//        boolQueryBuilder.must(QueryBuilders.rangeQuery(timeField).format(startTime).to(endTime));
//    }
//
//    @Override
//    public Boolean createIndex(String indexName) {
//        try {
//            Settings settings = Settings.builder()
//                    .put("index.number_of_shards", 1)
//                    .put("index.number_of_replicas", 1)
//                    .build();
//            String mapping = "{\"properties\":{\"author\":{\"type\":\"text\"},\n" +
//                    "\"content\":{\"type\":\"text\",\"analyzer\":\"ik_max_word\",\"search_analyzer\":\"ik_smart\"}\n" +
//                    ",\"title\":{\"type\":\"text\",\"analyzer\":\"ik_max_word\",\"search_analyzer\":\"ik_smart\"}\n" +
//                    ",\"createDate\":{\"type\":\"date\",\"format\":\"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd\"}\n" +
//                    "},\"url\":{\"type\":\"text\"}\n" +
//                    "}";
//            return createIndex(indexName, settings, mapping);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    @Override
//    public Boolean createIndex(String indexName, Settings settings, String mapping) {
//        try {
//            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
//            if (null != settings) {
//                createIndexRequest.settings(settings);
//            }
//            if (null != mapping && !"".equals(mapping)) {
//                createIndexRequest.mapping(mapping, XContentType.JSON);
//            }
//            return restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT).isAcknowledged();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    @Override
//    public Boolean deleteIndex(String indexName) throws IOException {
//        DeleteIndexRequest indexRequest = new DeleteIndexRequest(indexName);
//        return restHighLevelClient.indices().delete(indexRequest, RequestOptions.DEFAULT).isAcknowledged();
//    }
//
//    @Override
//    public Boolean exists(String indexName) throws IOException {
//        return restHighLevelClient.exists(new GetRequest(indexName), RequestOptions.DEFAULT);
//    }
//
//    @Override
//    public String getIndex(String index) throws IOException {
//        GetIndexRequest getRequest = new GetIndexRequest(index);
//        GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(getRequest, RequestOptions.DEFAULT);
//        return JSON.toJSONString(getIndexResponse.getMappings());
//    }
//
//    @Override
//    public List<UserModel> queryByKey(String keyWord) {
//        List<UserModel> result = new ArrayList<>();
//        try {
//            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyWord, "name", "address", "salary", "age", "remark", "birthDate"));
//            SearchRequest searchRequest = builderSearchRequest(searchSourceBuilder);
//            SearchResponse searchResponse = clientSearch(searchRequest, RequestOptions.DEFAULT);
//            builderQueryResult(searchResponse, result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    @Override
//    public List<UserModel> search(String indexName, String field, String key, String[] keys,
//                                  int page, int pageSize, SortOrder sortOrder) throws IOException {
//        List<UserModel> result = new ArrayList<>();
//        result.addAll(termQuery(indexName, field, key, keys, page, pageSize, sortOrder));
//        result.addAll(fuzziness(indexName, field, key, keys, page, pageSize, sortOrder));
//        return result.stream().distinct().collect(Collectors.toList());
//    }
//
//    @Override
//    public List<UserModel> termQuery(String indexName, String field, String key, String[] keys,
//                                     int page, int pageSize, SortOrder sortOrder) throws IOException {
//        List<UserModel> result = new ArrayList<>();
//
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        builderQueryConfig(searchSourceBuilder, page, pageSize, sortOrder);
//
//        if (key != null) {
//            searchSourceBuilder.query(QueryBuilders.termQuery(field, key));
//        } else {
//            if (keys.length <= 7) {
//                searchSourceBuilder.query(QueryBuilders.termsQuery(field, keys[0], keys[1], keys[2], keys[3], keys[4], keys[5], keys[6]));
//            }
//        }
//
//        SearchRequest searchRequest = builderSearchRequest(searchSourceBuilder, indexName);
//
//        builderQueryResult(restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT), result);
//        return result;
//    }
//
//    @Override
//    public List<UserModel> fuzziness(String indexName, String field, String key, String[] keys,
//                                     int page, int pageSize, SortOrder sortOrder) throws IOException {
//        List<UserModel> result = new ArrayList<>();
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        builderQueryConfig(searchSourceBuilder, page, pageSize, sortOrder);
//        searchSourceBuilder.query(QueryBuilders.fuzzyQuery(field, key).fuzziness(Fuzziness.AUTO));
//        SearchRequest searchRequest = builderSearchRequest(searchSourceBuilder, indexName);
//        SearchResponse searchResponse = clientSearch(searchRequest, RequestOptions.DEFAULT);
//        builderQueryResult(searchResponse, result);
//        return result;
//    }
//
//    /**
//     * 查询请求
//     *
//     * @param searchSourceBuilder 构建器
//     * @return SearchRequest
//     */
//    private SearchRequest builderSearchRequest(SearchSourceBuilder searchSourceBuilder) {
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.source(searchSourceBuilder);
//        return searchRequest;
//    }
//
//    /**
//     * 查询请求
//     *
//     * @param searchSourceBuilder 构建器
//     * @param indexName           索引名称
//     * @return SearchRequest
//     */
//    private SearchRequest builderSearchRequest(SearchSourceBuilder searchSourceBuilder, String indexName) {
//        SearchRequest searchRequest = new SearchRequest(indexName);
//        searchRequest.source(searchSourceBuilder);
//        return searchRequest;
//    }
//
//    /**
//     * 查询配置
//     *
//     * @param searchSourceBuilder searchSourceBuilder
//     * @param page                当前页
//     * @param pageSize            页长度
//     * @param sortOrder           倒序\正序
//     */
//    private void builderQueryConfig(SearchSourceBuilder searchSourceBuilder, int page, int pageSize, SortOrder sortOrder) {
//        searchSourceBuilder.from(page);
//        searchSourceBuilder.size(pageSize);
//        searchSourceBuilder.sort("salary", sortOrder);
//    }
//
//    /**
//     * 查询封装
//     *
//     * @param searchRequest  searchRequest
//     * @param requestOptions RequestOptions
//     * @return SearchResponse
//     */
//    private SearchResponse clientSearch(SearchRequest searchRequest, RequestOptions requestOptions) throws IOException {
//        return restHighLevelClient.search(searchRequest, requestOptions);
//    }
//
//    /**
//     * 查询结果封装
//     *
//     * @param searchResponse 封装结果
//     * @param result         查询结果
//     */
//    private void builderQueryResult(SearchResponse searchResponse, List<UserModel> result) {
//        if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getTotalShards() > 0) {
//            SearchHits hits = searchResponse.getHits();
//            for (SearchHit searchHit : hits) {
//                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
//                UserModel userModel = new UserModel();
//                userModel.setName(sourceAsMap.get("name").toString());
//                userModel.setAge(sourceAsMap.get("age").toString());
//                userModel.setSalary(sourceAsMap.get("salary").toString());
//                userModel.setAddress(sourceAsMap.get("address").toString());
//                userModel.setRemark(sourceAsMap.get("remark").toString());
//                userModel.setBirthDate(DateFormat.getInstance().format(sourceAsMap.get("birthDate")));
//                result.add(userModel);
//            }
//        }
//    }
//
//    /**
//     * user索引
//     *
//     * @return
//     * @throws IOException
//     */
//    private XContentBuilder createMapping() throws IOException {
//        return XContentFactory.jsonBuilder()
//                .startObject()
//                .field("dynamic", true)
//                .startObject("properties")
//
//                .startObject("name")
//                .field("type", "text")
//                .startObject("fields")
//                .startObject("keyword")
//                .field("type", "keyword")
//                .endObject()
//                .endObject()
//                .endObject()
//
//                .startObject("address")
//                .field("type", "text")
//                .startObject("fields")
//                .startObject("keyword")
//                .field("type", "keyword")
//                .endObject()
//                .endObject()
//                .endObject()
//
//                .startObject("remark")
//                .field("type", "text")
//                .startObject("fields")
//                .startObject("keyword")
//                .field("type", "keyword")
//                .endObject()
//                .endObject()
//                .endObject()
//
//                .startObject("age")
//                .field("type", "integer")
//                .endObject()
//
//                .startObject("salary")
//                .field("type", "float")
//                .endObject()
//
//                .startObject("birthDate")
//                .field("type", "date")
//                .field("format", "yyyy-MM-dd")
//                .endObject()
//
//                .startObject("createTime")
//                .field("type", "date")
//                .endObject()
//
//                .endObject()
//                .endObject();
//    }
//
//    /**
//     * 创建索引配置
//     *
//     * @return
//     */
//    private Settings createSettings() {
//        return Settings.builder()
//                .put("index.number_of_shards", 1)
//                .put("index.number_of_replicas", 0)
//                .build();
//    }

}
