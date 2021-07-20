package org.daeun.db.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;

import org.daeun.db.repository.CovidVaccineStatRepository;
import org.daeun.db.dao.CovidVaccineStatDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


@RestController
@Slf4j
public class CovidApiRowInsertController {

    @Autowired
    private CovidVaccineStatRepository covidVaccineStatRepository;

    @GetMapping("/insertCovidVaccineStat")
    public void insertCovidVaccineStat() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            Gson gson = new Gson();
            JsonParser gsonParser = new JsonParser();

            LocalDate date = LocalDate.now();
            String sido = "전국";

            String url = String.format("http://localhost:9090/covidVaccineStat?month=%02d&day=%02d&sido=%s",date.getMonthValue(),date.getDayOfMonth(), URLEncoder.encode(sido, "UTF-8"));
            log.info("url = {}",url);

            ResponseEntity<Map> resultMap = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            String jsonInString = gson.toJson(resultMap.getBody());

            JsonElement element = gsonParser.parse(jsonInString);
            JsonArray row = (JsonArray) element.getAsJsonObject().get("data");
            for (int i=0; i<row.size(); i++) {
                JsonObject rowList = (JsonObject) row.get(i);

                CovidVaccineStatDAO covidDAO = gson.fromJson(rowList, CovidVaccineStatDAO.class);

//                    covidVaccineStatRepository.insert(covidDAO);
                log.info("result = {}", covidDAO);
                }


        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.info(e.toString());

        } catch (Exception e) {
            log.info(e.toString());
        }

    }


}
