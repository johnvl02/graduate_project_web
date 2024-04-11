package com.john.graduate_project.model.ClassIDs;


import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ReviewForUserID implements Serializable {
    private String renter_username;
    private String owner_username;

    public ReviewForUserID(String renter_username, String owner_username) {
        this.renter_username = renter_username;
        this.owner_username = owner_username;
    }

    public ReviewForUserID() {
    }

    public String getRenter_username() {
        return renter_username;
    }

    public void setRenter_username(String renter_username) {
        this.renter_username = renter_username;
    }

    public String getOwner_username() {
        return owner_username;
    }

    public void setOwner_username(String owner_username) {
        this.owner_username = owner_username;
    }
}
