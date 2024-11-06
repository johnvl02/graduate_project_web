package com.john.graduate_project.model;


import com.john.graduate_project.model.ClassIDs.ReviewForCarID;
import jakarta.persistence.*;

import java.util.HashMap;

@Entity
@Table(name = "reviewforcar")
public class ReviewForCar {

    @EmbeddedId
    private ReviewForCarID reviewForCarID;

    @OneToOne
    @JoinColumn(insertable = false, updatable = false, name = "renter_username")
    private User renter;

    @OneToOne
    @JoinColumn(insertable = false, updatable = false, name = "car_license")
    private Car car;

    @Column(length = 500, name = "review")
    private String review;

    @Column(name = "stars")
    private int stars;

    public ReviewForCar(ReviewForCarID reviewForCarID, User renter, Car car, String review, int stars) {
        this.reviewForCarID = reviewForCarID;
        this.renter = renter;
        this.car = car;
        this.review = review;
        this.stars = stars;
    }

    public ReviewForCar() {
    }

    public ReviewForCar(ReviewForCarID reviewForCarID, User renter, Car car) {
        this.reviewForCarID = reviewForCarID;
        this.renter = renter;
        this.car = car;
    }

    public ReviewForCarID getReviewForCarID() {
        return reviewForCarID;
    }

    public void setReviewForCarID(ReviewForCarID reviewForCarID) {
        this.reviewForCarID = reviewForCarID;
    }

    public User getRenter() {
        return renter;
    }

    public void setRenter(User renter) {
        this.renter = renter;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
    public HashMap<String,Object> reviewToHashMap(){
        return new HashMap<>(){{
            put("username",getReviewForCarID().getRenter_username());
            put("review", getReview());
            put("stars",getStars());
        }};
    }
}
