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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.assessment.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;

public interface AssessmentResultDAO extends DAO {

    List<AssessmentResult> getAssessmentResults(Long assessmentUid, Long userId);

    List<AssessmentResult> getAssessmentResultsBySession(Long sessionId, Long userId);

    List<AssessmentResult> getFinishedAssessmentResultsBySession(Long sessionId, Long userId);

    AssessmentResult getLastAssessmentResult(Long assessmentUid, Long userId);

    AssessmentResult getLastFinishedAssessmentResult(Long assessmentUid, Long userId);

    Float getLastFinishedAssessmentResultGrade(Long assessmentUid, Long userId);

    Integer getLastFinishedAssessmentResultTimeTaken(Long assessmentUid, Long userId);

    AssessmentResult getLastFinishedAssessmentResultBySessionId(Long sessionId, Long userId);
    
    /**
     * Returns all last finished results for the specified assessment.
     * 
     * @param contentId
     * @return
     */
    List<AssessmentResult> getLastFinishedAssessmentResults(Long contentId);

    int getAssessmentResultCount(Long assessmentUid, Long userId);

    AssessmentResult getAssessmentResultByUid(Long assessmentResultUid);
}