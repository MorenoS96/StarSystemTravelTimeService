package com.moreno.starsystemtraveltimeservice.service.unittests;

import com.moreno.starsystemtraveltimeservice.model.entity.StarSystemEntity;
import com.moreno.starsystemtraveltimeservice.repository.StarSystemRepository;
import com.moreno.starsystemtraveltimeservice.service.RouteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RouteServiceUnitTest {



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

    @Mock
    private  StarSystemRepository starSystemRepository;
    @InjectMocks
    private RouteService routeService;
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
   void getDistanceTest(){
        Mockito.when(starSystemRepository.findById(solarSystem)).thenReturn(Optional.ofNullable(solarSystemEntity));
        Mockito.when(starSystemRepository.findById(alphaCentauri)).thenReturn(Optional.ofNullable(alphaCentauriEntity));
        Mockito.when(starSystemRepository.findById(sirius)).thenReturn(Optional.ofNullable(siriusEntity));
        Mockito.when(starSystemRepository.findById(betelgeuse)).thenReturn(Optional.ofNullable(betelgeuseEntity));
        Mockito.when(starSystemRepository.findById(vega)).thenReturn(Optional.ofNullable(vegaEntity));
        //positive Tests
        //Case 1
        List<String> distanceSystems1=new ArrayList<>();
        distanceSystems1.add(solarSystem);
        distanceSystems1.add(alphaCentauri);
        distanceSystems1.add(sirius);

        assertThat(routeService.getDistance(distanceSystems1))
                .isEqualTo(9);
        //Case 2
        List<String> distanceSystems2=new ArrayList<>();
        distanceSystems2.add(solarSystem);
        distanceSystems2.add(betelgeuse);
        assertThat(routeService.getDistance(distanceSystems2))
                .isEqualTo(5);

        //Case 3
        List<String> distanceSystems3=new ArrayList<>();
        distanceSystems3.add(solarSystem);
        distanceSystems3.add(betelgeuse);
        distanceSystems3.add(sirius);
        assertThat(routeService.getDistance(distanceSystems3))
                .isEqualTo(13);

        //Case 4
        List<String> distanceSystems4=new ArrayList<>();
        distanceSystems4.add(solarSystem);
        distanceSystems4.add(vega);
        distanceSystems4.add(alphaCentauri);
        distanceSystems4.add(sirius);
        distanceSystems4.add(betelgeuse);
        assertThat(routeService.getDistance(distanceSystems4))
                .isEqualTo(22);
        // negative Test

        List<String>distanceSystems5=new ArrayList<>();
        distanceSystems5.add(solarSystem);
        distanceSystems5.add(vega);
        distanceSystems5.add(betelgeuse);
        assertThat(routeService.getDistance(distanceSystems5)).isLessThan(0);

        List<String>distanceSystems6=new ArrayList<>();
        distanceSystems6.add(solarSystem);
        distanceSystems6.add(vega);
        distanceSystems6.add(betelgeuse);
        distanceSystems6.add("Andromeda");
        assertThat(routeService.getDistance(distanceSystems6)).isLessThan(0);

        //edgeCases
        assertThat(routeService.getDistance(new ArrayList<>())).isEqualTo(0);

   }
   @Test
   void getAllRoutesWithMaximumStopsTest(){
       Mockito.when(starSystemRepository.findById(sirius)).thenReturn(Optional.ofNullable(siriusEntity));
       Mockito.when(starSystemRepository.findById(vega)).thenReturn(Optional.ofNullable(vegaEntity));
       Mockito.when(starSystemRepository.findById(solarSystem)).thenReturn(Optional.ofNullable(solarSystemEntity));

       List<StarSystemEntity> path=new ArrayList<>();
       path.add(siriusEntity);
       path.add(betelgeuseEntity);
       path.add(siriusEntity);

       List<StarSystemEntity> path2=new ArrayList<>();
       path2.add(siriusEntity);
       path2.add(vegaEntity);
       path2.add(alphaCentauriEntity);
       path2.add(siriusEntity);
        //positive
        List<List<StarSystemEntity>> actual=routeService.getAllRoutesWithMaximumStops(sirius,sirius,3);
        assertThat(actual)
               .isNotNull()
               .hasSize(2)
               .contains(path2)
                .contains(path);

       //negative
       actual=routeService.getAllRoutesWithMaximumStops(vega,solarSystem,3);
       assertThat(actual)
               .isNotNull()
               .isEmpty();

       //edge case
       actual=routeService.getAllRoutesWithMaximumStops("Andromeda",solarSystem,3);
       assertThat(actual)
               .isNotNull()
               .isEmpty();

   }
    @Test
    void getAllRoutesWithExactStopsTest(){
        Mockito.when(starSystemRepository.findById(sirius)).thenReturn(Optional.ofNullable(siriusEntity));
        Mockito.when(starSystemRepository.findById(solarSystem)).thenReturn(Optional.ofNullable(solarSystemEntity));
        //positive
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
        List<List<StarSystemEntity>> actualPositive=routeService.getAllRoutesWithExactStops(solarSystem,sirius,3);
        assertThat(actualPositive)
                .isNotNull()
                .hasSize(3)
                .contains(path1)
                .contains(path2)
                .contains(path3);

        //negative
        List<List<StarSystemEntity>> actualNegative=routeService.getAllRoutesWithExactStops(solarSystem,betelgeuse,2);
        assertThat(actualNegative)
                .isNotNull()
                .isEmpty();

        //edge
        List<List<StarSystemEntity>> actualNegative2=routeService.getAllRoutesWithExactStops("AndroMeda",betelgeuse,2);
        assertThat(actualNegative2)
                .isNotNull()
                .isEmpty();
    }
    @Test
    void  getShortestRouteDurationTest(){
        Mockito.when(starSystemRepository.findById(sirius)).thenReturn(Optional.ofNullable(siriusEntity));
        Mockito.when(starSystemRepository.findById(solarSystem)).thenReturn(Optional.ofNullable(solarSystemEntity));

        List<StarSystemEntity> allEntities=new ArrayList<>();

        allEntities.add(solarSystemEntity);
        allEntities.add(alphaCentauriEntity);
        allEntities.add(siriusEntity);
        allEntities.add(vegaEntity);
        allEntities.add(betelgeuseEntity);
        Mockito.when(starSystemRepository.findAll()).thenReturn(allEntities);

        //positive
        int duration=routeService.getShortestRouteDuration(solarSystem,sirius);
        assertThat(duration).isEqualTo(9);



    }

    @Test
    void  getAllRoutesWithMaximumTravelTimeTest(){
        Mockito.when(starSystemRepository.findById(sirius)).thenReturn(Optional.ofNullable(siriusEntity));

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
        List<List<StarSystemEntity>> actualPositive=routeService.getAllRoutesWithMaximumTravelTime(sirius,sirius,30);
        assertThat(actualPositive)
                .isNotNull()
                .containsAll(expected)
                .hasSize(7);


        //negative
        List<List<StarSystemEntity>> actualNegative=routeService.getAllRoutesWithMaximumTravelTime(sirius,sirius,8);
        assertThat(actualNegative)
                .isNotNull()
                .isEmpty();

        //edge
        List<List<StarSystemEntity>> actualEdge=routeService.getAllRoutesWithMaximumTravelTime("Andromeda",sirius,80);
        assertThat(actualEdge)
                .isNotNull()
                .isEmpty();
    }


}
