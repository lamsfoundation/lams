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
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.dto.ThemeDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserBasicDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.LanguageUtil;

public class User implements Serializable, Comparable {

    /** identifier field */
    private Integer userId;

    /** persistent field */
    private String login;

    /**
     * persistent field
     */
    private String password;

    private Integer failedAttempts;

    private Date lockOutTime;

    private Boolean twoFactorAuthenticationEnabled;

    private String twoFactorAuthenticationSecret;

    /**
     * persistent field
     */
    private String salt;

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
    private String timeZone;

    /** persistent field */
    private Date createDate;

    /** persistent field */
    private WorkspaceFolder workspaceFolder;

    /** persistent field */
    private AuthenticationMethod authenticationMethod;

    /** persistent field */
    private Set<UserOrganisation> userOrganisations;

    /** persistent field */
    private Integer lastVisitedOrganisationId;

    /** persistent field */
    private Theme theme;

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

    private Date modifiedDate;

    /** default constructor */
    public User() {
	changePassword = false;
	twoFactorAuthenticationEnabled = false;
    }

    public Integer getUserId() {
	return userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    public String getPassword() {
	return password;
    }

    public String getSalt() {
	return salt;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public void setSalt(String salt) {
	this.salt = salt;
    }

    public Boolean isTwoFactorAuthenticationEnabled() {
	return twoFactorAuthenticationEnabled;
    }

    public void setTwoFactorAuthenticationEnabled(Boolean twoFactorAuthenticationEnabled) {
	this.twoFactorAuthenticationEnabled = twoFactorAuthenticationEnabled;
    }

    public String getTwoFactorAuthenticationSecret() {
	return twoFactorAuthenticationSecret;
    }

    public void setTwoFactorAuthenticationSecret(String twoFactorAuthenticationSecret) {
	this.twoFactorAuthenticationSecret = twoFactorAuthenticationSecret;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getFullName() {
	return this.getFirstName() + " " + this.getLastName();
    }

    public String getAddressLine1() {
	return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
	this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
	return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
	this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
	return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
	this.addressLine3 = addressLine3;
    }

    public String getCity() {
	return city;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public String getState() {
	return state;
    }

    public void setState(String state) {
	this.state = state;
    }

    public String getPostcode() {
	return postcode;
    }

    public void setPostcode(String postcode) {
	this.postcode = postcode;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public String getDayPhone() {
	return dayPhone;
    }

    public void setDayPhone(String dayPhone) {
	this.dayPhone = dayPhone;
    }

    public String getEveningPhone() {
	return eveningPhone;
    }

    public void setEveningPhone(String eveningPhone) {
	this.eveningPhone = eveningPhone;
    }

    public String getMobilePhone() {
	return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
	this.mobilePhone = mobilePhone;
    }

    public String getFax() {
	return fax;
    }

    public void setFax(String fax) {
	this.fax = fax;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public Boolean getDisabledFlag() {
	return disabledFlag;
    }

    public void setDisabledFlag(Boolean disabledFlag) {
	this.disabledFlag = disabledFlag;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public WorkspaceFolder getWorkspaceFolder() {
	return workspaceFolder;
    }

    public void setWorkspaceFolder(WorkspaceFolder workspace) {
	this.workspaceFolder = workspace;
    }

    public AuthenticationMethod getAuthenticationMethod() {
	return authenticationMethod;
    }

    public void setAuthenticationMethod(AuthenticationMethod authenticationMethod) {
	this.authenticationMethod = authenticationMethod;
    }

    public Theme getTheme() {
	return theme;
    }

    public void setTheme(Theme theme) {
	this.theme = theme;
    }

    public Set getUserOrganisations() {
	return userOrganisations;
    }

    public void setUserOrganisations(Set userOrganisations) {
	this.userOrganisations = userOrganisations;
    }

    public Integer getLastVisitedOrganisationId() {
	return lastVisitedOrganisationId;
    }

    public void setLastVisitedOrganisationId(Integer lastVisitedOrganisationId) {
	this.lastVisitedOrganisationId = lastVisitedOrganisationId;
    }

    /** This methods adds a new membership for the given user */
    public void addUserOrganisation(UserOrganisation userOrganisation) {
	if (userOrganisations == null) {
	    userOrganisations = new HashSet();
	}
	userOrganisations.add(userOrganisation);
    }

    public Set getLearnerProgresses() {
	return learnerProgresses;
    }

    public void setLearnerProgresses(Set learnerProgresses) {
	this.learnerProgresses = learnerProgresses;
    }

    public Set getUserToolSessions() {
	return userToolSessions;
    }

    public void setUserToolSessions(Set userToolSessions) {
	this.userToolSessions = userToolSessions;
    }

    public Set getLearningDesigns() {
	return learningDesigns;
    }

    public void setLearningDesigns(Set learningDesigns) {
	this.learningDesigns = learningDesigns;
    }

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

    @Override
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

	TimeZone timeZone = TimeZone.getTimeZone(getTimeZone());

	Set<String> tutorialPages = (pagesWithDisabledTutorials == null) || pagesWithDisabledTutorials.isEmpty() ? null
		: pagesWithDisabledTutorials;

	return new UserDTO(userId, firstName, lastName, login, languageIsoCode, countryIsoCode, direction, email,
		new ThemeDTO(theme),
		// TimeZone.getTimeZone("Australia/Sydney"),
		timeZone, authenticationMethod.getAuthenticationMethodId(), fckLanguageMapping, lamsCommunityToken,
		lamsCommunityUsername, 
		(tutorialsDisabled == null ? false : true), // assume tutorials enabled if not set
		tutorialPages, 
		(firstLogin == null ? true : false), // assume no firstLogin value means they haven't logged in
		lastVisitedOrganisationId
	);
    }

    public UserBasicDTO getUserBasicDTO() {
	return new UserBasicDTO(userId, firstName, lastName, login);
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
	for (UserOrganisation userOrganisation : userOrganisations) {
	    // not all orgs have a folder
	    Set<WorkspaceFolder> folders = userOrganisation.getOrganisation().getWorkspaceFolders();
	    if (folders != null) {
		if (checkFolders(folders, workspaceFolder.getWorkspaceFolderId())) {
		    return true;
		}
	    }
	}
	return false;
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

    public SupportedLocale getLocale() {
	return locale;
    }

    public void setLocale(SupportedLocale locale) {
	this.locale = locale;
    }

    public Long getPortraitUuid() {
	return portraitUuid;
    }

    public void setPortraitUuid(Long portraitUuid) {
	this.portraitUuid = portraitUuid;
    }

    public Boolean getChangePassword() {
	return changePassword;
    }

    public void setChangePassword(Boolean changePassword) {
	this.changePassword = changePassword;
    }

    public String getLamsCommunityToken() {
	return lamsCommunityToken;
    }

    public void setLamsCommunityToken(String lamsCommunityToken) {
	this.lamsCommunityToken = lamsCommunityToken;
    }

    public String getLamsCommunityUsername() {
	return lamsCommunityUsername;
    }

    public void setLamsCommunityUsername(String lamsCommunityUsername) {
	this.lamsCommunityUsername = lamsCommunityUsername;
    }

    public String getTimeZone() {
	if (timeZone == null) {
	    timeZone = TimeZone.getDefault().getID();
	}
	return timeZone;
    }

    public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
    }

    public Boolean getTutorialsDisabled() {
	return tutorialsDisabled;
    }

    public void setTutorialsDisabled(Boolean tutorialsDisabled) {
	this.tutorialsDisabled = tutorialsDisabled;
    }

    public Set<String> getPagesWithDisabledTutorials() {
	return pagesWithDisabledTutorials;
    }

    public void setPagesWithDisabledTutorials(Set<String> pagesWithDisabledTutorials) {
	this.pagesWithDisabledTutorials = pagesWithDisabledTutorials;
    }

    public Boolean isFirstLogin() {
	return firstLogin;
    }

    public void setFirstLogin(Boolean firstLogin) {
	this.firstLogin = firstLogin;
    }

    public Set<Long> getRecentlyModifiedLearningDesigns() {
	return recentlyModifiedLearningDesigns;
    }

    public void setRecentlyModifiedLearningDesigns(Set<Long> recentlyModifiedLearningDesigns) {
	this.recentlyModifiedLearningDesigns = recentlyModifiedLearningDesigns;
    }

    public Date getModifiedDate() {
	return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
	this.modifiedDate = modifiedDate;
    }

    public Integer getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(Integer failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public Date getLockOutTime() {
	return lockOutTime;
    }

    public void setLockOutTime(Date lockOutTime) {
	this.lockOutTime = lockOutTime;
    }
}