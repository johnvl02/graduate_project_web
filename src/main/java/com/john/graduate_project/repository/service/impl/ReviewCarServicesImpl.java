package com.john.graduate_project.repository.service.impl;

import com.john.graduate_project.model.ReviewForCar;
import com.john.graduate_project.repository.ReviewCarRepository;
import com.john.graduate_project.repository.service.ReviewCarServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewCarServicesImpl implements ReviewCarServices {
    private ReviewCarRepository reviewCarRepository;

    @Autowired
    public ReviewCarServicesImpl(ReviewCarRepository reviewCarRepository) {
        this.reviewCarRepository = reviewCarRepository;
    }

    @Override
    public List<ReviewForCar> findByLicence(String licence) {
        Iterable<ReviewForCar> cars = reviewCarRepository.findAll();
        List<ReviewForCar> reviews = new ArrayList<>();
        cars.forEach(r ->{
            if (r.getReviewForCarID().getCar_licence().equals(licence)) reviews.add(r);
        } );
        return reviews;
    }

    @Override
    public ReviewForCar findReview(String username, String licence) {
        Iterable<ReviewForCar> cars = reviewCarRepository.findAll();
        for (ReviewForCar r :cars){
            if (r.getReviewForCarID().getRenter_username().equals(username)
                    && r.getReviewForCarID().getCar_licence().equals(licence))
                return r;
        }
        return null;
    }

    @Override
    public ReviewForCar save(ReviewForCar review) {
        return reviewCarRepository.save(review);
    }
}
