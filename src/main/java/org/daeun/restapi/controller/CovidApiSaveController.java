package org.daeun.restapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.daeun.restapi.repository.CovidVaccineStatRepository;
import org.daeun.restapi.vo.CovidVaccineStatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class CovidApiSaveController {

    @Autowired
    CovidVaccineStatRepository covidVaccineStatRepository;

    @PostMapping (value = "/saveCovidVaccineStat", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveCovidVaccineStat(@RequestBody List<CovidVaccineStatVO> data) {

        log.info("data = {}",data);
//        covidVaccineStatRepository.insert(data);
        for (CovidVaccineStatVO vo: data) {
            List<CovidVaccineStatVO> covidVoList = covidVaccineStatRepository.findByBaseDateAndSido(vo.getBaseDate(), vo.getSido());

            if(covidVoList.isEmpty()) {
                covidVaccineStatRepository.insert(vo);
                log.info("insert data success!");
            }
        }

        log.info("It already in the data!");


    }

}
