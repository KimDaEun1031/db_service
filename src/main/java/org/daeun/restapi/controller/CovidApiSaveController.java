package org.daeun.restapi.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class CovidApiSaveController {

    @PostMapping ("/saveCovidVaccineStat")
    @ResponseBody
    public Object saveCovidVaccineStat(@RequestBody JsonArray arrayData) {

        log.info(String.valueOf(arrayData));
//        String jsonInString = "";
//
//        Gson gson = new Gson();
//        JsonParser jsonParser = new JsonParser();
//
//        jsonInString = gson.toJson(jsonData);

        return arrayData;
    }
}
