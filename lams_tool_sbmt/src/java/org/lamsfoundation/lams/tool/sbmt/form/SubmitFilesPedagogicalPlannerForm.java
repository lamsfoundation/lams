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

/* $Id$ */
package org.lamsfoundation.lams.tool.sbmt.form;

import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.planner.PedagogicalPlannerForm;

/**
 * @struts:form name="SbmtPedagogicalPlannerForm"
 *              type="org.lamsfoundation.lams.tool.sbmt.form.SubmitFilesPedagogicalPlannerForm"
 */
public class SubmitFilesPedagogicalPlannerForm extends PedagogicalPlannerForm {
    String instruction;

    public String getInstruction() {
	return instruction;
    }

    public void setInstruction(String instructions) {
	instruction = instructions;
    }

    public void fillForm(SubmitFilesContent content) {
	if (content != null) {
	    String instruction = content.getInstruction();
	    instruction = WebUtil.removeHTMLtags(instruction);
	    setInstruction(instruction);
	    setToolContentID(content.getContentID());
	}
    }
}