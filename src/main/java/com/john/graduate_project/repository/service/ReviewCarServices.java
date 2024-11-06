package com.john.graduate_project.repository.service;

import com.john.graduate_project.model.ReviewForCar;

import java.util.List;

public interface ReviewCarServices {
    List<ReviewForCar> findByLicense(String license);
    ReviewForCar findReview(String username, String license);
    ReviewForCar save(ReviewForCar review);
}
