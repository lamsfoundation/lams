package org.lamsfoundation.lams.integration;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.User;


/** 
 *        @hibernate.class
 *         table="lams_ext_user_userid_map"
 *     
*/
public class ExtUserUseridMap implements Serializable {

	private static final long serialVersionUID = 1755818193730728064L;

	/** identifier field */
    private Integer sid;

    /** persistent field */
    private String foreignUsername;

    /** persistent field */
    private User user;

    /** persistent field */
    private ExtServerOrgMap extServerOrgMap;

    /** full constructor */
    public ExtUserUseridMap(String foreignUsername, User user, org.lamsfoundation.lams.integration.ExtServerOrgMap extServerOrgMap) {
        this.foreignUsername = foreignUsername;
        this.user = user;
        this.extServerOrgMap = extServerOrgMap;
    }

    /** default constructor */
    public ExtUserUseridMap() {
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
     *             column="foreign_username"
     *             length="250"
     *             not-null="true"
     *         
     */
    public String getForeignUsername() {
        return this.foreignUsername;
    }

    public void setForeignUsername(String foreignUsername) {
        this.foreignUsername = foreignUsername;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="user_id"         
     *         
     */
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="ext_server_org_map_id"         
     *         
     */
    public org.lamsfoundation.lams.integration.ExtServerOrgMap getExtServerOrgMap() {
        return this.extServerOrgMap;
    }

    public void setExtServerOrgMap(org.lamsfoundation.lams.integration.ExtServerOrgMap extServerOrgMap) {
        this.extServerOrgMap = extServerOrgMap;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("sid", getSid())
            .append("foreignUsername", getForeignUsername())
            .toString();
    }

}
