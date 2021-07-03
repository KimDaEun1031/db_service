package org.daeun.restapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.daeun.restapi.repository.CovidVaccineStatRepository;
import org.daeun.restapi.vo.CovidVaccineStatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class CovidApiSearchController {
    @Autowired
    private CovidVaccineStatRepository covidVaccineStatRepository;

    @GetMapping("/searchCovidVaccineStat")
    public String searchCovidVaccineStat(@RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate dateTime,
                                         @RequestParam(required = false, defaultValue = "전국") String sido) {

        //defaultValue = "#{T(java.time.LocalDateTime).now()}"
        String search = "";
        try {
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd", Locale.ENGLISH);
//            LocalDateTime date = simpleDateFormat.parse(baseDate);
//
//            SimpleDateFormat tranSimpleFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.ENGLISH);
//            baseDate = "20"+tranSimpleFormat.format(date);
//

            List<CovidVaccineStatVO> list = new ArrayList<>();


            String baseDate = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));

            log.info(baseDate);

            list = covidVaccineStatRepository.findByBaseDate(baseDate);

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


    @GetMapping("/batchSearchCovidVaccineStat")
    public List<CovidVaccineStatVO> batchSearchCovidVaccineStat() {
        List<CovidVaccineStatVO> covidList = covidVaccineStatRepository.findAll();
        return covidList;
    }

}
