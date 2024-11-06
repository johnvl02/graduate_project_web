package com.john.graduate_project.controller.web;

import com.john.graduate_project.controller.services.MainService;
import com.john.graduate_project.dto.UserDto;
import com.john.graduate_project.model.Car;
import com.john.graduate_project.security.JWTGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@SessionAttributes("user")
public class MainController {
    private final JWTGenerator jwtGenerator = new JWTGenerator();
    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/homepage")
    public String HomePage(@CookieValue(value = "token", defaultValue = "none")String token,@ModelAttribute("user") UserDto user, Model model){
        if (jwtGenerator.validateToken(token)) {
            model.addAttribute("cars", mainService.homePage());
            model.addAttribute("owner", new Car());
            return "homepage";
        }
        else {
            model.addAttribute("msg","Please login first");
            return "/login";
        }
    }

    @GetMapping("updateStatus")
    public RedirectView updateStatus(@CookieValue(value = "token", defaultValue = "none")String token, @ModelAttribute("user")UserDto user, @RequestParam("param1")String id, @RequestParam("param2")String value, RedirectAttributes attributes){
        if (jwtGenerator.validateToken(token)) {
            attributes.addFlashAttribute("msg", mainService.updateStatus(id, value, jwtGenerator.getUsernameJWT(token)));
            return new RedirectView("/homepage");
        }
        else {
            attributes.addFlashAttribute("msg","Please login again");
            return new RedirectView("/login");
        }
    }

    @GetMapping("maps")
    public String maps(@CookieValue(value = "token", defaultValue = "none")String token, @ModelAttribute("user")UserDto user, Model model){
        if (jwtGenerator.validateToken(token)) {
            List<Car> cars = mainService.maps();
            for (Car c: cars) {
                c.setOwner(null);
            }
            model.addAttribute("cars",cars);
            return "maps";
        }
        else {
            model.addAttribute("msg","Please login first");
            return "/login";
        }
    }


    @GetMapping("contact")
    public String contact(@ModelAttribute("user")UserDto user){
        return "contact";
    }
}
