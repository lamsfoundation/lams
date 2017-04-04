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


package org.lamsfoundation.lams.tool.bbb.service;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.bbb.model.Bbb;
import org.lamsfoundation.lams.tool.bbb.model.BbbConfig;
import org.lamsfoundation.lams.tool.bbb.model.BbbSession;
import org.lamsfoundation.lams.tool.bbb.model.BbbUser;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Bbb Service
 */
public interface IBbbService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     *
     * @params newContentID
     * @return
     */
    public Bbb copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Bbb tools default content.
     *
     * @return
     */
    public Bbb getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public Bbb getBbbByContentId(Long toolContentID);

    /**
     * @param toolContentID
     * @return
     */
    public boolean isGroupedActivity(long toolContentID);
    
    /**
     * Audit log the teacher has started editing activity in monitor.
     * 
     * @param toolContentID
     */
    void auditLogStartEditingActivityInMonitor(long toolContentID);

    /**
     * @param bbb
     */
    public void saveOrUpdateBbb(Bbb bbb);

    /**
     * @param toolSessionId
     * @return
     */
    public BbbSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param bbbSession
     */
    public void saveOrUpdateBbbSession(BbbSession bbbSession);

    /**
     *
     * @param userId
     * @param toolSessionId
     * @return
     */
    public BbbUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     *
     * @param uid
     * @return
     */
    public BbbUser getUserByUID(Long uid);

    /**
     *
     * @param bbbUser
     */
    public void saveOrUpdateBbbUser(BbbUser bbbUser);

    /**
     *
     * @param user
     * @param bbbSession
     * @return
     */
    public BbbUser createBbbUser(UserDTO user, BbbSession bbbSession);

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
    NotebookEntry getNotebookEntry(Long uid);

    /**
     *
     * @param notebookEntry
     */
    void updateNotebookEntry(NotebookEntry notebookEntry);

    /**
     *
     * @param uid
     * @param title
     * @param entry
     */
    void updateNotebookEntry(Long uid, String entry);

    /**
     *
     * @param key
     */
    BbbConfig getConfig(String key);

    /**
     *
     * @param key
     */
    String getConfigValue(String key);

    /**
     *
     * @param key
     * @param value
     */
    void saveOrUpdateConfigEntry(BbbConfig bbbConfig);

    /**
     * Start a standard meeting
     *
     * @param meetingKey
     * @param attendeePassword
     * @param moderatorPassword
     * @return Meeting url
     * @throws Exception
     */
    String startConference(String meetingKey, String atendeePassword, String moderatorPassword, String returnURL,
	    String welcomeMessage) throws Exception;

    /**
     * Join a standard meeting
     *
     * @param userDTO
     * @param meetingKey
     * @param password
     * @return Meeting url
     */
    String getJoinMeetingURL(UserDTO userDTO, String meetingKey, String password) throws Exception;

    Boolean isMeetingRunning(String meetingKey) throws Exception;

}
