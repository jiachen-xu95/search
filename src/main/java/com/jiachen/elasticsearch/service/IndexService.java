package com.jiachen.elasticsearch.service;

import java.io.IOException;

/**
 * @Version 1.0
 * @ClassName IndexService
 * @Author jiachenXu
 * @Date 2020/4/12 22:01
 * @Description
 */
public interface IndexService {

    /**
     * 创建索引
     *
     * @return
     * @throws IOException
     */
    Boolean createIndex() throws IOException;

    /**
     * 删除索引
     *
     * @return
     * @throws IOException
     */
    Boolean deleteIndex() throws IOException;

    /**
     * 获取索引模型
     *
     * @param index
     * @return
     * @throws IOException
     */
    String getIndex(String index) throws IOException;
}
