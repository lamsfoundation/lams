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

package org.lamsfoundation.lams.tool.mindmap.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.service.IMindmapService;
import org.lamsfoundation.lams.tool.mindmap.web.forms.MindmapPedagogicalPlannerForm;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedagogicalPlanner")
public class PedagogicalPlannerController {
    private static Logger logger = Logger.getLogger(PedagogicalPlannerController.class);

    @Autowired
    private IMindmapService mindmapService;

    @Autowired
    @Qualifier("mindmapMessageService")
    private MessageService messageService;

    @RequestMapping("/initPedagogicalPlannerForm")
    public String initPedagogicalPlannerForm(@ModelAttribute MindmapPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request, HttpServletResponse response) {

	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Mindmap mindmap = mindmapService.getMindmapByContentId(toolContentID);
	plannerForm.fillForm(mindmap);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	plannerForm.setContentFolderID(contentFolderId);
	return "pages/authoring/pedagogicalPlannerForm";

    }

    @RequestMapping("/saveOrUpdatePedagogicalPlannerForm")
    public String saveOrUpdatePedagogicalPlannerForm(@ModelAttribute MindmapPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {

	MultiValueMap<String, String> errorMap = plannerForm.validate(messageService);
	if (errorMap.isEmpty()) {
	    String instructions = plannerForm.getInstructions();
	    Long toolContentID = plannerForm.getToolContentID();
	    Mindmap mindmap = mindmapService.getMindmapByContentId(toolContentID);
	    mindmap.setInstructions(instructions);
	    mindmapService.saveOrUpdateMindmap(mindmap);
	} else {
	    request.setAttribute("errorMap", errorMap);
	}
	return "pages/authoring/pedagogicalPlannerForm";
    }

}
