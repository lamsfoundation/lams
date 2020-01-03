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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.notebook.dao.INotebookEntryDAO;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;

public class CoreNotebookService implements ICoreNotebookService {

    private static Logger log = Logger.getLogger(CoreNotebookService.class);

    private INotebookEntryDAO notebookEntryDAO;

    private IBaseDAO baseDAO;

    protected IUserManagementService userManagementService;

    protected MessageService messageService;

    @Override
    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String title,
	    String entry) {
	User user = (User) getUserManagementService().findById(User.class, userID);
	NotebookEntry notebookEntry = new NotebookEntry(id, idType, signature, user, title, entry, new Date());
	saveOrUpdateNotebookEntry(notebookEntry);
	return notebookEntry.getUid();
    }

    @Override
    public TreeMap<Long, List<NotebookEntry>> getEntriesGroupedByLesson(Integer userID) {
	TreeMap<Long, List<NotebookEntry>> lessonIdToEntriesMap = new TreeMap<Long, List<NotebookEntry>>();
	List<NotebookEntry> entries = getEntry(userID, CoreNotebookConstants.SCRATCH_PAD);

	for (NotebookEntry entry : entries) {
	    if (lessonIdToEntriesMap.containsKey(entry.getExternalID())) {
		String lessonName = lessonIdToEntriesMap.get(entry.getExternalID()).get(0).getLessonName();
		entry.setLessonName(lessonName);
		lessonIdToEntriesMap.get(entry.getExternalID()).add(entry);
		
	    } else {
		Lesson lesson = (Lesson) baseDAO.find(Lesson.class, entry.getExternalID());
		List<NotebookEntry> newEntryList = new ArrayList<NotebookEntry>();

		entry.setLessonName(lesson.getLessonName());
		newEntryList.add(entry);

		lessonIdToEntriesMap.put(entry.getExternalID(), newEntryList);
	    }
	}

	return lessonIdToEntriesMap;
    }

    @Override
    public List<NotebookEntry> getEntry(Long id, Integer idType, String signature, Integer userID) {
	return notebookEntryDAO.get(id, idType, signature, userID);
    }

    /**
     * Add the SQL needed to look up entries for a given tool. Expects a valid string buffer to be supplied. This allows
     * a tool to get the single matching entry (assuming the tool has only created one notebook entry for each learner
     * in each session) for the teacher to view. This is an efficient way to get the entries at the same time as
     * retrieving the tool data, rather than making a separate lookup. Note - if there is more than on row for each
     * tool/session/learner, then the tool will end up with a cross product against the learner record and you will get
     * one row in the learner + notebook result for each notebook entry.
     *
     * May only be used for entries where the external_id_type = CoreNotebookConstants.NOTEBOOK_TOOL
     *
     * The parameters are strings, and the SQL is built up rather than using parameters as either sessionIdString or
     * userIdString may be the name of a field you are joining on. Typically the sessionId will be a number as the tool
     * would be requesting the entries for only one session but the user field will need to be a reference to a column
     * in the user table so that it can get entries for more than one user. If you wanted multiple users across multiple
     * sessions, then the sessionId would need to refer to the column in the user/session table.
     *
     * If you only want an entry for one user, use getEntry(id, idIdType, signature, userID);
     *
     * The return values are the entry for the select clause (will always have a leading but no trailing comma and an
     * alias of notebookEntry) and the sql join clause, which should go with any other join clauses.
     *
     * To make sure it always returns the same number of objects add the select clause like this:
     * queryText.append(notebookEntryStrings != null ? notebookEntryStrings[0] : ", NULL notebookEntry"); or
     * queryText.append(notebookEntryStrings != null ? notebookEntryStrings[0] :
     * ", NULL notebookEntry, NULL notebookModifiedDate"); or
     *
     * Then if there is isn't a notebookEntry to return, it still returns a notebookEntry column, which translates to
     * null. So you can return a collection like List<Object[UserObject, String,String]> irrespective of whether or not
     * the
     * notebook entries (the Strings) are needed.
     *
     * Finally, as it will be returning the notebook entry as a separate field in select clause, set up the sql -> java
     * object translation using ".addScalar("notebookEntry", Hibernate.STRING)". or
     * ".addScalar("notebookEntry", Hibernate.STRING).addScalar("notebookModifiedDate", Hibernate.TIMESTAMP)"
     *
     * @param sessionIdString
     *            Session identifier, usually the toolSessionId
     * @param toolSignature
     *            Tool's string signature (without any quotes) e.g. lantbk11
     * @param userIdString
     *            User identifier field string e.g.
     * @param includeDates
     *            if true, SQL should also return the date modified as well as the notebook entry
     * @return String[] { partial select string, join clause }
     *
     */
    @Override
    public String[] getNotebookEntrySQLStrings(String sessionIdString, String toolSignature, String userIdString,
	    boolean includeDates) {
	StringBuilder buf = new StringBuilder(" LEFT JOIN lams_notebook_entry entry ON entry.external_id=");
	buf.append(sessionIdString);
	buf.append(" AND entry.external_id_type=");
	buf.append(CoreNotebookConstants.NOTEBOOK_TOOL);
	buf.append(" AND entry.external_signature=\"");
	buf.append(toolSignature);
	buf.append("\" AND entry.user_id=");
	buf.append(userIdString);
	String[] retValue = new String[2];
	retValue[0] = includeDates
		? ", entry.entry notebookEntry, (CASE WHEN entry.last_modified IS NULL THEN entry.create_date ELSE entry.last_modified END) notebookModifiedDate "
		: ", entry.entry notebookEntry ";
	retValue[1] = buf.toString();
	return retValue;
    }

    @Override
    public String[] getNotebookEntrySQLStrings(String sessionIdString, String toolSignature, String userIdString) {
	return getNotebookEntrySQLStrings(sessionIdString, toolSignature, userIdString, false);
    }

    @Override
    public List<NotebookEntry> getEntry(Long id, Integer idType, String signature) {
	return notebookEntryDAO.get(id, idType, signature);
    }

    @Override
    public List<NotebookEntry> getEntry(Long id, Integer idType, Integer userID) {
	return notebookEntryDAO.get(id, idType, userID);
    }

    @Override
    public List<NotebookEntry> getEntry(Integer userID, Integer idType) {
	return notebookEntryDAO.get(userID, idType);
    }

    @Override
    public NotebookEntry getEntry(Long uid) {
	return notebookEntryDAO.get(uid);
    }

    @Override
    public void updateEntry(Long uid, String title, String entry) {
	NotebookEntry ne = getEntry(uid);
	if (ne != null) {
	    ne.setTitle(title);
	    ne.setEntry(entry);
	    ne.setLastModified(new Date());
	    saveOrUpdateNotebookEntry(ne);
	} else {
	    CoreNotebookService.log.debug("updateEntry: uid " + uid + "does not exist");
	}
    }

    @Override
    public void updateEntry(NotebookEntry notebookEntry) {
	notebookEntry.setLastModified(new Date());
	saveOrUpdateNotebookEntry(notebookEntry);
    }

    @Override
    public void saveOrUpdateNotebookEntry(NotebookEntry notebookEntry) {
	notebookEntryDAO.saveOrUpdate(notebookEntry);
    }

    @Override
    public void deleteEntry(NotebookEntry notebookEntry) {
	notebookEntryDAO.delete(notebookEntry);
    }

    /* ********** Used by Spring to "inject" the linked objects ************* */

    public void setNotebookEntryDAO(INotebookEntryDAO notebookEntryDAO) {
	this.notebookEntryDAO = notebookEntryDAO;
    }

    public void setBaseDAO(IBaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }

    /**
     *
     * @param IUserManagementService
     *            The userManagementService to set.
     */
    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    @Override
    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    /**
     * Set i18n MessageService
     */
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    /**
     * Get i18n MessageService
     */
    @Override
    public MessageService getMessageService() {
	return this.messageService;
    }
}
