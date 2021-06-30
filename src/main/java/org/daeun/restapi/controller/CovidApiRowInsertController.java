package org.daeun.restapi.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.daeun.restapi.repository.CovidVaccineStatRepository;
import org.daeun.restapi.vo.CovidVaccineStatVO;
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
        HashMap<String, Object> result = new HashMap<String, Object>();

        try {
                RestTemplate restTemplate = new RestTemplate();

                HttpHeaders header = new HttpHeaders();
                HttpEntity<?> entity = new HttpEntity<>(header);

                Gson gson = new Gson();
                JsonParser gsonParser = new JsonParser();

                LocalDate date = LocalDate.now();

                String url = String.format("http://localhost:9090/covidVaccineStat?month=%02d&day=%02d",date.getMonthValue(),date.getDayOfMonth());

                ResponseEntity<Map> resultMap = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
                result.put("statusCode", resultMap.getStatusCodeValue());
                result.put("header", resultMap.getHeaders());
                result.put("body", resultMap.getBody());

                String jsonInString = gson.toJson(resultMap.getBody());

                JsonElement element = gsonParser.parse(jsonInString);
                JsonArray row = (JsonArray) element.getAsJsonObject().get("data");
                for (int i=0; i<row.size(); i++) {
                    JsonObject rowList = (JsonObject) row.get(i);

                    CovidVaccineStatVO covidVO = gson.fromJson(rowList, CovidVaccineStatVO.class);

                    covidVaccineStatRepository.insert(covidVO);
                    }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body", e.getStatusText());
            log.info(e.toString());

        } catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body", "excpetion 오류");
            log.info(e.toString());
        }

    }


}
