package org.daeun.restapi.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CovidApiSearchControllerTest {

    @Test
    void searchTest() {
        CovidApiSearchController search = new CovidApiSearchController();
        search.mongoSidoInTest();
    }

}