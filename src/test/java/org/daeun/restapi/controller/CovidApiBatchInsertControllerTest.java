package org.daeun.restapi.controller;

import org.junit.jupiter.api.Test;

public class CovidApiBatchInsertControllerTest {

    @Test
    void batchInsert() {
        CovidApiBatchInsertController controller = new CovidApiBatchInsertController();
        controller.batchInsertCovidVaccineStat();
    }
}