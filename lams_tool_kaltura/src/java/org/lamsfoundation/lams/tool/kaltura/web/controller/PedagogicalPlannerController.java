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

package org.lamsfoundation.lams.tool.kaltura.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.kaltura.model.Kaltura;
import org.lamsfoundation.lams.tool.kaltura.service.IKalturaService;
import org.lamsfoundation.lams.tool.kaltura.web.forms.KalturaPedagogicalPlannerForm;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Andrey Balan
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
    @Qualifier("kalturaService")
    private IKalturaService kalturaService;
    
    @Autowired
    @Qualifier("kalturaMessageService")
    private MessageService messageService;

    @RequestMapping("")
    protected String unspecified(
	    @ModelAttribute("pedagogicalPlannerForm") KalturaPedagogicalPlannerForm pedagogicalPlannerForm,
	    HttpServletRequest request) {
	return initPedagogicalPlannerForm(pedagogicalPlannerForm, request);
    }

    @RequestMapping("/initPedagogicalPlannerForm")
    public String initPedagogicalPlannerForm(
	    @ModelAttribute("pedagogicalPlannerForm") KalturaPedagogicalPlannerForm pedagogicalPlannerForm,
	    HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Kaltura kaltura = kalturaService.getKalturaByContentId(toolContentID);
	pedagogicalPlannerForm.fillForm(kaltura);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	pedagogicalPlannerForm.setContentFolderID(contentFolderId);
	return "pages/authoring/pedagogicalPlannerForm";
    }

    @RequestMapping(path = "/saveOrUpdatePedagogicalPlannerForm", method = RequestMethod.POST)
    public String saveOrUpdatePedagogicalPlannerForm(
	    @ModelAttribute("pedagogicalPlannerForm") KalturaPedagogicalPlannerForm pedagogicalPlannerForm,
	    HttpServletRequest request) throws IOException {
	MultiValueMap<String, String> errorMap = pedagogicalPlannerForm.validate(messageService);
	if (errorMap.isEmpty()) {
	    String instructions = pedagogicalPlannerForm.getInstructions();
	    Long toolContentID = pedagogicalPlannerForm.getToolContentID();
	    Kaltura kaltura = kalturaService.getKalturaByContentId(toolContentID);
	    kaltura.setInstructions(instructions);
	    kalturaService.saveOrUpdateKaltura(kaltura);
	} else {
	    request.setAttribute("errorMap", errorMap);
	}
	return "pages/authoring/pedagogicalPlannerForm";
    }

}
