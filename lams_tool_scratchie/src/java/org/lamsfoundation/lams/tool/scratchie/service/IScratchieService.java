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

package org.lamsfoundation.lams.tool.scratchie.service;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.tomcat.util.json.JSONException;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.scratchie.dto.BurningQuestionItemDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.GroupSummary;
import org.lamsfoundation.lams.tool.scratchie.dto.LeaderResultsDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswer;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieBurningQuestion;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieConfigItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.util.ExcelCell;
import org.quartz.SchedulerException;

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
     * Populate scratchie items with the confidence levels from the activity specified in author
     * 
     * @param userId
     * @param toolSessionId
     * @param confidenceLevelsActivityUiid
     * @param items
     */
    void populateItemsWithConfidenceLevels(Long userId, Long toolSessionId, Integer confidenceLevelsActivityUiid,
	    Collection<ScratchieItem> items);
    
    /**
     * Returns all activities that precede specified activity and produce confidence levels.
     * 
     * @param toolContentId toolContentId of the specified activity 
     * @return
     */
    Set<ToolActivity> getPrecedingConfidenceLevelsActivities(Long toolContentId);

    /**
     * Set specified user as a leader. Also the previous leader (if any) is marked as non-leader.
     *
     * @param userId
     * @param toolSessionId
     */
    ScratchieUser checkLeaderSelectToolForSessionLeader(ScratchieUser user, Long toolSessionId);
    
    /**
     * Stores date when user has started activity with time limit.
     * 
     * @param sessionId
     * @throws SchedulerException 
     */
    void launchTimeLimit(Long sessionId) throws SchedulerException;
    
    /**
     * Checks if non-leaders should still wait for leader to submit either notebook or burning questions.
     * 
     * @param toolSession
     * @return
     */
    boolean isWaitingForLeaderToSubmit(ScratchieSession toolSession);

    List<ScratchieBurningQuestion> getBurningQuestionsBySession(Long sessionId);

    /**
     * Save or update burningQuestion into database.
     *
     * @param sessionId
     * @param itemUid
     *            item uid, if it's null - it signifies general burning question
     * @param question
     */
    void saveBurningQuestion(Long sessionId, Long itemUid, String question);

    ScratchieAnswer getScratchieAnswerByUid(Long answerUid);

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

    // ********** for user methods *************
    /**
     * Create a new user in database.
     */
    void createUser(ScratchieUser scratchieUser);

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

    /**
     * Save or update scratchie into database.
     *
     * @param Scratchie
     */
    void saveOrUpdateScratchie(Scratchie scratchie);

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
    void getScratchesOrder(Collection<ScratchieItem> items, Long toolSessionId);

    /**
     * Fill in scratchieItems with information about whether they were unraveled; and answers with information on their
     * scratched.
     *
     * @param scratchieItemList
     * @param item
     *            item parameter is optional. In case it's provided - these item collection is used instead of quering
     *            DB
     */
    Collection<ScratchieItem> getItemsWithIndicatedScratches(Long toolSessionId);

    /**
     * The same as getItemsWithIndicatedScratches(Long toolSessionId), but items are provided as parameter and not
     * queried from DB.
     *
     * @param scratchieItemList
     * @param item
     *            this item collection is used instead of quering DB
     */
    Collection<ScratchieItem> getItemsWithIndicatedScratches(Long toolSessionId, Collection<ScratchieItem> items);

    /**
     * Leader has scratched the specified answer. Will store this scratch for all users in his group. It will also
     * update all the marks.
     */
    void recordItemScratched(Long toolSessionId, Long scratchieItemUid);

    void recalculateMarkForSession(Long sessionId, boolean isPropagateToGradebook);

    /**
     * Mark all users in agroup as ScratchingFinished so that users can't continue scratching after this.
     *
     * @param toolSessionId
     * @throws IOException 
     * @throws JSONException 
     */
    void setScratchingFinished(Long toolSessionId) throws JSONException, IOException;

    /**
     * If success return next activity's url, otherwise return null.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    String finishToolSession(Long toolSessionId, Long userId) throws ScratchieApplicationException;

    ScratchieItem getScratchieItemByUid(Long itemUid);

    /**
     * @param contentId
     * @param isIncludeOnlyLeaders
     *            if true - return Summaries only for leader, all users in a group otherwise
     * @return
     */
    List<GroupSummary> getMonitoringSummary(Long contentId, boolean isIncludeOnlyLeaders);

    List<GroupSummary> getQuestionSummary(Long contentId, Long itemUid);

    /**
     * In order to group BurningQuestions by items, organise them as a list of BurningQuestionItemDTOs.
     *
     * @param scratchie
     * @param sessionId
     *            optional parameter, if it's specified, BurningQuestionDTOs will also contain information what leader
     *            of this group has liked
     * @return
     */
    List<BurningQuestionItemDTO> getBurningQuestionDtos(Scratchie scratchie, Long sessionId);

    boolean addLike(Long burningQuestionUid, Long sessionId);

    void removeLike(Long burningQuestionUid, Long sessionId);

    /**
     * Export excel spreadheet
     *
     * @param scratchie
     * @return
     */
    LinkedHashMap<String, ExcelCell[][]> exportExcel(Long contentId);

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
    List<ReflectDTO> getReflectionList(Long contentId);

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
     *            key of the message
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
     * Audit log the teacher has started editing activity in monitor.
     * 
     * @param toolContentID
     */
    void auditLogStartEditingActivityInMonitor(long toolContentID);

    /**
     * Return all leaders in activity for all groups
     *
     * @param contentId
     * @return
     */
    Set<ScratchieUser> getAllLeaders(Long contentId);

    void changeUserMark(Long userId, Long userUid, Integer newMark);

    /**
     * Recalculate marks after editing content from monitoring.
     *
     * @param scratchie
     * @param oldItems
     * @param newItems
     */
    void recalculateUserAnswers(Scratchie scratchie, Set<ScratchieItem> oldItems, Set<ScratchieItem> newItems,
	    List<ScratchieItem> deletedItems);

    void releaseItemsFromCache(Scratchie scratchie);

    ScratchieConfigItem getConfigItem(String key);

    void saveOrUpdateScratchieConfigItem(ScratchieConfigItem item);
    
    /** Get the raw marks for display in a graph in monitoring */
    List<Number> getMarksArray(Long contentId);

    /** Get the statistics such as average, max, min for the marks. Used in monitoring */
    LeaderResultsDTO getLeaderResultsDTOForLeaders(Long contentId);

}
