package org.daeun.RestAPI_DBsave.Controller;


import org.junit.jupiter.api.Test;

public class RestMongoDbControllerTest {

    @Test
    void insertToMongoDb() {
        RestMongoDbController controller = new RestMongoDbController();
        controller.insertToMongoDb();
    }
}