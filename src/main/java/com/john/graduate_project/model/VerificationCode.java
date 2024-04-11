package com.john.graduate_project.model;


import jakarta.persistence.*;

import java.security.SecureRandom;

@Entity
@Table(name = "verificationcode")
public class VerificationCode {

    @Id
    @Column(name = "user_username")
    private String user_username;
    @OneToOne
    //@MapsId
    //@JoinColumn(name = "user_username")
    @JoinColumn(insertable = false, updatable = false, name = "user_username")
    private User user;

    @Column(name = "code")
    private int code = getCode();
    @Column(name = "tries")
    private int tries = 3;
    @Transient
    private static final SecureRandom random = new SecureRandom();
    @Transient
    private static final int upperbound = 999999;


    public VerificationCode(String user_username, User user, int code) {
        this.user_username = user_username;
        this.user = user;
        this.code = code;
    }

    public VerificationCode(User user) {
        this.user_username = user.getUsername();
        this.user = user;
        this.code = generateCode();
    }

    public VerificationCode(String user_username) {
        this.user_username = user_username;
        this.code = generateCode();
        this.tries = 3;
    }

    public VerificationCode() {
    }

    public String getUser_username() {
        return user_username;
    }

    public void setUser_username(String username) {
        this.user_username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static int generateCode(){
        return random.nextInt(upperbound);
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }
}
