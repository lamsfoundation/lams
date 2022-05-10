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

package org.lamsfoundation.lams.tool.leaderselection.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.leaderselection.model.Leaderselection;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionSession;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionUser;
import org.lamsfoundation.lams.tool.service.ICommonToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Leaderselection Service
 */
public interface ILeaderselectionService extends ICommonToolService {

    /**
     * Get users by given toolSessionId.
     *
     * @param long1
     * @return
     */
    List<LeaderselectionUser> getUsersBySession(Long toolSessionId);

    Collection<User> getAllGroupUsers(Long toolSessionId);

    /**
     * Set specified user as a leader. Also the previous leader (if any) is marked as non-leader.
     *
     * @param userId
     * @param toolSessionId
     * @return
     * @throws IOException
     * @throws JSONException
     */
    boolean setGroupLeader(Long userId, Long toolSessionId);

    /**
     * Makes a copy of the default content and assigns it a newContentID
     *
     * @params newContentID
     * @return
     */
    Leaderselection copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Leaderselection tools default content.
     *
     * @return
     */
    Leaderselection getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    Leaderselection getContentByContentId(Long toolContentID);

    boolean isUserLeader(Long userId, Long toolSessionId);

    /**
     * @param leaderselection
     */
    void saveOrUpdateLeaderselection(Leaderselection leaderselection);

    /**
     * @param toolSessionId
     * @return
     */
    LeaderselectionSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param leaderselectionSession
     */
    void saveOrUpdateSession(LeaderselectionSession leaderselectionSession);

    /**
     *
     * @param userId
     * @param toolSessionId
     * @return
     */
    LeaderselectionUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    LeaderselectionUser getUserByLoginAndSessionId(String login, long toolSessionId);

    LeaderselectionUser getUserByUserIdAndContentId(Long userId, Long toolContentId);

    /**
     *
     * @param uid
     * @return
     */
    LeaderselectionUser getUserByUID(Long uid);

    /**
     *
     * @param leaderselectionUser
     */
    void saveOrUpdateUser(LeaderselectionUser leaderselectionUser);

    /**
     *
     * @param user
     * @param leaderselectionSession
     * @return
     */
    LeaderselectionUser createLeaderselectionUser(UserDTO user, LeaderselectionSession leaderselectionSession);

    String finishToolSession(Long toolSessionId, Long userId);

    /**
     *
     * @param id
     * @param idType
     * @param signature
     * @param userID
     * @param title
     * @param entry
     * @return
     */
    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    /**
     *
     * @param uid
     * @return
     */
    NotebookEntry getEntry(Long uid);

    /**
     *
     * @param uid
     * @param title
     * @param entry
     */
    void updateEntry(Long uid, String entry);
}
