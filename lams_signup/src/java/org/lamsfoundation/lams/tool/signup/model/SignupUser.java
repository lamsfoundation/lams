package org.lamsfoundation.lams.tool.signup.model;

import java.util.Date;

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;

public class SignupUser {

	private Integer signupUserId;
	private User user;
	private Organisation organisation;
	private Date signupDate;

	public SignupUser() {

	}

	public Integer getSignupUserId() {
		return signupUserId;
	}

	public void setSignupUserId(Integer signupUserId) {
		this.signupUserId = signupUserId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	public Date getSignupDate() {
		return signupDate;
	}

	public void setSignupDate(Date signupDate) {
		this.signupDate = signupDate;
	}

}
