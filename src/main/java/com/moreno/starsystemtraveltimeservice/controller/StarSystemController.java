package com.moreno.starsystemtraveltimeservice.controller;

import com.moreno.starsystemtraveltimeservice.model.dto.*;
import com.moreno.starsystemtraveltimeservice.model.entity.StarSystemEntity;
import com.moreno.starsystemtraveltimeservice.model.validator.RoutesRequestValidator;
import com.moreno.starsystemtraveltimeservice.model.validator.StarSystemsRequestValidator;
import com.moreno.starsystemtraveltimeservice.model.validator.exception.RequestInvalidException;
import com.moreno.starsystemtraveltimeservice.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class StarSystemController {
    private final RouteService routeService;

    private final StarSystemsRequestValidator starSystemsRequestValidator;
    private final RoutesRequestValidator routesRequestValidator;
    @Autowired
    public StarSystemController(RouteService routeService, StarSystemsRequestValidator starSystemsRequestValidator, RoutesRequestValidator routesRequestValidator) {
        this.routeService = routeService;
        this.starSystemsRequestValidator = starSystemsRequestValidator;
        this.routesRequestValidator = routesRequestValidator;
    }
    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<?> hello(){
        return ResponseEntity.ok("app is running");
    }
    @PostMapping("/api/distance")
    @ResponseBody
    public ResponseEntity<?> getDistance(@RequestBody StarSystemsRequestDTO request){
        starSystemsRequestValidator.validate(request);
        int distance=routeService.getDistance( request.getStarSystems());
        if(distance<0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO SUCH ROUTE");
        }
        return ResponseEntity.ok(convertToDurationResponseDTO(distance,"hours"));
    }

    @PostMapping("/api/maximum-stops-routes")
    @ResponseBody
    public ResponseEntity<?> getRoutesWithMaximumStops(@RequestBody RoutesRequestDTO routesRequestDTO){
        routesRequestValidator.validate(routesRequestDTO,false,true,false);
       List<List<StarSystemEntity>> routes= routeService.getAllRoutesWithMaximumStops(routesRequestDTO.getStartSystemName(),routesRequestDTO.getDestinationSystemName(),routesRequestDTO.getMaxStops());
      if(routes.isEmpty()){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO SUCH ROUTE");
      }
       return ResponseEntity.ok(convertToRouteResponseStopsList(routes));
    }

    @PostMapping("/api/shortest-route-duration")
    @ResponseBody
    public ResponseEntity<?> getDurationOfShortestRoute(@RequestBody RoutesRequestDTO routesRequestDTO){
        routesRequestValidator.validate(routesRequestDTO,false,false,false);
        int duration= routeService.getShortestRouteDuration(routesRequestDTO.getStartSystemName(),routesRequestDTO.getDestinationSystemName());
        if(duration<0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO SUCH ROUTE");
        }
        return ResponseEntity.ok(convertToDurationResponseDTO(duration,"hours"));
    }

    @PostMapping("/api/exact-stops-routes")
    @ResponseBody
    public ResponseEntity<?> getRoutesWithExactStops(@RequestBody RoutesRequestDTO routesRequestDTO){
        routesRequestValidator.validate(routesRequestDTO,false,false,true);
        List<List<StarSystemEntity>> routes= routeService.getAllRoutesWithExactStops(routesRequestDTO.getStartSystemName(),routesRequestDTO.getDestinationSystemName(), routesRequestDTO.getExactStops());

        if(routes.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO SUCH ROUTE");
        }
        return ResponseEntity.ok(convertToRouteResponseList(routes));
    }

    @PostMapping("/api/maximum-travel-time-routes")
    @ResponseBody
    public ResponseEntity<?> getRoutesWithMaximumTravelTime(@RequestBody RoutesRequestDTO routesRequestDTO){
        routesRequestValidator.validate(routesRequestDTO,true,false,false);
        List<List<StarSystemEntity>> routes=routeService.getAllRoutesWithMaximumTravelTime(routesRequestDTO.getStartSystemName(),routesRequestDTO.getDestinationSystemName(),routesRequestDTO.getMaxTravelTime());
        if(routes.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO SUCH ROUTE");
        }
        return ResponseEntity.ok(convertToRouteResponseList(routes));
    }
    @ExceptionHandler(RequestInvalidException.class)
    @ResponseBody
    public ResponseEntity<?> exceptionHandlerExpected(Exception ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler()
    @ResponseBody
    public ResponseEntity<?> exceptionHandler(Exception ex){
        return ResponseEntity.internalServerError().build();
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
