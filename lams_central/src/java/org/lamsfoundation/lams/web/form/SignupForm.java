package org.lamsfoundation.lams.web.form;

public class SignupForm {

    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String confirmEmail;
    private String courseKey;
    private Boolean submitted;
    private String context;
    private String usernameTab2;
    private String passwordTab2;
    private String courseKeyTab2;
    private String country;

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getConfirmPassword() {
	return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
	this.confirmPassword = confirmPassword;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getConfirmEmail() {
	return confirmEmail;
    }

    public void setConfirmEmail(String confirmEmail) {
	this.confirmEmail = confirmEmail;
    }

    public String getCourseKey() {
	return courseKey;
    }

    public void setCourseKey(String courseKey) {
	this.courseKey = courseKey;
    }

    public Boolean getSubmitted() {
	return submitted;
    }

    public void setSubmitted(Boolean submitted) {
	this.submitted = submitted;
    }

    public String getContext() {
	return context;
    }

    public void setContext(String context) {
	this.context = context;
    }

    public String getUsernameTab2() {
	return usernameTab2;
    }

    public void setUsernameTab2(String usernameTab2) {
	this.usernameTab2 = usernameTab2;
    }

    public String getPasswordTab2() {
	return passwordTab2;
    }

    public void setPasswordTab2(String passwordTab2) {
	this.passwordTab2 = passwordTab2;
    }

    public String getCourseKeyTab2() {
	return courseKeyTab2;
    }

    public void setCourseKeyTab2(String courseKeyTab2) {
	this.courseKeyTab2 = courseKeyTab2;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }
}