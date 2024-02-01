package com.moreno.starsystemtraveltimeservice.repository;

import com.moreno.starsystemtraveltimeservice.model.entity.StarSystemEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;


public interface StarSystemRepository extends Neo4jRepository<StarSystemEntity, String> {

}
