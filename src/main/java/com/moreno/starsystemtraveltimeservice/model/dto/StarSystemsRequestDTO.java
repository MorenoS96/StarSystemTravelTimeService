package com.moreno.starsystemtraveltimeservice.model.dto;

import java.util.ArrayList;
import java.util.List;

public class StarSystemsRequestDTO {
    private List<String> starSystems=new ArrayList<>();


    public StarSystemsRequestDTO() {
    }
    public void addStarSystemName(String starSystemSame){
        starSystems.add(starSystemSame);
    }


    public StarSystemsRequestDTO(List<String> starSystems) {
        this.starSystems = starSystems;
    }
    public List<String> getStarSystems() {
        return starSystems;
    }

    public void setStarSystems(List<String> starSystems) {
        this.starSystems = starSystems;
    }
}
