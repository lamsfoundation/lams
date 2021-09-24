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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SortNatural;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.dto.ThemeDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserBasicDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.LanguageUtil;

@Entity
@Table(name = "lams_user")
public class User implements Serializable, Comparable<User> {
    private static final long serialVersionUID = 8711215689846731994L;

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column
    private String login;

    @Column
    private String password;

    @Column(name = "failed_attempts")
    private Integer failedAttempts;

    @Column(name = "lock_out_time")
    private Date lockOutTime;

    @Column(name = "two_factor_auth_enabled")
    private Boolean twoFactorAuthenticationEnabled;

    @Column(name = "two_factor_auth_secret")
    private String twoFactorAuthenticationSecret;

    @Column
    private String salt;

    @Column
    private String title;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address_line_1")
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "address_line_3")
    private String addressLine3;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String postcode;

    @Column
    private String country;

    @Column(name = "day_phone")
    private String dayPhone;

    @Column(name = "evening_phone")
    private String eveningPhone;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column
    private String fax;

    @Column
    private String email;

    @Column(name = "email_verified")
    private Boolean emailVerified = true;

    @Column(name = "disabled_flag")
    private Boolean disabledFlag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "locale_id")
    private SupportedLocale locale;

    @Column(name = "timezone")
    private String timeZone;

    @Column(name = "create_date")
    private Date createDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "workspace_folder_id")
    private WorkspaceFolder workspaceFolder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authentication_method_id")
    private AuthenticationMethod authenticationMethod;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserOrganisation> userOrganisations = new HashSet<>();

    @Column(name = "last_visited_organisation_id")
    private Integer lastVisitedOrganisationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<LearnerProgress> learnerProgresses = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<LearningDesign> learningDesigns = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Lesson> lessons = new HashSet<>();

    @Column(name = "portrait_uuid")
    private UUID portraitUuid;

    @Column(name = "password_change_date")
    private LocalDateTime passwordChangeDate;

    @Column(name = "change_password")
    private Boolean changePassword;

    /**
     * Contains a list of recently used passwords in form: password change date -> "old_hash=old_salt"
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "lams_user_password_history", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "change_date")
    @Column(name = "password")
    @SortNatural
    private SortedMap<LocalDateTime, String> passwordHistory = new TreeMap<>();

    @Column(name = "first_login")
    private Boolean firstLogin;

    @ElementCollection
    @JoinTable(name = "lams_planner_recent_learning_designs", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "learning_design_id")
    @OrderBy("learning_design_id")
    private Set<Long> recentlyModifiedLearningDesigns = new LinkedHashSet<>();

    @Column(name = "modified_date")
    private Date modifiedDate;

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

    public String getFullNameMonitoringStyle() {
	return new StringBuilder(this.getLastName()).append(", ").append(this.getFirstName()).toString();
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

    public Boolean getEmailVerified() {
	return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
	this.emailVerified = emailVerified;
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

    public Set<UserOrganisation> getUserOrganisations() {
	return userOrganisations;
    }

    public void setUserOrganisations(Set<UserOrganisation> userOrganisations) {
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
	userOrganisations.add(userOrganisation);
    }

    public Set<LearnerProgress> getLearnerProgresses() {
	return learnerProgresses;
    }

    public void setLearnerProgresses(Set<LearnerProgress> learnerProgresses) {
	this.learnerProgresses = learnerProgresses;
    }

    public Set<LearningDesign> getLearningDesigns() {
	return learningDesigns;
    }

    public void setLearningDesigns(Set<LearningDesign> learningDesigns) {
	this.learningDesigns = learningDesigns;
    }

    public Set<Lesson> getLessons() {
	return lessons;
    }

    public void setLessons(Set<Lesson> lessons) {
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
    public int compareTo(User user) {
	return login.compareTo(user.getLogin());
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

	return new UserDTO(userId, firstName, lastName, login, languageIsoCode, countryIsoCode, direction, email,
		theme != null ? new ThemeDTO(theme) : null, timeZone, authenticationMethod.getAuthenticationMethodId(),
		fckLanguageMapping, (firstLogin == null || firstLogin ? true : false), // assume no firstLogin value means they haven't logged in
		lastVisitedOrganisationId, portraitUuid == null ? null : portraitUuid.toString());
    }

    public UserBasicDTO getUserBasicDTO() {
	return new UserBasicDTO(userId, firstName, lastName, login);
    }

    /**
     * This method checks whether user is a member of the given organisation
     */
    public boolean isMember(Organisation organisation) {
	Iterator<UserOrganisation> iterator = userOrganisations.iterator();
	while (iterator.hasNext()) {
	    UserOrganisation userOrganisation = iterator.next();
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

    private boolean checkFolders(Set<WorkspaceFolder> folders, Integer desiredWorkspaceFolderId) {
	boolean foundMemberFolder = false;
	Iterator<WorkspaceFolder> folderIter = folders.iterator();
	while (folderIter.hasNext() && !foundMemberFolder) {
	    WorkspaceFolder folder = folderIter.next();
	    Integer folderID = folder.getWorkspaceFolderId();
	    if (folderID.equals(desiredWorkspaceFolderId)) {
		foundMemberFolder = true;
	    } else {
		Set<WorkspaceFolder> childFolders = folder.getChildWorkspaceFolders();
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

    public UUID getPortraitUuid() {
	return portraitUuid;
    }

    public void setPortraitUuid(UUID portraitUuid) {
	this.portraitUuid = portraitUuid;
    }

    public LocalDateTime getPasswordChangeDate() {
	return passwordChangeDate;
    }

    public void setPasswordChangeDate(LocalDateTime passwordChangeDate) {
	this.passwordChangeDate = passwordChangeDate;
    }

    public Boolean getChangePassword() {
	return changePassword;
    }

    public void setChangePassword(Boolean changePassword) {
	this.changePassword = changePassword;
    }

    public SortedMap<LocalDateTime, String> getPasswordHistory() {
	return passwordHistory;
    }

    public void setPasswordHistory(SortedMap<LocalDateTime, String> passwordHistory) {
	this.passwordHistory = passwordHistory;
    }

    public String getTimeZone() {
	if (timeZone == null) {
	    return TimeZone.getDefault().getID();
	}
	return timeZone;
    }

    public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
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