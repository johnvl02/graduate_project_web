package com.john.graduate_project.controller.services.impl;

import com.john.graduate_project.controller.services.CarService;
import com.john.graduate_project.model.*;
import com.john.graduate_project.model.ClassIDs.RequestCarID;
import com.john.graduate_project.repository.RenterInfoIRepository;
import com.john.graduate_project.repository.UserRepository;
import com.john.graduate_project.repository.service.CarServices;
import com.john.graduate_project.repository.service.RequestCarServices;
import com.john.graduate_project.repository.service.ReviewCarServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public List<Car> availableCar(int page, int size, String sort) {
        return carServices.findAvailableCar(page,size, sort);
    }

    @Override
    public int findPages() {
        return carServices.findAllCars().size();
    }

    @Override
    public String addCar(String username, Car car, MultipartFile multipartFile) {
        Optional<Car> temp = carServices.findById(car.getLicense());
        if ((temp.isPresent() && temp.get().getOwner_username().equals(username) && !temp.get().isAvailable()) || temp.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            car.setPhoto(fileName);
            Optional<User> user = userRepository.findById(username);
            if (user.isPresent()) {
                car.setOwner_username(username);
                car.setOwner(user.get());
                if (car.getLicense() != null && !car.getLicense().equals("")) {
                    Car c = carServices.saveCar(car);
                    String uploadDir = "./src/main/resources/static/cars-photo/" + c.getOwner_username();
                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
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
                        return "The car didn't added! Please try again";
                        //throw new RuntimeException(e);
                    }
                } else return "Please give valid information about car";
            } else return "The username is invalid";
        }
        else return "The car is already exist";
    }

    @Override
    public String addCar(Car car) {
        Optional<User> user = userRepository.findById(car.getOwner_username());
        if (user.isPresent()) {
            car.setOwner(user.get());
            Car c = carServices.saveCar(car);
            if (c != null) {
                return "The car added successfully";
            } else
                return "The car didn't added! Please try again";
        }
        else
            return "The username is invalid";
    }

    @Override
    public Car getCar(String license) {
        Optional<Car> car = carServices.findById(license);
        return car.orElse(null);
    }

    @Override
    public String editCar(String username, Car car, MultipartFile multipartFile) {
        Optional<Car> oldCar = carServices.findById(car.getLicense());
        oldCar.get().setAvailableDays(car.getAvailableDays());
        oldCar.get().setValue(car.getValue());
        oldCar.get().setDescription(car.getDescription());
        oldCar.get().setAddress(car.getAddress());

        if (!multipartFile.getOriginalFilename().isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            oldCar.get().setPhoto(fileName);
            String uploadDir = "./src/main/resources/static/cars-photo/"+oldCar.get().getOwner_username();
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

            } catch (IOException e) {
                return  "The car didn't added! Please try again";
            }
        }
        Car c = carServices.saveCar(oldCar.get());
        if (c !=null)
            return "The car added successfully";
        else
            return "Something went wrong please try again!!";
    }

    @Override
    public String editCar(Car car) {
        Optional<User> user = userRepository.findById(car.getOwner_username());
        if (user.isPresent()) {
            car.setOwner(user.get());
            Car c = carServices.saveCar(car);
            if (c != null) {
                return "The car edited successfully";
            } else
                return "The car didn't edited! Please try again";
        }
        else
            return "The username is invalid";
    }


    @Override
    public String deleteCar(String license) {
        Optional<Car> car = carServices.findById(license);
        if (car.isPresent()){
            car.get().setAvailable(false);
            carServices.saveCar(car.get());
            return "the car deleted successfully";
        }
        else
            return "Something went wrong (the car doesn't found) please try again!!";
    }

    @Override
    public Car newCar() {
        return new Car();
    }

    @Override
    public Car showCar(String license, String username) {
        //List<Object> result = new ArrayList<>();
        Optional<RenterInfo> info = infoRepository.findById(username);
        if (info.isEmpty()){
            return null;
        }
        Optional<Car> carOptional = carServices.findById(license);
        return carOptional.orElseGet(Car::new);
    }

    @Override
    public List<ReviewForCar> getReviews(String license) {
        List<ReviewForCar> review = reviewCarServices.findByLicense(license);
        if (review.isEmpty()){
            return null;
        }
        else {
            for (ReviewForCar r: review) {
                r.setRenter(null);
                r.setCar(null);
            }
            return review;
        }
    }

    @Override
    public String rentCar(String days, String license, String username) {
        Optional<Car> car = carServices.findById(license);
        Optional<User> user = userRepository.findById(username);
        if (car.isPresent() && user.isPresent()) {
            Optional<User> owner = userRepository.findById(car.get().getOwner_username());
            if (owner.isPresent()) {
                RequestCarID r = new RequestCarID(owner.get().getUsername(), username, car.get().getLicense(), LocalDate.now());
                RequestCar rc = new RequestCar(r, owner.get(), user.get(), car.get(), days,"pending",4,4);
                RequestCar car1 = requestCarServices.saveRequest(rc);
                if (car1 != null){
                    return "Your request sent to the owner of the car successfully";
                }
                else {
                    return "Something went wrong and your request didn't sent to the owner of the car !!";
                }
            }
        }
        return "The car doesn't find or the username is invalid";
    }

    @Override
    public List<Object> request(String username) {
        List<Object> result = new ArrayList<>();
        List<RequestCar> want = new ArrayList<>();//i want to rent a car
        List<RequestCar> have = new ArrayList<>();//have to rent my car to other
        requestCarServices.findAll().forEach(requestCar -> {
            if (requestCar.getRequestCarID().getOwner_username().equals(username)){
                have.add(requestCar);
            }
            else if (requestCar.getRequestCarID().getRenter_username().equals(username)){
                want.add(requestCar);
            }
        });
        List<RequestCar> sortedWant = carServices.sortRequest(want);
        List<RequestCar> sortedHave = carServices.sortRequest(have);
        result.add(sortedWant);
        result.add(sortedHave);
        return result;
    }

    public Page<Car> showCarPage(int page, int size , String sort){
        Pageable pageable;
        switch (sort){
            case "descending":
                pageable = PageRequest.of(page, size, Sort.by("rating").descending());
                break;
            case "ascending":
                pageable = PageRequest.of(page, size, Sort.by("rating").ascending());
                break;
            default:
                pageable = PageRequest.of(page, size, Sort.by("rating"));
                break;
        }
        PageRequest.of(5,5, Sort.Direction.ASC,Car.class.getName());
        return carServices.findCarPage(pageable);
    }

    @Override
    public List<String> reserveDates(String license) {
        List<RequestCar> requestCars = requestCarServices.findByLicense(license);
        List<String> result = new ArrayList<>();
        requestCars.forEach(requestCar -> {
            String dates = requestCar.getDates();
            String[] s = dates.split(",");
            for (String s1:s) {
                if (LocalDate.parse(s1).isAfter(LocalDate.now()) && requestCar.getStatus().equals("accept")){
                    result.add(s1);
                }
            }
        });
        return result;
    }
}
