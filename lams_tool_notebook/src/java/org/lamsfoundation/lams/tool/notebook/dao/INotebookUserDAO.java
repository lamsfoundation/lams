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

package org.lamsfoundation.lams.tool.notebook.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.notebook.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

/**
 * DAO for accessing the NotebookUser objects - interface defining
 * methods to be implemented by the Hibernate or other implementation.
 */
public interface INotebookUserDAO extends IBaseDAO {
    /**
     *
     * @param userId
     * @param toolSessionId
     * @return
     */
    NotebookUser getByUserIdAndSessionId(Long userId, Long toolSessionId);

    void saveOrUpdate(NotebookUser notebookUser);

    /**
     *
     * @param loginName
     * @param sessionID
     * @return
     */
    NotebookUser getByLoginNameAndSessionId(String loginName, Long toolSessionId);

    /**
     *
     * @param uid
     * @return
     */
    NotebookUser getByUID(Long uid);

    /**
     * Will return List<[NotebookUser, String, Date]> where the String is the notebook entry and the modified date.
     */
    List<Object[]> getUsersEntriesDates(final Long sessionId, Integer page, Integer size, int sorting,
	    String searchString, ICoreNotebookService coreNotebookService,
	    IUserManagementService userManagementService);

    int getCountUsersBySession(final Long sessionId, String searchString);

    /** Get the statistics for monitoring */
    List<StatisticDTO> getStatisticsBySession(final Long contentId);
}