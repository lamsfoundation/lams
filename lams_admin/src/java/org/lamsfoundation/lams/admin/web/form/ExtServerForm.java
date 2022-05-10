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

/**
 * Form for managing ExtServers.
 */
public class ExtServerForm extends ExtServerCommonForm {

    private String userinfoUrl;

    private String extGroupsUrl;

    private String logoutUrl;

    private boolean timeToLiveLoginRequestEnabled = true;

    private Integer timeToLiveLoginRequest = 80;

    private boolean addStaffToAllLessons = false;

    public String getUserinfoUrl() {
	return userinfoUrl;
    }

    public void setUserinfoUrl(String userinfoUrl) {
	this.userinfoUrl = userinfoUrl;
    }

    public String getExtGroupsUrl() {
	return extGroupsUrl;
    }

    public void setExtGroupsUrl(String extGroupsUrl) {
	this.extGroupsUrl = extGroupsUrl;
    }

    public String getLogoutUrl() {
	return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
	this.logoutUrl = logoutUrl;
    }

    public boolean isTimeToLiveLoginRequestEnabled() {
	return timeToLiveLoginRequestEnabled;
    }

    public void setTimeToLiveLoginRequestEnabled(boolean timeToLiveLoginRequestEnabled) {
	this.timeToLiveLoginRequestEnabled = timeToLiveLoginRequestEnabled;
    }

    public Integer getTimeToLiveLoginRequest() {
	return timeToLiveLoginRequest;
    }

    public void setTimeToLiveLoginRequest(Integer timeToLiveLoginRequest) {
	this.timeToLiveLoginRequest = timeToLiveLoginRequest;
    }

    public boolean isAddStaffToAllLessons() {
	return addStaffToAllLessons;
    }

    public void setAddStaffToAllLessons(boolean addStaffToAllLessons) {
	this.addStaffToAllLessons = addStaffToAllLessons;
    }
}