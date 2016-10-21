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

import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewSession;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;

public interface PeerreviewUserDAO extends DAO {

    PeerreviewUser getUserByUserIDAndSessionID(Long userID, Long sessionId);

    PeerreviewUser getUserByUserIDAndContentID(Long userId, Long contentId);

    List<PeerreviewUser> getBySessionID(Long sessionId);

    List<Long> getUserIdsBySessionID(Long sessionId);

    List<PeerreviewUser> getByContentId(Long toolContentId);

    int getCountUsersBySession(final Long toolSessionId, final Long excludeUserId);

    List<Object[]> getRatingsComments(Long toolContentId, Long toolSessionId, RatingCriteria criteria, Long userId, Integer page,
	    Integer size, int sorting, String searchString, boolean getByUser, IRatingService coreRatingService);
	
    List<Object[]> getCommentsCounts(Long toolContentId, Long toolSessionId, RatingCriteria criteria,
	    Integer page, Integer size, int sorting, String searchString);
	
    List<Object[]> getDetailedRatingsComments(Long toolContentId, Long toolSessionId, Long criteriaId, Long itemId );
    
    int getCountUsersBySession(Long toolSessionId);

    int createUsersForSession(PeerreviewSession session);
    
    List<Object[]> getUserNotebookEntriesForTablesorter(final Long toolSessionId, int page, int size, int sorting, String searchString, 
	    ICoreNotebookService coreNotebookService);
}
