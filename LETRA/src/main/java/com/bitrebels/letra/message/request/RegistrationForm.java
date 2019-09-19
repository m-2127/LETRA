package com.bitrebels.letra.message.request;

import javax.validation.constraints.*;

public class RegistrationForm {
    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    

    @NotBlank
    @Size(min = 3, max = 20)
    private String mobilenumber;
    
    @NotBlank
    @Size(min = 4, max = 6)
    private String gender;

    //hrm's devicetoken
    private String deviceToken;

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    public String getMobilenumber() {
    	return mobilenumber;
    }
    
    public void setMobilenumber(String mobilenumber) {
    	this.mobilenumber = mobilenumber;
    }
    
    public String getGender() {
    	return gender;
    }
    
    public void setGender(String gender) {
    	this.gender = gender;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}