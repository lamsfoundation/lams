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
package org.lamsfoundation.lams.tool.vote.web;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.learningdesign.DataFlowObject;
import org.lamsfoundation.lams.tool.SimpleURL;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * <p>
 * Keeps all operations needed for Learning mode.
 * </p>
 * 
 * @author Ozgur Demirtas
 * 
 */
public class LearningUtil implements VoteAppConstants {

    /**
     * Build a map of the display ids -> nomination text. If checkedOptions != null then only include the display ids in
     * the checkedOptions list.
     * 
     * @param request
     * @param voteContent
     *                the content of the vote from the database
     * @param checkedOptions
     *                collection of String display IDs to which to restrict the map (optional)
     * @return Map of display id -> nomination text.
     */
    public static Map buildQuestionContentMap(HttpServletRequest request, IVoteService voteService,
	    VoteContent voteContent, Collection<String> checkedOptions) {
	Map mapQuestionsContent = new TreeMap(new VoteComparator());
	Set<VoteQueContent> nominations = voteContent.getVoteQueContents();
	// should we add nominations from data flow from other activities?
	if (Boolean.TRUE.equals(voteContent.getAssignedDataFlowObject())
		&& (voteContent.getMaxExternalInputs() == null || voteContent.getExternalInputsAdded() == null || voteContent
			.getExternalInputsAdded() < voteContent.getMaxExternalInputs())) {
	    // If we are using tool input, we need to get it now and
	    // create questions. Once they are created, they will be not altered, no matter if another learner gets to
	    // this point and the tool input changed
	    createQuestionsFromToolInput(voteContent, voteService);
	    nominations = voteContent.getVoteQueContents();
	}

	Iterator<VoteQueContent> contentIterator = nominations.iterator();
	while (contentIterator.hasNext()) {
	    VoteQueContent voteQueContent = contentIterator.next();
	    if (voteQueContent != null) {
		String displayOrder = (new Integer(voteQueContent.getDisplayOrder())).toString();
		if ((checkedOptions == null || checkedOptions.contains(displayOrder)) && !displayOrder.equals("0")) {
		    /* add the question to the questions Map in the displayOrder */
		    mapQuestionsContent.put(displayOrder.toString(), voteQueContent.getQuestion());
		}
	    }
	}

	return mapQuestionsContent;
    }

    public static VoteQueUsr getUser(IVoteService voteService) {
	/* get back login user DTO */
	HttpSession ss = SessionManager.getSession();
	UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	long userId = toolUser.getUserID().longValue();
	VoteQueUsr voteQueUsr = voteService.retrieveVoteQueUsr(userId);
	return voteQueUsr;
    }

    /**
     * creates a new vote record in the database
     * 
     * @param request
     * @param voteQueUsr
     * @param mapGeneralCheckedOptionsContent
     * @param userEntry
     * @param voteSession
     */
    public static void createAttempt(IVoteService voteService, VoteQueUsr voteQueUsr,
	    Map mapGeneralCheckedOptionsContent, String userEntry, VoteSession voteSession, Long toolContentUID) {

	Date attempTime = VoteUtils.getGMTDateTime();
	String timeZone = VoteUtils.getCurrentTimeZone();

	if (mapGeneralCheckedOptionsContent.size() == 0) {
	    VoteQueContent localVoteQueContent = voteService.getToolDefaultQuestionContent(1);
	    createIndividualOptions(voteService, localVoteQueContent, voteQueUsr, attempTime, timeZone, userEntry,
		    voteSession);

	} else {
	    if (toolContentUID != null) {
		Iterator itCheckedMap = mapGeneralCheckedOptionsContent.entrySet().iterator();
		while (itCheckedMap.hasNext()) {
		    Map.Entry checkedPairs = (Map.Entry) itCheckedMap.next();
		    Long questionDisplayOrder = new Long(checkedPairs.getKey().toString());

		    VoteQueContent voteQueContent = voteService.getQuestionContentByDisplayOrder(questionDisplayOrder,
			    toolContentUID);
		    if (voteQueContent != null) {
			createIndividualOptions(voteService, voteQueContent, voteQueUsr, attempTime, timeZone,
				userEntry, voteSession);
		    }
		}
	    }
	}

    }

    public static void createIndividualOptions(IVoteService voteService, VoteQueContent voteQueContent,
	    VoteQueUsr voteQueUsr, Date attempTime, String timeZone, String userEntry, VoteSession voteSession) {

	if (voteQueContent != null) {
	    VoteUsrAttempt existingVoteUsrAttempt = voteService.getAttemptForUserAndQuestionContentAndSession(
		    voteQueUsr.getQueUsrId(), voteQueContent.getVoteContentId(), voteSession.getUid());

	    if (existingVoteUsrAttempt != null) {
		existingVoteUsrAttempt.setUserEntry(userEntry);
		existingVoteUsrAttempt.setAttemptTime(attempTime);
		existingVoteUsrAttempt.setTimeZone(timeZone);
		voteService.updateVoteUsrAttempt(existingVoteUsrAttempt);
	    } else {
		VoteUsrAttempt voteUsrAttempt = new VoteUsrAttempt(attempTime, timeZone, voteQueContent, voteQueUsr,
			userEntry, true);
		voteService.createVoteUsrAttempt(voteUsrAttempt);
	    }
	}
    }

