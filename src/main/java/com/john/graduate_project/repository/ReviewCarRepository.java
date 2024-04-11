package com.john.graduate_project.repository;

import com.john.graduate_project.model.ReviewForCar;
import org.springframework.data.repository.CrudRepository;

public interface ReviewCarRepository extends CrudRepository<ReviewForCar, String> {
}
