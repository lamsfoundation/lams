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

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookSession;

public class NotebookDTO {

    public Long toolContentId;

    public String title;

    public String instructions;

    public boolean contentInUse;

    public boolean allowRichEditor;

    public boolean lockOnFinish;
    
    public boolean forceResponse;

    public Date submissionDeadline;

    public Set<NotebookSessionDTO> sessionDTOs = new TreeSet<NotebookSessionDTO>();

    /* Constructors */
    public NotebookDTO() {
    }

    public NotebookDTO(Notebook notebook) {
	toolContentId = notebook.getToolContentId();
	title = notebook.getTitle();
	instructions = notebook.getInstructions();
	contentInUse = notebook.isContentInUse();
	allowRichEditor = notebook.isAllowRichEditor();
	lockOnFinish = notebook.isLockOnFinished();
	forceResponse = notebook.isForceResponse();

	for (Iterator iter = notebook.getNotebookSessions().iterator(); iter.hasNext();) {
	    NotebookSession session = (NotebookSession) iter.next();
	    NotebookSessionDTO sessionDTO = new NotebookSessionDTO(session);

	    sessionDTOs.add(sessionDTO);
	}
    }

    /* Getters / Setters */
    public Set<NotebookSessionDTO> getSessionDTOs() {
	return sessionDTOs;
    }

    public void setSessionDTOs(Set<NotebookSessionDTO> sessionDTOs) {
	this.sessionDTOs = sessionDTOs;
    }

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
    
    public boolean isForceResponse() {
        return forceResponse;
    }

    public void setForceResponse(boolean forceResponse) {
        this.forceResponse = forceResponse;
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
}