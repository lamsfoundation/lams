/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.tool.gmap.service;

import java.util.List;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.lamsfoundation.lams.tool.gmap.model.GmapConfigItem;
import org.lamsfoundation.lams.tool.gmap.model.GmapMarker;
import org.lamsfoundation.lams.tool.gmap.model.GmapSession;
import org.lamsfoundation.lams.tool.gmap.model.GmapUser;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Gmap Service
 */
public interface IGmapService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     * 
     * @params newContentID
     * @return
     */
    public Gmap copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Gmap tools default content.
     * 
     * @return
     */
    public Gmap getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public Gmap getGmapByContentId(Long toolContentID);

    /**
     * @param gmap
     */
    public void saveOrUpdateGmap(Gmap gmap);

    /**
     * @param gmapMarker
     */
    public void saveOrUpdateGmapMarker(GmapMarker gmapMarker);

    /**
     * 
     * @param sessionId
     * @return
     */
    public List<GmapMarker> getGmapMarkersBySessionId(Long sessionId);

    /**
     * @param toolSessionId
     * @return
     */
    public GmapSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param gmapSession
     */
    public void saveOrUpdateGmapSession(GmapSession gmapSession);

    /**
     * 
     * @param userId
     * @param toolSessionId
     * @return
     */
    public GmapUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     * 
     * @param uid
     * @return
     */
    public GmapUser getUserByUID(Long uid);

    /**
     * 
     * @param gmapUser
     */
    public void saveOrUpdateGmapUser(GmapUser gmapUser);

    /**
     * 
     * @param user
     * @param gmapSession
     * @return
     */
    public GmapUser createGmapUser(UserDTO user, GmapSession gmapSession);

    /**
     * Updates the marker list for the gmap based upon a serialised xml version of the marker array
     * 
     * @param markerXML
     * @param gmap
     * @param guser
     * @param session
     */
    void updateMarkerListFromXML(String markerXML, Gmap gmap, GmapUser guser, boolean isAuthored, GmapSession session);

    /**
     * Creates a core notebook entry
     * 
     * @param id
     * @param idType
     * @param signature
     * @param userID
     * @param entry
     * @return
     */
    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    /**
     * Gets the entry from the database
     * 
     */
    NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID);

    /**
     * Updates an existing notebook entry
     * 
     */
    void updateEntry(NotebookEntry notebookEntry);

    /**
     * Get the Gmap config item by key
     * 
     * @param key
     * @return
     */
    public GmapConfigItem getConfigItem(String key);

    /**
     * Save a Gmap configItem
     * 
     * @param item
     */
    public void saveOrUpdateGmapConfigItem(GmapConfigItem item);

    /**
     * Returns whether activity is grouped and therefore it is expected more than one tool session.
     * 
     * @param toolContentID
     * @return
     */
    boolean isGroupedActivity(long toolContentID);
    
    /**
     * Audit log the teacher has started editing activity in monitor.
     * 
     * @param toolContentID
     */
    void auditLogStartEditingActivityInMonitor(long toolContentID);

    /** Get the list of users for a session for the monitoring tablesorter */
    List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting,
	    String searchString, boolean getNotebookEntries);
    
    /** Get the number of users in a session for the tablesorter */
    int getCountUsersBySession(Long sessionId, String searchString);
}
