package com.john.graduate_project.controller.services.impl;

import com.john.graduate_project.controller.services.ReviewService;
import com.john.graduate_project.model.*;
import com.john.graduate_project.repository.UserRepository;
import com.john.graduate_project.repository.service.CarServices;
import com.john.graduate_project.repository.service.RequestCarServices;
import com.john.graduate_project.repository.service.ReviewCarServices;
import com.john.graduate_project.repository.service.ReviewUserServices;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final CarServices carServices;
    private final RequestCarServices requestCarServices;
    private final ReviewCarServices reviewCarServices;
    private final UserRepository userRepository;
    private final ReviewUserServices reviewUserServices;

    public ReviewServiceImpl(CarServices carServices, RequestCarServices requestCarServices, ReviewCarServices reviewCarServices, UserRepository userRepository, ReviewUserServices reviewUserServices) {
        this.carServices = carServices;
        this.requestCarServices = requestCarServices;
        this.reviewCarServices = reviewCarServices;
        this.userRepository = userRepository;
        this.reviewUserServices = reviewUserServices;
    }

    @Override
    public Car requestReviewCar(String licence, String date, User user) {
        Optional<Car> car = carServices.findById(licence);
        if (car.isPresent()){
            RequestCar requestCar = requestCarServices.findByString(car.get().getOwner_username(), car.get().getLicence(), LocalDate.parse(date), user.getUsername());
            requestCar.setCanReviewCar(1);
            requestCarServices.saveRequest(requestCar);
            return car.get();
        }
        else return null;
    }

    @Override
    public String reviewCar(ReviewForCar review, User user) {
        ReviewForCar reviewForCar = reviewCarServices.save(review);
        StringBuilder stringBuilder = new StringBuilder();
        if (reviewForCar != null) {
            stringBuilder.append("The review for the ").append(review.getCar().getModel()).append(" stored successfully");
        }
        else {
            stringBuilder.append("The review for the ").append(review.getCar().getModel()).append(" didn't stored successfully");
        }
        return stringBuilder.toString();
    }

    @Override
    public User requestReviewUser(User user, String licence, String date, String username) {
        Optional<Car> car = carServices.findById(licence);
        if (car.isPresent()) {
            RequestCar requestCar = requestCarServices.findByString(car.get().getOwner_username(), car.get().getLicence(), LocalDate.parse(date), username);
            requestCar.setCanReviewUser(1);
            requestCarServices.saveRequest(requestCar);
            Optional<User> renter = userRepository.findById(username);
            /*if (renter.isPresent()) {
                return renter.get();
            }
            else return null;*/
            return renter.orElse(null);
        }
        else return null;
    }

    @Override
    public String reviewUser(ReviewForUser review) {
        ReviewForUser review1 = reviewUserServices.save(review);
        StringBuilder stringBuilder = new StringBuilder();
        if (review1 != null){
            stringBuilder.append("The review for the ").append(review.getRenter().getFirstName()).append(" ").append(review.getRenter().getLastName()).append(" stored successfully");
        }
        else {
            stringBuilder.append("The review for the ").append(review.getRenter().getFirstName()).append(" ").append(review.getRenter().getLastName()).append(" didn't stored successfully");
        }
        return stringBuilder.toString();
    }
}
