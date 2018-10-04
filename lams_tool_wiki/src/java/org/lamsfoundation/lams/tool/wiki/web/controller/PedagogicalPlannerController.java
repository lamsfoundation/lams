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

package org.lamsfoundation.lams.tool.wiki.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.service.IWikiService;
import org.lamsfoundation.lams.tool.wiki.web.forms.WikiPedagogicalPlannerForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Marcin Cieslak
 */
@Controller
@RequestMapping("/pedagogicalPlanner")
public class PedagogicalPlannerController {

    private static Logger logger = Logger.getLogger(PedagogicalPlannerController.class);

    @Autowired
    private IWikiService wikiService;

    @RequestMapping("/initPedagogicalPlannerForm")
    public String initPedagogicalPlannerForm(@ModelAttribute WikiPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request, HttpServletResponse response) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Wiki wiki = wikiService.getWikiByContentId(toolContentID);
	plannerForm.fillForm(wiki);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	plannerForm.setContentFolderID(contentFolderId);
	return "pages/authoring/pedagogicalPlannerForm";
    }

    @RequestMapping(path = "/saveOrUpdatePedagogicalPlannerForm", method = RequestMethod.POST)
    public String saveOrUpdatePedagogicalPlannerForm(@ModelAttribute WikiPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
//	ActionMessages errors = plannerForm.validate();
//	if (errors.isEmpty()) {
//	    String instructions = plannerForm.getWikiBody();
//	    Long toolContentID = plannerForm.getToolContentID();
//	    Wiki wiki = getWikiService().getWikiByContentId(toolContentID);
//	    WikiPage wikiPage = wiki.getWikiPages().iterator().next();
//	    wikiPage.setTitle(plannerForm.getTitle());
//	    wikiPage.getCurrentWikiContent().setBody(plannerForm.getWikiBody());
//	    getWikiService().saveOrUpdateWikiPage(wikiPage);
//	} else {
//	    saveErrors(request, errors);
//	}
	return "pages/authoring/pedagogicalPlannerForm";
    }

}