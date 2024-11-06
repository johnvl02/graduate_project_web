package com.john.graduate_project.repository.service;

import com.john.graduate_project.model.RequestCar;

import java.time.LocalDate;
import java.util.List;

public interface RequestCarServices {
    RequestCar findByString(String owner, String license, LocalDate date, String renter);
    List<RequestCar> findByLicense(String license);

    RequestCar saveRequest(RequestCar requestCar);

    List<RequestCar> findAll();

    /*
    default List<RequestCar> findByName(String name){
        List<RequestCar> requestCar1 = new ArrayList<>();
        findAll().forEach(requestCar -> { if (requestCar.getOwner_username().equals(name))requestCar1.add(requestCar);});
        return requestCar1;
    }*/
}
