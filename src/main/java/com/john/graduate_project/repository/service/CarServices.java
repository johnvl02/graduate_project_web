package com.john.graduate_project.repository.service;

import com.john.graduate_project.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarServices {
    List<Car> findByNama(String name);
    List<Car> findAvailableCar();
    List<Car> findAllCars();
    Car saveCar(Car car);
    Optional<Car> findById(String  licence);
}
