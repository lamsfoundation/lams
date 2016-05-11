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



package org.lamsfoundation.lams.tool.scribe.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

/**
 *
 */
public class AuthoringForm extends ActionForm {

    private static final long serialVersionUID = 3950453134542135495L;

    // Properties

    String title;

    String instructions;

    String offlineInstruction;

    String onlineInstruction;

    boolean reflectOnActivity;

    String reflectInstructions;

    boolean autoSelectScribe;

    boolean showAggregatedReports;

    FormFile onlineFile;

    FormFile offlineFile;

    String currentTab;

    String dispatch;

    String sessionMapID;

    Long deleteFileUuid;

    // Properties for AddHeading.jsp page

    String heading;
    Integer headingIndex;

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

    public FormFile getOfflineFile() {
	return offlineFile;
    }

    public void setOfflineFile(FormFile offlineFile) {
	this.offlineFile = offlineFile;
    }

    public String getOfflineInstruction() {
	return offlineInstruction;
    }

    public void setOfflineInstruction(String offlineInstruction) {
	this.offlineInstruction = offlineInstruction;
    }

    public FormFile getOnlineFile() {
	return onlineFile;
    }

    public void setOnlineFile(FormFile onlineFile) {
	this.onlineFile = onlineFile;
    }

    public String getOnlineInstruction() {
	return onlineInstruction;
    }

    public void setOnlineInstruction(String onlineInstruction) {
	this.onlineInstruction = onlineInstruction;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Long getDeleteFileUuid() {
	return deleteFileUuid;
    }

    public void setDeleteFileUuid(Long deleteFile) {
	this.deleteFileUuid = deleteFile;
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

    public boolean isAutoSelectScribe() {
	return autoSelectScribe;
    }

    public void setAutoSelectScribe(boolean autoSelectScribe) {
	this.autoSelectScribe = autoSelectScribe;
    }

    public String getHeading() {
	return heading;
    }

    public void setHeading(String heading) {
	this.heading = heading;
    }

    public Integer getHeadingIndex() {
	return headingIndex;
    }

    public void setHeadingIndex(Integer headingIndex) {
	this.headingIndex = headingIndex;
    }

    public boolean isShowAggregatedReports() {
	return showAggregatedReports;
    }

    public void setShowAggregatedReports(boolean showAggregatedReports) {
	this.showAggregatedReports = showAggregatedReports;
    }
}