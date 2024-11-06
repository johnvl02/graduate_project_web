package com.john.graduate_project.model.ClassIDs;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;
@Embeddable
public class RequestCarID implements Serializable {

    private String owner_username;
    private String renter_username;
    private String car_license;
    private LocalDate dateTime;


    public RequestCarID(String owner_username, String renter_username, String car_license, LocalDate dateTime) {
        this.owner_username = owner_username;
        this.renter_username = renter_username;
        this.car_license = car_license;
        this.dateTime = dateTime;
    }

    public RequestCarID() {
    }

    public String getOwner_username() {
        return owner_username;
    }

    public void setOwner_username(String owner_username) {
        this.owner_username = owner_username;
    }

    public String getRenter_username() {
        return renter_username;
    }

    public void setRenter_username(String renter_username) {
        this.renter_username = renter_username;
    }

    public String getCar_license() {
        return car_license;
    }

    public void setCar_license(String car_license) {
        this.car_license = car_license;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

}
