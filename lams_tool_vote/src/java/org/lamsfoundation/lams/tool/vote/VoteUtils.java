/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUploadedFile;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.web.VoteAuthoringForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * <p>
 * Common Voting utility functions live here.
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public abstract class VoteUtils implements VoteAppConstants {

    public static String replaceNewLines(String text) {
	String newText = "";
	if (text != null) {
	    newText = text.replaceAll("\n", "<br>");
	}

	return newText;
    }

    public static String getCurrentLearnerID() {
	String userID = "";
	HttpSession ss = SessionManager.getSession();

	if (ss != null) {
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if ((user != null) && (user.getUserID() != null)) {
		userID = user.getUserID().toString();
	    }
	}
	return userID;
    }

    /**
     * 
     * getGMTDateTime(HttpServletRequest request)
     * 
     * @param request
     * @return
     */
    /* fix this */
    public static Date getGMTDateTime() {
	Date date = new Date(System.currentTimeMillis());
	return date;
    }

    public static String getFormattedDateString(Date date) {
	return (DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(date));
    }

    public static void saveTimeZone(HttpServletRequest request) {
	TimeZone timeZone = TimeZone.getDefault();
    }

    public static String getCurrentTimeZone() {
	TimeZone timeZone = TimeZone.getDefault();
	return timeZone.getDisplayName();
    }

    /**
     * existsContent(long toolContentId)
     * 
     * @param long toolContentId
     * @return boolean determine whether a specific toolContentId exists in the db
     */
    public static boolean existsContent(Long toolContentId, HttpServletRequest request, IVoteService voteService) {

	VoteContent voteContent = voteService.retrieveVote(toolContentId);
	if (voteContent == null)
	    return false;

	return true;
    }

    /**
     * it is expected that the tool session id already exists in the tool sessions table existsSession(long
     * toolSessionId)
     * 
     * @param toolSessionId
     * @return boolean
     */
    public static boolean existsSession(Long toolSessionId, HttpServletRequest request, IVoteService voteService) {

	VoteSession voteSession = voteService.retrieveVoteSession(toolSessionId);

	if (voteSession == null)
	    return false;

	return true;
    }

    public static void readContentValues(HttpServletRequest request, VoteContent defaultVoteContent,
	    VoteAuthoringForm voteAuthoringForm, VoteGeneralAuthoringDTO voteGeneralAuthoringDTO) {
	/* should never be null anyway as default content MUST exist in the db */
	if (defaultVoteContent == null)
	    throw new NullPointerException("Default VoteContent cannot be null");

	voteGeneralAuthoringDTO.setActivityTitle(defaultVoteContent.getTitle());
	voteGeneralAuthoringDTO.setActivityInstructions(defaultVoteContent.getInstructions());

	voteAuthoringForm.setUseSelectLeaderToolOuput(defaultVoteContent.isUseSelectLeaderToolOuput() ? "1" : "0");
	voteAuthoringForm.setAllowText(defaultVoteContent.isAllowText() ? "1" : "0");
	voteAuthoringForm.setAllowTextEntry(defaultVoteContent.isAllowText() ? "1" : "0");

	voteAuthoringForm.setShowResults(defaultVoteContent.isShowResults() ? "1" : "0");

	voteAuthoringForm.setLockOnFinish(defaultVoteContent.isLockOnFinish() ? "1" : "0");
	voteAuthoringForm.setReflect(defaultVoteContent.isReflect() ? "1" : "0");

	voteAuthoringForm.setOnlineInstructions(defaultVoteContent.getOnlineInstructions());
	voteAuthoringForm.setOfflineInstructions(defaultVoteContent.getOfflineInstructions());

	voteGeneralAuthoringDTO
		.setUseSelectLeaderToolOuput(defaultVoteContent.isUseSelectLeaderToolOuput() ? "1" : "0");
	voteGeneralAuthoringDTO.setAllowText(defaultVoteContent.isAllowText() ? "1" : "0");
	voteGeneralAuthoringDTO.setLockOnFinish(defaultVoteContent.isLockOnFinish() ? "1" : "0");
	voteAuthoringForm.setReflect(defaultVoteContent.isReflect() ? "1" : "0");

	voteGeneralAuthoringDTO.setOnlineInstructions(defaultVoteContent.getOnlineInstructions());
	voteGeneralAuthoringDTO.setOfflineInstructions(defaultVoteContent.getOfflineInstructions());

	String maxNomcount = defaultVoteContent.getMaxNominationCount();
	if (maxNomcount.equals(""))
	    maxNomcount = "0";
	voteAuthoringForm.setMaxNominationCount(maxNomcount);
	voteGeneralAuthoringDTO.setMaxNominationCount(maxNomcount);

	String minNomcount = defaultVoteContent.getMinNominationCount();
	if ((minNomcount == null) || minNomcount.equals(""))
	    minNomcount = "0";
	voteAuthoringForm.setMinNominationCount(minNomcount);
	voteGeneralAuthoringDTO.setMinNominationCount(minNomcount);
    }

    public static String stripHTML(String htmlText) {
	String noHTMLText = htmlText.replaceAll("\\<.*?\\>", "").replaceAll("&nbsp;", "")
		.replaceAll("&#[0-9][0-9][0-9][0-9];", "");
	String[] htmlTokens = noHTMLText.split("\n");
	String noHtmlNoNewLineStr = "";
	for (int i = 0; i < htmlTokens.length; i++) {
	    if (!htmlTokens[i].trim().equals("")) {
		noHtmlNoNewLineStr = noHtmlNoNewLineStr.length() > 0 ? noHtmlNoNewLineStr + " " + htmlTokens[i]
			: htmlTokens[i];
	    }
	}

	if (noHtmlNoNewLineStr.trim().length() == 0) {
	    // nomination text is just composed of html markup, try getting just a src entry for a picture otherwise
	    // give up
	    htmlText = htmlText.toLowerCase();
	    int index = htmlText.indexOf("src");
	    if (index > -1) {
		index = htmlText.indexOf("\"", index);
		if (index > -1 && index < htmlText.length()) {
		    int indexStop = htmlText.indexOf("\"", index + 1);
		    if (indexStop > -1) {
			String srcEntry = htmlText.substring(index + 1, indexStop);
			// get rid of any leading path and just get the filename;
			index = srcEntry.lastIndexOf("/");
			if (index == srcEntry.length() - 1)
			    index = srcEntry.lastIndexOf("/", index);
			if (index > -1) {
			    srcEntry = srcEntry.substring(index + 1);
			}
			return srcEntry;
		    }
		}
	    }
	    if (htmlText.length() > 50)
		return htmlText.substring(0, 51);
	    else
		return htmlText;
	}

	if (noHtmlNoNewLineStr.length() > 50)
	    return noHtmlNoNewLineStr.substring(0, 51);

	return noHtmlNoNewLineStr;
    }

    public static void saveRichText(HttpServletRequest request, VoteGeneralAuthoringDTO voteGeneralAuthoringDTO,
	    SessionMap sessionMap) {
	String richTextTitle = request.getParameter(TITLE);
	String richTextInstructions = request.getParameter(INSTRUCTIONS);

	if (richTextTitle != null) {
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	}
	String noHTMLTitle = stripHTML(richTextTitle);

	if (richTextInstructions != null) {
	    voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	}

	String richTextOfflineInstructions = request.getParameter(RICHTEXT_OFFLINEINSTRUCTIONS);

	if ((richTextOfflineInstructions != null) && (richTextOfflineInstructions.length() > 0)) {
	    voteGeneralAuthoringDTO.setRichTextOfflineInstructions(richTextOfflineInstructions);
	    sessionMap.put(OFFLINE_INSTRUCTIONS_KEY, richTextOfflineInstructions);
	}

	String richTextOnlineInstructions = request.getParameter(RICHTEXT_ONLINEINSTRUCTIONS);

	if ((richTextOnlineInstructions != null) && (richTextOnlineInstructions.length() > 0)) {
	    voteGeneralAuthoringDTO.setRichTextOnlineInstructions(richTextOnlineInstructions);
	    sessionMap.put(ONLINE_INSTRUCTIONS_KEY, richTextOnlineInstructions);
	}
    }

    public static void configureContentRepository(HttpServletRequest request, IVoteService voteService) {
	voteService.configureContentRepository();
    }

    /**
     * temporary function
     * 
     * @return
     */
    public static int getCurrentUserId(HttpServletRequest request) throws VoteApplicationException {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user.getUserID().intValue();
    }

    /**
     * temporary function
     * 
     * @return
     */
    public static User createSimpleUser(Integer userId) {
	User user = new User();
	user.setUserId(userId);
	return user;
    }

    /**
     * temporary function
     * 
     * @return
     */
    public static boolean getDefineLaterStatus() {
	return false;
    }

    /**
     * builds a map from a list convertToMap(List sessionsList)
     * 
     * @param sessionsList
     * @return Map
     */
    public static Map convertToMap(List sessionsList, String listType) {
	Map map = new TreeMap(new VoteComparator());

	Iterator listIterator = sessionsList.iterator();
	Long mapIndex = new Long(1);

	while (listIterator.hasNext()) {
	    if (listType.equals("String")) {
		String text = (String) listIterator.next();
		map.put(mapIndex.toString(), text);
	    } else if (listType.equals("Long")) {
		Long LongValue = (Long) listIterator.next();
		map.put(mapIndex.toString(), LongValue);
	    }
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return map;
    }

    /**
     * builds a String based map from a list convertToMap(List sessionsList)
     * 
     * @param sessionsList
     * @return Map
     */
    public static Map convertToStringMap(List sessionsList, String listType) {
	Map map = new TreeMap(new VoteComparator());

	Iterator listIterator = sessionsList.iterator();
	Long mapIndex = new Long(1);

	while (listIterator.hasNext()) {
	    if (listType.equals("String")) {
		String text = (String) listIterator.next();
		map.put(mapIndex.toString(), text);
	    } else if (listType.equals("Long")) {
		Long LongValue = (Long) listIterator.next();
		map.put(mapIndex.toString(), LongValue.toString());
	    }
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return map;
    }

    /**
     * find out if the content is in use or not. If it is in use, the author can not modify it. The idea of content
     * being in use is, once any one learner starts using a particular content that content should become unmodifiable.
     * 
     * isContentInUse(VoteContent voteContent)
     * 
     * @param voteContent
     * @return boolean
     */
    public static boolean isContentInUse(VoteContent voteContent) {
	return voteContent.isContentInUse();
    }

    /**
     * find out if the content is being edited in monitoring interface or not. If it is, the author can not modify it.
     * 
     * isDefineLater(VoteContent voteContent)
     * 
     * @param voteContent
     * @return boolean
     */
    public static boolean isDefineLater(VoteContent voteContent) {
	return voteContent.isDefineLater();
    }

    /**
     * find out if the content is set to run offline or online. If it is set to run offline , the learners are informed
     * about that.. isRubnOffline(VoteContent voteContent)
     * 
     * @param voteContent
     * @return boolean
     */
    public static boolean isRunOffline(VoteContent voteContent) {
	return voteContent.isRunOffline();
    }

    public static String getDestination(String sourceVoteStarter) {

	if ((sourceVoteStarter != null) && !sourceVoteStarter.equals("monitoring")) {
	    // request is from authoring or define Later url. return to: LOAD_QUESTIONS
	    return LOAD_QUESTIONS;
	} else if (sourceVoteStarter == null) {
	    // request is from authoring url. return to: LOAD_QUESTIONS
	    return LOAD_QUESTIONS;
	} else {
	    // request is from monitoring url. return to: LOAD_MONITORING_CONTENT_EDITACTIVITY
	    return LOAD_MONITORING_CONTENT_EDITACTIVITY;
	}
    }

    public static void setDefineLater(HttpServletRequest request, boolean value, IVoteService voteService,
	    String toolContentID) {

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	if (voteContent != null) {
	    voteContent.setDefineLater(value);
	    voteService.updateVote(voteContent);
	}
    }

    /**
     * 
     * cleanUpSessionAbsolute(HttpServletRequest request)
     * 
     * @param request
     */
    public static void cleanUpSessionAbsolute(HttpServletRequest request) {
	cleanUpUserExceptions(request);
    }

    /**
     * removes attributes except USER_EXCEPTION_NO_STUDENT_ACTIVITY
     */
    public static void cleanUpUserExceptions(HttpServletRequest request) {
	request.getSession().removeAttribute(USER_EXCEPTION_WRONG_FORMAT);
	request.getSession().removeAttribute(USER_EXCEPTION_INCOMPATIBLE_IDS);
	request.getSession().removeAttribute(USER_EXCEPTION_NUMBERFORMAT);
	request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST);
	request.getSession().removeAttribute(USER_EXCEPTION_TOOLSESSION_DOESNOTEXIST);
	request.getSession().removeAttribute(USER_EXCEPTION_TOOLCONTENT_DOESNOTEXIST);
	request.getSession().removeAttribute(USER_EXCEPTION_LEARNER_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_CONTENTID_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_TOOLSESSIONID_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_TOOLSESSIONID_INCONSISTENT);
	request.getSession().removeAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOT_AVAILABLE);
	request.getSession().removeAttribute(USER_EXCEPTION_DEFAULTQUESTIONCONTENT_NOT_AVAILABLE);
	request.getSession().removeAttribute(USER_EXCEPTION_DEFAULTOPTIONSCONTENT_NOT_AVAILABLE);
	request.getSession().removeAttribute(USER_EXCEPTION_USERID_NOTAVAILABLE);
	request.getSession().removeAttribute(USER_EXCEPTION_USERID_NOTNUMERIC);
	request.getSession().removeAttribute(USER_EXCEPTION_ONLYCONTENT_ANDNOSESSIONS);
	request.getSession().removeAttribute(USER_EXCEPTION_USERID_EXISTING);
	request.getSession().removeAttribute(USER_EXCEPTION_USER_DOESNOTEXIST);
	request.getSession().removeAttribute(USER_EXCEPTION_MONITORINGTAB_CONTENTID_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP);
	request.getSession().removeAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS);
	request.getSession().removeAttribute(USER_EXCEPTION_MODE_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_IN_USE);
	request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_BEING_MODIFIED);
	request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_RUNOFFLINE);
	request.getSession().removeAttribute(USER_EXCEPTION_MODE_INVALID);
	request.getSession().removeAttribute(USER_EXCEPTION_QUESTION_EMPTY);
	request.getSession().removeAttribute(USER_EXCEPTION_ANSWER_EMPTY);
	request.getSession().removeAttribute(USER_EXCEPTION_ANSWERS_DUPLICATE);
	request.getSession().removeAttribute(USER_EXCEPTION_OPTIONS_COUNT_ZERO);
	request.getSession().removeAttribute(USER_EXCEPTION_CHKBOXES_EMPTY);
	request.getSession().removeAttribute(USER_EXCEPTION_SUBMIT_NONE);
	request.getSession().removeAttribute(USER_EXCEPTION_NUMBERFORMAT);
	request.getSession().removeAttribute(USER_EXCEPTION_FILENAME_EMPTY);
	request.getSession().removeAttribute(USER_EXCEPTION_WEIGHT_MUST_EQUAL100);
	request.getSession().removeAttribute(USER_EXCEPTION_SINGLE_OPTION);
    }

    public static void setFormProperties(HttpServletRequest request, IVoteService voteService,
	    VoteAuthoringForm voteAuthoringForm, VoteGeneralAuthoringDTO voteGeneralAuthoringDTO,
	    String strToolContentID, String defaultContentIdStr, String activeModule, SessionMap sessionMap,
	    String httpSessionID) {

	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	voteAuthoringForm.setToolContentID(strToolContentID);

	if ((defaultContentIdStr != null) && (defaultContentIdStr.length() > 0))
	    voteAuthoringForm.setDefaultContentIdStr(new Long(defaultContentIdStr).toString());

	voteAuthoringForm.setActiveModule(activeModule);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);

	String lockOnFinish = request.getParameter("lockOnFinish");
	voteAuthoringForm.setLockOnFinish(lockOnFinish);
	voteGeneralAuthoringDTO.setLockOnFinish(lockOnFinish);
	
	String useSelectLeaderToolOuput = request.getParameter("useSelectLeaderToolOuput");
	voteAuthoringForm.setUseSelectLeaderToolOuput(useSelectLeaderToolOuput);;
	voteGeneralAuthoringDTO.setAllowText(useSelectLeaderToolOuput);

	String allowText = request.getParameter("allowText");
	voteAuthoringForm.setAllowText(allowText);
	voteGeneralAuthoringDTO.setAllowText(allowText);

	String showResults = request.getParameter("showResults");
	voteAuthoringForm.setShowResults(showResults);
	voteGeneralAuthoringDTO.setShowResults(showResults);

	String maxNominationCount = request.getParameter("maxNominationCount");
	voteAuthoringForm.setMaxNominationCount(maxNominationCount);
	voteGeneralAuthoringDTO.setMaxNominationCount(maxNominationCount);

	String reflect = request.getParameter("reflect");
	voteAuthoringForm.setReflect(reflect);
	voteGeneralAuthoringDTO.setReflect(reflect);

	String reflectionSubject = request.getParameter("reflectionSubject");
	voteAuthoringForm.setReflectionSubject(reflectionSubject);
	voteGeneralAuthoringDTO.setReflectionSubject(reflectionSubject);

	String offlineInstructions = request.getParameter(OFFLINE_INSTRUCTIONS);
	voteAuthoringForm.setOfflineInstructions(offlineInstructions);
	voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	String onlineInstructions = request.getParameter(ONLINE_INSTRUCTIONS);
	voteAuthoringForm.setOnlineInstructions(onlineInstructions);
	voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
    }

    public static void setDefineLater(HttpServletRequest request, boolean value, String strToolContentID,
	    IVoteService voteService) {

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));

	if (voteContent != null) {
	    voteContent.setDefineLater(value);
	    voteService.updateVote(voteContent);
	}
    }

    /**
     * If this file exists in attachments map, move it to the deleted attachments map. Returns the updated
     * deletedAttachments map, creating a new one if needed. If uuid supplied then tries to match on that, otherwise
     * uses filename and isOnline.
     */
    public static List moveToDelete(String uuid, List attachmentsList, List deletedAttachmentsList) {

	List deletedList = deletedAttachmentsList != null ? deletedAttachmentsList : new ArrayList();

	if (attachmentsList != null) {
	    Iterator iter = attachmentsList.iterator();
	    VoteUploadedFile attachment = null;
	    while (iter.hasNext() && attachment == null) {
		VoteUploadedFile value = (VoteUploadedFile) iter.next();

		if (uuid.equals(value.getUuid())) {
		    attachment = value;
		}

	    }
	    if (attachment != null) {
		deletedList.add(attachment);
		attachmentsList.remove(attachment);
	    }
	}

	return deletedList;
    }

    public static List moveToDelete(String filename, boolean isOnline, List attachmentsList, List deletedAttachmentsList) {

	List deletedList = deletedAttachmentsList != null ? deletedAttachmentsList : new ArrayList();

	if (attachmentsList != null) {
	    Iterator iter = attachmentsList.iterator();
	    VoteUploadedFile attachment = null;
	    while (iter.hasNext() && attachment == null) {
		VoteUploadedFile value = (VoteUploadedFile) iter.next();
		if (filename.equals(value.getFileName()) && isOnline == value.isFileOnline()) {
		    attachment = value;
		}

	    }
	    if (attachment != null) {
		deletedList.add(attachment);
		attachmentsList.remove(attachment);
	    }
	}

	return deletedList;
    }

}
