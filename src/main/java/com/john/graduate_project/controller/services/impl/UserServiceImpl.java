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
    public HashMap<String, Object> login(User user) {
        HashMap<String, Object> result = new HashMap<>();
        String msg = "No user found...";
        UserDto u =null;
        Optional<User> userOptional= userRepository.findById(user.getUsername());
        if (userOptional.isPresent()){
            user.setPassword3(user.getPassword(), userOptional.get().getSalt());
            if (userOptional.get().getPassword().equals(user.getPassword()) && userOptional.get().isEnabled()){
                //TODO split the condition and make it possible to get the vcode
                msg = "Login sucess!";
                u = userOptional.get().userToUserDto();
                u.setPassword(null);
            }
            else{
                msg = "The password is wrong";
            }
        }
        result.put("user",u);
        result.put("msg", msg);
        return result;
    }

    @Override
    public User getUser(String username) {
        Optional<User> user = userRepository.findById(username);
        return user.orElse(null);
    }

    @Override
    public List<Object> alreadylogin(String username) {
        List<Object> result = new ArrayList<>();
        String msg = "No user found...";
        UserDto u =null;
        Optional<User> userOptional= userRepository.findById(username);
        if (userOptional.isPresent()){
            msg = "Welcome back!";
            u = userOptional.get().userToUserDto();
            u.setPassword(null);
        }
        result.add(u);
        result.add(msg);
        return result;
    }


    @Override
    public HashMap<String, Object> registerUser(User user) {
        HashMap<String, Object> result = new HashMap<>();
        String recipientAddress = user.getMail();
        String subject = "Registration Confirmation";
        String message = "You registered successfully. To confirm your registration, please insert the code below : \n";
        String msg ;
        UserDto u = null;
        user.setPassword2(user.getPassword());
        user.setUsername(user.getUsername().toLowerCase());
        Optional<User> optionalUser  = userRepository.findById(user.getUsername());
        if (optionalUser.isPresent()){
            msg = "The username already exist";
        }
        else {

            User user1 = userRepository.save(user);
            if (user1 != null) {
                u = user.userToUserDto();
                VerificationCode code = new VerificationCode(user);
                VerificationCode v = verificationCodeRepository.save(code);
                if (v != null){
                    sendEmail(senderMail,recipientAddress,subject,message, code.getCode());
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
        result.put("msg", msg);
        result.put("user", u);
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
        Optional<VerificationCode> data = verificationCodeRepository.findById(code.getUsername());
        Optional<User> user = userRepository.findById(code.getUsername());
        if( data.isPresent()){
            if (data.get().getCode() == code.getCode()){
                user.get().setEnabled(true);
                userRepository.save(user.get());
                result.put("msg", "The account created successfully");
                result.put("page",1);
                result.put("user",user.get().userToUserDto());
            }
            else {
                if (data.get().getTries() != 0){
                    data.get().setTries(data.get().getTries() - 1);
                    VerificationCode v = verificationCodeRepository.save(data.get());
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
                }
            }
        }
        else {
            result.put("msg", "There is no user with this username");
            result.put("page",0);
        }
        return result;
    }

    @Override
    public List<Object> myProfile(String username, boolean flag) {
        List<Object> result = new ArrayList<>();
        List<Object> cr = new ArrayList<>();
        List<Car> cars = carServices.findByNama(username);
        HashMap<String,Object> map = new HashMap<>();
        int j =0;
        for (Car c :cars){
            List<ReviewForCar> review = reviewCarServices.findByLicense(c.getLicense());
            if (flag){
                c.setOwner(null);
                HashMap<String,Object> r = new HashMap<>();
                int i =0;
                for (ReviewForCar reviewForCar:review) {
                    reviewForCar.setRenter(null);
                    reviewForCar.setCar(null);
                    r.put(String.valueOf(i),reviewForCar);
                    i++;
                }
                HashMap<String,Object> car = new HashMap<>();
                car.put("car",c);
                car.put("review",r);
                map.put(String.valueOf(j),car);
                j++;
            }
            else {
                List<Object> temp = new ArrayList<>();
                temp.add(c);
                temp.add(review);
                cr.add(temp);
            }
        }

        List<ReviewForUser> reviews = reviewUserServices.findReviewsByUsername(username);
        if (flag){
            HashMap<String,Object> revUser =new HashMap<>();
            int k=0;
            for (ReviewForUser reviewForUser : reviews){
                reviewForUser.setOwner(null);
                reviewForUser.setRenter(null);
                revUser.put(String.valueOf(k),reviewForUser);
                k++;
            }
            result.add(map);
            result.add(revUser);
        }
        else {
            result.add(cr);
            result.add(reviews);
        }
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
    public List<Object> profile(String username) {
        List<Object> result = new ArrayList<>();
        Optional<User> optionalUser = userRepository.findById(username);
        optionalUser.ifPresent(user1 -> {
            List<ReviewForUser> reviews = reviewUserServices.findReviewsByUsername(username);
            int count=0;
            double aver=0;
            double sum=0;
            for (ReviewForUser r : reviews) {
                count++;
                sum += r.getStars();
            }
            if (count!=0){
                aver = sum / count;
            }
            result.add(aver);
            result.add(count);
            result.add(reviews);
            result.add(optionalUser.get().userToUserDto());
            Optional<RenterInfo> info = renterInfoIRepository.findById(username);
            info.ifPresent(result::add);
        });
        return result;
    }

    @Override
    public String addInfo(RenterInfo renterInfo, String username) {
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            renterInfo.setRenter(user.get());
            renterInfo.setUsername(username);
            user.get().setInfo(renterInfo);
            userRepository.save(user.get());
            RenterInfo info = renterInfoIRepository.save(renterInfo);
            if (info != null) {
                return "The information saved successfully";
            } else {
                return "The information didn't saved !!";
            }
        }
        else return "something went wrong try again";
    }

    @Override
    public HashMap<String, Object> verifyAccount(UserDto userDto) {
        HashMap<String, Object> result = new HashMap<>();
        Optional<User> optionalUser = userRepository.findById(userDto.getUsername());
        if (optionalUser.isPresent()){
            if (optionalUser.get().getMail().equals(userDto.getMail())){
                Optional<VerificationCode> verificationCode = verificationCodeRepository.findById(userDto.getUsername());
                verificationCode.get().setCode(VerificationCode.generateCode());
                verificationCodeRepository.save(verificationCode.get());
                sendEmail(senderMail,userDto.getMail(),"Verification code for reset password",
                        "To reset your password please insert the code bellow and the new password \n", verificationCode.get().getCode());
                result.put("find",true);
                result.put("msg","Please check your email for the verification code !");
            }
            else {
                result.put("find",false);
                result.put("msg", "The email is incorrect");
            }
        }
        else {
            result.put("find",false);
            result.put("msg", "The username is incorrect");
        }
        return result;
    }

    @Override
    public HashMap<String, Object> createNewPassword(UserDto userDto, VerificationCodeDto verificationCodeDto) {
        HashMap<String, Object> result = new HashMap<>();
        Optional<VerificationCode> code = verificationCodeRepository.findById(userDto.getUsername());
        if (code.isPresent() && code.get().getCode() == verificationCodeDto.getCode()){
            if (userDto.getPassword().equals(userDto.getConfPassword())){
                Optional<User> user = userRepository.findById(userDto.getUsername());
                user.get().setPassword2(userDto.getPassword());
                User user1 = userRepository.save(user.get());
                if (user1 != null){
                    result.put("user", true);
                    result.put("msg", "The password change successfully!!");
                }
                else {
                    result.put("user", false);
                    result.put("msg", "Something went wrong, please try again");
                }
            }
            else {
                result.put("user", false);
                result.put("msg", "The password and the confirm password is different");
            }
        }
        else {
            result.put("user", false);
            result.put("msg", "The verification code is incorrect");
        }
        return result;
    }


}
