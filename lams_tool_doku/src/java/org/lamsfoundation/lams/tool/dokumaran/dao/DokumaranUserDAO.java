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

package org.lamsfoundation.lams.tool.dokumaran.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranUser;

public interface DokumaranUserDAO extends DAO {

    DokumaranUser getUserByUserIDAndSessionID(Long userID, Long sessionId);

    DokumaranUser getUserByUserIDAndContentID(Long userId, Long contentId);

    List<DokumaranUser> getBySessionID(Long sessionId);

    /**
     * Get the list of all leaders in the specified session. This method is applicable only in case allowMultipleLeaders
     * option is ON.
     *
     * @param sessionId
     * @return
     */
    List<DokumaranUser> getLeadersBySessionId(Long sessionId);
}
