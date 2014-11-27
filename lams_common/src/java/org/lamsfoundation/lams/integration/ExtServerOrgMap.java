package org.lamsfoundation.lams.integration;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.Organisation;

public class ExtServerOrgMap implements Serializable, Comparable {

    private static final long serialVersionUID = 337894825609071182L;

    /** identifier field */
    private Integer sid;

    /** persistent field */
    private String serverid;

    /** persistent field */
    private String serverkey;

    /** persistent field */
    private String servername;

    /** persistent field */
    private String serverdesc;

    /** persistent field */
    private String prefix;

    /** persistent field */
    private String userinfoUrl;
    
    /** persistent field */
    private String serverUrl;

    /** persistent field */
    private String timeoutUrl;
    
    /** persistent field */
    private String lessonFinishUrl;

    /** persistent field */
    private Boolean disabled;
    
    /** persistent field */
    private int timeToLiveLoginRequest;

    /** persistent field */
    private Organisation organisation;

    /** persistent field */
    private Set extCourseClassMaps;

    /** persistent field */
    private Set extUserUseridMaps;

    /** default constructor */
    public ExtServerOrgMap() {
	timeToLiveLoginRequest = 80;
    }

    public Integer getSid() {
	return this.sid;
    }

    public void setSid(Integer sid) {
	this.sid = sid;
    }

    public String getServerid() {
	return this.serverid;
    }

    public void setServerid(String serverid) {
	this.serverid = serverid;
    }

    public String getServerkey() {
	return this.serverkey;
    }

    public void setServerkey(String serverkey) {
	this.serverkey = serverkey;
    }

    public String getServername() {
	return this.servername;
    }

    public void setServername(String servername) {
	this.servername = servername;
    }

    public String getServerdesc() {
	return this.serverdesc;
    }

    public void setServerdesc(String serverdesc) {
	this.serverdesc = serverdesc;
    }

    public String getPrefix() {
	return this.prefix;
    }

    public void setPrefix(String prefix) {
	this.prefix = prefix;
    }

    public String getUserinfoUrl() {
	return this.userinfoUrl;
    }
    
    public void setUserinfoUrl(String userinfoUrl) {
	this.userinfoUrl = userinfoUrl;
    }

    public String getServerUrl() {
	return this.serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getTimeoutUrl() {
	return this.timeoutUrl;
    }

    public void setTimeoutUrl(String timeoutUrl) {
	this.timeoutUrl = timeoutUrl;
    }

    public String getLessonFinishUrl() {
	return this.lessonFinishUrl;
    }

    public void setLessonFinishUrl(String lessonFinishUrl) {
	this.lessonFinishUrl = lessonFinishUrl;
    }

    public Boolean getDisabled() {
	return this.disabled;
    }

    public void setDisabled(Boolean disabled) {
	this.disabled = disabled;
    }
    
    /**
     * Measured in minutes.
     * 
     * @return
     */
    public int getTimeToLiveLoginRequest() {
	return this.timeToLiveLoginRequest;
    }

    public void setTimeToLiveLoginRequest(int timeToLiveLoginRequest) {
	this.timeToLiveLoginRequest = timeToLiveLoginRequest;
    }

    public Organisation getOrganisation() {
	return this.organisation;
    }

    public void setOrganisation(Organisation organisation) {
	this.organisation = organisation;
    }

    public Set getExtCourseClassMaps() {
	return this.extCourseClassMaps;
    }

    public void setExtCourseClassMaps(Set extCourseClassMaps) {
	this.extCourseClassMaps = extCourseClassMaps;
    }

    public Set getExtUserUseridMaps() {
	return this.extUserUseridMaps;
    }

    public void setExtUserUseridMaps(Set extUserUseridMaps) {
	this.extUserUseridMaps = extUserUseridMaps;
    }

    public String toString() {
	return new ToStringBuilder(this).append("sid", getSid()).append("serverid", getServerid()).append("serverkey",
		getServerkey()).append("servername", getServername()).toString();
    }

    public int compareTo(Object o) {
	return serverid.compareToIgnoreCase(((ExtServerOrgMap) o).getServerid());
    }

}
