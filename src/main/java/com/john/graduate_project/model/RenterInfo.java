package com.john.graduate_project.model;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "renter_info")
public class RenterInfo {

    @Id
    @Column(name = "renter_username")
    private String renter_username;
    @OneToOne
    @MapsId
    @JoinColumn(name = "renter_username")
    private User renter;

    @Column(name = "driving_license_date")
    private LocalDate dateOfDL;//driving license

    @Column(name = "info", length = 500)
    private String info;

    public RenterInfo(String renter_username, User renter, LocalDate dateOfDL, String info) {
        this.renter_username = renter_username;
        this.dateOfDL = dateOfDL;
        this.info = info;
    }

    public RenterInfo() {
    }

    public String getRenter_username() {
        return renter_username;
    }

    public void setRenter_username(String renter_username) {
        this.renter_username = renter_username;
    }

    public User getRenter() {
        return renter;
    }

    public void setRenter(User renter) {
        this.renter = renter;
    }

    public LocalDate getDateOfDL() {
        return dateOfDL;
    }

    public void setDateOfDL(LocalDate dateOfDL) {
        this.dateOfDL = dateOfDL;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
