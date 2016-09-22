package org.lamsfoundation.lams.integration;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @hibernate.class table="lams_ext_user_userid_map"
 */
public class ExtUserUseridMap implements Serializable {

    private static final long serialVersionUID = 1755818193730728064L;

    /** identifier field */
    private Integer sid;

    /** persistent field */
    private String extUsername;

    /** persistent field */
    private User user;

    /** persistent field */
    private ExtServerOrgMap extServerOrgMap;
    
    /** id of gradebook item in LTI tool consumer */
    private String tcGradebookId;

    /** full constructor */
    public ExtUserUseridMap(String extUsername, User user, ExtServerOrgMap extServerOrgMap) {
	this.extUsername = extUsername;
	this.user = user;
	this.extServerOrgMap = extServerOrgMap;
    }

    /** default constructor */
    public ExtUserUseridMap() {
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Integer" column="sid"
     *
     */
    public Integer getSid() {
	return this.sid;
    }

    public void setSid(Integer sid) {
	this.sid = sid;
    }

    /**
     * @hibernate.property column="external_username" length="250" not-null="true"
     */
    public String getExtUsername() {
	return this.extUsername;
    }

    public void setExtUsername(String extUsername) {
	this.extUsername = extUsername;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="user_id" lazy="true"
     *
     */
    public User getUser() {
	return this.user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="ext_server_org_map_id"
     *
     */
    public ExtServerOrgMap getExtServerOrgMap() {
	return this.extServerOrgMap;
    }

    public void setExtServerOrgMap(ExtServerOrgMap extServerOrgMap) {
	this.extServerOrgMap = extServerOrgMap;
    }
    
    /**
     * @hibernate.property column="tc_gradebook_id" length="250" 
     */
    public String getTcGradebookId() {
	return this.tcGradebookId;
    }

    public void setTcGradebookId(String tcGradebookId) {
	this.tcGradebookId = tcGradebookId;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("sid", getSid()).append("extUsername", getExtUsername()).toString();
    }

}
