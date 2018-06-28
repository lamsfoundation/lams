package org.lamsfoundation.lams.policies;

import java.util.Date;

import org.lamsfoundation.lams.usermanagement.User;

public class PolicyConsent {
    
    private Long uid;
    
    private Date dateAgreedOn;

    private Policy policy;
    
    /** persistent field */
    private User user;
    
    /** default constructor */
    public PolicyConsent() {
	dateAgreedOn = new Date(); // default value is set to when the object is created
    }
    
    /**
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }
    
    public Date getDateAgreedOn() {
	return dateAgreedOn;
    }

    /* If dateAgreedOn is null, then it will default to the current date/time */
    public void setDateAgreedOn(Date dateAgreedOn) {
	this.dateAgreedOn = dateAgreedOn != null ? dateAgreedOn : new Date();
    }

    /**
     */
    public Policy getPolicy() {
	return policy;
    }

    public void setPolicy(Policy policy) {
	this.policy = policy;
    }
    
    public User getUser() {
	return this.user;
    }

    public void setUser(User user) {
	this.user = user;
    }

}
