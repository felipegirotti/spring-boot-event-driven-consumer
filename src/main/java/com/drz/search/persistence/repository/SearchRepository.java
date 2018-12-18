package com.drz.search.persistence.repository;

import com.drz.search.dto.place.PlaceDTO;

import java.util.List;

public interface SearchRepository {

    public void save(PlaceDTO placeDTO);

    public void delete(Long id);

    public List<PlaceDTO> searchByGeoLocation(Integer from, Integer size, double lat, double lon, Long distanceMeters);
}
