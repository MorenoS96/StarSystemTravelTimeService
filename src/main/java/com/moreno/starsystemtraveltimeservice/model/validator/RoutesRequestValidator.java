package com.moreno.starsystemtraveltimeservice.model.validator;

import com.moreno.starsystemtraveltimeservice.model.dto.RoutesRequestDTO;
import com.moreno.starsystemtraveltimeservice.model.validator.exception.RequestInvalidException;
import org.springframework.stereotype.Component;

@Component
public class RoutesRequestValidator {
    public void validate(RoutesRequestDTO routesRequestDTO,boolean maxTravelTimeNeeded,boolean maxStopsNeeded,boolean exactStopsNeeded) throws RequestInvalidException {
        String startName=routesRequestDTO.getStartSystemName();
        String destName=routesRequestDTO.getDestinationSystemName();

        if(startName==null ||startName.isEmpty() ){
            throw new RequestInvalidException("Parameter startSystemName is empty");
        }
        if(destName==null ||destName.isEmpty() ){
            throw new RequestInvalidException("Parameter destinationSystemName is empty");
        }
        if(maxTravelTimeNeeded){
            if(routesRequestDTO.getMaxTravelTime()==null){
                throw new RequestInvalidException("Parameter maxTravelTime is empty");
            }
            if(routesRequestDTO.getMaxTravelTime()<0){
                numberHasToBePositive("maxTravelTime");
            }

        }
        if(maxStopsNeeded){
            if(routesRequestDTO.getMaxStops()==null){
                throw new RequestInvalidException("Parameter maxStops is empty");
            }
            if(routesRequestDTO.getMaxStops()<0){
                numberHasToBePositive("maxStops");
            }
        }
        if(exactStopsNeeded){
            if(routesRequestDTO.getExactStops()==null){
                throw new RequestInvalidException("Parameter exactStops is empty");
            }
            if(routesRequestDTO.getExactStops()<0){
                numberHasToBePositive("exactStops");
            }
        }




    }
    private void numberHasToBePositive(String param){
        throw new RequestInvalidException("Parameter" + param + " has be to a positive Integer");
    }
}
