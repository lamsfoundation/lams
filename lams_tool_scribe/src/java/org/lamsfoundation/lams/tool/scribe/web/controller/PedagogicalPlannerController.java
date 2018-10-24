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

package org.lamsfoundation.lams.tool.scribe.web.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.scribe.model.Scribe;
import org.lamsfoundation.lams.tool.scribe.model.ScribeHeading;
import org.lamsfoundation.lams.tool.scribe.service.IScribeService;
import org.lamsfoundation.lams.tool.scribe.web.forms.ScribePedagogicalPlannerForm;
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
    @Qualifier("lascrbScribeService")
    private IScribeService scribeService;

    @RequestMapping("")
    protected String unspecified(@ModelAttribute("pedagogicalPlannerForm") ScribePedagogicalPlannerForm pedagogicalPlannerForm,
	    HttpServletRequest request) {
	return initPedagogicalPlannerForm(pedagogicalPlannerForm, request);
    }

    @RequestMapping("/initPedagogicalPlannerForm")
    public String initPedagogicalPlannerForm(@ModelAttribute("pedagogicalPlannerForm") ScribePedagogicalPlannerForm pedagogicalPlannerForm,
	    HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Scribe scribe = scribeService.getScribeByContentId(toolContentID);
	pedagogicalPlannerForm.fillForm(scribe);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	pedagogicalPlannerForm.setContentFolderID(contentFolderId);
	return "pages/authoring/pedagogicalPlannerForm";
    }

    @RequestMapping(value = "/saveOrUpdatePedagogicalPlannerForm", method = RequestMethod.POST)
    public String saveOrUpdatePedagogicalPlannerForm(
	    @ModelAttribute("pedagogicalPlannerForm") ScribePedagogicalPlannerForm pedagogicalPlannerForm, HttpServletRequest request)
	    throws IOException {
	MultiValueMap<String, String> errorMap = pedagogicalPlannerForm.validate();
	if (errorMap.isEmpty()) {
	    Scribe scribe = scribeService.getScribeByContentId(pedagogicalPlannerForm.getToolContentID());

	    int headingIndex = 0;
	    String heading = null;
	    ScribeHeading scribeHeading = null;
	    List<ScribeHeading> newHeadings = new LinkedList<>();
	    Iterator<ScribeHeading> scribeHeadingIterator = scribe.getScribeHeadings().iterator();
	    do {
		heading = pedagogicalPlannerForm.getHeading(headingIndex);
		if (StringUtils.isEmpty(heading)) {
		    pedagogicalPlannerForm.removeHeading(headingIndex);
		} else {
		    if (scribeHeadingIterator.hasNext()) {
			scribeHeading = scribeHeadingIterator.next();
			scribeHeading.setDisplayOrder(headingIndex);
		    } else {
			scribeHeading = new ScribeHeading();
			scribeHeading.setHeadingText(heading);
			scribeHeading.setDisplayOrder(headingIndex);

			newHeadings.add(scribeHeading);
			scribeHeading.setScribe(scribe);
		    }
		    headingIndex++;
		}

	    } while (heading != null);
	    while (scribeHeadingIterator.hasNext()) {
		scribeHeading = scribeHeadingIterator.next();
		scribeHeadingIterator.remove();
		scribeService.deleteHeading(scribeHeading.getUid());
	    }
	    scribe.getScribeHeadings().addAll(newHeadings);
	    scribeService.saveOrUpdateScribe(scribe);
	} else {
	    request.setAttribute("errorMap", errorMap);
	}
	return "pages/authoring/pedagogicalPlannerForm";
    }

    @RequestMapping("/createPedagogicalPlannerHeading")
    public String createPedagogicalPlannerHeading(
	    @ModelAttribute("pedagogicalPlannerForm") ScribePedagogicalPlannerForm pedagogicalPlannerForm, HttpServletRequest request,
	    HttpServletResponse response) {
	pedagogicalPlannerForm.setHeading(pedagogicalPlannerForm.getHeadingCount().intValue(), "");
	return "pages/authoring/pedagogicalPlannerForm";
    }
}