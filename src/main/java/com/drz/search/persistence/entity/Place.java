package com.drz.search.persistence.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Place {

    private Long id;

    private String name;

    @JsonProperty("client_id")
    private Long clientId;

    private GeoPoint location;
}
