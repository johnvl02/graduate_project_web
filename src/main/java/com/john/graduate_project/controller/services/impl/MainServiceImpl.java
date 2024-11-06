package com.john.graduate_project.controller.services.impl;

import com.john.graduate_project.controller.services.MainService;
import com.john.graduate_project.model.*;
import com.john.graduate_project.repository.RenterInfoIRepository;
import com.john.graduate_project.repository.UserRepository;
import com.john.graduate_project.repository.service.CarServices;
import com.john.graduate_project.repository.service.RequestCarServices;
import com.john.graduate_project.repository.service.ReviewUserServices;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MainServiceImpl implements MainService {

    private final CarServices carServices;
    private final UserRepository userRepository;
    private final ReviewUserServices reviewUserServices;
    private final RenterInfoIRepository renterInfoIRepository;
    private final RequestCarServices requestCarServices;

    public MainServiceImpl(CarServices carServices, UserRepository userRepository, ReviewUserServices reviewUserServices, RenterInfoIRepository renterInfoIRepository,
                           RequestCarServices requestCarServices) {
        this.carServices = carServices;
        this.userRepository = userRepository;
        this.reviewUserServices = reviewUserServices;
        this.renterInfoIRepository = renterInfoIRepository;
        this.requestCarServices = requestCarServices;
    }

    @Override
    public List<Car> homePage() {
        return carServices.findAvailableCar(0,12,"descending");//If a car was unavailable the value will update with this else the value will not be updated
    }


    @Override
    public String updateStatus(String id, String value, String username) {
        String[] str = id.split(" ");// render,license,date
        RequestCar rc = requestCarServices.findByString(username,str[1], LocalDate.parse(str[2]), str[0]);
        rc.setStatus(value);
        rc.setUpdateDate(LocalDate.now());
        RequestCar requestCar = requestCarServices.saveRequest(rc);
        if (requestCar != null) {
           return "The request updated successfully";
        }
        else {
            return  "Something went wrong and the request don't updated successfully";
        }
    }

    @Override
    public List<Car> maps() {
        return carServices.findAllCars();
    }

    @Override
    public Car findCar(String license) {
        Optional<Car> optionalCar = carServices.findById(license);
        return optionalCar.get();
    }
}
