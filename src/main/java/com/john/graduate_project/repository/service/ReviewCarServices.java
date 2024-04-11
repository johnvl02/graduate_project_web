package com.john.graduate_project.repository.service;

import com.john.graduate_project.model.ReviewForCar;

import java.util.List;

public interface ReviewCarServices {
    List<ReviewForCar> findByLicence(String licence);
    ReviewForCar findReview(String username, String licence);
    ReviewForCar save(ReviewForCar review);
}
