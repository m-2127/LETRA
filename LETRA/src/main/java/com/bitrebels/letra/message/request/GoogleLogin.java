package com.bitrebels.letra.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class GoogleLogin {

    @NotBlank
    @Size(min=3, max = 60)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
