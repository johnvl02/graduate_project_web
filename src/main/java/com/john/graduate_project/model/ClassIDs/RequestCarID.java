package com.john.graduate_project.model.ClassIDs;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;
@Embeddable
public class RequestCarID implements Serializable {

    private String owner_username;
    private String renter_username;
    private String car_licence;
    private LocalDate dateTime;


    public RequestCarID(String owner_username, String renter_username, String car_licence, LocalDate dateTime) {
        this.owner_username = owner_username;
        this.renter_username = renter_username;
        this.car_licence = car_licence;
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

    public String getCar_licence() {
        return car_licence;
    }

    public void setCar_licence(String car_license) {
        this.car_licence = car_license;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

}
