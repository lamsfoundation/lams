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

package org.lamsfoundation.lams.tool.whiteboard.service;

import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.service.ICommonToolService;
import org.lamsfoundation.lams.tool.whiteboard.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.whiteboard.dto.SessionDTO;
import org.lamsfoundation.lams.tool.whiteboard.model.Whiteboard;
import org.lamsfoundation.lams.tool.whiteboard.model.WhiteboardConfigItem;
import org.lamsfoundation.lams.tool.whiteboard.model.WhiteboardSession;
import org.lamsfoundation.lams.tool.whiteboard.model.WhiteboardUser;
import org.lamsfoundation.lams.usermanagement.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface IWhiteboardService extends ICommonToolService {

    /**
     * Get <code>Whiteboard</code> by toolContentID.
     */
    Whiteboard getWhiteboardByContentId(Long contentId);

    /**
     * Get a cloned copy of tool default tool content (Whiteboard) and assign the toolContentId of that copy as the
     * given
     * <code>contentId</code>
     */
    Whiteboard getDefaultContent(Long contentId) throws WhiteboardApplicationException;

    WhiteboardUser checkLeaderSelectToolForSessionLeader(WhiteboardUser user, Long toolSessionId);

    /**
     * Stores date when user has started activity with time limit.
     */
    LocalDateTime launchTimeLimit(long toolContentId, int userId);

    /**
     * @return whether the time limit is exceeded already
     */
    boolean checkTimeLimitExceeded(Whiteboard whiteboard, int userId);

    List<User> getPossibleIndividualTimeLimitUsers(long toolContentId, String searchString);

    /**
     * Get learner by given user ID and tool content ID, i.e. user who has a session assigned.
     */
    WhiteboardUser getLearnerByIDAndContent(Long userId, Long contentId);

    WhiteboardUser getUserByIDAndContent(Long userID, Long contentId);

    WhiteboardUser getUserByLoginAndContent(String login, long contentId);

    /**
     * Get user by sessionID and UserID
     */
    WhiteboardUser getUserByIDAndSession(Long userId, Long toolSessionId);

    /**
     * Get users by the given toolSessionId.
     */
    List<WhiteboardUser> getUsersBySession(Long toolSessionId);

    /**
     * Save or update any object into database.
     */
    void saveOrUpdate(Object entity);

    Whiteboard getWhiteboardBySessionId(Long sessionId);

    /**
     * Get Whiteboard toolSession by toolSessionId
     */
    WhiteboardSession getWhiteboardSessionBySessionId(Long toolSessionId);

    /**
     * If success return next activity's url, otherwise return null.
     */
    String finishToolSession(Long toolSessionId, Long userId) throws WhiteboardApplicationException;

    /**
     * Mark session as finished after time limit is exceeded.
     */
    void finishExpiredSessions(Long toolContentId);

    /**
     * Create refection entry into notebook tool.
     */
    Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText);

    /**
     * Get reflection entry from notebook tool.
     */
    NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID);

    void updateEntry(NotebookEntry notebookEntry);

    /**
     * Get Reflect DTO list.
     */
    List<ReflectDTO> getReflectList(Long contentId);

    List<SessionDTO> getSummary(Long contentId, Long ratingUserId) throws WhiteboardApplicationException;

    /**
     * Get user by UID
     */
    WhiteboardUser getUser(Long uid);

    void startGalleryWalk(long toolContentId);

    void skipGalleryWalk(long toolContentId);

    void finishGalleryWalk(long toolContentId);

    void enableGalleryWalkLearnerEdit(long toolContentId) throws IOException;

    void changeLeaderForGroup(long toolSessionId, long leaderUserId);

    Grouping getGrouping(long toolContentId);

    WhiteboardConfigItem getConfigItem(String key);

    void saveOrUpdateWhiteboardConfigItem(WhiteboardConfigItem item);

    String getWhiteboardServerUrl() throws WhiteboardApplicationException;

    String getWhiteboardAccessTokenHash(String wid, String sourceWid);

    String getWhiteboardReadOnlyWid(String wid) throws WhiteboardApplicationException;

    String getWhiteboardPrefixedId(String wid);
}