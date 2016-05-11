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


package org.lamsfoundation.lams.learning.export;

import java.util.Date;

/**
 * Models the portfolio for any type of notebook entry
 * 
 * @author mseaton
 *
 */
public class NotebookPortfolio {

    private String entry;
    private String title;
    private Date created;
    private Date modified;
    private boolean teacherViewable;

    public NotebookPortfolio() {
	this.entry = null;
	this.title = null;
	this.created = null;
	this.modified = null;
	this.teacherViewable = false;
    }

    /**
     * @return Returns the entry.
     */
    public String getEntry() {
	return entry;
    }

    /**
     * @param entry
     *            The entry name to set.
     */
    public void setEntry(String entry) {
	this.entry = entry;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * 
     * @return Returns the date/time the entry was created.
     */
    public Date getCreated() {
	return created;
    }

    /**
     * 
     * @param created
     *            The date/time to set
     */
    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     * 
     * @return Returns the date/time the entry was last modified.
     */
    public Date getModified() {
	return modified;
    }

    /**
     * 
     * @param modified
     *            The date/time to set
     */
    public void setModified(Date modified) {
	this.modified = modified;
    }

    /**
     * 
     * @return true if teacher can view entry, otherwise false.
     */
    public boolean getTeacherViewable() {
	return teacherViewable;
    }

    /**
     * 
     * @param teacherViewable
     */
    public void setTeacherViewable(boolean teacherViewable) {
	this.teacherViewable = teacherViewable;
    }
}
