package org.lamsfoundation.lams.integration;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "lams_ext_server_org_map")
public class ExtServer implements Serializable, Comparable<ExtServer> {
    
    private static final long serialVersionUID = 337894825609071182L;

    /*
     * variables indicating the type of servers available.
     */
    /* **************************************************************** */
    public static final int INTEGRATION_SERVER_TYPE = 1;
    public static final int LTI_CONSUMER_SERVER_TYPE = 2;
    /* *************************************************************** */

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sid;

    @Column
    private String serverid;

    @Column
    private String serverkey;

    @Column
    private String servername;

    @Column
    private String serverdesc;

    @Column(name = "server_type_id")
    private Integer serverTypeId;

    @Column
    private String prefix;

    @Column(name = "userinfo_url")
    private String userinfoUrl;

    @Column(name = "lesson_finish_url")
    private String lessonFinishUrl;

    @Column(name = "ext_groups_url")
    private String extGroupsUrl;
    
    @Column(name = "membership_url")
    private String membershipUrl;

    @Column
    private Boolean disabled;

    @Column(name = "time_to_live_login_request_enabled")
    private Boolean timeToLiveLoginRequestEnabled;

    @Column(name = "time_to_live_login_request")
    private int timeToLiveLoginRequest;

    @Column(name = "learner_presence_avail")
    private Boolean learnerPresenceAvailable;

    @Column(name = "learner_im_avail")
    private Boolean learnerImAvailable;

    @Column(name = "live_edit_enabled")
    private Boolean liveEditEnabled;

    @Column(name = "enable_lesson_notifications")
    private Boolean enableLessonNotifications;

    /**
     * Should Learner start the lesson from the beginning each time he enters it.
     * Content is not removed, LessonProgress is deleted, not archived.
     */
    @Column(name = "force_restart")
    private Boolean forceLearnerRestart;

    /**
     * Should Learners be allowed to restart the lesson after finishing it.
     * Content is not removed, LessonProgress is archived and then deleted.
     */
    @Column(name = "allow_restart")
    private Boolean allowLearnerRestart;

    /**
     * Should learners be displayed activity gradebook on lesson complete.
     */
    @Column(name = "gradebook_on_complete")
    private Boolean gradebookOnComplete;

    @OneToMany(mappedBy = "extServer")
    private Set<ExtCourseClassMap> extCourseClassMaps = new HashSet<ExtCourseClassMap>();

    @OneToMany(mappedBy = "extServer")
    private Set<ExtUserUseridMap> extUserUseridMaps = new HashSet<ExtUserUseridMap>();

    /**
     * Comma-separated list of roles that LTI tool consumer uses to indicate user monitor role
     */
    @Column(name = "lti_consumer_monitor_roles")
    private String ltiToolConsumerMonitorRoles;
    
    @Column(name = "use_alternative_user_id_parameter_name")
    private Boolean useAlternativeUseridParameterName;

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
    
    public String getMembershipUrl() {
	return this.membershipUrl;
    }

    public void setMembershipUrl(String membershipUrl) {
	this.membershipUrl = membershipUrl;
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
     */
    public int getTimeToLiveLoginRequest() {
	return this.timeToLiveLoginRequest;
    }

    public void setTimeToLiveLoginRequest(int timeToLiveLoginRequest) {
	this.timeToLiveLoginRequest = timeToLiveLoginRequest;
    }

    public Set<ExtCourseClassMap> getExtCourseClassMaps() {
	return this.extCourseClassMaps;
    }

    public void setExtCourseClassMaps(Set<ExtCourseClassMap> extCourseClassMaps) {
	this.extCourseClassMaps = extCourseClassMaps;
    }

    public Set<ExtUserUseridMap> getExtUserUseridMaps() {
	return this.extUserUseridMaps;
    }

    public void setExtUserUseridMaps(Set<ExtUserUseridMap> extUserUseridMaps) {
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
    public int compareTo(ExtServer o) {
	return serverid.compareToIgnoreCase(o.getServerid());
    }

    public boolean isIntegrationServer() {
	return getServerTypeId().intValue() == ExtServer.INTEGRATION_SERVER_TYPE;
    }

    public boolean isLtiConsumer() {
	return getServerTypeId().intValue() == ExtServer.LTI_CONSUMER_SERVER_TYPE;
    }

    public Boolean getLearnerPresenceAvailable() {
	return learnerPresenceAvailable;
    }

    public void setLearnerPresenceAvailable(Boolean learnerPresenceAvailable) {
	this.learnerPresenceAvailable = learnerPresenceAvailable;
    }

    public Boolean getLearnerImAvailable() {
	return learnerImAvailable;
    }

    public void setLearnerImAvailable(Boolean learnerImAvailable) {
	this.learnerImAvailable = learnerImAvailable;
    }

    public Boolean getLiveEditEnabled() {
	return liveEditEnabled;
    }

    public void setLiveEditEnabled(Boolean liveEditEnabled) {
	this.liveEditEnabled = liveEditEnabled;
    }

    public Boolean getEnableLessonNotifications() {
	return enableLessonNotifications;
    }

    public void setEnableLessonNotifications(Boolean enableLessonNotifications) {
	this.enableLessonNotifications = enableLessonNotifications;
    }

    public Boolean getForceLearnerRestart() {
	return forceLearnerRestart;
    }

    public void setForceLearnerRestart(Boolean forceLearnerRestart) {
	this.forceLearnerRestart = forceLearnerRestart;
    }

    public Boolean getAllowLearnerRestart() {
	return allowLearnerRestart;
    }

    public void setAllowLearnerRestart(Boolean allowLearnerRestart) {
	this.allowLearnerRestart = allowLearnerRestart;
    }

    public Boolean getGradebookOnComplete() {
	return gradebookOnComplete;
    }

    public void setGradebookOnComplete(Boolean gradebookOnComplete) {
	this.gradebookOnComplete = gradebookOnComplete;
    }
    
    public Boolean getUseAlternativeUseridParameterName() {
	return useAlternativeUseridParameterName;
    }

    public void setUseAlternativeUseridParameterName(Boolean useAlternativeUseridParameterName) {
	this.useAlternativeUseridParameterName = useAlternativeUseridParameterName;
    }
}