package com.john.graduate_project.controller.android;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.john.graduate_project.controller.services.UserService;
import com.john.graduate_project.dto.UserDto;
import com.john.graduate_project.dto.VerificationCodeDto;
import com.john.graduate_project.model.RenterInfo;
import com.john.graduate_project.model.ReviewForUser;
import com.john.graduate_project.model.User;
import com.john.graduate_project.model.VerificationCode;
import com.john.graduate_project.model.types.UserType;
import com.john.graduate_project.security.JWTGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class UserControllerAndroid {

    private final JWTGenerator jwtGenerator = new JWTGenerator();
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UserControllerAndroid(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/android/login")
    public ResponseEntity<HashMap<String, Object>> loginPage(@RequestBody String body) {
        try {
            UserDto userDto = objectMapper.readValue(body, UserDto.class);
            HashMap<String, Object>res = userService.login(userDto.userdtoToUSer());
            if (res.get("user") != null){
                String token = jwtGenerator.generateToken((UserDto) res.get("user"));
                return ResponseEntity.ok(
                        new HashMap<String, Object>(){{
                            put("msg",res.get("msg"));
                            put("token", token);
                            put("name", ((UserDto) res.get("user")).getFirstName() + " " + ((UserDto) res.get("user")).getLastName());
                        }}
                );
            }
            else return ResponseEntity.ok(
                    new HashMap<String,Object>(){{put("msg",res.get("msg"));}}
                );

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/android/register")
    public ResponseEntity<HashMap<String, Object>> register(@RequestBody String body){
        try {
            UserDto userDto = objectMapper.readValue(body, UserDto.class);
            HashMap<String, Object> res =  userService.registerUser(userDto.userdtoToUSer());
            if (res.get("user") != null){
                System.out.println("ok");
                return ResponseEntity.ok(
                        new HashMap<>(){{
                            put("msg", res.get("msg"));
                            put("status", 0);
                            put("token",jwtGenerator.generateToken(userDto));
                        }}
                );
            }
            else {
                System.out.println("not");
                return ResponseEntity.ofNullable(
                        new HashMap<>() {{
                            put("msg", res.get("msg"));
                            put("status", 1);
                        }}
                );

            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/android/verification")
    public ResponseEntity<HashMap<String, Object>> activateAccount(@RequestBody String body){
        try {
            VerificationCode code = objectMapper.readValue(body, VerificationCode.class);
            HashMap<String,Object> map = userService.verificationAccount(code);
            return ResponseEntity.ok(map);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/android/resetPassword")
    public ResponseEntity<HashMap<String, Object>> resetPassword(@RequestBody String body){
        try {
            UserDto userDto  = objectMapper.readValue(body, UserDto.class);
            HashMap<String, Object> map = userService.verifyAccount(userDto);
            map.put("username",userDto.getUsername());
            return ResponseEntity.ok(map);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/android/createNewPassword")
    public ResponseEntity<HashMap<String, Object>> createPassword(@RequestBody String body){
        body = body.replace("\"","");
        String[] s = body.split(",");
        HashMap<String,String> temp = new HashMap<>();
        for (String s1: s) {
            String[] t = s1.split(":");
            temp.put(t[0].replace("{",""), t[1].replace("}",""));
        }
        HashMap<String, Object> res = userService.createNewPassword(new UserDto(temp.get("username"),temp.get("password"),temp.get("confirmPassword")),
                new VerificationCodeDto(temp.get("username"), Integer.parseInt(temp.get("code"))));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/android/moreInfo")
    public ResponseEntity<HashMap<String,String>> info(@RequestBody HashMap<String, String> body){
        User renter = userService.getUser(jwtGenerator.getUsernameJWT(body.get("token")));
        LocalDate date = LocalDate.parse(body.get("date"));
        String msg = userService.addInfo(new RenterInfo(renter.getUsername(), renter,date, body.get("info")),renter.getUsername());
        if (msg.equals("The information saved successfully")){
            return ResponseEntity.ok(new HashMap<>(){{put("msg",msg);}});
        }
        else {
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body(new HashMap<>(){{put("msg",msg);}});
        }
    }

    @GetMapping("/android/MyProfile")
    public ResponseEntity<HashMap<String,Object>> viewMyProfile(@RequestParam(name = "token")String token){
        List<Object> result = userService.myProfile(jwtGenerator.getUsernameJWT(token), true);
        User user = userService.getUser(jwtGenerator.getUsernameJWT(token));
        return ResponseEntity.ok(new HashMap<>(){
            {
                put("cars",result.get(0));
                put("reviews",result.get(1));
                put("user", user.userToUserDto());
            }
        });
    }

    @PostMapping("/android/editProfile")
    public ResponseEntity<HashMap<String,String>> editProfile(@RequestBody HashMap<String, String> body){
        String token = body.get("token");
        User user = new User(jwtGenerator.getUsernameJWT(token),null, body.get("first name"), body.get("last name"), body.get("email"),
                Long.parseLong(body.get("phone")),Integer.parseInt(body.get("age")), null);
        user.setEnabled(true);
        User olduser = userService.getUser(jwtGenerator.getUsernameJWT(token));
        String msg = userService.updateUser(user , olduser);
        if (msg.equals("Your profile updated successfully")){
            return ResponseEntity.ok(new HashMap<>(){{put("msg",msg);}});
        }
        else {
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body(new HashMap<>(){{put("msg",msg);}});
        }
    }

    @GetMapping("/android/profile")
    public ResponseEntity<HashMap<String,Object>> showProfile(@RequestParam("username")String username, Model model){
        System.out.println(username);
        List<Object> res = userService.profile(username);
        HashMap<String,HashMap<String,String>> temp= new HashMap();
        int i =0;
        for (ReviewForUser r :(ArrayList<ReviewForUser>) res.get(2)){
            temp.put(String.valueOf(i),r.ReviewToHashMap());
            i++;
        }
        return ResponseEntity.ok(new HashMap<>(){
            {put("rating",res.get(0));
            put("people", res.get(1));
            put("reviews", temp);
            put("renter", res.get(3));
            put("info",((RenterInfo) res.get(4)).RenterToHashMap());
            }
        });
    }

}
