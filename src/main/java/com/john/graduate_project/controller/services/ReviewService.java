package com.john.graduate_project.controller.services;

import com.john.graduate_project.model.Car;
import com.john.graduate_project.model.ReviewForCar;
import com.john.graduate_project.model.ReviewForUser;
import com.john.graduate_project.model.User;

public interface ReviewService {

    Car requestReviewCar(String licence, String date, String username);
    String reviewCar(ReviewForCar review);
    User requestReviewUser(String licence, String date, String username);
    String reviewUser(ReviewForUser review);
}
