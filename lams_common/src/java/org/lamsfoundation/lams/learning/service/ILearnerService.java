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

package org.lamsfoundation.lams.learning.service;

import org.lamsfoundation.lams.learningdesign.dto.ActivityPositionDTO;
import org.lamsfoundation.lams.tool.ToolOutput;

/**
 * Learner service methods available to tools. These methods should work fine as long as the web context contains the
 * core Spring context files.
 */
public interface ILearnerService {
    /**
     * Marks an tool session as complete and calculates the next activity against the learning design. This method is
     * for tools to redirect the client on complete.
     *
     * Do not change learnerId to Integer (to match the other calls) as all the tools expect this to be a Long.
     *
     * @param toolSessionId
     *            , session ID for completed tool
     * @param learnerId
     *            the learner who is completing the tool session.
     * @return the URL for the next activity
     * @throws LearnerServiceException
     *             in case of problems.
     */
    public String completeToolSession(Long toolSessionId, Long learnerId);

    ToolOutput getToolInput(Long requestingToolContentId, Integer assigmentId, Integer learnerId);

    ActivityPositionDTO getActivityPosition(Long activityId);

    ActivityPositionDTO getActivityPositionByToolSessionId(Long toolSessionId);

    void createCommandForLearner(Long lessonId, String userName, String jsonCommand);
}
