/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */

package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.themes.dto.CSSThemeBriefDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserFlashDTO;
import org.lamsfoundation.lams.util.LanguageUtil;

/**
 * @hibernate.class table="lams_user"
 * 
 */
public class User implements Serializable, Comparable {

    /** identifier field */
    private Integer userId;

    /** persistent field */
    private String login;

    /**
     * persistent field ] TODO needs to be removed for security reason
     */
    private String password;

    /** nullable persistent field */
    private String title;

    /** nullable persistent field */
    private String firstName;

    /** nullable persistent field */
    private String lastName;

    /** nullable persistent field */
    private String addressLine1;

    /** nullable persistent field */
    private String addressLine2;

    /** nullable persistent field */
    private String addressLine3;

    /** nullable persistent field */
    private String city;

    /** nullable persistent field */
    private String state;

    /** nullable persistent field */
    private String postcode;

    /** nullable persistent field */
    private String country;

    /** nullable persistent field */
    private String dayPhone;

    /** nullable persistent field */
    private String eveningPhone;

    /** nullable persistent field */
    private String mobilePhone;

    /** nullable persistent field */
    private String fax;

    /** nullable persistent field */
    private String email;

    /** persistent field */
    private Boolean disabledFlag;

    /** persistent field */
    private SupportedLocale locale;

    /** persistent field */
    private Short timeZone;

    /** persistent field */
    private Date createDate;

    /** persistent field */
    private Workspace workspace;

    /** persistent field */
    private AuthenticationMethod authenticationMethod;

    /** persistent field */
    private Set userOrganisations;

    /** persistent field */
    private CSSThemeVisualElement flashTheme;

    /** persistent field */
    private CSSThemeVisualElement htmlTheme;

    /** nullable persistent field */
    private String chatId;

    /** persistent field */
    private Set learnerProgresses;

    /** persistent field */
    private Set userToolSessions;

    /** persistent field */
    private Set userGroups;

    /** persistent field */
    private Set learningDesigns;

    /** persistent field */
    private Set lessons;

    /** persistent field */
    private Long portraitUuid;

    /** persistent field */
    private Boolean changePassword;

    /** persistent field */
    private Boolean enableFlash;

    /** persistent field */
    private String lamsCommunityToken;

    /** persistent field */
    private String lamsCommunityUsername;

    /** persistent field */
    private Boolean tutorialsDisabled;

    /** persistent field */
    private Set<String> pagesWithDisabledTutorials = new HashSet<String>();

    /** persistent field - latch */
    private Boolean firstLogin;

    /** persistent field - for Pedagogical Planner */
    private Set<Long> recentlyModifiedLearningDesigns = new LinkedHashSet<Long>();

    // ------- TIMEZONES (hardcoded, there is no need to put them into database --------------

    public static String[] timezoneList = new String[] { "GMT-12", "GMT-11", "GMT-10", "GMT-9", "GMT-8", "GMT-7",
	    "GMT-6", "GMT-5", "GMT-4", "GMT-3:30", "GMT-3", "GMT-2", "GMT-1", "GMT", "GMT+1", "GMT+2", "GMT+3",
	    "GMT+3:30", "GMT+4", "GMT+4:30", "GMT+5", "GMT+5:30", "GMT+5:45", "GMT+6", "GMT+7", "GMT+8", "GMT+9",
	    "GMT+9:30", "GMT+10", "GMT+11", "GMT+12" };

