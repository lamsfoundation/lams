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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.assessment.dao;

import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionVisitLog;

public interface AssessmentQuestionVisitDAO extends DAO {

    public AssessmentQuestionVisitLog getAssessmentQuestionLog(Long questionUid, Long userId);

    public int getUserViewLogCount(Long sessionId, Long userUid);

    /**
     * Return list which contains key pair which key is assessment question uid, value is number view.
     * 
     * @param contentId
     * @return
     */
    public Map<Long, Integer> getSummary(Long contentId);

    public List<AssessmentQuestionVisitLog> getAssessmentQuestionLogBySession(Long sessionId, Long questionUid);

}
