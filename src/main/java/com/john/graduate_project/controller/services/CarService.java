package com.john.graduate_project.controller.services;

import com.john.graduate_project.model.Car;
import com.john.graduate_project.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


//@Service
public interface CarService {

    List<Car> availableCar();
    String addCar(User user, Car car, MultipartFile multipartFile);
    Car newCar();
    List<Object> showCar(String license, User user);
    String rentCar(int days, String license,User user);
    List<Object> request(User user);
}