    /** full constructor */
    public User(String login, String password, String title, String firstName, String lastName, String addressLine1,
	    String addressLine2, String addressLine3, String city, String state, String postcode, String country,
	    String dayPhone, String eveningPhone, String mobilePhone, String fax, String email, Boolean disabledFlag,
	    Date createDate, Workspace workspace, AuthenticationMethod authenticationMethod,
	    CSSThemeVisualElement flashTheme, CSSThemeVisualElement htmlTheme, Set userOrganisations, String chatId,
	    Set learnerProgresses, Set userToolSessions, Set userGroups, Set learningDesigns, Set lessons,
	    Long portraitUuid, Boolean changePassword, Boolean enableFlash, String lamsCommunityToken) {
	this.login = login;
	this.password = password;
	this.title = title;
	this.firstName = firstName;
	this.lastName = lastName;
	this.addressLine1 = addressLine1;
	this.addressLine2 = addressLine2;
	this.addressLine3 = addressLine3;
	this.city = city;
	this.state = state;
	this.postcode = postcode;
	this.country = country;
	this.dayPhone = dayPhone;
	this.eveningPhone = eveningPhone;
	this.mobilePhone = mobilePhone;
	this.fax = fax;
	this.email = email;
	this.disabledFlag = disabledFlag;
	this.createDate = createDate;
	this.workspace = workspace;
	this.authenticationMethod = authenticationMethod;
	this.flashTheme = flashTheme;
	this.htmlTheme = htmlTheme;
	this.chatId = chatId;
	this.userOrganisations = userOrganisations;
	this.learnerProgresses = learnerProgresses;
	this.userToolSessions = userToolSessions;
	this.userGroups = userGroups;
	this.learningDesigns = learningDesigns;
	this.lessons = lessons;
	this.portraitUuid = portraitUuid;
	this.changePassword = changePassword;
	this.enableFlash = enableFlash;
	this.lamsCommunityToken = lamsCommunityToken;
    }

    /** default constructor */
    public User() {
	changePassword = false;
	enableFlash = true;
    }

