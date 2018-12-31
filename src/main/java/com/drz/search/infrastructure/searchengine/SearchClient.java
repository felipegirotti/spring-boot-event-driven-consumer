package com.drz.search.infrastructure.searchengine;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.util.Arrays;
import java.util.Map;

public class SearchClient {

    private RestOperations restOperations;

    private String elasticSearchUrl;

    public SearchClient(RestOperations restOperations, String elasticSearchUrl) {
        this.restOperations = restOperations;
        this.elasticSearchUrl = elasticSearchUrl;
    }

    public <T> ResponseEntity<T> post(String indexType, String req, Class<T> clazz) {
        HttpEntity<String> entityReq = new HttpEntity<>(req, buildHeaders());

        return restOperations.exchange(buildUrl(indexType), HttpMethod.POST, entityReq, clazz);
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    private String buildUrl(String path) {
        return String.format("%s/%s", this.elasticSearchUrl, path);
    }

    public void delete(String indexTypeId) {
        HttpEntity<String> entityReq = new HttpEntity<>("parameters", buildHeaders());

        try {
            restOperations.exchange(buildUrl(indexTypeId), HttpMethod.DELETE, entityReq, Map.class);
        } catch (RestClientException e) {
            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException ex = (HttpClientErrorException) e;
                if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
                    throw e;
                }
            }
        }
    }

    public <T> ResponseEntity<T> get(String path, Class<T> clazz) {
        HttpEntity<String> entityReq = new HttpEntity<>("parameters", buildHeaders());

        return restOperations.exchange(buildUrl(path), HttpMethod.GET, entityReq, clazz);
    }
}
