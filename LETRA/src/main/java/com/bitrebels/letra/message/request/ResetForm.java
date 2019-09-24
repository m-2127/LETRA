package com.bitrebels.letra.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ResetForm {
    @NotBlank
    private String password;

    private String token;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
