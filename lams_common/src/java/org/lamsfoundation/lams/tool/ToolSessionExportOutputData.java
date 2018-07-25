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

package org.lamsfoundation.lams.tool;

import java.util.List;

/**
 * Value object for exporting output. We expect this object to be immutable once
 * it is created.
 * 
 * @author Jacky Fang 2004-12-7
 *
 */
public class ToolSessionExportOutputData {
    private List toolSessionIds;
    private String exportData;

    /**
     * Construtor
     */
    public ToolSessionExportOutputData(String exportData, List toolSessionIds) {
	this.exportData = exportData;
	this.toolSessionIds = toolSessionIds;
    }

    /**
     * @return Returns the exportData.
     */
    public String getExportData() {
	return exportData;
    }

    /**
     * @return Returns the toolSessionIds.
     */
    public List getToolSessionIds() {
	return toolSessionIds;
    }
}
