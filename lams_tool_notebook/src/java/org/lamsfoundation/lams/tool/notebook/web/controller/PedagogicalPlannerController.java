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

package org.lamsfoundation.lams.tool.notebook.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.service.INotebookService;
import org.lamsfoundation.lams.tool.notebook.web.forms.NotebookPedagogicalPlannerForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author
 * @version
 *
 *
 *
 *
 *
 */
@Controller
@RequestMapping("/pedagogicalPlanner")
public class PedagogicalPlannerController {

    private static Logger logger = Logger.getLogger(PedagogicalPlannerController.class);

    @Autowired
    @Qualifier("notebookService")
    private INotebookService notebookService;

    @RequestMapping("/")
    protected String unspecified(NotebookPedagogicalPlannerForm pedagogicalPlannerForm, HttpServletRequest request) {
//	if (notebookService == null) {
//	    notebookService = NotebookServiceProxy.getNotebookService(this.getServlet().getServletContext());
//	}
	return initPedagogicalPlannerForm(pedagogicalPlannerForm, request);
    }

    @RequestMapping("/initPedagogicalPlannerForm")
    public String initPedagogicalPlannerForm(NotebookPedagogicalPlannerForm pedagogicalPlannerForm,
	    HttpServletRequest request) {
	NotebookPedagogicalPlannerForm plannerForm = pedagogicalPlannerForm;
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Notebook notebook = notebookService.getNotebookByContentId(toolContentID);
	plannerForm.fillForm(notebook);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	plannerForm.setContentFolderID(contentFolderId);
	return "pages/authoring/pedagogicalPlannerForm";
    }

    @RequestMapping("/saveOrUpdatePedagogicalPlannerForm")
    public String saveOrUpdatePedagogicalPlannerForm(NotebookPedagogicalPlannerForm pedagogicalPlannerForm,
	    Errors errors, HttpServletRequest request) throws IOException {
	pedagogicalPlannerForm.validate();
	if (!errors.hasErrors()) {
	    String instructions = pedagogicalPlannerForm.getInstructions();
	    Long toolContentID = pedagogicalPlannerForm.getToolContentID();
	    Notebook notebook = notebookService.getNotebookByContentId(toolContentID);
	    notebook.setInstructions(instructions);
	    notebookService.saveOrUpdateNotebook(notebook);
	}
	return "pages/authoring/pedagogicalPlannerForm";
    }

}