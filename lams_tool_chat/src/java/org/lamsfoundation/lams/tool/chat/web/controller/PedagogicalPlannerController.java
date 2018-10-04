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

package org.lamsfoundation.lams.tool.chat.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.service.IChatService;
import org.lamsfoundation.lams.tool.chat.web.forms.ChatPedagogicalPlannerForm;
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
    private IChatService chatService;

    @Autowired
    @Qualifier("chatMessageService")
    private MessageService messageService;

    @RequestMapping("/initPedagogicalPlannerForm")
    public String initPedagogicalPlannerForm(@ModelAttribute ChatPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request, HttpServletResponse response) {

	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Chat chat = chatService.getChatByContentId(toolContentID);
	plannerForm.fillForm(chat);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	plannerForm.setContentFolderID(contentFolderId);
	return "pages/authoring/pedagogicalPlannerForm";
    }

    @RequestMapping("/saveOrUpdatePedagogicalPlannerForm")
    public String saveOrUpdatePedagogicalPlannerForm(@ModelAttribute ChatPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {

	MultiValueMap<String, String> errorMap = plannerForm.validate(messageService);
	if (errorMap.isEmpty()) {
	    String instructions = plannerForm.getInstructions();
	    Long toolContentID = plannerForm.getToolContentID();
	    Chat chat = chatService.getChatByContentId(toolContentID);
	    chat.setInstructions(instructions);
	    chatService.saveOrUpdateChat(chat);
	} else {
	    request.setAttribute("errorMap", errorMap);
	    ;
	}
	return "pages/authoring/pedagogicalPlannerForm";
    }

}