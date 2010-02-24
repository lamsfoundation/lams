package org.lamsfoundation.lams.tool.signup.model;

import java.util.Date;

import org.lamsfoundation.lams.usermanagement.Organisation;

public class SignupOrganisation {

    private Integer signupOrganisationId;
    private Organisation organisation;
    private Boolean addToLessons;
    private Boolean addAsStaff;
    private Boolean addWithAuthor;
    private Boolean addWithMonitor;
    private String courseKey;
    private String blurb;
    private Date createDate;
    private Boolean disabled;
    private String context;

    public SignupOrganisation() {

    }

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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

}
