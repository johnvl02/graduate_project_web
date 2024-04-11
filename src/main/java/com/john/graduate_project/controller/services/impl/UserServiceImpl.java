package com.john.graduate_project.controller.services.impl;

import com.john.graduate_project.controller.services.UserService;
import com.john.graduate_project.dto.UserDto;
import com.john.graduate_project.dto.VerificationCodeDto;
import com.john.graduate_project.model.*;
import com.john.graduate_project.repository.RenterInfoIRepository;
import com.john.graduate_project.repository.UserRepository;
import com.john.graduate_project.repository.VerificationCodeRepository;
import com.john.graduate_project.repository.service.CarServices;
import com.john.graduate_project.repository.service.ReviewCarServices;
import com.john.graduate_project.repository.service.ReviewUserServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private CarServices carServices;
    private ReviewCarServices reviewCarServices;
    private ReviewUserServices reviewUserServices;
    private RenterInfoIRepository renterInfoIRepository;
    private VerificationCodeRepository verificationCodeRepository;
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String senderMail;

    public UserServiceImpl(UserRepository userRepository, CarServices carServices, ReviewCarServices reviewCarServices, ReviewUserServices reviewUserServices,
                           RenterInfoIRepository renterInfoIRepository, VerificationCodeRepository verificationCodeRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.carServices = carServices;
        this.reviewCarServices = reviewCarServices;
        this.reviewUserServices = reviewUserServices;
        this.renterInfoIRepository = renterInfoIRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.mailSender = mailSender;
    }

    @Override
    public List<Object> login(User user) {
        List<Object> result = new ArrayList<>();
        String msg = "No user found...";
        User u =null;
        Optional<User> userOptional= userRepository.findById(user.getUsername());
        if (userOptional.isPresent()){
            user.setPassword3(user.getPassword(), userOptional.get().getSalt());
            if (userOptional.get().getPassword().equals(user.getPassword()) && userOptional.get().isEnabled()){
                msg = "Login sucess!";
                u = userOptional.get();
                u.setPassword2(null);
                u.setSalt(null);
            }
            else{
                msg = "The password is wrong";
            }
        }
        result.add(u.userToUserDto());
        result.add(msg);
        return result;
    }

   /* @Override
    public User newUser() {
        return new User();
    }*/


    @Override
    public List<Object> registerUser(User user) {
        List<Object> result = new ArrayList<>();
        String recipientAddress = user.getMail();
        String subject = "Registration Confirmation";
        String message = "You registered successfully. To confirm your registration, please insert the code below : \n";
        String msg ;
        User u = null;
        user.setPassword2(user.getPassword());
        user.setUsername(user.getUsername().toLowerCase());
        Optional<User> optionalUser  = userRepository.findById(user.getUsername());
        if (optionalUser.isPresent()){
            msg = "The username already exist";
        }
        else {
            User user1 = userRepository.save(user);
            if (user1 != null) {
                u = user;
                VerificationCode code = new VerificationCode(user.getUsername());
                //code.setUser(user);
                /*code.setUser_username(user.getUsername());

                code.setCode(code.generateCode());*/
                VerificationCode v = verificationCodeRepository.save(code);
                if (v != null){
                    sendEmail(senderMail,recipientAddress,subject,message, code.getCode());
                    /*SimpleMailMessage email = new SimpleMailMessage();
                    email.setFrom(senderMail);
                    email.setTo(recipientAddress);
                    email.setSubject(subject);
                    email.setText(message + code.getCode());
                    mailSender.send(email);*/
                    msg = "The user created successfully";
                }
                else {
                    msg = "The user created successfully, but the verification code doesn't saved";
                }
            }
            else {
                msg = "The user didn't created !!";
            }
        }
        result.add(msg);
        result.add(u.userToUserDto());
        return result;
    }

    private void sendEmail(String sender, String recipient, String subject, String message, int code){
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(sender);
        email.setTo(recipient);
        email.setSubject(subject);
        email.setText(message + code);
        mailSender.send(email);
    }

    @Override
    public HashMap<String,Object> verificationAccount(VerificationCode code) {
        HashMap<String,Object> result = new HashMap<>();
        Optional<VerificationCode> data =verificationCodeRepository.findById(code.getUser_username());
        Optional<User> user = userRepository.findById(code.getUser_username());
        if( data.isPresent()){
            if (data.get().getCode() == code.getCode()){
                user.get().setEnabled(true);
                userRepository.save(user.get());
                result.put("msg", "All good");//----------------------------------------------------------------------------------------------------------------
                result.put("page",1);
                result.put("user",user.get().userToUserDto());
            }
            else {
                if (data.get().getTries() != 0){
                    data.get().setTries(data.get().getTries() - 1);
                    VerificationCode v = verificationCodeRepository.save(data.get());
                    //todo
                    result.put("msg", "Wrong code, try again");
                    result.put("page",2);
                    result.put("vCode",new VerificationCodeDto(user.get().getUsername()));
                }
                else {
                    data.get().setCode(VerificationCode.generateCode());
                    data.get().setTries(3);
                    VerificationCode v = verificationCodeRepository.save(data.get());
                    if (v != null) {
                        sendEmail(senderMail, user.get().getMail(), "New verification code",
                                "You enter too many times wrong code. Here a new code :\n", v.getCode());
                        result.put("msg", "Too many wrong code, enter the new code from your email!!");
                        result.put("page",3);
                        result.put("vCode",new VerificationCodeDto(user.get().getUsername()));
                    }
                    else {
                        //todo
                    }
                }
            }
        }
        else {
            result.put("msg", "There is no user with this username");
            result.put("page",0);
        }
        result.put("code", code);
        return result;
    }

    @Override
    public List<Object> myProfile(User user) {
        List<Object> result = new ArrayList<>();
        List<Object> cr = new ArrayList<>();
        List<Car> cars = carServices.findByNama(user.getUsername());
        for (Car c :cars){
            List<ReviewForCar> review = reviewCarServices.findByLicence(c.getLicence());
            List<Object> temp = new ArrayList<>();
            temp.add(c);
            temp.add(review);
            cr.add(temp);
        }
        result.add(cr);
        List<ReviewForUser> reviews = reviewUserServices.findReviewsByUsername(user.getUsername());
        result.add(reviews);
        return result;
    }

    @Override
    public String updateUser(User temp, User user) {
        temp.setUsername(user.getUsername());
        temp.setPassword(userRepository.findById(user.getUsername()).get().getPassword());
        temp.setSalt(userRepository.findById(user.getUsername()).get().getSalt());
        temp.setRole(user.getRole());
        User user1 = userRepository.save(temp);
        if (user1 != null) {
            return "Your profile updated successfully";
        }
        else {
            return "Your profile didn't updated !!!";
        }
    }

    @Override
    public String addInfo(RenterInfo renterInfo, User user) {
        renterInfo.setRenter_username(user.getUsername());
        RenterInfo info = renterInfoIRepository.save(renterInfo);
        if (info != null) {
           return "The information saved successfully";
        }
        else {
            return  "The information didn't saved !!";
        }
    }


}
