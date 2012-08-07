package org.lamsfoundation.lams.signup.model;

import java.util.Date;

import org.lamsfoundation.lams.usermanagement.Organisation;

/**
 * SignupOrganisation. 
 * 
 * @hibernate.class table="lams_signup_organisation"
 */
public class SignupOrganisation {
    
    /**
     * Link on Lams wiki signup help page.
     */
    public static String SIGNUP_HELP_PAGE = "LAMS+Signup";

    /** identifier field */
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

    /**
     * @hibernate.id generator-class="native" type="java.lang.Integer" column="signup_organisation_id"
     */
    public Integer getSignupOrganisationId() {
	return signupOrganisationId;
    }

    public void setSignupOrganisationId(Integer signupOrganisationId) {
	this.signupOrganisationId = signupOrganisationId;
    }

    /**
     * @hibernate.many-to-one not-null="true" cascade="delete" lazy="false"
     * @hibernate.column name="organisation_id"
     */
    public Organisation getOrganisation() {
	return organisation;
    }

    public void setOrganisation(Organisation organisation) {
	this.organisation = organisation;
    }

    /**
     * @hibernate.property column="add_to_lessons" length="1"
     */
    public Boolean getAddToLessons() {
	return addToLessons;
    }

    public void setAddToLessons(Boolean addToLessons) {
	this.addToLessons = addToLessons;
    }

    /**
     * @hibernate.property column="add_as_staff" length="1"
     */
    public Boolean getAddAsStaff() {
	return addAsStaff;
    }

    public void setAddAsStaff(Boolean addAsStaff) {
	this.addAsStaff = addAsStaff;
    }

    /**
     * @hibernate.property column="add_with_author" length="1"
     */
    public Boolean getAddWithAuthor() {
        return addWithAuthor;
    }

    public void setAddWithAuthor(Boolean addWithAuthor) {
        this.addWithAuthor = addWithAuthor;
    }

    /**
     * @hibernate.property column="add_with_monitor" length="1"
     */
    public Boolean getAddWithMonitor() {
        return addWithMonitor;
    }

    public void setAddWithMonitor(Boolean addWithMonitor) {
        this.addWithMonitor = addWithMonitor;
    }

    /**
     * @hibernate.property column="course_key" length="255"
     */
    public String getCourseKey() {
	return courseKey;
    }

    public void setCourseKey(String courseKey) {
	this.courseKey = courseKey;
    }

    /**
     * @hibernate.property column="blurb" length="2147483647"
     */
    public String getBlurb() {
	return blurb;
    }

    public void setBlurb(String blurb) {
	this.blurb = blurb;
    }

    /**
     * @hibernate.property column="create_date" length="19" not-null="true"
     */
    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     * @hibernate.property column="disabled" length="1"
     */
    public Boolean getDisabled() {
	return disabled;
    }

    public void setDisabled(Boolean disabled) {
	this.disabled = disabled;
    }

    /**
     * @hibernate.property column="context" length="255"
     */
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

}
