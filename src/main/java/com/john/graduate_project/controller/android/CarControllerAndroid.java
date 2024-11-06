package com.john.graduate_project.controller.android;


import com.john.graduate_project.controller.services.CarService;
import com.john.graduate_project.model.Car;
import com.john.graduate_project.model.RequestCar;
import com.john.graduate_project.model.ReviewForCar;
import com.john.graduate_project.model.types.FuelType;
import com.john.graduate_project.model.types.TransmissionType;
import com.john.graduate_project.security.JWTGenerator;
import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

@Controller
public class CarControllerAndroid {

    private final String path = "src/main/resources/static/cars-photo/";
    private final JWTGenerator jwtGenerator = new JWTGenerator();
    private final CarService carService;

    public CarControllerAndroid(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/android/rent")
    public ResponseEntity<HashMap<String, Object>> availableCar(@RequestParam(name = "page")int page,@RequestParam(name = "size")int size, @RequestParam(name = "sort")String sort){
        List<Car> carList = carService.availableCar(page, size, sort);
        int cars = carService.findPages();
        HashMap<String,HashMap<String,Object>> map = new HashMap<>();
        int i=0;
        for (Car c:carList) {
            map.put(String.valueOf(i),c.carToHashMap());
            i++;
        }
        int allpages;
        if (cars == size){
            allpages = cars/size;
        }
        else
            allpages = (cars/size)+1;
        return ResponseEntity.ok(
                new HashMap<>(){{
                    put("cars", map);
                    put("currentPage",page);
                    put("allPages",allpages);
                    put("currentSize",size);
                    put("currentSort",sort);
                }}
        );
    }

    @PostMapping("/android/showCar")
    public ResponseEntity<HashMap<String, Object>> show(@RequestBody HashMap<String,String> body){
        Car car = carService.showCar(body.get("license"), jwtGenerator.getUsernameJWT(body.get("token")));
        if (car == null){
            return ResponseEntity.ok(
                    new HashMap<>(){{
                        put("status", 0);
                        put("msg", "Please fill out the form first");
                    }});
        } else if (!car.getLicense().equals("null")) {
            List<ReviewForCar> review = carService.getReviews(body.get("license"));
            List<String> reserveDates = carService.reserveDates(body.get("license"));
            int len ;
            List<HashMap<String,Object>> reviews = new ArrayList<>();
            if (review == null)
                len = 0;
            else{
                len = review.size();
                for (ReviewForCar r:review) {
                    reviews.add(r.reviewToHashMap());
                }
            }

            return ResponseEntity.ok(
                    new HashMap<>(){{
                        put("status", 1);
                        put("car", car.carToHashMap());
                        put("reviews", reviews);
                        put("reviewsLen",len);
                        put("reserve",reserveDates);
                    }});
        }
        else return null;
    }

    @GetMapping("/android/showCar/Rent")
    public ResponseEntity<HashMap<String, String>> rent(@RequestParam(name = "token")String token, @RequestParam("days")String d, @RequestParam("license")String l){
        String msg = carService.rentCar(d, l, jwtGenerator.getUsernameJWT(token));
        return ResponseEntity.ok(new HashMap<>(){
            {put("msg",msg);}
        });
    }

    @PostMapping("/android/addCar")
    public ResponseEntity<HashMap<String, String>>  addCar(@RequestBody HashMap<String,String> body){
        String token = body.get("token");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");

        String photo = LocalDateTime.now().format(formatter)+".jpg";

        String img = body.get("image");
        byte[] bytes = Base64.getDecoder().decode(img);
        File outFile = new File(path + jwtGenerator.getUsernameJWT(token)+"/" + photo);
        try {
            FileUtils.writeByteArrayToFile(outFile,bytes);
            String msg = carService.addCar(new Car(
                    body.get("license"), body.get("model"), FuelType.valueOf(body.get("fuel")) , TransmissionType.valueOf(body.get("transmission")),
                    body.get("days"), true, Integer.parseInt(body.get("seats")), Long.parseLong(body.get("value")), photo, body.get("address"),
                    body.get("description"), 0.0, jwtGenerator.getUsernameJWT(token)));
            return ResponseEntity.ok(new HashMap<>(){
                {put("msg",msg);}
            });
        } catch (IOException e) {
            return null;
        }
    }

    @GetMapping("/android/editCar")
    public ResponseEntity<HashMap<String, Object>> getEditCar(@RequestParam(name = "license")String license){
        Car car = carService.getCar(license);
        if (car != null){
            car.setOwner(null);
            HashMap<String, Object> temp = car.carToHashMap();
            temp.put("msg","good");
            return  ResponseEntity.ok(temp);
        }
        else {
            return ResponseEntity.ok(new HashMap<>(){{put("msg", "The car don't found, try again");}});
        }
    }

    @PostMapping("/android/editCar")
    public ResponseEntity<HashMap<String, String>> editCar(@RequestBody HashMap<String,String> body){
        String token = body.get("token");
        String msg;
        Car car = carService.getCar(body.get("license"));
        if (Boolean.parseBoolean(body.get("new image"))){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");

            String photo = LocalDateTime.now().format(formatter)+".jpg";

            String img = body.get("image");
            byte[] bytes = Base64.getDecoder().decode(img);
            File outFile = new File(path + jwtGenerator.getUsernameJWT(token)+"/" + photo);
            try {
                FileUtils.writeByteArrayToFile(outFile,bytes);
                msg = carService.editCar(new Car(
                        body.get("license"), car.getModel(), car.getFuel() , car.getTransmission(),
                        body.get("days"), true, car.getNumSeats(), Long.parseLong(body.get("value")), photo, body.get("address"),
                        body.get("description"), car.getRating(), jwtGenerator.getUsernameJWT(token)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {

            msg = carService.editCar(new Car(
                    body.get("license"), car.getModel(), car.getFuel() , car.getTransmission(),
                    body.get("days"), true, car.getNumSeats(), Long.parseLong(body.get("value")), car.getPhoto(), body.get("address"),
                    body.get("description"), car.getRating(), jwtGenerator.getUsernameJWT(token)));
        }
        return ResponseEntity.ok(new HashMap<>(){
            {put("msg",msg);}
        });
    }

    @GetMapping("/android/deleteCar")
    public ResponseEntity<HashMap<String, String>> deleteCar(@RequestParam(name = "license")String license){
        String msg =  carService.deleteCar(license);
        return ResponseEntity.ok(new HashMap<>(){
            {put("msg",msg);}
        });
    }

    @GetMapping("/android/requestCar")
    public ResponseEntity<HashMap<String,HashMap<String,String>>> requestsCar(@RequestParam(name = "token")String token){
        List<Object> res = carService.request(jwtGenerator.getUsernameJWT(token));
        HashMap<String,HashMap<String,String>> temp = new HashMap<>();
        int i = 0;
        for (RequestCar o:(ArrayList<RequestCar>)res.get(0)) {
            temp.put(String.valueOf(i),o.requestToHashmap());
            i++;
        }
        return ResponseEntity.ok(temp);
    }

    @GetMapping("/android/requestMyCar")
    public ResponseEntity<HashMap<String,HashMap<String,String>>> requestsUser(@RequestParam(name = "token")String token){
        List<Object> res = carService.request(jwtGenerator.getUsernameJWT(token));
        HashMap<String,HashMap<String,String>> temp = new HashMap<>();
        int i = 0;
        for (RequestCar o:(ArrayList<RequestCar>)res.get(1)) {
            temp.put(String.valueOf(i),o.requestToHashmap());
            i++;
        }
        return ResponseEntity.ok(temp);
    }
}
