package com.example.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
public class Controller {

    private final CarService carService;

    @Autowired // Not needed but added for clarity
    public Controller(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/{id}")
    public CarDTO getCar(@PathVariable Integer id) {
        return carService.getCar(id);
    }

    @GetMapping()
    public List<CarDTO> getAllCars() {
        return carService.getAllCars();
    }

    @PostMapping
    public CarDTO addCar(@Validated @RequestBody CarSpec carSpec) {
        return carService.addCar(carSpec);
    }

    @PutMapping("/{id}")
    public CarDTO updateCar(@PathVariable Integer id, @Validated @RequestBody CarSpec carSpec) {
        return carService.updateCar(id, carSpec);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Integer id) {
        carService.deleteCar(id);
    }
}
