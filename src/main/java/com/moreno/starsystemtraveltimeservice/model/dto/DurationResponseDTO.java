package com.moreno.starsystemtraveltimeservice.model.dto;

public class DurationResponseDTO {
    private int time;
    private String unit;

    public DurationResponseDTO(int time, String unit) {
        this.time = time;
        this.unit = unit;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
