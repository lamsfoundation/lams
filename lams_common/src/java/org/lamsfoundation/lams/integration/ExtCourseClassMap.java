package org.lamsfoundation.lams.integration;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.Organisation;


/** 
 *        @hibernate.class
 *         table="lams_ext_course_class_map"
 *     
*/
public class ExtCourseClassMap implements Serializable {

	private static final long serialVersionUID = -6179393464356966543L;

	/** identifier field */
    private Integer sid;

    /** persistent field */
    private String courseid;

    /** persistent field */
    private ExtServerOrgMap extServerOrgMap;

    /** persistent field */
    private Organisation organisation;

    /** full constructor */
    public ExtCourseClassMap(String courseid, ExtServerOrgMap extServerOrgMap, Organisation organisation) {
        this.courseid = courseid;
        this.extServerOrgMap = extServerOrgMap;
        this.organisation = organisation;
    }

    /** default constructor */
    public ExtCourseClassMap() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Integer"
     *             column="sid"
     *         
     */
    public Integer getSid() {
        return this.sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    /** 
     *            @hibernate.property
     *             column="courseid"
     *             length="255"
     *             not-null="true"
     *         
     */
    public String getCourseid() {
        return this.courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="ext_server_org_map_id"         
     *         
     */
    public ExtServerOrgMap getExtServerOrgMap() {
        return this.extServerOrgMap;
    }

    public void setExtServerOrgMap(ExtServerOrgMap extServerOrgMap) {
        this.extServerOrgMap = extServerOrgMap;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="classid"         
     *         
     */
    public Organisation getOrganisation() {
        return this.organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("sid", getSid())
            .append("courseid", getCourseid())
            .toString();
    }

}
