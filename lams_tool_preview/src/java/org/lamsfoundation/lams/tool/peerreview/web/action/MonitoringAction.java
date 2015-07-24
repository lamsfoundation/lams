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

/* $Id$ */
package org.lamsfoundation.lams.tool.peerreview.web.action;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.rating.dto.ItemRatingCriteriaDTO;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.dto.RatingCommentDTO;
import org.lamsfoundation.lams.rating.dto.RatingDTO;
import org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants;
import org.lamsfoundation.lams.tool.peerreview.dto.GroupSummary;
import org.lamsfoundation.lams.tool.peerreview.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewSession;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;
import org.lamsfoundation.lams.tool.peerreview.service.IPeerreviewService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {
    public static Logger log = Logger.getLogger(MonitoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException {
	String param = mapping.getParameter();

	request.setAttribute("initialTabId", WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));

	if (param.equals("summary")) {
	    return summary(mapping, form, request, response);
	}
	if (param.equals("getUsers")) {
	    return getUsers(mapping, form, request, response);
	}
	if (param.equals("getSubgridData")) {
	    return getSubgridData(mapping, form, request, response);
	}
	if (param.equals("viewReflection")) {
	    return viewReflection(mapping, form, request, response);
	}

	return mapping.findForward(PeerreviewConstants.ERROR);
    }

    private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	// save contentFolderID into session
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	IPeerreviewService service = getPeerreviewService();
	List<GroupSummary> groupList = service.getGroupSummaries(contentId);

	Peerreview peerreview = service.getPeerreviewByContentId(contentId);

	// Create reflectList if reflection is enabled.
	if (peerreview.isReflectOnActivity()) {
	    List<ReflectDTO> relectList = service.getReflectList(contentId);
	    sessionMap.put(PeerreviewConstants.ATTR_REFLECT_LIST, relectList);
	}

	// user name map
	List<PeerreviewUser> sessionUsers = service.getUsersByContent(contentId);
	HashMap<Long, String> userNameMap = new HashMap<Long, String>();
	for (PeerreviewUser userIter : sessionUsers) {
	    userNameMap.put(userIter.getUserId(), userIter.getFirstName() + " " + userIter.getLastName());
	}
	sessionMap.put("userNameMap", userNameMap);

	// cache into sessionMap
	sessionMap.put(PeerreviewConstants.ATTR_SUMMARY_LIST, groupList);
	sessionMap.put(PeerreviewConstants.PAGE_EDITABLE, peerreview.isContentInUse());
	sessionMap.put(PeerreviewConstants.ATTR_PEERREVIEW, peerreview);
	sessionMap.put(PeerreviewConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(PeerreviewConstants.ATTR_IS_GROUPED_ACTIVITY, service.isGroupedActivity(contentId));
	return mapping.findForward(PeerreviewConstants.SUCCESS);
    }
    
    /**
     * Refreshes user list.
     */
    public ActionForward getUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException, JSONException {
	IPeerreviewService service = getPeerreviewService();

	Long toolContentId = WebUtil.readLongParam(request, "toolContentId");
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");
	
	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, PeerreviewConstants.PARAM_PAGE) - 1;
	int size = WebUtil.readIntParam(request, PeerreviewConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, PeerreviewConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, PeerreviewConstants.PARAM_SIDX, true);

	int sorting = PeerreviewConstants.SORT_BY_NO;
	if (sortBy != null && sortBy.equals(PeerreviewConstants.PARAM_ROW_NAME)) {
	    if (sortOrder != null && sortOrder.equals(PeerreviewConstants.SORT_DESC)) {
		sorting = PeerreviewConstants.SORT_BY_USERNAME_ASC;
	    } else {
		sorting = PeerreviewConstants.SORT_BY_USERNAME_DESC;
	    }
	}

	// in case of monitoring we show all results. in case of learning - don't show results from the current user
	Long userId = -1L;
	List<PeerreviewUser> users = service.getUsersForTablesorter(toolSessionId, userId, page, size, sorting);

	JSONObject responcedata = new JSONObject();
	JSONArray rows = new JSONArray();

	responcedata.put("total", "" + service.getCountUsersBySession(toolSessionId, userId));
	responcedata.put("page", "" + page);
	responcedata.put("records", "" + size);
	
	// handle rating criterias
	List<ItemRatingDTO> itemRatingDtos = null;
	if (!users.isEmpty()) {
	    // create itemIds list
	    List<Long> itemIds = new LinkedList<Long>();
	    for (PeerreviewUser user : users) {
		itemIds.add(user.getUserId());
	    }

	    boolean isCommentsByOtherUsersRequired = false;
	    itemRatingDtos = service.getRatingCriteriaDtos(toolContentId, itemIds, isCommentsByOtherUsersRequired,
		    userId);
	}

	for (PeerreviewUser user : users) {
	    JSONArray rowData = new JSONArray();
	    rowData.put(toolSessionId);
	    rowData.put(user.getUserId());
	    rowData.put(StringEscapeUtils.escapeHtml(user.getFirstName()) + " "
		    + StringEscapeUtils.escapeHtml(user.getLastName()));
	    
	    // find corresponding itemRatingDto
	    ItemRatingDTO itemRatingDto = null;
	    for (ItemRatingDTO itemRatingDtoIter : itemRatingDtos) {
		if (user.getUserId().equals(itemRatingDtoIter.getItemId())) {
		    itemRatingDto = itemRatingDtoIter;
		    break;
		}
	    }
	    
	    String criteriasString = "";
	    for (ItemRatingCriteriaDTO criteriaDto : itemRatingDto.getCriteriaDtos()) {
		JSONObject criteriasRow = new JSONObject();
		criteriasRow.put("ratingCriteriaId", criteriaDto.getRatingCriteria().getRatingCriteriaId());
		criteriasRow.put("title", criteriaDto.getRatingCriteria().getTitle());
		criteriasRow.put("averageRating", criteriaDto.getAverageRating());
		criteriasRow.put("numberOfVotes", criteriaDto.getNumberOfVotes());
		criteriasRow.put("userRating", criteriaDto.getUserRating());
		
		criteriasString += criteriaDto.getRatingCriteria().getTitle() + ": Avg rating " + criteriaDto.getAverageRating()
			+ " out of " + criteriaDto.getNumberOfVotes() + " votes <br>";
	    }
	    rowData.put(criteriasString);

	    JSONObject row = new JSONObject();
	    row.put("id", "" + user.getUserId());
	    row.put("cell", rowData);
	    rows.put(row);
	}

	responcedata.put("rows", rows);

	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responcedata.toString()));
	return null;
    }

    private ActionForward getSubgridData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	IPeerreviewService service = getPeerreviewService();
	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(
		sessionMapID);
	HashMap<Long, String> userNameMap = (HashMap<Long, String>) sessionMap.get("userNameMap");

	Long userId = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);
	Long contentId = (Long) sessionMap.get(PeerreviewConstants.ATTR_TOOL_CONTENT_ID);

	// ratings left by others for this user
	ItemRatingDTO userRatingDto = service.getRatingCriteriaDtoWithActualRatings(contentId, userId);
