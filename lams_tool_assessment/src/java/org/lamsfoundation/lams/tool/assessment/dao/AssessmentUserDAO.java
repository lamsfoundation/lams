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

import org.lamsfoundation.lams.tool.assessment.dto.AssessmentUserDTO;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

public interface AssessmentUserDAO extends DAO {

    AssessmentUser getUserByUserIDAndSessionID(Long userID, Long sessionId);

    AssessmentUser getUserCreatedAssessment(Long userId, Long contentId);

    AssessmentUser getUserByIdAndContent(Long userId, Long contentId);

    AssessmentUser getUserByLoginAndContent(String login, Long contentId);

    List<AssessmentUser> getBySessionID(Long sessionId);

    List<AssessmentUserDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy, String sortOrder,
	    String searchString, IUserManagementService userManagementService);

    int getCountUsersBySession(Long sessionId, String searchString);

    int getCountUsersByContentId(Long contentId);

    List<AssessmentUserDTO> getPagedUsersBySessionAndQuestion(Long sessionId, Long questionUid, int page, int size,
	    String sortBy, String sortOrder, String searchString, IUserManagementService userManagementService);

    List<Float> getRawUserMarksBySession(Long sessionId);

    List<Float> getRawUserMarksByToolContentId(Long toolContentId);

    List<Float> getRawLeaderMarksByToolContentId(Long toolContentId);
}