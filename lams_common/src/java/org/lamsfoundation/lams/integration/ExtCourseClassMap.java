package org.lamsfoundation.lams.integration;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.Organisation;

@Entity
@Table(name = "lams_ext_course_class_map")
public class ExtCourseClassMap implements Serializable {
    private static final long serialVersionUID = -6179393464356966543L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sid;

    @Column
    private String courseid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ext_server_org_map_id")
    private ExtServer extServer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classid")
    private Organisation organisation;

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