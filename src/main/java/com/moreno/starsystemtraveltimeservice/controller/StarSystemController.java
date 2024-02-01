package com.moreno.starsystemtraveltimeservice.controller;

import com.moreno.starsystemtraveltimeservice.model.dto.*;
import com.moreno.starsystemtraveltimeservice.model.entity.StarSystemEntity;
import com.moreno.starsystemtraveltimeservice.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class StarSystemController {
    private final RouteService routeService;
    @Autowired
    public StarSystemController(RouteService routeService) {
        this.routeService = routeService;
    }
    @GetMapping("/api/")
    public String testConnection(){
        return routeService.testConnection();
    }

    @PostMapping("/api/distance")
    @ResponseBody
    public ResponseEntity<?> getDistance(@RequestBody StarSystemsRequest request){
        int distance=routeService.getDistance( request.getStarSystems());
        if(distance<0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO SUCH ROUTE");
        }
        return ResponseEntity.ok(convertToDurationResponseDTO(distance,"hours"));
    }

    @PostMapping("/api/maximum-stops-routes")
    @ResponseBody
    public ResponseEntity<?> getRoutesWithMaximumStops(@RequestBody RoutesRequestDTO routesRequestDTO){
       List<List<StarSystemEntity>> routes= routeService.getAllRoutesWithMaximumStops(routesRequestDTO.getStartSystemName(),routesRequestDTO.getDestinationSystemName(),routesRequestDTO.getMaxStops());
      if(routes.isEmpty()){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO SUCH ROUTE");
      }
       return ResponseEntity.ok(convertToRouteResponseStopsList(routes));
    }

    @PostMapping("/api/shortest-route-duration")
    @ResponseBody
    public ResponseEntity<?> getDurationOfShortestRoute(@RequestBody RoutesRequestDTO routesRequestDTO){

        int duration= routeService.getShortestRouteDuration(routesRequestDTO.getStartSystemName(),routesRequestDTO.getDestinationSystemName());
        if(duration<0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO SUCH ROUTE");
        }
        return ResponseEntity.ok(convertToDurationResponseDTO(duration,"hours"));
    }

    @PostMapping("/api/exact-stops-routes")
    @ResponseBody
    public ResponseEntity<?> getRoutesWithExactStops(@RequestBody RoutesRequestDTO routesRequestDTO){
        List<List<StarSystemEntity>> routes= routeService.getAllRoutesWithExactStops(routesRequestDTO.getStartSystemName(),routesRequestDTO.getDestinationSystemName(), routesRequestDTO.getExactStops());

        if(routes.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO SUCH ROUTE");
        }
        return ResponseEntity.ok(convertToRouteResponseList(routes));
    }

    @PostMapping("/api/maximum-travel-time-routes")
    @ResponseBody
    public ResponseEntity<?> getRoutesWithMaximumTravelTime(@RequestBody RoutesRequestDTO routesRequestDTO){
        List<List<StarSystemEntity>> routes=routeService.getAllRoutesWithMaximumTravelTime(routesRequestDTO.getStartSystemName(),routesRequestDTO.getDestinationSystemName(),routesRequestDTO.getMaxTravelTime());
        if(routes.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO SUCH ROUTE");
        }
        return ResponseEntity.ok(convertToRouteResponseList(routes));
    }
    @ExceptionHandler
    @ResponseBody
    public ResponseEntity<?> exceptionHandler(Exception ex){
        return ResponseEntity.badRequest().body(ex.getClass().descriptorString());
    }

    private DurationResponseDTO convertToDurationResponseDTO(int number, String unit){
        return new DurationResponseDTO(number,unit);
    }
    private RouteResponseDTO convertToRouteResponseDTO(List<StarSystemEntity> path){
        List<String> starSystemNames=path.stream().map(StarSystemEntity::getName).toList();
        return new RouteResponseDTO(starSystemNames);
    }
    private RouteResponseStopsDTO convertToRouteResponseStopsDTO(List<StarSystemEntity> path){
        List<String> starSystemNames=path.stream().map(StarSystemEntity::getName).toList();
        return new RouteResponseStopsDTO(starSystemNames);

    }
    private List<RouteResponseDTO> convertToRouteResponseList(List<List<StarSystemEntity>> paths){
        return paths.stream().map(this::convertToRouteResponseDTO).toList();
    }
    private List<RouteResponseStopsDTO> convertToRouteResponseStopsList(List<List<StarSystemEntity>> paths){
        return paths.stream().map(this::convertToRouteResponseStopsDTO).toList();
    }

}
