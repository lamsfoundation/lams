/*
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
*/

package org.lamsfoundation.lams.learning.web.form;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.notebook.JournalEntry;
import org.lamsfoundation.lams.notebook.NotebookEntry;

/**
 * @author daveg
 *
 * XDoclet definition:
 * @struts:form name="notebookForm"
 * 
 */
public class NotebookForm extends ActionForm {
	
	private static String DATE_FORMAT = "HH:mm 'on' dd/MM/yyyy";
	
	// properties for display
	
	private NotebookEntry notebookEntry;
	private String timeZoneId;

	// properties for update
	
	private Long notebookEntryId;
	private String title;
	private String body;
	
	
	public void setNotebookEntry(NotebookEntry notebookEntry) {
		this.notebookEntry = notebookEntry;
		this.notebookEntryId = notebookEntry.getNotebookEntryId();
		this.title = notebookEntry.getTitle();
		this.body = notebookEntry.getBody();
	}

	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}
	
	
	private String formatDateTime(Date date) {
		TimeZone timeZone;
		if (this.timeZoneId == null) timeZone = TimeZone.getDefault();
		else timeZone = TimeZone.getTimeZone(this.timeZoneId);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		dateFormat.setTimeZone(timeZone);
		return dateFormat.format(date);
	}
	
	public String getCreatedDateTime() {
		if (this.notebookEntry.getCreatedDateTime() == null) return null;
		return formatDateTime(this.notebookEntry.getCreatedDateTime());
	}
	
	public String getUpdatedDateTime() {
		if (this.notebookEntry.getUpdatedDateTime() == null) return null;
		return formatDateTime(this.notebookEntry.getUpdatedDateTime());
	}
	
	public String getNoteType() {
		String noteType = null;
		if (this.notebookEntry instanceof NotebookEntry) noteType = "Notebook Entry";
		else if (this.notebookEntry instanceof JournalEntry) noteType = "Journal Entry";
		return noteType;
	}
	
	public boolean isUpdateable() {
		return this.notebookEntry.isUpdateable();
	}
	

	/**
	 * Returns the notebookEntryId property.
	 */
	public Long getNotebookEntryId() {
		return notebookEntryId;
	}
	
	/**
	 * Sets notebookEntryId. If notebookEntryId is 0 then it is set as null.
	 */
	public void setNotebookEntryId(Long notebookEntryId) {
		if ((notebookEntryId != null) && (notebookEntryId.longValue() == 0)) this.notebookEntryId = null;
		else this.notebookEntryId = notebookEntryId;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public String getBodyFormatted() {
		String formatted = body.replaceAll("\n", "<br />");
		return formatted;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
