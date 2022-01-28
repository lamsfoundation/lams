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

import org.apache.commons.lang.StringUtils;

/**
 * Form for managing LTI tool consumers (ExtServer instances).
 *
 * @author Andrey Balan
 */
public class LtiConsumerForm extends ExtServerCommonForm {

    private String ltiToolConsumerMonitorRoles;

    private String userIdParameterName;

    private boolean useCoursePrefix;

    private boolean userRegistrationEnabled;

    private boolean userNameLowerCase;

    private boolean enforceStateCookie;

    private boolean toolReregistrationEnabled;

    private String issuer;

    private String clientId;

    private String platformKeySetUrl;

    private String oidcAuthUrl;

    private String accessTokenUrl;

    private String toolName;

    private String toolDescription;

    private String toolKeySetUrl;

    private String toolKeyId;

    private String publicKey;

    private String privateKey;

    public LtiConsumerForm() {
	userIdParameterName = "user_id";
	toolReregistrationEnabled = true;
	useCoursePrefix = true;
	userRegistrationEnabled = true;
	toolName = "LAMS Advantage";
	toolDescription = "platform for delivering online collaborative learning activities";
    }

    public String getLtiToolConsumerMonitorRoles() {
	return ltiToolConsumerMonitorRoles;
    }

    public void setLtiToolConsumerMonitorRoles(String ltiToolConsumerMonitorRoles) {
	this.ltiToolConsumerMonitorRoles = ltiToolConsumerMonitorRoles;
    }

    public String getUserIdParameterName() {
	return userIdParameterName;
    }

    public void setUserIdParameterName(String userIdParameterName) {
	this.userIdParameterName = StringUtils.trim(userIdParameterName);
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