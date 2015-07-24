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
package org.lamsfoundation.lams.tool.peerreview.service;

import java.util.List;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.rating.ToolRatingManager;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.tool.peerreview.dto.GroupSummary;
import org.lamsfoundation.lams.tool.peerreview.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewSession;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;

/**
 * Interface that defines the contract that all Peerreview service provider must follow.
 * 
 * @author Andrey Balan
 */
public interface IPeerreviewService extends ToolRatingManager {

    /**
     * Get <code>Peerreview</code> by toolContentID.
     * 
     * @param contentId
     * @return
     */
    Peerreview getPeerreviewByContentId(Long contentId);

    /**
     * Get a cloned copy of tool default tool content (Peerreview) and assign the toolContentId of that copy as the
     * given <code>contentId</code>
     * 
     * @param contentId
     * @return
     * @throws PeerreviewApplicationException
     */
    Peerreview getDefaultContent(Long contentId) throws PeerreviewApplicationException;

    // ********** for user methods *************
    /**
     * Create a new user in database.
     */
    void createUser(PeerreviewUser peerreviewUser);
    
    List<PeerreviewUser> getUsersBySession(Long toolSessionId);
    
    List<PeerreviewUser> getUsersByContent(Long toolContentId);

    /**
     * Get user by given userID and toolContentID.
     * 
     * @param long1
     * @return
     */
    PeerreviewUser getUserByIDAndContent(Long userID, Long contentId);

    /**
     * Get user by sessionID and UserID
     * 
     * @param long1
     * @param sessionId
     * @return
     */
    PeerreviewUser getUserByIDAndSession(Long long1, Long sessionId);

    /**
     * Save or update peerreview into database.
     * 
     * @param Peerreview
     */
    void saveOrUpdatePeerreview(Peerreview Peerreview);

    /**
     * Get peerreview which is relative with the special toolSession.
     * 
     * @param sessionId
     * @return
     */
    Peerreview getPeerreviewBySessionId(Long sessionId);

    /**
     * Get peerreview toolSession by toolSessionId
     * 
     * @param sessionId
     * @return
     */
    PeerreviewSession getPeerreviewSessionBySessionId(Long sessionId);

    /**
     * Save or update peerreview session.
     * 
     * @param resSession
     */
    void saveOrUpdatePeerreviewSession(PeerreviewSession resSession);
    
    /**
     * Sets user.setSessionFinished(true)
     * 
     * @param toolSessionId
     * @param userId
     */
    void markUserFinished(Long toolSessionId, Long userId);

    /**
     * If success return next activity's url, otherwise return null.
     * 
     * @param toolSessionId
     * @param userId
     * @return
     */
    String finishToolSession(Long toolSessionId, Long userId) throws PeerreviewApplicationException;

    /**
     * Return monitoring summary list. The return value is list of peerreview summaries for each groups.
     * 
     * @param contentId
     * @return
     */
    List<GroupSummary> getGroupSummaries(Long contentId);
    
    List<PeerreviewUser> getUsersForTablesorter(final Long qaSessionId, final Long excludeUserId,
	    int page, int size, int sorting);
    
    int getCountUsersBySession(final Long qaSessionId, final Long excludeUserId);

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
    PeerreviewUser getUser(Long uid);
    
    /**
     * @param toolSessionId
     * @return
     */
    List<PeerreviewUser> fetchUsersFromLesson(Long toolSessionId);

    /**
     * Returns whether activity is grouped and therefore it is expected more than one tool session.
     * 
     * @param toolContentID
     * @return
     */
    boolean isGroupedActivity(long toolContentID);
    
    int getCommentsMinWordsLimit(Long toolContentId);
    
    /**
     * Returns item DTO with all corresponding ratings and comments. Doesn't contain average and total amount of rates.
     * 
     * @param contentId
     * @param itemId
     * @return
     */
    ItemRatingDTO getRatingCriteriaDtoWithActualRatings(Long contentId, Long itemId);
}
