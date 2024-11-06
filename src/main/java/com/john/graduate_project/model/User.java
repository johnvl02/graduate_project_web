package com.john.graduate_project.model;

import com.john.graduate_project.dto.UserDto;
import com.john.graduate_project.model.types.UserType;
import com.john.graduate_project.security.EncryptionUtil;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "mail")
    private String mail;
    @Column(name = "phone")
    private long phone;
    @Column(name = "age")
    private int age;
    @Column(name = "role")
    private UserType role;
    @Column(name = "salt")
    private String salt;
    @Column(name = "enabled")
    private boolean enabled = false;
    @Column(name = "expire_date")
    private LocalDate expireDate = LocalDate.now().plusYears(1);

    @OneToOne(cascade = CascadeType.ALL, optional = true, mappedBy = "users")
    @PrimaryKeyJoinColumn
    private VerificationCode code;
    @OneToOne(cascade = CascadeType.ALL, optional = true,mappedBy = "renter")
    @PrimaryKeyJoinColumn
    private RenterInfo info;

    public VerificationCode getCode() {
        return code;
    }

    public void setCode(VerificationCode code) {
        this.code = code;
    }

    public RenterInfo getInfo() {
        return info;
    }

    public void setInfo(RenterInfo info) {
        this.info = info;
    }

    public User(String username, String password, String firstName, String lastName, String mail, long phone, int age, UserType role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.phone = phone;
        this.age = age;
        this.role = role;
    }

    public User(String username) {
        this.username = username;
    }

    public User() {
    }

    public UserDto userToUserDto(){
        return new UserDto(getUsername(), null, null, getFirstName(),
                getLastName(),getMail(), getPhone(), getAge(), getRole());
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }


    public void setPassword3(String password, String salt) {
        this.password = EncryptionUtil.encruptPasswoed(password,salt);
        this.salt = salt;
    }

    public void setPassword2(String password) {
        List<String> l =  EncryptionUtil.encruptPasswoed(password);
        this.password = l.get(0);
        this.salt = l.get(1);
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public UserType getRole() {
        return role;
    }

    public void setRole(UserType role) {
        this.role = role;
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

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }
}
