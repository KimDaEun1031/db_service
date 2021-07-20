package org.daeun.db.controller;

import lombok.extern.slf4j.Slf4j;
import org.daeun.db.repository.CovidVaccineStatRepository;
import org.daeun.db.dao.CovidVaccineStatDAO;
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
    public void saveCovidVaccineStat(@RequestBody List<CovidVaccineStatDAO> data) {

        log.info("data = {}",data);
        for (CovidVaccineStatDAO vo: data) {
            List<CovidVaccineStatDAO> covidDaoList = covidVaccineStatRepository.findByBaseDateAndSido(vo.getBaseDate(), vo.getSido());

            if(covidDaoList.isEmpty()) {
                covidVaccineStatRepository.insert(vo);
                log.info("insert data success!");
            }
        }

        log.info("It already in the data!");
    }

}
