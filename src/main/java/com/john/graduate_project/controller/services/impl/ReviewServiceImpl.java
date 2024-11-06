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
import java.util.List;
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
    public Car requestReviewCar(String license, String date, String username) {
        Optional<Car> car = carServices.findById(license);
        if (car.isPresent()){
            RequestCar requestCar = requestCarServices.findByString(car.get().getOwner_username(), car.get().getLicense(), LocalDate.parse(date), username);
            requestCar.setCanReviewCar(1);
            requestCarServices.saveRequest(requestCar);
            return car.get();
        }
        else return null;
    }

    @Override
    public String reviewCar(ReviewForCar review) {
        ReviewForCar reviewForCar = reviewCarServices.save(review);
        StringBuilder stringBuilder = new StringBuilder();
        if (reviewForCar != null) {
            stringBuilder.append("The review for the ").append(review.getCar().getModel()).append(" stored successfully");

            List<ReviewForCar> list = reviewCarServices.findByLicense(review.getReviewForCarID().getCar_license());
            double aver=0;
            double sum=0;
            for (ReviewForCar review1 :list){
                sum+=review1.getStars();
            }
            aver = sum/list.size();
            Optional<Car> car = carServices.findById(review.getReviewForCarID().getCar_license());
            car.get().setRating(aver);
            carServices.saveCar(car.get());
        }
        else {
            stringBuilder.append("The review for the ").append(review.getCar().getModel()).append(" didn't stored successfully");
        }
        return stringBuilder.toString();
    }

    @Override
    public User requestReviewUser(String license, String date, String username) {
        Optional<Car> car = carServices.findById(license);
        if (car.isPresent()) {
            RequestCar requestCar = requestCarServices.findByString(car.get().getOwner_username(), car.get().getLicense(), LocalDate.parse(date), username);
            requestCar.setCanReviewUser(1);
            requestCarServices.saveRequest(requestCar);
            Optional<User> renter = userRepository.findById(username);
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
