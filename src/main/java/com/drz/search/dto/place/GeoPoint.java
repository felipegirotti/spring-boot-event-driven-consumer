package com.drz.search.dto.place;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeoPoint {

    private double lat;

    private double lon;
}
