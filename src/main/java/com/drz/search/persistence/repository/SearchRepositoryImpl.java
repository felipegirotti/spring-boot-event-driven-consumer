package com.drz.search.persistence.repository;


import com.drz.search.dto.client.ClientDTO;
import com.drz.search.dto.place.PlaceDTO;
import com.drz.search.infrastructure.searchengine.SearchClient;
import com.drz.search.persistence.entity.Client;
import com.drz.search.persistence.entity.GeoPoint;
import com.drz.search.persistence.entity.Place;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

public class SearchRepositoryImpl implements SearchRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchRepositoryImpl.class);

    private SearchClient searchClient;

    private String indexTypePlace;

    private String indexTypeClient;

    private ObjectMapper objectMapper;

    public SearchRepositoryImpl(SearchClient searchClient, String indexTypePlace, String indexTypeClient) {
        this.searchClient = searchClient;
        this.indexTypePlace = indexTypePlace;
        this.indexTypeClient = indexTypeClient;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void save(PlaceDTO placeDTO) {
        try {
            Place place = new Place(
                    placeDTO.getId(),
                    placeDTO.getName(),
                    placeDTO.getClientId(),
                    new GeoPoint(placeDTO.getLatitude(), placeDTO.getLongitude())
             );
            String req = this.objectMapper.writeValueAsString(place);
            searchClient.post(String.format("%s/%d", indexTypePlace, placeDTO.getId()), req, Map.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error to save place [%s] into elasticsearch", placeDTO, e);
        }
    }

    @Override
    public void save(ClientDTO clientDTO) {
        try {
            Client client = new Client(
                    clientDTO.getId(),
                    clientDTO.getName(),
                    new GeoPoint(
                            clientDTO.getLatitude(),
                            clientDTO.getLongitude()
                    )
            );
            String req = this.objectMapper.writeValueAsString(client);
            searchClient.post(String.format("%s/%d", indexTypeClient, clientDTO.getId()), req, Map.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error to save client [%s] into elasticsearch", clientDTO, e);
        }
    }

    @Override
    public void deletePlace(Long id) {
        searchClient.delete(String.format("%s/%d", indexTypePlace, id));
    }

    @Override
    public void deleteClient(Long id) {
        searchClient.delete(String.format("%s/%d", indexTypeClient, id));
    }

    @Override
    public List<ClientDTO> getAllClients(Integer from, Integer size) {
        BoolQueryBuilder filter = boolQuery().must(matchAllQuery());

        String req = buildSearch(from, size, filter.toString());
        ResponseEntity<Map> responseClient = searchClient.post(String.format("%s/_search", indexTypeClient), req, Map.class);

        List<ClientDTO> response = new ArrayList<>();
        if (responseClient.getStatusCode().equals(HttpStatus.OK)) {
            Map<String, Object> hits = ((Map) responseClient.getBody().get("hits"));

            if ((Integer) hits.get("total") > 0) {
                response =  ((List<Map>) hits.get("hits")).stream()
                        .map(p -> {
                            Map client = (Map) p.get("_source");
                            Map location = (Map) client.get("location");
                            return new ClientDTO(
                                    Long.valueOf(p.get("_id").toString()),
                                    client.get("name").toString(),
                                    Double.valueOf(location.get("lat").toString()),
                                    Double.valueOf(location.get("lon").toString())
                            );
                        })
                        .collect(Collectors.toList());
            }
        }

        return response;
    }

    private String buildSearch(Integer from, Integer size, String filter) {
        return String.format("{\"from\": %d, \"size\": %d, \"query\": %s}", from, size, filter);
    }

    @Override
    public List<PlaceDTO> searchByGeoLocation(Integer from, Integer size, GeoPoint topLeft, GeoPoint bottomRight, Long clientId) {
        GeoBoundingBoxQueryBuilder geoBuilder = geoBoundingBoxQuery("location")
                .setCorners(topLeft.getLat(), topLeft.getLon(), bottomRight.getLat(), bottomRight.getLon());


        BoolQueryBuilder filter = boolQuery().must(termQuery("client_id", clientId))
                .filter(geoBuilder);

        String req = buildSearch(from, size, filter.toString());

        ResponseEntity<Map> responseClient = searchClient.post(String.format("%s/_search", indexTypePlace), req, Map.class);

        List<PlaceDTO> response = new ArrayList<>();
        if (responseClient.getStatusCode().equals(HttpStatus.OK)) {
            Map<String, Object> hits = ((Map) responseClient.getBody().get("hits"));

            if ((Integer) hits.get("total") > 0) {
               response =  ((List<Map>) hits.get("hits")).stream()
                        .map(p -> {
                            Place place = objectMapper.convertValue(p.get("_source"), Place.class);

                            return new PlaceDTO(place.getId(), place.getClientId(), place.getName(), place.getLocation().getLat(), place.getLocation().getLon());
                        })
                        .collect(Collectors.toList());
            }
        }

        return response;
    }
}
