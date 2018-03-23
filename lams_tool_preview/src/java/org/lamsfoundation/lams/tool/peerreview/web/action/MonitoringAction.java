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

package org.lamsfoundation.lams.tool.peerreview.web.action;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants;
import org.lamsfoundation.lams.tool.peerreview.dto.GroupSummary;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.service.IPeerreviewService;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.ExcelCell;
import org.lamsfoundation.lams.util.ExcelUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MonitoringAction extends Action {
    public static Logger log = Logger.getLogger(MonitoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	String param = mapping.getParameter();

	request.setAttribute("initialTabId", WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));

	if (param.equals("summary")) {
	    return summary(mapping, form, request, response);
	}
	if (param.equals("criteria")) {
	    return criteria(mapping, form, request, response);
	}
	if (param.equals("getUsers")) {
	    return getUsers(mapping, form, request, response);
	}
	if (param.equals("getSubgridData")) {
	    return getSubgridData(mapping, form, request, response);
	}
	// refresh statistic page by Ajax call.
	if (param.equals("statistic")) {
	    return statistic(mapping, form, request, response);
	}
	if (param.equals("reflections")) {
	    return reflections(mapping, form, request, response);
	}
	if (param.equals("getReflections")) {
	    return getReflections(mapping, form, request, response);
	}
	if (param.equals("sendResultsToUser")) {
	    return sendResultsToUser(mapping, form, request, response);
	}
	if (param.equals("sendResultsToSessionUsers")) {
	    return sendResultsToSessionUsers(mapping, form, request, response);
	}
	if (param.equals("exportTeamReport")) {
	    return exportTeamReport(mapping, form, request, response);
	}
	if (param.equals("manageUsers")) {
	    return manageUsers(mapping, form, request, response);
	}
	if (param.equals("getManageUsers")) {
	    return getManageUsers(mapping, form, request, response);
	}
	if (param.equals("setUserHidden")) {
	    return setUserHidden(mapping, form, request, response);
	}
	return mapping.findForward(PeerreviewConstants.ERROR);
    }

    private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	// save contentFolderID into session
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	IPeerreviewService service = getPeerreviewService();
	List<GroupSummary> groupList = service.getGroupSummaries(contentId);

	Peerreview peerreview = service.getPeerreviewByContentId(contentId);

	// cache into sessionMap
	sessionMap.put(PeerreviewConstants.ATTR_SUMMARY_LIST, groupList);
	sessionMap.put(PeerreviewConstants.PAGE_EDITABLE, peerreview.isContentInUse());
	sessionMap.put(PeerreviewConstants.ATTR_PEERREVIEW, peerreview);
	sessionMap.put(PeerreviewConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(PeerreviewConstants.ATTR_IS_GROUPED_ACTIVITY, service.isGroupedActivity(contentId));

	List<RatingCriteria> criterias = service.getRatingCriterias(contentId);
	request.setAttribute(PeerreviewConstants.ATTR_CRITERIAS, criterias);

	return mapping.findForward(PeerreviewConstants.SUCCESS);
    }

    private ActionForward criteria(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long contentId = (Long) sessionMap.get(PeerreviewConstants.ATTR_TOOL_CONTENT_ID);
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");

	IPeerreviewService service = getPeerreviewService();
	Long criteriaId = WebUtil.readLongParam(request, "criteriaId");
	RatingCriteria criteria = service.getCriteriaByCriteriaId(criteriaId);

	request.setAttribute("criteria", criteria);
	request.setAttribute("toolSessionId", toolSessionId);
	return mapping.findForward(PeerreviewConstants.SUCCESS);
    }

    /**
     * Refreshes user list.
     */
    public ActionForward getUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException {
	IPeerreviewService service = getPeerreviewService();

	Long toolContentId = WebUtil.readLongParam(request, "toolContentId");
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");
	Long criteriaId = WebUtil.readLongParam(request, "criteriaId");

	RatingCriteria criteria = service.getCriteriaByCriteriaId(criteriaId);

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, PeerreviewConstants.PARAM_PAGE) - 1;
	int size = WebUtil.readIntParam(request, PeerreviewConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, PeerreviewConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, PeerreviewConstants.PARAM_SIDX, true);

	int sorting = PeerreviewConstants.SORT_BY_AVERAGE_RESULT_DESC;
	if (criteria.isRankingStyleRating()) {
	    sorting = PeerreviewConstants.SORT_BY_AVERAGE_RESULT_ASC;
	} else if (criteria.isCommentRating()) {
	    sorting = PeerreviewConstants.SORT_BY_USERNAME_ASC;
	}

	if (sortBy != null && sortBy.equals(PeerreviewConstants.PARAM_SORT_NAME)) {
	    if (sortOrder != null && sortOrder.equals(PeerreviewConstants.SORT_DESC)) {
		sorting = PeerreviewConstants.SORT_BY_USERNAME_DESC;
	    } else {
		sorting = PeerreviewConstants.SORT_BY_USERNAME_ASC;
	    }
	} else if (sortBy != null && sortBy.equals(PeerreviewConstants.PARAM_SORT_RATING)) {
	    if (sortOrder != null && sortOrder.equals(PeerreviewConstants.SORT_DESC)) {
		sorting = PeerreviewConstants.SORT_BY_AVERAGE_RESULT_DESC;
	    } else {
		sorting = PeerreviewConstants.SORT_BY_AVERAGE_RESULT_ASC;
	    }
	}

	String searchString = WebUtil.readStrParam(request, "itemDescription", true);

	// in case of monitoring we show all results. in case of learning - don't show results from the current user
	Long dummyUserId = -1L;

	ObjectNode responseData = JsonNodeFactory.instance.objectNode();
	int numUsersInSession = service.getCountUsersBySession(toolSessionId, dummyUserId);
	responseData.put("page", page + 1);
	responseData.put("total", Math.ceil((float) numUsersInSession / size));
	responseData.put("records", numUsersInSession);

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();

	String emailResultsText = service.getLocalisedMessage("button.email.results", null);
	if (criteria.isCommentRating()) {
	    // special db lookup just for this - gets the user's & how many comments left for them
	    List<Object[]> rawRows = service.getCommentsCounts(toolContentId, toolSessionId, criteria, page, size,
		    sorting, searchString);

	    for (int i = 0; i < rawRows.size(); i++) {
		Object[] rawRow = rawRows.get(i);
		ObjectNode cell = JsonNodeFactory.instance.objectNode();
		cell.put("itemId", (Long) rawRow[0]);
		cell.put("itemDescription", (String) rawRow[2]);
		cell.put("itemDescription2", (String) rawRow[3]);

		Number numCommentsNumber = (Number) rawRow[1];
		int numComments = numCommentsNumber != null ? numCommentsNumber.intValue() : 0;
		if (numComments > 0) {
		    cell.put("rating", service.getLocalisedMessage("label.monitoring.num.of.comments",
			    new Object[] { numComments }));
		    cell.put("email", generateResultsButton(toolSessionId, rawRow[0], emailResultsText));
		} else {
		    cell.put("rating", "");
		    cell.put("email", "");
		}

		ObjectNode row = JsonNodeFactory.instance.objectNode();
		row.put("id", "" + rawRow[0]);
		row.set("cell", cell);
		rows.add(row);
	    }
	} else {
	    // all other styles can use the "normal" routine and munge the JSON to suit jqgrid
	    ArrayNode rawRows = service.getUsersRatingsCommentsByCriteriaIdJSON(toolContentId, toolSessionId, criteria,
		    dummyUserId, page, size, sorting, searchString, true, true, false);
	    for (JsonNode rawNode : rawRows) {
		ObjectNode rawRow = (ObjectNode) rawNode;
		String averageRating = JsonUtil.optString(rawRow, "averageRating");
		Object numberOfVotes = rawRow.get("numberOfVotes");

		if (averageRating != null && averageRating.length() > 0) {
		    if (criteria.isStarStyleRating()) {
			String starString = "<div class='rating-stars-holder'>";
			starString += "<div class='rating-stars-disabled rating-stars-new' data-average='"
				+ averageRating + "' data-id='" + criteriaId + "'>";
			starString += "</div>";
			starString += "<div class='rating-stars-caption' id='rating-stars-caption-" + criteriaId
				+ "' >";
			String msg = service.getLocalisedMessage("label.average.rating",
				new Object[] { averageRating, numberOfVotes });
			starString += msg;
			starString += "</div>";
			rawRow.put("rating", starString);
			rawRow.put("email", generateResultsButton(toolSessionId, JsonUtil.optLong(rawRow, "itemId"),
				emailResultsText));
		    } else {
			rawRow.put("rating", averageRating);
			rawRow.put("email", generateResultsButton(toolSessionId, JsonUtil.optLong(rawRow, "itemId"),
				emailResultsText));
		    }
		}
		ObjectNode row = JsonNodeFactory.instance.objectNode();
		row.put("id", "" + rawRow.get("itemId"));
		row.set("cell", rawRow);
		rows.add(row);
	    }
	}
	responseData.set("rows", rows);

	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responseData.toString()));
	return null;
    }

    private String generateResultsButton(Object toolSessionId, Object userId, String emailResultsText) {
	return new StringBuilder("<a href=\"javascript:sendResultsForLearner(").append(toolSessionId).append(", ")
		.append(userId).append(")\" class=\"btn btn-default btn-xs email-button\">").append(emailResultsText)
		.append("</a>").toString();
    }

    private ActionForward getSubgridData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	IPeerreviewService service = getPeerreviewService();

	Long itemId = WebUtil.readLongParam(request, "itemId");
	Long toolContentId = WebUtil.readLongParam(request, "toolContentId");
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");
	Long criteriaId = WebUtil.readLongParam(request, "criteriaId");

	// ratings left by others for this user
	List<Object[]> ratings = service.getDetailedRatingsComments(toolContentId, toolSessionId, criteriaId, itemId);
	RatingCriteria criteria = service.getCriteriaByCriteriaId(criteriaId);
	String title = HtmlUtils.htmlEscape(criteria.getTitle());

	// processed data from db is userId, comment, rating, first_name, escaped( firstname + last_name)
	// if no rating or comment, then the entries will be null and not an empty string
	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	int i = 0;

	for (Object[] ratingDetails : ratings) {
	    if (ratingDetails[2] != null) {
		ArrayNode userData = JsonNodeFactory.instance.arrayNode();
		userData.add(i);
		userData.add(JsonUtil.readObject(ratingDetails[4]));
		userData.add(JsonUtil.readObject(ratingDetails[2]));
		userData.add(title);

		ObjectNode userRow = JsonNodeFactory.instance.objectNode();
		userRow.put("id", i++);
		userRow.set("cell", userData);

		rows.add(userRow);
	    }
	}

	if (criteria.isCommentsEnabled()) {
	    for (Object[] ratingDetails : ratings) {

		// Show comment if comment has been left by user. Exclude the special case where it is a hedging rating
		//  and the rating is not null - otherwise we end up putting the justification comment against entries that were not rated.
		String comment = (String) ratingDetails[1];
		if (comment != null && (!criteria.isHedgeStyleRating()
			|| (criteria.isHedgeStyleRating() && ratingDetails[2] != null))) {
		    ArrayNode userData = JsonNodeFactory.instance.arrayNode();
		    userData.add(i);
		    userData.add(JsonUtil.readObject(ratingDetails[4]));
		    String commentText = HtmlUtils.htmlEscape(comment);
		    commentText = StringUtils.replace(commentText, "&lt;BR&gt;", "<BR/>");
		    userData.add(commentText);
		    userData.add("Comments");

		    ObjectNode userRow = JsonNodeFactory.instance.objectNode();
		    userRow.put("id", i++);
		    userRow.set("cell", userData);

		    rows.add(userRow);
		}
	    }
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("total", 1);
	responseJSON.put("page", 1);
	responseJSON.put("records", rows.size());
	responseJSON.set("rows", rows);

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    private ActionForward statistic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	IPeerreviewService service = getPeerreviewService();
	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	Long toolContentId = WebUtil.readLongParam(request, PeerreviewConstants.ATTR_TOOL_CONTENT_ID);
	request.setAttribute("summaryList", service.getStatistics(toolContentId));
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(PeerreviewConstants.SUCCESS);
    }

    private ActionForward reflections(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");
	request.setAttribute("toolSessionId", toolSessionId);

	return mapping.findForward(PeerreviewConstants.SUCCESS);
    }

    private ActionForward getReflections(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, PeerreviewConstants.PARAM_PAGE) - 1;
	int size = WebUtil.readIntParam(request, PeerreviewConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, PeerreviewConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, PeerreviewConstants.PARAM_SIDX, true);

	int sorting = PeerreviewConstants.SORT_BY_USERNAME_ASC;

	if (sortBy != null && sortBy.equals(PeerreviewConstants.PARAM_SORT_NAME)) {
	    if (sortOrder != null && sortOrder.equals(PeerreviewConstants.SORT_DESC)) {
		sorting = PeerreviewConstants.SORT_BY_USERNAME_DESC;
	    } else {
		sorting = PeerreviewConstants.SORT_BY_USERNAME_ASC;
	    }
	} else if (sortBy != null && sortBy.equals(PeerreviewConstants.PARAM_SORT_NOTEBOOK)) {
	    if (sortOrder != null && sortOrder.equals(PeerreviewConstants.SORT_DESC)) {
		sorting = PeerreviewConstants.SORT_BY_NOTEBOOK_ENTRY_DESC;
	    } else {
		sorting = PeerreviewConstants.SORT_BY_NOTEBOOK_ENTRY_ASC;
	    }
	}

	String searchString = WebUtil.readStrParam(request, "itemDescription", true);

	// setting date format to ISO8601 for jquery.timeago
	DateFormat dateFormatterTimeAgo = new SimpleDateFormat(DateUtil.ISO8601_FORMAT);
	dateFormatterTimeAgo.setTimeZone(TimeZone.getTimeZone("GMT"));

	IPeerreviewService service = getPeerreviewService();
	int sessionUserCount = service.getCountUsersBySession(toolSessionId, -1L);

	ObjectNode responcedata = JsonNodeFactory.instance.objectNode();
	responcedata.put("page", page + 1);
	responcedata.put("total", Math.ceil((float) sessionUserCount / size));
	responcedata.put("records", sessionUserCount);

	List<Object[]> nbEntryList = service.getUserNotebookEntriesForTablesorter(toolSessionId, page, size, sorting,
		searchString);

	// processed data from db is user.user_id, user.first_name, escaped( first_name + last_name), notebook entry, notebook date
	// if no rating or comment, then the entries will be null and not an empty string
	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	int i = 0;

	for (Object[] nbEntry : nbEntryList) {
	    ArrayNode userData = JsonNodeFactory.instance.arrayNode();
	    userData.add(JsonUtil.readObject(nbEntry[0]));

	    Date entryTime = (Date) nbEntry[4];
	    if (entryTime == null) {
		userData.add((String) nbEntry[2]);
	    } else {
		StringBuilder nameField = new StringBuilder((String) nbEntry[2]).append("<BR/>")
			.append("<time class=\"timeago\" title=\"")
			.append(DateUtil.convertToStringForJSON(entryTime, request.getLocale()))
			.append("\" datetime=\"").append(dateFormatterTimeAgo.format(entryTime)).append("\"></time>");
		userData.add(nameField.toString());
	    }

	    userData.add(HtmlUtils.htmlEscape((String) nbEntry[3]));

	    ObjectNode userRow = JsonNodeFactory.instance.objectNode();
	    userRow.put("id", i++);
	    userRow.set("cell", userData);

	    rows.add(userRow);
	}

	responcedata.set("rows", rows);

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responcedata.toString());
	return null;
    }

    private ActionForward sendResultsToUser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	return sendResults(mapping, request, response, true);
    }

    private ActionForward sendResultsToSessionUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	return sendResults(mapping, request, response, false);
    }

    private ActionForward sendResults(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response,
	    boolean oneUserOnly) throws IOException {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long contentId = (Long) sessionMap.get(PeerreviewConstants.ATTR_TOOL_CONTENT_ID);
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");

	IPeerreviewService service = getPeerreviewService();
	int numEmailsSent = 0;

	if (oneUserOnly) {
	    Long userId = WebUtil.readLongParam(request, PeerreviewConstants.PARAM_USERID);
	    numEmailsSent = service.emailReportToUser(contentId, toolSessionId, userId);
	} else {
	    numEmailsSent = service.emailReportToSessionUsers(contentId, toolSessionId);
	}

	response.setContentType("text/html;charset=utf-8");
	response.getWriter().write(service.getLocalisedMessage("msg.results.sent", new Object[] { numEmailsSent }));
	return null;
    }

    /**
     * Exports Team Report into Excel spreadsheet.
     *
     * @throws ServletException
     * @throws IOException
     */
    private ActionForward exportTeamReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	IPeerreviewService service = getPeerreviewService();
	Long toolContentId = WebUtil.readLongParam(request, PeerreviewConstants.ATTR_TOOL_CONTENT_ID);

	Peerreview peerreview = service.getPeerreviewByContentId(toolContentId);
	if (peerreview == null) {
	    log.warn("Did not find Peer Review with toolContentId: " + toolContentId + " export content");
	    return null;
	}

	String fileName = peerreview.getTitle().replaceAll(" ", "_") + ".xlsx";

	try {
	    fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	    if (log.isDebugEnabled()) {
		log.debug("Exporting to a spreadsheet for toolContentId: " + toolContentId + "filename " + fileName);
	    }

	    response.setContentType("application/x-download");
	    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	    ServletOutputStream out = response.getOutputStream();

	    LinkedHashMap<String, ExcelCell[][]> dataToExport = service.exportTeamReportSpreadsheet(toolContentId);

	    // set cookie that will tell JS script that export has been finished
	    String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	    Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	    fileDownloadTokenCookie.setPath("/");
	    response.addCookie(fileDownloadTokenCookie);

	    ExcelUtil.createExcel(out, dataToExport, "Exported on:", true);

	} catch (IOException e) {
	    log.error("exportTeamReportExcelSpreadsheet i/o error occured: " + e.getMessage(), e);
	    throw new ServletException(e);
	}

	return null;
    }

    private ActionForward manageUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	IPeerreviewService service = getPeerreviewService();
	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	return mapping.findForward(PeerreviewConstants.SUCCESS);
    }

    /**
     * Gets a paged set of data for stars or comments. These are directly saved to the database, not through
     * LearnerAction like Ranking and Hedging.
     */
    private ActionForward getManageUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException {

	IPeerreviewService service = getPeerreviewService();
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, PeerreviewConstants.PARAM_PAGE) - 1;
	int size = WebUtil.readIntParam(request, PeerreviewConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, PeerreviewConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, PeerreviewConstants.PARAM_SIDX, true);

	int sorting = PeerreviewConstants.SORT_BY_USERNAME_ASC;
	if (sortBy != null && sortBy.equals(PeerreviewConstants.PARAM_SORT_USER_NAME)) {
	    if (sortOrder != null && sortOrder.equals(PeerreviewConstants.SORT_DESC)) {
		sorting = PeerreviewConstants.SORT_BY_USERNAME_DESC;
	    } else {
		sorting = PeerreviewConstants.SORT_BY_USERNAME_ASC;
	    }
	}
	String searchString = WebUtil.readStrParam(request, "userName", true);

	ObjectNode responcedata = JsonNodeFactory.instance.objectNode();

	int numUsersInSession = service.getCountUsersBySession(toolSessionId);
	responcedata.put("page", page + 1);
	responcedata.put("total", Math.ceil((float) numUsersInSession / size));
	responcedata.put("records", numUsersInSession);

	// gets the user
	List<Object[]> rawRows = service.getPagedUsers(toolSessionId, page, size, sorting, searchString);
	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	for (int i = 0; i < rawRows.size(); i++) {
	    Object[] rawRow = rawRows.get(i);
	    ObjectNode cell = JsonNodeFactory.instance.objectNode();
	    cell.put("hidden", !(Boolean) rawRow[1]);
	    cell.put("userUid", (Integer) rawRow[0]);
	    cell.put("userName", (String) rawRow[2]);

	    ObjectNode row = JsonNodeFactory.instance.objectNode();
	    row.put("id", "" + rawRow[0]);
	    row.set("cell", cell);
	    rows.add(row);
	}
	responcedata.set("rows", rows);

	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responcedata.toString()));
	return null;
    }

    private ActionForward setUserHidden(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	IPeerreviewService service = getPeerreviewService();

	Long toolContentId = WebUtil.readLongParam(request, PeerreviewConstants.ATTR_TOOL_CONTENT_ID);
	Long userUid = WebUtil.readLongParam(request, "userUid");
	boolean isHidden = !WebUtil.readBooleanParam(request, "hidden");

	service.setUserHidden(toolContentId, userUid, isHidden);
	return null;
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private IPeerreviewService getPeerreviewService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IPeerreviewService) wac.getBean(PeerreviewConstants.PEERREVIEW_SERVICE);
    }
}
