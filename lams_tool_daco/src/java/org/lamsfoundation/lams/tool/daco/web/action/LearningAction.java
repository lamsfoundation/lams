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
package org.lamsfoundation.lams.tool.daco.web.action;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswer;
import org.lamsfoundation.lams.tool.daco.model.DacoQuestion;
import org.lamsfoundation.lams.tool.daco.model.DacoSession;
import org.lamsfoundation.lams.tool.daco.model.DacoUser;
import org.lamsfoundation.lams.tool.daco.service.DacoApplicationException;
import org.lamsfoundation.lams.tool.daco.service.IDacoService;
import org.lamsfoundation.lams.tool.daco.service.UploadDacoFileException;
import org.lamsfoundation.lams.tool.daco.util.DacoQuestionComparator;
import org.lamsfoundation.lams.tool.daco.web.form.RecordForm;
import org.lamsfoundation.lams.tool.daco.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class LearningAction extends Action {

	private static Logger log = Logger.getLogger(LearningAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String param = mapping.getParameter();
		// -----------------------Daco Learner function
		// ---------------------------
		if (param.equals("start")) {
			return start(mapping, form, request, response);
		}

		if (param.equals("finish")) {
			return finish(mapping, form, request, response);
		}

		if (param.equals("saveOrUpdateRecord")) {
			return saveOrUpdateRecord(mapping, form, request, response);
		}

		if (param.equals("editRecord")) {
			return editRecord(mapping, form, request);
		}

		if (param.equals("removeRecord")) {
			return removeRecord(mapping, request);
		}

		if (param.equals("diplayHorizontalRecordList")) {
			return diplayHorizontalRecordList(mapping, request);
		}

		if (param.equals("changeView")) {
			return changeView(mapping, request);
		}

		// ================ Reflection =======================
		if (param.equals("newReflection")) {
			return newReflection(mapping, form, request, response);
		}
		if (param.equals("submitReflection")) {
			return submitReflection(mapping, form, request, response);
		}

		return mapping.findForward(DacoConstants.ERROR);
	}

	/**
	 * @param mapping
	 * @param request
	 * @return
	 */
	protected ActionForward diplayHorizontalRecordList(ActionMapping mapping, HttpServletRequest request) {
		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID));
		return mapping.findForward(DacoConstants.SUCCESS);
	}

	/**
	 * Read daco data from database and put them into HttpSession. It will redirect to init.do directly after this method run
	 * successfully.
	 * 
	 * This method will avoid read database again and lost un-saved resouce question lost when user "refresh page",
	 * 
	 */
	protected ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		// initial Session Map
		SessionMap sessionMap = new SessionMap();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

		// save toolContentID into HTTPSession
		ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

		Long sessionId = new Long(request.getParameter(DacoConstants.PARAM_TOOL_SESSION_ID));

		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
		request.setAttribute(DacoConstants.ATTR_DISPLAYED_RECORD_NUMBER, 1);

		// get back the daco and question list and display them on page
		IDacoService service = getDacoService();
		DacoUser dacoUser = null;
		if (mode != null && mode.isTeacher()) {
			// monitoring mode - user is specified in URL
			// dacoUser may be null if the user was force completed.
			dacoUser = getSpecifiedUser(service, sessionId, WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
		}
		else {
			dacoUser = getCurrentUser(service, sessionId);
		}

		Daco daco = service.getDacoBySessionId(sessionId);

		// check whehter finish lock is on/off
		boolean lock = daco.getLockWhenFinished() && dacoUser != null && dacoUser.isSessionFinished();

		// get notebook entry
		String entryText = new String();
		if (dacoUser != null) {
			NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
					DacoConstants.TOOL_SIGNATURE, dacoUser.getUserId().intValue());
			if (notebookEntry != null) {
				entryText = notebookEntry.getEntry();
			}
		}

		sessionMap.put(DacoConstants.ATTR_FINISH_LOCK, lock);
		sessionMap.put(DacoConstants.ATTR_USER_FINISHED, dacoUser != null && dacoUser.isSessionFinished());
		sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
		sessionMap.put(AttributeNames.ATTR_MODE, mode);
		sessionMap.put(DacoConstants.ATTR_REFLECTION_ENTRY, entryText);
		sessionMap.put(DacoConstants.ATTR_DACO, daco);
		sessionMap.put(DacoConstants.ATTR_LEARNING_VIEW, DacoConstants.LEARNING_VIEW_VERTICAL);

		List<List<DacoAnswer>> records = service.getDacoAnswersByUserAndDaco(dacoUser.getUid(), daco);
		sessionMap.put(DacoConstants.ATTR_RECORD_LIST, records);

		// add define later support
		if (daco.isDefineLater()) {
			return mapping.findForward(DacoConstants.DEFINE_LATER);
		}

		// set contentInUse flag to true!
		daco.setContentInUse(true);
		daco.setDefineLater(false);
		service.saveOrUpdateDaco(daco);

		// add run offline support
		if (daco.getRunOffline()) {
			sessionMap.put(DacoConstants.PARAM_RUN_OFFLINE, true);
			return mapping.findForward(DacoConstants.RUN_OFFLINE);
		}
		else {
			sessionMap.put(DacoConstants.PARAM_RUN_OFFLINE, false);
		}

		sessionMap.put(DacoConstants.ATTR_DACO, daco);
		return mapping.findForward(DacoConstants.SUCCESS);
	}

	/**
	 * Finish learning session.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		// get back SessionMap
		String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

		// get mode and ToolSessionID from sessionMAP
		ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
		Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

		ActionErrors errors = validateBeforeFinish(request, sessionMapID);
		if (!errors.isEmpty()) {
			this.addErrors(request, errors);
			return mapping.getInputForward();
		}

		IDacoService service = getDacoService();
		// get sessionId from HttpServletRequest
		String nextActivityUrl = null;
		try {
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			Long userID = new Long(user.getUserID().longValue());

			nextActivityUrl = service.finishToolSession(sessionId, userID);
			request.setAttribute(DacoConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
		}
		catch (DacoApplicationException e) {

			LearningAction.log.error("Failed get next activity url:" + e.getMessage());
		}

		return mapping.findForward(DacoConstants.SUCCESS);
	}

	/**
	 * Save file or textfield daco question into database.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward saveOrUpdateRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		RecordForm recordForm = (RecordForm) form;
		String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		Set<DacoQuestion> questionList = ((Daco) sessionMap.get(DacoConstants.ATTR_DACO)).getDacoQuestions();
		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		sessionMap.put(DacoConstants.ATTR_LEARNING_CURRENT_TAB, 1);

		ActionErrors errors = validateRecordForm(recordForm, questionList);
		if (!errors.isEmpty()) {
			this.addErrors(request, errors);
			return mapping.findForward(DacoConstants.SUCCESS);
		}

		Long sessionId = (Long) sessionMap.get(DacoConstants.ATTR_TOOL_SESSION_ID);
		IDacoService service = getDacoService();
		DacoUser user = getCurrentUser(service, sessionId);

		List<DacoAnswer> record = null;

		List<List<DacoAnswer>> records = (List<List<DacoAnswer>>) sessionMap.get(DacoConstants.ATTR_RECORD_LIST);
		int displayedRecordNumber = recordForm.getDisplayedRecordNumber();
		boolean isEdit = false;
		if (displayedRecordNumber <= records.size()) {
			record = records.get(displayedRecordNumber - 1);
			isEdit = true;
		}
		else {
			record = new LinkedList<DacoAnswer>();
		}

		Iterator<DacoQuestion> questionIterator = questionList.iterator();
		DacoQuestion question = null;
		int formAnswerNumber = 0;
		int answerNumber = 0;
		int fileNumber = 0;
		DacoAnswer answer = null;

		while (questionIterator.hasNext()) {
			question = questionIterator.next();
			if (isEdit) {
				answer = record.get(answerNumber++);
			}
			else {
				answer = new DacoAnswer();
				answer.setQuestion(question);
				answer.setRecordId(displayedRecordNumber);
				answer.setUser(user);
			}

			switch (question.getType()) {
				case DacoConstants.QUESTION_TYPE_DATE: {
					String[] dateParts = new String[] { recordForm.getAnswer(formAnswerNumber++),
							recordForm.getAnswer(formAnswerNumber++), recordForm.getAnswer(formAnswerNumber) };
					if (!(StringUtils.isBlank(dateParts[0]) || StringUtils.isBlank(dateParts[1]) || StringUtils
							.isBlank(dateParts[2]))) {
						DateFormat dateFormat = new SimpleDateFormat(DacoConstants.DATE_FORMAT);
						Calendar calendar = Calendar.getInstance();
						calendar.set(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer
								.parseInt(dateParts[0]));
						answer.setAnswer(dateFormat.format(calendar.getTime()));
					}
					else {
						answer.setAnswer(null);

					}
				}
					formAnswerNumber++;
					break;
				case DacoConstants.QUESTION_TYPE_CHECKBOX: {
					String formAnswer = recordForm.getAnswer(formAnswerNumber);
					String[] checkboxes = formAnswer.split("&");
					if (isEdit) {
						DacoAnswer localAnswer = answer;
						do {
							service.deleteDacoAnswer(localAnswer.getUid());
							record.remove(answerNumber - 1);
							if (answerNumber <= record.size()) {
								localAnswer = record.get(answerNumber - 1);
								if (localAnswer.getQuestion().getType() != DacoConstants.QUESTION_TYPE_CHECKBOX) {
									localAnswer = null;
								}
							}
							else {
								localAnswer = null;
							}
						}
						while (localAnswer != null);
						answer = (DacoAnswer) answer.clone();

					}
					if (StringUtils.isBlank(formAnswer)) {
						answer.setAnswer(null);
					}
					else {
						for (int checkboxNumber = 0; checkboxNumber < checkboxes.length; checkboxNumber++) {
							answer.setAnswer(checkboxes[checkboxNumber]);
							if (checkboxNumber < checkboxes.length - 1) {
								service.saveOrUpdateAnswer(answer);
								record.add(answer);
								answer = (DacoAnswer) answer.clone();
							}
						}
					}
					if (isEdit) {
						record.add(answer);
					}
				}
					formAnswerNumber++;
					break;
				case DacoConstants.QUESTION_TYPE_LONGLAT: {
					answer.setAnswer(recordForm.getAnswer(formAnswerNumber++));
					service.saveOrUpdateAnswer(answer);
					if (isEdit) {
						answer = record.get(answerNumber++);
					}
					else {
						record.add(answer);
						answer = (DacoAnswer) answer.clone();
					}
					answer.setAnswer(recordForm.getAnswer(formAnswerNumber));
				}
					formAnswerNumber++;
					break;
				case DacoConstants.QUESTION_TYPE_FILE:
				case DacoConstants.QUESTION_TYPE_IMAGE: {
					FormFile file = recordForm.getFile(fileNumber);
					if (file != null) {
						try {
							service.uploadDacoAnswerFile(answer, file);
						}
						catch (UploadDacoFileException e) {
							LearningAction.log.error("Failed upload Resource File " + e.toString());
							e.printStackTrace();
						}
					}
				}
					fileNumber++;
					break;
				case DacoConstants.QUESTION_TYPE_DROPDOWN: {
					String formAnswer = recordForm.getAnswer(formAnswerNumber);
					answer.setAnswer("0".equals(formAnswer) ? null : formAnswer);
				}
					formAnswerNumber++;
					break;
				default:
					answer.setAnswer(recordForm.getAnswer(formAnswerNumber));
					formAnswerNumber++;
					break;
			}

			service.saveOrUpdateAnswer(answer);
			if (!isEdit) {
				record.add(answer);
			}
		}

		form.reset(mapping, request);
		if (isEdit) {
			request.setAttribute(DacoConstants.ATTR_RECORD_OPERATION_SUCCESS, DacoConstants.RECORD_OPERATION_EDIT);
		}
		else {

			records.add(record);
			request.setAttribute(DacoConstants.ATTR_RECORD_OPERATION_SUCCESS, DacoConstants.RECORD_OPERATION_ADD);
		}
		sessionMap.put(DacoConstants.ATTR_RECORD_LIST, records);
		request.setAttribute(DacoConstants.ATTR_DISPLAYED_RECORD_NUMBER, records.size() + 1);

		return mapping.findForward(DacoConstants.SUCCESS);
	}

	/**
	 * Display empty reflection form.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		// get session value
		String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
		ActionErrors errors = validateBeforeFinish(request, sessionMapID);
		if (!errors.isEmpty()) {
			this.addErrors(request, errors);
			return mapping.getInputForward();
		}

		ReflectionForm refForm = (ReflectionForm) form;
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

		refForm.setUserID(user.getUserID());
		refForm.setSessionMapID(sessionMapID);

		// get the existing reflection entry
		IDacoService submitFilesService = getDacoService();

		SessionMap map = (SessionMap) request.getSession().getAttribute(sessionMapID);
		Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
				DacoConstants.TOOL_SIGNATURE, user.getUserID());

		if (entry != null) {
			refForm.setEntryText(entry.getEntry());
		}

		return mapping.findForward(DacoConstants.SUCCESS);
	}

	/**
	 * Submit reflection form input database.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ReflectionForm refForm = (ReflectionForm) form;
		Integer userId = refForm.getUserID();

		String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

		IDacoService service = getDacoService();

		// check for existing notebook entry
		NotebookEntry entry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL, DacoConstants.TOOL_SIGNATURE,
				userId);

		if (entry == null) {
			// create new entry
			service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL, DacoConstants.TOOL_SIGNATURE, userId,
					refForm.getEntryText());
		}
		else {
			// update existing entry
			entry.setEntry(refForm.getEntryText());
			entry.setLastModified(new Date());
			service.updateEntry(entry);
		}

		return finish(mapping, form, request, response);
	}

	// *************************************************************************************
	// Private method
	// *************************************************************************************
	protected ActionErrors validateBeforeFinish(HttpServletRequest request, String sessionMapID) {
		ActionErrors errors = new ActionErrors();
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		int recordCount = ((List) sessionMap.get(DacoConstants.ATTR_RECORD_LIST)).size();
		Daco daco = (Daco) sessionMap.get(DacoConstants.ATTR_DACO);
		if (daco.getMinRecords() != null && recordCount < daco.getMinRecords()) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(DacoConstants.ERROR_MSG_RECORD_NOTENOUGH, daco
					.getMinRecords()));
		}
		else if (daco.getMaxRecords() != null && recordCount > daco.getMaxRecords()) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(DacoConstants.ERROR_MSG_RECORD_TOOMUCH, daco
					.getMaxRecords()));
		}
		return errors;
	}

	protected IDacoService getDacoService() {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
		return (IDacoService) wac.getBean(DacoConstants.DACO_SERVICE);
	}

	/**
	 * List save current daco questions.
	 * 
	 * @param request
	 * @return
	 */
	protected SortedSet<DacoQuestion> getDacoQuestionList(SessionMap sessionMap) {
		SortedSet<DacoQuestion> list = (SortedSet<DacoQuestion>) sessionMap.get(DacoConstants.ATTR_QUESTION_LIST);
		if (list == null) {
			list = new TreeSet<DacoQuestion>(new DacoQuestionComparator());
			sessionMap.put(DacoConstants.ATTR_QUESTION_LIST, list);
		}
		return list;
	}

	/**
	 * Get <code>java.util.List</code> from HttpSession by given name.
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	protected List getListFromSession(SessionMap sessionMap, String name) {
		List list = (List) sessionMap.get(name);
		if (list == null) {
			list = new ArrayList();
			sessionMap.put(name, list);
		}
		return list;
	}

	protected DacoUser getCurrentUser(IDacoService service, Long sessionId) {
		// try to get form system session
		HttpSession ss = SessionManager.getSession();
		// get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		DacoUser dacoUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()), sessionId);

		if (dacoUser == null) {
			DacoSession session = service.getDacoSessionBySessionId(sessionId);
			dacoUser = new DacoUser(user, session);
			service.createUser(dacoUser);
		}
		return dacoUser;
	}

	protected DacoUser getSpecifiedUser(IDacoService service, Long sessionId, Integer userId) {
		DacoUser dacoUser = service.getUserByIDAndSession(new Long(userId.intValue()), sessionId);
		if (dacoUser == null) {
			LearningAction.log.error("Unable to find specified user for daco activity. Screens are likely to fail. SessionId="
					+ sessionId + " UserId=" + userId);
		}
		return dacoUser;
	}

	protected ActionErrors validateRecordForm(RecordForm recordForm, Set<DacoQuestion> questionList) {
		ActionErrors errors = new ActionErrors();
		Iterator<DacoQuestion> questionIterator = questionList.iterator();
		DacoQuestion question = null;
		int answerNumber = 0;
		int fileNumber = 0;
		int questionNumber = 1;
		while (questionIterator.hasNext()) {
			question = questionIterator.next();
			switch (question.getType()) {
				case DacoConstants.QUESTION_TYPE_TEXTFIELD:
				case DacoConstants.QUESTION_TYPE_RADIO:
					if (question.isRequired() && StringUtils.isBlank(recordForm.getAnswer(answerNumber))) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
								questionNumber));

					}
					answerNumber++;
					break;
				case DacoConstants.QUESTION_TYPE_TEXTAREA: {
					if (StringUtils.isBlank(recordForm.getAnswer(answerNumber))) {
						if (question.isRequired()) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
									questionNumber));
						}
					}
					else if (question.getMax() != null) {
						long words = 0;
						int index = 0;
						boolean prevWhiteSpace = true;
						while (index < recordForm.getAnswer(answerNumber).length()) {
							char c = recordForm.getAnswer(answerNumber).charAt(index++);
							boolean currWhiteSpace = Character.isWhitespace(c);
							if (prevWhiteSpace && !currWhiteSpace) {
								words++;
							}
							prevWhiteSpace = currWhiteSpace;
						}
						int max = question.getMax().intValue();
						if (words > max) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									DacoConstants.ERROR_MSG_RECORD_TEXTAREA_LONG, questionNumber, max));
						}
					}
				}
					answerNumber++;
					break;
				case DacoConstants.QUESTION_TYPE_NUMBER: {
					if (StringUtils.isBlank(recordForm.getAnswer(answerNumber))) {
						if (question.isRequired()) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
									questionNumber));
						}
					}
					else {
						try {
							float number = Float.parseFloat(recordForm.getAnswer(answerNumber));
							Float min = question.getMin();
							Float max = question.getMax();
							if (min != null && number < min) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
										DacoConstants.ERROR_MSG_RECORD_NUMBER_MIN, questionNumber, min));
							}
							else if (max != null && number > max) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
										DacoConstants.ERROR_MSG_RECORD_NUMBER_MAX, questionNumber, max));
							}
						}
						catch (NumberFormatException e) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									DacoConstants.ERROR_MSG_RECORD_NUMBER_FLOAT, questionNumber));
						}
					}
				}
					answerNumber++;
					break;
				case DacoConstants.QUESTION_TYPE_DATE: {
					String day = recordForm.getAnswer(answerNumber++);
					String month = recordForm.getAnswer(answerNumber++);
					String year = recordForm.getAnswer(answerNumber);
					if (StringUtils.isBlank(day) && StringUtils.isBlank(month) && StringUtils.isBlank(year)) {
						if (question.isRequired()) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
									questionNumber));
						}
					}
					else {
						Integer yearNum = null;
						Integer monthNum = null;
						if (StringUtils.isBlank(year)) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									DacoConstants.ERROR_MSG_RECORD_DATE_YEAR_BLANK, questionNumber));
						}
						else {
							try {
								yearNum = Integer.parseInt(year);
							}
							catch (NumberFormatException e) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
										DacoConstants.ERROR_MSG_RECORD_DATE_YEAR_INT, questionNumber));
							}
						}

						if (StringUtils.isBlank(month)) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									DacoConstants.ERROR_MSG_RECORD_DATE_MONTH_BLANK, questionNumber));
						}
						else {
							try {
								monthNum = Integer.parseInt(month);
								if (monthNum < 1 || monthNum > 12) {
									errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
											DacoConstants.ERROR_MSG_RECORD_DATE_MONTH_LIMIT, questionNumber));
								}
							}
							catch (NumberFormatException e) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
										DacoConstants.ERROR_MSG_RECORD_DATE_MONTH_INT, questionNumber));
							}
						}

						if (StringUtils.isBlank(day)) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									DacoConstants.ERROR_MSG_RECORD_DATE_DAY_BLANK, questionNumber));
						}
						else {
							try {

								int dayNum = Integer.parseInt(day);
								Integer maxDays = yearNum == null || monthNum == null ? null : getMaxDays(monthNum, yearNum);
								if (dayNum < 1 || maxDays != null && dayNum > maxDays) {
									errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
											DacoConstants.ERROR_MSG_RECORD_DATE_DAY_LIMIT, questionNumber, maxDays));
								}
							}
							catch (NumberFormatException e) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
										DacoConstants.ERROR_MSG_RECORD_DATE_DAY_INT, questionNumber));
							}
						}
					}
				}
					answerNumber++;
					break;
				case DacoConstants.QUESTION_TYPE_FILE: {
					FormFile file = recordForm.getFile(fileNumber);
					if (file == null || file.getFileSize() == 0) {
						if (question.isRequired()) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
									questionNumber));
						}
					}
					else {
						FileValidatorUtil.validateFileSize(file, true, errors);
					}
					fileNumber++;
				}
					break;
				case DacoConstants.QUESTION_TYPE_IMAGE: {
					FormFile file = recordForm.getFile(fileNumber);
					if (file == null || file.getFileSize() == 0) {
						if (question.isRequired()) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
									questionNumber));
						}
					}
					else {
						String fileName = file.getFileName();
						boolean isImage = false;
						if (fileName.length() > 5) {

							String extension = fileName.substring(fileName.length() - 3);
							for (String acceptedExtension : DacoConstants.IMAGE_EXTENSIONS) {
								if (extension.equalsIgnoreCase(acceptedExtension)) {
									isImage = true;
									break;
								}
							}
						}
						if (!isImage) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									DacoConstants.ERROR_MSG_RECORD_IMAGE_FORMAT, questionNumber));
						}
						else {
							FileValidatorUtil.validateFileSize(file, true, errors);
						}
					}
					fileNumber++;
				}
					break;
				case DacoConstants.QUESTION_TYPE_DROPDOWN: {
					if (question.isRequired() && "0".equals(recordForm.getAnswer(answerNumber))) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
								questionNumber));

					}
				}
					answerNumber++;
					break;
				case DacoConstants.QUESTION_TYPE_CHECKBOX: {
					if (StringUtils.isBlank(recordForm.getAnswer(answerNumber))) {
						if (question.isRequired()) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
									questionNumber));
						}
					}
					else {
						int count = recordForm.getAnswer(answerNumber).split("&").length;
						Float min = question.getMin();
						Float max = question.getMax();
						if (min != null && count < min) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									DacoConstants.ERROR_MSG_RECORD_CHECKBOX_MIN, questionNumber, min.intValue()));
						}
						else if (max != null && count > max) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									DacoConstants.ERROR_MSG_RECORD_CHECKBOX_MAX, questionNumber, max.intValue()));
						}
					}
				}
					answerNumber++;
					break;
				case DacoConstants.QUESTION_TYPE_LONGLAT: {
					String longitude = recordForm.getAnswer(answerNumber++);
					String latitude = recordForm.getAnswer(answerNumber);
					if (StringUtils.isBlank(longitude)) {
						if (StringUtils.isBlank(latitude)) {
							if (question.isRequired()) {
								errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
										questionNumber));
							}
						}
						else {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									DacoConstants.ERROR_MSG_RECORD_LONGITUDE_BLANK, questionNumber));
						}

					}
					else if (StringUtils.isBlank(latitude)) {
						errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								DacoConstants.ERROR_MSG_RECORD_LATITUDE_BLANK, questionNumber));
					}
					else {
						try {
							Float.parseFloat(longitude);
						}
						catch (NumberFormatException e) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									DacoConstants.ERROR_MSG_RECORD_LONGITUDE_FLOAT, questionNumber));
						}
						try {
							Float.parseFloat(latitude);
						}
						catch (NumberFormatException e) {
							errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
									DacoConstants.ERROR_MSG_RECORD_LATITUDE_FLOAT, questionNumber));
						}
					}
				}
					answerNumber++;
					break;
			}
			questionNumber++;
		}
		return errors;
	}

	protected final int getMaxDays(int month, int year) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			return 31;
		}
		if (month == 2) {
			return isLeapYear(year) ? 29 : 28;
		}
		return 30;
	}

	protected final boolean isLeapYear(int year) {
		if (year % 4 == 0) {

			if (year % 100 != 0) {
				return true;
			}
			else if (year % 400 == 0) {
				return false;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	protected ActionForward editRecord(ActionMapping mapping, ActionForm form, HttpServletRequest request) {
		String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		int recordIndex = NumberUtils.stringToInt(request.getParameter(DacoConstants.PARAM_RECORD_INDEX), -1);
		List<List<DacoAnswer>> records = (List<List<DacoAnswer>>) sessionMap.get(DacoConstants.ATTR_RECORD_LIST);
		if (recordIndex != -1 && records != null && records.size() >= recordIndex) {
			List<DacoAnswer> record = records.get(recordIndex - 1);
			RecordForm recordForm = (RecordForm) form;
			recordForm.setDisplayedRecordNumber(recordIndex);
			recordForm.setSessionMapID(sessionMapID);
			StringBuilder checkboxes = null;
			int checkboxQuestionNumber = 0;
			int formAnswerNumber = 0;
			for (int answerNumber = 0; answerNumber < record.size(); answerNumber++) {
				DacoAnswer answer = record.get(answerNumber);
				short questionType = answer.getQuestion().getType();
				if (questionType == DacoConstants.QUESTION_TYPE_CHECKBOX) {
					if (checkboxes == null) {
						checkboxes = new StringBuilder(record.size() * 3);
						checkboxQuestionNumber = formAnswerNumber;
					}
					checkboxes.append(answer.getAnswer()).append('&');
				}
				else {
					if (checkboxes != null) {
						recordForm.setAnswer(checkboxQuestionNumber, checkboxes.toString());
						checkboxes = null;
					}
					if (questionType == DacoConstants.QUESTION_TYPE_DATE) {
						String[] dateParts = null;
						if (answer.getAnswer() != null) {
							dateParts = answer.getAnswer().split(DacoConstants.DATE_PART_DELIMETER);
						}
						else {
							dateParts = new String[] { null, null, null };
						}
						recordForm.setAnswer(formAnswerNumber++, dateParts[0]);
						recordForm.setAnswer(formAnswerNumber++, dateParts[1]);
						recordForm.setAnswer(formAnswerNumber++, dateParts[2]);
					}
					else if (!(questionType == DacoConstants.QUESTION_TYPE_FILE || questionType == DacoConstants.QUESTION_TYPE_IMAGE)) {
						recordForm.setAnswer(formAnswerNumber++, answer.getAnswer());
					}
				}
			}
			if (checkboxes != null) {
				recordForm.setAnswer(checkboxQuestionNumber, checkboxes.toString());
			}
			request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
			sessionMap.put(DacoConstants.ATTR_LEARNING_CURRENT_TAB, 1);
			request.setAttribute(DacoConstants.ATTR_DISPLAYED_RECORD_NUMBER, recordIndex);
			return mapping.findForward(DacoConstants.SUCCESS);
		}
		else {
			return null;
		}
	}

	protected ActionForward removeRecord(ActionMapping mapping, HttpServletRequest request) {
		String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		int recordIndex = NumberUtils.stringToInt(request.getParameter(DacoConstants.PARAM_RECORD_INDEX), -1);
		List<List<DacoAnswer>> records = (List<List<DacoAnswer>>) sessionMap.get(DacoConstants.ATTR_RECORD_LIST);
		if (recordIndex != -1 && records != null && records.size() >= recordIndex) {
			records.remove(recordIndex - 1);
			sessionMap.put(DacoConstants.ATTR_RECORD_LIST, records);
			request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
			return mapping.findForward(DacoConstants.SUCCESS);
		}
		else {
			return null;
		}
	}

	protected ActionForward changeView(ActionMapping mapping, HttpServletRequest request) {
		String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		request.setAttribute(DacoConstants.ATTR_DISPLAYED_RECORD_NUMBER, WebUtil.readIntParam(request,
				DacoConstants.ATTR_DISPLAYED_RECORD_NUMBER));
		String currentView = (String) sessionMap.get(DacoConstants.ATTR_LEARNING_VIEW);
		if (DacoConstants.LEARNING_VIEW_HORIZONTAL.equals(currentView)) {
			sessionMap.put(DacoConstants.ATTR_LEARNING_VIEW, DacoConstants.LEARNING_VIEW_VERTICAL);
		}
		else {
			sessionMap.put(DacoConstants.ATTR_LEARNING_VIEW, DacoConstants.LEARNING_VIEW_HORIZONTAL);
		}
		return mapping.findForward(DacoConstants.SUCCESS);
	}
}