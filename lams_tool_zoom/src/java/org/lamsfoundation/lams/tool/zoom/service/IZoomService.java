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

package org.lamsfoundation.lams.tool.zoom.service;

import java.io.IOException;

import org.apache.tomcat.util.json.JSONException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.zoom.model.Zoom;
import org.lamsfoundation.lams.tool.zoom.model.ZoomSession;
import org.lamsfoundation.lams.tool.zoom.model.ZoomUser;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Zoom Service
 */
public interface IZoomService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     *
     * @params newContentID
     * @return
     */
    public Zoom copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Zoom tools default content.
     *
     * @return
     */
    public Zoom getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public Zoom getZoomByContentId(Long toolContentID);

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
     * @param zoom
     */
    public void saveOrUpdateZoom(Zoom zoom);

    /**
     * @param toolSessionId
     * @return
     */
    public ZoomSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param zoomSession
     */
    public void saveOrUpdateZoomSession(ZoomSession zoomSession);

    /**
     *
     * @param userId
     * @param toolSessionId
     * @return
     */
    public ZoomUser getUserByUserIdAndSessionId(Integer userId, Long toolSessionId);

    /**
     *
     * @param uid
     * @return
     */
    public ZoomUser getUserByUID(Long uid);

    /**
     *
     * @param zoomUser
     */
    public void saveOrUpdateZoomUser(ZoomUser zoomUser);

    /**
     *
     * @param user
     * @param zoomSession
     * @return
     */
    public ZoomUser createZoomUser(UserDTO user, ZoomSession zoomSession);

    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    NotebookEntry getNotebookEntry(Long uid);

    void updateNotebookEntry(NotebookEntry notebookEntry);

    void updateNotebookEntry(Long uid, String entry);

    boolean chooseApiKeys(Long zoomUid);

    String createMeeting(Long zoomUid) throws IOException, JSONException;

    String registerUser(Long zoomUid, Long userUid, String sessionName) throws IOException, JSONException;
}