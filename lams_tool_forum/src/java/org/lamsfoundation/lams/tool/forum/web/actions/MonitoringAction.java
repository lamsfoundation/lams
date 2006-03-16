/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.forum.web.actions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.dto.SessionDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumReport;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.util.ForumWebUtils;
import org.lamsfoundation.lams.tool.forum.web.forms.ForumForm;
import org.lamsfoundation.lams.tool.forum.web.forms.MarkForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {

	private static Logger log = Logger.getLogger(MonitoringAction.class);

	private IForumService forumService;

	public final ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String param = mapping.getParameter();

		if (param.equals("init")) {
			return init(mapping, form, request, response);
		}
		// ***************** Marks Functions ********************
		if (param.equals("viewAllMarks")) {
			return viewAllMarks(mapping, form, request, response);
		}
		if (param.equals("downloadMarks")) {
			return downloadMarks(mapping, form, request, response);
		}
		if (param.equals("viewUserMark")) {
			return viewUserMark(mapping, form, request, response);
		}
		if (param.equals("editMark")) {
			return editMark(mapping, form, request, response);
		}
		if (param.equals("updateMark")) {
			return updateMark(mapping, form, request, response);
		}

		// ***************** Miscellaneous ********************
		if (param.equals("viewTopic")) {
			return viewTopic(mapping, form, request, response);
		}
		return mapping.findForward("error");
	}
	
    /**
     * Default ActionForward for Monitor
     *  (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	
	/**
	 * The initial method for monitoring
	 */
	private ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		// read in parameters and set session attributes.
		Long toolContentID = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_CONTENT_ID));
		request.getSession().setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID,
				toolContentID);
		request.getSession().setAttribute(AttributeNames.PARAM_MODE,
				ToolAccessMode.TEACHER);
		
		// perform the actions for all the tabs.
		doTabs(mapping, form, request, response);
		
		return mapping.findForward("load");
	}
	
	private ActionForward doTabs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
   	 	this.listContentUsers(mapping, form, request, response);
   	 	this.viewInstructions(mapping, form, request, response);
   	 	this.viewActivity(mapping, form, request, response);
   	 	this.statistic(mapping, form, request, response);
      	return mapping.findForward("load");
	}
	
	/**
	 * The initial method for monitoring. List all users according to given
	 * Content ID.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward listContentUsers(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return userList(mapping, request);
	}

	/**
	 * View all user marks for a special Session ID
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward viewAllMarks(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Long sessionID = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_SESSION_ID));
		forumService = getForumService();
		List topicList = forumService.getAllTopicsFromSession(sessionID);

		Map topicsByUser = getTopicsSortedByAuthor(topicList);
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionID);
		request.setAttribute("report", topicsByUser);

		return mapping.findForward("success");
	}

	/**
	 * Download marks for all users in a speical session.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward downloadMarks(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long sessionID = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_SESSION_ID));
		forumService = getForumService();
		List topicList = forumService.getAllTopicsFromSession(sessionID);
		// construct Excel file format and download
		ActionMessages errors = new ActionMessages();
		try {
			// create an empty excel file
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("Marks");
			sheet.setColumnWidth((short) 0, (short) 5000);
			HSSFRow row = null;
			HSSFCell cell;
			Iterator iter = getTopicsSortedByAuthor(topicList).values()
					.iterator();
			Iterator dtoIter;
			boolean first = true;
			int idx = 0;
			int fileCount = 0;
			while (iter.hasNext()) {
				List list = (List) iter.next();
				dtoIter = list.iterator();
				first = true;

				while (dtoIter.hasNext()) {
					MessageDTO dto = (MessageDTO) dtoIter.next();
					if (first) {
						first = false;
						row = sheet.createRow(0);
						cell = row.createCell((short) idx);
						cell.setCellValue("Subject");
						sheet.setColumnWidth((short) idx, (short) 8000);
						++idx;

						cell = row.createCell((short) idx);
						cell.setCellValue("Author");
						sheet.setColumnWidth((short) idx, (short) 8000);
						++idx;

						cell = row.createCell((short) idx);
						cell.setCellValue("Date");
						sheet.setColumnWidth((short) idx, (short) 8000);
						++idx;

						cell = row.createCell((short) idx);
						cell.setCellValue("Marks");
						sheet.setColumnWidth((short) idx, (short) 8000);
						++idx;

						cell = row.createCell((short) idx);
						cell.setCellValue("Comments");
						sheet.setColumnWidth((short) idx, (short) 8000);
						++idx;
					}
					++fileCount;
					idx = 0;
					row = sheet.createRow(fileCount);
					cell = row.createCell((short) idx++);
					cell.setCellValue(dto.getMessage().getSubject());

					cell = row.createCell((short) idx++);
					cell.setCellValue(dto.getAuthor());

					cell = row.createCell((short) idx++);
					cell.setCellValue(DateFormat.getInstance().format(
							dto.getMessage().getCreated()));

					cell = row.createCell((short) idx++);

					if (dto.getMessage() != null
							&& dto.getMessage().getReport() != null
							&& dto.getMessage().getReport().getMark() != null)
						cell.setCellValue(dto.getMessage().getReport()
								.getMark().doubleValue());
					else
						cell.setCellValue("");

					cell = row.createCell((short) idx++);
					if (dto.getMessage() != null
							&& dto.getMessage().getReport() != null)
						cell.setCellValue(dto.getMessage().getReport()
								.getComment());
					else
						cell.setCellValue("");
				}
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			wb.write(bos);
			// construct download file response header
			String fileName = "marks" + sessionID + ".xls";
			String mineType = "application/vnd.ms-excel";
			String header = "attachment; filename=\"" + fileName + "\";";
			response.setContentType(mineType);
			response.setHeader("Content-Disposition", header);

			byte[] data = bos.toByteArray();
			response.getOutputStream().write(data, 0, data.length);
			response.getOutputStream().flush();
		} catch (IOException e) {
			log.error(e);
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"monitoring.download.error", e.toString()));
		}

		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,
					sessionID);
			return mapping.getInputForward();
		}

		return null;
	}

	/**
	 * View a special user's mark
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward viewUserMark(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long userUid = new Long(WebUtil.readLongParam(request,
				ForumConstants.USER_UID));
		Long sessionId = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_SESSION_ID));

		forumService = getForumService();
		List messageList = forumService
				.getMessagesByUserUid(userUid, sessionId);
		ForumUser user = forumService.getUser(userUid);

		// each back to web page
		request.setAttribute("topicList", messageList);
		request.setAttribute("user", user);
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
		return mapping.findForward("success");
	}

	/**
	 * Edit a special user's mark.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward editMark(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long userUid = new Long(WebUtil.readLongParam(request,
				ForumConstants.USER_UID));
		Long messageId = new Long(WebUtil.readLongParam(request,
				ForumConstants.MESSAGE_UID));
		Long sessionId = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_SESSION_ID));

		// get Message and User from database
		forumService = getForumService();
		Message msg = forumService.getMessage(messageId);
		ForumUser user = forumService.getUser(userUid);

		// echo back to web page
		MarkForm markForm = (MarkForm) form;
		if (msg.getReport() != null) {
			if (msg.getReport().getMark() != null)
				markForm.setMark(msg.getReport().getMark().toString());
			else
				markForm.setMark("");
			markForm.setComment(msg.getReport().getComment());
		}
		markForm.setUser(user);
		markForm.setMessageDto(MessageDTO.getMessageDTO(msg));
		markForm.setSessionId(sessionId);
		return mapping.findForward("success");
	}

	/**
	 * Update mark for a special user
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateMark(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long messageId = new Long(WebUtil.readLongParam(request,
				ForumConstants.MESSAGE_UID));
		Long userUid = new Long(WebUtil.readLongParam(request,
				ForumConstants.USER_UID));
		Long sessionId = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_SESSION_ID));

		forumService = getForumService();
		Message msg = forumService.getMessage(messageId);

		// save it into database
		MarkForm markForm = (MarkForm) form;
		forumService = getForumService();
		ForumReport report = msg.getReport();
		if (report == null) {
			report = new ForumReport();
			msg.setReport(report);
		}
		report.setMark(new Float(Float.parseFloat(markForm.getMark())));
		report.setComment(markForm.getComment());
		forumService.updateTopic(msg);

		// echo back to web page
		forumService = getForumService();
		List messageList = forumService
				.getMessagesByUserUid(userUid, sessionId);
		ForumUser user = forumService.getUser(userUid);
		request.setAttribute("topicList", messageList);
		request.setAttribute("user", user);
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
		return mapping.findForward("success");

	}

	/**
	 * View activity for content.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward viewActivity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long contentId = (Long) request.getSession().getAttribute(
				AttributeNames.PARAM_TOOL_CONTENT_ID);
		forumService = getForumService();
		Forum forum = forumService.getForumByContentId(contentId);
		// if can not find out forum, echo back error message
		if (forum == null) {
			ActionErrors errors = new ActionErrors();
			errors.add("activity.globel", new ActionMessage(
					"error.fail.get.forum"));
			this.addErrors(request, errors);
			return mapping.getInputForward();
		}
		String title = forum.getTitle();
		String instruction = forum.getInstructions();
			
		boolean isForumEditable = ForumWebUtils.isForumEditable(forum);
		request.setAttribute(ForumConstants.PAGE_EDITABLE, new Boolean(isForumEditable));
		request.setAttribute("title", title);
		request.setAttribute("instruction", instruction);
		return mapping.findForward("success");
	}

	/**
	 * Show edit page for a content activity.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward editActivity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		MonitorForm monitorForm = (MonitorForm) form;

		Long toolContentId = (Long) request.getSession().getAttribute(
				AttributeNames.PARAM_TOOL_CONTENT_ID);
		forumService = getForumService();
		Forum forum = forumService.getForumByContentId(toolContentId);

		// if can not find out forum, echo back error message
		if (forum == null) {
			ActionErrors errors = new ActionErrors();
			errors.add("activity.globel", new ActionMessage(
					"error.fail.get.forum"));
			this.addErrors(request, errors);
			log.error("Forum is null");
			return mapping.getInputForward();
		}
		String title = forum.getTitle();
		String instruction = forum.getInstructions();
		request.setAttribute("title", title);
		request.setAttribute("instruction", instruction);

		if (ForumWebUtils.isForumEditable(forum)) {
			request.setAttribute(ForumConstants.PAGE_EDITABLE, "true");
			log.debug("Forum is editable");

			// set up the request parameters to append to the URL
			Map map = new HashMap();
			map.put(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentId);
			monitorForm.setParametersToAppend(map);
		} else {
			request.setAttribute(ForumConstants.PAGE_EDITABLE, "false");
			log.debug("Forum is not editable");
		}
		return mapping.findForward("success");
	}

	/**
	 * Update activity for a content.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward updateActivity(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long contentId = (Long) request.getSession().getAttribute(
				AttributeNames.PARAM_TOOL_CONTENT_ID);
		String title = request.getParameter("title");
		String instruction = request.getParameter("instruction");

		forumService = getForumService();
		Forum forum = forumService.getForumByContentId(contentId);
		// if can not find out forum, echo back error message
		ActionErrors errors = new ActionErrors();
		if (forum == null) {
			errors.add("activity.globel", new ActionMessage(
					"error.fail.get.forum"));
		}
		if (StringUtils.isEmpty(title)) {
			errors
					.add("activity.title", new ActionMessage(
							"error.title.empty"));
		}
		// echo back to screen
		request.setAttribute("title", title);
		request.setAttribute("instruction", instruction);
		if (!errors.isEmpty()) {
			this.addErrors(request, errors);
			return mapping.getInputForward();
		}
		forum.setTitle(title);
		forum.setInstructions(instruction);
		forumService.updateForum(forum);

		return mapping.findForward("success");
	}

	/**
	 * View instruction inforamtion for a content.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward viewInstructions(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long contentId = (Long) request.getSession().getAttribute(
				AttributeNames.PARAM_TOOL_CONTENT_ID);

		forumService = getForumService();
		Forum forum = forumService.getForumByContentId(contentId);
		// if can not find out forum, echo back error message
		if (forum == null) {
			ActionErrors errors = new ActionErrors();
			errors.add("instruction.globel", new ActionMessage(
					"error.fail.get.forum"));
			this.addErrors(request, errors);
			return mapping.getInputForward();
		}

		ForumForm forumForm = new ForumForm();
		forumForm.setForum(forum);

		request.setAttribute("forumBean", forumForm);
		return mapping.findForward("success");
	}

	/**
	 * Show statisitc page for a session.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward statistic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long contentID = (Long) request.getSession().getAttribute(
				AttributeNames.PARAM_TOOL_CONTENT_ID);

		forumService = getForumService();
		Map sessionTopicsMap = new HashMap();
		Map sessionAvaMarkMap = new HashMap();
		Map sessionTotalMsgMap = new HashMap();

		List sessList = forumService.getSessionsByContentId(contentID);
		Iterator sessIter = sessList.iterator();
		while (sessIter.hasNext()) {
			ForumToolSession session = (ForumToolSession) sessIter.next();
			List topicList = forumService.getRootTopics(session.getSessionId());
			Iterator iter = topicList.iterator();
			int totalMsg = 0;
			int msgNum;
			float totalMsgMarkSum = 0;
			float msgMarkSum = 0;
			for (; iter.hasNext();) {
				MessageDTO msgDto = (MessageDTO) iter.next();
				// get all message under this topic
				List topicThread = forumService.getTopicThread(msgDto
						.getMessage().getUid());
				// loop all message under this topic
				msgMarkSum = 0;
				Iterator threadIter = topicThread.iterator();
				for (msgNum = 0; threadIter.hasNext(); msgNum++) {
					MessageDTO dto = (MessageDTO) threadIter.next();
					if (dto.getMark() != null)
						msgMarkSum += dto.getMark().floatValue();
				}
				// summary to total mark
				totalMsgMarkSum += msgMarkSum;
				// set average mark to topic message DTO for display use
				msgDto.setMark(getAverageFormat(msgMarkSum / (float) msgNum));
				totalMsg += msgNum;
			}

			float averMark = totalMsg == 0 ? 0
					: (totalMsgMarkSum / (float) totalMsg);

			SessionDTO sessionDto = new SessionDTO();
			sessionDto.setSessionID(session.getSessionId());
			sessionDto.setSessionName(session.getSessionName());

			sessionTopicsMap.put(sessionDto, topicList);
			sessionAvaMarkMap.put(session.getSessionId(),
					getAverageFormat(averMark));
			sessionTotalMsgMap.put(session.getSessionId(),
					new Integer(totalMsg));
		}
		request.setAttribute("topicList", sessionTopicsMap);
		request.setAttribute("markAverage", sessionAvaMarkMap);
		request.setAttribute("totalMessage", sessionTotalMsgMap);
		return mapping.findForward("success");
	}

	/**
	 * View topic subject, content and attachement.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward viewTopic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Long msgUid = new Long(WebUtil.readLongParam(request,
				ForumConstants.MESSAGE_UID));

		forumService = getForumService();
		Message topic = forumService.getMessage(msgUid);

		request.setAttribute(ForumConstants.AUTHORING_TOPIC, MessageDTO
				.getMessageDTO(topic));
		return mapping.findForward("success");
	}

	// ==========================================================================================
	// Utility methods
	// ==========================================================================================
	/**
	 * Show all users in a ToolContentID.
	 * 
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward userList(ActionMapping mapping,
			HttpServletRequest request) {
		Long contentID = (Long) request.getSession().getAttribute(
				AttributeNames.PARAM_TOOL_CONTENT_ID);

		forumService = getForumService();
		List sessionsList = forumService.getSessionsByContentId(contentID);

		Map sessionUsersMap = new HashMap();
		// build a map with all users in the submitFilesSessionList
		Iterator it = sessionsList.iterator();
		while (it.hasNext()) {
			SessionDTO sessionDto = new SessionDTO();
			ForumToolSession fts = (ForumToolSession) it.next();
			sessionDto.setSessionID(fts.getSessionId());
			sessionDto.setSessionName(fts.getSessionName());
			List userList = forumService
					.getUsersBySessionId(fts.getSessionId());
			sessionUsersMap.put(sessionDto, userList);
		}

		// request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionID);
		request.setAttribute("sessionUserMap", sessionUsersMap);
		return mapping.findForward("success");
	}

	/**
	 * Get Forum Service.
	 * 
	 * @return
	 */
	private IForumService getForumService() {
		if (forumService == null) {
			WebApplicationContext wac = WebApplicationContextUtils
					.getRequiredWebApplicationContext(getServlet()
							.getServletContext());
			forumService = (IForumService) wac
					.getBean(ForumConstants.FORUM_SERVICE);
		}
		return forumService;
	}

	/**
	 * @param topicList
	 * @return
	 */
	private Map getTopicsSortedByAuthor(List topicList) {
		Map topicsByUser = new HashMap();
		Iterator iter = topicList.iterator();
		while (iter.hasNext()) {
			MessageDTO dto = (MessageDTO) iter.next();
			dto.getMessage().getReport();
			List list = (List) topicsByUser
					.get(dto.getMessage().getCreatedBy());
			if (list == null) {
				list = new ArrayList();
				topicsByUser.put(dto.getMessage().getCreatedBy(), list);
			}
			list.add(dto);
		}
		return topicsByUser;
	}

	/**
	 * Get formatted average mark.
	 * 
	 * @param aver
	 * @return
	 */
	private Float getAverageFormat(float aver) {
		try {
			NumberFormat format = NumberFormat.getInstance();
			format.setMaximumFractionDigits(1);
			return new Float(Float.parseFloat(format.format(aver)));
		} catch (Exception e) {
			return new Float(0);
		}
	}

}
