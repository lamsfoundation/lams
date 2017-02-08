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

package org.lamsfoundation.lams.usermanagement.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.themes.dto.ThemeDTO;

/**
 * @author Manpreet Minhas
 */
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 5299784226270953691L;

    private Integer userID;
    private String firstName;
    private String lastName;
    private String login;
    private String localeLanguage;
    private String localeCountry;
    private String fckLanguageMapping;
    private String direction;
    private String email;
    private ThemeDTO theme;
    private TimeZone timeZone;
    private Integer authenticationMethodId;
    private Boolean tutorialsDisabled;
    private Set<String> pagesWithDisabledTutorials;
    private Boolean firstLogin;
    private Integer lastVisitedOrganisationId;

    public UserDTO(Integer userID, String firstName, String lastName, String login, String localeLanguage,
	    String localeCountry, String direction, String email, ThemeDTO htmlTheme, TimeZone timezone,
	    Integer authenticationMethodId, String fckLanguageMapping, Boolean tutorialsDisabled,
	    Set<String> pagesWithDisabledTutorials, Boolean firstLogin, Integer lastVisitedOrganisationId) {
	this.userID = userID;
	this.firstName = firstName;
	this.lastName = lastName;
	this.login = login;
	this.localeCountry = localeCountry;
	this.localeLanguage = localeLanguage;
	this.direction = direction;
	this.email = email;
	this.theme = htmlTheme;
	timeZone = timezone;
	this.authenticationMethodId = authenticationMethodId;
	this.fckLanguageMapping = fckLanguageMapping;
	this.tutorialsDisabled = tutorialsDisabled;
	this.pagesWithDisabledTutorials = pagesWithDisabledTutorials;
	this.firstLogin = firstLogin;
	this.lastVisitedOrganisationId = lastVisitedOrganisationId;
    }

    /**
     * Equality test of UserDTO objects
     */
    @Override
    public boolean equals(Object o) {
	if ((o != null) && (o.getClass() == this.getClass())) {
	    return ((UserDTO) o).userID == userID;
	} else {
	    return false;
	}
    }

    /**
     * Returns the hash code value for this object.
     */
    @Override
    public int hashCode() {
	// TODO this might be an ineffcient implementation since userIDs are likely to be sequential,
	// hence we dont get a good spread of values
	return userID.intValue();
    }

    /**
     * @return Returns the firstName.
     */
    public String getFirstName() {
	return firstName;
    }

    /**
     * @return Returns the lastName.
     */
    public String getLastName() {
	return lastName;
    }

    /**
     * @return Returns the login.
     */
    public String getLogin() {
	return login;
    }

    /**
     * @return Returns the userID.
     */
    public Integer getUserID() {
	return userID;
    }

    public ThemeDTO getTheme() {
	return theme;
    }

    public void setTheme(ThemeDTO theme) {
	this.theme = theme;
    }

    public String getEmail() {
	return email;
    }

    public String getLocaleCountry() {
	return localeCountry;
    }

    public String getLocaleLanguage() {
	return localeLanguage;
    }

    /** Should the page be displayed left to right (LTR) or right to left (RTL) */
    public String getDirection() {
	return direction;
    }

    /** User's timezone. */
    public TimeZone getTimeZone() {
	return timeZone;
    }

    public Integer getAuthenticationMethodId() {
	return authenticationMethodId;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("userID", getUserID()).append("firstName", getFirstName())
		.append("lastName", getLastName()).append("login", getLogin())
		.append("localeLanguage", getLocaleLanguage()).append("localeCountry", getLocaleCountry())
		.append("direction", getDirection()).append("email", getEmail()).append("htmlTheme", getTheme())
		.append("timeZone", getTimeZone()).append("authenticationMethodId", getAuthenticationMethodId())
		.append("fckLanguageMapping", getFckLanguageMapping())
		.append("tutorialsDisabled", "" + getTutorialsDisabled()).toString();
    }

    public String getFckLanguageMapping() {
	return fckLanguageMapping;
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

    public Integer getLastVisitedOrganisationId() {
	return lastVisitedOrganisationId;
    }
}