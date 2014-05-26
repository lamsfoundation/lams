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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.learningdesign.DataFlowObject;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.vote.dto.SessionDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteQuestionDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author Ozgur Demirtas
 * 
 *         Interface that defines the contract Voting service provider must follow.
 */
public interface IVoteService {

    /**
     * @param user
     * @param toolSessionId
     * @return
     */
    boolean isUserGroupLeader(VoteQueUsr user, Long toolSessionId);

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
     * Generates data for all sessions in the Monitoring Summary, including all sessions summary.
     * 
     * @param toolContentID
     * @return
     */
    LinkedList<SessionDTO> getSessionDTOs(Long toolContentID);

    List<VoteMonitoredAnswersDTO> getOpenVotes(Long voteContentUid, Long currentSessionId, Long userId);

    List<ReflectionDTO> getReflectionData(VoteContent voteContent, Long userID);

    VoteContent getVoteContent(Long toolContentID);

    VoteUsrAttempt getAttemptByUID(Long uid);

    void createVoteQue(VoteQueContent voteQueContent);

    void createVoteSession(VoteSession voteSession);

    void createVoteQueUsr(VoteQueUsr voteQueUsr);

    VoteQueUsr getVoteUserBySession(final Long queUsrId, final Long sessionUid);

    VoteQueUsr getUserByUserId(Long userId);

    List getVoteUserBySessionUid(final Long voteSessionUid);

    int getCompletedVoteUserBySessionUid(final Long voteSessionUid);

    VoteQueContent getQuestionByDisplayOrder(final Long displayOrder, final Long voteContentUid);

    Set<String> getAttemptsForUserAndSessionUseOpenAnswer(final Long userUid, final Long sessionUid);

    Set getSessionUserEntriesSet(final Long voteSessionUid);

    void createVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt);

    void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt);

    List<VoteQueUsr> getUserBySessionOnly(final VoteSession voteSession);

    void hideOpenVote(VoteUsrAttempt voteUsrAttempt);

    void showOpenVote(VoteUsrAttempt voteUsrAttempt);

    int getAttemptsForQuestionContent(final Long questionUid);

    boolean studentActivityOccurredStandardAndOpen(VoteContent voteContent);

    int getUserEnteredVotesCountForContent(final Long voteContentUid);

    List<VoteUsrAttempt> getAttemptsForQuestionContentAndSessionUid(final Long questionUid, final Long voteSessionUid);

    int getStandardAttemptsForQuestionContentAndSessionUid(final Long questionUid, final Long voteSessionId);

    int getSessionEntriesCount(final Long voteSessionId);

    List<VoteUsrAttempt> getAttemptsForUserAndQuestionContent(final Long userUid, final Long questionUid);

    VoteUsrAttempt getAttemptForUserAndQuestionContentAndSession(final Long queUsrId, final Long questionUid,
	    final Long toolSessionUid);

    VoteQueContent getQuestionByUid(Long uid);

    void removeVoteQueContent(VoteQueContent voteQueContent);

    VoteQueContent getVoteQueContentByUID(Long uid);

    void saveOrUpdateVoteQueContent(VoteQueContent voteQueContent);

    List<VoteUsrAttempt> getStandardAttemptsByQuestionUid(final Long questionUid);
    
    VoteQueContent getDefaultVoteContentFirstQuestion();

    VoteQueUsr getVoteUserByUID(Long uid);

    void updateVoteUser(VoteQueUsr voteUser);

    void removeAttemptsForUserandSession(final Long queUsrId, final Long sessionUid);

    VoteQueUsr getUserById(long voteQueUsrId);

    VoteSession getSessionBySessionId(Long voteSessionId);

    void updateVote(VoteContent vote);

    void updateVoteSession(VoteSession voteSession);

    VoteSession getVoteSessionByUID(Long uid);

    /**
     * Get the count of all the potential learners for the vote session. This will include the people that have never
     * logged into the lesson. Not great, but it is a better estimate of how many users there will be eventually than
     * the number of people already known to the tool.
     * 
     * @param voteSessionId
     *            The tool session id
     */
    int getVoteSessionPotentialLearnersCount(Long voteSessionId);

    void deleteVote(VoteContent vote);

    void deleteVoteById(Long voteId);

    void deleteVoteSession(VoteSession voteSession);

    List getSessionNamesFromContent(VoteContent voteContent);

    void removeAttempt(VoteUsrAttempt attempt);

    void deleteVoteQueUsr(VoteQueUsr voteQueUsr);

    User getCurrentUserData(String username);

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

    void removeToolContent(Long toolContentID, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException;

    void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentID) throws ToolException;

    void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException;

    ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException, ToolException;

    IToolVO getToolBySignature(String toolSignature);

    long getToolDefaultContentIdBySignature(String toolSignature);

    List getToolSessionsForContent(VoteContent vote);

    List getAttemptsForUser(final Long userUid);

    int countSessionComplete();

    List getAllQuestionsSorted(final long voteContentId);

    List<Long> getSessionsFromContent(VoteContent mcContent);

    Set<String> getAttemptsForUserAndSession(final Long queUsrId, final Long voteSessionUid);

    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID);

    void removeQuestionsFromCache(VoteContent voteContent);
    
    void removeVoteContentFromCache(VoteContent voteContent);

    ToolOutput getToolInput(Long requestingToolContentId, Integer learnerId);

    List<DataFlowObject> getDataFlowObjects(Long toolContentId);

    void saveDataFlowObjectAssigment(DataFlowObject assignedDataFlowObject);

    DataFlowObject getAssignedDataFlowObject(Long toolContentId);

    /**
     * Returns whether activity is grouped and therefore it is expected more than one tool session.
     * 
     * @param toolContentID
     * @return
     */
    boolean isGroupedActivity(long toolContentID);
}