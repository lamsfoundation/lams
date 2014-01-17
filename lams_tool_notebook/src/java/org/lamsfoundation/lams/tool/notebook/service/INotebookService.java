/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.notebook.service;

import java.util.Collection;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookCondition;
import org.lamsfoundation.lams.tool.notebook.model.NotebookSession;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.tool.notebook.util.NotebookException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Notebook Service
 */
public interface INotebookService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     * 
     * @params newContentID
     * @return
     */
    public Notebook copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Notebook tools default content.
     * 
     * @return
     */
    public Notebook getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public Notebook getNotebookByContentId(Long toolContentID);

    /**
     * @param notebook
     */
    public void saveOrUpdateNotebook(Notebook notebook);

    /**
     * @param toolSessionId
     * @return
     */
    public NotebookSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param notebookSession
     */
    public void saveOrUpdateNotebookSession(NotebookSession notebookSession);

    /**
     * 
     * @param userId
     * @param toolSessionId
     * @return
     */
    public NotebookUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     * 
     * @param uid
     * @return
     */
    public NotebookUser getUserByUID(Long uid);

    /**
     * 
     * @param notebookUser
     */
    public void saveOrUpdateNotebookUser(NotebookUser notebookUser);

    /**
     * 
     * @param user
     * @param notebookSession
     * @return
     */
    public NotebookUser createNotebookUser(UserDTO user, NotebookSession notebookSession);

    /**
     * 
     * @param id
     * @param idType
     * @param signature
     * @param userID
     * @param title
     * @param entry
     * @return
     */
    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    /**
     * 
     * @param uid
     * @return
     */
    NotebookEntry getEntry(Long uid);

    /**
     * 
     * @param uid
     * @param title
     * @param entry
     */
    void updateEntry(Long uid, String entry);

    /**
     * Creates an unique name for a ChatCondition. It consists of the tool output definition name and a unique positive
     * integer number.
     * 
     * @param existingConditions
     *                existing conditions; required to check if a condition with the same name does not exist.
     * @return unique ChatCondition name
     */
    String createConditionName(Collection<NotebookCondition> existingConditions);

    void releaseConditionsFromCache(Notebook notebook);

    void deleteCondition(NotebookCondition condition);
    
    boolean isGroupedActivity(long toolContentID);
    
    /**
     * Return content folder (unique to each learner and lesson) which is used for storing user generated content.
     * It's been used by CKEditor.
     * 
     * @param toolSessionId
     * @param userId
     * @return
     */
    String getLearnerContentFolder(Long toolSessionId, Long userId);

}