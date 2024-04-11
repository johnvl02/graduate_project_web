package com.john.graduate_project.controller.services;

import com.john.graduate_project.model.Car;
import com.john.graduate_project.model.ReviewForCar;
import com.john.graduate_project.model.ReviewForUser;
import com.john.graduate_project.model.User;

public interface ReviewService {

    Car requestReviewCar(String licence, String date, User user);
    String reviewCar(ReviewForCar review, User user);
    User requestReviewUser(User user, String licence, String date, String username);
    String reviewUser(ReviewForUser review);
}
