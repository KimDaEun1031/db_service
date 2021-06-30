package org.daeun.restapi;

import lombok.extern.slf4j.Slf4j;
import org.daeun.restapi.controller.CovidApiRowInsertController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CovidApiSceduler {

    @Autowired
    CovidApiRowInsertController covidApiRowInsertController;

    @Scheduled(cron = "0 0 10 * * ?")
    public void insertCovidVaccineStatToMongoDb(){
        log.info("Insert Success : ");
        covidApiRowInsertController.insertCovidVaccineStat();

    }
}
