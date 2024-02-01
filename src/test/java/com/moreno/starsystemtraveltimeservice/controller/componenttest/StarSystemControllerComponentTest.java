package com.moreno.starsystemtraveltimeservice.controller.componenttest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moreno.starsystemtraveltimeservice.model.dto.RoutesRequestDTO;
import com.moreno.starsystemtraveltimeservice.model.dto.StarSystemsRequestDTO;
import com.moreno.starsystemtraveltimeservice.repository.StarSystemRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.Neo4jContainer;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc


public class StarSystemControllerComponentTest {
    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", neo4jContainer::getAdminPassword);
    }
    static  Neo4jContainer<?> neo4jContainer ;
    @BeforeAll
    static void initializeNeo4j() {

        neo4jContainer  = new Neo4jContainer<>("neo4j:5.16.0")
                .withAdminPassword("somePassword")
                .withClasspathResourceMapping("neo4j/seed", "/var/lib/neo4j/import", BindMode.READ_ONLY)
                .withClasspathResourceMapping("neo4j/conf/apoc.conf", "/var/lib/neo4j/conf/apoc.conf", BindMode.READ_ONLY)
                .withClasspathResourceMapping("neo4j/plugins", "/var/lib/neo4j/plugins", BindMode.READ_ONLY)
                 .withLogConsumer(outputFrame -> System.out.println(outputFrame.getUtf8String()));
        neo4jContainer.start();
    }

    @AfterAll
    static void stopNeo4j() {

        neo4jContainer.close();
    }
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper=new ObjectMapper();


    @Test
    public void getDistanceComponentTest() throws Exception {

        StarSystemsRequestDTO starSystemsRequestDTO =new StarSystemsRequestDTO();
        starSystemsRequestDTO.addStarSystemName("Solar System");
        starSystemsRequestDTO.addStarSystemName("Alpha Centauri");
        starSystemsRequestDTO.addStarSystemName("Sirius");
        String content=objectMapper.writeValueAsString(starSystemsRequestDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/distance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").value(9));

    }

    @Test
    public void getShortestRouteComponentTest() throws Exception {

        RoutesRequestDTO routesRequestDTO=new RoutesRequestDTO();
        routesRequestDTO.setStartSystemName("Alpha Centauri");
        routesRequestDTO.setDestinationSystemName("Alpha Centauri");
        String content=objectMapper.writeValueAsString(routesRequestDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/shortest-route-duration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").value(9));

    }

}
