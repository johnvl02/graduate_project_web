package com.john.graduate_project.controller.web;

import com.john.graduate_project.controller.services.ReviewService;
import com.john.graduate_project.dto.UserDto;
import com.john.graduate_project.model.Car;
import com.john.graduate_project.model.ClassIDs.ReviewForCarID;
import com.john.graduate_project.model.ClassIDs.ReviewForUserID;
import com.john.graduate_project.model.ReviewForCar;
import com.john.graduate_project.model.ReviewForUser;
import com.john.graduate_project.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@SessionAttributes("user")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("request/ReviewCar")
    public String reviewCar(@ModelAttribute("user") UserDto user, @RequestParam("param")String licence, @RequestParam("param2")String date, Model model, HttpSession session){
        Car car = reviewService.requestReviewCar(licence,date,user.userdtoToUSer());
        if (car != null){
            model.addAttribute("reviewC", new ReviewForCar());
            session.setAttribute("review",new ReviewForCar(new ReviewForCarID(user.getUsername(),car.getLicence()),user.userdtoToUSer(), car));
        }
        return "reviewCar";
    }

    @PostMapping("Review/Car")
    public RedirectView reviewCarSave(@ModelAttribute("user")UserDto user, @ModelAttribute("reviewC")ReviewForCar review, HttpSession session, RedirectAttributes attributes){
        ReviewForCar forCar = (ReviewForCar) session.getAttribute("review");
        forCar.setReview(review.getReview());
        forCar.setStars(review.getStars());
        String message = reviewService.reviewCar(forCar,user.userdtoToUSer());
        attributes.addFlashAttribute("msg", message);
        return new RedirectView("/homepage");
    }

    @GetMapping("request/ReviewUser")
    public String reviewUser(@ModelAttribute("user")UserDto user, @RequestParam("param")String licence, @RequestParam("param2")String date, @RequestParam("param3")String username,
                             Model model, HttpSession session){
        User renter = reviewService.requestReviewUser(user.userdtoToUSer(), licence, date, username);
        if (renter != null){
            model.addAttribute("reviewU", new ReviewForUser());
            session.setAttribute("review",new ReviewForUser(new ReviewForUserID(username,user.getUsername()), user.userdtoToUSer(), renter));
        }
        else {
            //todo
        }
        return "reviewUser";
    }

    @PostMapping("Review/User")
    public RedirectView reviewUserSave(@ModelAttribute("user")UserDto user, @ModelAttribute("reviewU")ReviewForUser review,HttpSession session, RedirectAttributes attributes){
        ReviewForUser forUser = (ReviewForUser) session.getAttribute("review");
        forUser.setReview(review.getReview());
        forUser.setStars(review.getStars());
        String message = reviewService.reviewUser(forUser);
        attributes.addFlashAttribute("msg", message);
        return new RedirectView("/homepage");
    }

}
