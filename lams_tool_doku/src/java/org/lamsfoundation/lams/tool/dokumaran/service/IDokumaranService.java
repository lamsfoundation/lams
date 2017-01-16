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

import java.util.List;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.dokumaran.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.dokumaran.dto.SessionDTO;
import org.lamsfoundation.lams.tool.dokumaran.model.Dokumaran;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranConfigItem;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranSession;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranUser;

import net.gjerull.etherpad.client.EPLiteClient;

/**
 * @author Dapeng.Ni
 *
 *         Interface that defines the contract that all ShareDokumaran service provider must follow.
 */
public interface IDokumaranService {

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
     */
    DokumaranUser checkLeaderSelectToolForSessionLeader(DokumaranUser user, Long toolSessionId);
    
    String getEtherpadReadOnlyId(String padId) throws DokumaranConfigurationException;
    
    /**
     * Stores etherpadReadOnlyId to the session.
     * 
     * @param toolSessionId
     * @param etherpadReadOnlyId
     */
    void setEtherpadReadOnlyId(Long toolSessionId, String etherpadReadOnlyId);
    
    /**
     * Creates EPLiteClient that will make calls to Etherpad server. Throws DokumaranConfigurationException tf the tool
     * is not configured appropriately (either etherpadServerUrl or etherpadApiKey is missing).
     * 
     * @return
     * @throws DokumaranConfigurationException
     */
    EPLiteClient initializeEPLiteClient() throws DokumaranConfigurationException;

    // ********** for user methods *************
    /**
     * Create a new user in database.
     */
    void createUser(DokumaranUser dokumaranUser);

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
    
    DokumaranConfigItem getConfigItem(String key);

    void saveOrUpdateDokumaranConfigItem(DokumaranConfigItem item);

    /**
     * Save or update dokumaran into database.
     *
     * @param Dokumaran
     */
    void saveOrUpdateDokumaran(Dokumaran Dokumaran);

    /**
     * Get dokumaran which is relative with the special toolSession.
     *
     * @param sessionId
     * @return
     */
    Dokumaran getDokumaranBySessionId(Long sessionId);

    /**
     * Get dokumaran toolSession by toolSessionId
     *
     * @param sessionId
     * @return
     */
    DokumaranSession getDokumaranSessionBySessionId(Long sessionId);

    /**
     * Save or update dokumaran session.
     *
     * @param resSession
     */
    void saveOrUpdateDokumaranSession(DokumaranSession resSession);

    /**
     * If success return next activity's url, otherwise return null.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    String finishToolSession(Long toolSessionId, Long userId) throws DokumaranApplicationException;

    /**
     * Return monitoring summary list. The return value is list of dokumaran summaries for each groups.
     *
     * @param contentId
     * @return
     */
    List<SessionDTO> getSummary(Long contentId);

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

    /**
     * Returns whether activity is grouped and therefore it is expected more than one tool session.
     *
     * @param toolContentID
     * @return
     */
    boolean isGroupedActivity(long toolContentID);
}