//	request.setAttribute("itemRatingDto", userRatingDto);

	JSONArray rows = new JSONArray();
	DateFormat timeTakenFormatter = new SimpleDateFormat("H:mm:ss");
	DateFormat dateFormatter = new SimpleDateFormat("d-MMM-yyyy h:mm a");
	int i = 0;
	for (ItemRatingCriteriaDTO criteriaDto : userRatingDto.getCriteriaDtos()) {

	    for (RatingDTO ratingDto : criteriaDto.getRatingDtos()) {
		JSONArray userData = new JSONArray();
		userData.put(i);
		userData.put(ratingDto.getLearner().getFirstName() + " " + ratingDto.getLearner().getLastName());
		userData.put(ratingDto.getRating());
		String title = criteriaDto.getRatingCriteria().getTitle();
		userData.put(title);

		JSONObject userRow = new JSONObject();
		userRow.put("id", i++);
		userRow.put("cell", userData);

		rows.put(userRow);
	    }
	}

	// if comments are enabled display them too
	if (userRatingDto.isCommentsEnabled()) {
	    for (RatingCommentDTO commentDto : userRatingDto.getCommentDtos()) {
		JSONArray userData = new JSONArray();
		userData.put(i);
		userData.put(userNameMap.get(commentDto.getUserId()));
		userData.put(commentDto.getComment());
		userData.put("Comments");

		JSONObject userRow = new JSONObject();
		userRow.put("id", i++);
		userRow.put("cell", userData);

		rows.put(userRow);
	    }
	}

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("total", 1);
	responseJSON.put("page", 1);
	responseJSON.put("records", rows.length());
	responseJSON.put("rows", rows);

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    private ActionForward viewReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long uid = WebUtil.readLongParam(request, PeerreviewConstants.ATTR_USER_UID);
	Long sessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	IPeerreviewService service = getPeerreviewService();
	PeerreviewUser user = service.getUser(uid);
	NotebookEntry notebookEntry = service.getEntry(sessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		PeerreviewConstants.TOOL_SIGNATURE, user.getUserId().intValue());

	PeerreviewSession session = service.getPeerreviewSessionBySessionId(sessionID);

	ReflectDTO refDTO = new ReflectDTO(user);
	if (notebookEntry == null) {
	    refDTO.setFinishReflection(false);
	    refDTO.setReflect(null);
	} else {
	    refDTO.setFinishReflection(true);
	    refDTO.setReflect(notebookEntry.getEntry());
	}
	refDTO.setReflectInstrctions(session.getPeerreview().getReflectInstructions());

	request.setAttribute("userDTO", refDTO);
	return mapping.findForward("success");
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private IPeerreviewService getPeerreviewService() {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (IPeerreviewService) wac.getBean(PeerreviewConstants.PEERREVIEW_SERVICE);
    }
}
