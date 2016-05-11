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



package org.lamsfoundation.lams.tool.notebook.dto;

/** Contains just the basic information for a notebook and it's sessions. It does not contain any user information. Used for the paging version of monitoring */

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookSession;

public class NotebookSessionsDTO {

    public Long toolContentId;

    public String title;

    public String instructions;

    public boolean contentInUse;

    public boolean allowRichEditor;

    public boolean lockOnFinish;

    public Date submissionDeadline;

    public Map<Long, String> sessions;

    public Long currentTab;

    /* Constructors */
    @SuppressWarnings("rawtypes")
    public NotebookSessionsDTO(Notebook notebook) {
	toolContentId = notebook.getToolContentId();
	title = notebook.getTitle();
	instructions = notebook.getInstructions();
	contentInUse = notebook.isContentInUse();
	allowRichEditor = notebook.isAllowRichEditor();
	lockOnFinish = notebook.isLockOnFinished();

	sessions = new TreeMap<Long, String>();
	for (Iterator iter = notebook.getNotebookSessions().iterator(); iter.hasNext();) {
	    NotebookSession session = (NotebookSession) iter.next();
	    sessions.put(session.getSessionId(), session.getSessionName());
	}
    }

    /* Getters / Setters */
    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentID) {
	this.toolContentId = toolContentID;
    }

    public Boolean getContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(Boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    public boolean isAllowRichEditor() {
	return allowRichEditor;
    }

    public void setAllowRichEditor(boolean allowRichEditor) {
	this.allowRichEditor = allowRichEditor;
    }

    public boolean isLockOnFinish() {
	return lockOnFinish;
    }

    public void setLockOnFinish(boolean lockOnFinish) {
	this.lockOnFinish = lockOnFinish;
    }

    /**
     * @return the submissionDeadline
     */
    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    /**
     * @param submissionDeadline
     *            the submissionDeadline to set
     */
    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    public Long getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(Long currentTab) {
	this.currentTab = currentTab;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    public void setSessions(Map<Long, String> sessions) {
	this.sessions = sessions;
    }

    public Map<Long, String> getSessions() {
	return sessions;
    }
}