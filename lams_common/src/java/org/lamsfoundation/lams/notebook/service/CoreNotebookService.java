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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */

package org.lamsfoundation.lams.notebook.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.dao.INotebookEntryDAO;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;

public class CoreNotebookService implements ICoreNotebookService {

	private static Logger log = Logger.getLogger(CoreNotebookService.class);

	private INotebookEntryDAO notebookEntryDAO;

	public Long createNotebookEntry(Long id, Integer idType, String signature,
			Integer userID, String title, String entry) {

		NotebookEntry notebookEntry = new NotebookEntry(id, idType, signature,
				userID, title, entry);
		saveOrUpdateNotebookEntry(notebookEntry);
		return notebookEntry.getUid();
	}

	public List<NotebookEntry> getEntry(String id, Long idType, Long userID) {
		List list = notebookEntryDAO.get(id, idType, userID);
		List<NotebookEntry> notebookEntries = new ArrayList<NotebookEntry>();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			NotebookEntry element = (NotebookEntry) iter.next();
			notebookEntries.add(element);
		}
		return notebookEntries;
	}

	public NotebookEntry getEntry(Long uid) {
		return notebookEntryDAO.get(uid);
	}

	public void updateEntry(Long uid, String title, String entry) {
		NotebookEntry ne = getEntry(uid);
		if (ne != null) {
			ne.setTitle(title);
			ne.setEntry(entry);
			saveOrUpdateNotebookEntry(ne);
		} else {
			log.debug("updateEntry: uid " + uid + "does not exist");
		}
	}

	public void updateEntry(NotebookEntry notebookEntry) {
		saveOrUpdateNotebookEntry(notebookEntry);
	}

	/* Private methods */

	private void saveOrUpdateNotebookEntry(NotebookEntry notebookEntry) {
		notebookEntryDAO.saveOrUpdate(notebookEntry);
	}

	/* ********** Used by Spring to "inject" the linked objects ************* */

	public void setNotebookEntryDAO(INotebookEntryDAO notebookEntryDAO) {
		this.notebookEntryDAO = notebookEntryDAO;
	}
}
