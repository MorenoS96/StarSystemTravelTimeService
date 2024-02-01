package com.moreno.starsystemtraveltimeservice.model.validator;

import com.moreno.starsystemtraveltimeservice.model.dto.StarSystemsRequestDTO;
import com.moreno.starsystemtraveltimeservice.model.validator.exception.RequestInvalidException;
import org.springframework.stereotype.Component;


import java.util.List;
@Component
public class StarSystemsRequestValidator {

    public StarSystemsRequestValidator() {
    }

    public void validate(StarSystemsRequestDTO starSystemsRequestDTO)  {
        List<String> starsystemNames= starSystemsRequestDTO.getStarSystems();
       if(starsystemNames.stream().anyMatch(s-> s==null || s.isEmpty())){
           throw new RequestInvalidException("atleast one system name is empty");
       }
    }
}
