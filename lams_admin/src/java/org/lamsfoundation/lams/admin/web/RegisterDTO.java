/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.admin.web;

public class RegisterDTO {

    int groupNumber;
    int subgroupNumber;
    int sysadminNumber;
    int adminNumber;
    int authorNumber;
    int managerNumber;
    int monitorNumber;
    int learnerNumber;
    int authorAdminNumber;
    int userNumber;
    String serverUrl;
    String serverVersion;
    String serverBuild;
    String serverLocale;
    String serverLanguageDate;

    public RegisterDTO() {
    }

    public int getGroupNumber() {
	return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
	this.groupNumber = groupNumber;
    }

    public int getSubgroupNumber() {
	return subgroupNumber;
    }

    public void setSubgroupNumber(int subgroupNumber) {
	this.subgroupNumber = subgroupNumber;
    }

    public int getSysadminNumber() {
	return sysadminNumber;
    }

    public void setSysadminNumber(int sysadminNumber) {
	this.sysadminNumber = sysadminNumber;
    }

    public int getAdminNumber() {
	return adminNumber;
    }

    public void setAdminNumber(int adminNumber) {
	this.adminNumber = adminNumber;
    }

    public int getAuthorNumber() {
	return authorNumber;
    }

    public void setAuthorNumber(int authorNumber) {
	this.authorNumber = authorNumber;
    }

    public int getManagerNumber() {
	return managerNumber;
    }

    public void setManagerNumber(int managerNumber) {
	this.managerNumber = managerNumber;
    }

    public int getMonitorNumber() {
	return monitorNumber;
    }

    public void setMonitorNumber(int monitorNumber) {
	this.monitorNumber = monitorNumber;
    }

    public int getLearnerNumber() {
	return learnerNumber;
    }

    public void setLearnerNumber(int learnerNumber) {
	this.learnerNumber = learnerNumber;
    }

    public int getAuthorAdminNumber() {
	return authorAdminNumber;
    }

    public void setAuthorAdminNumber(int authorAdminNumber) {
	this.authorAdminNumber = authorAdminNumber;
    }

    public int getUserNumber() {
	return userNumber;
    }

    public void setUserNumber(int userNumber) {
	this.userNumber = userNumber;
    }

    public String getServerUrl() {
	return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
	this.serverUrl = serverUrl;
    }

    public String getServerVersion() {
	return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
	this.serverVersion = serverVersion;
    }

    public String getServerBuild() {
	return serverBuild;
    }

    public void setServerBuild(String serverBuild) {
	this.serverBuild = serverBuild;
    }

    public String getServerLocale() {
	return serverLocale;
    }

    public void setServerLocale(String serverLocale) {
	this.serverLocale = serverLocale;
    }

    public String getServerLanguageDate() {
	return serverLanguageDate;
    }

    public void setServerLanguageDate(String serverLanguageDate) {
	this.serverLanguageDate = serverLanguageDate;
    }
}
