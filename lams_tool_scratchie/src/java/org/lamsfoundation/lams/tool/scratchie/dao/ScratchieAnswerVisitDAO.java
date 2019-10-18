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

import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswerVisitLog;

public interface ScratchieAnswerVisitDAO extends DAO {

    /**
     * Get log for MCQ question.
     */
    ScratchieAnswerVisitLog getLog(Long optionUid, Long itemUid, Long sessionId);
  
    /**
     * Get log for VSA question.
     */
    ScratchieAnswerVisitLog getLog(Long sessionId, Long itemUid, boolean isCaseSensitive, String answer);

    int getLogCountTotal(Long sessionId);
    
    int getLogCountPerItem(Long sessionId, Long itemUid);

    List<ScratchieAnswerVisitLog> getLogsBySessionAndItem(Long sessionId, Long itemUid);

    List<ScratchieAnswerVisitLog> getLogsBySession(Long sessionId);

}
