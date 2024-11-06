package com.john.graduate_project.model;


import jakarta.persistence.*;

import java.security.SecureRandom;

@Entity
@Table(name = "verificationcode")
public class VerificationCode {

    @Id
    @Column(name = "username")
    private String username;
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId("username")
    @JoinColumn(insertable = false, updatable = false, name = "username")
    private User users;

    @Column(name = "code")
    private int code = getCode();
    @Column(name = "tries")
    private int tries = 3;
    @Transient
    private static final SecureRandom random = new SecureRandom();
    @Transient
    private static final int upperbound = 999999;
    @Transient
    private static final int lowerbound = 100000;


    public VerificationCode(String username, User users, int code) {
        this.username = username;
        this.users = users;
        this.code = code;
    }

    public VerificationCode(String username, int code) {
        this.users = new User(username);
        this.username = username;
        this.code = code;
    }

    public VerificationCode(User users) {
        this.username = users.getUsername();
        this.users = users;
        this.code = generateCode();
        this.tries = 3;
    }

    public VerificationCode() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User user) {
        this.users = user;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static int generateCode(){
        return random.nextInt(lowerbound,upperbound);
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }
}
