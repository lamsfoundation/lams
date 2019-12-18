/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.learningdesign.DataFlowObject;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ICommonToolService;
import org.lamsfoundation.lams.tool.vote.dto.OpenTextAnswerDTO;
import org.lamsfoundation.lams.tool.vote.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.vote.dto.SessionDTO;
import org.lamsfoundation.lams.tool.vote.dto.SummarySessionDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteQuestionDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteStatsDTO;
import org.lamsfoundation.lams.tool.vote.model.VoteContent;
import org.lamsfoundation.lams.tool.vote.model.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.model.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.model.VoteSession;
import org.lamsfoundation.lams.tool.vote.model.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.util.VoteApplicationException;
import org.lamsfoundation.lams.util.MessageService;

/**
 * @author Ozgur Demirtas
 *
 *         Interface that defines the contract Voting service provider must follow.
 */
public interface IVoteService extends ICommonToolService {

    /**
     * @return Returns the MessageService.
     */
    MessageService getMessageService();

    /**
     *
     * /**
     *
     * @param user
     * @param toolSessionId
     * @return
     */
    boolean isUserGroupLeader(Long userId, Long toolSessionId);

    /**
     * Set specified user as a leader. Also the previous leader (if any) is marked as non-leader.
     *
     * @param userId
     * @param toolSessionID
     */
    VoteQueUsr checkLeaderSelectToolForSessionLeader(VoteQueUsr user, Long toolSessionID);

    /**
     * Check user has the same answers logs as group leader. If not - creates missing ones.
     *
     * @param user
     * @param leader
     */
    void copyAnswersFromLeader(VoteQueUsr user, VoteQueUsr leader);

    /**
     * Generates chart data for the learner module and monitoring module Summary tab (Individual Sessions mode)
     *
     * @param request
     * @param voteService
     * @param toolContentID
     * @param toolSessionUid
     */
    VoteGeneralLearnerFlowDTO prepareChartData(HttpServletRequest request, Long toolContentID, Long toolSessionUid,
	    VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO);

    /**
     * Generates data for all sessions, including all sessions summary.
     *
     * @param toolContentID
     * @return
     */
    LinkedList<SessionDTO> getSessionDTOs(Long toolContentID);

    /**
     * Generates data for all sessions in the Monitoring Summary, including all sessions summary.
     *
     * @param toolContentID
     * @return
     */
    SortedSet<SummarySessionDTO> getMonitoringSessionDTOs(Long toolContentID);

    List<VoteMonitoredAnswersDTO> getOpenVotes(Long voteContentUid, Long currentSessionId, Long userId);

    List<ReflectionDTO> getReflectionData(VoteContent voteContent, Long userID);

    VoteContent getVoteContent(Long toolContentID);

    VoteUsrAttempt getAttemptByUID(Long uid);

    void createVoteQueUsr(VoteQueUsr voteQueUsr);

    VoteQueUsr getVoteUserBySession(final Long queUsrId, final Long sessionUid);

    VoteQueUsr getUserByUserId(Long userId);

    VoteQueContent getQuestionByDisplayOrder(final Long displayOrder, final Long voteContentUid);

    Set<String> getAttemptsForUserAndSessionUseOpenAnswer(final Long userUid, final Long sessionUid);

