package com.example.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class RestApiApplication {

    private final CarService carService;

    @Autowired
    public RestApiApplication(CarService carService) {
        this.carService = carService;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }

    @PostConstruct
    public void addDefaultCars() {
        carService.addCar(new CarSpec(
                "Volvo",
                2014,
                14400.22
        ));
        carService.addCar(new CarSpec(
                "Tesla",
                2018,
                24400.50
        ));
        carService.addCar(new CarSpec(
                "Huyndai",
                2015,
                12400.10
        ));
    }

}
