package com.example.restapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestApiApplicationTests {

    @LocalServerPort
    int localServerPort;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    public void getCars() {
        ResponseEntity<CarDTO[]> response = testRestTemplate.getForEntity(getBaseUrl(), CarDTO[].class);

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        CarDTO[] carArray = response.getBody();
        Assertions.assertNotNull(carArray);
        Assertions.assertTrue(carArray.length > 2);
    }

    @Test
    public void getSingleCar() {
        ResponseEntity<CarDTO> response = testRestTemplate.getForEntity(getBaseUrl() + "/1", CarDTO.class);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

        CarDTO car = response.getBody();
        Assertions.assertNotNull(car);
        Assertions.assertNotNull(car.id());

        // Assert that we get 404 if there is no car with that id
        ResponseEntity<CarDTO> responseNotFound = testRestTemplate.getForEntity(getBaseUrl() + "/99", CarDTO.class);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), responseNotFound.getStatusCodeValue());
    }

    @Test
    public void addCar() {
        CarSpec carSpec = new CarSpec("Honda", 2006, 4800.45);

        ResponseEntity<CarDTO> response = testRestTemplate.postForEntity(getBaseUrl(), carSpec, CarDTO.class);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        CarDTO car = response.getBody();
        Assertions.assertNotNull(car);
        Assertions.assertNotNull(car.id());

        // Try sending in with faulty request body
        ResponseEntity<CarDTO> responseBadRequest = testRestTemplate.postForEntity(getBaseUrl(), BigDecimal.valueOf(2), CarDTO.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), responseBadRequest.getStatusCodeValue());
    }

    @Test
    public void updateCar() {
        ResponseEntity<CarDTO> response = testRestTemplate.getForEntity(getBaseUrl() + "/1", CarDTO.class);
        CarDTO car = response.getBody();
        String newModel = "Ford";

        // Update existing car with new model name but keep other data
        CarSpec carSpec = new CarSpec(newModel, car.modelYear(), car.price());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity entity = new HttpEntity(carSpec, headers);

        ResponseEntity<CarDTO> putResponse = testRestTemplate.exchange(getBaseUrl() + "/" + car.id(), HttpMethod.PUT, entity, CarDTO.class);

        Assertions.assertEquals(HttpStatus.OK.value(), putResponse.getStatusCodeValue());
        CarDTO updatedCar = putResponse.getBody();
        Assertions.assertEquals(car.id(), updatedCar.id());
        Assertions.assertEquals(newModel, updatedCar.model());
    }

    @Test
    public void deleteCar() {
        ResponseEntity<CarDTO[]> response = testRestTemplate.getForEntity(getBaseUrl(), CarDTO[].class);
        int originalSize = response.getBody().length;
        testRestTemplate.delete(getBaseUrl() + "/3");

        ResponseEntity<CarDTO[]> responseAfterDelete = testRestTemplate.getForEntity(getBaseUrl(), CarDTO[].class);
        int newSize = responseAfterDelete.getBody().length;
        Assertions.assertEquals(originalSize, newSize + 1);
    }

    private String getBaseUrl() {
        return "http://localhost:" + localServerPort + "/car";
    }
}
