package com.john.graduate_project.repository.service.impl;

import com.john.graduate_project.model.Car;
import com.john.graduate_project.model.RequestCar;
import com.john.graduate_project.repository.CarRepository;
import com.john.graduate_project.repository.service.CarServices;
import com.john.graduate_project.repository.service.RequestCarServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class CarServicesImpl implements CarServices {

    private CarRepository carRepository;

    @Autowired
    public CarServicesImpl(CarRepository carRepository, RequestCarServices requestCarServices) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> findByNama(String name) {
        List<Car> cars = new ArrayList<>();
        carRepository.findAll().forEach(car -> {
            if (car.getOwner_username().equals(name) && car.isAvailable())cars.add(car);
        });
        return cars;
    }

    @Override
    public List<Car> findAvailableCar(int page, int size , String sort) {
        List<Car> cars = new ArrayList<>();
        carRepository.findAll().forEach(car -> {
            if (car.isAvailable())
             cars.add(car);
        });
        List<Car> result;
        switch (sort){
            case "descending":
               result = sortCarsD(cars);
                break;
            case "ascending":
                result = sortCarsA(cars);
                break;
            default:
                result = cars;
                break;
        }
        if ((page+1)*size<result.size()){
            return result.subList(page*size,(page + 1)*size);
        }
        else
            return result.subList(page*size,result.size());
    }

    public List<RequestCar> sortRequest(List<RequestCar> carList){
        RequestCar[] requestArray = carList.toArray(new RequestCar[0]);
        for (int j=1; j < requestArray.length; j++){
            RequestCar key = requestArray[j];
            int i = j-1;
            while (i>=0 && compareDate(key.getUpdateDate(), requestArray[i].getUpdateDate()) >0){
                requestArray[i+1]=requestArray[i];
                i-=1;
            }
            requestArray[i+1] = key;
        }
        return new ArrayList<>(Arrays.asList(requestArray));
    }

    private List<Car> sortCarsA(List<Car> carList){
        Car[] carArray = carList.toArray(new Car[0]);
        for (int j=1; j < carArray.length; j++){
            Car key = carArray[j];
            int i = j-1;
            while (i>=0 && key.getRating() < carArray[i].getRating()){
                carArray[i+1]=carArray[i];
                i-=1;
            }
            carArray[i+1] = key;
        }
        return new ArrayList<>(Arrays.asList(carArray));
    }
    private List<Car> sortCarsD(List<Car> carList){
        Car[] carArray = carList.toArray(new Car[0]);
        for (int j=1; j < carArray.length; j++){
            Car key = carArray[j];
            int i = j-1;
            while (i>=0 && key.getRating() > carArray[i].getRating()){
                carArray[i+1]=carArray[i];
                i-=1;
            }
            carArray[i+1] = key;
        }
        return new ArrayList<>(Arrays.asList(carArray));
    }

    @Override
    public List<Car> findAllCars(){
        List<Car> cars = new ArrayList<>();
        carRepository.findAll().forEach(car -> cars.add(car));
        return cars ;
    }

    private int compareDate(LocalDate d1, LocalDate d2){
        return d1.compareTo(d2);
    }

    @Override
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Optional<Car> findById(String license) {
        return carRepository.findById(license);
    }


    public Page<Car> findCarPage(Pageable pageable){
        return carRepository.findAll(pageable);
    }
}
