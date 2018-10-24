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

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardUser;

/**
 * @author mtruong
 *
 *         <p>
 *         Interface for the NoticeboardSession DAO, defines methods needed to access/modify noticeboard users (learners
 *         of the noticeboard activity)
 *         </p>
 */
public interface INoticeboardUserDAO extends IBaseDAO {

    /**
     * <p>
     * Return the persistent instance of a NoticeboardUser with the given user id <code>userId</code>, returns null if
     * not found.
     * </p>
     *
     * @param userId
     *            The id of a NoticeboardUser
     * @return the persistent instance of a NoticeboardUser or null if not found.
     */
    public NoticeboardUser getNbUser(Long userId, Long toolSessionId);

    /**
     * <p>
     * Return the persistent instance of a NoticeboardUser who has the user id <code>userId</code> and tool session id
     * <code>sessionId</code> returns null if not found.
     * </p>
     *
     * @param userId.
     *            The id of the learner
     * @param sessionId.
     *            The tool session id to which this user belongs to.
     * @return the persistent instance of a NoticeboardUser or null if not found.
     */
    public NoticeboardUser getNbUserBySession(Long userId, Long sessionId);

    /**
     * <p>
     * Persist the given persistent instance of NoticeboardUser.
     * </p>
     *
     * @param nbUser
     *            The instance of NoticeboardUser to persist.
     */
    public void saveNbUser(NoticeboardUser nbUser);

    /**
     * <p>
     * Update the given persistent instance of NoticeboardUser.
     * </p>
     *
     * @param nbUser
     *            The instance of NoticeboardUser to persist.
     */
    public void updateNbUser(NoticeboardUser nbUser);

    /**
     * Returns the number of users that are in this particular session.
     *
     * @param nbSession
     * @return the number of users that are in this session
     */
    public int getNumberOfUsers(NoticeboardSession nbSession);

    public List getNbUsersBySession(Long sessionId);
}