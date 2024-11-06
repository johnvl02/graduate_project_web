package com.john.graduate_project.controller.web;

import com.john.graduate_project.controller.services.UserService;
import com.john.graduate_project.dto.UserDto;
import com.john.graduate_project.dto.VerificationCodeDto;
import com.john.graduate_project.model.RenterInfo;
import com.john.graduate_project.model.VerificationCode;
import com.john.graduate_project.security.JWTGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;
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

    private Cookie createCookie(UserDto user){
        String token = jwtGenerator.generateToken(user);
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(7*24*60*60);//expires in 7 days
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    @ModelAttribute("user")
    public UserDto user(){
        return new UserDto();
    }

    @GetMapping("/login")
    public Object loginPage(@CookieValue(value = "token", defaultValue = "none")String token, RedirectAttributes attributes, HttpServletResponse response){
        if (token.equals("none")){
            return "/login";
        }
        else {
            if (jwtGenerator.validateToken(token)){
                String username = jwtGenerator.getUsernameJWT(token);
                List<Object> list = userService.alreadylogin(username);
                attributes.addFlashAttribute("msg", list.get(1));
                attributes.addFlashAttribute("user", list.get(0));
                return new RedirectView("/homepage");
            }
            else {
                return "/login";
            }
        }
    }

    @PostMapping("/login")
    public RedirectView login(@ModelAttribute("user") UserDto user, RedirectAttributes attributes, HttpServletResponse response){
        HashMap<String, Object> res = userService.login(user.userdtoToUSer());
        if (res.get("user") != null){
            attributes.addFlashAttribute("msg", res.get("msg"));
            attributes.addFlashAttribute("user", res.get("user"));
            response.addCookie(createCookie((UserDto) res.get("user")));
            Cookie cookie = new Cookie("Name", ((UserDto) res.get("user")).getFirstName());
            cookie.setMaxAge(24*60*60);
            cookie.setPath("/");
            response.addCookie(cookie);
            return new RedirectView("/homepage");
        }
        else {
            attributes.addFlashAttribute("msg", res.get("msg"));
            attributes.addFlashAttribute("user", user());
            return new RedirectView("/login");
        }
    }

    @GetMapping("/resetPassword")
    public String resetPasswordForm(Model model){
        model.addAttribute("user", new UserDto());
        return "emailForm";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@ModelAttribute("user")UserDto userDto, Model model){
        HashMap<String, Object> f = userService.verifyAccount(userDto);
        model.addAttribute("msg",f.get("msg"));
        if ((boolean) f.get("find")){
            model.addAttribute("user", new UserDto(userDto.getUsername()));
            model.addAttribute("code", new VerificationCodeDto());
            return "changePassword";
        }
        else {
            model.addAttribute("user", new UserDto());
            return "emailForm";
        }
    }

    @PostMapping("/createNewPassword")
    public Object createPassword(@ModelAttribute("user")UserDto userDto, @ModelAttribute("code") VerificationCodeDto dto, RedirectAttributes attributes, Model model){
        HashMap<String, Object> res = userService.createNewPassword(userDto,dto);
        if ((boolean) res.get("user")){
            attributes.addFlashAttribute("msg", res.get("msg"));
            return new RedirectView("/login");
        }
        else {
            model.addAttribute("msg", res.get("msg"));
            model.addAttribute("user", new UserDto(userDto.getUsername()));
            model.addAttribute("code", new VerificationCodeDto());
            return "changePassword";
        }

    }

    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDto user, Model model){
        HashMap<String, Object> list = userService.registerUser(user.userdtoToUSer());
        model.addAttribute("msg", list.get("msg"));
        model.addAttribute("vCode", new VerificationCodeDto(((UserDto) list.get("user")).getUsername()));
        return "activateAccount";
    }

    @GetMapping("/verification")
    public String verificationForm(@ModelAttribute("msg")String message, @ModelAttribute("vCode")VerificationCodeDto dto, Model model){
        model.addAttribute("msg", message);
        model.addAttribute("vCode",dto);
        return "activateAccount";
    }

    @PostMapping("/verification")
    public RedirectView activateAccount(@ModelAttribute("vCode")VerificationCodeDto code, RedirectAttributes attributes, HttpServletResponse response){
        HashMap<String,Object> map = userService.verificationAccount(code.VCDtoToVC());
        switch ((int)map.get("page")){
            case 0:
                attributes.addFlashAttribute("msg",map.get("msg").toString());
                attributes.addFlashAttribute("user", new UserDto());
                return new RedirectView("/register");
            case 1:
                attributes.addFlashAttribute("msg",map.get("msg").toString());
                attributes.addFlashAttribute("user", map.get("user"));
                response.addCookie(createCookie((UserDto) map.get("user")));
                Cookie cookie = new Cookie("Name", ((UserDto) map.get("user")).getFirstName());
                cookie.setMaxAge(24*60*60);
                cookie.setPath("/");
                response.addCookie(cookie);
                return new RedirectView("/homepage");
            default:
                attributes.addFlashAttribute("msg",map.get("msg").toString());
                attributes.addFlashAttribute("vCode",map.get("vCode"));
                return new RedirectView("/verification");
        }
    }

    @GetMapping("MyProfile")
    public String viewMyProfile(@CookieValue(value = "token", defaultValue = "none")String token, @ModelAttribute("user")UserDto user, Model model){
        if (jwtGenerator.validateToken(token)) {
            List<Object> result = userService.myProfile(jwtGenerator.getUsernameJWT(token), false);
            model.addAttribute("cars_review", result.get(0));
            model.addAttribute("reviews", result.get(1));
            return "myProfile";
        }
        else {
            model.addAttribute("msg","Please login again");
            return "/login";
        }
    }

    @GetMapping("editProfile")
    public String edit(@CookieValue(value = "token", defaultValue = "none")String token, @ModelAttribute("user")UserDto u, Model model){
        if (jwtGenerator.validateToken(token)) {
            model.addAttribute("temp", u);
            return "editProfile";
        }
        else {
            model.addAttribute("msg","Please login first");
            return "/login";
        }
    }

    @PostMapping("editProfile")
    public RedirectView editProfile(@CookieValue(value = "token", defaultValue = "none")String token, @ModelAttribute("user")UserDto u, @ModelAttribute("temp")UserDto user, RedirectAttributes attributes){
        if (jwtGenerator.validateToken(token)) {
            String msg = userService.updateUser(user.userdtoToUSer(), u.userdtoToUSer());
            attributes.addFlashAttribute("msg", msg);
            return new RedirectView("/homepage");
        }
        else {
            attributes.addFlashAttribute("msg","Please login again");
            return new RedirectView("/login");
        }
    }

    @GetMapping("profile")
    public String showProfile(@CookieValue(value = "token", defaultValue = "none")String token, @ModelAttribute("user")UserDto owner, @RequestParam("name")String username, Model model){
        if (jwtGenerator.validateToken(token)) {
            List<Object> res = userService.profile(username);
            model.addAttribute("rating", res.get(0));
            model.addAttribute("people", res.get(1));
            model.addAttribute("reviews", res.get(2));
            model.addAttribute("renter", res.get(3));
            model.addAttribute("info", res.get(4));
            return "profile";
        }
        else {
            model.addAttribute("msg","Please login again");
            return "/login";
        }
    }

    @GetMapping("logout")
    public String logout(Model model, HttpServletResponse response){
        model.addAttribute("user",user());
        model.addAttribute("msg", "You have been log out successfully");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        Cookie cookie2 = new Cookie("Name", null);
        cookie2.setMaxAge(0);
        cookie2.setPath("/");
        response.addCookie(cookie2);
        return "/login";
    }

    @PostMapping("moreInfo")
    public RedirectView info(@CookieValue(value = "token", defaultValue = "none")String token, @ModelAttribute("user")UserDto user, @ModelAttribute("info") RenterInfo info, RedirectAttributes attributes){
        if (jwtGenerator.validateToken(token)) {
            attributes.addFlashAttribute("msg", userService.addInfo(info, jwtGenerator.getUsernameJWT(token)));
            return new RedirectView("/rent?page=0&size=12&sort=none");
        }
        else {
            attributes.addFlashAttribute("msg","Please login again");
            return new RedirectView("/login");
        }
    }
}
