package org.lamsfoundation.lams.integration;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.Organisation;


/** 
 *        @hibernate.class
 *         table="lams_ext_server_org_map"
 *     
*/
public class ExtServerOrgMap implements Serializable {

	private static final long serialVersionUID = 337894825609071182L;

	/** identifier field */
    private Integer sid;

    /** persistent field */
    private String serverid;

    /** persistent field */
    private String serverkey;

    /** persistent field */
    private String servername;

    /** nullable persistent field */
    private String serverdesc;

    /** persistent field */
    private String prefix;

    /** persistent field */
    private String userinfoUrl;

    /** persistent field */
    private String timeoutUrl;

    /** persistent field */
    private Boolean disabled;

    /** persistent field */
    private Organisation organisation;

    /** persistent field */
    private Set extCourseClassMaps;

    /** persistent field */
    private Set extUserUseridMaps;

    /** full constructor */
    public ExtServerOrgMap(String serverid, String serverkey, String servername, String serverdesc, String prefix, String userinfoUrl, String timeoutUrl, Boolean disabled, Organisation organisation, Set extCourseClassMaps, Set extUserUseridMaps) {
        this.serverid = serverid;
        this.serverkey = serverkey;
        this.servername = servername;
        this.serverdesc = serverdesc;
        this.prefix = prefix;
        this.userinfoUrl = userinfoUrl;
        this.timeoutUrl = timeoutUrl;
        this.disabled = disabled;
        this.organisation = organisation;
        this.extCourseClassMaps = extCourseClassMaps;
        this.extUserUseridMaps = extUserUseridMaps;
    }

    /** default constructor */
    public ExtServerOrgMap() {
    }

    /** minimal constructor */
    public ExtServerOrgMap(String serverid, String serverkey, String servername, String prefix, String userinfoUrl, String timeoutUrl, Boolean disabled, Organisation organisation, Set extCourseClassMaps, Set extUserUseridMaps) {
        this.serverid = serverid;
        this.serverkey = serverkey;
        this.servername = servername;
        this.prefix = prefix;
        this.userinfoUrl = userinfoUrl;
        this.timeoutUrl = timeoutUrl;
        this.disabled = disabled;
        this.organisation = organisation;
        this.extCourseClassMaps = extCourseClassMaps;
        this.extUserUseridMaps = extUserUseridMaps;
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
     *             column="serverid"
     *             unique="true"
     *             length="255"
     *             not-null="true"
     *         
     */
    public String getServerid() {
        return this.serverid;
    }

    public void setServerid(String serverid) {
        this.serverid = serverid;
    }

    /** 
     *            @hibernate.property
     *             column="serverkey"
     *             length="65535"
     *             not-null="true"
     *         
     */
    public String getServerkey() {
        return this.serverkey;
    }

    public void setServerkey(String serverkey) {
        this.serverkey = serverkey;
    }

    /** 
     *            @hibernate.property
     *             column="servername"
     *             length="255"
     *             not-null="true"
     *         
     */
    public String getServername() {
        return this.servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    /** 
     *            @hibernate.property
     *             column="serverdesc"
     *             length="65535"
     *         
     */
    public String getServerdesc() {
        return this.serverdesc;
    }

    public void setServerdesc(String serverdesc) {
        this.serverdesc = serverdesc;
    }

    /** 
     *            @hibernate.property
     *             column="prefix"
     *             unique="true"
     *             length="11"
     *             not-null="true"
     *         
     */
    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /** 
     *            @hibernate.property
     *             column="userinfo_url"
     *             length="65535"
     *             not-null="true"
     *         
     */
    public String getUserinfoUrl() {
        return this.userinfoUrl;
    }

    public void setUserinfoUrl(String userinfoUrl) {
        this.userinfoUrl = userinfoUrl;
    }

    /** 
     *            @hibernate.property
     *             column="timeout_url"
     *             length="65535"
     *             not-null="true"
     *         
     */
    public String getTimeoutUrl() {
        return this.timeoutUrl;
    }

    public void setTimeoutUrl(String timeoutUrl) {
        this.timeoutUrl = timeoutUrl;
    }

    /** 
     *            @hibernate.property
     *             column="disabled"
     *             length="1"
     *             not-null="true"
     *         
     */
    public Boolean getDisabled() {
        return this.disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="orgid"
     *             lazy="true"         
     *         
     */
    public Organisation getOrganisation() {
        return this.organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="ext_server_org_map_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.integration.ExtCourseClassMap"
     *         
     */
    public Set getExtCourseClassMaps() {
        return this.extCourseClassMaps;
    }

    public void setExtCourseClassMaps(Set extCourseClassMaps) {
        this.extCourseClassMaps = extCourseClassMaps;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="ext_server_org_map_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.integration.ExtUserUseridMap"
     *         
     */
    public Set getExtUserUseridMaps() {
        return this.extUserUseridMaps;
    }

    public void setExtUserUseridMaps(Set extUserUseridMaps) {
        this.extUserUseridMaps = extUserUseridMaps;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("sid", getSid())
            .append("serverid", getServerid())
            .append("serverkey", getServerkey())
            .append("servername", getServername())
            .toString();
    }

}
