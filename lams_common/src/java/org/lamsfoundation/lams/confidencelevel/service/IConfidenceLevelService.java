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



package org.lamsfoundation.lams.confidencelevel.service;

import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.confidencelevel.ConfidenceLevel;

public interface IConfidenceLevelService {

    void saveOrUpdateConfidenceLevel(ConfidenceLevel confidenceLevel);
    
    /** 
     * Save a group of confidenceLevels as the new confidenceLevels for this criteria, marking any existing confidenceLevels NULL.
     * Returns the number of "real" confidenceLevels, which should be newConfidenceLevels.size.
     * @return
     */
    int saveConfidenceLevels(Long toolSessionId, Integer userId, Map<Long, Integer> questionUidToConfidenceLevelMap);

    void saveConfidenceLevel(Long toolSessionId, Integer userId, Long questionUid, int confidenceLevelInt);

    /**
     * Returns all confidence levels user left in this activity.
     *
     * @param userId
     * @param toolSessionId
     * @return
     */
    List<ConfidenceLevel> getConfidenceLevelsByUser(Integer userId, Long toolSessionId);
 
    /**
     * Returns all confidence levels that was left for the specified question and by the users from the specified session.
     *
     * @param userId
     * @param toolSessionId
     * @return
     */
    List<ConfidenceLevel> getConfidenceLevelsByQuestionAndSession(Long questionUid, Long toolSessionId);
    
    /**
     * Removes all confidenceLevels and comments left by the specified user.
     * 
     * @param contentId
     * @param userId
     * @return
     */
    void removeUserCommitsByContent(Integer userId, Long toolSessionId);

}
