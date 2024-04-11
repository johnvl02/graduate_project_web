package com.john.graduate_project.model.ClassIDs;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ReviewForCarID implements Serializable {

    private String renter_username;
    private String car_licence;

    public ReviewForCarID(String renter_username, String car_licence) {
        this.renter_username = renter_username;
        this.car_licence = car_licence;
    }

    public ReviewForCarID() {
    }

    public String getRenter_username() {
        return renter_username;
    }

    public void setRenter_username(String renter_username) {
        this.renter_username = renter_username;
    }

    public String getCar_licence() {
        return car_licence;
    }

    public void setCar_licence(String car_licence) {
        this.car_licence = car_licence;
    }
}
