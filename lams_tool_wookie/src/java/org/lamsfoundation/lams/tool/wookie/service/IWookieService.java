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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.wookie.service;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.wookie.model.Wookie;
import org.lamsfoundation.lams.tool.wookie.model.WookieConfigItem;
import org.lamsfoundation.lams.tool.wookie.model.WookieSession;
import org.lamsfoundation.lams.tool.wookie.model.WookieUser;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Wookie Service
 */
public interface IWookieService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     *
     * @params newContentID
     * @return
     */
    public Wookie copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Wookie tools default content.
     *
     * @return
     */
    public Wookie getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public Wookie getWookieByContentId(Long toolContentID);

    /**
     * @param wookie
     */
    public void saveOrUpdateWookie(Wookie wookie);

    /**
     * @param toolSessionId
     * @return
     */
    public WookieSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param wookieSession
     */
    public void saveOrUpdateWookieSession(WookieSession wookieSession);

    /**
     *
     * @param userId
     * @param toolSessionId
     * @return
     */
    public WookieUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     *
     * @param uid
     * @return
     */
    public WookieUser getUserByUID(Long uid);

    /**
     *
     * @param wookieUser
     */
    public void saveOrUpdateWookieUser(WookieUser wookieUser);

    /**
     *
     * @param user
     * @param wookieSession
     * @return
     */
    public WookieUser createWookieUser(UserDTO user, WookieSession wookieSession);

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
     * Helper method to extract file extension from a string
     *
     * @param fileName
     * @return
     */
    String getFileExtension(String fileName);

    /**
     * Get the wookie config item by key
     *
     * @param key
     * @return
     */
    public WookieConfigItem getConfigItem(String key);

    /**
     * Save a wookie configItem
     *
     * @param item
     */
    public void saveOrUpdateWookieConfigItem(WookieConfigItem item);

    /**
     * Get the wookie URL
     *
     * @return
     */
    public String getWookieURL();

    /**
     * Get the wookie api key
     *
     * @return
     */
    public String getWookieAPIKey();

    /**
     * Get an I18n message by key
     * 
     * @param key
     * @return
     */
    public String getMessage(String key);

}
