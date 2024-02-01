package com.moreno.starsystemtraveltimeservice.model.dto;


public class RoutesRequestDTO {

    private String startSystemName;
    private String destinationSystemName;
    private Integer maxTravelTime;
    private Integer maxStops;
    private Integer exactStops;

    public Integer getExactStops() {
        return exactStops;
    }

    public void setExactStops(Integer exactStops) {
        this.exactStops = exactStops;
    }

    public String getStartSystemName() {
        return startSystemName;
    }

    public void setStartSystemName(String startSystemName) {
        this.startSystemName = startSystemName;
    }

    public String getDestinationSystemName() {
        return destinationSystemName;
    }

    public void setDestinationSystemName(String destinationSystemName) {
        this.destinationSystemName = destinationSystemName;
    }

    public Integer getMaxTravelTime() {
        return maxTravelTime;
    }

    public void setMaxTravelTime(Integer maxTravelTime) {
        this.maxTravelTime = maxTravelTime;
    }

    public Integer getMaxStops() {
        return maxStops;
    }

    public void setMaxStops(Integer maxStops) {
        this.maxStops = maxStops;
    }
}
