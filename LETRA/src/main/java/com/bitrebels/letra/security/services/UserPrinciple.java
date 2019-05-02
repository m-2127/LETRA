package com.bitrebels.letra.security.services;

import com.bitrebels.letra.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrinciple implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;

    private String name;

    private String email;

    @JsonIgnore
    private String password;
    
    private String mobilenumber;
    
    private String gender;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(Long id, String name, String email, String password,
    				String mobilenumber, String gender,
    				Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobilenumber = mobilenumber;
        this.gender = gender;
        this.authorities = authorities;
    }

    public static UserPrinciple build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new UserPrinciple(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getMobilenumber(),
                user.getGender(),
                authorities
        );
    }

	public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    
    public String getMobilenumber() {
    	return mobilenumber;
    }
    
    public String getGender() {
    	return gender;
    }
    
    //email is used as the username and hence returning email
    @Override
    public String getUsername() { 
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }
}