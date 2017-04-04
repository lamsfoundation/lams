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


package org.lamsfoundation.lams.util.audit;

import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/** Public interface for a service that writes audit records */
public interface IAuditService {

    /**
     * Log a message. The username, date, moduleName and message are recorded in the audit log
     *
     * @param moduleName
     *            module generating the audit entry e.g. learning, voting tool
     * @param message
     *            message to be logged
     */
    void log(String moduleName, String message);

    /**
     * Same as above, but logs message using given userDTO instead of current session DTO. Useful when importing users
     * in a separate thread which isn't linked to main session.
     *
     * @param userDTO
     * @param moduleName
     * @param message
     */
    void log(UserDTO userDTO, String moduleName, String message);

    /**
     * Log a data change. The username, date, moduleName and change details are recorded in the audit log.
     *
     * @param moduleName
     *            tool generating the audit entry
     * @param originalText
     *            the text before it was changed
     * @param newText
     *            the text after it was changed
     * @param originalUserID
     *            the userID of the user who created the text initially
     * @param originalUserLogin
     *            the login of the user who created the text initially
     */
    void logChange(String moduleName, Long originalUserId, String originalUserLogin, String originalText,
	    String newText);

    /**
     * Log a mark change in Gradebook. The username, date, moduleName and change details are recorded in the audit log.
     *
     * @param moduleName
     *            tool generating the audit entry
     * @param originalMark
     *            the text before it was changed
     * @param newMark
     *            the text after it was changed
     * @param originalUserId
     *            the userID of the user who created the text initially
     * @param originalUserLogin
     *            the login of the user who created the text initially
     */
    void logMarkChange(String moduleName, Long originalUserId, String originalUserLogin, String originalMark,
	    String newMark);

    /**
     * Log staff member hiding a user entry. The username, date, moduleName and hidden entry are recorded in the audit
     * log.
     *
     * @param moduleName
     *            tool generating the audit entry
     * @param hiddenItem
     *            String version of the item that has been hidden
     * @param newText
     *            the text after it was changed
     * @param originalUserID
     *            the userID of the user who created the text initially
     * @param originalUserLogin
     *            the login of the user who created the text initially
     */
    void logHideEntry(String moduleName, Long originalUserId, String originalUserLogin, String hiddenItem);

    /**
     * Log staff member showing a user entry. The username, date, moduleName and hidden entry are recorded in the audit
     * log. Presumably the item was hidden previously.
     *
     * @param moduleName
     *            tool generating the audit entry
     * @param hiddenItem
     *            String version of the item that has been hidden
     * @param newText
     *            the text after it was changed
     * @param originalUserID
     *            the userID of the user who created the text initially
     * @param originalUserLogin
     *            the login of the user who created the text initially
     */
    void logShowEntry(String moduleName, Long originalUserId, String originalUserLogin, String hiddenItem);

    /**
     * Log teacher has started editing activity in monitor.
     * 
     * @param toolContentId
     */
    void logStartEditingActivityInMonitor(Long toolContentId);

    /**
     * Log teacher has pressed Save button when editing activity in monitor.
     * 
     * @param toolContentId
     */
    void logFinishEditingActivityInMonitor(Long toolContentId);

    /**
     * Log teacher has pressed Cancel button when editing activity in monitor.
     * 
     * @param toolContentId
     */
    void logCancelEditingActivityInMonitor(Long toolContentId);
}
