package org.lamsfoundation.lams.integration;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;

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

    @Column(name = "logout_url")
    private String logoutUrl;

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

    @Column(name = "add_staff_to_all_lessons")
    private boolean addStaffToAllLessons;

    @Column(name = "live_edit_enabled")
    private Boolean liveEditEnabled;

    @Column(name = "enable_lesson_notifications")
    private Boolean enableLessonNotifications;

    /**
     * Should Learner start the lesson from the beginning each time he enters it. Content is not removed, LessonProgress
     * is deleted, not archived.
     */
    @Column(name = "force_restart")
    private Boolean forceLearnerRestart;

    /**
     * Should Learners be allowed to restart the lesson after finishing it. Content is not removed, LessonProgress is
     * archived and then deleted.
     */
    @Column(name = "allow_restart")
    private Boolean allowLearnerRestart;

    @Column(name = "start_in_monitor")
    private boolean startInMonitor;

    /**
     * Should learners be displayed activity gradebook on lesson complete.
     */
    @Column(name = "gradebook_on_complete")
    private Boolean gradebookOnComplete;

    @OneToMany(mappedBy = "extServer")
    private Set<ExtCourseClassMap> extCourseClassMaps = new HashSet<>();

    @OneToMany(mappedBy = "extServer")
    private Set<ExtUserUseridMap> extUserUseridMaps = new HashSet<>();

    /**
     * Comma-separated list of roles that LTI tool consumer uses to indicate user monitor role
     */
    @Column(name = "lti_consumer_monitor_roles")
    private String ltiToolConsumerMonitorRoles;

    @Column(name = "user_id_parameter_name")
    private String userIdParameterName;

    @Column(name = "default_country")
    private String defaultCountry;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "default_locale_id")
    private SupportedLocale defaultLocale;

    @Column(name = "default_timezone")
    private String defaultTimeZone;

    /**
     * Options currently used by LTI Advantage, but eventually also used in SAML
     */
    @Column(name = "use_course_prefix")
    private boolean useCoursePrefix;

    @Column(name = "user_registration_enabled")
    private boolean userRegistrationEnabled;

    @Column(name = "user_name_lower_case")
    private boolean userNameLowerCase;

    /**
     * Options used by LTI Advantage
     */
    @Column(name = "lti_adv_enforce_cookie")
    private boolean enforceStateCookie;

    @Column(name = "lti_adv_reregistration_enabled")
    private boolean toolReregistrationEnabled;

    @Column(name = "lti_adv_issuer")
    private String issuer;

    @Column(name = "lti_adv_client_id")
    private String clientId;

    @Column(name = "lti_adv_platform_key_set_url")
    private String platformKeySetUrl;

    @Column(name = "lti_adv_oidc_auth_url")
    private String oidcAuthUrl;

    @Column(name = "lti_adv_access_token_url")
    private String accessTokenUrl;

    @Column(name = "lti_adv_tool_name")
    private String toolName;

    @Column(name = "lti_adv_tool_description")
    private String toolDescription;

    @Column(name = "lti_adv_tool_key_set_url")
    private String toolKeySetUrl;

    @Column(name = "lti_adv_tool_key_id")
    private String toolKeyId;

    @Column(name = "lti_adv_public_key")
    private String publicKey;

    @Column(name = "lti_adv_private_key")
    private String privateKey;

    public ExtServer() {
	timeToLiveLoginRequest = 80;
	userIdParameterName = "user_id";
	toolReregistrationEnabled = true;
	useCoursePrefix = true;
	userRegistrationEnabled = true;
	addStaffToAllLessons = false;
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

    public String getLogoutUrl() {
	return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
	this.logoutUrl = logoutUrl;
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

    public boolean isAddStaffToAllLessons() {
	return addStaffToAllLessons;
    }

    public void setAddStaffToAllLessons(boolean addStaffToAllLessons) {
	this.addStaffToAllLessons = addStaffToAllLessons;
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

    public boolean isStartInMonitor() {
	return startInMonitor;
    }

    public void setStartInMonitor(boolean startInMonitor) {
	this.startInMonitor = startInMonitor;
    }

    public Boolean getGradebookOnComplete() {
	return gradebookOnComplete;
    }

    public void setGradebookOnComplete(Boolean gradebookOnComplete) {
	this.gradebookOnComplete = gradebookOnComplete;
    }

    public String getUserIdParameterName() {
	return userIdParameterName;
    }

    public void setUserIdParameterName(String userIdParameterName) {
	this.userIdParameterName = userIdParameterName;
    }

    public String getDefaultCountry() {
	return defaultCountry;
    }

    public void setDefaultCountry(String defaultCountry) {
	this.defaultCountry = defaultCountry;
    }

    public SupportedLocale getDefaultLocale() {
	return defaultLocale;
    }

    public void setDefaultLocale(SupportedLocale defaultLocale) {
	this.defaultLocale = defaultLocale;
    }

    public String getDefaultTimeZone() {
	return defaultTimeZone;
    }

    public void setDefaultTimeZone(String defaultTimeZone) {
	this.defaultTimeZone = defaultTimeZone;
    }

    public boolean isUseCoursePrefix() {
	return useCoursePrefix;
    }

    public void setUseCoursePrefix(boolean useCoursePrefix) {
	this.useCoursePrefix = useCoursePrefix;
    }

    public boolean isUserRegistrationEnabled() {
	return userRegistrationEnabled;
    }

    public void setUserRegistrationEnabled(boolean userRegistrationEnabled) {
	this.userRegistrationEnabled = userRegistrationEnabled;
    }

    public boolean isUserNameLowerCase() {
	return userNameLowerCase;
    }

    public void setUserNameLowerCase(boolean userNameLowerCase) {
	this.userNameLowerCase = userNameLowerCase;
    }

    public boolean isEnforceStateCookie() {
	return enforceStateCookie;
    }

    public void setEnforceStateCookie(boolean enforceStateCookie) {
	this.enforceStateCookie = enforceStateCookie;
    }

    public boolean isToolReregistrationEnabled() {
	return toolReregistrationEnabled;
    }

    public void setToolReregistrationEnabled(boolean toolReregistrationEnabled) {
	this.toolReregistrationEnabled = toolReregistrationEnabled;
    }

    public String getToolName() {
	return toolName;
    }

    public void setToolName(String toolName) {
	this.toolName = toolName;
    }

    public String getToolDescription() {
	return toolDescription;
    }

    public void setToolDescription(String toolDescription) {
	this.toolDescription = toolDescription;
    }

    public String getIssuer() {
	return issuer;
    }

    public void setIssuer(String issuer) {
	this.issuer = issuer;
    }

    public String getClientId() {
	return clientId;
    }

    public void setClientId(String clientId) {
	this.clientId = clientId;
    }

    public String getPlatformKeySetUrl() {
	return platformKeySetUrl;
    }

    public void setPlatformKeySetUrl(String platformKeySetUrl) {
	this.platformKeySetUrl = platformKeySetUrl;
    }

    public String getOidcAuthUrl() {
	return oidcAuthUrl;
    }

    public void setOidcAuthUrl(String oidcAuthUrl) {
	this.oidcAuthUrl = oidcAuthUrl;
    }

    public String getAccessTokenUrl() {
	return accessTokenUrl;
    }

    public void setAccessTokenUrl(String accessTokenUrl) {
	this.accessTokenUrl = accessTokenUrl;
    }

    public String getToolKeySetUrl() {
	return toolKeySetUrl;
    }

    public void setToolKeySetUrl(String toolKeySetUrl) {
	this.toolKeySetUrl = toolKeySetUrl;
    }

    public String getToolKeyId() {
	return toolKeyId;
    }

    public void setToolKeyId(String toolKeyId) {
	this.toolKeyId = toolKeyId;
    }

    public String getPublicKey() {
	return publicKey;
    }

    public void setPublicKey(String publicKey) {
	this.publicKey = publicKey;
    }

    public String getPrivateKey() {
	return privateKey;
    }

    public void setPrivateKey(String privateKey) {
	this.privateKey = privateKey;
    }
}