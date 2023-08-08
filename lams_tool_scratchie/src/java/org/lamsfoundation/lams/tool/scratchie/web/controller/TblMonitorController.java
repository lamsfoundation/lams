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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.scratchie.web.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.flux.FluxRegistry;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dto.BurningQuestionItemDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.GroupSummary;
import org.lamsfoundation.lams.tool.scratchie.dto.ScratchieItemDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieConfigItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieItemComparator;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.lamsfoundation.lams.util.excel.ExcelUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/tblmonitoring")
public class TblMonitorController {
    private static Logger log = Logger.getLogger(TblMonitorController.class);

    @Autowired
    private IScratchieService scratchieService;

    /**
     * Shows TRA page
     */
    @RequestMapping("/tra")
    public String tra(HttpServletRequest request) throws IOException, ServletException {
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentId);

	int attemptedLearnersNumber = scratchieService.countUsersByContentId(toolContentId);
	request.setAttribute("attemptedLearnersNumber", attemptedLearnersNumber);

	Set<ScratchieItem> items = new TreeSet<>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());
	request.setAttribute("items", items);

	if (attemptedLearnersNumber != 0) {
	    List<GroupSummary> groupSummaries = scratchieService.getSummaryByTeam(scratchie, items);

	    //calculate what is the percentage of first choice events in each session
	    for (GroupSummary summary : groupSummaries) {
		int numberOfFirstChoiceEvents = 0;
		for (ScratchieItemDTO itemDto : summary.getItemDtos()) {
		    if (itemDto.isUnraveledOnFirstAttempt()) {
			numberOfFirstChoiceEvents++;
		    }
		}

		Double percentage = (items.size() == 0) ? 0 : (double) numberOfFirstChoiceEvents * 100 / items.size();
		summary.setTotalPercentage(percentage);
	    }

	    request.setAttribute("groupSummaries", groupSummaries);
	}

	return "pages/tblmonitoring/tra";
    }

    /**
     * Shows tra StudentChoices page
     */
    @RequestMapping("/traStudentChoices")
    public String traStudentChoices(@RequestParam(name = AttributeNames.PARAM_TOOL_CONTENT_ID) long toolContentId,
	    Model model) throws IOException, ServletException {
	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentId);

	Map<String, Object> modelAttributes = scratchieService.prepareStudentChoicesData(scratchie);
	model.addAllAttributes(modelAttributes);

	model.addAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId);
	model.addAttribute("scratchie", scratchie);

	return "pages/monitoring/studentChoices5";
    }

    @RequestMapping("/traStudentChoicesTable")
    public String traStudentChoicesTable(@RequestParam(name = AttributeNames.PARAM_TOOL_CONTENT_ID) long toolContentId,
	    Model model) throws IOException, ServletException {
	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentId);

	Map<String, Object> modelAttributes = scratchieService.prepareStudentChoicesData(scratchie);
	model.addAllAttributes(modelAttributes);

	if (scratchie.isBurningQuestionsEnabled()) {
	    List<BurningQuestionItemDTO> burningQuestionItemDtos = scratchieService.getBurningQuestionDtos(scratchie,
		    null, true, true);
	    model.addAttribute(ScratchieConstants.ATTR_BURNING_QUESTION_ITEM_DTOS, burningQuestionItemDtos);
	}

	return "pages/monitoring/studentChoicesTable";
    }

    @RequestMapping(path = "/traStudentChoicesFlux", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<String> getTraStudentChoicesFlux(@RequestParam long toolContentId)
	    throws JsonProcessingException, IOException {
	return FluxRegistry.get(ScratchieConstants.STUDENT_CHOICES_UPDATE_FLUX_NAME, toolContentId);
    }

    @RequestMapping(path = "/burningQuestionsFlux", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<String> getBurningQuestionsFlux(@RequestParam long toolContentId)
	    throws JsonProcessingException, IOException {
	return FluxRegistry.get(ScratchieConstants.BURNING_QUESTIONS_UPDATED_FLUX_NAME, toolContentId);
    }

    @RequestMapping(path = "/getTimeLimitPanelUpdateFlux", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<String> getTimeLimitPanelUpdateFlux(@RequestParam long toolContentId, HttpServletResponse response) {
	response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
	return FluxRegistry.get(ScratchieConstants.TIME_LIMIT_PANEL_UPDATE_FLUX_NAME, toolContentId);
    }

    /**
     * Exports tool results into excel.
     *
     * Had to move it from the tool as tool uses SessionMap
     */
    @RequestMapping(path = "/exportExcel", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	List<ExcelSheet> sheets = scratchieService.exportExcel(toolContentId);

	String fileName = "scratchie_export.xlsx";
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

	// Code to generate file and write file contents to response
	ServletOutputStream out = response.getOutputStream();
	ExcelUtil.createExcel(out, sheets, null, false);
    }

    /**
     * Shows Teams page
     *
     * @throws JSONException
     */
    @RequestMapping(value = "/isBurningQuestionsEnabled")
    @ResponseBody
    public String isBurningQuestionsEnabled(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentId);

	// build JSON
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("isBurningQuestionsEnabled", scratchie.isBurningQuestionsEnabled());
	response.setContentType("application/json;charset=UTF-8");
	return responseJSON.toString();

    }

    /**
     * Shows Burning Questions container page
     */
    @RequestMapping("/burningQuestions")
    public String burningQuestions(HttpServletRequest request) throws IOException, ServletException {
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentId);
	request.setAttribute("discussionSentimentEnabled", scratchie.isDiscussionSentimentEnabled());

	return "pages/monitoring/parts/burningQuestions5";
    }

    /**
     * Shows Burning Questions content page
     */
    @RequestMapping("/burningQuestionsTable")
    public String burningQuestionsTable(HttpServletRequest request) throws IOException, ServletException {
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentId);

	//find available burningQuestionDtos, if any
	if (scratchie.isBurningQuestionsEnabled()) {
	    List<BurningQuestionItemDTO> burningQuestionItemDtos = scratchieService.getBurningQuestionDtos(scratchie,
		    null, true, true);

	    MonitoringController.setUpBurningQuestions(burningQuestionItemDtos);

	    request.setAttribute(ScratchieConstants.ATTR_BURNING_QUESTION_ITEM_DTOS, burningQuestionItemDtos);

	    ScratchieConfigItem hideTitles = scratchieService.getConfigItem(ScratchieConfigItem.KEY_HIDE_TITLES);
	    request.setAttribute(ScratchieConfigItem.KEY_HIDE_TITLES, Boolean.valueOf(hideTitles.getConfigValue()));

	    request.setAttribute("discussionSentimentEnabled", scratchie.isDiscussionSentimentEnabled());
	}

	return "pages/monitoring/parts/burningQuestionsTable";
    }

    /**
     * Shows Teams page
     */
    @RequestMapping("/getModalDialogForTeamsTab")
    public String getModalDialogForTeamsTab(HttpServletRequest request) throws IOException, ServletException {
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Long userId = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);

	ScratchieUser user = scratchieService.getUserByUserIDAndContentID(userId, toolContentId);
	Collection<ScratchieItem> scratchieItems = user == null ? new LinkedList<>()
		: scratchieService.getItemsWithIndicatedScratches(user.getSession().getSessionId());

	request.setAttribute("scratchieItems", scratchieItems);
	return "pages/tblmonitoring/teams";
    }
}