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

public class LtiConsumerForm {

    private Integer sid;

    private String serverid;

    private String serverkey;

    private String servername;

    private String serverdesc;

    private String prefix;

    private boolean disabled = false;

    private String lessonFinishUrl;

    private boolean timeToLiveLoginRequestEnabled = false;

    private String ltiToolConsumerMonitorRoles;

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

    public boolean isDisabled() {
	return disabled;
    }

    public void setDisabled(boolean disabled) {
	this.disabled = disabled;
    }

    public String getLessonFinishUrl() {
	return lessonFinishUrl;
    }

    public void setLessonFinishUrl(String lessonFinishUrl) {
	this.lessonFinishUrl = lessonFinishUrl;
    }

    public boolean isTimeToLiveLoginRequestEnabled() {
	return timeToLiveLoginRequestEnabled;
    }

    public void setTimeToLiveLoginRequestEnabled(boolean timeToLiveLoginRequestEnabled) {
	this.timeToLiveLoginRequestEnabled = timeToLiveLoginRequestEnabled;
    }

    public String getLtiToolConsumerMonitorRoles() {
	return ltiToolConsumerMonitorRoles;
    }

    public void setLtiToolConsumerMonitorRoles(String ltiToolConsumerMonitorRoles) {
	this.ltiToolConsumerMonitorRoles = ltiToolConsumerMonitorRoles;
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
