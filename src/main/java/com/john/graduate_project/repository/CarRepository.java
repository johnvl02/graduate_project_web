package com.john.graduate_project.repository;

import com.john.graduate_project.model.Car;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CarRepository extends CrudRepository<Car, String>, PagingAndSortingRepository<Car, String> {


}
