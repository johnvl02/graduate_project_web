package com.john.graduate_project.repository.service.impl;

import com.john.graduate_project.model.RequestCar;
import com.john.graduate_project.model.ReviewForCar;
import com.john.graduate_project.model.ReviewForUser;
import com.john.graduate_project.repository.RequestCarRepository;
import com.john.graduate_project.repository.service.ReviewCarServices;
import com.john.graduate_project.repository.service.RequestCarServices;
import com.john.graduate_project.repository.service.ReviewUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestCarServicesImpl implements RequestCarServices {
    private RequestCarRepository requestCarRepository;
    private ReviewUserServices reviewUserServices;
    private ReviewCarServices reviewCarServices;

    @Autowired
    public RequestCarServicesImpl(RequestCarRepository requestCarRepository, ReviewUserServices reviewUserServices, ReviewCarServices reviewCarServices) {
        this.requestCarRepository = requestCarRepository;
        this.reviewUserServices = reviewUserServices;
        this.reviewCarServices = reviewCarServices;
    }

    @Override
    public RequestCar findByString(String owner, String license, LocalDate date, String renter) {
        Iterable<RequestCar> rc =  requestCarRepository.findAll();
        for (RequestCar requestCar :rc) {
            if (requestCar.getRequestCarID().getOwner_username().equals(owner) &&
                    requestCar.getRequestCarID().getRenter_username().equals(renter) &&
                    requestCar.getRequestCarID().getCar_license().equals(license) &&
                    requestCar.getRequestCarID().getDateTime().equals(date)) {
                return requestCar;
            }
        }
        return null;
    }

    @Override
    public List<RequestCar> findByLicense(String license) {
        List<RequestCar> requestCar1 = new ArrayList<>();
        requestCarRepository.findAll().forEach(requestCar -> { if (requestCar.getRequestCarID().getCar_license().equals(license))requestCar1.add(requestCar);});
        return requestCar1;
    }

    @Override
    public RequestCar saveRequest(RequestCar requestCar) {
        return requestCarRepository.save(requestCar);
    }

    @Override
    public List<RequestCar> findAll() {
        List<RequestCar> requestCars = new ArrayList<>();
        requestCarRepository.findAll().forEach(requestCar -> requestCars.add(setCanReviewCar(setCanReviewUser(requestCar))));
        return requestCars;
    }

    //0=>you can't review yet
    //1=>you can review now
    //2=>you already review
    //3=>you can't review
    //4=>you must wait
    private RequestCar setCanReviewCar(RequestCar requestCar) {
        ReviewForCar review = reviewCarServices.findReview(requestCar.getRequestCarID().getRenter_username(),requestCar.getRequestCarID().getCar_license());
        if (review != null){
            if (requestCar.getCanReviewCar() !=2) {
                requestCar.setCanReviewCar(2);
                requestCarRepository.save(requestCar);
            }
        }
        else {
            switch (requestCar.getStatus()) {
                case "accept" -> {
                    String[] s = requestCar.getDates().split(",");

                    LocalDate lasteday = LocalDate.parse(s[s.length-1]);
                    if (LocalDate.now().isAfter(lasteday)) {
                        if (requestCar.getCanReviewCar() != 1) {
                            requestCar.setCanReviewCar(1);
                            requestCarRepository.save(requestCar);
                        }
                    } else {
                        if (requestCar.getCanReviewCar() != 0) {
                            requestCar.setCanReviewCar(0);
                            requestCarRepository.save(requestCar);
                        }
                    }
                }
                case "decline" -> {
                    if (requestCar.getCanReviewCar() !=3) {
                        requestCar.setCanReviewCar(3);
                        requestCarRepository.save(requestCar);
                    }
                }
            }
        }
        return requestCar;
    }

    private RequestCar setCanReviewUser(RequestCar requestCar) {
        ReviewForUser review = reviewUserServices.findReview(requestCar.getRequestCarID().getOwner_username(),requestCar.getRequestCarID().getRenter_username());
        if (review !=null){
            if (requestCar.getCanReviewUser() !=2) {
                requestCar.setCanReviewUser(2);
                requestCarRepository.save(requestCar);
            }
        }
        else {
            switch (requestCar.getStatus()) {
                case "accept" -> {
                    String[] s = requestCar.getDates().split(",");

                    LocalDate lasteday = LocalDate.parse(s[s.length-1]);
                    if (LocalDate.now().isAfter(lasteday)) {
                        if (requestCar.getCanReviewUser() != 1) {
                            requestCar.setCanReviewUser(1);
                            requestCarRepository.save(requestCar);
                        }
                    } else {
                        if (requestCar.getCanReviewUser() != 0) {
                            requestCar.setCanReviewUser(0);
                            requestCarRepository.save(requestCar);
                        }
                    }
                }
                case "decline" -> {
                    if (requestCar.getCanReviewUser() != 3) {
                        requestCar.setCanReviewUser(3);
                        requestCarRepository.save(requestCar);
                    }
                }
            }
        }
        return requestCar;
    }

}
