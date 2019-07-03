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

package org.lamsfoundation.lams.tool.sbmt.service;

import org.lamsfoundation.lams.learningdesign.service.ToolContentVersionFilter;
import org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesContent;

/**
 * Import filter class for different version of SubmitFiles content.
 */
public class SubmitFilesImportContentVersionFilter extends ToolContentVersionFilter {

    /**
     * Import 20110325 version content to 20140102 version tool server.
     */
    public void up20110325To20140102() {
	this.removeField(SubmitFilesContent.class, "runOffline");
	this.removeField(SubmitFilesContent.class, "onlineInstruction");
	this.removeField(SubmitFilesContent.class, "offlineInstruction");
	this.removeField(SubmitFilesContent.class, "instructionFiles");
    }

    /**
     * Import 20140407 version content to 20140520 version tool server.
     */
    public void up20140407To20140520() {
	this.removeField(SubmitFilesContent.class, "createdBy");
    }

    public void up20180720To20181202() {
	this.renameClass("org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent",
		"org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesContent");
	this.renameClass("org.lamsfoundation.lams.tool.sbmt.SubmitFilesDetails",
		"org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesDetails");
	this.renameClass("org.lamsfoundation.lams.tool.sbmt.SubmitFilesReport",
		"org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesReport");
	this.renameClass("org.lamsfoundation.lams.tool.sbmt.SubmitFilesUser",
		"org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesUser");
	this.renameClass("org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession",
		"org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesSession");
    }
}
