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

public class ExtServerForm {

    private Integer sid = -1;

    private String serverid;

    private String serverkey;

    private String servername;

    private String serverdesc;

    private String prefix;

    private String userinfoUrl;

    private String lessonFinishUrl;

    private String extGroupsUrl;

    private boolean disabled = false;

    private boolean timeToLiveLoginRequestEnabled = true;

    private Integer timeToLiveLoginRequest = 80;

    private String requiredField;
    
    private String uniqueField;
    
    public Integer getSid() {
	return sid;
    }

    public void setSid(Integer sid) {
	this.sid = sid;
    }

    public String getServerid() {
	return serverid;
    }

    public void setServerid(String serverid) {
	this.serverid = serverid;
    }

    public String getServerkey() {
	return serverkey;
    }

    public void setServerkey(String serverkey) {
	this.serverkey = serverkey;
    }

    public String getServername() {
	return servername;
    }

    public void setServername(String servername) {
	this.servername = servername;
    }

    public String getServerdesc() {
	return serverdesc;
    }

    public void setServerdesc(String serverdesc) {
	this.serverdesc = serverdesc;
    }

    public String getPrefix() {
	return prefix;
    }

    public void setPrefix(String prefix) {
	this.prefix = prefix;
    }

    public String getUserinfoUrl() {
	return userinfoUrl;
    }

    public void setUserinfoUrl(String userinfoUrl) {
	this.userinfoUrl = userinfoUrl;
    }

    public String getLessonFinishUrl() {
	return lessonFinishUrl;
    }

    public void setLessonFinishUrl(String lessonFinishUrl) {
	this.lessonFinishUrl = lessonFinishUrl;
    }

    public String getExtGroupsUrl() {
	return extGroupsUrl;
    }

    public void setExtGroupsUrl(String extGroupsUrl) {
	this.extGroupsUrl = extGroupsUrl;
    }

    public boolean isDisabled() {
	return disabled;
    }

    public void setDisabled(boolean disabled) {
	this.disabled = disabled;
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

    public String getRequiredField() {
        return requiredField;
    }

    public void setRequiredField(String requiredField) {
        this.requiredField = requiredField;
    }

    public String getUniqueField() {
        return uniqueField;
    }

    public void setUniqueField(String uniqueField) {
        this.uniqueField = uniqueField;
    }

}
