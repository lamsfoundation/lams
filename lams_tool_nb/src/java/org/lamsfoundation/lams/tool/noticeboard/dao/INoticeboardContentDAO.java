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

import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardSession;

/**
 * <p>
 * Interface for the NoticeboardContent DAO, defines methods needed to access/modify
 * noticeboard content
 * </p>
 * 
 * @author mtruong
 */
public interface INoticeboardContentDAO {

    /**
     * <p>
     * Return the persistent instance of a NoticeboardContent
     * with the given identifier <code>uid</code>, returns null if not found.
     * </p>
     * 
     * @param uid
     *            an identifier for the NoticeboardContent instance.
     * @return the persistent instance of a NoticeboardContent or null if not found
     */
    public NoticeboardContent getNbContentByUID(Long uid);

    /**
     * <p>
     * Return the persistent instance of a NoticeboardContent
     * with the given tool content id <code>nbContentId</code>,
     * returns null if not found.
     * </p>
     * 
     * @param nbContentId
     *            The tool content id
     * @return the persistent instance of a NoticeboardContent or null if not found.
     */
    public NoticeboardContent findNbContentById(Long nbContentId);

    /**
     * <p>
     * Returns the persistent instance of NoticeboardContent
     * with the given tool session id <code>nbSessionId</code>, returns null if not found.
     *
     * @param nbSessionId
     *            The tool session id
     * @return a persistent instance of NoticeboardContent or null if not found.
     */
    public NoticeboardContent getNbContentBySession(Long nbSessionId);

    /**
     * <p>
     * Persist the given persistent instance of NoticeboardContent.
     * </p>
     * 
     * @param nbContent
     *            The instance of NoticeboardContent to persist.
     */
    public void saveNbContent(NoticeboardContent nbContent);

    /**
     * <p>
     * Update the given persistent instance of NoticeboardContent.
     * </p>
     *
     * @param nbContent
     *            The instance of NoticeboardContent to persist.
     */
    public void updateNbContent(NoticeboardContent nbContent);

    /**
     * <p>
     * Delete the given instance of NoticeboardContent
     * </p>
     *
     * @param nbContent
     *            The instance of NoticeboardContent to delete.
     */
    public void removeNoticeboard(NoticeboardContent nbContent);

    /**
     * <p>
     * Delete the given instance of NoticeboardContent with the
     * given tool content id <code>nbContentId</code>
     *
     * @param nbContentId
     *            The tool content Id.
     */
    public void removeNoticeboard(Long nbContentId);

    /**
     * <p>
     * Deletes all instances of NoticeboardSession that are associated
     * with the given instance of NoticeboardContent
     * </p>
     *
     * @param nbContent
     *            The instance of NoticeboardContent in which corresponding instances of NoticeboardSession should be
     *            deleted.
     */
    public void removeNbSessions(NoticeboardContent nbContent);

    /**
     * <p>
     * Creates a persistent instance of NoticeboardSession which is associated
     * with the NoticeboardContent with tool content id <code>nbContentId</code>
     * </p>
     *
     * @param nbContentId
     *            The tool content id
     * @param nbSession
     *            The instance of NoticeboardSession to add
     */
    public void addNbSession(Long nbContentId, NoticeboardSession nbSession);

    public void delete(Object object);
}