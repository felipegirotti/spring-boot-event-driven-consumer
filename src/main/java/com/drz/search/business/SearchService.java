package com.drz.search.business;


import com.drz.search.dto.client.ClientDTO;
import com.drz.search.dto.place.PlaceDTO;
import com.drz.search.persistence.entity.GeoPoint;

import java.util.List;

public interface SearchService {

    public List<PlaceDTO> search(Integer from, Integer size, GeoPoint topLeft, GeoPoint bottomRight, Long clientId);

    public List<ClientDTO> getClients(Integer from, Integer size);
}
