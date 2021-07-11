package org.daeun.restapi.controller;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.daeun.restapi.repository.CovidVaccineStatRepository;
import org.daeun.restapi.vo.CovidVaccineStatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class CovidApiSearchController {
    @Autowired
    private CovidVaccineStatRepository covidVaccineStatRepository;

    @GetMapping("/searchCovidVaccineStat")
    public String searchCovidVaccineStat(@RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate dateTime,
                                         @RequestParam(required = false, defaultValue = "전국") String sido) {

        String search = "";
        try {

            List<CovidVaccineStatVO> list = new ArrayList<>();

            String baseDate = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));

            log.info(baseDate);

            list = covidVaccineStatRepository.findByBaseDateAndSido(baseDate, sido);

            for (int j=0; j<list.size(); j++) {
                search = String.valueOf(list);

            }
            log.info("success");

        }
        catch (Exception e) {
            log.info("error");
            log.info(e.toString());
        }

        return search;
    }


    @GetMapping("/searchPeriodDataCovidVaccineStat")
    public String searchPeriodDataCovidVaccineStat(String startDate, String endDate, String sido) {
        Map<String, Object> result = new HashMap<String, Object>();

        log.info("date = {}", startDate);
        log.info("date = {}", endDate);

        String jsonInString = "";
        try {
            startDate += " 00:00:00";
            endDate += " 00:00:00";

            List<CovidVaccineStatVO> list = new ArrayList<>();

            list = covidVaccineStatRepository.findByBaseDateBetweenAndSido(startDate, endDate, sido);

            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();

            String jsonList = gson.toJson(list);
            JsonElement element = jsonParser.parse(jsonList);
            jsonInString = String.valueOf(element);

            log.info("data = {} ", jsonInString);

            log.info("SUCCESS");


        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body", e.getStatusText());
            log.error(e.toString());

        } catch (Exception e) {
            result.put("statusCode", "999");
            result.put("body", "excpetion 오류");
            log.error(e.toString());
        }

        return jsonInString;
    }


}
