package com.jiachen.elasticsearch.service;

import com.jiachen.elasticsearch.model.UserModel;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 * @ClassName IndexService
 * @Author jiachenXu
 * @Date 2020/4/12
 * @Description
 */
public interface IndexService {

    /**
     * 创建索引
     *
     * @return
     * @throws IOException
     */
    Boolean createAccountIndex() throws IOException;

    /**
     * 创建索引
     *
     * @param indexName 索引名称
     * @param settings
     * @param mapping
     * @return
     * @throws IOException
     */
    Boolean createIndex(String indexName, Map<String, Object> settings, Map<String, Object> mapping) throws IOException;

    /**
     * 删除索引
     *
     * @param indexName 索引名称
     * @return
     * @throws IOException
     */
    Boolean deleteIndex(String indexName) throws IOException;

    /**
     * 清空索引数据
     *
     * @param indexName
     * @return
     */
    Boolean clearIndex(String indexName) throws IOException;

    /**
     * 索引是否存在
     *
     * @param indexName
     * @return
     */
    Boolean exists(String indexName) throws IOException;

    /**
     * 搜索
     *
     * @param indexName 索引名称
     * @param field     索引词
     * @param key       关键词
     * @param keys      关键词（可空）
     * @param page      当前页
     * @param pageSize  页长度
     * @param sortOrder 倒序\正序
     * @return List<UserModel>
     * @throws IOException
     */
    List<UserModel> search(String indexName, String field, String key, String[] keys, int page, int pageSize, SortOrder sortOrder) throws IOException;

    /**
     * 精确查询
     *
     * @param indexName 索引名称
     * @param field     索引词
     * @param key       关键词
     * @param page      当前页
     * @param pageSize  页长度
     * @param sortOrder 倒序\正序
     * @return List<UserModel>
     * @throws IOException
     */
    List<UserModel> termQuery(String indexName, String field, String key, String value, int page, int pageSize, SortOrder sortOrder) throws IOException;

    /**
     * 模糊查询
     *
     * @param indexName 索引名称
     * @param field     索引词
     * @param key       关键词
     * @param value     关键词（可空）
     * @param page      当前页
     * @param pageSize  页长度
     * @param sortOrder 倒序\正序
     * @return List<UserModel>
     * @throws IOException
     */
    List<UserModel> fuzziness(String indexName, String field, String key, String value, int page, int pageSize, SortOrder sortOrder) throws IOException;

}
