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
/* $$Id$ */

package org.lamsfoundation.lams.tool.scribe.service;

import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.scribe.model.Scribe;
import org.lamsfoundation.lams.tool.scribe.model.ScribeSession;
import org.lamsfoundation.lams.tool.scribe.model.ScribeUser;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Scribe Service
 */
public interface IScribeService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     *
     * @params newContentID
     * @return
     */
    public Scribe copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Scribe tools default content.
     *
     * @return
     */
    public Scribe getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public Scribe getScribeByContentId(Long toolContentID);

    /**
     * @param scribe
     */
    public void saveOrUpdateScribe(Scribe scribe);

    /**
     * @param toolSessionId
     * @return
     */
    public ScribeSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param scribeSession
     */
    public void saveOrUpdateScribeSession(ScribeSession scribeSession);

    /**
     *
     * @param userId
     * @param toolSessionId
     * @return
     */
    public ScribeUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     *
     * @param loginName
     * @param sessionID
     * @return
     */
    public ScribeUser getUserByLoginNameAndSessionId(String loginName, Long sessionId);

    /**
     *
     * @param uid
     * @return
     */
    public ScribeUser getUserByUID(Long uid);

    /**
     *
     * @param scribeUser
     */
    public void saveOrUpdateScribeUser(ScribeUser scribeUser);

    /**
     *
     * @param user
     * @param scribeSession
     * @return
     */
    public ScribeUser createScribeUser(UserDTO user, ScribeSession scribeSession);

    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    public NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID);

    /**
     * Delete heading's report by given heading UID.
     *
     * @param uid
     */
    public void deleteHeadingReport(Long uid);

    /**
     * Clone heading from scribe content.
     *
     * @param toolSessionId
     */
    public void createReportEntry(Long toolSessionId);

    boolean isGroupedActivity(long toolContentID);

    void submitReport(Long toolSessionId, String userName, JSONObject requestJSON) throws JSONException;

    public void deleteHeading(Long headingUid);
}