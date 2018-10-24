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


package org.lamsfoundation.lams.tool.noticeboard.dao;

import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardUser;

/**
 * <p>
 * Interface for the NoticeboardSession DAO, defines methods needed to access/modify noticeboard session
 * </p>
 *
 * @author mtruong
 */
public interface INoticeboardSessionDAO {

    /**
     * <p>
     * Return the persistent instance of a NoticeboardSession with the given tool session id <code>nbSessionId</code>,
     * returns null if not found.
     * </p>
     *
     * @param nbSessionId
     *            The tool session id
     * @return the persistent instance of a NoticeboardSession or null if not found.
     */
    public NoticeboardSession findNbSessionById(Long nbSessionId);

    /**
     * <p>
     * Persist the given persistent instance of NoticeboardSession.
     * </p>
     *
     * @param nbSession
     *            The instance of NoticeboardSession to persist.
     */
    public void saveNbSession(NoticeboardSession nbSession);

    /**
     * <p>
     * Update the given persistent instance of NoticeboardSession.
     * </p>
     *
     * @param nbContent
     *            The instance of NoticeboardSession to persist.
     */
    public void updateNbSession(NoticeboardSession nbSession);

    /**
     * <p>
     * Delete the given instance of NoticeboardSession
     * </p>
     *
     * @param nbSession
     *            The instance of NoticeboardSession to delete.
     */
    public void removeNbSession(NoticeboardSession nbSession);

    /**
     * <p>
     * Delete the given instance of NoticeboardSession with the given tool session id <code>nbSessionid</code>
     *
     * @param nbSessionId
     *            The tool session Id.
     */
    public void removeNbSession(Long nbSessionId);

    /**
     * <p>
     * Returns the persistent instance of NoticeboardSession associated with the given noticeboard user, with user id
     * <code>userId</code>, returns null if not found.
     *
     * @param userId
     *            The noticeboard user id
     * @return a persistent instance of NoticeboardSessions or null if not found.
     */
    public NoticeboardSession getNbSessionByUser(Long userId);

    /**
     * <p>
     * Deletes all instances of NoticeboardUser that are associated with the given instance of NoticeboardSession
     * </p>
     *
     * @param nbSession
     *            The instance of NoticeboardSession in which corresponding instances of NoticeboardUser should be
     *            deleted.
     */
    public void removeNbUsers(NoticeboardSession nbSession);

    /**
     * <p>
     * Creates and persists an instance of NoticeboardUser which is associated with the NoticeboardSession with tool
     * session id <code>nbSessionId</code>
     * </p>
     *
     * @param nbSessionId
     *            The tool session id
     * @param user
     *            The instance of NoticeboardUser
     */
    public void addNbUsers(Long nbSessionId, NoticeboardUser user);
}