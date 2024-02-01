package com.moreno.starsystemtraveltimeservice.model.validator;

import com.moreno.starsystemtraveltimeservice.model.dto.StarSystemsRequest;
import com.moreno.starsystemtraveltimeservice.model.validator.exception.RequestInvalidException;
import org.springframework.stereotype.Component;


import java.util.List;
@Component
public class StarSystemsRequestValidator {

    public StarSystemsRequestValidator() {
    }

    public void validate(StarSystemsRequest starSystemsRequest)  {
        List<String> starsystemNames=starSystemsRequest.getStarSystems();
       if(starsystemNames.stream().anyMatch(s-> s==null || s.isEmpty())){
           throw new RequestInvalidException("atleast one system name is empty");
       }
    }
}
