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

import java.util.List;

import org.lamsfoundation.lams.tool.mc.dto.McUserMarkDTO;
import org.lamsfoundation.lams.tool.mc.model.McQueUsr;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

/**
 * @author Ozgur Demirtas
 *         <p>
 *         Interface for the McUser DAO, defines methods needed to access/modify user data
 *         </p>
 */
public interface IMcUserDAO {

    /**
     * <p>
     * Return the persistent instance of a McQueUsr with the given identifier <code>uid</code>, returns null if not
     * found.
     * </p>
     *
     * @param uid
     *            an identifier for the McQueUsr.
     * @return the persistent instance of a McQueUsr or null if not found
     */
    McQueUsr getMcUserByUID(Long uid);
    
    McQueUsr getMcUserByContentId(Long userId, Long contentId);

    McQueUsr getMcUserBySession(Long userId, Long sessionUid);
    
    List<Object[]> getUsersWithPortraitsBySessionID(Long sessionId);

    void saveMcUser(McQueUsr mcUser);

    /**
     * <p>
     * Update the given persistent instance of McQueUsr.
     * </p>
     *
     * @param nbUser
     *            The instance of McQueUsr to persist.
     */
    void updateMcUser(McQueUsr mcUser);

    /**
     * <p>
     * Delete the given instance of McQueUsr
     * </p>
     *
     * @param nbUser
     *            The instance of McQueUsr to delete.
     */
    void removeMcUser(McQueUsr mcUser);

    List<McUserMarkDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy, String sortOrder,
	    String searchString, IUserManagementService userManagementService);

    int getCountPagedUsersBySession(Long sessionId, String searchString);
    
    Object[] getStatsMarksBySession(Long sessionId);
    Object[] getStatsMarksForLeaders(Long toolContentId);
    List<Number> getRawUserMarksBySession(Long sessionId);
    List<Number> getRawLeaderMarksByToolContentId(Long toolContentId);
}
