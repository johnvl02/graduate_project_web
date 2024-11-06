package com.john.graduate_project.controller.web;

import com.john.graduate_project.controller.services.CarService;
import com.john.graduate_project.model.Car;
import com.john.graduate_project.model.RenterInfo;
import com.john.graduate_project.model.ReviewForCar;
import com.john.graduate_project.security.JWTGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@SessionAttributes("user")
public class CarController {
    private final JWTGenerator jwtGenerator = new JWTGenerator();
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/rent")
    public String availableCar(@CookieValue(value = "token", defaultValue = "none")String token, @RequestParam(name = "page")int page,@RequestParam(name = "size")int size, @RequestParam(name = "sort")String sort, Model model){
        if (jwtGenerator.validateToken(token)) {
            List<Car> carList = carService.availableCar(page, size, sort);
            int cars = carService.findPages();
            model.addAttribute("cars", carList);
            model.addAttribute("currentPage",page);
            if (cars == size){
                model.addAttribute("allPages",(cars/size));
            }
            else
                model.addAttribute("allPages",(cars/size)+1);
            model.addAttribute("currentSize",size);
            model.addAttribute("currentSort",sort);
            model.addAttribute("owner", new Car());
            return "rentCar";
        }
        else {
            model.addAttribute("msg","Please login first");
            return "/login";
        }
    }

    @GetMapping("/addcar")
    public String Car(@CookieValue(value = "token", defaultValue = "none")String token, Model model){
        if (jwtGenerator.validateToken(token)) {
            model.addAttribute("car", carService.newCar());
            return "addCar";
        }
        else {
            model.addAttribute("msg","Please login first");
            return "/login";
        }
    }

    @PostMapping("/addcar")
    public RedirectView addCar(@CookieValue(value = "token", defaultValue = "none")String token, @ModelAttribute(name = "car") Car car, @RequestParam("image") MultipartFile multipartFile, RedirectAttributes attributes){
        if (jwtGenerator.validateToken(token)) {
            attributes.addFlashAttribute("msg", carService.addCar(jwtGenerator.getUsernameJWT(token), car, multipartFile));
            return new RedirectView("/homepage");
        }
        else {
            attributes.addFlashAttribute("msg","Please login again");
            return new RedirectView("/login");
        }
    }

    @GetMapping("/editCar")
    public Object getEditCar(@CookieValue(value = "token", defaultValue = "none")String token, @RequestParam(name = "param")String license, RedirectAttributes attributes,Model model){
        if (jwtGenerator.validateToken(token)) {
            Car car = carService.getCar(license);
            if (car != null){
                car.setOwner(null);
                model.addAttribute("temp", car);
                model.addAttribute("car", new Car());
                return  "editCar";
            }
            else {
                attributes.addFlashAttribute("msg", "The car don't found, try again");
                return new RedirectView("/MyProfile");
            }

        }
        else {
            attributes.addFlashAttribute("msg","Please login again");
            return new RedirectView("/login");
        }
    }

    @PostMapping("/editCar")
    public RedirectView editCar(@CookieValue(value = "token", defaultValue = "none")String token, @ModelAttribute(name = "car") Car car, @RequestParam("image") MultipartFile multipartFile, RedirectAttributes attributes){
        if (jwtGenerator.validateToken(token)) {
            attributes.addFlashAttribute("msg", carService.editCar(jwtGenerator.getUsernameJWT(token), car, multipartFile));
            return new RedirectView("/MyProfile");
        }
        else {
            attributes.addFlashAttribute("msg","Please login again");
            return new RedirectView("/login");
        }
    }

    @GetMapping("/deleteCar")
    public Object deleteCar(@CookieValue(value = "token", defaultValue = "none")String token, @RequestParam(name = "param")String license, RedirectAttributes attributes,Model model){
        if (jwtGenerator.validateToken(token)) {
            attributes.addFlashAttribute("msg",  carService.deleteCar(license));
            return new RedirectView("/MyProfile");
        }
        else {
            attributes.addFlashAttribute("msg","Please login again");
            return new RedirectView("/login");
        }
    }

    @GetMapping("/showCar")
    public String show(@CookieValue(value = "token", defaultValue = "none")String token, @RequestParam(name = "param") String license, Model model){
        if (jwtGenerator.validateToken(token)) {
            Car c = carService.showCar(license, jwtGenerator.getUsernameJWT(token));
            if (c == null){
                model.addAttribute("info", new RenterInfo());
                return "moreInfo";
            }
            else if (!c.getLicense().equals("null")) {
                List<ReviewForCar> review = carService.getReviews(license);
                List<String> reserveDates = carService.reserveDates(license);
                model.addAttribute("car", c);
                model.addAttribute("Review", review);
                model.addAttribute("reserve", reserveDates);
                if (review == null)
                    model.addAttribute("people", 0);
                else
                    model.addAttribute("people", review.size());
                return "showCar";
            }
            else {
                System.out.println("No car found");
                return null;
            }
        }
        else {
            model.addAttribute("msg","Please login again");
            return "/login";
        }
    }

    @GetMapping("showCar/Rent")
    public RedirectView rent(@CookieValue(value = "token", defaultValue = "none")String token, @RequestParam("days")String d,@RequestParam("license")String l, @ModelAttribute("msg")String message, RedirectAttributes attributes){
        if (jwtGenerator.validateToken(token)) {
            attributes.addFlashAttribute("msg", carService.rentCar(d, l, jwtGenerator.getUsernameJWT(token)));
            return new RedirectView("/homepage");
        }
        else {
            attributes.addFlashAttribute("msg","Please login again");
            return new RedirectView("/login");
        }
    }

    @GetMapping("requestFor")
    public String requestsFor(@CookieValue(value = "token", defaultValue = "none")String token, Model model){
        if (jwtGenerator.validateToken(token)) {
            List<Object> res = carService.request(jwtGenerator.getUsernameJWT(token));
            model.addAttribute("request_for", res.get(0));
            return "requestRentACar";
        }
        else {
            model.addAttribute("msg","Please login again");
            return "/login";
        }
    }

    @GetMapping("requestMy")
    public String requestsMy(@CookieValue(value = "token", defaultValue = "none")String token, Model model){
        if (jwtGenerator.validateToken(token)) {
            List<Object> res = carService.request(jwtGenerator.getUsernameJWT(token));
            model.addAttribute("request_to", res.get(1));
            return "requestRentMyCar";
        }
        else {
            model.addAttribute("msg","Please login again");
            return "/login";
        }
    }
}
