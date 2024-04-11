package com.john.graduate_project.controller.services;

import com.john.graduate_project.dto.VerificationCodeDto;
import com.john.graduate_project.model.RenterInfo;
import com.john.graduate_project.model.User;
import com.john.graduate_project.model.VerificationCode;

import java.util.HashMap;
import java.util.List;


public interface UserService {
    List<Object> login(User user);
//    User newUser();
    List<Object> registerUser(User user);
    HashMap<String,Object> verificationAccount(VerificationCode code);
    List<Object> myProfile(User user);
    String updateUser(User temp, User user);
    String addInfo(RenterInfo renterInfo, User user);

}
