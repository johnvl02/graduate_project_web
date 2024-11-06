package com.john.graduate_project.controller.android;

import com.john.graduate_project.controller.services.CarService;
import com.john.graduate_project.controller.services.ReviewService;
import com.john.graduate_project.controller.services.UserService;
import com.john.graduate_project.dto.UserDto;
import com.john.graduate_project.model.Car;
import com.john.graduate_project.model.ClassIDs.ReviewForCarID;
import com.john.graduate_project.model.ClassIDs.ReviewForUserID;
import com.john.graduate_project.model.ReviewForCar;
import com.john.graduate_project.model.ReviewForUser;
import com.john.graduate_project.model.User;
import com.john.graduate_project.security.JWTGenerator;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;

@Controller
public class ReviewControllerAndroid {
    private final JWTGenerator jwtGenerator = new JWTGenerator();
    private final ReviewService reviewService;
    private final CarService carService;
    private final UserService userService;

    public ReviewControllerAndroid(ReviewService reviewService, CarService carService, UserService userService) {
        this.reviewService = reviewService;
        this.carService = carService;
        this.userService = userService;
    }
    @PostMapping("/android/Review/Car")
    public ResponseEntity<HashMap<String, String>> reviewCarSave(@RequestBody HashMap<String,String> body){
        System.out.println(body);
        String username = jwtGenerator.getUsernameJWT(body.get("renter"));
        Car car = reviewService.requestReviewCar(body.get("license"), body.get("date"), username);
        if (car != null) {
            ReviewForCar reviewForCar = new ReviewForCar(new ReviewForCarID(username, body.get("license")), userService.getUser(body.get("renter")),
                    carService.showCar(body.get("license"),username), body.get("review"), Integer.parseInt(body.get("rating")));
            String message = reviewService.reviewCar(reviewForCar);
            return ResponseEntity.ok(
                    new HashMap<>(){{
                        put("msg",message);
                    }}
            );
        }
        else {
            return ResponseEntity.ok(
                    new HashMap<>(){{
                        put("msg","Something went wrong, please try again");
                    }}
            );
        }
    }

    @PostMapping("/android/Review/User")
    public ResponseEntity<HashMap<String, String>> reviewUserSave(@RequestBody HashMap<String,String> body){
        User renter = reviewService.requestReviewUser(body.get("license"), body.get("date"), body.get("renter"));
        String username = jwtGenerator.getUsernameJWT(body.get("owner"));
        if (renter != null) {
            ReviewForUser reviewForUser = new ReviewForUser(new ReviewForUserID(body.get("renter"), username),userService.getUser(username),
                    userService.getUser(body.get("renter")), body.get("review"), Integer.parseInt(body.get("rating")));
            String message = reviewService.reviewUser(reviewForUser);
            return ResponseEntity.ok(
                    new HashMap<>(){{
                        put("msg",message);
                    }}
            );
        }
        else {
            return ResponseEntity.ok(
                    new HashMap<>(){{
                        put("msg","Something went wrong, please try again");
                    }}
            );
        }
    }

}
