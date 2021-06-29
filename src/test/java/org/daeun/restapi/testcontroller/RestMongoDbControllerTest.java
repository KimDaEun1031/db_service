package org.daeun.restapi.testcontroller;


import org.daeun.restapi.controller.RestMongoDbController;
import org.junit.jupiter.api.Test;

public class RestMongoDbControllerTest {

    @Test
    void insertToMongoDb() {
        RestMongoDbController controller = new RestMongoDbController();
        controller.insertToMongoDb();
    }
}