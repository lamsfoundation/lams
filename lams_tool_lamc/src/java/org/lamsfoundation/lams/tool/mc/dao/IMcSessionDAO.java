/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.dao;

import org.lamsfoundation.lams.tool.mc.model.McSession;

/**
 * @author Ozgur Demirtas
 *         <p>
 *         Interface for the McSession DAO, defines methods needed to access/modify mc session
 *         </p>
 *
 */
public interface IMcSessionDAO {

    /**
     * <p>
     * Return the persistent instance of a McSession with the given tool session id <code>mcSessionId</code>, returns
     * null if not found.
     * </p>
     *
     * @param mcSessionId
     *            The tool session id
     * @return the persistent instance of a McSession or null if not found.
     */
    public McSession getMcSessionById(Long mcSessionId);

    /**
     * <p>
     * Persist the given persistent instance of McSession.
     * </p>
     *
     * @param mcSession
     *            The instance of McSession to persist.
     */
    public void saveMcSession(McSession mcSession);

    /**
     * <p>
     * Update the given persistent instance of McSession.
     * </p>
     *
     * @param mcContent
     *            The instance of McSession to persist.
     */
    public void updateMcSession(McSession mcSession);

    /**
     * Delete the given instance of McSession
     */
    public void removeMcSession(McSession mcSession);

    /**
     * <p>
     * Returns the persistent instance of McSession associated with the given mc user, with user id <code>userId</code>,
     * returns null if not found.
     *
     * @param userId
     *            The mc user id
     * @return a persistent instance of McSessions or null if not found.
     */
    public McSession getMcSessionByUser(Long userId);

}