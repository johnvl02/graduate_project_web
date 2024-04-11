package com.john.graduate_project.controller.services.impl;

import com.john.graduate_project.controller.services.MainService;
import com.john.graduate_project.model.*;
import com.john.graduate_project.repository.RenterInfoIRepository;
import com.john.graduate_project.repository.UserRepository;
import com.john.graduate_project.repository.service.CarServices;
import com.john.graduate_project.repository.service.RequestCarServices;
import com.john.graduate_project.repository.service.ReviewUserServices;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MainServiceImpl implements MainService {

    private final CarServices carServices;
    private final UserRepository userRepository;
    private final ReviewUserServices reviewUserServices;
    private final RenterInfoIRepository renterInfoIRepository;
    private final RequestCarServices requestCarServices;

    public MainServiceImpl(CarServices carServices, UserRepository userRepository, ReviewUserServices reviewUserServices, RenterInfoIRepository renterInfoIRepository,
                           RequestCarServices requestCarServices) {
        this.carServices = carServices;
        this.userRepository = userRepository;
        this.reviewUserServices = reviewUserServices;
        this.renterInfoIRepository = renterInfoIRepository;
        this.requestCarServices = requestCarServices;
    }

    @Override
    public List<Car> homePage() {
        carServices.findAvailableCar();//If a car was unavailable the value will update with this else the value will not be updated
        return carServices.findAllCars();
    }

    @Override
    public List<Object> profile(User user2, String username) {
        List<Object> result = new ArrayList<>();
        Optional<User> optionalUser = userRepository.findById(username);
        optionalUser.ifPresent(user1 -> {
            List<ReviewForUser> reviews = reviewUserServices.findReviewsByUsername(username);
            int count=0;
            double aver=0;
            double sum=0;
            for (ReviewForUser r : reviews) {
                count++;
                sum += r.getStars();
            }
            if (count!=0){
                aver = sum / count;
            }
            result.add(aver);
            result.add(count);
            result.add(reviews);
            result.add(optionalUser.get().userToUserDto());
            Optional<RenterInfo> info = renterInfoIRepository.findById(username);
            info.ifPresent(result::add);
        });
        return result;
    }

    @Override
    public String updateStatus(String id, String value, User user) {
        String[] str = id.split(" ");// render,licence,date
        RequestCar rc = requestCarServices.findByString(user.getUsername(),str[1], LocalDate.parse(str[2]), str[0]);
        rc.setStatus(value);
        rc.setUpdateDate(LocalDate.now());
        RequestCar requestCar = requestCarServices.saveRequest(rc);
        if (requestCar != null) {
            if (value.equals("accept")) {
                Optional<Car> car = carServices.findById(str[1]);
                car.ifPresent(car1 -> {
                    car1.setAvailable(false);
                    carServices.saveCar(car1);
                });

            }
           return "The request updated successfully";
        }
        else {
            return  "Something went wrong and the request don't updated successfully";
        }
    }

    @Override
    public List<Car> maps() {
        return carServices.findAvailableCar();
    }
}
