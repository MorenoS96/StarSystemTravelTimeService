package com.moreno.starsystemtraveltimeservice.model.entity;

import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import java.util.Objects;


@RelationshipProperties
public class TravelToRelationShip {
    @RelationshipId
    private Long id;


    private StarSystemEntity sourceSystem;
    @TargetNode
    private StarSystemEntity destinationSystem;
    @Property
    private int travelTimeInHours;

    private String startSystemId;
    private String destinationSystemId;
    public TravelToRelationShip(StarSystemEntity sourceSystem, StarSystemEntity destinationSystem, int travelTimeInHours) {
        this.sourceSystem = sourceSystem;
        this.destinationSystem = destinationSystem;
        this.travelTimeInHours = travelTimeInHours;
        startSystemId=sourceSystem.getId();
        destinationSystemId=destinationSystem.getId();

    }

    public StarSystemEntity getSourceSystem() {
        return sourceSystem;
    }

    public StarSystemEntity getDestinationSystem() {
        return destinationSystem;
    }

    public TravelToRelationShip(int travelTime, String start, String end, Long identity) {
        this.travelTimeInHours = travelTime;
        this.startSystemId = start;
        this.destinationSystemId = end;
        this.id=identity;
    }

    public TravelToRelationShip() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSourceSystem(StarSystemEntity sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public void setDestinationSystem(StarSystemEntity destinationSystem) {
        this.destinationSystem = destinationSystem;
    }

    public void setTravelTimeInHours(int travelTimeInHours) {
        this.travelTimeInHours = travelTimeInHours;
    }

    public void setStartSystemId(String startSystemId) {
        this.startSystemId = startSystemId;
    }

    public void setDestinationSystemId(String destinationSystemId) {
        this.destinationSystemId = destinationSystemId;
    }

    public int getTravelTimeInHours() {
        return travelTimeInHours;
    }

    public Long getId() {
        return id;
    }

    public String getStartSystemId() {
        return startSystemId;
    }

    public String getDestinationSystemId() {
        return destinationSystemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TravelToRelationShip that = (TravelToRelationShip) o;
        return Objects.equals(travelTimeInHours, that.travelTimeInHours) && Objects.equals(startSystemId, that.startSystemId) && Objects.equals(destinationSystemId, that.destinationSystemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(travelTimeInHours, startSystemId, destinationSystemId);
    }
}
