package com.drz.search.business;


import com.drz.search.dto.place.PlaceDTO;

import java.util.List;

public interface SearchService {

    public List<PlaceDTO> search(Integer from, Integer size, double lat, double lon, Long distanceMeters);
}
