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

package org.lamsfoundation.lams.tool.daco.service;

import java.util.Collection;
import java.util.List;

import org.lamsfoundation.lams.tool.daco.dao.DacoAnswerDAO;
import org.lamsfoundation.lams.tool.daco.dto.MonitoringSummarySessionDTO;
import org.lamsfoundation.lams.tool.daco.dto.QuestionSummaryDTO;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswer;
import org.lamsfoundation.lams.tool.daco.model.DacoQuestion;
import org.lamsfoundation.lams.tool.daco.model.DacoSession;
import org.lamsfoundation.lams.tool.daco.model.DacoUser;
import org.lamsfoundation.lams.tool.service.ICommonToolService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Marcin Cieslak
 *
 *         Interface that defines the contract that all Data Collection service provider must follow.
 */
public interface IDacoService extends ICommonToolService {

    /**
     * Get <code>Daco</code> by toolContentID.
     *
     * @param contentId
     * @return
     */
    Daco getDacoByContentId(Long contentId);

    /**
     * Get a cloned copy of tool default tool content (Daco) and assign the toolContentId of that copy as the given
     * <code>contentId</code>
     *
     * @param contentId
     * @return
     * @throws DacoApplicationException
     */
    Daco getDefaultContent(Long contentId) throws DacoApplicationException;

    /**
     * Upload daco answer file to repository
     *
     * @param answer
     * @param file
     * @throws UploadDacoFileException
     */
    void uploadDacoAnswerFile(DacoAnswer answer, MultipartFile file) throws UploadDacoFileException;

    // ********** for user methods *************
    /**
     * Create a new user in database.
     */
    void createUser(DacoUser dacoUser);

    /**
     * Get user by given userID and toolContentID.
     *
     * @param long1
     * @return
     */
    DacoUser getUserByUserIdAndContentId(Long userID, Long contentId);

    /**
     * Get user by sessionID and UserID
     *
     * @param long1
     * @param sessionId
     * @return
     */
    DacoUser getUserByUserIdAndSessionId(Long long1, Long sessionId);

    /**
     * Save or update daco into database.
     *
     * @param Daco
     */
    void saveOrUpdateDaco(Daco Daco);

    void saveOrUpdateAnswer(DacoAnswer answer);

    /**
     * Delete question from database.
     *
     * @param uid
     */
    void deleteDacoQuestion(Long uid);

    void deleteDacoAnswer(Long uid);

    void deleteDacoRecord(List<DacoAnswer> record);

    /**
     * Return all answers for a given user, grouped by record.
     *
     * @param user
     *
     * @return
     */
    List<List<DacoAnswer>> getDacoAnswersByUser(DacoUser user);

    /**
     * Get daco which is relative with the special toolSession.
     *
     * @param sessionId
     * @return
     */
    Daco getDacoBySessionId(Long sessionId);

    /**
     * Get daco toolSession by toolSessionId
     *
     * @param sessionId
     * @return
     */
    DacoSession getSessionBySessionId(Long sessionId);

    /**
     * Save or update daco session.
     *
     * @param resSession
     */
    void saveOrUpdateDacoSession(DacoSession resSession);

    /**
     * If success return next activity's url, otherwise return null.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    String finishToolSession(Long toolSessionId, Long userId) throws DacoApplicationException;

    DacoQuestion getDacoQuestionByUid(Long questionUid);

    /**
     * Get user by UID
     *
     * @param uid
     * @return
     */
    DacoUser getUser(Long uid);

    /**
     * Gets a message from resource bundle. Same as <code><fmt:message></code> in JSP pages.
     *
     * @param key
     *            key of the message
     * @param args
     *            arguments for the message
     * @return message content
     */
    String getLocalisedMessage(String key, Object[] args);

    void notifyTeachersOnLearnerEntry(Long sessionId, DacoUser dacoUser);

    void notifyTeachersOnRecordSumbit(Long sessionId, DacoUser dacoUser);

    /**
     * Returns summaries for particular questions. A list of {@link QuestionSummaryDTO question summaries} is created,
     * one for each question. They are filled with default, blank data. Then the proper summaries are
     * {@link DacoAnswerDAO#getQuestionSummaries(Long, List) read} from the database.
     *
     * @param userUid
     *            user for who the summary should be created
     * @return list of question summaries
     */
    List<QuestionSummaryDTO> getQuestionSummaries(Long userUid);

    /**
     * Removes a Daco object and all of its Questions from Hibernate cache. It is required to avoid errors when same
     * object was read from the database twice and one of the copies is being saved.
     *
     * @param daco
     *            object to release
     */
    void releaseDacoFromCache(Daco daco);

    /**
     * Removes Answers from Hibernate cache. It is required to avoid errors when same object was read from the database
     * twice and one of the copies is being saved.
     *
     * @param answers
     *            collection of answers to remove from cache
     */
    void releaseAnswersFromCache(Collection<DacoAnswer> answers);

    /**
     * Gets the number of records in the group. It uses database connection.
     *
     * @param sessionId
     *            session ID of the group
     * @return number of records in that group
     */
    Integer getGroupRecordCount(Long sessionId);

    /**
     * Gets the number of records in the group. It uses provided monitoring summary.
     *
     * @param monitoringSummary
     *            summary which will be iterated through and the records counted
     * @return number of records in that group
     */
    Integer getGroupRecordCount(MonitoringSummarySessionDTO monitoringSummary);

    /**
     * Creates summary that is later used in the monitoring. Does not include users.
     *
     * @param contentId
     *            ID of Daco for which the summary should be created
     * @param userUid
     *            ID of the user for who the summary details should be created; <code>null</code> if the summary
     *            details should be created for all users; <code>< 0 </code> if the summary details should be
     *            created for noone
     * @return list of monitoring summaries, one for each session
     */
    List<MonitoringSummarySessionDTO> getMonitoringSummary(Long contentId, Long userUid);

    /**
     * Get the detailed records for a user (userId != null) or group (userId == null). SessionId should
     * always be set.
     */
    MonitoringSummarySessionDTO getAnswersAsRecords(final Long sessionId, final Long userId, int sorting);

    /**
     * Creates summary that is later used in the export.
     *
     * @param contentId
     *            ID of Daco for which the summary should be created
     * @param userUid
     *            ID of the user for who the summary details should be created; <code>null</code> if the summary
     *            details should be created for all users; <code>< 0 </code> if the summary details should be
     *            created for noone
     * @return list of monitoring summaries, one for each session
     */
    List<MonitoringSummarySessionDTO> getSummaryForExport(Long contentId, Long userUid);

    /** Get a paged user list for monitoring */
    List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting, String searchString);

    int getCountUsersBySession(final Long sessionId, String searchString);

    /** Get the basic statistics for all sessions. Used by Monitoring */
    List<MonitoringSummarySessionDTO> getSessionStatistics(Long toolContentUid);

    /**
     * Get how many records has the given user posted.
     *
     * @param userID
     * @param sessionId
     * @return
     */
    int getRecordNum(Long userID, Long sessionId);
}
