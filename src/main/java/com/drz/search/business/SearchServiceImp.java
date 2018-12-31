package com.drz.search.business;

import com.drz.search.dto.client.ClientDTO;
import com.drz.search.dto.place.PlaceDTO;
import com.drz.search.persistence.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImp implements SearchService{

    private SearchRepository searchRepository;

    @Autowired
    public SearchServiceImp(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public List<PlaceDTO> search(Integer from, Integer size, double lat, double lon, Long clientId, Long distanceMeters) {
        return searchRepository.searchByGeoLocation(from, size, lat, lon, clientId, distanceMeters);
    }

    @Override
    public List<ClientDTO> getClients(Integer from, Integer size) {
        return searchRepository.getAllClients(from, size);
    }
}
