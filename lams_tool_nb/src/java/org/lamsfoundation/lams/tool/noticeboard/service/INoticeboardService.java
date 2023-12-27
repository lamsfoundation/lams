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


package org.lamsfoundation.lams.tool.noticeboard.service;

import java.util.List;

import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardUser;
import org.lamsfoundation.lams.tool.service.ICommonToolService;

/**
 * Defines the contract that the tool service provider must follow
 *
 * @author mtruong
 */
public interface INoticeboardService extends ICommonToolService {

    // ===================================================================
    // NoticeboardContent access methods
    // ===================================================================

    /**
     * <p>
     * Returns the persistent instance of NoticeboardContent with the given tool session id <code>nbSessionId</code>,
     * returns null if not found.
     *
     * @param nbSessionId
     *            The tool session id
     * @return a persistent instance of NoticeboardContent or null if not found.
     */
    public NoticeboardContent retrieveNoticeboardBySessionID(Long nbSessionId);

    /**
     * <p>
     * Retrieve an instance of NoticeboardContent with the given tool content id <code>nbContentId</code>
     * </p>
     *
     * @param nbContentId
     *            The tool content id
     * @return an instance of NoticeboardContent
     */
    public NoticeboardContent retrieveNoticeboard(Long nbContentId);

    /**
     * <p>
     * Persist/Update the given persistent instance of NoticeboardContent.
     * </p>
     *
     * @param nbContent
     *            The instance of NoticeboardContent to persist.
     */
    public void saveNoticeboard(NoticeboardContent nbContent);

    /**
     * <p>
     * Deletes all instances of NoticeboardSession that are associated with the given instance of NoticeboardContent
     * </p>
     *
     * @param nbContent
     *            The instance of NoticeboardContent in which corresponding instances of NoticeboardSession should be
     *            deleted.
     */
    public void removeNoticeboardSessionsFromContent(NoticeboardContent nbContent);

    /**
     * <p>
     * Delete the given instance of NoticeboardContent with the given tool content id <code>nbContentId</code>
     *
     * @param nbContentId
     *            The tool content Id.
     */
    public void removeNoticeboard(Long nbContentId);

    /**
     * <p>
     * Delete the given instance of NoticeboardContent
     * </p>
     *
     * @param nbContent
     *            The instance of NoticeboardContent to delete.
     */
    public void removeNoticeboard(NoticeboardContent nbContent);

    // ===================================================================
    // NoticeboardSession access methods
    // ===================================================================
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
    public NoticeboardSession retrieveNoticeboardSession(Long nbSessionId);

    /**
     * Persists the new NoticeboardSession object into the database.
     *
     * @param nbSession
     *            the NoticeboardSession object to persist
     */
    public void saveNoticeboardSession(NoticeboardSession nbSession);

    /**
     * Updates the values of the noticeboard session.
     *
     * @param nbSession
     */
    public void updateNoticeboardSession(NoticeboardSession nbSession);

    /**
     * Remove the noticeboard session object with session id of that specified in the argument.
     *
     * @param nbSessionId
     *            The id of the requested noticeboard object
     *
     */
    public void removeSession(Long nbSessionId);

    /**
     * <p>
     * Delete the given instance of NoticeboardSession
     * </p>
     *
     * @param nbSession
     *            The instance of NoticeboardSession to delete.
     */
    public void removeSession(NoticeboardSession nbSession);

    /**
     * <p>
     * Deletes all instances of NoticeboardUser that are associated with the given instance of NoticeboardSession
     * </p>
     *
     * @param nbSession
     *            The instance of NoticeboardSession in which corresponding instances of NoticeboardUser should be
     *            deleted.
     */
    public void removeNoticeboardUsersFromSession(NoticeboardSession nbSession);

    /**
     * <p>
     * Returns the persistent instance of NoticeboardSession with the given noticeboard user id<code>userId</code>,
     * returns null if not found.
     *
     * @param userId
     *            The user id
     * @return a persistent instance of NoticeboardSession or null if not found.
     */
    public NoticeboardSession retrieveNbSessionByUserID(Long userId);

    // ===================================================================
    // NoticeboardUser access methods
    // ===================================================================
    /**
     * <p>
     * Return the persistent instance of a NoticeboardUser with the given user id<code>nbUserId</code>, returns null if
     * not found.
     * </p>
     *
     * @param nbUserId
     *            The user id of the instance of NoticeboardUser
     * @return the persistent instance of a NoticeboardUser or null if not found.
     */
    public NoticeboardUser retrieveNoticeboardUser(Long nbUserId, Long toolSessionId);

    /**
     * <p>
     * Return the persistent instance of a NoticeboardUser who has the user id <code>userId</code> and tool session id
     * <code>sessionId</code> returns null if not found.
     * </p>
     *
     * @param userId
     *            . The id of the learner
     * @param sessionId
     *            . The tool session id to which this user belongs to.
     * @return the persistent instance of a NoticeboardUser or null if not found.
     */
    public NoticeboardUser retrieveNbUserBySession(Long userId, Long sessionId);

    /**
     * Persists the new NoticeboardUser object into the database.
     *
     * @param nbUser
     *            the NoticeboardUser object to persist
     */
    public void saveNoticeboardUser(NoticeboardUser nbUser);

    /**
     * Updates the values of the noticeboard user.
     *
     * @param nbUser
     */
    public void updateNoticeboardUser(NoticeboardUser nbUser);

    /**
     * Remove the noticeboard user object with user id of that specified in the argument.
     *
     * @param nbUserId
     *            The id of the requested noticeboard object
     * @param toolSessionId
     *            The id of the nbUser's associated nbSession
     *
     */
    public void removeUser(Long nbUserId, Long toolSessionId);

    /**
     * <p>
     * Delete the given instance of NoticeboardUser
     * </p>
     *
     * @param nbUser
     *            The instance of NoticeboardUser to delete.
     */
    public void removeUser(NoticeboardUser nbUser);

    /**
     * <p>
     * Saves the instance of NoticeboardSession to the database. This instance is added to the collection of sessions
     * from NoticeboardContent with tool content id <code>nbContentId</code>
     * </p>
     *
     * @param nbContentId
     *            The tool content Id
     * @param session
     *            The instance of NoticeboardSession to persist
     */
    public void addSession(Long nbContentId, NoticeboardSession session);

    /**
     * <p>
     * Saves the instance of NoticeboardUser to the database. <code>nbUser</code> is added to the collection of users
     * from NoticeboardSession with tool session id <code>nbSessionId</code>
     * </p>
     *
     * @param nbSessionId
     *            The tool session id
     * @param user
     *            The instance of NoticeboardUser to persist
     */
    public void addUser(Long nbSessionId, NoticeboardUser nbUser);

    /**
     * <p>
     * Returns the number of users in this session
     * </p>
     *
     * @param session
     * @return The number of users in the session
     */
    public int getNumberOfUsersInSession(NoticeboardSession session);

    /**
     * <p>
     * Finds the number of learners that have participated in this tool activity with the given toolContentId. It finds
     * all the toolSessionIds relating to this toolContentId, and calculates the number of users in each tool
     * session(group). Returns the total number of users across all sessions
     * </p>
     *
     * @param toolContentId
     * @return the total number of users for this tool activity
     */
    public int calculateTotalNumberOfUsers(Long toolContentId);

    /**
     * This method retrieves the default content id.
     *
     * @param toolSignature
     *            The tool signature which is defined in lams_tool table.
     * @return the default content id
     */
    public Long getToolDefaultContentIdBySignature(String toolSignature);

    public List<NoticeboardUser> getUsersBySession(Long sessionId);
}