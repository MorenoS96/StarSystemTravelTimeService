package com.moreno.starsystemtraveltimeservice.controller.unittests;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.moreno.starsystemtraveltimeservice.controller.StarSystemController;
import com.moreno.starsystemtraveltimeservice.model.dto.RoutesRequestDTO;
import com.moreno.starsystemtraveltimeservice.model.dto.StarSystemsRequestDTO;
import com.moreno.starsystemtraveltimeservice.model.entity.StarSystemEntity;
import com.moreno.starsystemtraveltimeservice.service.RouteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StarSystemController.class)

public class StarSystemControllerUnitTest {

    @MockBean
    private RouteService routeService;
    @Autowired
    private MockMvc mockMvc;

    private static final String solarSystem="Solar System";
    private static final String alphaCentauri="Alpha Centauri";
    private static final String sirius="Sirius";
    private static final String vega="Vega";
    private static final String betelgeuse="Betelgeuse";

    private static StarSystemEntity solarSystemEntity;
    private static StarSystemEntity alphaCentauriEntity;
    private static StarSystemEntity siriusEntity;
    private static StarSystemEntity vegaEntity;
    private static StarSystemEntity betelgeuseEntity;

    private static final ObjectMapper objectMapper=new ObjectMapper();

    @BeforeAll
    static void setUp(){

        solarSystemEntity=new StarSystemEntity(solarSystem,solarSystem);
        alphaCentauriEntity=new StarSystemEntity(alphaCentauri,alphaCentauri);
        siriusEntity=new StarSystemEntity(sirius,sirius);
        vegaEntity=new StarSystemEntity(vega,vega);
        betelgeuseEntity=new StarSystemEntity(betelgeuse,betelgeuse);
        solarSystemEntity.createConnectionTo(betelgeuseEntity,5);
        solarSystemEntity.createConnectionTo(vegaEntity,7);
        solarSystemEntity.createConnectionTo(alphaCentauriEntity,5);
        alphaCentauriEntity.createConnectionTo(siriusEntity,4);
        siriusEntity.createConnectionTo(vegaEntity,2);
        siriusEntity.createConnectionTo(betelgeuseEntity,8);
        vegaEntity.createConnectionTo(alphaCentauriEntity,3);
        betelgeuseEntity.createConnectionTo(siriusEntity,8);
        betelgeuseEntity.createConnectionTo(vegaEntity,6);

    }
    @Test
    void getDistanceTest() throws Exception {
        List<String> starSystemNames = new ArrayList<>();
        starSystemNames.add("Solar System");
        starSystemNames.add("Alpha Centauri");
        starSystemNames.add("Sirius");
        Mockito.when(this.routeService.getDistance(starSystemNames)).thenReturn(9);
        final String link = "/api/distance";
        StarSystemsRequestDTO starSystemsRequestDTO =new StarSystemsRequestDTO();
        starSystemsRequestDTO.setStarSystems(starSystemNames);
        mockMvc.perform(MockMvcRequestBuilders
                        .post(link)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(starSystemsRequestDTO)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").value(9));


    }

    @Test
    void getRoutesWithMaximumStopsTest() throws Exception {


        List<List<StarSystemEntity>> expected=new ArrayList<>();
        List<StarSystemEntity> path1=new ArrayList<>();
                path1.add(siriusEntity);
        path1.add(betelgeuseEntity);
        path1.add(siriusEntity);
        expected.add(path1);

        List<StarSystemEntity> path2=new ArrayList<>();
        path2.add(siriusEntity);
        path2.add(vegaEntity);
        path2.add(alphaCentauriEntity);
        path2.add(siriusEntity);
        expected.add(path2);
        Mockito.when(this.routeService.getAllRoutesWithMaximumStops("Sirius","Sirius",3)).thenReturn(expected);

        RoutesRequestDTO routesRequestDTO=new RoutesRequestDTO();
        routesRequestDTO.setStartSystemName("Sirius");
        routesRequestDTO.setDestinationSystemName("Sirius");
        routesRequestDTO.setMaxStops(3);
        String content=objectMapper.writeValueAsString(routesRequestDTO);


        final String link="/api/maximum-stops-routes";
        mockMvc.perform(MockMvcRequestBuilders.post(link)
                        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)));

    }
    @Test
   void getDurationOfShortestRouteTest() throws Exception {
        RoutesRequestDTO routesRequestDTO=new RoutesRequestDTO();
        routesRequestDTO.setStartSystemName("Solar System");
        routesRequestDTO.setDestinationSystemName("Sirius");
        Mockito.when(this.routeService.getShortestRouteDuration("Solar System","Sirius")).thenReturn(9);


        String content=objectMapper.writeValueAsString(routesRequestDTO);
        final String link="/api/shortest-route-duration";
        mockMvc.perform(MockMvcRequestBuilders.post(link)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").value(9));

    }
    @Test
    void getRoutesWithExactStopsTest() throws Exception {
        RoutesRequestDTO routesRequestDTO=new RoutesRequestDTO();
        routesRequestDTO.setStartSystemName("Solar System");
        routesRequestDTO.setDestinationSystemName("Sirius");
        routesRequestDTO.setExactStops(3);
        List<StarSystemEntity> path1=new ArrayList<>();
        path1.add(solarSystemEntity);
        path1.add(alphaCentauriEntity);
        path1.add(siriusEntity);
        path1.add(betelgeuseEntity);
        path1.add(siriusEntity);
        List<StarSystemEntity> path2=new ArrayList<>();
        path2.add(solarSystemEntity);
        path2.add(betelgeuseEntity);
        path2.add(siriusEntity);
        path2.add(betelgeuseEntity);
        path2.add(siriusEntity);
        List<StarSystemEntity> path3=new ArrayList<>();
        path3.add(solarSystemEntity);
        path3.add(betelgeuseEntity);
        path3.add(vegaEntity);
        path3.add(alphaCentauriEntity);
        path3.add(siriusEntity);
        List< List<StarSystemEntity> > expected=new ArrayList<>();
        expected.add(path1);
        expected.add(path2);
        expected.add(path3);
        Mockito.when(this.routeService.getAllRoutesWithExactStops("Solar System","Sirius",3)).thenReturn(expected);
        final String link="/api/exact-stops-routes";
        String content=objectMapper.writeValueAsString(routesRequestDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(link)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)));
    }
    @Test
    void getRoutesWithMaximumTravelTimeTest() throws Exception {
        List<StarSystemEntity> path1=new ArrayList<>();
        List<StarSystemEntity> path2=new ArrayList<>();
        List<StarSystemEntity> path3=new ArrayList<>();
        List<StarSystemEntity> path4=new ArrayList<>();
        List<StarSystemEntity> path5=new ArrayList<>();
        List<StarSystemEntity> path6=new ArrayList<>();
        List<StarSystemEntity> path7=new ArrayList<>();
        //path1
        path1.add(siriusEntity);
        path1.add(betelgeuseEntity);
        path1.add(siriusEntity);

        //path2
        path2.add(siriusEntity);
        path2.add(vegaEntity);
        path2.add(alphaCentauriEntity);
        path2.add(siriusEntity);


        //path3
        path3.add(siriusEntity);
        path3.add(vegaEntity);
        path3.add(alphaCentauriEntity);
        path3.add(siriusEntity);
        path3.add(betelgeuseEntity);
        path3.add(siriusEntity);

        //path4
        path4.add(siriusEntity);
        path4.add(betelgeuseEntity);
        path4.add(siriusEntity);
        path4.add(vegaEntity);
        path4.add(alphaCentauriEntity);
        path4.add(siriusEntity);

        //path5
        path5.add(siriusEntity);
        path5.add(betelgeuseEntity);
        path5.add(vegaEntity);
        path5.add(alphaCentauriEntity);
        path5.add(siriusEntity);


        //path6
        path6.add(siriusEntity);
        path6.add(vegaEntity);
        path6.add(alphaCentauriEntity);
        path6.add(siriusEntity);
        path6.add(vegaEntity);
        path6.add(alphaCentauriEntity);
        path6.add(siriusEntity);


        //path7
        path7.add(siriusEntity);
        path7.add(vegaEntity);
        path7.add(alphaCentauriEntity);
        path7.add(siriusEntity);
        path7.add(vegaEntity);
        path7.add(alphaCentauriEntity);
        path7.add(siriusEntity);
        path7.add(vegaEntity);
        path7.add(alphaCentauriEntity);
        path7.add(siriusEntity);

        List<List<StarSystemEntity>> expected=new ArrayList<>();
        expected.add(path1);
        expected.add(path2);
        expected.add(path3);
        expected.add(path4);
        expected.add(path5);
        expected.add(path6);
        expected.add(path7);

        Mockito.when(this.routeService.getAllRoutesWithMaximumTravelTime("Sirius","Sirius",30)).thenReturn(expected);
        RoutesRequestDTO routesRequestDTO=new RoutesRequestDTO();
        routesRequestDTO.setStartSystemName("Sirius");
        routesRequestDTO.setDestinationSystemName("Sirius");
        routesRequestDTO.setMaxTravelTime(30);
        final String link="/api/maximum-travel-time-routes";
        String content=objectMapper.writeValueAsString(routesRequestDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(link)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(7)));
    }
    @Test
    void emptyExceptionTest() throws Exception {
        final String link="/api/maximum-travel-time-routes";
        mockMvc.perform(MockMvcRequestBuilders.post(link)
                .contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());
    }
    @Test
    void invalidParameterTest() throws Exception {
        final String link="/api/maximum-travel-time-routes";
        RoutesRequestDTO routesRequestDTO=new RoutesRequestDTO();
        routesRequestDTO.setStartSystemName(sirius);
        routesRequestDTO.setDestinationSystemName(sirius);
        routesRequestDTO.setMaxTravelTime(-1);
        mockMvc.perform(MockMvcRequestBuilders.post(link)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(routesRequestDTO))).andExpect(status().isBadRequest());
    }
}
