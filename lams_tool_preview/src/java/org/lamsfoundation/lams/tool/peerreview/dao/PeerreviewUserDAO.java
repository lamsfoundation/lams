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

package org.lamsfoundation.lams.tool.peerreview.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewSession;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

public interface PeerreviewUserDAO extends IBaseDAO {

    PeerreviewUser getUserByUserIDAndSessionID(Long userID, Long sessionId);

    PeerreviewUser getUserByUid(Long userUid);

    PeerreviewUser getUserByUserIDAndContentID(Long userId, Long contentId);

    List<PeerreviewUser> getBySessionID(Long sessionId);

    /**
     * Counts number of users in a session excluding specified user. Besides, it also *excludes all hidden users*.
     *
     * @param toolSessionId
     * @param excludeUserId
     * @param includeHiddenUsers
     *            whether hidden users should be counted as well or not
     * @return
     */
    int getCountUsersBySession(final Long toolSessionId, final Long excludeUserId);

    List<Object[]> getRatingsComments(Long toolContentId, Long toolSessionId, RatingCriteria criteria, Long userId,
	    Integer page, Integer size, int sorting, String searchString, boolean getByUser, boolean includeCurrentUser,
	    IRatingService coreRatingService, IUserManagementService userManagementService);

    List<Object[]> getCommentsCounts(Long toolContentId, Long toolSessionId, RatingCriteria criteria, Integer page,
	    Integer size, int sorting, String searchString, IUserManagementService userManagementService);

    List<Object[]> getDetailedRatingsComments(Long toolContentId, Long toolSessionId, Long criteriaId, Long itemId);

    /**
     * Counts number of users in a specified session. It counts it regardless whether a user is hidden or not.
     *
     * @param toolSessionId
     * @return
     */
    int getCountUsersBySession(Long toolSessionId);

    int createUsersForSession(PeerreviewSession session);

    /**
     * Returns list of <userUid, userName> pairs. Used by monitor's manageUsers functionality.
     *
     * @param toolSessionId
     * @param page
     * @param size
     * @param sorting
     * @param searchString
     * @return
     */
    List<Object[]> getPagedUsers(Long toolSessionId, Integer page, Integer size, int sorting, String searchString);
}
