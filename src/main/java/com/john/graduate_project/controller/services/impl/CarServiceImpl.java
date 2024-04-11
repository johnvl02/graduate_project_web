package com.john.graduate_project.controller.services.impl;

import com.john.graduate_project.controller.services.CarService;
import com.john.graduate_project.model.*;
import com.john.graduate_project.model.ClassIDs.RequestCarID;
import com.john.graduate_project.repository.RenterInfoIRepository;
import com.john.graduate_project.repository.UserRepository;
import com.john.graduate_project.repository.service.CarServices;
import com.john.graduate_project.repository.service.RequestCarServices;
import com.john.graduate_project.repository.service.ReviewCarServices;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {
    private final CarServices carServices;
    private final RenterInfoIRepository infoRepository;
    private final ReviewCarServices reviewCarServices;
    private final UserRepository userRepository;
    private final RequestCarServices requestCarServices;

    public CarServiceImpl(CarServices carServices, RenterInfoIRepository infoRepository, ReviewCarServices reviewCarServices,
                          UserRepository userRepository, RequestCarServices requestCarServices) {
        this.carServices = carServices;
        this.infoRepository = infoRepository;
        this.reviewCarServices = reviewCarServices;
        this.userRepository = userRepository;
        this.requestCarServices = requestCarServices;
    }

    @Override
    public List<Car> availableCar() {
        return carServices.findAvailableCar();
    }

    @Override
    public String addCar(User user, Car car, MultipartFile multipartFile) {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        car.setPhoto(fileName);
        car.setOwner_username(user.getUsername());
        car.setOwner(user);
        if (car.getLicence() != null && !car.getLicence().equals("")){
            Car c = carServices.saveCar(car);
            String uploadDir = "./src/main/resources/static/cars-photo/"+c.getOwner_username();
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)){
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                InputStream inputStream = multipartFile.getInputStream();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                return "The car added successfully";
            } catch (IOException e) {
                return  "The car didn't added !!!";
                //throw new RuntimeException(e);
            }
        }
        else return "Please give valid information about car";
    }

    @Override
    public Car newCar() {
        return new Car();
    }

    @Override
    public List<Object> showCar(String license, User user) {
        List<Object> result = new ArrayList<>();
        Optional<RenterInfo> info = infoRepository.findById(user.getUsername());
        if (info.isEmpty()){
            result.add(new  RenterInfo());
            return result;
        }
        int count=0;
        double aver=0;
        double sum=0;
        Optional<Car> carOptional = carServices.findById(license);
        if (carOptional.isPresent()){
            result.add(carOptional.get());// first argument
            List<ReviewForCar> review = reviewCarServices.findByLicence(carOptional.get().getLicence());
            if (review.isEmpty()){
                result.add(null);
                result.add(0);
                result.add(0);
            }
            else {
                result.add(review);
                for (ReviewForCar r : review) {
                    count++;
                    sum += r.getStars();
                }
                aver = sum / count;
                result.add(aver);
                result.add(count);
            }
        }
        return  result;
    }

    @Override
    public String rentCar(int days, String license, User user) {
        Optional<Car> car = carServices.findById(license);
        if (car.isPresent()) {
            Optional<User> owner = userRepository.findById(car.get().getOwner_username());
            if (owner.isPresent()) {
                RequestCarID r = new RequestCarID(owner.get().getUsername(), user.getUsername(), car.get().getLicence(), LocalDate.now());
                RequestCar rc = new RequestCar(r, owner.get(), user, car.get(), days,"pending",4,4);
                RequestCar car1 = requestCarServices.saveRequest(rc);
                if (car1 != null){
                    return "Your request sent to the owner of the car successfully";
                }
                else {
                    return "Something went wrong and your request didn't sent to the owner of the car !!";
                }
            }
        }
        return "The car doesn't find";
    }

    @Override
    public List<Object> request(User user) {
        List<Object> result = new ArrayList<>();
        List<RequestCar> want = new ArrayList<>();//i want to rent a car
        List<RequestCar> have = new ArrayList<>();//have to rent my car to other
        requestCarServices.findAll().forEach(requestCar -> {
            if (requestCar.getRequestCarID().getOwner_username().equals(user.getUsername())){
                have.add(requestCar);
            }
            else if (requestCar.getRequestCarID().getRenter_username().equals(user.getUsername())){
                want.add(requestCar);
            }
        });
        result.add(want);
        result.add(have);
        return result;
    }
}
