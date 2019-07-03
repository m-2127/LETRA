 package com.bitrebels.letra.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NaturalId;

import com.bitrebels.letra.model.leavequota.LeaveQuota;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "mobilenumber"
        }),
        @UniqueConstraint(columnNames = {
            "email"
        })
})
public class User{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="user_id")
    private Long id;

    @NotBlank
    @Size(min=3, max = 50)
    private String name;

    @NaturalId
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min=6, max = 100)
    private String password;
    
    @NotBlank
    @Size(min = 3, max = 20)
    private String mobilenumber;
    

	@NotBlank
    @Size(min = 4, max = 6)
    private String gender;
	
	@Column(name = "reset_token")
	private String resetToken;

    @ManyToMany(fetch = FetchType.LAZY, cascade={CascadeType.ALL})
    @JoinTable(name = "user_roles", 
    	joinColumns = @JoinColumn(name = "user_id"), 
    	inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    
//    @OneToOne
//	@JoinColumn(name="user_id")
//	private LeaveQuota leaveQuota;

	public User() {}

    public User(String name,String email, String password, String mobilenumber, String gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobilenumber = mobilenumber;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
    
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
//    public LeaveQuota getLeaveQuota() {
//    	return leaveQuota;
//    }
//
//    public void setLeaveQuota(LeaveQuota leaveQuota) {
//    	this.leaveQuota = leaveQuota;
//    }

    public String getResetToken() {
    	return resetToken;
    }

    public void setResetToken(String resetToken) {
    	this.resetToken = resetToken;
    }
}
