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
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumReport;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
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
	            HttpServletRequest request, HttpServletResponse response) throws Exception {
		 String param = mapping.getParameter();
		 
		 if (param.equals("listContentUsers")) {
			 return listContentUsers(mapping,form, request, response);
		 }
		 //***************** Marks Functions ********************
		 if (param.equals("viewAllMarks")) {
			 return viewAllMarks(mapping,form, request, response);
		 }
		 if (param.equals("releaseMarks")) {
			 return releaseMarks(mapping,form, request, response);
		 }
		 if (param.equals("downloadMarks")) {
			 return downloadMarks(mapping,form, request, response);
		 }
		 if (param.equals("viewUserMark")) {
			 return viewUserMark(mapping,form, request, response);
		 }
		 if (param.equals("editMark")) {
			 return editMark(mapping,form, request, response);
		 }
		 if (param.equals("updateMark")) {
			 return updateMark(mapping,form, request, response);
		 }
		 
		 //***************** Activity and Instructions ********************
		 if (param.equals("viewActivity")) {
			 return viewActivity(mapping,form, request, response);
		 }
		 if (param.equals("editActivity")) {
			 return editActivity(mapping,form, request, response);
		 }
		 if (param.equals("updateActivity")) {
			 return updateActivity(mapping,form, request, response);
		 }
		 if (param.equals("viewInstructions")) {
			 return viewInstructions(mapping,form, request, response);
		 }
		 //***************** Statistic ********************		 
		 if (param.equals("statistic")) {
			 return statistic(mapping,form, request, response);
		 }
		 //***************** Miscellaneous ********************		 
		 if (param.equals("viewTopic")) {
			 return viewTopic(mapping,form, request, response);
		 }
		 return mapping.findForward("error");
	 }


	private ActionForward listContentUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		//get content ID from URL	
        Long contentID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		
		forumService = getForumService();
        List sessionsList = forumService.getSessionsByContentId(contentID);

        Map sessionUserMap = new HashMap();
        //build a map with all users in the submitFilesSessionList
        Iterator it = sessionsList.iterator();
        while(it.hasNext()){
            Long sessionID = ((ForumToolSession)it.next()).getUid();
            List userList = forumService.getUsersBySessionId(sessionID);
            sessionUserMap.put(sessionID, userList);
        }
        
		//request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionID);
		request.setAttribute("sessionUserMap",sessionUserMap);
		return mapping.findForward("success");
	}

	private ActionForward viewAllMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		Long sessionID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
		forumService = getForumService();
		List topicList = forumService.getAllTopicsFromSession(sessionID);
		
		Map topicsByUser = getTopicsSortedByAuthor(topicList);
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionID);
		request.setAttribute("report",topicsByUser);
		return mapping.findForward("success");
	}


	private ActionForward releaseMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		
		Long sessionID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
		//get service then update report table
		forumService = getForumService();
		forumService.releaseMarksForSession(sessionID);
		
		return mapping.findForward("success");
	}

	private ActionForward downloadMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long sessionID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
		forumService = getForumService();
		List topicList = forumService.getAllTopicsFromSession(sessionID);
		//construct Excel file format and download
		ActionMessages errors = new ActionMessages();
		try {
			//create an empty excel file
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("Marks");
			sheet.setColumnWidth((short)0,(short)5000);
			HSSFRow row=null;
			HSSFCell cell;
			Iterator iter = getTopicsSortedByAuthor(topicList).values().iterator();
			Iterator dtoIter; 
			boolean first = true;
			int idx = 0;
			int fileCount = 0;
			while(iter.hasNext()){
				List list = (List) iter.next();
				dtoIter = list.iterator();
				first = true;
				
				while(dtoIter.hasNext()){
					MessageDTO dto = (MessageDTO) dtoIter.next();
					if(first){
						first = false;
						row = sheet.createRow(0);
						cell = row.createCell((short) idx);
						cell.setCellValue("Subject");
						sheet.setColumnWidth((short)idx,(short)8000);
						++idx;
						
						cell = row.createCell((short) idx);
						cell.setCellValue("Author");
						sheet.setColumnWidth((short)idx,(short)8000);
						++idx;
						
						cell = row.createCell((short) idx);
						cell.setCellValue("Date");
						sheet.setColumnWidth((short)idx,(short)8000);
						++idx;
						
						cell = row.createCell((short) idx);
						cell.setCellValue("Marks");
						sheet.setColumnWidth((short)idx,(short)8000);
						++idx;
						
						cell = row.createCell((short) idx);
						cell.setCellValue("Comments");
						sheet.setColumnWidth((short)idx,(short)8000);
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
					cell.setCellValue(DateFormat.getInstance().format(dto.getMessage().getCreated()));
					
					cell = row.createCell((short) idx++);
					
					if(dto.getMessage() != null && dto.getMessage().getReport() != null 
							&& dto.getMessage().getReport().getMark() != null)
						cell.setCellValue(dto.getMessage().getReport().getMark().doubleValue());
					else
						cell.setCellValue("");
					
					cell = row.createCell((short) idx++);
					if(dto.getMessage() != null && dto.getMessage().getReport() != null)
						cell.setCellValue(dto.getMessage().getReport().getComment());
					else
						cell.setCellValue("");
				}
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			wb.write(bos);
			//construct download file response header
			String fileName = "marks" + sessionID+".xls";
			String mineType = "application/vnd.ms-excel";
			String header = "attachment; filename=\"" + fileName + "\";";
			response.setContentType(mineType);
			response.setHeader("Content-Disposition",header);

			byte[] data = bos.toByteArray();
			response.getOutputStream().write(data,0,data.length);
			response.getOutputStream().flush();
		} catch (IOException e) {
			log.error(e);
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("monitoring.download.error",e.toString()));
		}

		if(!errors.isEmpty()){
			saveErrors(request,errors);
			request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionID);
			return mapping.getInputForward();
		}
			
		
		return null;
	}

	private ActionForward viewUserMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long userUid = new Long(WebUtil.readLongParam(request,ForumConstants.USER_UID));
		Long sessionId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
		
		forumService = getForumService();
		List messageList = forumService.getMessagesByUserUid(userUid,sessionId);
		ForumUser user = forumService.getUser(userUid);

		//each back to web page
		request.setAttribute("topicList",messageList);
		request.setAttribute("user",user);
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionId);
		return mapping.findForward("success");
	}

	private ActionForward editMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long userUid = new Long(WebUtil.readLongParam(request,ForumConstants.USER_UID));
		Long messageId = new Long(WebUtil.readLongParam(request,ForumConstants.MESSAGE_UID));
		Long sessionId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
		
		//get Message and User from database
		forumService = getForumService();
		Message msg  = forumService.getMessage(messageId);
		ForumUser user = forumService.getUser(userUid);
		
		//echo back to web page
		MarkForm markForm = (MarkForm) form;
		if(msg.getReport() != null ){
			if(msg.getReport().getMark() != null)
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

	private ActionForward updateMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long messageId = new Long(WebUtil.readLongParam(request,ForumConstants.MESSAGE_UID));
		Long userUid = new Long(WebUtil.readLongParam(request,ForumConstants.USER_UID));
		Long sessionId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
		
		forumService = getForumService();
		Message msg  = forumService.getMessage(messageId);
		
		//save it into database
		MarkForm markForm = (MarkForm) form;
		forumService = getForumService();
		ForumReport report = msg.getReport();
		if(report == null){
			report = new ForumReport();
			msg.setReport(report);
		}
		report.setMark(new Float(Float.parseFloat(markForm.getMark())));
		report.setComment(markForm.getComment());
		forumService.updateTopic(msg);
		
		//echo back to web page
		forumService = getForumService();
		List messageList = forumService.getMessagesByUserUid(userUid,sessionId);
		ForumUser user = forumService.getUser(userUid);
		request.setAttribute("topicList",messageList);
		request.setAttribute("user",user);
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionId);
		return mapping.findForward("success");
		
	}
	
	private ActionForward viewActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long contentId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		forumService = getForumService();
		Forum forum = forumService.getForumByContentId(contentId);
		//if can not find out forum, echo back error message
		if(forum == null){
			ActionErrors errors = new ActionErrors();
			errors.add("activity.globel", new ActionMessage("error.fail.get.forum"));
			this.addErrors(request,errors);
			return mapping.getInputForward();
		}
		String title = forum.getTitle();
		String instruction = forum.getInstructions();
		
		request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID,contentId);
		request.setAttribute("title",title);
		request.setAttribute("instruction",instruction);
		return mapping.findForward("success");
	}
	private ActionForward editActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		Long contentId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		forumService = getForumService();
		Forum forum = forumService.getForumByContentId(contentId);
		//if can not find out forum, echo back error message
		if(forum == null){
			ActionErrors errors = new ActionErrors();
			errors.add("activity.globel", new ActionMessage("error.fail.get.forum"));
			this.addErrors(request,errors);
			//echo back to screen
			request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID,contentId);
			return mapping.getInputForward();
		}
		
		String title = forum.getTitle();
		String instruction = forum.getInstructions();
		request.setAttribute("title",title);
		request.setAttribute("instruction",instruction);
		request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID,contentId);
		return mapping.findForward("success");
	}

	private ActionForward updateActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long contentId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		String title = request.getParameter("title");
		String instruction = request.getParameter("instruction");
		
		forumService = getForumService();
		Forum forum = forumService.getForumByContentId(contentId);
		//if can not find out forum, echo back error message
		ActionErrors errors = new ActionErrors();
		if(forum == null){
			errors.add("activity.globel", new ActionMessage("error.fail.get.forum"));
		}
		if(StringUtils.isEmpty(title)){
			errors.add("activity.title", new ActionMessage("error.title.empty"));
		}
		//echo back to screen
		request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID,contentId);
		request.setAttribute("title",title);
		request.setAttribute("instruction",instruction);			
		if(!errors.isEmpty()){
			this.addErrors(request,errors);
			return mapping.getInputForward();
		}
		forum.setTitle(title);
		forum.setInstructions(instruction);
		forumService.updateForum(forum);
		
		return mapping.findForward("success");
	}

	private ActionForward viewInstructions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long contentId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		
		forumService = getForumService();
		Forum forum = forumService.getForumByContentId(contentId);
		//if can not find out forum, echo back error message
		if(forum == null){
			ActionErrors errors = new ActionErrors();
			errors.add("instruction.globel", new ActionMessage("error.fail.get.forum"));
			this.addErrors(request,errors);
			return mapping.getInputForward();
		}
		
		ForumForm forumForm = new ForumForm();
		forumForm.setForum(forum);
		
		request.setAttribute("forumBean",forumForm);
		return mapping.findForward("success");
	}

	private ActionForward statistic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long sessionId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));

		forumService = getForumService();
		List topicList = forumService.getRootTopics(sessionId);
		Iterator iter = topicList.iterator();
		int totalMsg = 0;
		int msgNum;
		float totalMsgMarkSum = 0;
		float msgMarkSum = 0;
		for(;iter.hasNext();){
			MessageDTO msgDto = (MessageDTO) iter.next();
			//get all message under this topic
			List topicThread = forumService.getTopicThread(msgDto.getMessage().getUid());
			//loop all message under this topic
			msgMarkSum = 0;
			Iterator threadIter = topicThread.iterator();
			for(msgNum=0;threadIter.hasNext();msgNum++){
				MessageDTO dto = (MessageDTO) threadIter.next();
				if(dto.getMark() != null)
					msgMarkSum += dto.getMark().floatValue();
			}	
			//summary to total mark
			totalMsgMarkSum += msgMarkSum;
			//set average mark to topic message DTO for display use
			msgDto.setMark(getAverageFormat(msgMarkSum/(float)msgNum));
			totalMsg += msgNum;
		}
		
		float averMark = totalMsg == 0 ? 0: (totalMsgMarkSum/(float)totalMsg);
		request.setAttribute("topicList",topicList);
		request.setAttribute("markAverage",getAverageFormat(averMark));
		request.setAttribute("totalMessage",new Integer(totalMsg));
		return mapping.findForward("success");
	}

	private Float getAverageFormat(float aver) {
		try {
			NumberFormat format = NumberFormat.getInstance();
			format.setMaximumFractionDigits(1);
			return new Float(Float.parseFloat(format.format(aver)));
		} catch (Exception e) {
			return new Float(0);
		}
	}


	private ActionForward viewTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
    	
    	Long msgUid = new Long(WebUtil.readLongParam(request,ForumConstants.MESSAGE_UID));
		
    	forumService = getForumService();
    	Message topic = forumService.getMessage(msgUid);
    	
		request.setAttribute(ForumConstants.AUTHORING_TOPIC,MessageDTO.getMessageDTO(topic));
    	return mapping.findForward("success");
	}

	//==========================================================================================
	// Utility methods
	//==========================================================================================
	/**
	 * Get Forum Service.
	 * 
	 * @return
	 */
  	private IForumService getForumService() {
  	    if ( forumService == null ) {
  	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
  	      forumService = (IForumService) wac.getBean(ForumConstants.FORUM_SERVICE);
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
		while(iter.hasNext()){
			MessageDTO dto = (MessageDTO) iter.next();
			dto.getMessage().getReport();
			List list = (List) topicsByUser.get(dto.getMessage().getCreatedBy());
			if(list == null){
				list = new ArrayList();
				topicsByUser.put(dto.getMessage().getCreatedBy(),list);
			}
			list.add(dto);
		}
		return topicsByUser;
	}
}