    public static void readParameters(HttpServletRequest request, VoteLearningForm voteLearningForm) {
	String optionCheckBoxSelected = request.getParameter("optionCheckBoxSelected");
	if (optionCheckBoxSelected != null && optionCheckBoxSelected.equals("1")) {
	    voteLearningForm.setOptionCheckBoxSelected("1");
	}

	String questionIndex = request.getParameter("questionIndex");
	if (questionIndex != null) {
	    voteLearningForm.setQuestionIndex(questionIndex);
	}

	String optionIndex = request.getParameter("optionIndex");
	if (optionIndex != null) {
	    voteLearningForm.setOptionIndex(optionIndex);
	}

	String optionValue = request.getParameter("optionValue");
	if (optionValue != null) {
	    voteLearningForm.setOptionValue(optionValue);
	}

	String checked = request.getParameter("checked");
	if (checked != null) {
	    voteLearningForm.setChecked(checked);
	}
    }

    public static Map selectOptionsCheckBox(HttpServletRequest request, VoteLearningForm voteLearningForm,
	    String questionIndex, Map mapGeneralCheckedOptionsContent, Map mapQuestionContentLearner) {

	String selectedNomination = (String) mapQuestionContentLearner.get(voteLearningForm.getQuestionIndex());

	Map mapFinal = new TreeMap(new VoteComparator());

	if (mapGeneralCheckedOptionsContent.size() == 0) {
	    Map mapLeanerCheckedOptionsContent = new TreeMap(new VoteComparator());

	    if (voteLearningForm.getChecked().equals("true")) {
		mapLeanerCheckedOptionsContent.put(voteLearningForm.getQuestionIndex(), selectedNomination);
	    } else {
		mapLeanerCheckedOptionsContent.remove(voteLearningForm.getQuestionIndex());
	    }

	    mapFinal = mapLeanerCheckedOptionsContent;
	} else {
	    Map mapCurrentOptions = mapGeneralCheckedOptionsContent;

	    if (mapCurrentOptions != null) {
		if (voteLearningForm.getChecked().equals("true")) {
		    mapCurrentOptions.put(voteLearningForm.getQuestionIndex(), selectedNomination);
		} else {
		    mapCurrentOptions.remove(voteLearningForm.getQuestionIndex());
		}

		mapFinal = mapCurrentOptions;
	    } else {
		//no options for this questions has been selected yet
		Map mapLeanerCheckedOptionsContent = new TreeMap(new VoteComparator());

		if (voteLearningForm.getChecked().equals("true")) {
		    mapLeanerCheckedOptionsContent.put(voteLearningForm.getQuestionIndex(), selectedNomination);
		} else {
		    mapLeanerCheckedOptionsContent.remove(voteLearningForm.getOptionIndex());
		}

		mapFinal = mapLeanerCheckedOptionsContent;
	    }
	}

	return mapFinal;
    }

