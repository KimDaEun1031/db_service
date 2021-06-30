package org.daeun.restapi.controller;

import com.google.gson.*;

import lombok.extern.slf4j.Slf4j;
import org.daeun.restapi.repository.CovidVaccineStatRepository;
import org.daeun.restapi.vo.CovidVaccineStatVO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
public class CovidApiBatchInsertController {

    @Autowired
    private CovidVaccineStatRepository covidVaccineStatRepository;

    @GetMapping("/batchInsertCovidVaccineStat")
    public void batchInsertCovidVaccineStat() {
        Map<String, Object> result = new HashMap<String, Object>();

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders header = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(header);

            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();

            LocalDate date = LocalDate.now();

            String url = String.format("http://localhost:9090/covidVaccineStat?month=%02d&day=%02d", date.getMonthValue(), date.getDayOfMonth());

            ResponseEntity<Map> resultMap = restTemplate.exchange(URI.create(url), HttpMethod.GET, entity, Map.class);
            result.put("statusCode", resultMap.getStatusCodeValue());
            result.put("header", resultMap.getHeaders());
            result.put("body", resultMap.getBody());

            String jsonInString = gson.toJson(resultMap.getBody());

            JsonElement element = jsonParser.parse(jsonInString);
            JsonArray row = (JsonArray) element.getAsJsonObject().get("data");

            List<CovidVaccineStatVO> batchList = new ArrayList<>();

            for (int j = 0; j < row.size(); j++) {
                JsonObject rowList = (JsonObject) row.get(j);

                CovidVaccineStatVO covidVO = gson.fromJson(rowList, CovidVaccineStatVO.class);
                batchList.add(covidVO);

            }
//          covidVaccineStatRepository.insert(batchList);

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

    @GetMapping("/batchSearchCovidVaccineStat")
    public List<CovidVaccineStatVO> batchSearchCovidVaccineStat() {
        List<CovidVaccineStatVO> covidList = covidVaccineStatRepository.findAll();
        return covidList;
    }

}