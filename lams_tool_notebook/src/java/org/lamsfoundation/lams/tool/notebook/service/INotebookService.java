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

package org.lamsfoundation.lams.tool.notebook.service;

import java.util.Collection;
import java.util.List;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.notebook.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookCondition;
import org.lamsfoundation.lams.tool.notebook.model.NotebookSession;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.tool.service.ICommonToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Notebook Service
 */
public interface INotebookService extends ICommonToolService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     *
     * @params newContentID
     * @return
     */
    Notebook copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Notebook tools default content.
     *
     * @return
     */
    Notebook getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    Notebook getNotebookByContentId(Long toolContentID);

    /**
     * @param notebook
     */
    void saveOrUpdateNotebook(Notebook notebook);

    /**
     * @param toolSessionId
     * @return
     */
    NotebookSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param notebookSession
     */
    void saveOrUpdateNotebookSession(NotebookSession notebookSession);

    /**
     *
     * @param userId
     * @param toolSessionId
     * @return
     */
    NotebookUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     *
     * @param uid
     * @return
     */
    NotebookUser getUserByUID(Long uid);

    /**
     *
     * @param notebookUser
     */
    void saveOrUpdateNotebookUser(NotebookUser notebookUser);

    /**
     *
     * @param user
     * @param notebookSession
     * @return
     */
    NotebookUser createNotebookUser(UserDTO user, NotebookSession notebookSession);

    boolean notifyUser(Integer userId, String comment);

    String finishToolSession(NotebookUser notebookUser, Boolean isContentEditable, String entryText);

    /**
     *
     * @param uid
     * @return
     */
    NotebookEntry getEntry(Long uid);

    /**
     * Creates an unique name for a ChatCondition. It consists of the tool output definition name and a unique positive
     * integer number.
     *
     * @param existingConditions
     *            existing conditions; required to check if a condition with the same name does not exist.
     * @return unique ChatCondition name
     */
    String createConditionName(Collection<NotebookCondition> existingConditions);

    void releaseConditionsFromCache(Notebook notebook);

    void deleteCondition(NotebookCondition condition);

    /**
     * Return content folder (unique to each learner and lesson) which is used for storing user generated content.
     * It's been used by CKEditor.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    String getLearnerContentFolder(Long toolSessionId, Long userId);

    /**
     * Will return List<[NotebookUser, String, Date]> where the String is the notebook entry and the modified date.
     */
    List<Object[]> getUsersEntriesDates(final Long sessionId, Integer page, Integer size, int sorting,
	    String searchString);

    int getCountUsersBySession(final Long sessionId, String searchString);

    /** Get the statistics for monitoring */
    List<StatisticDTO> getStatisticsBySession(final Long contentId);

}