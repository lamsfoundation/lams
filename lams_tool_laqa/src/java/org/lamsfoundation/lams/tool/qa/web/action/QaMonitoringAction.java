/***************************************************************************
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License version 2 as
published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
USA

http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.qa.web.action;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Ozgur Demirtas
 */
public class QaMonitoringAction extends LamsDispatchAction implements QaAppConstants {
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	return null;
    }

    public ActionForward updateResponse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	Long responseUid = WebUtil.readLongParam(request, QaAppConstants.RESPONSE_UID);
	String updatedResponse = request.getParameter("updatedResponse");
	QaUsrResp qaUsrResp = qaService.getResponseById(responseUid);

	/*
	 * write out the audit log entry. If you move this after the update of the response, then make sure you update
	 * the audit call to use a copy of the original answer
	 */
	qaService.getAuditService().logChange(QaAppConstants.MY_SIGNATURE, qaUsrResp.getQaQueUser().getQueUsrId(),
		qaUsrResp.getQaQueUser().getUsername(), qaUsrResp.getAnswer(), updatedResponse);

	qaUsrResp.setAnswer(updatedResponse);
	qaService.updateUserResponse(qaUsrResp);

	return null;
    }

    public ActionForward updateResponseVisibility(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	Long responseUid = WebUtil.readLongParam(request, QaAppConstants.RESPONSE_UID);
	boolean isHideItem = WebUtil.readBooleanParam(request, QaAppConstants.IS_HIDE_ITEM);
	qaService.updateResponseVisibility(responseUid, isHideItem);

	return null;
    }

    /**
     * Set Submission Deadline
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ActionForward setSubmissionDeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	IQaService qaService = getQAService();

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaContent content = qaService.getQaContent(contentID);

	Long dateParameter = WebUtil.readLongParam(request, QaAppConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	String formattedDate = "";
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	    formattedDate = DateUtil.convertToStringForJSON(tzSubmissionDeadline, request.getLocale());
	} else {
	    //set showOtherAnswersAfterDeadline to false
	    content.setShowOtherAnswersAfterDeadline(false);
	}
	content.setSubmissionDeadline(tzSubmissionDeadline);
	qaService.saveOrUpdateQaContent(content);

	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().print(formattedDate);
	return null;
    }

    /**
     * Set Submission Deadline
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward setShowOtherAnswersAfterDeadline(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	IQaService qaService = getQAService();

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaContent content = qaService.getQaContent(contentID);

	boolean showOtherAnswersAfterDeadline = WebUtil.readBooleanParam(request,
		QaAppConstants.PARAM_SHOW_OTHER_ANSWERS_AFTER_DEADLINE);
	content.setShowOtherAnswersAfterDeadline(showOtherAnswersAfterDeadline);
	qaService.saveOrUpdateQaContent(content);

	return null;
    }

    private IQaService getQAService() {
	return QaServiceProxy.getQaService(getServlet().getServletContext());
    }

    /**
     * Get Paged Reflections
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward getReflectionsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {

	Long toolSessionId = WebUtil.readLongParam(request, QaAppConstants.TOOL_SESSION_ID);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByName = WebUtil.readIntParam(request, "column[0]", true);
	String searchString = request.getParameter("fcol[0]");

	int sorting = QaAppConstants.SORT_BY_NO;
	if (sortByName != null) {
	    sorting = sortByName.equals(0) ? QaAppConstants.SORT_BY_USERNAME_ASC : QaAppConstants.SORT_BY_USERNAME_DESC;
	}

	//return user list according to the given sessionID
	IQaService qaService = getQAService();
	List<Object[]> users = qaService.getUserReflectionsForTablesorter(toolSessionId, page, size, sorting,
		searchString);

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	responsedata.put("total_rows", qaService.getCountUsersBySessionWithSearch(toolSessionId, searchString));

	for (Object[] userAndReflection : users) {
	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();
	    responseRow.put("username", StringEscapeUtils.escapeHtml((String) userAndReflection[1]));
	    if (userAndReflection.length > 2 && userAndReflection[2] != null) {
		String reflection = StringEscapeUtils.escapeHtml((String) userAndReflection[2]);
		responseRow.put(QaAppConstants.NOTEBOOK, reflection.replaceAll("\n", "<br>"));
	    }
	    rows.add(responseRow);
	}
	responsedata.set("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;
    }

}