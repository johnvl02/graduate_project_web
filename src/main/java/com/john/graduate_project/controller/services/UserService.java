package com.john.graduate_project.controller.services;

import com.john.graduate_project.dto.UserDto;
import com.john.graduate_project.dto.VerificationCodeDto;
import com.john.graduate_project.model.RenterInfo;
import com.john.graduate_project.model.User;
import com.john.graduate_project.model.VerificationCode;

import java.util.HashMap;
import java.util.List;


public interface UserService {
    HashMap<String, Object> login(User user);
    User getUser(String username);
    List<Object> alreadylogin(String username);
    HashMap<String, Object> registerUser(User user);
    HashMap<String,Object> verificationAccount(VerificationCode code);
    List<Object> myProfile(String username, boolean flag);
    String updateUser(User temp, User user);
    List<Object> profile(String username);
    String addInfo(RenterInfo renterInfo, String username);
    HashMap<String, Object> verifyAccount(UserDto userDto);
    HashMap<String, Object> createNewPassword(UserDto userDto, VerificationCodeDto verificationCodeDto);
}
