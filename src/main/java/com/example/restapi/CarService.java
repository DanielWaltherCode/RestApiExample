package com.example.restapi;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CarService {

    private List<CarDTO> carList = new ArrayList<>();

    public CarDTO addCar(CarSpec carSpec) {
        CarDTO carToAdd = new CarDTO(
                getNewId(),
                carSpec.model(),
                carSpec.modelYear(),
                carSpec.price()
        );
        carList.add(carToAdd);
        return carToAdd;
    }

    public CarDTO getCar(Integer carId) {
        return carList.stream()
                .filter(car -> car.id().equals(carId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No car with that id found"));
    }
    public List<CarDTO> getAllCars() {
        return carList;
    }

    public CarDTO updateCar(Integer id, CarSpec carSpec) {
        int placeInlist = -1;
        for (int i = 0; i < carList.size(); i++) {
            if (carList.get(i).id().equals(id)) {
                placeInlist = i;
            }
        }
        if (placeInlist == -1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No car with that id found");
        }
        else {
            CarDTO carToAdd = new CarDTO(
                    id,
                    carSpec.model(),
                    carSpec.modelYear(),
                    carSpec.price()
            );
            carList.set(placeInlist, carToAdd);
            return carToAdd;
        }
    }

    public void deleteCar(Integer id) {
        int placeInlist = -1;
        for (int i = 0; i < carList.size(); i++) {
            if (carList.get(i).id().equals(id)) {
                placeInlist = i;
            }
        }
        if (placeInlist == -1) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No car with that id found");
        }
        else {
            carList.remove(placeInlist);
        }
    }

    private Integer getNewId() {
        if (carList.isEmpty()) {
            return 1;
        }
        int currentMax = carList.stream()
                .map(CarDTO::id)
                .mapToInt(c -> c)
                .max().orElseThrow(NoSuchElementException::new);
        return currentMax + 1;
    }
}
