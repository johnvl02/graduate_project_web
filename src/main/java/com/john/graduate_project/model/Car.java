package com.john.graduate_project.model;


import com.john.graduate_project.model.types.FuelType;
import com.john.graduate_project.model.types.TransmissionType;
import jakarta.persistence.*;

import java.util.HashMap;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @Column(name = "license", length = 10)
    private String license;
    @Column(name = "model")
    private String model;
    @Column(name = "fuel")
    private FuelType fuel;
    @Column(name = "transmission")
    private TransmissionType transmission;
    @Column(name = "available_days")
    private String availableDays;
    @Column(name = "available")
    private boolean available;
    @Column(name = "num_seats")
    private int numSeats;
    @Column(name = "value")
    private long value;
    @Column(name = "photo")
    private String photo;
    @Column(name = "address")
    private String address;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "rating")
    private double rating = 0;
    @Column(insertable = false, updatable = false, name = "owner_username")
    private String owner_username;
    @OneToOne
    @JoinColumn(name = "owner_username")
    private User owner;

    public Car(String license, String model, FuelType fuel, TransmissionType transmission, String availableDays, boolean available, int numSeats, long value, String photo, String address, String description, String owner_username, User owner) {
        this.license = license;
        this.model = model;
        this.fuel = fuel;
        this.transmission = transmission;
        this.availableDays = availableDays;
        this.available = available;
        this.numSeats = numSeats;
        this.value = value;
        this.photo = photo;
        this.address = address;
        this.description = description;
        this.owner_username = owner_username;
        this.owner = owner;
    }

    public Car(String license, String model, FuelType fuel, TransmissionType transmission, String availableDays, boolean available, int numSeats, long value, String photo, String address, String description, String owner_username) {
        this.license = license;
        this.model = model;
        this.fuel = fuel;
        this.transmission = transmission;
        this.availableDays = availableDays;
        this.available = available;
        this.numSeats = numSeats;
        this.value = value;
        this.photo = photo;
        this.address = address;
        this.description = description;
        this.owner_username = owner_username;
    }

    public Car(String license, String model, FuelType fuel, TransmissionType transmission, String availableDays, boolean available, int numSeats, long value, String photo, String address, String description, double rating, String owner_username) {
        this.license = license;
        this.model = model;
        this.fuel = fuel;
        this.transmission = transmission;
        this.availableDays = availableDays;
        this.available = available;
        this.numSeats = numSeats;
        this.value = value;
        this.photo = photo;
        this.address = address;
        this.description = description;
        this.rating = rating;
        this.owner_username = owner_username;
    }

    public Car() {
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public FuelType getFuel() {
        return fuel;
    }

    public void setFuel(FuelType fuel) {
        this.fuel = fuel;
    }

    public TransmissionType getTransmission() {
        return transmission;
    }

    public void setTransmission(TransmissionType transmission) {
        this.transmission = transmission;
    }

    public String getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(String availiableDates) {
        this.availableDays = availiableDates;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean availiable) {
        this.available = availiable;
    }

    public int getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoPath(){
        if (photo == null || owner_username == null) return null;
        return "/src/main/resources/static/cars-photo/"+ owner_username + "/"+photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner_username() {
        return owner_username;
    }

    public void setOwner_username(String owner_username) {
        this.owner_username = owner_username;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public HashMap<String,Object> carToHashMap(){
        return new HashMap<>(){{
            put("license", getLicense());
            put("model", getModel());
            put("fuel", getFuel());
            put("transmission", getTransmission());
            put("availableDays", getAvailableDays());
            put("seats", getNumSeats());
            put("value", getValue());
            put("address", getAddress());
            put("description", getDescription());
            put("rating", getRating());
        }};
    }

}
