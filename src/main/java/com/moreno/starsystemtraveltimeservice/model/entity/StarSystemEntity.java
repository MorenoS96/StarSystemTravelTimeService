package com.moreno.starsystemtraveltimeservice.model.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Node("StarSystem")
public class StarSystemEntity {
    @Id
    private  String id;
    @Property()
    private  String name;
    @Relationship(type = "TRAVEL_TO",direction = Relationship.Direction.OUTGOING)
    private List<TravelToRelationShip> travelTo=new ArrayList<>();

    public StarSystemEntity(String identifier, String name) {
        this.id = identifier;
        this.name = name;
    }

    public StarSystemEntity() {
    }

    public List<TravelToRelationShip> getTravelTo() {
        return travelTo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTravelTo(List<TravelToRelationShip> travelTo) {
        this.travelTo = travelTo;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void createConnectionTo(StarSystemEntity other,int travelDuration){
        TravelToRelationShip travelToRelationShip=new TravelToRelationShip(this,other,travelDuration);
        this.travelTo.add(travelToRelationShip);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StarSystemEntity that = (StarSystemEntity) o;
        return Objects.equals(name, that.name) && Objects.equals(travelTo, that.travelTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, travelTo);
    }

    @Override
    public String toString() {
        return "StarSystemEntity{" +
                "name='" + name + '\'' +
                '}';
    }
}
