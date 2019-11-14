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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dto.BurningQuestionDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.BurningQuestionItemDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.GroupSummary;
import org.lamsfoundation.lams.tool.scratchie.dto.OptionDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.ScratchieItemDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieItemComparator;
import org.lamsfoundation.lams.util.AlphanumComparator;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.excel.ExcelRow;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.lamsfoundation.lams.util.excel.ExcelUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
	    // find the first page in excel file
	    List<ExcelSheet> sheets = scratchieService.exportExcel(toolContentId);
	    ExcelSheet firstPageData = sheets.get(0);

	    int groupsSize = scratchieService.countSessionsByContentId(toolContentId);
	    ArrayList<String[]> groupRows = new ArrayList<>();
	    for (int groupCount = 0; groupCount < groupsSize; groupCount++) {
		ExcelRow groupRow = firstPageData.getRow(5 + groupCount);

		String[] groupRow2 = new String[2];
		groupRow2[0] = (String) groupRow.getCell(1);
		groupRow2[1] = String.valueOf((Double) groupRow.getCell(groupRow.getCells().size() - 1) * 100);
		groupRows.add(groupRow2);
	    }
	    request.setAttribute("groupRows", groupRows);
	}

	return "pages/tblmonitoring/tra";
    }

    /**
     * Shows tra StudentChoices page
     */
    @RequestMapping("/traStudentChoices")
    public String traStudentChoices(HttpServletRequest request) throws IOException, ServletException {
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentId);

	Set<ScratchieItem> items = new TreeSet<>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());
	request.setAttribute("items", items);

	//find second page in excel file
	List<ExcelSheet> sheets = scratchieService.exportExcel(toolContentId);
	ExcelSheet secondPageData = sheets.get(1);

	//correct answers
	ExcelRow correctOptionsRow = secondPageData.getRow(4);
	request.setAttribute("correctAnswers", correctOptionsRow);

	//prepare data for displaying user answers table
	int groupsSize = scratchieService.countSessionsByContentId(toolContentId);
	ArrayList<GroupSummary> sessionDtos = new ArrayList<>();
	for (int groupCount = 0; groupCount < groupsSize; groupCount++) {
	    ExcelRow groupRow = secondPageData.getRows().get(6 + groupCount);

	    GroupSummary groupSummary = new GroupSummary();
	    String sessionName = groupRow.getCell(0).toString();
	    groupSummary.setSessionName(sessionName);

	    Collection<ScratchieItemDTO> itemDtos = new ArrayList<>();
	    for (int i = 1; i <= items.size(); i++) {
		ScratchieItemDTO itemDto = new ScratchieItemDTO();
		String optionSequence = groupRow.getCell(i).toString();
		String[] optionLetters = optionSequence.split(", ");

		List<OptionDTO> optionDtos = new LinkedList<>();
		for (int j = 0; j < optionLetters.length; j++) {
		    String optionLetter = optionLetters[j];
		    String correctOptionLetter = correctOptionsRow.getCell(i).toString();

		    OptionDTO optionDto = new OptionDTO();
		    optionDto.setAnswer(optionLetter);
		    optionDto.setCorrect(correctOptionLetter.equals(optionLetter));
		    optionDtos.add(optionDto);
		}

		itemDto.setOptionDtos(optionDtos);
		itemDtos.add(itemDto);
	    }
	    groupSummary.setItemDtos(itemDtos);

	    if (!itemDtos.isEmpty()) {
		int total = (Integer) groupRow.getCell(itemDtos.size() + 1);
		groupSummary.setMark(total);

		String totalPercentageStr = groupRow.getCell(itemDtos.size() + 2).toString();
		int totalPercentage = NumberUtils.isNumber(totalPercentageStr) ? (int) (Double.parseDouble(totalPercentageStr)*100) : 0;
		groupSummary.setTotalPercentage(totalPercentage + "%");
	    }

	    sessionDtos.add(groupSummary);
	}
	request.setAttribute("sessionDtos", sessionDtos);

	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId);
	return "pages/tblmonitoring/traStudentChoices";
    }

    /**
     * Exports tool results into excel.
     *
     * Had to move it from the tool as tool uses SessionMap
     *
     * @throws IOException
     */
    @RequestMapping("/exportExcel")
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
     * Shows Teams page
     */
    @RequestMapping("/burningQuestions")
    public String burningQuestions(HttpServletRequest request) throws IOException, ServletException {

	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentId);

	//find available burningQuestionDtos, if any
	if (scratchie.isBurningQuestionsEnabled()) {
	    List<BurningQuestionItemDTO> burningQuestionItemDtos = scratchieService.getBurningQuestionDtos(scratchie,
		    null, true);

	    //unescape previously escaped session names
	    for (BurningQuestionItemDTO burningQuestionItemDto : burningQuestionItemDtos) {
		List<BurningQuestionDTO> burningQuestionDtos = burningQuestionItemDto.getBurningQuestionDtos();

		for (BurningQuestionDTO burningQuestionDto : burningQuestionItemDto.getBurningQuestionDtos()) {

		    String escapedBurningQuestion = StringEscapeUtils
			    .unescapeJavaScript(burningQuestionDto.getEscapedBurningQuestion());
		    burningQuestionDto.setEscapedBurningQuestion(escapedBurningQuestion);

		    String sessionName = StringEscapeUtils.unescapeJavaScript(burningQuestionDto.getSessionName());
		    burningQuestionDto.setSessionName(sessionName);
		}

		Collections.sort(burningQuestionDtos, new Comparator<BurningQuestionDTO>() {
		    @Override
		    public int compare(BurningQuestionDTO o1, BurningQuestionDTO o2) {
			return new AlphanumComparator().compare(o1.getSessionName(), o2.getSessionName());
		    }
		});
	    }

	    request.setAttribute(ScratchieConstants.ATTR_BURNING_QUESTION_ITEM_DTOS, burningQuestionItemDtos);
	}

	return "pages/tblmonitoring/burningQuestions";
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
