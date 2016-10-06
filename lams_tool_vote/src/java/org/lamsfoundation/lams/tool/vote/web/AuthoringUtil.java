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

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dto.VoteQuestionDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.util.VoteComparator;
import org.lamsfoundation.lams.tool.vote.web.form.VoteAuthoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Keeps all operations needed for Authoring mode.
 *
 * @author Ozgur Demirtas
 */
public class AuthoringUtil implements VoteAppConstants {

    private static Logger logger = Logger.getLogger(AuthoringUtil.class.getName());

    protected static List<VoteQuestionDTO> swapQuestions(List<VoteQuestionDTO> questionDTOs, String questionIndex,
	    String direction) {

	int intQuestionIndex = new Integer(questionIndex).intValue();
	int intOriginalQuestionIndex = intQuestionIndex;

	int replacedQuestionIndex = 0;
	if (direction.equals("down")) {
	    replacedQuestionIndex = ++intQuestionIndex;
	} else {
	    replacedQuestionIndex = --intQuestionIndex;
	}

	VoteQuestionDTO mainQuestion = AuthoringUtil.getQuestionAtDisplayOrder(questionDTOs, intOriginalQuestionIndex);

	VoteQuestionDTO replacedQuestion = AuthoringUtil.getQuestionAtDisplayOrder(questionDTOs, replacedQuestionIndex);

	List<VoteQuestionDTO> newQuestionDtos = new LinkedList<VoteQuestionDTO>();

	Iterator<VoteQuestionDTO> iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    VoteQuestionDTO questionDTO = iter.next();
	    VoteQuestionDTO tempQuestion = new VoteQuestionDTO();

	    if (!questionDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())
		    && !questionDTO.getDisplayOrder().equals(new Integer(replacedQuestionIndex).toString())) {
		AuthoringUtil.logger.info("Normal Copy");
		// normal copy
		tempQuestion = questionDTO;

	    } else if (questionDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())) {
		// move type 1
		AuthoringUtil.logger.info("Move type 1");
		tempQuestion = replacedQuestion;

	    } else if (questionDTO.getDisplayOrder().equals(new Integer(replacedQuestionIndex).toString())) {
		// move type 1
		AuthoringUtil.logger.info("Move type 1");
		tempQuestion = mainQuestion;
	    }

	    newQuestionDtos.add(tempQuestion);
	}

	return newQuestionDtos;
    }

    protected static VoteQuestionDTO getQuestionAtDisplayOrder(List<VoteQuestionDTO> questionDTOs,
	    int intOriginalQuestionIndex) {

	Iterator<VoteQuestionDTO> iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    VoteQuestionDTO voteQuestionDTO = iter.next();

	    if (new Integer(intOriginalQuestionIndex).toString().equals(voteQuestionDTO.getDisplayOrder())) {
		return voteQuestionDTO;
	    }
	}
	return null;
    }

    protected static List<VoteQuestionDTO> reorderQuestionDTOs(List<VoteQuestionDTO> listQuestionDTO) {
	List<VoteQuestionDTO> listFinalQuestionDTO = new LinkedList<VoteQuestionDTO>();

	int queIndex = 0;
	Iterator<VoteQuestionDTO> iter = listQuestionDTO.iterator();
	while (iter.hasNext()) {
	    VoteQuestionDTO voteQuestionDTO = iter.next();

	    String question = voteQuestionDTO.getNomination();

	    String displayOrder = voteQuestionDTO.getDisplayOrder();

	    if (question != null && !question.equals("")) {
		++queIndex;

		voteQuestionDTO.setNomination(question);
		voteQuestionDTO.setDisplayOrder(new Integer(queIndex).toString());

		listFinalQuestionDTO.add(voteQuestionDTO);
	    }
	}

	return listFinalQuestionDTO;
    }

    public static boolean checkDuplicateNominations(List listQuestionDTO, String newQuestion) {
	if (AuthoringUtil.logger.isDebugEnabled()) {
	    AuthoringUtil.logger.debug("New Question" + newQuestion);
	}

	Map mapQuestion = AuthoringUtil.extractMapQuestion(listQuestionDTO);

	Iterator itMap = mapQuestion.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    if (pairs.getValue() != null && !pairs.getValue().equals("")) {

		if (pairs.getValue().equals(newQuestion)) {
		    return true;
		}
	    }
	}
	return false;
    }

    protected static Map extractMapQuestion(List listQuestionDTO) {
	Map mapQuestion = new TreeMap(new VoteComparator());

	Iterator iter = listQuestionDTO.iterator();
	int queIndex = 0;
	while (iter.hasNext()) {
	    VoteQuestionDTO voteQuestionDTO = (VoteQuestionDTO) iter.next();

	    queIndex++;
	    mapQuestion.put(new Integer(queIndex).toString(), voteQuestionDTO.getNomination());
	}
	return mapQuestion;
    }

    protected static List reorderUpdateListQuestionDTO(List listQuestionDTO, VoteQuestionDTO voteQuestionDTONew,
	    String editableQuestionIndex) {

	List listFinalQuestionDTO = new LinkedList();

	int queIndex = 0;
	Iterator iter = listQuestionDTO.iterator();
	while (iter.hasNext()) {
	    VoteQuestionDTO voteQuestionDTO = (VoteQuestionDTO) iter.next();

	    ++queIndex;
	    String question = voteQuestionDTO.getNomination();

	    String displayOrder = voteQuestionDTO.getDisplayOrder();

	    if (displayOrder.equals(editableQuestionIndex)) {
		voteQuestionDTO.setNomination(voteQuestionDTONew.getNomination());
		voteQuestionDTO.setDisplayOrder(voteQuestionDTONew.getDisplayOrder());

		listFinalQuestionDTO.add(voteQuestionDTO);
	    } else {
		voteQuestionDTO.setNomination(question);
		voteQuestionDTO.setDisplayOrder(displayOrder);

		listFinalQuestionDTO.add(voteQuestionDTO);
	    }
	}

	return listFinalQuestionDTO;
    }

    public static VoteContent saveOrUpdateVoteContent(IVoteService voteService, VoteAuthoringForm voteAuthoringForm,
	    HttpServletRequest request, VoteContent voteContent, String strToolContentID) {
	if (AuthoringUtil.logger.isDebugEnabled()) {
	    AuthoringUtil.logger.debug("ToolContentID" + strToolContentID);
	}
	UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	String lockOnFinish = request.getParameter("lockOnFinish");

	String allowTextEntry = request.getParameter("allowText");

	String showResults = request.getParameter("showResults");

	String maxInputs = request.getParameter("maxInputs");

	String useSelectLeaderToolOuput = request.getParameter("useSelectLeaderToolOuput");

	String reflect = request.getParameter(VoteAppConstants.REFLECT);

	String reflectionSubject = voteAuthoringForm.getReflectionSubject();

	String maxNomcount = voteAuthoringForm.getMaxNominationCount();

	String minNomcount = voteAuthoringForm.getMinNominationCount();

	boolean lockOnFinishBoolean = false;
	boolean allowTextEntryBoolean = false;
	boolean useSelectLeaderToolOuputBoolean = false;
	boolean reflectBoolean = false;
	boolean showResultsBoolean = false;
	short maxInputsShort = 0;

	if (lockOnFinish != null && lockOnFinish.equalsIgnoreCase("1")) {
	    lockOnFinishBoolean = true;
	}

	if (allowTextEntry != null && allowTextEntry.equalsIgnoreCase("1")) {
	    allowTextEntryBoolean = true;
	}

	if (useSelectLeaderToolOuput != null && useSelectLeaderToolOuput.equalsIgnoreCase("1")) {
	    useSelectLeaderToolOuputBoolean = true;
	}

	if (reflect != null && reflect.equalsIgnoreCase("1")) {
	    reflectBoolean = true;
	}

	if (showResults != null && showResults.equalsIgnoreCase("1")) {
	    showResultsBoolean = true;
	}

	if (maxInputs != null && !"0".equals(maxInputs)) {
	    maxInputsShort = Short.parseShort(maxInputs);
	}

	long userId = 0;
	if (toolUser != null) {
	    userId = toolUser.getUserID().longValue();
	} else {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null) {
		userId = user.getUserID().longValue();
	    } else {
		userId = 0;
	    }
	}

	boolean newContent = false;
	if (voteContent == null) {
	    voteContent = new VoteContent();
	    newContent = true;
	}

	voteContent.setVoteContentId(new Long(strToolContentID));
	voteContent.setTitle(richTextTitle);
	voteContent.setInstructions(richTextInstructions);
	voteContent.setUpdateDate(new Date(System.currentTimeMillis()));
	/** keep updating this one */
	voteContent.setCreatedBy(userId);
	/** make sure we are setting the userId from the User object above */

	voteContent.setLockOnFinish(lockOnFinishBoolean);
	voteContent.setAllowText(allowTextEntryBoolean);
	voteContent.setShowResults(showResultsBoolean);
	voteContent.setUseSelectLeaderToolOuput(useSelectLeaderToolOuputBoolean);
	voteContent.setReflect(reflectBoolean);
	voteContent.setMaxNominationCount(maxNomcount);
	voteContent.setMinNominationCount(minNomcount);

	voteContent.setReflectionSubject(reflectionSubject);

	voteContent.setMaxExternalInputs(maxInputsShort);

	if (newContent) {
	    AuthoringUtil.logger.info("In New Content");
	    voteService.saveVoteContent(voteContent);
	} else {
	    voteService.updateVote(voteContent);
	}

	voteContent = voteService.getVoteContent(new Long(strToolContentID));

	return voteContent;
    }

}
