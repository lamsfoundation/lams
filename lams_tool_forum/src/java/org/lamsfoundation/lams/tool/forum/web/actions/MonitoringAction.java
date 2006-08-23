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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.forum.web.actions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {

	private static Logger log = Logger.getLogger(MonitoringAction.class);

	private IForumService forumService;
	
	private class SessionDTOComparator implements Comparator<SessionDTO>{
		public int compare(SessionDTO o1, SessionDTO o2) {
			if(o1 != null && o2 != null){
				return o1.getSessionName().compareTo(o2.getSessionName());
			}else if(o1 != null)
				return 1;
			else
				return -1;
		}
	}
	
	private class ForumUserComparator implements Comparator<ForumUser>{
		public int compare(ForumUser o1, ForumUser o2) {
			if(o1 != null && o2 != null){
				return o1.getLoginName().compareTo(o2.getLoginName());
			}else if(o1 != null)
				return 1;
			else
				return -1;
		}
	}
	/**
	 * Action method entry.
	 */
	public final ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String param = mapping.getParameter();

		if (param.equals("init")) {
			return init(mapping, form, request, response);
		}
		//refresh statistic page by Ajax call.
		if (param.equals("statistic")) {
			return statistic(mapping, form, request, response);
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
		
		if(param.equals("releaseMark"))
			return releaseMark(mapping, form, request, response); 
		
		// ***************** Miscellaneous ********************
		if (param.equals("viewTopic")) {
			return viewTopic(mapping, form, request, response);
		}
		if (param.equals("viewTopicTree")) {
			return viewTopicTree(mapping, form, request, response);
		}
		return mapping.findForward("error");
	}
	


	/**
	 * The initial method for monitoring
	 */
	private ActionForward init(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
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
		request.setAttribute(ForumConstants.ATTR_REPORT, topicsByUser);
		request.setAttribute(ForumConstants.PARAM_UPDATE_MODE, "listAllMarks");
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
	private ActionForward viewUserMark(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long userUid = new Long(WebUtil.readLongParam(request,
				ForumConstants.USER_UID));
		Long sessionId = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_SESSION_ID));

		forumService = getForumService();
		List<MessageDTO> messageList = forumService.getMessagesByUserUid(userUid, sessionId);
		ForumUser user = forumService.getUser(userUid);

		// each back to web page
		Map<ForumUser,List<MessageDTO>> report = new TreeMap(this.new ForumUserComparator());
		report.put(user,messageList);
		request.setAttribute(ForumConstants.ATTR_REPORT, report);
		
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
		request.setAttribute(ForumConstants.PARAM_UPDATE_MODE, "listMarks");
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
		String updateMode = request.getParameter(ForumConstants.PARAM_UPDATE_MODE);
		
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
		
		// each back to web page
		request.setAttribute(ForumConstants.ATTR_TOPIC, MessageDTO.getMessageDTO(msg));
		request.setAttribute(ForumConstants.ATTR_USER, user);
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
		request.setAttribute(ForumConstants.PARAM_UPDATE_MODE, updateMode);
		
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
		MarkForm markForm = (MarkForm) form;
		Long messageId = new Long(WebUtil.readLongParam(request,
				ForumConstants.MESSAGE_UID));
		Long userUid = new Long(WebUtil.readLongParam(request,
				ForumConstants.USER_UID));
		Long sessionId = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_SESSION_ID));
		String updateMode = request.getParameter(ForumConstants.PARAM_UPDATE_MODE);
		request.setAttribute(ForumConstants.PARAM_UPDATE_MODE, updateMode);
		
		String mark = markForm.getMark();
        ActionErrors errors = new ActionErrors();
        if (StringUtils.isBlank(mark)) {
          ActionMessage error = new ActionMessage("error.valueReqd");
          errors.add("report.mark", error);
        }else if(!NumberUtils.isNumber(mark)){
        	ActionMessage error = new ActionMessage("error.mark.needNumber");
        	errors.add("report.mark", error);
        }else {
        	try{
        		Float.parseFloat(mark);
        	}catch(Exception e){
              	ActionMessage error = new ActionMessage("error.mark.invalid.number");
            	errors.add("report.mark", error);
        	}
        }
        
		forumService = getForumService();
		// echo back to web page
		ForumUser user = forumService.getUser(userUid);
		Message msg = forumService.getMessage(messageId);
		
		request.setAttribute(ForumConstants.ATTR_USER, user);
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
		if(!errors.isEmpty()){
			// each back to web page
			request.setAttribute(ForumConstants.ATTR_TOPIC, MessageDTO.getMessageDTO(msg));
        	saveMessages(request, errors);
        	return mapping.getInputForward();
        }

		//update message report
		
		forumService = getForumService();
		ForumReport report = msg.getReport();
		if (report == null) {
			report = new ForumReport();
			msg.setReport(report);
		}
		//only session has been released mark, the data of mark release will have value.
		ForumToolSession toolSession = forumService.getSessionBySessionId(sessionId);
		if(toolSession.isMarkReleased())
			report.setDateMarksReleased(new Date());
		
		report.setMark(new Float(Float.parseFloat(mark)));
		report.setComment(markForm.getComment());
		forumService.updateTopic(msg);
		
		//echo back to topic list page: it depends which screen is come from: view special user mark, or view all user marks. 
		if(StringUtils.equals(updateMode, "listAllMarks")){
			List topicList = forumService.getAllTopicsFromSession(sessionId);
			Map topicsByUser = getTopicsSortedByAuthor(topicList);
			request.setAttribute(ForumConstants.ATTR_REPORT, topicsByUser);
		}else{
			List<MessageDTO> messageList = forumService.getMessagesByUserUid(userUid, sessionId);
			Map<ForumUser,List<MessageDTO>> topicMap = new TreeMap(this.new ForumUserComparator());
			topicMap.put(user,messageList);
			request.setAttribute(ForumConstants.ATTR_REPORT, topicMap);
		}

		//listMark or listAllMark.
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
		Long toolContentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		forumService = getForumService();
		Forum forum = forumService.getForumByContentId(toolContentID);
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
		Long toolContentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		
		forumService = getForumService();
		Forum forum = forumService.getForumByContentId(toolContentID);
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
		Long toolContentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		
		forumService = getForumService();
		Map sessionTopicsMap = new TreeMap<SessionDTO, List<MessageDTO>>(this.new SessionDTOComparator());
		Map sessionAvaMarkMap = new HashMap();
		Map sessionTotalMsgMap = new HashMap();

		List sessList = forumService.getSessionsByContentId(toolContentID);
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
	 * View all messages under one topic.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward viewTopicTree(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Long rootTopicId = WebUtil.readLongParam(request, ForumConstants.ATTR_TOPIC_ID);
		forumService = getForumService();
		// get root topic list
		List<MessageDTO> msgDtoList = forumService.getTopicThread(rootTopicId);
		request.setAttribute(ForumConstants.AUTHORING_TOPIC_THREAD, msgDtoList);
		
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



	private ActionForward releaseMark(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//get service then update report table
		forumService = getForumService();
		Long sessionID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
		forumService.releaseMarksForSession(sessionID);
		try {
			PrintWriter out = response.getWriter();
			ForumToolSession session = forumService.getSessionBySessionId(sessionID);
			String sessionName = "";
			if(session != null)
				sessionName = session.getSessionName();
			out.write(getMessageService().getMessage("msg.mark.released",new String[]{sessionName}));
			out.flush();
		} catch (IOException e) {
		}
		
		return null;
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
		Long toolContentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));

		forumService = getForumService();
		List sessionsList = forumService.getSessionsByContentId(toolContentID);

		Map sessionUsersMap = new TreeMap(this.new SessionDTOComparator());
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
		Map<ForumUser,List<MessageDTO>> topicsByUser = new TreeMap(this.new ForumUserComparator());
		Iterator iter = topicList.iterator();
		forumService = getForumService();
		while (iter.hasNext()) {
			MessageDTO dto = (MessageDTO) iter.next();
			dto.getMessage().getReport();
			ForumUser user = (ForumUser) dto.getMessage().getCreatedBy().clone();
			List<MessageDTO> list = (List) topicsByUser.get(user);
			if (list == null) {
				list = new ArrayList<MessageDTO>();
				topicsByUser.put(user, list);
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
			return format.parse(format.format(aver)).floatValue();
		} catch (Exception e) {
			return new Float(0);
		}
	}
	/**
	 * Return ResourceService bean.
	 */
	private MessageService getMessageService() {
	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
	      return (MessageService) wac.getBean("forumMessageService");
	}
}
