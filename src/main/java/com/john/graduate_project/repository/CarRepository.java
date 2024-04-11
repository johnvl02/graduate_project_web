package com.john.graduate_project.repository;

import com.john.graduate_project.model.Car;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car, String> {

}
