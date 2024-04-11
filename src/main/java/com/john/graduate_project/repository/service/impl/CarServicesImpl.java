package com.john.graduate_project.repository.service.impl;

import com.john.graduate_project.model.Car;
import com.john.graduate_project.model.RequestCar;
import com.john.graduate_project.repository.CarRepository;
import com.john.graduate_project.repository.service.CarServices;
import com.john.graduate_project.repository.service.RequestCarServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class CarServicesImpl implements CarServices {

    private CarRepository carRepository;
    private RequestCarServices requestCarServices;

    @Autowired
    public CarServicesImpl(CarRepository carRepository, RequestCarServices requestCarServices) {
        this.carRepository = carRepository;
        this.requestCarServices = requestCarServices;
    }

    @Override
    public List<Car> findByNama(String name) {
        List<Car> cars = new ArrayList<>();
        carRepository.findAll().forEach(car -> {
            if (car.getOwner_username().equals(name))cars.add(car);
        });
        return cars;
    }

    @Override
    public List<Car> findAvailableCar() {
        List<Car> cars = new ArrayList<>();
        carRepository.findAll().forEach(car -> {
            if (car.isAvailable()) cars.add(car);
            else {
                List<RequestCar> requestCars = requestCarServices.findByLicence(car.getLicence());
                requestCars =sortRequest(requestCars);
                RequestCar requestCarLatest ;
                int count =0;
                while (true){
                    if (requestCars.get(count).getStatus().equals("accept")){
                        requestCarLatest = requestCars.get(count);
                        break;
                    }
                    else count++;
                }
                String[] date = requestCarLatest.getRequestCarID().getDateTime().toString().split("-");
                LocalDate d = requestCarLatest.getRequestCarID().getDateTime();
                String[] updatedate = requestCarLatest.getUpdateDate().toString().split("-");
                LocalDate u = requestCarLatest.getUpdateDate();
                String[] now = LocalDate.now().toString().split("-");
                int rentDays = requestCarLatest.getNumDates();
                if (d.isEqual(u)) {
                    if (Integer.parseInt(date[2]) + rentDays < Integer.parseInt(now[2]) || Integer.parseInt(date[1]) < Integer.parseInt(now[1])) {//need to change
                        car.setAvailable(true);
                        carRepository.save(car);
                        cars.add(car);
                    }
                }
                else {
                    if (Integer.parseInt(updatedate[2]) + rentDays < Integer.parseInt(now[2]) || Integer.parseInt(updatedate[1]) < Integer.parseInt(now[1])) {//need to change
                        car.setAvailable(true);
                        carRepository.save(car);
                        cars.add(car);
                    }
                }
            }
        });
        return cars;
    }

    @Override
    public List<Car> findAllCars(){
        List<Car> cars = new ArrayList<>();
        carRepository.findAll().forEach(car -> cars.add(car));
        return cars ;
    }

    private List<RequestCar> sortRequest(List<RequestCar> carList){
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

    private int compareDate(LocalDate d1, LocalDate d2){
        return d1.compareTo(d2);
    }

    @Override
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Optional<Car> findById(String licence) {
        return carRepository.findById(licence);
    }
}
