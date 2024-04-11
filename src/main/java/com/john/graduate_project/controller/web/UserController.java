package com.john.graduate_project.controller.web;

import com.john.graduate_project.controller.services.UserService;
import com.john.graduate_project.dto.UserDto;
import com.john.graduate_project.dto.VerificationCodeDto;
import com.john.graduate_project.model.RenterInfo;
import com.john.graduate_project.model.User;
import com.john.graduate_project.model.VerificationCode;
import com.john.graduate_project.security.JWTGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;

@Controller
@SessionAttributes("user")
public class UserController {

    private final JWTGenerator jwtGenerator = new JWTGenerator();
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserDto user(){
        return new UserDto();
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/login")
    public RedirectView login(@ModelAttribute("user") UserDto user, RedirectAttributes attributes){
        List<Object> list = userService.login(user.userdtoToUSer());
        if (list.get(0) != null){
            attributes.addFlashAttribute("msg", list.get(1));
            attributes.addFlashAttribute("user", list.get(0));//maybe need cast to user because the list have object
            attributes.addFlashAttribute("token", jwtGenerator.generateToken((UserDto) list.get(0)));
            return new RedirectView("/homepage");
        }
        else {
            attributes.addFlashAttribute("msg", list.get(1));
            attributes.addFlashAttribute("user", user());
            return new RedirectView("/login");
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDto user, Model model){
        List<Object> list = userService.registerUser(user.userdtoToUSer());
        model.addAttribute("msg", list.get(0));
        //model.addAttribute("user", list.get(1));
        model.addAttribute("vCode", new VerificationCodeDto(((UserDto) list.get(1)).getUsername()));
        return "activateAccount";
//        return  new ModelAndView("login","user",user);
    }

    @GetMapping("/verification")
    public String das(@ModelAttribute("msg")String message, @ModelAttribute("vCode")VerificationCodeDto dto, Model model){
        model.addAttribute("msg", message);
        model.addAttribute("vCode",dto);
        return "activateAccount";
    }

    @PostMapping("/verification")
    public RedirectView activateAccount(@ModelAttribute("vCode")VerificationCodeDto code, RedirectAttributes attributes){
        HashMap<String,Object> map = userService.verificationAccount(code.VCDtoToVC());
        switch ((int)map.get("page")){
            case 0:
                attributes.addFlashAttribute("msg",map.get("msg").toString());
                attributes.addFlashAttribute("user", new UserDto());
                return new RedirectView("/register");
            case 1:
                attributes.addFlashAttribute("msg",map.get("msg").toString());
                attributes.addFlashAttribute("user", map.get("user"));
                return new RedirectView("/homepage");
            default:
                attributes.addFlashAttribute("msg",map.get("msg").toString());
                attributes.addFlashAttribute("vCode",map.get("vCode"));
                return new RedirectView("/verification");
        }
    }

    @GetMapping("MyProfile")
    public String viewMyProfile(@ModelAttribute("user")UserDto user, Model model){
        List<Object> result = userService.myProfile(user.userdtoToUSer());
        model.addAttribute("cars_review",result.get(0));
        model.addAttribute("reviews",result.get(1));
        return "myProfile";
    }

    @GetMapping("editProfile")
    public String edit(@ModelAttribute("user")UserDto u, Model model){
        model.addAttribute("temp", u);
        return "editProfile";
    }

    @PostMapping("editProfile")// change this method from post to put
    public RedirectView editProfile(@ModelAttribute("user")UserDto u, @ModelAttribute("temp")UserDto user, RedirectAttributes attributes){
        String msg = userService.updateUser(user.userdtoToUSer(), u.userdtoToUSer());
        attributes.addFlashAttribute("msg", msg);
        return new RedirectView("/homepage");
    }

    @GetMapping("logout")
    public RedirectView logout(Model model, RedirectAttributes attributes){
        model.addAttribute("user",user());
        attributes.addFlashAttribute("msg", "You have been log out successfully");
        return new RedirectView("login");
    }

    @PostMapping("moreInfo")
    public RedirectView info(@ModelAttribute("user")UserDto user, @ModelAttribute("info") RenterInfo info, RedirectAttributes attributes){
        attributes.addFlashAttribute("msg", userService.addInfo(info, user.userdtoToUSer()));
        return new RedirectView("/rent");
    }
}
