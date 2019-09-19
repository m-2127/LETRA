package com.bitrebels.letra.message.response;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String username;
	private String name;
	private Collection<? extends GrantedAuthority> authorities;

	public JwtResponse(String accessToken, String username, Collection<? extends GrantedAuthority> authorities,String name) {
		this.token = accessToken;
		this.username = username;
		this.authorities = authorities;
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String accessToken) {
		this.token = accessToken;
	}

	public String getType() {
		return type;
	}

	public void setType(String tokenType) {
		this.type = tokenType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}