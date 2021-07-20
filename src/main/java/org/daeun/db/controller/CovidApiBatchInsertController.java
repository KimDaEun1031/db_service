package org.daeun.db.controller;

import com.google.gson.*;

import lombok.extern.slf4j.Slf4j;
import org.daeun.db.repository.CovidVaccineStatRepository;
import org.daeun.db.dao.CovidVaccineStatDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
public class CovidApiBatchInsertController {

    @Autowired
    private CovidVaccineStatRepository covidVaccineStatRepository;

    @GetMapping("/batchInsertCovidVaccineStat")
    public void batchInsertCovidVaccineStat() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();

            LocalDate date = LocalDate.now().minusDays(1);

            String url = String.format("http://localhost:9090/covidVaccineStat?month=%02d&day=%02d", date.getMonthValue(), date.getDayOfMonth());
            log.info("url = {}",url);

            ResponseEntity<Map> resultMap = restTemplate.exchange(URI.create(url), HttpMethod.GET, entity, Map.class);

            String jsonInString = gson.toJson(resultMap.getBody());

            JsonElement element = jsonParser.parse(jsonInString);
            JsonArray row = (JsonArray) element.getAsJsonObject().get("data");

            List<CovidVaccineStatDAO> batchList = new ArrayList<>();

            for (int j = 0; j < row.size(); j++) {
                JsonObject rowList = (JsonObject) row.get(j);

                CovidVaccineStatDAO covidDAO = gson.fromJson(rowList, CovidVaccineStatDAO.class);
                batchList.add(covidDAO);

            }

          covidVaccineStatRepository.insert(batchList);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.info(e.toString());

        } catch (Exception e) {
            log.info(e.toString());
        }
    }

    @GetMapping("/CovidVaccineStatTotal")
    public String CovidVaccineStatTotal() {

        String jsonInString = "";
        int totalCount = 0;

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();

            String url = String.format("http://localhost:9090/covidVaccineStatBatch?totalCount=%d", totalCount);
            log.info("url = {}",url);

            ResponseEntity<Map> resultMap = restTemplate.exchange(URI.create(url), HttpMethod.GET, entity, Map.class);

            jsonInString = gson.toJson(resultMap.getBody());

            JsonElement element = jsonParser.parse(jsonInString);
            JsonArray row = (JsonArray) element.getAsJsonObject().get("data");

            List<CovidVaccineStatDAO> batchList = new ArrayList<>();

            for (int j = 0; j < row.size(); j++) {
                JsonObject rowList = (JsonObject) row.get(j);

                CovidVaccineStatDAO covidDAO = gson.fromJson(rowList, CovidVaccineStatDAO.class);
                batchList.add(covidDAO);

            }

//            covidVaccineStatRepository.insert(batchList);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.info(e.toString());

        } catch (Exception e) {
            log.info(e.toString());
        }

        return jsonInString;
    }


}
