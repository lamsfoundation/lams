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


package org.lamsfoundation.lams.tool.scratchie.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dto.BurningQuestionDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.BurningQuestionItemDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieItemComparator;
import org.lamsfoundation.lams.util.AlphanumComparator;
import org.lamsfoundation.lams.util.ExcelCell;
import org.lamsfoundation.lams.util.ExcelUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class TblMonitorAction extends LamsDispatchAction {
    private static Logger log = Logger.getLogger(TblMonitorAction.class);

    private static IScratchieService scratchieService;

    /**
     * Shows tra page
     */
    public ActionForward tra(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	initializeScratchieService();

	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentId);

	int attemptedLearnersNumber = scratchieService.countUsersByContentId(toolContentId);
	request.setAttribute("attemptedLearnersNumber", attemptedLearnersNumber);

	Set<ScratchieItem> items = new TreeSet<ScratchieItem>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());
	request.setAttribute("items", items);

	if (attemptedLearnersNumber != 0) {
	    // find first page in excel file
	    LinkedHashMap<String, ExcelCell[][]> excelDoc = scratchieService.exportExcel(toolContentId);
	    ExcelCell[][] firstPageData = null;
	    for (String key : excelDoc.keySet()) {
		firstPageData = excelDoc.get(key);
		break;
	    }

	    int groupsSize = scratchieService.countSessionsByContentId(toolContentId);
	    ArrayList<String[]> groupRows = new ArrayList<String[]>();
	    for (int groupCount = 0; groupCount < groupsSize; groupCount++) {
		ExcelCell[] groupRow = firstPageData[5 + groupCount];

		String[] groupRow2 = new String[2];
		groupRow2[0] = (String) groupRow[1].getCellValue();
		groupRow2[1] = ((String) groupRow[groupRow.length - 1].getCellValue()).replaceAll("%", "");
		groupRows.add(groupRow2);
	    }
	    request.setAttribute("groupRows", groupRows);
	}

	return mapping.findForward("tra");
    }

    /**
     * Shows tra StudentChoices page
     */
    public ActionForward traStudentChoices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	initializeScratchieService();

	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentId);

	//find second page in excel file
	LinkedHashMap<String, ExcelCell[][]> excelDoc = scratchieService.exportExcel(toolContentId);
	ExcelCell[][] secondPageData = null;
	for (ExcelCell[][] excelPage : excelDoc.values()) {
	    //check last row string starts with "*" (i.e. the string "*- Denotes the correct answer")
	    if (excelPage.length > 0) {
		ExcelCell lastRow = excelPage[excelPage.length - 1][0];
		if (lastRow != null && ((String) lastRow.getCellValue()).startsWith("*")) {
		    secondPageData = excelPage;
		    break;
		}
	    }
	}

	ExcelCell[] correctAnswersRow = secondPageData[4];
	request.setAttribute("correctAnswers", correctAnswersRow);

	int groupsSize = scratchieService.countSessionsByContentId(toolContentId);
	ArrayList<ExcelCell[]> groupRows = new ArrayList<ExcelCell[]>();
	for (int groupCount = 0; groupCount < groupsSize; groupCount++) {
	    ExcelCell[] groupRow = secondPageData[6 + groupCount];
	    groupRows.add(groupRow);
	}
	request.setAttribute("groupRows", groupRows);

	Set<ScratchieItem> items = new TreeSet<ScratchieItem>(new ScratchieItemComparator());
	items.addAll(scratchie.getScratchieItems());
	request.setAttribute("items", items);

	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId);
	return mapping.findForward("traStudentChoices");
    }

    /**
     * Exports tool results into excel.
     *
     * Had to move it from the tool as tool uses SessionMap
     * 
     * @throws IOException
     */
    public ActionForward exportExcel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	initializeScratchieService();

	Long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	LinkedHashMap<String, ExcelCell[][]> dataToExport = scratchieService.exportExcel(toolContentId);

	String fileName = "scratchie_export.xlsx";
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

	// Code to generate file and write file contents to response
	ServletOutputStream out = response.getOutputStream();
	ExcelUtil.createExcel(out, dataToExport, null, false);

	return null;
    }
    
    /**
     * Shows Teams page
     * @throws JSONException 
     */
    public ActionForward isBurningQuestionsEnabled(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException {
	initializeScratchieService();
	
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentId);
	
	// build JSON
	JSONObject responseJSON = new JSONObject();
	responseJSON.put("isBurningQuestionsEnabled", scratchie.isBurningQuestionsEnabled());
	writeResponse(response, "text/json", LamsDispatchAction.ENCODING_UTF8, responseJSON.toString());
	return null;
    }   

    /**
     * Shows Teams page
     */
    public ActionForward burningQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	initializeScratchieService();
	
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

	return mapping.findForward("burningQuestions");
    }
    
    /**
     * Shows Teams page
     */
    public ActionForward getModalDialogForTeamsTab(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	initializeScratchieService();
	
	long toolContentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Long userId = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);
	
	ScratchieUser user = scratchieService.getUserByUserIDAndContentID(userId, toolContentId);
	Collection<ScratchieItem> scratchieItems = user == null ? new LinkedList<ScratchieItem>()
		: scratchieService.getItemsWithIndicatedScratches(user.getSession().getSessionId());
	 
	request.setAttribute("scratchieItems", scratchieItems);
	return mapping.findForward("teams");
    }
    
    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private void initializeScratchieService() {
	if (scratchieService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    scratchieService = (IScratchieService) wac.getBean(ScratchieConstants.SCRATCHIE_SERVICE);
	}
    }
}
