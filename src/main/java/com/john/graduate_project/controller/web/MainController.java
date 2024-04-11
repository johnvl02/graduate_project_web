package com.john.graduate_project.controller.web;

import com.john.graduate_project.controller.services.MainService;
import com.john.graduate_project.dto.UserDto;
import com.john.graduate_project.model.Car;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@SessionAttributes("user")
public class MainController {
    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    /*@ModelAttribute("user")
    public User user(){
        return new User();
    }*/

    @GetMapping("/homepage")
    public String HomePage(@ModelAttribute("user") UserDto user, Model model){
        model.addAttribute("cars",mainService.homePage());
        model.addAttribute("owner", new Car());
        return "homepage";
    }

    @GetMapping("profile")
    public String showProfile(@ModelAttribute("user")UserDto owner, @RequestParam("name")String username, Model model){
        List<Object> res = mainService.profile(owner.userdtoToUSer(), username);
        model.addAttribute("rating", res.get(0));
        model.addAttribute("people", res.get(1));
        model.addAttribute("reviews",res.get(2));
        model.addAttribute("renter",res.get(3));
        model.addAttribute("info", res.get(4));
        return "profile";
    }

    @GetMapping("updateStatus")
    public RedirectView updateStatus(@ModelAttribute("user")UserDto user, @RequestParam("param1")String id, @RequestParam("param2")String value, RedirectAttributes attributes){
        attributes.addFlashAttribute("msg", mainService.updateStatus(id,value,user.userdtoToUSer()));
        return new RedirectView("/request");
    }

    @GetMapping("maps")
    public String maps(@ModelAttribute("user")UserDto user, Model model){
        List<Car> cars = mainService.maps();
        model.addAttribute("cars",cars);
        return "maps";
    }

    @GetMapping("contact")
    public String contact(@ModelAttribute("user")UserDto user){
        return "contact";
    }
}
