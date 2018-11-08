/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.sbmt.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.sbmt.model.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.web.form.SubmitFilesPedagogicalPlannerForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedagogicalPlanner")
public class SubmitFilesPedagogicalPlannerController {

    @Autowired
    private ISubmitFilesService submitFilesService;

    protected String unspecified(SubmitFilesPedagogicalPlannerForm plannerForm, HttpServletRequest request) {
	return initPedagogicalPlannerForm(plannerForm, request);
    }

    @RequestMapping("/initPedagogicalPlannerForm")
    public String initPedagogicalPlannerForm(@ModelAttribute SubmitFilesPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	SubmitFilesContent submitFiles = submitFilesService.getSubmitFilesContent(toolContentID);
	plannerForm.fillForm(submitFiles);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	plannerForm.setContentFolderID(contentFolderId);
	return "authoring/pedagogicalPlannerForm";
    }

    @RequestMapping("/saveOrUpdatePedagogicalPlannerForm")
    public String saveOrUpdatePedagogicalPlannerForm(@ModelAttribute SubmitFilesPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request) {
	String content = plannerForm.getInstruction();
	Long toolContentID = plannerForm.getToolContentID();
	SubmitFilesContent submitFiles = submitFilesService.getSubmitFilesContent(toolContentID);
	submitFiles.setInstruction(content);
	submitFilesService.saveOrUpdateContent(submitFiles);
	return "authoring/pedagogicalPlannerForm";
    }
}