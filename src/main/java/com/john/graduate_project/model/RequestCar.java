package com.john.graduate_project.model;


import com.john.graduate_project.model.ClassIDs.RequestCarID;
import com.john.graduate_project.repository.service.ReviewCarServices;
import com.john.graduate_project.repository.service.ReviewUserServices;
import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "requests")
public class RequestCar {


    @EmbeddedId
    private RequestCarID requestCarID;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @OneToOne
    @JoinColumn(insertable=false, updatable=false, name = "owner_username")
    private User owner;

    @OneToOne
    @JoinColumn(insertable=false, updatable=false, name = "renter_username")
    private User renter;

    @OneToOne
    @JoinColumn(insertable=false, updatable=false, name = "car_licence")
    private Car car;

    @Column(name = "num_dates")
    private int numDates;

    @Column(name = "status")
    private String status;

    //@Transient
    @Column(name = "can_review_car")
    private int canReviewCar;

    @Column(name = "can_review_user")
    private int canReviewUser;

    @Transient
    private ReviewCarServices reviewCarServices;

    @Transient
    private ReviewUserServices reviewUserServices;

    public RequestCar() {
    }

    public RequestCar(RequestCarID requestCarID, User owner, User renter, Car car, int numDates, String status, int canReviewCar, int canReviewUser) {
        this.requestCarID = requestCarID;
        this.owner = owner;
        this.renter = renter;
        this.car = car;
        this.numDates = numDates;
        this.status = status;
        this.canReviewCar = canReviewCar;
        this.canReviewUser = canReviewUser;
        this.updateDate =requestCarID.getDateTime();
    }

    public RequestCar(RequestCarID requestCarID, LocalDate updateDate, User owner, User renter, Car car, int numDates, String status, int canReviewCar, int canReviewUser) {
        this.requestCarID = requestCarID;
        this.updateDate = updateDate;
        this.owner = owner;
        this.renter = renter;
        this.car = car;
        this.numDates = numDates;
        this.status = status;
        this.canReviewCar = canReviewCar;
        this.canReviewUser = canReviewUser;
    }

    public RequestCar(RequestCarID requestCarID,LocalDate updateDate, User owner, User renter, Car car, int numDates, String status) {
        this.requestCarID = requestCarID;
        this.updateDate = updateDate;
        this.owner = owner;
        this.renter = renter;
        this.car = car;
        this.numDates = numDates;
        this.status = status;
    }

    public RequestCarID getRequestCarID() {
        return requestCarID;
    }

    public void setRequestCarID(RequestCarID requestCarID) {
        this.requestCarID = requestCarID;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
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

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public int getNumDates() {
        return numDates;
    }

    public void setNumDates(int numDates) {
        this.numDates = numDates;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCanReviewCar() {
        return canReviewCar;
    }

    public void setCanReviewCar(int canReviewCar) {
        this.canReviewCar = canReviewCar;
    }


    public int getCanReviewUser() {
        return canReviewUser;
    }

    public void setCanReviewUser(int canReviewUser) {
        this.canReviewUser = canReviewUser;
    }


}
