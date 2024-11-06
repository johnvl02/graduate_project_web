package com.john.graduate_project.model;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashMap;

@Entity
@Table(name = "renter_info")
public class RenterInfo {

    @Id
    @Column(name = "username")
    private String username;
    @OneToOne
    @MapsId
    @JoinColumn(insertable = false, updatable = false, name = "username")
    private User renter;

    @Column(name = "driving_license_date")
    private LocalDate dateOfDL;//driving license

    @Column(name = "info", length = 500)
    private String info;

    public RenterInfo(String username, User renter, LocalDate dateOfDL, String info) {
        this.username = username;
        this.dateOfDL = dateOfDL;
        this.info = info;
    }

    public RenterInfo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String renter_username) {
        this.username = renter_username;
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

    public HashMap<String, String> RenterToHashMap(){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("username",getUsername());
        hashMap.put("date", String.valueOf(getDateOfDL()));
        hashMap.put("info", getInfo());
        return hashMap;
    }
}