package org.lamsfoundation.lams.integration;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.Organisation;

public class ExtCourseClassMap implements Serializable {

    private static final long serialVersionUID = -6179393464356966543L;

    /** identifier field */
    private Integer sid;

    /** persistent field */
    private String courseid;

    /** persistent field */
    private ExtServer extServer;

    /** persistent field */
    private Organisation organisation;

    /** full constructor */
    public ExtCourseClassMap(String courseid, ExtServer extServer, Organisation organisation) {
	this.courseid = courseid;
	this.extServer = extServer;
	this.organisation = organisation;
    }

    /** default constructor */
    public ExtCourseClassMap() {
    }

    public Integer getSid() {
	return this.sid;
    }

    public void setSid(Integer sid) {
	this.sid = sid;
    }

    public String getCourseid() {
	return this.courseid;
    }

    public void setCourseid(String courseid) {
	this.courseid = courseid;
    }

    public ExtServer getExtServer() {
	return this.extServer;
    }

    public void setExtServer(ExtServer extServer) {
	this.extServer = extServer;
    }

    public Organisation getOrganisation() {
	return this.organisation;
    }

    public void setOrganisation(Organisation organisation) {
	this.organisation = organisation;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("sid", getSid()).append("courseid", getCourseid()).toString();
    }

}
