package com.drz.search.persistence.repository;

import com.drz.search.dto.client.ClientDTO;
import com.drz.search.dto.place.PlaceDTO;

import java.util.List;

public interface SearchRepository {

    public void save(PlaceDTO placeDTO);

    public void save(ClientDTO clientDTO);

    public void deletePlace(Long id);

    public void deleteClient(Long id);

    public List<ClientDTO> getAllClients(Integer from, Integer size);

    public List<PlaceDTO> searchByGeoLocation(Integer from, Integer size, double lat, double lon, Long clientId, Long distanceMeters);
}
