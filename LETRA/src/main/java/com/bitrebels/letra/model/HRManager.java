package com.bitrebels.letra.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class HRManager {

    @Id
    private Long hrmId ;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "hrManager")
    private Set<Progress> progressSet;

    @OneToMany(mappedBy = "hrManager")
    private Set<User> userSet;

    public HRManager(Long hrmId) {
        this.hrmId = hrmId;
    }

    public Long getHrmId() {
        return hrmId;
    }

    public void setHrmId(Long hrmId) {
        this.hrmId = hrmId;
    }

    public Set<Progress> getProgressSet() {
        return progressSet;
    }

    public void setProgressSet(Set<Progress> progressSet) {
        this.progressSet = progressSet;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setUserSet(Set<User> userSet) {
        this.userSet = userSet;
    }


}
