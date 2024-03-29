package com.moreno.starsystemtraveltimeservice.model.dto;

import java.util.List;
//used for maximum stops as the query result wants the stops in the output
public class RouteResponseStopsDTO extends RouteResponseDTO{
    private Integer stops;
    public RouteResponseStopsDTO(List<String> starSystemNames) {
        super(starSystemNames);
        this.stops=starSystemNames.size()-1;
    }

    public Integer getStops() {
        return stops;
    }

    public void setStops(Integer stops) {
        this.stops = stops;
    }
}
