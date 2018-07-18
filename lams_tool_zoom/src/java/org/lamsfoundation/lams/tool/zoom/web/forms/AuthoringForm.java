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

package org.lamsfoundation.lams.tool.zoom.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 *
 */
public class AuthoringForm extends ActionForm {
    private static final long serialVersionUID = 3950453134542135495L;

    // Fields

    private String title;

    private String instructions;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    private boolean startInMonitor;

    private Integer duration;

    private String currentTab;

    private String dispatch;

    private String sessionMapID;

    private SessionMap<String, Object> sessionMap;

    @Override
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
	ActionErrors ac = new ActionErrors();
	ac.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("this is an error"));

	return ac;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(String currentTab) {
	this.currentTab = currentTab;
    }

    public String getDispatch() {
	return dispatch;
    }

    public void setDispatch(String dispatch) {
	this.dispatch = dispatch;
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    public boolean isStartInMonitor() {
	return startInMonitor;
    }

    public void setStartInMonitor(boolean startInMonitor) {
	this.startInMonitor = startInMonitor;
    }

    public Integer getDuration() {
	return duration;
    }

    public void setDuration(Integer duration) {
	this.duration = duration;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public void setSessionMap(SessionMap<String, Object> sessionMap) {
	this.sessionMap = sessionMap;
    }

    public SessionMap<String, Object> getSessionMap() {
	return sessionMap;
    }
}