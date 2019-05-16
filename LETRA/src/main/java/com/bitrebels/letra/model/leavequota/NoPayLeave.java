package com.bitrebels.letra.model.leavequota;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("NoPay")
public class NoPayLeave extends LeaveQuota {

}