    private static void createQuestionsFromToolInput(VoteContent voteContent, IVoteService voteService) {
	
	/* get back login user DTO */
	HttpSession ss = SessionManager.getSession();
	UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	long userId = toolUser.getUserID().longValue();
	
	// We get whatever the source tool provides us with and try to create questions out of it
	ToolOutput toolInput = voteService.getToolInput(voteContent.getVoteContentId(), new Long(userId).intValue());

	Object value = toolInput.getValue().getComplex();
	short inputsAdded = voteContent.getExternalInputsAdded() == null ? 0 : voteContent.getExternalInputsAdded();
	Short maxInputs = voteContent.getMaxExternalInputs();
	Set<VoteQueContent> existingNominations = voteContent.getVoteQueContents();
	// The input is an array (users) of arrays of strings (their answers)
	if (value instanceof String[][]) {
	    if (value != null) {
		String[][] usersAndAnswers = (String[][]) value;
		int nominationIndex = voteContent.getVoteQueContents().size() + 1;
		for (String[] userAnswers : usersAndAnswers) {
		    if (userAnswers != null) {
			if (maxInputs != null && inputsAdded >= maxInputs) {
			    // if we reached the maximum number of inputs, i.e. number of students that will be taken
			    // into account
			    break;
			}
			boolean anyAnswersAdded = false;
			for (String questionText : userAnswers) {
			    if (!StringUtils.isBlank(questionText)) {
				VoteQueContent nomination = new VoteQueContent();
				nomination.setDisplayOrder(nominationIndex);
				nomination.setMcContent(voteContent);
				nomination.setQuestion(questionText);
				if (!nominationExists(nomination, existingNominations)) {
				    voteService.saveOrUpdateVoteQueContent(nomination);
				    voteContent.getVoteQueContents().add(nomination);
				    nominationIndex++;
				    anyAnswersAdded = true;
				}
			    }
			}
			if (anyAnswersAdded) {
			    inputsAdded++;
			}
		    }
		}
	    }
	} else if (value instanceof String[]) {
	    // the input is a list of strings (questions, for example)
	    int nominationIndex = voteContent.getVoteQueContents().size() + 1;
	    String[] userAnswers = (String[]) value;
	    for (String questionText : userAnswers) {
		if (maxInputs != null && inputsAdded >= maxInputs) {
		    // if we reached the maximum number of inputs, i.e. number of students that will be taken
		    // into account
		    break;
		}

		if (!StringUtils.isBlank(questionText)) {
		    VoteQueContent nomination = new VoteQueContent();
		    nomination.setDisplayOrder(nominationIndex);
		    nomination.setMcContent(voteContent);
		    nomination.setQuestion(questionText);
		    if (!nominationExists(nomination, existingNominations)) {
			voteService.saveOrUpdateVoteQueContent(nomination);
			voteContent.getVoteQueContents().add(nomination);
			nominationIndex++;
			inputsAdded++;
		    }
		}
	    }
	} else if (value instanceof String && !StringUtils.isBlank((String) value)) {
	    int nominationIndex = voteContent.getVoteQueContents().size() + 1;
	    VoteQueContent nomination = new VoteQueContent();
	    nomination.setDisplayOrder(nominationIndex);
	    nomination.setMcContent(voteContent);
	    nomination.setQuestion((String) value);
	    if (!nominationExists(nomination, existingNominations)) {
		voteService.saveOrUpdateVoteQueContent(nomination);
		voteContent.getVoteQueContents().add(nomination);
	    }
	}
	if (value instanceof SimpleURL[][]) {
	    if (value != null) {
		SimpleURL[][] usersAndUrls = (SimpleURL[][]) value;
		int nominationIndex = voteContent.getVoteQueContents().size() + 1;
		for (SimpleURL[] userUrls : usersAndUrls) {
		    if (userUrls != null) {
			if (maxInputs != null && inputsAdded >= maxInputs) {
			    // if we reached the maximum number of inputs, i.e. number of students that will be taken
			    // into account
			    break;
			}
			boolean anyAnswersAdded = false;
			for (SimpleURL url : userUrls) {
			    if (url != null) {
				VoteQueContent nomination = new VoteQueContent();
				nomination.setDisplayOrder(nominationIndex);
				nomination.setMcContent(voteContent);

				String link = "<a href=\"" + url.getUrl() + "\">" + url.getNameToDisplay() + "</a>";
				nomination.setQuestion(link);
				if (!nominationExists(nomination, existingNominations)) {
				    voteService.saveOrUpdateVoteQueContent(nomination);
				    voteContent.getVoteQueContents().add(nomination);
				    nominationIndex++;
				    anyAnswersAdded = true;
				}
			    }
			}
			if (anyAnswersAdded) {
			    inputsAdded++;
			}
		    }
		}
	    }
	}

	else if (value instanceof SimpleURL[]) {
	    // the input is a list of strings (questions, for example)
	    int nominationIndex = voteContent.getVoteQueContents().size() + 1;
	    SimpleURL[] userUrls = (SimpleURL[]) value;
	    for (SimpleURL url : userUrls) {
		if (maxInputs != null && inputsAdded >= maxInputs) {
		    // if we reached the maximum number of inputs, i.e. number of students that will be taken
		    // into account
		    break;
		}
		if (url != null) {
		    VoteQueContent nomination = new VoteQueContent();
		    nomination.setDisplayOrder(nominationIndex);
		    nomination.setMcContent(voteContent);

		    String link = "<a href=\"" + url.getUrl() + "\">" + url.getNameToDisplay() + "</a>";
		    nomination.setQuestion(link);
		    if (!nominationExists(nomination, existingNominations)) {
			voteService.saveOrUpdateVoteQueContent(nomination);
			voteContent.getVoteQueContents().add(nomination);
			nominationIndex++;
			inputsAdded++;
		    }
		}
	    }
	} else if (value instanceof SimpleURL) {
	    int nominationIndex = voteContent.getVoteQueContents().size() + 1;
	    VoteQueContent nomination = new VoteQueContent();
	    nomination.setDisplayOrder(nominationIndex);

	    SimpleURL url = (SimpleURL) value;
	    String link = "<a href=\"" + url.getUrl() + "\">" + url.getNameToDisplay() + "</a>";
	    nomination.setQuestion(link);
	    if (!nominationExists(nomination, existingNominations)) {
		nomination.setMcContent(voteContent);
		voteService.saveOrUpdateVoteQueContent(nomination);
		voteContent.getVoteQueContents().add(nomination);
	    }
	}

	voteContent.setExternalInputsAdded(inputsAdded);
	voteService.saveVoteContent(voteContent);
    }

    private static boolean nominationExists(VoteQueContent nomination, Set<VoteQueContent> existingNominations) {
	if (existingNominations != null && nomination != null) {
	    for (VoteQueContent existingNomination : existingNominations) {
		if (existingNomination.getQuestion() != null
			&& existingNomination.getQuestion().equals(nomination.getQuestion())) {
		    return true;
		}
	    }
	}
	return false;
    }
}