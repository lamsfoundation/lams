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


package org.lamsfoundation.lams.tool.assessment.service;

import org.lamsfoundation.lams.learningdesign.service.ToolContentVersionFilter;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;

/**
 * Import filter class for different version of Assessment content.
 */
public class AssessmentImportContentVersionFilter extends ToolContentVersionFilter {

    /**
     * Import 20140101 version content to 20140102 version tool server.
     */
    public void up20140101To20140102() {
	this.removeField(Assessment.class, "runOffline");
	this.removeField(Assessment.class, "onlineInstructions");
	this.removeField(Assessment.class, "offlineInstructions");
	this.removeField(Assessment.class, "attachments");
    }

    /**
     * Import 20140102 version content to 20140428 version tool server.
     */
    public void up20140102To20140428() {
	this.removeField(Assessment.class, "contentInUse");
    }

    /**
     * Import 20140428 version content to 20140707 version tool server.
     */
    public void up20140428To20140707() {
	this.renameField(AssessmentQuestion.class, "questionOptions", "options");
	this.addField(AssessmentQuestion.class, "answerRequired", "true");
    }
    
    /**
     * Import 20140428 version content to 20140707 version tool server.
     */
    public void up20170101To20170315() {
	this.removeField(AssessmentQuestion.class, "createDate");
	this.removeField(AssessmentQuestion.class, "createBy");
    }
}
