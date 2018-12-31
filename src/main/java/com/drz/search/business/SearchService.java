package com.drz.search.business;


import com.drz.search.dto.client.ClientDTO;
import com.drz.search.dto.place.PlaceDTO;

import java.util.List;

public interface SearchService {

    public List<PlaceDTO> search(Integer from, Integer size, double lat, double lon, Long clientId, Long distanceMeters);

    public List<ClientDTO> getClients(Integer from, Integer size);
}
