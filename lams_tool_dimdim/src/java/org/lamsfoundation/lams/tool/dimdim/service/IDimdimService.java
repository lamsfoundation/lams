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
/* $Id$ */

package org.lamsfoundation.lams.tool.dimdim.service;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.dimdim.model.Dimdim;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimAttachment;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimConfig;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimSession;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimUser;
import org.lamsfoundation.lams.tool.dimdim.util.DimdimException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Dimdim Service
 */
public interface IDimdimService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     * 
     * @params newContentID
     * @return
     */
    public Dimdim copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Dimdim tools default content.
     * 
     * @return
     */
    public Dimdim getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public Dimdim getDimdimByContentId(Long toolContentID);

    /**
     * @param toolContentId
     * @param file
     * @param type
     * @return
     */
    public DimdimAttachment uploadFileToContent(Long toolContentId, FormFile file, String type);

    /**
     * @param uuid
     * @param versionID
     */
    public void deleteFromRepository(Long uuid, Long versionID) throws DimdimException;

    /**
     * @param dimdim
     */
    public void saveOrUpdateDimdim(Dimdim dimdim);

    /**
     * @param toolSessionId
     * @return
     */
    public DimdimSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param dimdimSession
     */
    public void saveOrUpdateDimdimSession(DimdimSession dimdimSession);

    /**
     * 
     * @param userId
     * @param toolSessionId
     * @return
     */
    public DimdimUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     * 
     * @param uid
     * @return
     */
    public DimdimUser getUserByUID(Long uid);

    /**
     * 
     * @param dimdimUser
     */
    public void saveOrUpdateDimdimUser(DimdimUser dimdimUser);

    /**
     * 
     * @param user
     * @param dimdimSession
     * @return
     */
    public DimdimUser createDimdimUser(UserDTO user, DimdimSession dimdimSession);

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
    DimdimConfig getConfigEntry(String key);

    /**
     * 
     * @param key
     * @param value
     */
    void saveOrUpdateConfigEntry(DimdimConfig dimdimConfig);

    /**
     * 
     * @param userDTO
     * @param meetingKey
     * @param topic
     * @return
     */
    String getDimdimStartConferenceURL(UserDTO userDTO, String meetingKey, String topic) throws Exception;

    /**
     * 
     * @param userDTOm
     * @param meetingKey
     * @return
     */
    String getDimdimJoinConferenceURL(UserDTO userDTO, String meetingKey) throws Exception;

}
