package com.john.graduate_project.controller.web;

import com.john.graduate_project.controller.services.CarService;
import com.john.graduate_project.dto.UserDto;
import com.john.graduate_project.model.Car;
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
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/rent")
    public String availableCar(@ModelAttribute("user") UserDto user, Model model){
        model.addAttribute("cars",carService.availableCar());
        model.addAttribute("owner", new Car());
        return "rentCar";
    }

    @GetMapping("/addcar")
    public String Car(@ModelAttribute("user")UserDto user, Model model){
        model.addAttribute("car", carService.newCar());
        return "addCar";
    }

    @PostMapping("/addcar")
    public RedirectView addCar(@ModelAttribute("user") UserDto user, @ModelAttribute(name = "car") Car car, @RequestParam("image") MultipartFile multipartFile, RedirectAttributes attributes){
        attributes.addFlashAttribute("msg", carService.addCar(user.userdtoToUSer(), car,multipartFile));
        return new RedirectView("/homepage");
    }

    @GetMapping("/showCar")
    public String show(@ModelAttribute("user")UserDto user, @RequestParam(name = "param") String license, Model model){
        List<Object> res = carService.showCar(license,user.userdtoToUSer());
        if (res.size() <=2){
            model.addAttribute("info", res.get(0));
            return "moreInfo";
        }
        else {
            model.addAttribute("car", res.get(0));
            model.addAttribute("Review", res.get(1));
            model.addAttribute("rating", res.get(2));
            model.addAttribute("people", res.get(3));
            return  "showCar";
        }
    }
    @GetMapping("showCar/Rent")
    public RedirectView rent(@ModelAttribute("user") UserDto renter, @RequestParam("days")int d,@RequestParam("licence")String l, @ModelAttribute("msg")String message, RedirectAttributes attributes){
        attributes.addFlashAttribute("msg", carService.rentCar(d,l,renter.userdtoToUSer()));
        return new RedirectView("/homepage") ;
    }

    @GetMapping("request")
    public String requests(@ModelAttribute("user")UserDto user, Model model){
        List<Object> res = carService.request(user.userdtoToUSer());
        model.addAttribute("request_for",res.get(0));
        model.addAttribute("request_to",res.get(1));
        return "request";
    }
}
