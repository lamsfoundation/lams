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

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.DataFlowObject;
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
    static Logger logger = Logger.getLogger(LearningUtil.class.getName());

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
	if (Boolean.TRUE.equals(voteContent.getAssignedDataFlowObject())
		&& !Boolean.TRUE.equals(voteContent.getDataFlowObjectUsed())) {
	    // If we are using tool input, we need to get it now and
	    // create questions. Once they are created, they will be not altered, no matter if another learner gets to
	    // this point and the tool input changed
	    createQuestionsFromToolInput(voteContent, voteService);
	    nominations = voteContent.getVoteQueContents();
	    voteContent.setDataFlowObjectUsed(true);
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

    /**
     * determines if a user is already in the tool's tables
     * 
     * @param request
     * @return
     */
    public static boolean doesUserExists(HttpServletRequest request, IVoteService voteService) {
	Long queUsrId = VoteUtils.getUserId();
	VoteQueUsr voteQueUsr = voteService.retrieveVoteQueUsr(queUsrId);

	if (voteQueUsr != null) {
	    return true;
	}

	return false;
    }

    /**
     * creates a new user for the tool
     * 
     * @param request
     * @return
     */
    public static VoteQueUsr createUser(HttpServletRequest request, IVoteService voteService, Long toolSessionID) {
	LearningUtil.logger.debug("createUser: " + toolSessionID);

	Long queUsrId = VoteUtils.getUserId();
	String username = VoteUtils.getUserName();
	String fullname = VoteUtils.getUserFullName();
	VoteSession voteSession = voteService.retrieveVoteSession(toolSessionID);
	VoteQueUsr voteQueUsr = new VoteQueUsr(queUsrId, username, fullname, voteSession, new TreeSet());
	voteService.createVoteQueUsr(voteQueUsr);
	LearningUtil.logger.debug("created voteQueUsr in the db: " + voteQueUsr);
	return voteQueUsr;
    }

    public static VoteQueUsr getUser(HttpServletRequest request, IVoteService voteService) {
	Long queUsrId = VoteUtils.getUserId();
	VoteQueUsr voteQueUsr = voteService.retrieveVoteQueUsr(queUsrId);
	return voteQueUsr;
    }

    /**
     * creates a new vote record in the database
     * 
     * @param request
     * @param voteQueUsr
     * @param mapGeneralCheckedOptionsContent
     * @param userEntry
     * @param singleUserEntry
     * @param voteSession
     */
    public static void createAttempt(HttpServletRequest request, IVoteService voteService, VoteQueUsr voteQueUsr,
	    Map mapGeneralCheckedOptionsContent, String userEntry, boolean singleUserEntry, VoteSession voteSession,
	    Long toolContentUID) {
	LearningUtil.logger.debug("doing voteSession: " + voteSession);
	LearningUtil.logger.debug("doing createAttempt: " + mapGeneralCheckedOptionsContent);
	LearningUtil.logger.debug("userEntry: " + userEntry);
	LearningUtil.logger.debug("singleUserEntry: " + singleUserEntry);

	Date attempTime = VoteUtils.getGMTDateTime();
	String timeZone = VoteUtils.getCurrentTimeZone();
	LearningUtil.logger.debug("timeZone: " + timeZone);

	LearningUtil.logger.debug("toolContentUID: " + toolContentUID);

	if (mapGeneralCheckedOptionsContent.size() == 0) {
	    LearningUtil.logger.debug("mapGeneralCheckedOptionsContent is empty");
	    VoteQueContent localVoteQueContent = voteService.getToolDefaultQuestionContent(1);
	    LearningUtil.logger.debug("localVoteQueContent: " + localVoteQueContent);
	    createIndividualOptions(request, voteService, localVoteQueContent, voteQueUsr, attempTime, timeZone,
		    userEntry, singleUserEntry, voteSession);

	} else {
	    LearningUtil.logger.debug("mapGeneralCheckedOptionsContent is not empty");
	    if (toolContentUID != null) {
		Iterator itCheckedMap = mapGeneralCheckedOptionsContent.entrySet().iterator();
		LearningUtil.logger.debug("iterating mapGeneralCheckedOptionsContent");
		while (itCheckedMap.hasNext()) {
		    Map.Entry checkedPairs = (Map.Entry) itCheckedMap.next();
		    Long questionDisplayOrder = new Long(checkedPairs.getKey().toString());

		    LearningUtil.logger.debug("questionDisplayOrder: " + questionDisplayOrder);

		    VoteQueContent voteQueContent = voteService.getQuestionContentByDisplayOrder(questionDisplayOrder,
			    toolContentUID);
		    LearningUtil.logger.debug("voteQueContent: " + voteQueContent);
		    if (voteQueContent != null) {
			createIndividualOptions(request, voteService, voteQueContent, voteQueUsr, attempTime, timeZone,
				userEntry, false, voteSession);
		    }
		}
	    }
	}

    }

    public static void createIndividualOptions(HttpServletRequest request, IVoteService voteService,
	    VoteQueContent voteQueContent, VoteQueUsr voteQueUsr, Date attempTime, String timeZone, String userEntry,
	    boolean singleUserEntry, VoteSession voteSession) {
	LearningUtil.logger.debug("doing voteSession: " + voteSession);
	LearningUtil.logger.debug("userEntry: " + userEntry);
	LearningUtil.logger.debug("singleUserEntry: " + singleUserEntry);

	LearningUtil.logger.debug("voteQueContent: " + voteQueContent);
	LearningUtil.logger.debug("user " + voteQueUsr.getQueUsrId());
	LearningUtil.logger.debug("voteQueContent.getVoteContentId() " + voteQueContent.getVoteContentId());

	if (voteQueContent != null) {
	    VoteUsrAttempt existingVoteUsrAttempt = voteService.getAttemptsForUserAndQuestionContentAndSession(
		    voteQueUsr.getQueUsrId(), voteQueContent.getVoteContentId(), voteSession.getUid());
	    LearningUtil.logger.debug("existingVoteUsrAttempt: " + existingVoteUsrAttempt);

	    if (existingVoteUsrAttempt != null) {
		LearningUtil.logger.debug("update existingVoteUsrAttempt: " + existingVoteUsrAttempt);
		existingVoteUsrAttempt.setUserEntry(userEntry);
		existingVoteUsrAttempt.setAttemptTime(attempTime);
		existingVoteUsrAttempt.setTimeZone(timeZone);
		voteService.updateVoteUsrAttempt(existingVoteUsrAttempt);
		LearningUtil.logger.debug("done updating existingVoteUsrAttempt: " + existingVoteUsrAttempt);
	    } else {
		LearningUtil.logger.debug("create new attempt");
		VoteUsrAttempt voteUsrAttempt = new VoteUsrAttempt(attempTime, timeZone, voteQueContent, voteQueUsr,
			userEntry, singleUserEntry, true);
		LearningUtil.logger.debug("voteUsrAttempt: " + voteUsrAttempt);
		voteService.createVoteUsrAttempt(voteUsrAttempt);
		LearningUtil.logger.debug("created voteUsrAttempt in the db :" + voteUsrAttempt);
	    }
	}
    }

    public static void readParameters(HttpServletRequest request, VoteLearningForm voteLearningForm) {
	String optionCheckBoxSelected = request.getParameter("optionCheckBoxSelected");
	LearningUtil.logger.debug("parameter optionCheckBoxSelected: " + optionCheckBoxSelected);
	if (optionCheckBoxSelected != null && optionCheckBoxSelected.equals("1")) {
	    LearningUtil.logger.debug("parameter optionCheckBoxSelected is selected " + optionCheckBoxSelected);
	    voteLearningForm.setOptionCheckBoxSelected("1");
	}

	String questionIndex = request.getParameter("questionIndex");
	LearningUtil.logger.debug("parameter questionIndex: " + questionIndex);
	if (questionIndex != null) {
	    LearningUtil.logger.debug("parameter questionIndex is selected " + questionIndex);
	    voteLearningForm.setQuestionIndex(questionIndex);
	}

	String optionIndex = request.getParameter("optionIndex");
	LearningUtil.logger.debug("parameter optionIndex: " + optionIndex);
	if (optionIndex != null) {
	    LearningUtil.logger.debug("parameter optionIndex is selected " + optionIndex);
	    voteLearningForm.setOptionIndex(optionIndex);
	}

	String optionValue = request.getParameter("optionValue");
	LearningUtil.logger.debug("parameter optionValue: " + optionValue);
	if (optionValue != null) {
	    voteLearningForm.setOptionValue(optionValue);
	}

	String checked = request.getParameter("checked");
	LearningUtil.logger.debug("parameter checked: " + checked);
	if (checked != null) {
	    LearningUtil.logger.debug("parameter checked is selected " + checked);
	    voteLearningForm.setChecked(checked);
	}
    }

    public static Map selectOptionsCheckBox(HttpServletRequest request, VoteLearningForm voteLearningForm,
	    String questionIndex, Map mapGeneralCheckedOptionsContent, Map mapQuestionContentLearner) {
	LearningUtil.logger.debug("requested optionCheckBoxSelected...");
	LearningUtil.logger.debug("questionIndex: " + voteLearningForm.getQuestionIndex());
	LearningUtil.logger.debug("optionValue: " + voteLearningForm.getOptionValue());
	LearningUtil.logger.debug("checked: " + voteLearningForm.getChecked());
	LearningUtil.logger.debug("mapQuestionContentLearner: " + mapQuestionContentLearner);

	String selectedNomination = (String) mapQuestionContentLearner.get(voteLearningForm.getQuestionIndex());
	LearningUtil.logger.debug("selectedNomination: " + selectedNomination);

	Map mapFinal = new TreeMap(new VoteComparator());

	if (mapGeneralCheckedOptionsContent.size() == 0) {
	    LearningUtil.logger.debug("mapGeneralCheckedOptionsContent size is 0");
	    Map mapLeanerCheckedOptionsContent = new TreeMap(new VoteComparator());

	    if (voteLearningForm.getChecked().equals("true")) {
		mapLeanerCheckedOptionsContent.put(voteLearningForm.getQuestionIndex(), selectedNomination);
	    } else {
		mapLeanerCheckedOptionsContent.remove(voteLearningForm.getQuestionIndex());
	    }

	    mapFinal = mapLeanerCheckedOptionsContent;
	} else {
	    Map mapCurrentOptions = mapGeneralCheckedOptionsContent;

	    LearningUtil.logger.debug("mapCurrentOptions: " + mapCurrentOptions);
	    if (mapCurrentOptions != null) {
		if (voteLearningForm.getChecked().equals("true")) {
		    mapCurrentOptions.put(voteLearningForm.getQuestionIndex(), selectedNomination);
		} else {
		    mapCurrentOptions.remove(voteLearningForm.getQuestionIndex());
		}

		LearningUtil.logger.debug("updated mapCurrentOptions: " + mapCurrentOptions);

		mapFinal = mapCurrentOptions;
	    } else {
		LearningUtil.logger.debug("no options for this questions has been selected yet");
		Map mapLeanerCheckedOptionsContent = new TreeMap(new VoteComparator());

		if (voteLearningForm.getChecked().equals("true")) {
		    mapLeanerCheckedOptionsContent.put(voteLearningForm.getQuestionIndex(), selectedNomination);
		} else {
		    mapLeanerCheckedOptionsContent.remove(voteLearningForm.getOptionIndex());
		}

		mapFinal = mapLeanerCheckedOptionsContent;
	    }
	}

	LearningUtil.logger.debug("mapFinal: " + mapFinal);
	return mapFinal;
    }

    private static void createQuestionsFromToolInput(VoteContent voteContent, IVoteService voteService) {
	// We get whatever the source tool provides us with and try to create questions out of it
	DataFlowObject dataFlowObject = voteService.getAssignedDataFlowObject(voteContent.getVoteContentId());
	ToolOutput toolInput = voteService.getToolInput(voteContent.getVoteContentId(), VoteUtils.getUserId()
		.intValue());
	Object value = toolInput.getValue().getComplex();
	// The input is an array (users) of arrays of strings (their answers)
	if (value instanceof String[][]) {
	    if (value != null) {
		String[][] usersAndAnswers = (String[][]) value;
		int nominationIndex = voteContent.getVoteQueContents().size() + 1;
		Short maxInputs = voteContent.getMaxInputs();
		short inputCount = 0;
		for (String[] userAnswers : usersAndAnswers) {
		    if (userAnswers != null) {
			if (maxInputs != null && maxInputs > 0 && inputCount >= maxInputs) {
			    // if we reached the maximum number of inputs, i.e. number of students that will be taken
			    // into
			    // account
			    break;
			}
			boolean anyAnswersAdded = false;
			for (String questionText : userAnswers) {
			    if (questionText != null) {
				VoteQueContent nomination = new VoteQueContent();
				nomination.setDisplayOrder(nominationIndex);
				nomination.setMcContent(voteContent);
				nomination.setQuestion(questionText);
				voteService.saveOrUpdateVoteQueContent(nomination);
				voteContent.getVoteQueContents().add(nomination);
				nominationIndex++;
				anyAnswersAdded = true;
			    }
			}
			if (anyAnswersAdded) {
			    inputCount++;
			}
		    }
		}
	    }
	} else if (value instanceof String[]) {
	    // the input is a list of strings (questions, for example)
	    int nominationIndex = voteContent.getVoteQueContents().size() + 1;
	    String[] userAnswers = (String[]) value;
	    for (String questionText : userAnswers) {
		VoteQueContent nomination = new VoteQueContent();
		nomination.setDisplayOrder(nominationIndex);
		nomination.setMcContent(voteContent);
		nomination.setQuestion(questionText);
		voteService.saveOrUpdateVoteQueContent(nomination);
		voteContent.getVoteQueContents().add(nomination);
		nominationIndex++;
	    }

	}
    }

}