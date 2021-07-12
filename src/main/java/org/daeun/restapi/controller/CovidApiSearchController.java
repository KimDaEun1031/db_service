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
import java.util.*;

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

            List<String> sidoList = Arrays.asList(sido.split(","));
            log.info("sidoList = {}", sidoList);

            List<CovidVaccineStatVO> list = new ArrayList<>();

            list = covidVaccineStatRepository.findAllByBaseDateBetweenAndSidoIn(startDate, endDate, sidoList);
            log.info("list = {}", list);

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

    @GetMapping("/sido")
    public String mongoSidoInTest() {
//        List<CovidVaccineStatVO> list = new ArrayList<>();
        List<String> sidoList = new ArrayList<>();

        sidoList.add("전국");
        sidoList.add("서울특별시");

        String jsonInString = "";

        List<CovidVaccineStatVO> list = covidVaccineStatRepository.findAllByBaseDateBetweenAndSidoIn ("2021-04-01 00:00:00","2021-04-03 00:00:00",sidoList);
//        log.info("list = {}", list);

        return jsonInString;

    }

}
