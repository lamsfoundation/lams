package org.lamsfoundation.lams.integration;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ExtServer implements Serializable, Comparable {

    private static final long serialVersionUID = 337894825609071182L;
    
    /*
     * static final variables indicating the type of servers available. 
     */
    /* **************************************************************** */
    public static final int INTEGRATION_SERVER_TYPE = 1;
    public static final int LTI_CONSUMER_SERVER_TYPE = 2;
    /** *************************************************************** */

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
    
    /** The type of activity */
    private Integer serverTypeId;

    /** persistent field */
    private String prefix;

    /** persistent field */
    private String userinfoUrl;

    /** persistent field */
    private String lessonFinishUrl;

    private String extGroupsUrl;

    /** persistent field */
    private Boolean disabled;

    private Boolean timeToLiveLoginRequestEnabled;

    /** persistent field */
    private int timeToLiveLoginRequest;

    /** persistent field */
    private Set extCourseClassMaps;

    /** persistent field */
    private Set extUserUseridMaps;
    
    /**
     * Comma-separated list of roles that LTI tool consumer uses to indicate user monitor role
     */
    private String ltiToolConsumerMonitorRoles;

    /** default constructor */
    public ExtServer() {
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
    
    public Integer getServerTypeId() {
	return serverTypeId;
    }

    public void setServerTypeId(Integer serverTypeId) {
	this.serverTypeId = serverTypeId;
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

    public String getLessonFinishUrl() {
	return this.lessonFinishUrl;
    }

    public void setLessonFinishUrl(String lessonFinishUrl) {
	this.lessonFinishUrl = lessonFinishUrl;
    }

    public String getExtGroupsUrl() {
	return this.extGroupsUrl;
    }

    public void setExtGroupsUrl(String extGroupsUrl) {
	this.extGroupsUrl = extGroupsUrl;
    }

    public Boolean getDisabled() {
	return this.disabled;
    }

    public void setDisabled(Boolean disabled) {
	this.disabled = disabled;
    }

    public Boolean getTimeToLiveLoginRequestEnabled() {
	return this.timeToLiveLoginRequestEnabled;
    }

    public void setTimeToLiveLoginRequestEnabled(Boolean timeToLiveLoginRequestEnabled) {
	this.timeToLiveLoginRequestEnabled = timeToLiveLoginRequestEnabled;
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
    
    public String getLtiToolConsumerMonitorRoles() {
	return this.ltiToolConsumerMonitorRoles;
    }
    
    public void setLtiToolConsumerMonitorRoles(String ltiToolConsumerMonitorRoles) {
	this.ltiToolConsumerMonitorRoles = ltiToolConsumerMonitorRoles;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("sid", getSid()).append("serverid", getServerid())
		.append("serverkey", getServerkey()).append("servername", getServername()).toString();
    }

    @Override
    public int compareTo(Object o) {
	return serverid.compareToIgnoreCase(((ExtServer) o).getServerid());
    }
    
    public boolean isIntegrationServer() {
	return getServerTypeId().intValue() == ExtServer.INTEGRATION_SERVER_TYPE;
    }
    
    public boolean isLtiConsumer() {
	return getServerTypeId().intValue() == ExtServer.LTI_CONSUMER_SERVER_TYPE;
    }

}
