package com.john.graduate_project.controller.services;

import com.john.graduate_project.model.Car;
import com.john.graduate_project.model.User;

import java.util.List;

public interface MainService {
    List<Car> homePage();
    List<Object> profile(User user, String username);
    String updateStatus(String id, String value, User user);
    List<Car> maps();
}
