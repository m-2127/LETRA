 package com.bitrebels.letra.model;

import com.bitrebels.letra.model.Firebase.Topic;
import com.bitrebels.letra.model.leavequota.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private long id;

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

    private String deviceToken;

    @ManyToMany(fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    @JoinTable(name = "user_roles", 
    	joinColumns = @JoinColumn(name = "user_id"), 
    	inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "hrManagerId")
    private HRManager hrManager;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL )
    private Set<LeaveQuota> leaveQuotas;

    @ManyToMany(mappedBy = "users" , cascade = CascadeType.ALL )
    private Set<Topic> topics;

//    @OneToOne(mappedBy = "user")
//    private ReportingManager rm;

	public User() {}

    public User(String name,String email, String password, String mobilenumber, String gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobilenumber = mobilenumber;
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public HRManager getHrManager() {
        return hrManager;
    }

    public void setHrManager(HRManager hrManager) {
        this.hrManager = hrManager;
    }

    public String getResetToken() {
    	return resetToken;
    }

    public void setResetToken(String resetToken) {
    	this.resetToken = resetToken;
    }

    public Set<LeaveQuota> getLeaveQuotas() {
        return leaveQuotas;
    }

    public void setLeaveQuotas(Set<LeaveQuota> leaveQuotas) {
        this.leaveQuotas = leaveQuotas;
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

//    public ReportingManager getRm() {
//        return rm;
//    }
//
//    public void setRm(ReportingManager rm) {
//        this.rm = rm;
//    }
}

