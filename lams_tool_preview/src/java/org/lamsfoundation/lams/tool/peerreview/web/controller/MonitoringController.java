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

package org.lamsfoundation.lams.tool.peerreview.web.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.rating.dto.StyledCriteriaRatingDTO;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants;
import org.lamsfoundation.lams.tool.peerreview.dto.EmailPreviewDTO;
import org.lamsfoundation.lams.tool.peerreview.dto.GroupSummary;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;
import org.lamsfoundation.lams.tool.peerreview.service.IPeerreviewService;
import org.lamsfoundation.lams.tool.peerreview.service.PeerreviewServiceImpl;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.lamsfoundation.lams.util.excel.ExcelUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {
    public static Logger log = Logger.getLogger(MonitoringController.class);

    private static final String MONITORING_PATH = "/pages/monitoring/monitoring";
    private static final String CRITERIA_PATH = "/pages/monitoring/criteria";
    private static final String STATISTICS_PATH = "/pages/monitoring/statisticpart";
    private static final String REFLECTIONS_PATH = "/pages/monitoring/reflections";
    private static final String EMAIL_PREVIEW_PATH = "pages/monitoring/emailpreview";
    private static final String MANAGE_USERS_PATH = "/pages/monitoring/manageUsers";

    @Autowired
    @Qualifier("peerreviewService")
    private IPeerreviewService service;

    @Autowired
    private IRatingService ratingService;

    @RequestMapping("/summary")
    public String summary(HttpServletRequest request, HttpServletResponse response) {
	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	// save contentFolderID into session
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	List<GroupSummary> groupList = service.getGroupSummaries(contentId);

	Peerreview peerreview = service.getPeerreviewByContentId(contentId);

	// cache into sessionMap
	sessionMap.put(PeerreviewConstants.ATTR_SUMMARY_LIST, groupList);
	sessionMap.put(PeerreviewConstants.PAGE_EDITABLE, peerreview.isContentInUse());
	sessionMap.put(PeerreviewConstants.ATTR_PEERREVIEW, peerreview);
	sessionMap.put(PeerreviewConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(PeerreviewConstants.ATTR_IS_GROUPED_ACTIVITY, service.isGroupedActivity(contentId));

	List<RatingCriteria> criterias = service.getRatingCriterias(contentId);
	List<RatingCriteria> flattenedCriterias = new ArrayList<>(criterias);
	PeerreviewServiceImpl.removeGroupedCriteria(flattenedCriterias);

	if (flattenedCriterias.size() == 1 && flattenedCriterias.get(0).isRubricsStyleRating()) {
	    Map<Long, Map<PeerreviewUser, StyledCriteriaRatingDTO>> rubricsData = service.getRubricsData(sessionMap,
		    flattenedCriterias.get(0), criterias);
	    request.setAttribute("rubricsData", rubricsData);
	}

	request.setAttribute(PeerreviewConstants.ATTR_CRITERIAS, flattenedCriterias);
	return MONITORING_PATH;
    }

    @RequestMapping("/criteria")
    @SuppressWarnings("unchecked")
    public String criteria(HttpServletRequest request, HttpServletResponse response) {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	sessionMap.remove("emailPreviewDTO"); // clear any old cached emails

	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");

	Long criteriaId = WebUtil.readLongParam(request, "criteriaId");
	RatingCriteria criteria = service.getCriteriaByCriteriaId(criteriaId);

	if (criteria.isRubricsStyleRating()) {
	    Long toolContentId = (Long) sessionMap.get(PeerreviewConstants.ATTR_TOOL_CONTENT_ID);

	    List<RatingCriteria> criterias = service.getRatingCriterias(toolContentId);
	    Map<PeerreviewUser, StyledCriteriaRatingDTO> rubricsLearnerData = service
		    .getRubricsLearnerData(toolSessionId, criteria, criterias);
	    request.setAttribute("rubricsLearnerData", rubricsLearnerData);
	}

	request.setAttribute("criteria", criteria);
	request.setAttribute("toolSessionId", toolSessionId);
	return CRITERIA_PATH;
    }

    // may be a BigInteger, may be a Long. Who knows as it has come from Hibernate and it seems to change!
    private Long asLong(Object value) {
	if (value != null) {
	    try {
		Number num = (Number) value;
		return num.longValue();
	    } catch (Exception e1) {
		try {
		    return new Long((String) value);
		} catch (Exception e2) {
		}
	    }
	}
	return null;
    }

    /**
     * Refreshes user list.
     */
    @RequestMapping("/getUsers")
    @ResponseBody
    public String getUsers(HttpServletRequest request, HttpServletResponse res) throws IOException, ServletException {

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

	String emailResultsText = service.getLocalisedMessage("button.preview.results", null);
	if (criteria.isCommentRating()) {
	    // special db lookup just for this - gets the user's & how many comments left for them
	    List<Object[]> rawRows = service.getCommentsCounts(toolContentId, toolSessionId, criteria, page, size,
		    sorting, searchString);

	    for (int i = 0; i < rawRows.size(); i++) {
		Object[] rawRow = rawRows.get(i);
		ObjectNode cell = JsonNodeFactory.instance.objectNode();
		cell.put("itemId", asLong(rawRow[0]));
		cell.put("itemDescription", (String) rawRow[2]);
		cell.put("itemDescription2", asLong(rawRow[3]));

		Long numCommentsLong = asLong(rawRow[1]);
		int numComments = numCommentsLong != null ? numCommentsLong.intValue() : 0;
		if (numComments > 0) {
		    cell.put("rating", service.getLocalisedMessage("label.monitoring.num.of.comments",
			    new Object[] { numComments }));
		    cell.put("email", generatePreviewButton(toolSessionId, rawRow[0], emailResultsText));
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
			rawRow.put("email", generatePreviewButton(toolSessionId, JsonUtil.optLong(rawRow, "itemId"),
				emailResultsText));
		    } else {
			rawRow.put("rating", averageRating);
			rawRow.put("email", generatePreviewButton(toolSessionId, JsonUtil.optLong(rawRow, "itemId"),
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
	return responseData.toString();
    }

    private String generatePreviewButton(Object toolSessionId, Object userId, String emailResultsText) {
	return new StringBuilder("<button onclick=\"javascript:previewResultsForLearner(").append(toolSessionId)
		.append(", ").append(userId)
		.append(")\" class=\"btn btn-default btn-xs email-button btn-disable-on-submit\">")
		.append(emailResultsText).append("</button>").toString();
    }

    @RequestMapping("/getSubgridData")
    @ResponseBody
    public String getSubgridData(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
		userData.add((String) ratingDetails[4]);
		userData.add((String) ratingDetails[2]);
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
		    userData.add((String) ratingDetails[4]);
		    String commentText = HtmlUtils.htmlEscape(comment);
		    commentText = StringUtils.replace(commentText, "&lt;BR&gt;", "<BR/>").replace("\n", "<BR/>");
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
	return responseJSON.toString();
    }

    @RequestMapping("/statistic")
    public String statistic(HttpServletRequest request, HttpServletResponse response) {
	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	Long toolContentId = WebUtil.readLongParam(request, PeerreviewConstants.ATTR_TOOL_CONTENT_ID);
	request.setAttribute("summaryList", service.getStatistics(toolContentId));
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return STATISTICS_PATH;
    }

    @RequestMapping("/reflections")
    @SuppressWarnings("unchecked")
    public String reflections(HttpServletRequest request, HttpServletResponse response) {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	sessionMap.remove("emailPreviewDTO"); // clear any old cached emails

	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");
	request.setAttribute("toolSessionId", toolSessionId);

	return REFLECTIONS_PATH;
    }

    @RequestMapping("/getReflections")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public String getReflections(HttpServletRequest request, HttpServletResponse response) throws IOException {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	sessionMap.remove("emailPreviewDTO"); // clear any old cached emails

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
	    userData.add(asLong(nbEntry[0])); // user id
	    userData.add((String) nbEntry[2]); // user name

	    Date entryTime = (Date) nbEntry[5]; // when....
	    if (entryTime == null) {
		userData.add("");
	    } else {
		StringBuilder nameField = new StringBuilder("<time class=\"timeago\" title=\"")
			.append(DateUtil.convertToStringForJSON(entryTime, request.getLocale()))
			.append("\" datetime=\"").append(dateFormatterTimeAgo.format(entryTime)).append("\"></time>");
		userData.add(nameField.toString());
	    }

	    userData.add((String) nbEntry[3]);
	    userData.add(HtmlUtils.htmlEscape((String) nbEntry[4]));

	    ObjectNode userRow = JsonNodeFactory.instance.objectNode();
	    userRow.put("id", i++);
	    userRow.set("cell", userData);

	    rows.add(userRow);
	}

	responcedata.set("rows", rows);

	response.setContentType("application/json;charset=utf-8");
	return responcedata.toString();
    }

    @RequestMapping("/previewResultsToUser")
    @SuppressWarnings("unchecked")
    public String previewResultsToUser(HttpServletRequest request, HttpServletResponse response) throws IOException {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	sessionMap.remove("emailPreviewDTO"); // clear any old cached emails

	Long contentId = (Long) sessionMap.get(PeerreviewConstants.ATTR_TOOL_CONTENT_ID);
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");

	// only supports single user
	Long userId = WebUtil.readLongParam(request, PeerreviewConstants.PARAM_USERID);
	String emailHTML = service.generateEmailReportToUser(contentId, toolSessionId, userId);
	EmailPreviewDTO dto = new EmailPreviewDTO(emailHTML, toolSessionId, userId);
	sessionMap.put("emailPreviewDTO", dto);
	return EMAIL_PREVIEW_PATH;
    }

    @RequestMapping("/sendPreviewedResultsToUser")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public String sendPreviewedResultsToUser(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {
	// if we regenerate the results, it is more work for the server and the results may change. if we want to
	// just send what the monitor already sees, get it back from the sessionMap, check that it should be the
	// same email (ie check the parameters) and if all good then send.
	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	EmailPreviewDTO previewDTO = (EmailPreviewDTO) sessionMap.get("emailPreviewDTO");
	Long contentId = (Long) sessionMap.get(PeerreviewConstants.ATTR_TOOL_CONTENT_ID);
	Long dateTimeStamp = WebUtil.readLongParam(request, "dateTimeStamp");
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");
	Long userId = WebUtil.readLongParam(request, "userID");

	if (previewDTO == null || !dateTimeStamp.equals(previewDTO.getDateTimeStamp())
		|| !toolSessionId.equals(previewDTO.getToolSessionId())
		|| !userId.equals(previewDTO.getLearnerUserId())) {
	    log.error(
		    "Unable to send preview as requested parameters to not matched the catched parameters. Email text in session does not match the requested email. Cached preview: "
			    + previewDTO);
	    sessionMap.remove("emailPreviewDTO"); // Cached email removed so it can't be resent.
	    response.setContentType("text/html;charset=utf-8");
	    return service.getLocalisedMessage("label.email.send.failed.preview.wrong", new Object[] {});
	}

	// Use the details from the DTO, so that if somehow something has got stuff up (and the above check has not picked
	// up the issue, make sure you use the user id originally associated with this email.
	int numEmailsSent = service.emailReportToUser(contentId, previewDTO.getToolSessionId(),
		previewDTO.getLearnerUserId(), previewDTO.getEmailHTML());
	sessionMap.remove("emailPreviewDTO"); // Cached email removed so it can't be resent.
	response.setContentType("text/html;charset=utf-8");
	return service.getLocalisedMessage("msg.results.sent", new Object[] { numEmailsSent });
    }

    @RequestMapping("/sendResultsToSessionUsers")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public String sendResultsToSessionUsers(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	sessionMap.remove("emailPreviewDTO"); // clear any old cached emails

	Long contentId = (Long) sessionMap.get(PeerreviewConstants.ATTR_TOOL_CONTENT_ID);
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");

	int numEmailsSent = service.emailReportToSessionUsers(contentId, toolSessionId);

	response.setContentType("text/html;charset=utf-8");
	return service.getLocalisedMessage("msg.results.sent", new Object[] { numEmailsSent });
    }

    /**
     * Exports Team Report into Excel spreadsheet.
     */
    @RequestMapping(path = "/exportTeamReport", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void exportTeamReport(HttpServletRequest request, HttpServletResponse response) throws ServletException {
	Long toolContentId = WebUtil.readLongParam(request, PeerreviewConstants.ATTR_TOOL_CONTENT_ID);

	Peerreview peerreview = service.getPeerreviewByContentId(toolContentId);
	if (peerreview == null) {
	    log.warn("Did not find Peer Review with toolContentId: " + toolContentId + " export content");
	    return;
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

	    List<ExcelSheet> sheets = service.exportTeamReportSpreadsheet(toolContentId);

	    // set cookie that will tell JS script that export has been finished
	    WebUtil.setFileDownloadTokenCookie(request, response);

	    ExcelUtil.createExcel(out, sheets, "Exported on:", true);

	} catch (IOException e) {
	    log.error("exportTeamReportExcelSpreadsheet i/o error occured: " + e.getMessage(), e);
	    throw new ServletException(e);
	}
    }

    @RequestMapping("/manageUsers")
    @SuppressWarnings("unchecked")
    public String manageUsers(HttpServletRequest request, HttpServletResponse response) throws ServletException {

	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	return MANAGE_USERS_PATH;
    }

    /**
     * Gets a paged set of data for stars or comments. These are directly saved to the database, not through
     * LearnerAction like Ranking and Hedging.
     */
    @RequestMapping("/getManageUsers")
    @ResponseBody
    public String getManageUsers(HttpServletRequest request, HttpServletResponse res)
	    throws IOException, ServletException {

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
	    cell.put("userUid", asLong(rawRow[0]));
	    cell.put("userName", (String) rawRow[2]);

	    ObjectNode row = JsonNodeFactory.instance.objectNode();
	    row.put("id", "" + rawRow[0]);
	    row.set("cell", cell);
	    rows.add(row);
	}
	responcedata.set("rows", rows);

	res.setContentType("application/json;charset=utf-8");
	return responcedata.toString();
    }

    @RequestMapping("/setUserHidden")
    @ResponseBody
    public String setUserHidden(HttpServletRequest request, HttpServletResponse response) {

	Long toolContentId = WebUtil.readLongParam(request, PeerreviewConstants.ATTR_TOOL_CONTENT_ID);
	Long userUid = WebUtil.readLongParam(request, "userUid");
	boolean isHidden = !WebUtil.readBooleanParam(request, "hidden");

	service.setUserHidden(toolContentId, userUid, isHidden);
	return "";
    }
}