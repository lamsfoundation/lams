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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.mindmap.service;

import org.lamsfoundation.lams.learningdesign.service.ToolContentVersionFilter;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;

/**
 * Import filter class for different version of Mindmap content.
 */
public class MindmapImportContentVersionFilter extends ToolContentVersionFilter {

    /**
     * Import 20110414 version content to 20140102 version tool server.
     */
    public void up20110414To20140102() {
	this.removeField(Mindmap.class, "runOffline");
	this.removeField(Mindmap.class, "onlineInstructions");
	this.removeField(Mindmap.class, "offlineInstructions");
	this.removeField(Mindmap.class, "mindmapAttachments");
    }
}
