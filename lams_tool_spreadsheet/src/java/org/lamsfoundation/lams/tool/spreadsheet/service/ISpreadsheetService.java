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

package org.lamsfoundation.lams.tool.spreadsheet.service;

import java.util.List;

import org.lamsfoundation.lams.tool.service.ICommonToolService;
import org.lamsfoundation.lams.tool.spreadsheet.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.spreadsheet.dto.Summary;
import org.lamsfoundation.lams.tool.spreadsheet.model.Spreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetSession;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;
import org.lamsfoundation.lams.tool.spreadsheet.model.UserModifiedSpreadsheet;
import org.lamsfoundation.lams.util.MessageService;

/**
 * @author Andrey Balan
 *
 *         Interface that defines the contract that all ShareSpreadsheet service provider must follow.
 */
public interface ISpreadsheetService extends ICommonToolService {

    /**
     * Get <code>Spreadsheet</code> by toolContentID.
     *
     * @param contentId
     * @return
     */
    Spreadsheet getSpreadsheetByContentId(Long contentId);

    /**
     * Get a cloned copy of tool default tool content (Spreadsheet) and assign the toolContentId of that copy as the
     * given <code>contentId</code>
     *
     * @param contentId
     * @return
     * @throws SpreadsheetApplicationException
     */
    Spreadsheet getDefaultContent(Long contentId) throws SpreadsheetApplicationException;

    //********** user methods *************
    /**
     * Save or update SpreadsheetUser in database, mostly for saving userModifiedSpreadsheet.
     *
     * @param user
     *            user which did modifications to spreadsheet
     */
    void saveOrUpdateUser(SpreadsheetUser spreadsheetUser);

    /**
     * Save or update UserModifiedSpreadsheet in database.
     *
     * @param userModifiedSpreadsheet
     */
    void saveOrUpdateUserModifiedSpreadsheet(UserModifiedSpreadsheet userModifiedSpreadsheet);

    /**
     * Get user by given userID and toolContentID.
     *
     * @param long1
     * @return
     */
    SpreadsheetUser getUserByIDAndContent(Long userID, Long contentId);

    /**
     * Get user by sessionID and UserID
     *
     * @param long1
     * @param sessionId
     * @return
     */
    SpreadsheetUser getUserByIDAndSession(Long long1, Long sessionId);

    /**
     * Get user list by sessionId
     *
     * @param sessionId
     * @return
     */
    List<SpreadsheetUser> getUserListBySessionId(Long sessionId);

    /**
     * Get user by UID
     *
     * @param uid
     * @return
     */
    SpreadsheetUser getUser(Long uid);

    /**
     * Save or update spreadsheet into database.
     *
     * @param Spreadsheet
     */
    void saveOrUpdateSpreadsheet(Spreadsheet Spreadsheet);

    /**
     * Get spreadsheet which is relative with the special toolSession.
     *
     * @param sessionId
     * @return
     */
    Spreadsheet getSpreadsheetBySessionId(Long sessionId);

    /**
     * Get spreadsheet toolSession by toolSessionId
     *
     * @param sessionId
     * @return
     */
    SpreadsheetSession getSessionBySessionId(Long sessionId);

    /**
     * Save or update spreadsheet session.
     *
     * @param resSession
     */
    void saveOrUpdateSpreadsheetSession(SpreadsheetSession resSession);

    /**
     * If success return next activity's url, otherwise return null.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    String finishToolSession(Long toolSessionId, Long userId) throws SpreadsheetApplicationException;

    /**
     * Return monitoring summary list. The return value is list of spreadsheet summaries for each groups. It does not
     * return the users in each session as we now use paging.
     *
     * @param contentId
     * @return
     */
    List<Summary> getSummary(Long contentId);

    /**
     * Get a paged, optionally sorted and filtered, list of users.
     *
     * @return
     */
    List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting, String searchString);

    /**
     * Get the number of users that would be returned by getUsersForTablesorter() if it was not paged. Supports
     * filtering.
     *
     * @return
     */
    int getCountUsersBySession(Long sessionId, String searchString);

    /**
     * Return monitoring statistic list. The return value is list of statistics for each groups.
     *
     * @param contentId
     * @return
     */
    List<StatisticDTO> getStatistics(Long contentId);

    /**
     * Returns messageService
     *
     * @return
     */
    public MessageService getMessageService();

    /**
     * Release marks
     *
     * @param sessionId
     */
    public void releaseMarksForSession(Long sessionId);
}
