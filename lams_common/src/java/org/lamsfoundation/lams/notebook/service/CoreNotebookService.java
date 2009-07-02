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

/* $Id$ */

package org.lamsfoundation.lams.notebook.service;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.notebook.dao.INotebookEntryDAO;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;

import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.Role;


public class CoreNotebookService implements ICoreNotebookService, IExtendedCoreNotebookService{

	private static Logger log = Logger.getLogger(CoreNotebookService.class);

	private INotebookEntryDAO notebookEntryDAO;
	
    private IBaseDAO baseDAO;

	protected IUserManagementService userManagementService;
	
	protected MessageService messageService;
	
	public Long createNotebookEntry(Long id, Integer idType, String signature,
			Integer userID, String title, String entry) {
		User user = (User)getUserManagementService().findById(User.class, userID);
		NotebookEntry notebookEntry = new NotebookEntry(id, idType, signature,
				user, title, entry, new Date());
		saveOrUpdateNotebookEntry(notebookEntry);
		return notebookEntry.getUid();
	}
	
	public TreeMap<Long, List<NotebookEntry>> getEntryByLesson(Integer userID, Integer idType) {
		TreeMap<Long, List<NotebookEntry>> entryMap = new TreeMap<Long, List<NotebookEntry>>();
		List<NotebookEntry> list = getEntry(userID, idType);
		
		for (NotebookEntry entry : list) {
			if(entryMap.containsKey(entry.getExternalID())) {
				String lessonName = (String) entryMap.get(entry.getExternalID()).get(0).getLessonName();
				entry.setLessonName(lessonName);
				entryMap.get(entry.getExternalID()).add(entry);
			} else {
				Lesson lesson = (Lesson) baseDAO.find(Lesson.class, entry.getExternalID());
				List<NotebookEntry> newEntryList = new ArrayList<NotebookEntry>();
				
				entry.setLessonName(lesson.getLessonName());
				newEntryList.add(entry);
				
				entryMap.put(entry.getExternalID(), newEntryList);
			}
		}
		
		return entryMap;
	}

	public List<NotebookEntry> getEntry(Long id, Integer idType, String signature, Integer userID) {
		return notebookEntryDAO.get(id, idType, signature, userID);
	}
	
	public List<NotebookEntry> getEntry(Long id, Integer idType, String signature) {
		return notebookEntryDAO.get(id, idType, signature);
	}
	
	public List<NotebookEntry> getEntry(Long id, Integer idType, Integer userID) {
		return notebookEntryDAO.get(id, idType, userID);
	}

	public List<NotebookEntry> getEntry(Integer userID) {
		return notebookEntryDAO.get(userID);
	}
	
	public List<NotebookEntry> getEntry(Integer userID, Integer idType) {
		return notebookEntryDAO.get(userID, idType);
	}

	public List<NotebookEntry> getEntry(Integer userID, Long lessonID) {
		return notebookEntryDAO.get(userID, lessonID);		
	}
	
	public NotebookEntry getEntry(Long uid) {
		return notebookEntryDAO.get(uid);
	}

	public void updateEntry(Long uid, String title, String entry) {
		NotebookEntry ne = getEntry(uid);
		if (ne != null) {
			ne.setTitle(title);
			ne.setEntry(entry);
			ne.setLastModified(new Date());
			saveOrUpdateNotebookEntry(ne);
		} else {
			log.debug("updateEntry: uid " + uid + "does not exist");
		}
	}

	public void updateEntry(NotebookEntry notebookEntry) {
		notebookEntry.setLastModified(new Date());
		saveOrUpdateNotebookEntry(notebookEntry);
	}

	public void saveOrUpdateNotebookEntry(NotebookEntry notebookEntry) {
		notebookEntryDAO.saveOrUpdate(notebookEntry);
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
     * @param IUserManagementService The userManagementService to set.
     */
	public void setUserManagementService(IUserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}
	
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
	public MessageService getMessageService() {
		return this.messageService;
	}
}
