package com.john.graduate_project.repository.service.impl;

import com.john.graduate_project.model.ReviewForUser;
import com.john.graduate_project.repository.ReviewUserRepository;
import com.john.graduate_project.repository.service.ReviewUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewUserServicesImpl implements ReviewUserServices {
    private ReviewUserRepository reviewUserRepository;

    @Autowired
    public ReviewUserServicesImpl(ReviewUserRepository reviewUserRepository) {
        this.reviewUserRepository = reviewUserRepository;
    }

    @Override
    public ReviewForUser findReview(String owner_username, String renter_username) {
        Iterable<ReviewForUser> review = reviewUserRepository.findAll();
        for (ReviewForUser r :review){
            if (r.getReviewForUserID().getOwner_username().equals(owner_username)
                    && r.getReviewForUserID().getRenter_username().equals(renter_username))
                return r;
        }
        return null;
    }

    @Override
    public List<ReviewForUser> findReviewsByUsername(String username) {
        Iterable<ReviewForUser> review = reviewUserRepository.findAll();
        List<ReviewForUser> reviews = new ArrayList<>();
        for (ReviewForUser r : review) {
            if (r.getReviewForUserID().getRenter_username().equals(username)) reviews.add(r);
        }
        return reviews;
    }

    @Override
    public ReviewForUser save(ReviewForUser review) {
        return reviewUserRepository.save(review);
    }
}
