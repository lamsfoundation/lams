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


package org.lamsfoundation.lams.tool.pixlr.service;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.pixlr.model.Pixlr;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrConfigItem;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrSession;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrUser;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Pixlr Service
 */
public interface IPixlrService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     *
     * @params newContentID
     * @return
     */
    public Pixlr copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Pixlr tools default content.
     *
     * @return
     */
    public Pixlr getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public Pixlr getPixlrByContentId(Long toolContentID);

    /**
     * @param pixlr
     */
    public void saveOrUpdatePixlr(Pixlr pixlr);

    /**
     * @param toolSessionId
     * @return
     */
    public PixlrSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param pixlrSession
     */
    public void saveOrUpdatePixlrSession(PixlrSession pixlrSession);

    /**
     *
     * @param userId
     * @param toolSessionId
     * @return
     */
    public PixlrUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     *
     * @param uid
     * @return
     */
    public PixlrUser getUserByUID(Long uid);

    /**
     *
     * @param pixlrUser
     */
    public void saveOrUpdatePixlrUser(PixlrUser pixlrUser);

    /**
     *
     * @param user
     * @param pixlrSession
     * @return
     */
    public PixlrUser createPixlrUser(UserDTO user, PixlrSession pixlrSession);

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
     * Get the pixlr config item by key
     *
     * @param key
     * @return
     */
    public PixlrConfigItem getConfigItem(String key);

    /**
     * Save a pixlr configItem
     *
     * @param item
     */
    public void saveOrUpdatePixlrConfigItem(PixlrConfigItem item);

    /**
     * Returns whether activity is grouped and therefore it is expected more than one tool session.
     *
     * @param toolContentID
     * @return
     */
    boolean isGroupedActivity(long toolContentID);

}
