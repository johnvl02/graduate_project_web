package com.john.graduate_project.dto;

import com.john.graduate_project.model.VerificationCode;

public class VerificationCodeDto {
    String username;
    int code;

    public VerificationCodeDto(String username, int code) {
        this.username = username;
        this.code = code;
    }

    public VerificationCodeDto(String username) {
        this.username = username;
    }

    public VerificationCodeDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public VerificationCode VCDtoToVC(){
        VerificationCode code1 = new VerificationCode();
        code1.setUser_username(getUsername());
        code1.setCode(getCode());
        return code1;
    }
}
