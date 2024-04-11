package com.john.graduate_project.model;


import com.john.graduate_project.model.ClassIDs.ReviewForUserID;
import jakarta.persistence.*;




@Entity
@Table(name = "reviewforuser")
public class ReviewForUser {

    @EmbeddedId
    private ReviewForUserID reviewForUserID;

    @OneToOne
    @JoinColumn(insertable = false, updatable = false, name = "owner_username")
    private User owner;

    @OneToOne
    @JoinColumn(insertable = false, updatable = false, name = "renter_username")
    private User renter;

    @Column(length = 500, name = "review")
    private String review;

    @Column(name = "stars")
    private int stars;

    public ReviewForUser(ReviewForUserID reviewForUserID, User owner, User renter) {
        this.reviewForUserID = reviewForUserID;
        this.owner = owner;
        this.renter = renter;
    }

    public ReviewForUser(ReviewForUserID reviewForUserID, User owner, User renter, String review, int stars) {
        this.reviewForUserID = reviewForUserID;
        this.owner = owner;
        this.renter = renter;
        this.review = review;
        this.stars = stars;
    }

    public ReviewForUser(ReviewForUserID reviewForUserID, User owner, User renter, String review) {
        this.reviewForUserID = reviewForUserID;
        this.owner = owner;
        this.renter = renter;
        this.review = review;
    }

    public ReviewForUser() {
    }

    public ReviewForUserID getReviewForUserID() {
        return reviewForUserID;
    }

    public void setReviewForUserID(ReviewForUserID reviewForUserID) {
        this.reviewForUserID = reviewForUserID;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getRenter() {
        return renter;
    }

    public void setRenter(User renter) {
        this.renter = renter;
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
}
