/****************************************************************
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
 * ****************************************************************
 */

package org.lamsfoundation.lams.notebook.service;

import java.util.List;
import java.util.TreeMap;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;

public interface ICoreNotebookService {

    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String title, String entry);

    TreeMap<Long, List<NotebookEntry>> getEntriesGroupedByLesson(Integer userID);

    List<NotebookEntry> getEntry(Long id, Integer idType, String signature, Integer userID);

    String[] getNotebookEntrySQLStrings(String sessionIdString, String toolSignature, String userIdString);

    String[] getNotebookEntrySQLStrings(String sessionIdString, String toolSignature, String userIdString,
	    boolean includeDates);

    List<NotebookEntry> getEntry(Long id, Integer idType, String signature);

    List<NotebookEntry> getEntry(Long id, Integer idType, Integer userID);

    List<NotebookEntry> getEntry(Integer userID, Integer idType);

    NotebookEntry getEntry(Long uid);

    void updateEntry(Long uid, String title, String entry);

    void updateEntry(NotebookEntry notebookEntry);

    void saveOrUpdateNotebookEntry(NotebookEntry notebookEntry);

    IUserManagementService getUserManagementService();

    MessageService getMessageService();

    void deleteEntry(NotebookEntry entry);
}
