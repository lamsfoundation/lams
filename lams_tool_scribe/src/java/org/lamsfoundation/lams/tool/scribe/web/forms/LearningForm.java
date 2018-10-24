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

package org.lamsfoundation.lams.tool.scribe.web.forms;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Anthony Sukkar
 *
 *
 */
public class LearningForm {

    private static final long serialVersionUID = -4728946254882237144L;

    private Long scribeUserUID;
    private Long toolSessionID;
    private String mode;
    private String entryText;

    public Map reports = new HashMap();

    public String getMode() {
	return mode;
    }

    public void setMode(String mode) {
	this.mode = mode;
    }

    public Long getScribeUserUID() {
	return scribeUserUID;
    }

    public void setScribeUserUID(Long scribeUserUID) {
	this.scribeUserUID = scribeUserUID;
    }

    public Long getToolSessionID() {
	return toolSessionID;
    }

    public void setToolSessionID(Long toolSessionID) {
	this.toolSessionID = toolSessionID;
    }

    public String getEntryText() {
	return entryText;
    }

    public void setEntryText(String entryText) {
	this.entryText = entryText;
    }

    public void setReport(String key, Object value) {
	reports.put(key, value);
    }

    public Object getReport(String key) {
	return reports.get(key);
    }
}
