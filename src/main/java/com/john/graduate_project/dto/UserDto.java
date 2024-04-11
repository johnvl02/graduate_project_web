package com.john.graduate_project.dto;

import com.john.graduate_project.model.User;
import com.john.graduate_project.model.types.UserType;

public class UserDto {
    private String username;
    private String password;
    private String confPassword;
    private String firstName;
    private String lastName;
    private String mail;
    private long phone;
    private int age;
    private UserType role = UserType.User;


    public UserDto(String username, String password, String confPassword, String firstName, String lastName, String mail, long phone, int age, UserType role) {
        this.username = username;
        this.password = password;
        this.confPassword = confPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.phone = phone;
        this.age = age;
        this.role = role;
    }

    public UserDto() {
    }

    public User userdtoToUSer(){
        return new User(getUsername(), getPassword(), getFirstName(),
                getLastName(), getMail(), getPhone(), getAge(), getRole());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public UserType getRole() {
        return role;
    }

    public void setRole(UserType role) {
        this.role = role;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getConfPassword() {
        return confPassword;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }
}
