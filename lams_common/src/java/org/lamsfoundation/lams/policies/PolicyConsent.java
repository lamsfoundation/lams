package org.lamsfoundation.lams.policies;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.lamsfoundation.lams.usermanagement.User;

@Entity
@Table(name = "lams_policy_consent")
public class PolicyConsent {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "date_agreed_on")
    private Date dateAgreedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_uid")
    private Policy policy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** default constructor */
    public PolicyConsent() {
	dateAgreedOn = new Date(); // default value is set to when the object is created
    }

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