    /** minimal constructor */
    public User(String login, String password, Boolean disabledFlag, Date createDate, Workspace workspace,
	    AuthenticationMethod authenticationMethod, Set userOrganisations, Set learnerProgresses,
	    Set userToolSessions, Set userGroups, Set learningDesigns, Set lessons) {
	this.login = login;
	this.password = password;
	this.disabledFlag = disabledFlag;
	this.createDate = createDate;
	this.workspace = workspace;
	this.authenticationMethod = authenticationMethod;
	this.userOrganisations = userOrganisations;
	this.learnerProgresses = learnerProgresses;
	this.userToolSessions = userToolSessions;
	this.userGroups = userGroups;
	this.learningDesigns = learningDesigns;
	this.lessons = lessons;
	changePassword = false;
	enableFlash = true;
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Integer" column="user_id"
     * 
     */
    public Integer getUserId() {
	return userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }

    /**
     * @hibernate.property column="login" length="255" unique="true" not-null="true"
     * 
     */
    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    /**
     * @hibernate.property column="password" length="50" not-null="true"
     * 
     */
    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    /**
     * @hibernate.property column="title" length="32"
     * 
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @hibernate.property column="first_name" length="128"
     * 
     */
    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     * @hibernate.property column="last_name" length="128"
     * 
     */
    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getFullName() {
	return this.getFirstName() + " " + this.getLastName();
    }

    /**
     * @hibernate.property column="address_line_1" length="64"
     * 
     */
    public String getAddressLine1() {
	return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
	this.addressLine1 = addressLine1;
    }

    /**
     * @hibernate.property column="address_line_2" length="64"
     * 
     */
    public String getAddressLine2() {
	return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
	this.addressLine2 = addressLine2;
    }

    /**
     * @hibernate.property column="address_line_3" length="64"
     * 
     */
    public String getAddressLine3() {
	return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
	this.addressLine3 = addressLine3;
    }

    /**
     * @hibernate.property column="city" length="64"
     * 
     */
    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    /**
     * @hibernate.property column="state" length="64"
     * 
     */
    public String getState() {
	return state;
    }

    public void setState(String state) {
	this.state = state;
    }

    /**
     * @hibernate.property column="postcode" length="10"
     * 
     */
    public String getPostcode() {
	return postcode;
    }

    public void setPostcode(String postcode) {
	this.postcode = postcode;
    }

    /**
     * @hibernate.property column="country" length="64"
     * 
     */
    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    /**
     * @hibernate.property column="day_phone" length="64"
     * 
     */
    public String getDayPhone() {
	return dayPhone;
    }

    public void setDayPhone(String dayPhone) {
	this.dayPhone = dayPhone;
    }

    /**
     * @hibernate.property column="evening_phone" length="64"
     * 
     */
    public String getEveningPhone() {
	return eveningPhone;
    }

    public void setEveningPhone(String eveningPhone) {
	this.eveningPhone = eveningPhone;
    }

    /**
     * @hibernate.property column="mobile_phone" length="64"
     * 
     */
    public String getMobilePhone() {
	return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
	this.mobilePhone = mobilePhone;
    }

    /**
     * @hibernate.property column="fax" length="64"
     * 
     */
    public String getFax() {
	return fax;
    }

    public void setFax(String fax) {
	this.fax = fax;
    }

    /**
     * @hibernate.property column="email" length="128"
     * 
     */
    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    /**
     * @hibernate.property column="disabled_flag" length="1" not-null="true"
     * 
     */
    public Boolean getDisabledFlag() {
	return disabledFlag;
    }

    public void setDisabledFlag(Boolean disabledFlag) {
	this.disabledFlag = disabledFlag;
    }

    /**
     * @hibernate.property column="create_date" length="19" not-null="true"
     * 
     */
    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     * @hibernate.many-to-one not-null="true" lazy="false" cascade="all"
     * @hibernate.column name="workspace_id"
     * 
     */
    public Workspace getWorkspace() {
	return workspace;
    }

    public void setWorkspace(Workspace workspace) {
	this.workspace = workspace;
    }

    /**
     * @hibernate.many-to-one not-null="true" lazy="false"
     * @hibernate.column name="authentication_method_id"
     * 
     */
    public AuthenticationMethod getAuthenticationMethod() {
	return authenticationMethod;
    }

    public void setAuthenticationMethod(AuthenticationMethod authenticationMethod) {
	this.authenticationMethod = authenticationMethod;
    }

    /**
     * @hibernate.many-to-one not-null="false"
     * @hibernate.column name="flash_theme_id"
     * 
     */
    public CSSThemeVisualElement getFlashTheme() {
	return flashTheme;
    }

    public void setFlashTheme(CSSThemeVisualElement flashTheme) {
	this.flashTheme = flashTheme;
    }

    /**
     * @hibernate.many-to-one not-null="false"
     * @hibernate.column name="html_theme_id"
     * 
     */
    public CSSThemeVisualElement getHtmlTheme() {
	return htmlTheme;
    }

    public void setHtmlTheme(CSSThemeVisualElement htmlTheme) {
	this.htmlTheme = htmlTheme;
    }

    /**
     * @hibernate.property column="chat_id" length="255"
     * 
     */
    public String getChatId() {
	return chatId;
    }

    public void setChatId(String chatId) {
	this.chatId = chatId;
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * @hibernate.collection-key column="user_id"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.usermanagement.UserOrganisation"
     * 
     */
    public Set getUserOrganisations() {
	return userOrganisations;
    }

    public void setUserOrganisations(Set userOrganisations) {
	this.userOrganisations = userOrganisations;
    }

    /** This methods adds a new membership for the given user */
    public void addUserOrganisation(UserOrganisation userOrganisation) {
	if (userOrganisations == null) {
	    userOrganisations = new HashSet();
	}
	userOrganisations.add(userOrganisation);
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * @hibernate.collection-key column="user_id"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.lesson.LearnerProgress"
     * 
     */
    public Set getLearnerProgresses() {
	return learnerProgresses;
    }

    public void setLearnerProgresses(Set learnerProgresses) {
	this.learnerProgresses = learnerProgresses;
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * @hibernate.collection-key column="user_id"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.lesson.UserToolSession"
     * 
     */
    public Set getUserToolSessions() {
	return userToolSessions;
    }

    public void setUserToolSessions(Set userToolSessions) {
	this.userToolSessions = userToolSessions;
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * @hibernate.collection-key column="user_id"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.learningdesign.LearningDesign"
     * 
     */
    public Set getLearningDesigns() {
	return learningDesigns;
    }

    public void setLearningDesigns(Set learningDesigns) {
	this.learningDesigns = learningDesigns;
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * @hibernate.collection-key column="user_id"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.learningdesign.Lesson"
     * 
     */
    public Set getLessons() {
	return lessons;
    }

    public void setLessons(Set lessons) {
	this.lessons = lessons;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("userId", getUserId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof User)) {
	    return false;
	}
	User castOther = (User) other;
	return new EqualsBuilder().append(this.getUserId(), castOther.getUserId()).isEquals();
    }

    public int compareTo(Object user) {
	User u = (User) user;
	return login.compareTo(u.getLogin());
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getUserId()).toHashCode();
    }

    public UserDTO getUserDTO() {
	String languageIsoCode = null;
	String countryIsoCode = null;
	String direction = null;
	String fckLanguageMapping = null;
	if (locale != null) {
	    languageIsoCode = locale.getLanguageIsoCode();
	    countryIsoCode = locale.getCountryIsoCode();
	    direction = locale.getDirection();
	    fckLanguageMapping = locale.getFckLanguageMapping();

	} else {
	    String defaults[] = LanguageUtil.getDefaultLangCountry();
	    languageIsoCode = defaults[0];
	    countryIsoCode = defaults[1];
	    direction = LanguageUtil.getDefaultDirection();
	    fckLanguageMapping = LanguageUtil.getSupportedLocale(languageIsoCode, countryIsoCode)
		    .getFckLanguageMapping();
	}

	TimeZone tz = TimeZone.getTimeZone(User.timezoneList[getTimeZone()]);

	Set<String> tutorialPages = pagesWithDisabledTutorials == null || pagesWithDisabledTutorials.isEmpty() ? null
		: pagesWithDisabledTutorials;

	return new UserDTO(userId, firstName, lastName, login, languageIsoCode, countryIsoCode, direction, email,
		new CSSThemeBriefDTO(flashTheme), new CSSThemeBriefDTO(htmlTheme),
		// TimeZone.getTimeZone("Australia/Sydney"),
		tz, authenticationMethod.getAuthenticationMethodId(), fckLanguageMapping, enableFlash,
		lamsCommunityToken, lamsCommunityUsername, tutorialsDisabled, tutorialPages, 
		(firstLogin == null ? true : false)  // assume no firstLogin value means they haven't logged in
	);
    }

    public UserFlashDTO getUserFlashDTO() {
	return new UserFlashDTO(userId, firstName, lastName, login);
    }

    /**
     * This method checks whether user is a member of the given organisation
     */
    public boolean isMember(Organisation organisation) {
	Iterator iterator = userOrganisations.iterator();
	while (iterator.hasNext()) {
	    UserOrganisation userOrganisation = (UserOrganisation) iterator.next();
	    Integer organisationID = userOrganisation.getOrganisation().getOrganisationId();
	    if (organisationID == organisation.getOrganisationId()) {
		return true;
	    }
	}
	return false;
    }

    /**
     * This method checks whether the user has membership access to the given workspaceFolder. Membership access means I
     * am a member of the organisation relating to this folder (either as the root folder or a folder under the root
     * folder).
     * 
     * Membership access means that the user has read and write access but cannot modify anybody else's content/stuff
     */
    public boolean hasMemberAccess(WorkspaceFolder workspaceFolder) {
	boolean foundMemberFolder = false;
	Integer workspaceFolderID = workspaceFolder != null ? workspaceFolder.getWorkspaceFolderId() : null;
	if (workspaceFolderID != null) {
	    Iterator iterator = userOrganisations.iterator();
	    while (iterator.hasNext() && !foundMemberFolder) {
		UserOrganisation userOrganisation = (UserOrganisation) iterator.next();
		// not all orgs have a folder
		Workspace workspace = userOrganisation.getOrganisation().getWorkspace();
		if (workspace != null) {
		    foundMemberFolder = checkFolders(workspace.getFolders(), workspaceFolderID);
		}
	    }
	}
	return foundMemberFolder;
    }

    private boolean checkFolders(Set folders, Integer desiredWorkspaceFolderId) {
	boolean foundMemberFolder = false;
	Iterator folderIter = folders.iterator();
	while (folderIter.hasNext() && !foundMemberFolder) {
	    WorkspaceFolder folder = (WorkspaceFolder) folderIter.next();
	    Integer folderID = folder.getWorkspaceFolderId();
	    if (folderID.equals(desiredWorkspaceFolderId)) {
		foundMemberFolder = true;
	    } else {
		Set childFolders = folder.getChildWorkspaceFolders();
		if (childFolders != null) {
		    foundMemberFolder = checkFolders(childFolders, desiredWorkspaceFolderId);
		}
	    }
	}
	return foundMemberFolder;
    }

    /**
     * @hibernate.many-to-one not-null="true" lazy="false"
     * @hibernate.column name="locale_id"
     * @param localeCountry
     */
    public SupportedLocale getLocale() {
	return locale;
    }

    public void setLocale(SupportedLocale locale) {
	this.locale = locale;
    }

    /**
     * @hibernate.property column="portrait_uuid" length="20"
     * 
     */
    public Long getPortraitUuid() {
	return portraitUuid;
    }

    public void setPortraitUuid(Long portraitUuid) {
	this.portraitUuid = portraitUuid;
    }

    /**
     * @hibernate.property column="change_password" length="1" not-null="true"
     * 
     */
    public Boolean getChangePassword() {
	return changePassword;
    }

    public void setChangePassword(Boolean changePassword) {
	this.changePassword = changePassword;
    }

    /**
     * @hibernate.property column="enable_flash" length="1" not-null="true"
     * 
     */
    public Boolean getEnableFlash() {
	return enableFlash;
    }

    public void setEnableFlash(Boolean enableFlash) {
	this.enableFlash = enableFlash;
    }

    /**
     * @hibernate.property column="lams_community_token" length="255" not-null="false"
     * 
     */
    public String getLamsCommunityToken() {
	return lamsCommunityToken;
    }

    public void setLamsCommunityToken(String lamsCommunityToken) {
	this.lamsCommunityToken = lamsCommunityToken;
    }

    /**
     * @hibernate.property column="lams_community_username" length="255" not-null="false"
     * 
     */
    public String getLamsCommunityUsername() {
	return lamsCommunityUsername;
    }

    public void setLamsCommunityUsername(String lamsCommunityUsername) {
	this.lamsCommunityUsername = lamsCommunityUsername;
    }

    /**
     * Returns user's time zone. If NULL, returns server default time zone. If server default time zone is not in the
     * list of supported time zones, returns GMT.
     * 
     * @hibernate.property column="timezone"
     * 
     */
    public Short getTimeZone() {
	if (timeZone == null) {
	    TimeZone defaultTimeZone = TimeZone.getDefault();
	    int defaultRawOffset = defaultTimeZone.getRawOffset();
	    // initial index of GMT time zone, but later it is verified
	    short fallbackTimeZone = 13;
	    for (short timeZoneIndex = 0; timeZoneIndex < User.timezoneList.length; timeZoneIndex++) {
		TimeZone candidateTimeZone = TimeZone.getTimeZone(User.timezoneList[timeZoneIndex]);
		if (defaultRawOffset == candidateTimeZone.getRawOffset()) {
		    // we found a time zone from the list which has the same offset as the server's one
		    timeZone = timeZoneIndex;
		    break;
		} else if (candidateTimeZone.getRawOffset() == 0) {
		    // we found GMT time zone; it will be used if server default time zone is not in the list
		    fallbackTimeZone = timeZoneIndex;
		}
	    }
	    if (timeZone == null) {
		timeZone = fallbackTimeZone;
	    }
	}
	return timeZone;
    }

    public void setTimeZone(Short timeZone) {

	this.timeZone = timeZone;
    }

    /**
     * @hibernate.property column="tutorials_disabled" length="1"
     * 
     */
    public Boolean getTutorialsDisabled() {
	return tutorialsDisabled;
    }

    public void setTutorialsDisabled(Boolean tutorialsDisabled) {
	this.tutorialsDisabled = tutorialsDisabled;
    }

    /**
     * @hibernate.set lazy="false" cascade="all-delete-orphan"
     * @hibernate.collection-key column="user_id"
     * @hibernate.collection-element column="page_str" type="string" length="5" not-null="true"
     */
    public Set<String> getPagesWithDisabledTutorials() {
	return pagesWithDisabledTutorials;
    }

    public void setPagesWithDisabledTutorials(Set<String> pagesWithDisabledTutorials) {
	this.pagesWithDisabledTutorials = pagesWithDisabledTutorials;
    }

    /**
     * @hibernate.property column="first_login" length="1"
     * 
     */
    public Boolean isFirstLogin() {
	return firstLogin;
    }

    public void setFirstLogin(Boolean firstLogin) {
	this.firstLogin = firstLogin;
    }

    /**
     * @hibernate.set lazy="true" table="lams_planner_recent_learning_designs" cascade="all-delete-orphan"
     *                order-by="last_modified_date DESC"
     * @hibernate.collection-key column="user_id"
     * @hibernate.collection-element column="learning_design_id" type="long" not-null="true"
     */
    public Set<Long> getRecentlyModifiedLearningDesigns() {
	return recentlyModifiedLearningDesigns;
    }

    public void setRecentlyModifiedLearningDesigns(Set<Long> recentlyModifiedLearningDesigns) {
	this.recentlyModifiedLearningDesigns = recentlyModifiedLearningDesigns;
    }

}