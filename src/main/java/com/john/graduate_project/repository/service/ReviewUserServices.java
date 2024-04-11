package com.john.graduate_project.repository.service;

import com.john.graduate_project.model.ReviewForUser;

import java.util.List;

public interface ReviewUserServices {
    ReviewForUser findReview(String owner_username, String renter_username);
    List<ReviewForUser> findReviewsByUsername(String username);
    ReviewForUser save(ReviewForUser review);
}
