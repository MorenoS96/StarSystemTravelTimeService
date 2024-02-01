package com.moreno.starsystemtraveltimeservice.model.dto;

import java.util.ArrayList;
import java.util.List;

public class StarSystemsRequest {
    private List<String> starSystems=new ArrayList<>();


    public StarSystemsRequest() {
        // Default constructor (no-args)
    }
    public void addStarSystemName(String starSystemSame){
        starSystems.add(starSystemSame);
    }


    public StarSystemsRequest(List<String> starSystems) {
        this.starSystems = starSystems;
    }
    public List<String> getStarSystems() {
        return starSystems;
    }

    public void setStarSystems(List<String> starSystems) {
        this.starSystems = starSystems;
    }
}
