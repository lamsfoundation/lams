package org.lamsfoundation.lams.signup.model;

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

import org.lamsfoundation.lams.usermanagement.Organisation;

/**
 * Signup organisation.
 */
@Entity
@Table(name = "lams_signup_organisation")
public class SignupOrganisation {

    /**
     * Link on Lams wiki signup help page.
     */
    public static String SIGNUP_HELP_PAGE = "LAMS+Signup";

    @Id
    @Column(name = "signup_organisation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer signupOrganisationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @Column(name = "add_to_lessons")
    private Boolean addToLessons;

    @Column(name = "add_as_staff")
    private Boolean addAsStaff;

    @Column(name = "add_with_author")
    private Boolean addWithAuthor;

    @Column(name = "add_with_monitor")
    private Boolean addWithMonitor;

    @Column(name = "email_verify")
    private Boolean emailVerify;

    @Column(name = "course_key")
    private String courseKey;

    @Column
    private String blurb;

    @Column(name = "create_date")
    private Date createDate;

    @Column
    private Boolean disabled;

    @Column(name = "login_tab_active")
    private Boolean loginTabActive;

    @Column
    private String context;

    public Integer getSignupOrganisationId() {
	return signupOrganisationId;
    }

    public void setSignupOrganisationId(Integer signupOrganisationId) {
	this.signupOrganisationId = signupOrganisationId;
    }

    public Organisation getOrganisation() {
	return organisation;
    }

    public void setOrganisation(Organisation organisation) {
	this.organisation = organisation;
    }

    public Boolean getAddToLessons() {
	return addToLessons;
    }

    public void setAddToLessons(Boolean addToLessons) {
	this.addToLessons = addToLessons;
    }

    public Boolean getAddAsStaff() {
	return addAsStaff;
    }

    public void setAddAsStaff(Boolean addAsStaff) {
	this.addAsStaff = addAsStaff;
    }

    public Boolean getAddWithAuthor() {
	return addWithAuthor;
    }

    public void setAddWithAuthor(Boolean addWithAuthor) {
	this.addWithAuthor = addWithAuthor;
    }

    public Boolean getAddWithMonitor() {
	return addWithMonitor;
    }

    public void setAddWithMonitor(Boolean addWithMonitor) {
	this.addWithMonitor = addWithMonitor;
    }

    public Boolean getEmailVerify() {
	return emailVerify;
    }

    public void setEmailVerify(Boolean emailValidation) {
	this.emailVerify = emailValidation;
    }

    public String getCourseKey() {
	return courseKey;
    }

    public void setCourseKey(String courseKey) {
	this.courseKey = courseKey;
    }

    public String getBlurb() {
	return blurb;
    }

    public void setBlurb(String blurb) {
	this.blurb = blurb;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public Boolean getDisabled() {
	return disabled;
    }

    public void setDisabled(Boolean disabled) {
	this.disabled = disabled;
    }

    public Boolean getLoginTabActive() {
	return loginTabActive;
    }

    public void setLoginTabActive(Boolean loginTabActive) {
	this.loginTabActive = loginTabActive;
    }

    public String getContext() {
	return context;
    }

    public void setContext(String context) {
	this.context = context;
    }

}
