package com.drz.search.persistence.repository;

import com.drz.search.dto.place.GeoPoint;
import com.drz.search.dto.place.PlaceDTO;
import com.drz.search.infrastructure.searchengine.SearchClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.geoDistanceQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

public class SearchRepositoryImpl implements SearchRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchRepositoryImpl.class);

    private SearchClient searchClient;

    private String indexTypePlace;

    private ObjectMapper objectMapper;

    public SearchRepositoryImpl(SearchClient searchClient, String indexTypePlace) {
        this.searchClient = searchClient;
        this.indexTypePlace = indexTypePlace;
        this.objectMapper = new ObjectMapper();
    }

    public void save(PlaceDTO placeDTO) {
        try {
            placeDTO.setLocation(new GeoPoint(placeDTO.getLatitude(), placeDTO.getLongitude()));
            String req = this.objectMapper.writeValueAsString(placeDTO);
            searchClient.post(String.format("%s/%d", indexTypePlace, placeDTO.getId()), req, Map.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error to save place [%s] into elasticsearch", placeDTO, e);
        }
    }

    public void delete(Long id) {
        searchClient.delete(String.format("%s/%d", indexTypePlace, id));
    }

    public List<PlaceDTO> searchByGeoLocation(Integer from, Integer size, double lat, double lon, Long distanceMeters) {
        GeoDistanceQueryBuilder geoBuilder = geoDistanceQuery("location")
                .point(lat, lon)
                .distance(distanceMeters, DistanceUnit.METERS);

        BoolQueryBuilder filter = boolQuery().must(matchAllQuery())
                .filter(geoBuilder);


        String req = String.format("{\"from\": %d, \"size\": %d, \"query\": %s}", from, size, filter.toString());

        ResponseEntity<Map> response = searchClient.post(String.format("%s/_search", indexTypePlace), req, Map.class);

        return null;
    }
}
