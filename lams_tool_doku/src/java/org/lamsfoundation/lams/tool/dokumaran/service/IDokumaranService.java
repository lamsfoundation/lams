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

package org.lamsfoundation.lams.tool.dokumaran.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.Cookie;

import org.lamsfoundation.lams.etherpad.EtherpadException;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.dokumaran.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.dokumaran.dto.SessionDTO;
import org.lamsfoundation.lams.tool.dokumaran.model.Dokumaran;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranSession;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranUser;
import org.lamsfoundation.lams.tool.service.ICommonToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * @author Dapeng.Ni
 *
 *         Interface that defines the contract that all ShareDokumaran service provider must follow.
 */
public interface IDokumaranService extends ICommonToolService {

    /**
     * Get <code>Dokumaran</code> by toolContentID.
     *
     * @param contentId
     * @return
     */
    Dokumaran getDokumaranByContentId(Long contentId);

    /**
     * Get a cloned copy of tool default tool content (Dokumaran) and assign the toolContentId of that copy as the given
     * <code>contentId</code>
     *
     * @param contentId
     * @return
     * @throws DokumaranApplicationException
     */
    Dokumaran getDefaultContent(Long contentId) throws DokumaranApplicationException;

    /**
     * Set specified user as a leader. Also the previous leader (if any) is marked as non-leader.
     *
     * @param userId
     * @param toolSessionId
     * @param isFirstTimeAccess
     *            whether user is accessing this doKumaran tool for the first time. If it's true - it will try to update
     *            leaders list from the Leader Selection activity
     */
    List<DokumaranUser> checkLeaderSelectToolForSessionLeader(DokumaranUser user, Long toolSessionId,
	    boolean isFirstTimeAccess);

    /**
     * Is user is as a leader. It works OK in all cases regardless whether isAllowMultipleLeaders option is ON or OFF
     * (as all leaders are kept in leaders list anyway).
     *
     * @param leaders
     * @param userId
     * @return
     */
    boolean isUserLeader(List<DokumaranUser> leaders, Long userId);

    /**
     * Checks whether at least one of the leaders has finished activity and thus all non-leaders can proceed with
     * finishing it as well.
     *
     * @param leaders
     * @return
     */
    boolean isLeaderResponseFinalized(List<DokumaranUser> leaders);

    /**
     * Checks whether at least one of the leaders has finished activity and thus all non-leaders can proceed with
     * finishing it as well. It differs from the above method that is should be used when leaders list is not
     * constructed yet.
     *
     * @param session
     * @return
     */
    boolean isLeaderResponseFinalized(Long toolSessionId);

    /**
     * Stores date when user has started activity with time limit.
     */
    LocalDateTime launchTimeLimit(long toolContentId, int userId);

    /**
     * @return whether the time limit is exceeded already
     */
    boolean checkTimeLimitExceeded(Dokumaran dokumaran, int userId);

    List<User> getPossibleIndividualTimeLimitUsers(long toolContentId, String searchString);

    Cookie createEtherpadCookieForLearner(DokumaranUser user, DokumaranSession session)
	    throws DokumaranApplicationException, EtherpadException;

    Cookie createEtherpadCookieForMonitor(UserDTO user, Long contentId) throws EtherpadException;

    /**
     * Creates pad on Etherpad server side.
     */
    void createPad(Dokumaran dokumaran, DokumaranSession session) throws DokumaranApplicationException;

    // ********** for user methods *************
    /**
     * Get learner by given user ID and tool content ID, i.e. user who has a session assigned.
     */
    DokumaranUser getLearnerByIDAndContent(Long userId, Long contentId);

    /**
     * Get user by given userID and toolContentID.
     *
     * @param long1
     * @return
     */
    DokumaranUser getUserByIDAndContent(Long userID, Long contentId);

    /**
     * Get user by sessionID and UserID
     *
     * @param long1
     * @param sessionId
     * @return
     */
    DokumaranUser getUserByIDAndSession(Long long1, Long sessionId);

    /**
     * Get users by the given toolSessionId.
     *
     * @param toolSessionId
     * @return
     */
    List<DokumaranUser> getUsersBySession(Long toolSessionId);

    /**
     * Save or update any object into database.
     */
    void saveOrUpdate(Object entity);

    /**
     * Get dokumaran which is relative with the special toolSession.
     */
    Dokumaran getDokumaranBySessionId(Long sessionId);

    /**
     * Get dokumaran toolSession by toolSessionId
     */
    DokumaranSession getDokumaranSessionBySessionId(Long sessionId);

    /**
     * If success return next activity's url, otherwise return null.
     */
    String finishToolSession(Long toolSessionId, Long userId) throws DokumaranApplicationException;

    /**
     * Return monitoring summary list. The return value is list of dokumaran summaries for each groups.
     */
    List<SessionDTO> getSummary(Long contentId, Long ratingUserId);

    /**
     * Create refection entry into notebook tool.
     *
     * @param sessionId
     * @param notebook_tool
     * @param tool_signature
     * @param userId
     * @param entryText
     */
    Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText);

    /**
     * Get reflection entry from notebook tool.
     *
     * @param sessionId
     * @param idType
     * @param signature
     * @param userID
     * @return
     */
    NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID);

    /**
     * @param notebookEntry
     */
    void updateEntry(NotebookEntry notebookEntry);

    /**
     * Get Reflect DTO list.
     *
     * @param contentId
     * @return
     */
    List<ReflectDTO> getReflectList(Long contentId);

    /**
     * Get user by UID
     *
     * @param uid
     * @return
     */
    DokumaranUser getUser(Long uid);

    void startGalleryWalk(long toolContentId) throws IOException;

    void finishGalleryWalk(long toolContentId) throws IOException;

    void changeLeaderForGroup(long toolSessionId, long leaderUserId);

    Grouping getGrouping(long toolContentId);
}