package com.moreno.starsystemtraveltimeservice.model.dto;


import java.util.ArrayList;
import java.util.List;


public class RouteResponseDTO {
    protected List<String> starSystemNames;

    public RouteResponseDTO(List<String> starSystemNames) {
        this.starSystemNames = starSystemNames;
    }

    public RouteResponseDTO() {
        this.starSystemNames = new ArrayList<>();
    }

    public List<String> getStarSystemNames() {
        return starSystemNames;
    }

    public void setStarSystemNames(List<String> starSystemNames) {
        this.starSystemNames = starSystemNames;
    }
}
