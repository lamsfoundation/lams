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

package org.lamsfoundation.lams.tool.scratchie.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;

public interface ScratchieSessionDAO extends DAO {

    ScratchieSession getSessionBySessionId(Long sessionId);

    /**
     * Returns list of sessions sorted alphanumerically (that is taking into account Group 1, Group 2, Group 10 case).
     *
     * @param toolContentId
     * @return
     */
    List<ScratchieSession> getByContentId(Long toolContentId);

    void delete(ScratchieSession session);

    void deleteBySessionId(Long toolSessionId);
    
    List<Number> getRawLeaderMarksByToolContentId(Long toolContentId);
    
    Object[] getStatsMarksForLeaders(Long toolContentId);
    
    /**
     * Returns all session ids where specified qbQuestionUid is used with the given answer
     */
    List<Long> getSessionIdsByQbQuestion(Long qbQuestionUid, String answer);

}
