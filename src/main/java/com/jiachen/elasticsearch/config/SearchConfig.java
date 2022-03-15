package com.jiachen.elasticsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Version 1.0
 * @ClassName SearchConfig
 * @Author jiachenXu
 * @Date 2020/4/12
 * @Description
 */
@Configuration
public class SearchConfig {

    /**
     * 协议
     */
    @Value("${elasticsearch.schema}")
    private String schema;

    /**
     * 集群地址，如果有多个用“,”隔开
     */
    @Value("${elasticsearch.address}")
    private String address;

    /**
     * 连接超时时间
     */
    @Value("${elasticsearch.connectTimeout}")
    private int connectTimeout;

    /**
     * Socket 连接超时时间
     */
    @Value("${elasticsearch.socketTimeout}")
    private int socketTimeout;

    /**
     * 获取连接的超时时间
     */
    @Value("${elasticsearch.connectionRequestTimeout}")
    private int connectionRequestTimeout;

    /**
     * 最大连接数
     */
    @Value("${elasticsearch.maxConnectNum}")
    private int maxConnectNum;

    /**
     * 最大路由连接数
     */
    @Value("${elasticsearch.maxConnectPerRoute}")
    private int maxConnectPerRoute;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        List<HttpHost> httpHosts = new ArrayList<>();
        String[] hostList = address.split(",");
        for (String addr : hostList) {
            String host = addr.split(":")[0];
            String post = addr.split(":")[1];
            httpHosts.add(new HttpHost(host, Integer.parseInt(post), schema));
        }

        HttpHost[] httpHost = httpHosts.toArray(new HttpHost[]{});
        // 构建连接对象
        RestClientBuilder builder = RestClient.builder(httpHost);
        // 异步连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeout);
            requestConfigBuilder.setSocketTimeout(socketTimeout);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeout);
            return requestConfigBuilder;
        });
        // 异步连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectNum);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            return httpClientBuilder;
        });
        return new RestHighLevelClient(builder);

    }

}
