package com.drz.search.config;


import com.drz.search.infrastructure.http.BaseHttpClient;
import com.drz.search.persistence.repository.SearchRepository;
import com.drz.search.persistence.repository.SearchRepositoryImpl;
import com.drz.search.infrastructure.searchengine.SearchClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;

@Configuration
@ConfigurationProperties(prefix = "place.elasticsearch")
@Data
public class PlaceElasticSearchConfig {

    private Integer connectionTimeout;

    private Integer readTimeout;

    private Integer maxConnections;

    private String url;

    private String indexTypePlace;

    @Bean
    public RestOperations restOperations()  {
        return new BaseHttpClient(connectionTimeout, readTimeout, maxConnections).getRestTemplate();
    }

    @Bean
    public SearchClient searchClient(RestOperations restOperations) {
        return new SearchClient(restOperations, url);
    }

    @Bean
    public SearchRepository searchRepository(SearchClient searchClient) {
        return new SearchRepositoryImpl(searchClient, indexTypePlace);
    }
}
