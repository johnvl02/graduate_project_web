package com.john.graduate_project.controller.services;

import com.john.graduate_project.model.Car;
import com.john.graduate_project.model.User;

import java.util.List;

public interface MainService {
    List<Car> homePage();

    String updateStatus(String id, String value, String username);
    List<Car> maps();
    Car findCar(String licence);
}
