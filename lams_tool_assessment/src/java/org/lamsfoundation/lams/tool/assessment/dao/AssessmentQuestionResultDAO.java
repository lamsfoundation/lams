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

package org.lamsfoundation.lams.tool.assessment.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;

public interface AssessmentQuestionResultDAO extends DAO {

    int getNumberWrongAnswersDoneBefore(Long assessmentUid, Long userId, Long questionUid);

    /**
     * Returns array, first element is AssessmentQuestionResult, second - according AssessmentResult
     */
    List<Object[]> getAssessmentQuestionResultList(Long assessmentUid, Long userId, Long questionUid);

    AssessmentQuestionResult getAssessmentQuestionResultByUid(Long questionResultUid);

    /**
     * Returns question result mark from the last finished assessment result, and null if not available.
     *
     * @param assessmentUid
     * @param userId
     * @param questionDisplayOrder
     * @return
     */
    Float getQuestionResultMark(Long assessmentUid, Long userId, int questionDisplayOrder);

}
