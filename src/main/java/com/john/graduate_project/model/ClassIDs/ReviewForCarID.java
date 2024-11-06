package com.john.graduate_project.model.ClassIDs;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ReviewForCarID implements Serializable {

    private String renter_username;
    private String car_license;

    public ReviewForCarID(String renter_username, String car_license) {
        this.renter_username = renter_username;
        this.car_license = car_license;
    }

    public ReviewForCarID() {
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
}
