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
package org.lamsfoundation.lams.tool.scratchie.service;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.scratchie.dto.GroupSummary;
import org.lamsfoundation.lams.tool.scratchie.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.Summary;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswer;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAttachment;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.ExcelCell;

/**
 * Interface that defines the contract that all ShareScratchie service provider must follow.
 * 
 * @author Andrey Balan
 */
public interface IScratchieService {

    /**
     * Get <code>Scratchie</code> by toolContentID.
     * 
     * @param contentId
     * @return
     */
    Scratchie getScratchieByContentId(Long contentId);
    
    /**
     * @param user
     * @param toolSessionId
     * @return
     */
    boolean isUserGroupLeader(ScratchieUser user, ScratchieSession session);
    
    /**
     * Set specified user as a leader. Also the previous leader (if any) is marked as non-leader.
     * 
     * @param userId
     * @param toolSessionId
     */
    ScratchieUser checkLeaderSelectToolForSessionLeader(ScratchieUser user, Long toolSessionId);
    
    ScratchieAnswer getScratchieAnswerById (Long answerUid);
    
    /**
     * Check user has the same scrathes logs as group leader. If not - creates missing ones. 
     * 
     * @param user
     * @param leader
     */
    void copyScratchesFromLeader(ScratchieUser user, ScratchieUser leader);

    /**
     * Get a cloned copy of tool default tool content (Scratchie) and assign the toolContentId of that copy as the given
     * <code>contentId</code>
     * 
     * @param contentId
     * @return
     * @throws ScratchieApplicationException
     */
    Scratchie getDefaultContent(Long contentId) throws ScratchieApplicationException;

    /**
     * Get list of scratchie items by given scratchieUid. These scratchie items must be created by author.
     * 
     * @param scratchieUid
     * @return
     */
    List getAuthoredItems(Long scratchieUid);

    /**
     * Upload instruciton file into repository.
     * 
     * @param file
     * @param type
     * @return
     * @throws UploadScratchieFileException
     */
    ScratchieAttachment uploadInstructionFile(FormFile file, String type) throws UploadScratchieFileException;

    // ********** for user methods *************
    /**
     * Create a new user in database.
     */
    void createUser(ScratchieUser scratchieUser);

    /**
     * Get user by given userID and toolContentID.
     * 
     * @param long1
     * @return
     */
    ScratchieUser getUserByIDAndContent(Long userID, Long contentId);

    /**
     * Get user by sessionID and UserID
     * 
     * @param userId
     * @param sessionId
     * @return
     */
    ScratchieUser getUserByIDAndSession(Long userId, Long sessionId);
    
    /**
     * Get users by given toolSessionId.
     * 
     * @param long1
     * @return
     */
    List<ScratchieUser> getUsersBySession(Long toolSessionId);
    
    /**
     * Save specified user.
     * 
     * @param long1
     * @return
     */
    void saveUser(ScratchieUser user);

    // ********** Repository methods ***********************
    /**
     * Delete file from repository.
     */
    void deleteFromRepository(Long fileUuid, Long fileVersionId) throws ScratchieApplicationException;

    /**
     * Save or update scratchie into database.
     * 
     * @param Scratchie
     */
    void saveOrUpdateScratchie(Scratchie Scratchie);

    /**
     * Delete reource attachment(i.e., offline/online instruction file) from database. This method does not delete the
     * file from repository.
     * 
     * @param attachmentUid
     */
    void deleteScratchieAttachment(Long attachmentUid);

    /**
     * Delete resoruce item from database.
     * 
     * @param uid
     */
    void deleteScratchieItem(Long uid);

    /**
     * Get scratchie which is relative with the special toolSession.
     * 
     * @param sessionId
     * @return
     */
    Scratchie getScratchieBySessionId(Long sessionId);

    /**
     * Get scratchie toolSession by toolSessionId
     * 
     * @param sessionId
     * @return
     */
    ScratchieSession getScratchieSessionBySessionId(Long sessionId);

    /**
     * Save or update scratchie session.
     * 
     * @param resSession
     */
    void saveOrUpdateScratchieSession(ScratchieSession resSession);
    
    /**
     * Fills in which order the student selects answers
     * 
     * @param items
     * @param user
     */
    void retrieveScratchesOrder(Collection<ScratchieItem> items, ScratchieUser user);

    /**
     * Fill in scratchieItems with information about whether they were unraveled; and answers with information on their scratched.
     * 
     * @param scratchieItemList
     * @param user
     */
    Set<ScratchieItem> getItemsWithIndicatedScratches(Long toolSessionId, ScratchieUser user);

    /**
     * Leader has scratched the specified answer. Will store this scratch for all users in his group.
     */
    void setAnswerAccess(Long scratchieItemUid, Long sessionId);
    
    int getUserMark(ScratchieSession session, Long userUid);
    
    /**
     * Mark all users in agroup as ScratchingFinished so that users can't continue scratching after this.
     * 
     * @param toolSessionId
     */
    void setScratchingFinished(Long toolSessionId);

    /**
     * If success return next activity's url, otherwise return null.
     * 
     * @param toolSessionId
     * @param userId
     * @return
     */
    String finishToolSession(Long toolSessionId, Long userId) throws ScratchieApplicationException;

    ScratchieItem getScratchieItemByUid(Long itemUid);
    
    List<GroupSummary> getMonitoringSummary(Long contentId);
    
    List<GroupSummary> getQuestionSummary(Long contentId, Long itemUid);
    
    /**
     * Export excel spreadheet
     * 
     * @param scratchie
     * @return
     */
    LinkedHashMap<String, ExcelCell[][]> exportExcel(Long contentId);

    /**
     * Get scratchie item <code>Summary</code> list according to sessionId and skipHide flag.
     * 
     * @param sessionId
     * @param skipHide
     *                true, don't get scratchie item if its <code>isHide</code> flag is true. Otherwise, get all
     *                scratchie item
     * @return
     */
    List<Summary> exportBySessionId(Long sessionId);

    List<List<Summary>> exportByContentId(Long contentId);

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
     * Get Reflection list grouped by sessionID.
     * 
     * @param contentId
     * @return
     */
    List<ReflectDTO> getReflectionList(Set<ScratchieUser> users);

    /**
     * Get user by UID
     * 
     * @param uid
     * @return
     */
    ScratchieUser getUser(Long uid);

    IEventNotificationService getEventNotificationService();

    /**
     * Gets a message from scratchie bundle. Same as <code><fmt:message></code> in JSP pages.
     * 
     * @param key
     *                key of the message
     * @return message content
     */
    String getMessage(String key);
    
    /**
     * Returns whether activity is grouped and therefore it is expected more than one tool session.
     * 
     * @param toolContentID
     * @return
     */
    boolean isGroupedActivity(long toolContentID);
    
    /**
     * Populates items with results, i.e. correctAnswer, userMark, userAttempts. Used for displaying this data on learner results page.
     * 
     * @param sessionId
     * @param userUid
     * @return
     */
    Set<ScratchieItem> populateItemsResults(Long sessionId, Long userUid);
    
    /**
     * Return all learners in activity
     * 
     * @param contentId
     * @return
     */
    Set<ScratchieUser> getAllLearners(Long contentId);
}