    void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt);

    List<VoteQueUsr> getUserBySessionOnly(final VoteSession voteSession);

    void hideOpenVote(VoteUsrAttempt voteUsrAttempt);

    void showOpenVote(VoteUsrAttempt voteUsrAttempt);

    List<VoteUsrAttempt> getAttemptsForQuestionContentAndSessionUid(final Long questionUid, final Long voteSessionUid);

    List<VoteUsrAttempt> getAttemptsForUserAndQuestionContent(final Long userUid, final Long questionUid);

    /**
     * creates a new vote record in the database
     */
    void createAttempt(VoteQueUsr voteQueUsr, Map<String, String> mapGeneralCheckedOptionsContent, String userEntry,
	    VoteSession voteSession, Long voteContentUid);

    VoteQueContent getQuestionByUid(Long uid);

    void removeVoteQueContent(VoteQueContent voteQueContent);

    VoteQueContent getVoteQueContentByUID(Long uid);

    void saveOrUpdateVoteQueContent(VoteQueContent voteQueContent);

    VoteContent createQuestions(List<VoteQuestionDTO> questionDTOs, VoteContent voteContent);

    /**
     * Build a map of the display ids -> nomination text. If checkedOptions != null then only include the display ids in
     * the checkedOptions list.
     *
     * @param request
     * @param voteContent
     *            the content of the vote from the database
     * @param checkedOptions
     *            collection of String display IDs to which to restrict the map (optional)
     * @return Map of display id -> nomination text.
     */
    Map<String, String> buildQuestionMap(VoteContent voteContent, Collection<String> checkedOptions);

    void updateVoteUser(VoteQueUsr voteUser);

    void removeAttemptsForUserandSession(final Long queUsrId, final Long sessionUid);

    VoteQueUsr getUserById(long voteQueUsrId);

    VoteSession getSessionBySessionId(Long voteSessionId);

    void updateVote(VoteContent vote);

    VoteSession getVoteSessionByUID(Long uid);

    int getTotalNumberOfUsers();

    void saveVoteContent(VoteContent vote);

    /**
     * checks the parameter content in the user responses table
     *
     * @param voteContent
     * @return boolean
     * @throws VoteApplicationException
     */
    boolean studentActivityOccurredGlobal(VoteContent voteContent);

    void recalculateUserAnswers(VoteContent content, Set<VoteQueContent> oldQuestions,
	    List<VoteQuestionDTO> questionDTOs, List<VoteQuestionDTO> deletedQuestions);

    void copyToolContent(Long fromContentId, Long toContentId) throws ToolException;

    void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentID) throws ToolException;

    void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException;

    ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    ToolSessionExportOutputData exportToolSession(List<Long> toolSessionIds) throws DataMissingException, ToolException;

    Tool getToolBySignature(String toolSignature);

    long getToolDefaultContentIdBySignature(String toolSignature);

    List<Long> getToolSessionsForContent(VoteContent vote);

    List<VoteUsrAttempt> getAttemptsForUser(final Long userUid);

    int countSessionComplete();

    List<VoteQueContent> getAllQuestionsSorted(final long voteContentId);

    List<Long> getSessionsFromContent(VoteContent mcContent);

    Set<String> getAttemptsForUserAndSession(final Long queUsrId, final Long voteSessionUid);

    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID);

    void updateEntry(NotebookEntry notebookEntry);

    void removeQuestionsFromCache(VoteContent voteContent);

    void removeVoteContentFromCache(VoteContent voteContent);

    ToolOutput getToolInput(Long requestingToolContentId, Integer learnerId);

    List<DataFlowObject> getDataFlowObjects(Long toolContentId);

    void saveDataFlowObjectAssigment(DataFlowObject assignedDataFlowObject);

    DataFlowObject getAssignedDataFlowObject(Long toolContentId);

    /**
     * Gets the basic details about an attempt for a nomination. questionUid must not be null, sessionUid may be NULL.
     * This is
     * unusual for these methods - usually sessionId may not be null. In this case if sessionUid is null then you get
     * the values for the whole class, not just the group.
     *
     * Will return List<[login (String), fullname(String), attemptTime(Timestamp]>
     */
    List<Object[]> getUserAttemptsForTablesorter(Long sessionUid, Long questionUid, int page, int size, int sorting,
	    String searchString);

    int getCountUsersBySession(Long sessionUid, Long questionUid, String searchString);

    List<Object[]> getUserReflectionsForTablesorter(Long sessionUid, int page, int size, int sorting,
	    String searchString);

    List<VoteStatsDTO> getStatisticsBySession(Long toolContentId);

    /** Gets the details for the open text nominations */
    List<OpenTextAnswerDTO> getUserOpenTextAttemptsForTablesorter(Long sessionUid, Long contentUid, int page, int size,
	    int sorting, String searchStringVote, String searchStringUsername);

    int getCountUsersForOpenTextEntries(Long sessionUid, Long contentUid, String searchStringVote,
	    String searchStringUsername);
}