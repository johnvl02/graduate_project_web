package com.john.graduate_project.controller.android;


import com.john.graduate_project.controller.services.MainService;
import com.john.graduate_project.model.Car;
import com.john.graduate_project.security.JWTGenerator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Controller
public class MainControllerAndroid {

    private final JWTGenerator jwtGenerator = new JWTGenerator();
    private final MainService mainService;

    public MainControllerAndroid(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/android/homepage")
    public ResponseEntity<HashMap<String, HashMap<String, Object>>> HomePage(){
        HashMap<String, HashMap<String, Object>> map = new HashMap<>();
        int i =0;
        List<Car> carList = mainService.homePage();
        for (Car car: carList) {
            map.put(String.valueOf(i), car.carToHashMap());
            i++;
        }
        return ResponseEntity.ok(map);
    }


    @GetMapping("/android/image")
    public ResponseEntity<Resource> test(@RequestParam("name")String name) throws MalformedURLException {
        // Path to the image file

        Car car = mainService.findCar(name);
        Path path = Paths.get(car.getPhotoPath().replaceFirst("/",""));
        // Load the resource
        Resource resource = new UrlResource(path.toUri());
        // Return ResponseEntity with image content type
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/android/updateStatus")
    public ResponseEntity<HashMap<String, String>> updateStatus(@RequestParam("token")String token, @RequestParam("license")String license, @RequestParam("date")String date,
                                     @RequestParam("status")String status, @RequestParam("renter")String renter){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(renter).append(" ").append(license).append(" ").append(date);
        String msg = mainService.updateStatus(stringBuilder.toString(), status, jwtGenerator.getUsernameJWT(token));
        return ResponseEntity.ok(new HashMap<>(){{put("msg",msg);}});
    }

    @GetMapping("/android/maps")
    public ResponseEntity<HashMap<String, Object>> maps(){
        List<Car> cars = mainService.maps();
        HashMap<String, Object> map = new HashMap<>();
        int i =0;
        for (Car c: cars) {
            c.setOwner(null);
            map.put(String.valueOf(i),c);
            i++;
        }
        return ResponseEntity.ok(map);
    }


}
