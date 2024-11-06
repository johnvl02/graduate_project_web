package com.john.graduate_project.repository.service;

import com.john.graduate_project.model.Car;
import com.john.graduate_project.model.RequestCar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CarServices {
    List<Car> findByNama(String name);
    List<Car> findAvailableCar(int page, int size , String sort);
    List<Car> findAllCars();
    Car saveCar(Car car);
    Optional<Car> findById(String  licence);
    Page<Car> findCarPage(Pageable pageable);
    List<RequestCar> sortRequest(List<RequestCar> carList);
}
