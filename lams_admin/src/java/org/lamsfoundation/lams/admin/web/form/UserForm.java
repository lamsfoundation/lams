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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.admin.web.form;

import java.util.Date;

public class UserForm {

    private Integer orgId;
    private Integer userId;
    private String login;
    private String password;
    private String password2;
    private String changePassword;
    private Integer authenticationMethodId;
    private String title;
    private String firstName;
    private String lastName;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String city;
    private String state;
    private String postcode;
    private String country;
    private String dayPhone;
    private String eveningPhone;
    private String mobilePhone;
    private String fax;
    private String email;
    private Integer localeId;
    private boolean tutorialsDisabled = false;
    private boolean firstLogin = true;
    private String timeZone;
    private Date createDate;
    private Long userTheme;
    private boolean twoFactorAuthenticationEnabled = false;
    // <!-- Name different to real field to avoid overwriting in bean copies -->
    private String initialPortraitId = null;

    public Integer getOrgId() {
	return orgId;
    }

    public void setOrgId(Integer orgId) {
	this.orgId = orgId;
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

    public void setPassword(String password) {
	this.password = password;
    }

    public String getPassword2() {
	return password2;
    }

    public void setPassword2(String password2) {
	this.password2 = password2;
    }

    public String getChangePassword() {
	return changePassword;
    }

    public void setChangePassword(String changePassword) {
	this.changePassword = changePassword;
    }

    public Integer getAuthenticationMethodId() {
	return authenticationMethodId;
    }

    public void setAuthenticationMethodId(Integer authenticationMethodId) {
	this.authenticationMethodId = authenticationMethodId;
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

    public Integer getLocaleId() {
	return localeId;
    }

    public void setLocaleId(Integer localeId) {
	this.localeId = localeId;
    }

    public boolean isTutorialsDisabled() {
	return tutorialsDisabled;
    }

    public void setTutorialsDisabled(boolean tutorialsDisabled) {
	this.tutorialsDisabled = tutorialsDisabled;
    }

    public boolean isFirstLogin() {
	return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
	this.firstLogin = firstLogin;
    }

    public String getTimeZone() {
	return timeZone;
    }

    public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public Long getUserTheme() {
	return userTheme;
    }

    public void setUserTheme(Long userTheme) {
	this.userTheme = userTheme;
    }

    public boolean isTwoFactorAuthenticationEnabled() {
	return twoFactorAuthenticationEnabled;
    }

    public void setTwoFactorAuthenticationEnabled(boolean twoFactorAuthenticationEnabled) {
	this.twoFactorAuthenticationEnabled = twoFactorAuthenticationEnabled;
    }

    public String getInitialPortraitId() {
	return initialPortraitId;
    }

    public void setInitialPortraitId(String initialPortraitId) {
	this.initialPortraitId = initialPortraitId;
    }

}
