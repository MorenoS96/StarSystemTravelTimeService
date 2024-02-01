package com.moreno.starsystemtraveltimeservice.repository.integrationtests;

import com.moreno.starsystemtraveltimeservice.model.entity.StarSystemEntity;
import com.moreno.starsystemtraveltimeservice.repository.StarSystemRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataNeo4jTest

public class StarSystemRepositoryTest {

    private static Neo4jContainer<?> neo4jContainer;

    @Autowired
    StarSystemRepository starSystemRepository;
    @BeforeAll
    static void initializeNeo4j() {

        neo4jContainer = new Neo4jContainer<>("neo4j:5.16.0")
                .withAdminPassword("somePassword");
        neo4jContainer.start();
    }

    @AfterAll
    static void stopNeo4j() {

        neo4jContainer.close();
    }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", neo4jContainer::getAdminPassword);
    }

    @Test
    public void findSomethingShouldWork(@Autowired Neo4jClient client) {

        Optional<Long> result = client.query("MATCH (n) RETURN COUNT(n)")
                .fetchAs(Long.class)
                .one();
        assertThat(result).hasValue(0L);
    }
    @Test
    void testSaveAndRetrieveStarSystem(){
        StarSystemEntity starSystemEntity=new StarSystemEntity("AndroMeda","AndroMeda");
        starSystemRepository.save(starSystemEntity);
        List<StarSystemEntity> retrievedEntities=starSystemRepository.findAll();
        assertThat(retrievedEntities.contains(starSystemEntity)).isTrue();

        assertThat(retrievedEntities.get(0).getName()).isEqualTo("AndroMeda");

    }
    @Test
    void testSaveAndRetrieveStarSystemsWithRelationShip(){
        StarSystemEntity starSystemEntity=new StarSystemEntity("AndroMeda","AndroMeda");
        StarSystemEntity starSystemEntity2=new StarSystemEntity("MilkyWay","MilkyWay");
        starSystemEntity.createConnectionTo(starSystemEntity2,3);
        starSystemRepository.save(starSystemEntity);
        starSystemRepository.save(starSystemEntity2);
        StarSystemEntity retrievedStarSystemEntity=starSystemRepository.findById("AndroMeda").orElse(null);
        assertThat(retrievedStarSystemEntity)
                .isNotNull()
                .isEqualTo(starSystemEntity);

    }


}
