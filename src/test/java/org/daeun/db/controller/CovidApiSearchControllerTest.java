package org.daeun.db.controller;

import org.junit.jupiter.api.Test;

class CovidApiSearchControllerTest {

    @Test
    void searchTest() {
        CovidApiSearchController search = new CovidApiSearchController();
        search.mongoSidoInTest();
    }

}