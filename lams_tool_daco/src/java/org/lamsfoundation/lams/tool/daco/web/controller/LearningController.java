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

package org.lamsfoundation.lams.tool.daco.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.dto.QuestionSummaryDTO;
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
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Marcin Cieslak
 */
@Controller
@RequestMapping("/learning")
public class LearningController {

    private static Logger log = Logger.getLogger(LearningController.class);

    @Autowired
    private IDacoService dacoService;

    @Autowired
    @Qualifier("dacoMessageService")
    private MessageService messageService;

    /**
     * @param mapping
     * @param request
     * @return
     */
    @RequestMapping("/diplayHorizontalRecordList")
    protected String diplayHorizontalRecordList(HttpServletRequest request) {
	request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID,
		request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID));
	Long userUid = WebUtil.readLongParam(request, DacoConstants.USER_UID, true);
	if (userUid != null) {
	    request.setAttribute(DacoConstants.USER_UID, userUid);
	}
	return "pages/learning/listRecordsHorizontalPart";
    }

    /**
     * Read daco data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce question lost when user "refresh page",
     *
     */
    @RequestMapping("/start")
    protected String start(@ModelAttribute("recordForm") RecordForm recordForm, HttpServletRequest request) {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long sessionId = new Long(request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID));

	request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	// get back the daco and question list and display them on page
	DacoUser dacoUser = null;
	Daco daco = dacoService.getDacoBySessionId(sessionId);

	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // dacoUser may be null if the user was force completed.
	    dacoUser = getSpecifiedUser(dacoService, sessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    dacoUser = getCurrentUser(dacoService, sessionId, daco);
	}

	// check whehter finish lock is on/off
	boolean lock = daco.getLockOnFinished() && dacoUser != null && dacoUser.isSessionFinished();

	sessionMap.put(DacoConstants.ATTR_FINISH_LOCK, lock);
	sessionMap.put(DacoConstants.ATTR_USER_FINISHED, dacoUser != null && dacoUser.isSessionFinished());
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	sessionMap.put(AttributeNames.PARAM_USER_ID, dacoUser.getUserId());
	sessionMap.put(DacoConstants.ATTR_DACO, daco);
	String currentView = request.getParameter(DacoConstants.ATTR_LEARNING_VIEW);
	sessionMap.put(DacoConstants.ATTR_LEARNING_VIEW,
		StringUtils.isBlank(currentView) ? DacoConstants.LEARNING_VIEW_VERTICAL : currentView);

	List<List<DacoAnswer>> records = dacoService.getDacoAnswersByUser(dacoUser);
	sessionMap.put(DacoConstants.ATTR_RECORD_LIST, records);
	request.setAttribute(DacoConstants.ATTR_DISPLAYED_RECORD_NUMBER, records.size() + 1);

	List<QuestionSummaryDTO> summaries = dacoService.getQuestionSummaries(dacoUser.getUid());
	sessionMap.put(DacoConstants.ATTR_QUESTION_SUMMARIES, summaries);

	Integer totalRecordCount = dacoService.getGroupRecordCount(dacoUser.getSession().getSessionId());
	sessionMap.put(DacoConstants.ATTR_TOTAL_RECORD_COUNT, totalRecordCount);

	sessionMap.put(AttributeNames.ATTR_IS_LAST_ACTIVITY, dacoService.isLastActivity(sessionId));

	// add define later support
	if (daco.isDefineLater()) {
	    return "pages/learning/definelater";
	}

	// set contentInUse flag to true!
	daco.setContentInUse(true);
	daco.setDefineLater(false);
	dacoService.saveOrUpdateDaco(daco);

	sessionMap.put(DacoConstants.ATTR_DACO, daco);

	if (daco.isNotifyTeachersOnLearnerEntry()) {
	    dacoService.notifyTeachersOnLearnerEntry(sessionId, dacoUser);
	}

	return "pages/learning/learning";
    }

    /**
     * Finish learning session.
     */
    @RequestMapping("/finish")
    protected String finish(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, DacoApplicationException {

	// get back SessionMap
	String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	// get mode and ToolSessionID from sessionMAP
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	MultiValueMap<String, String> errorMap = validateBeforeFinish(request, sessionMapID);
	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    request.setAttribute(DacoConstants.ATTR_DISPLAYED_RECORD_NUMBER,
		    request.getParameter(DacoConstants.ATTR_DISPLAYED_RECORD_NUMBER));
	    return "pages/learning/learning";
	}

	// get sessionId from HttpServletRequest
	HttpSession httpSession = SessionManager.getSession();
	UserDTO user = (UserDTO) httpSession.getAttribute(AttributeNames.USER);
	Long userUid = new Long(user.getUserID().longValue());

	String nextActivityUrl = dacoService.finishToolSession(sessionId, userUid);
	response.sendRedirect(nextActivityUrl);
	return null;
    }

    /**
     * Save file or textfield daco question into database.
     */
    @RequestMapping(value = "/saveOrUpdateRecord", method = RequestMethod.POST)
    protected String saveOrUpdateRecord(@ModelAttribute("recordForm") RecordForm recordForm,
	    HttpServletRequest request) {
	String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Daco daco = (Daco) sessionMap.get(DacoConstants.ATTR_DACO);
	Set<DacoQuestion> questionList = daco.getDacoQuestions();
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	DacoUser user = getCurrentUser(dacoService, sessionId, daco);

	dacoService.releaseAnswersFromCache(user.getAnswers());

	request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	sessionMap.put(DacoConstants.ATTR_LEARNING_CURRENT_TAB, 1);

	/*
	 * design decision - assume users will not have a lot of records each. If very large record sets
	 * we should go to the db and just get the size & the next id, and the record to be updated,
	 * rather than manipulating the full list of records.
	 */
	List<DacoAnswer> record = null;
	List<List<DacoAnswer>> records = (List<List<DacoAnswer>>) sessionMap.get(DacoConstants.ATTR_RECORD_LIST);
	int recordCount = records.size();
	int displayedRecordNumber = recordForm.getDisplayedRecordNumber();

	/*
	 * Cannot use the displayRecordNumber as the new record id as records may be deleted and there will
	 * be missing numbers in the recordId sequence. Just using displayRecordNumber will add entries to
	 * existing records.
	 */
	int nextRecordId = 1;
	if (recordCount > 0) {
	    // records should be in recordId order, so find the next record id based on the last record
	    List<DacoAnswer> lastRecord = records.get(recordCount - 1);
	    DacoAnswer lastRecordAnswer = lastRecord.get(0);
	    if (lastRecordAnswer.getRecordId() >= nextRecordId) {
		nextRecordId = lastRecordAnswer.getRecordId() + 1;
	    }
	}

	boolean isEdit = false;
	if (displayedRecordNumber <= recordCount) {
	    record = records.get(displayedRecordNumber - 1);
	    isEdit = true;
	} else {
	    record = new LinkedList<>();
	    recordCount++;
	}

	MultiValueMap<String, String> errorMap = validateRecordForm(daco, recordForm, questionList, recordCount);
	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    refreshQuestionSummaries(request);
	    request.setAttribute(DacoConstants.ATTR_DISPLAYED_RECORD_NUMBER, recordForm.getDisplayedRecordNumber());
	    return "pages/learning/learning";
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
	    } else {
		answer = new DacoAnswer();
		answer.setQuestion(question);
		answer.setRecordId(nextRecordId);
		answer.setUser(user);
	    }
	    answer.setCreateDate(new Date());

	    switch (question.getType()) {
		case DacoConstants.QUESTION_TYPE_NUMBER: {
		    String formAnswer = recordForm.getAnswer(formAnswerNumber);
		    if (StringUtils.isBlank(formAnswer)) {
			answer.setAnswer(null);
		    } else {
			if (question.getDigitsDecimal() != null) {
			    formAnswer = NumberUtil.formatLocalisedNumber(Double.parseDouble(formAnswer), (Locale) null,
				    question.getDigitsDecimal());
			}
			answer.setAnswer(formAnswer);
		    }
		    formAnswerNumber++;
		}
		    break;
		case DacoConstants.QUESTION_TYPE_DATE: {
		    String[] dateParts = new String[] { recordForm.getAnswer(formAnswerNumber++),
			    recordForm.getAnswer(formAnswerNumber++), recordForm.getAnswer(formAnswerNumber) };
		    if (!(StringUtils.isBlank(dateParts[0]) || StringUtils.isBlank(dateParts[1])
			    || StringUtils.isBlank(dateParts[2]))) {

			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]) - 1,
				Integer.parseInt(dateParts[0]));
			answer.setAnswer(DacoConstants.DEFAULT_DATE_FORMAT.format(calendar.getTime()));
		    } else {
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
			answerNumber--;
			do {
			    dacoService.deleteDacoAnswer(localAnswer.getUid());
			    record.remove(answerNumber);
			    if (answerNumber < record.size()) {
				localAnswer = record.get(answerNumber);
				if (localAnswer.getQuestion().getType() != DacoConstants.QUESTION_TYPE_CHECKBOX) {
				    localAnswer = null;
				}
			    } else {
				localAnswer = null;
			    }
			} while (localAnswer != null);
			answer = (DacoAnswer) answer.clone();

		    }
		    if (StringUtils.isBlank(formAnswer)) {
			answer.setAnswer(null);
		    } else {
			for (int checkboxNumber = 0; checkboxNumber < checkboxes.length; checkboxNumber++) {
			    answer.setAnswer(checkboxes[checkboxNumber]);
			    if (checkboxNumber < checkboxes.length - 1) {
				dacoService.saveOrUpdateAnswer(answer);
				if (isEdit) {
				    record.add(answerNumber++, answer);
				} else {
				    record.add(answer);
				}

				answer = (DacoAnswer) answer.clone();
			    }
			}
		    }
		    if (isEdit) {
			record.add(answerNumber++, answer);
		    }
		}
		    formAnswerNumber++;
		    break;
		case DacoConstants.QUESTION_TYPE_LONGLAT: {
		    answer.setAnswer(recordForm.getAnswer(formAnswerNumber++));
		    dacoService.saveOrUpdateAnswer(answer);
		    if (isEdit) {
			answer = record.get(answerNumber++);
		    } else {
			record.add(answer);
			answer = (DacoAnswer) answer.clone();
		    }
		    answer.setAnswer(recordForm.getAnswer(formAnswerNumber));
		}
		    formAnswerNumber++;
		    break;
		case DacoConstants.QUESTION_TYPE_FILE:
		case DacoConstants.QUESTION_TYPE_IMAGE: {
		    MultipartFile file = recordForm.getFile(fileNumber);
		    if (file != null && !file.isEmpty()) {
			try {
			    dacoService.uploadDacoAnswerFile(answer, file);
			} catch (UploadDacoFileException e) {
			    LearningController.log.error("Failed upload Daco File " + e.toString());
			    errorMap.add("GLOBAL", messageService.getMessage(DacoConstants.ERROR_MSG_UPLOAD_FAILED,
				    new Object[] { e.getMessage() }));
			    request.setAttribute("errorMap", errorMap);
			    return "pages/learning/learning";
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

	    dacoService.saveOrUpdateAnswer(answer);
	    if (!isEdit) {
		record.add(answer);
	    }
	}

	recordForm.reset(request);
	if (isEdit) {
	    request.setAttribute(DacoConstants.ATTR_RECORD_OPERATION_SUCCESS, DacoConstants.RECORD_OPERATION_EDIT);
	} else {

	    records.add(record);
	    request.setAttribute(DacoConstants.ATTR_RECORD_OPERATION_SUCCESS, DacoConstants.RECORD_OPERATION_ADD);

	    // notify teachers
	    if (daco.isNotifyTeachersOnRecordSumbit()) {
		dacoService.notifyTeachersOnRecordSumbit(sessionId, user);
	    }
	}

	request.setAttribute(DacoConstants.ATTR_DISPLAYED_RECORD_NUMBER, records.size() + 1);
	refreshQuestionSummaries(request);

	return "pages/learning/learning";
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    protected MultiValueMap<String, String> validateBeforeFinish(HttpServletRequest request, String sessionMapID) {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	int recordCount = ((List) sessionMap.get(DacoConstants.ATTR_RECORD_LIST)).size();
	Daco daco = (Daco) sessionMap.get(DacoConstants.ATTR_DACO);
	Short min = daco.getMinRecords();
	if (min != null && min > 0 && recordCount < min) {
	    errorMap.add("GLOBAL", messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_NOTENOUGH,
		    new Object[] { daco.getMinRecords() }));
	}
	return errorMap;
    }

    /**
     * List save current daco questions.
     *
     * @param request
     * @return
     */
    protected SortedSet<DacoQuestion> getDacoQuestionList(SessionMap<String, Object> sessionMap) {
	SortedSet<DacoQuestion> list = (SortedSet<DacoQuestion>) sessionMap.get(DacoConstants.ATTR_QUESTION_LIST);
	if (list == null) {
	    list = new TreeSet<>(new DacoQuestionComparator());
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
    protected List getListFromSession(SessionMap<String, Object> sessionMap, String name) {
	List list = (List) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList();
	    sessionMap.put(name, list);
	}
	return list;
    }

    protected DacoUser getCurrentUser(IDacoService service, Long sessionId, Daco daco) {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	DacoUser dacoUser = service.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()), sessionId);

	if (dacoUser == null) {
	    DacoSession session = service.getSessionBySessionId(sessionId);
	    dacoUser = new DacoUser(user, session);
	    dacoUser.setDaco(daco);
	    service.createUser(dacoUser);
	}
	return dacoUser;
    }

    protected DacoUser getSpecifiedUser(IDacoService service, Long sessionId, Integer userId) {
	DacoUser dacoUser = service.getUserByUserIdAndSessionId(new Long(userId.intValue()), sessionId);
	if (dacoUser == null) {
	    LearningController.log
		    .error("Unable to find specified user for daco activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return dacoUser;
    }

    protected MultiValueMap<String, String> validateRecordForm(Daco daco, @ModelAttribute RecordForm recordForm,
	    Set<DacoQuestion> questionList, int recordCount) {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	Short maxRecords = daco.getMaxRecords();
	if (maxRecords != null && maxRecords > 0 && recordCount > maxRecords) {
	    errorMap.add("GLOBAL", messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_TOOMUCH,
		    new Object[] { daco.getMaxRecords() }));
	}

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
			errorMap.add("GLOBAL", messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
				new Object[] { questionNumber }));

		    }
		    answerNumber++;
		    break;
		case DacoConstants.QUESTION_TYPE_TEXTAREA: {
		    if (StringUtils.isBlank(recordForm.getAnswer(answerNumber))) {
			if (question.isRequired()) {
			    errorMap.add("GLOBAL", messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
				    new Object[] { questionNumber }));
			}
		    } else if (question.getMax() != null) {
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
			    errorMap.add("GLOBAL",
				    messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_TEXTAREA_LONG,
					    new Object[] { questionNumber, max }));
			}
		    }
		}
		    answerNumber++;
		    break;
		case DacoConstants.QUESTION_TYPE_NUMBER: {
		    if (StringUtils.isBlank(recordForm.getAnswer(answerNumber))) {
			if (question.isRequired()) {
			    errorMap.add("GLOBAL", messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
				    new Object[] { questionNumber }));
			}
		    } else {
			try {
			    float number = Float.parseFloat(recordForm.getAnswer(answerNumber));
			    Float min = question.getMin();
			    Float max = question.getMax();
			    if (min != null && number < min) {
				errorMap.add("GLOBAL",
					messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_NUMBER_MIN,
						new Object[] { questionNumber, min }));
			    } else if (max != null && number > max) {
				errorMap.add("GLOBAL",
					messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_NUMBER_MAX,
						new Object[] { questionNumber, max }));
			    }
			} catch (NumberFormatException e) {
			    errorMap.add("GLOBAL", messageService.getMessage(
				    DacoConstants.ERROR_MSG_RECORD_NUMBER_FLOAT, new Object[] { questionNumber }));
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
			    errorMap.add("GLOBAL", messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
				    new Object[] { questionNumber }));
			}
		    } else {
			Integer yearNum = null;
			Integer monthNum = null;
			if (StringUtils.isBlank(year)) {
			    errorMap.add("GLOBAL", messageService.getMessage(
				    DacoConstants.ERROR_MSG_RECORD_DATE_YEAR_BLANK, new Object[] { questionNumber }));
			} else {
			    try {
				yearNum = Integer.parseInt(year);
			    } catch (NumberFormatException e) {
				errorMap.add("GLOBAL", messageService.getMessage(
					DacoConstants.ERROR_MSG_RECORD_DATE_YEAR_INT, new Object[] { questionNumber }));
			    }
			}
			boolean monthValid = false;
			if (StringUtils.isBlank(month)) {
			    errorMap.add("GLOBAL", messageService.getMessage(
				    DacoConstants.ERROR_MSG_RECORD_DATE_MONTH_BLANK, new Object[] { questionNumber }));
			} else {
			    try {
				monthNum = Integer.parseInt(month);
				if (monthNum < 1 || monthNum > 12) {
				    errorMap.add("GLOBAL",
					    messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_DATE_MONTH_LIMIT,
						    new Object[] { questionNumber }));
				} else {
				    monthValid = true;
				}
			    } catch (NumberFormatException e) {
				errorMap.add("GLOBAL",
					messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_DATE_MONTH_INT,
						new Object[] { questionNumber }));
			    }
			}

			if (StringUtils.isBlank(day)) {
			    errorMap.add("GLOBAL", messageService.getMessage(
				    DacoConstants.ERROR_MSG_RECORD_DATE_DAY_BLANK, new Object[] { questionNumber }));
			} else if (monthValid) {
			    try {

				int dayNum = Integer.parseInt(day);
				Integer maxDays = yearNum == null || monthNum == null ? null
					: getMaxDays(monthNum, yearNum);
				if (dayNum < 1 || maxDays != null && dayNum > maxDays) {
				    errorMap.add("GLOBAL",
					    messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_DATE_DAY_LIMIT,
						    new Object[] { questionNumber, maxDays }));
				}
			    } catch (NumberFormatException e) {
				errorMap.add("GLOBAL", messageService.getMessage(
					DacoConstants.ERROR_MSG_RECORD_DATE_DAY_INT, new Object[] { questionNumber }));
			    }
			}
		    }
		}
		    answerNumber++;
		    break;
		case DacoConstants.QUESTION_TYPE_FILE: {
		    MultipartFile file = recordForm.getFile(fileNumber);
		    if (file == null || file.getSize() == 0) {
			if (question.isRequired()) {
			    errorMap.add("GLOBAL", messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
				    new Object[] { questionNumber }));
			}
		    } else {
			FileValidatorUtil.validateFileSize(file, true);
		    }
		    fileNumber++;
		}
		    break;
		case DacoConstants.QUESTION_TYPE_IMAGE: {
		    MultipartFile file = recordForm.getFile(fileNumber);
		    if (file == null || file.getSize() == 0) {
			if (question.isRequired()) {
			    errorMap.add("GLOBAL", messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
				    new Object[] { questionNumber }));
			}
		    } else {
			String fileName = file.getOriginalFilename();
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
			    errorMap.add("GLOBAL", messageService.getMessage(
				    DacoConstants.ERROR_MSG_RECORD_IMAGE_FORMAT, new Object[] { questionNumber }));
			} else {
			    FileValidatorUtil.validateFileSize(file, true);
			}
		    }
		    fileNumber++;
		}
		    break;
		case DacoConstants.QUESTION_TYPE_DROPDOWN: {
		    if (question.isRequired() && "0".equals(recordForm.getAnswer(answerNumber))) {
			errorMap.add("GLOBAL", messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
				new Object[] { questionNumber }));

		    }
		}
		    answerNumber++;
		    break;
		case DacoConstants.QUESTION_TYPE_CHECKBOX: {
		    if (StringUtils.isBlank(recordForm.getAnswer(answerNumber))) {
			if (question.isRequired()) {
			    errorMap.add("GLOBAL", messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
				    new Object[] { questionNumber }));
			}
		    } else {
			int count = recordForm.getAnswer(answerNumber).split("&").length;
			Float min = question.getMin();
			Float max = question.getMax();
			if (min != null && count < min) {
			    errorMap.add("GLOBAL",
				    messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_CHECKBOX_MIN,
					    new Object[] { questionNumber, min.intValue() }));
			} else if (max != null && count > max) {
			    errorMap.add("GLOBAL",
				    messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_CHECKBOX_MAX,
					    new Object[] { questionNumber, max.intValue() }));
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
				errorMap.add("GLOBAL", messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_BLANK,
					new Object[] { questionNumber }));
			    }
			} else {
			    errorMap.add("GLOBAL", messageService.getMessage(
				    DacoConstants.ERROR_MSG_RECORD_LONGITUDE_BLANK, new Object[] { questionNumber }));
			}

		    } else if (StringUtils.isBlank(latitude)) {
			errorMap.add("GLOBAL", messageService.getMessage(DacoConstants.ERROR_MSG_RECORD_LATITUDE_BLANK,
				new Object[] { questionNumber }));
		    } else {
			try {
			    Float.parseFloat(longitude);
			} catch (NumberFormatException e) {
			    errorMap.add("GLOBAL", messageService.getMessage(
				    DacoConstants.ERROR_MSG_RECORD_LONGITUDE_FLOAT, new Object[] { questionNumber }));
			}
			try {
			    Float.parseFloat(latitude);
			} catch (NumberFormatException e) {
			    errorMap.add("GLOBAL", messageService.getMessage(
				    DacoConstants.ERROR_MSG_RECORD_LATITUDE_FLOAT, new Object[] { questionNumber }));
			}
		    }
		}
		    answerNumber++;
		    break;
	    }
	    questionNumber++;
	}
	return errorMap;
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
	    } else if (year % 400 == 0) {
		return false;
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    @RequestMapping("/editRecord")
    protected String editRecord(@ModelAttribute("recordForm") RecordForm recordForm, HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	int recordIndex = NumberUtils.stringToInt(request.getParameter(DacoConstants.PARAM_RECORD_INDEX), -1);
	List<List<DacoAnswer>> records = (List<List<DacoAnswer>>) sessionMap.get(DacoConstants.ATTR_RECORD_LIST);
	if (recordIndex != -1 && records != null && records.size() >= recordIndex) {
	    List<DacoAnswer> record = records.get(recordIndex - 1);
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
			checkboxQuestionNumber = formAnswerNumber++;
		    }
		    checkboxes.append(answer.getAnswer()).append('&');
		} else {
		    if (checkboxes != null) {
			recordForm.setAnswer(checkboxQuestionNumber, checkboxes.toString());
			checkboxes = null;
		    }
		    if (questionType == DacoConstants.QUESTION_TYPE_DATE) {
			String[] dateParts = new String[3];
			if (answer.getAnswer() != null) {
			    Calendar calendar = Calendar.getInstance();
			    calendar.clear();

			    try {
				calendar.setTime(DacoConstants.DEFAULT_DATE_FORMAT.parse(answer.getAnswer()));
			    } catch (ParseException e) {
				LearningController.log.error(e.getMessage());
				e.printStackTrace();
			    }

			    dateParts[0] = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			    dateParts[1] = String.valueOf(calendar.get(Calendar.MONTH) + 1);
			    dateParts[2] = String.valueOf(calendar.get(Calendar.YEAR));
			}
			recordForm.setAnswer(formAnswerNumber++, dateParts[0]);
			recordForm.setAnswer(formAnswerNumber++, dateParts[1]);
			recordForm.setAnswer(formAnswerNumber++, dateParts[2]);
		    } else if (!(questionType == DacoConstants.QUESTION_TYPE_FILE
			    || questionType == DacoConstants.QUESTION_TYPE_IMAGE)) {
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
	    return "pages/learning/addRecord";
	} else {
	    return null;
	}
    }

    @RequestMapping("/removeRecord")
    protected String removeRecord(HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	int recordIndex = NumberUtils.stringToInt(request.getParameter(DacoConstants.PARAM_RECORD_INDEX), -1);
	List<List<DacoAnswer>> records = (List<List<DacoAnswer>>) sessionMap.get(DacoConstants.ATTR_RECORD_LIST);
	if (recordIndex != -1 && records != null && records.size() >= recordIndex) {
	    List<DacoAnswer> record = records.get(recordIndex - 1);
	    dacoService.deleteDacoRecord(record);
	    records.remove(record);
	    sessionMap.put(DacoConstants.ATTR_RECORD_LIST, records);
	    request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	    return "pages/learning/listRecords";
	} else {
	    return null;
	}
    }

    @RequestMapping("/changeView")
    protected String changeView(HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long userId = (Long) sessionMap.get(AttributeNames.PARAM_USER_ID);
	
	String currentView = (String) sessionMap.get(DacoConstants.ATTR_LEARNING_VIEW);
	if (DacoConstants.LEARNING_VIEW_HORIZONTAL.equals(currentView)) {
	    currentView = DacoConstants.LEARNING_VIEW_VERTICAL;
	} else {
	    currentView = DacoConstants.LEARNING_VIEW_HORIZONTAL;
	}
	
	return "redirect:start.do?" + AttributeNames.PARAM_TOOL_SESSION_ID + "=" + sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID) + 
		"&" + AttributeNames.PARAM_MODE + "=" + sessionMap.get(AttributeNames.PARAM_MODE) + 
		(mode.isTeacher() ? "&" + AttributeNames.PARAM_USER_ID + "=" + userId : "") +
		"&" + DacoConstants.ATTR_LEARNING_VIEW + "=" + currentView;
    }

    @RequestMapping("/refreshQuestionSummaries")
    protected String refreshQuestionSummaries(HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Daco daco = (Daco) sessionMap.get(DacoConstants.ATTR_DACO);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	DacoUser user = getCurrentUser(dacoService, sessionId, daco);

	// get mode - monitoring vs learner
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // user may be null if the user was force completed.
	    user = getSpecifiedUser(dacoService, sessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    user = getCurrentUser(dacoService, sessionId, daco);
	}

	if (user != null) {
	    List<QuestionSummaryDTO> summaries = dacoService.getQuestionSummaries(user.getUid());
	    sessionMap.put(DacoConstants.ATTR_QUESTION_SUMMARIES, summaries);
	    Integer totalRecordCount = dacoService.getGroupRecordCount(user.getSession().getSessionId());
	    sessionMap.put(DacoConstants.ATTR_TOTAL_RECORD_COUNT, totalRecordCount);
	} else {
	    sessionMap.put(DacoConstants.ATTR_QUESTION_SUMMARIES, new LinkedList<QuestionSummaryDTO>());
	    sessionMap.put(DacoConstants.ATTR_TOTAL_RECORD_COUNT, 0);
	}

	request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/learning/questionSummaries";
    }

}