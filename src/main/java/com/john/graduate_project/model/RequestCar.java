package com.john.graduate_project.model;


import com.john.graduate_project.model.ClassIDs.RequestCarID;
import com.john.graduate_project.repository.service.ReviewCarServices;
import com.john.graduate_project.repository.service.ReviewUserServices;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashMap;


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
    @JoinColumn(insertable=false, updatable=false, name = "car_license")
    private Car car;

    @Column(name = "dates")
    private String dates;

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

    public RequestCar(RequestCarID requestCarID, User owner, User renter, Car car, String dates, String status, int canReviewCar, int canReviewUser) {
        this.requestCarID = requestCarID;
        this.owner = owner;
        this.renter = renter;
        this.car = car;
        this.dates = dates;
        this.status = status;
        this.canReviewCar = canReviewCar;
        this.canReviewUser = canReviewUser;
        this.updateDate =requestCarID.getDateTime();
    }

    public RequestCar(RequestCarID requestCarID, LocalDate updateDate, User owner, User renter, Car car, String dates, String status, int canReviewCar, int canReviewUser) {
        this.requestCarID = requestCarID;
        this.updateDate = updateDate;
        this.owner = owner;
        this.renter = renter;
        this.car = car;
        this.dates = dates;
        this.status = status;
        this.canReviewCar = canReviewCar;
        this.canReviewUser = canReviewUser;
    }

    public RequestCar(RequestCarID requestCarID, LocalDate updateDate, User owner, User renter, Car car, String dates, String status) {
        this.requestCarID = requestCarID;
        this.updateDate = updateDate;
        this.owner = owner;
        this.renter = renter;
        this.car = car;
        this.dates = dates;
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

    public String getDates() {
        return dates;
    }

    public void setDates(String numDates) {
        this.dates = numDates;
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

    public HashMap<String,String> requestToHashmap(){
        HashMap<String, String> temp = new HashMap<>();
        temp.put("license", this.getCar().getLicense());
        temp.put("owner", this.requestCarID.getOwner_username());
        temp.put("renter", this.requestCarID.getRenter_username());
        temp.put("request_date", this.requestCarID.getDateTime().toString());
        temp.put("dates",this.getDates());
        temp.put("status",this.getStatus());
        temp.put("update_date", this.getUpdateDate().toString());
        temp.put("reviewCar",String.valueOf(this.getCanReviewCar()));
        temp.put("reviewUser",String.valueOf(this.getCanReviewUser()));
        return temp;
    }


}
