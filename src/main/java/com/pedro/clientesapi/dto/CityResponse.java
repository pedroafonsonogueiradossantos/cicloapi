package com.pedro.clientesapi.dto;

import com.pedro.clientesapi.entity.City;

public record CityResponse(
        Integer id,
        String name
) {
    public static CityResponse from(City city) {
        return new CityResponse(city.getId(), city.getName());
    }
}
