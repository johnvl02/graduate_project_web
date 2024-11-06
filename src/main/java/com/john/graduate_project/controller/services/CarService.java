package com.john.graduate_project.controller.services;

import com.john.graduate_project.model.Car;
import com.john.graduate_project.model.ReviewForCar;
import com.john.graduate_project.model.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


//@Service
public interface CarService {

    List<Car> availableCar(int page, int size, String sort);
    int findPages();
    String addCar(String username, Car car, MultipartFile multipartFile);
    String addCar(Car car);
    Car getCar(String license);
    String editCar(String username, Car car, MultipartFile multipartFile);
    String editCar(Car car);
    String deleteCar(String license);
    Car newCar();
    Car showCar(String license, String username);
    List<ReviewForCar> getReviews(String license);
    String rentCar(String days, String license, String username);
    List<Object> request(String username);
    Page<Car> showCarPage(int page, int size, String sort);
    List<String> reserveDates(String license);
}
