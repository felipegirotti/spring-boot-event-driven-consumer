package com.drz.search.infrastructure.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

public class BaseHttpClient {

    private RestTemplate restTemplate = new RestTemplate();

    public BaseHttpClient(Integer connectionTimeout, Integer readTimeout, Integer maxConnections) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        HttpClient httpClient = httpClientBuilder
                .setMaxConnPerRoute(maxConnections)
                .setMaxConnTotal(maxConnections)
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        factory.setConnectTimeout(connectionTimeout);
        factory.setConnectionRequestTimeout(connectionTimeout);
        factory.setReadTimeout(readTimeout);

        this.restTemplate.setRequestFactory(factory);
    }

    public RestOperations getRestTemplate() {
        return restTemplate;
    }
}
