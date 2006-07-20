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

package org.lamsfoundation.lams.journal.service;

import org.lamsfoundation.lams.journal.dao.IJournalEntryDAO;

public class JournalService implements IJournalService {

	private IJournalEntryDAO journalEntryDAO;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Long createJournalEntry(String toolSignature, Long toolSessionID, Integer userID, String title, String entry) {
		// TODO Auto-generated method stub
		return null;
	}

	public Long createNotebookEntry(String toolSignature, Long toolSessionID, Integer userID, String title, String entry) {
		// TODO Auto-generated method stub
		return null;
	}

	public Long createJournalEntry(Long lessonID, Integer userID, String title, String entry) {
		// TODO Auto-generated method stub
		return null;
	}

	public Long createNotebookEntry(Long lessonID, Integer userID, String title, String entry) {
		
		return new Long(99);
	}

	public void getEntry(String toolSignature, Long toolSessionID, Long userID) {
		// TODO Auto-generated method stub
		
	}

	public void getEntry(Long JournalEntryID) {
		// TODO Auto-generated method stub
		
	}

	/* ********** Used by Spring to "inject" the linked objects ************* */
	
	public void setJournalEntryDAO(IJournalEntryDAO journalEntryDAO) {
		this.journalEntryDAO = journalEntryDAO;
	}

}
