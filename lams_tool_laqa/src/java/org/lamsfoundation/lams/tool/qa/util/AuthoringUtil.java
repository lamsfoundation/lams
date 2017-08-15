/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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


package org.lamsfoundation.lams.tool.qa.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;

/**
 * Keeps all operations needed for Authoring mode.
 *
 * @author Ozgur Demirtas
 */
public class AuthoringUtil implements QaAppConstants {

    public static List<QaQuestionDTO> reorderUpdateQuestionDTOs(List<QaQuestionDTO> questionDTOs,
	    QaQuestionDTO qaQuestionContentDTONew, String editableQuestionIndex) {

	List<QaQuestionDTO> listFinalQuestionDTO = new LinkedList<QaQuestionDTO>();

	int queIndex = 0;
	Iterator iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    QaQuestionDTO qaQuestionDTO = (QaQuestionDTO) iter.next();

	    ++queIndex;

	    String question = qaQuestionDTO.getQuestion();
	    String displayOrder = qaQuestionDTO.getDisplayOrder();
	    String feedback = qaQuestionDTO.getFeedback();
	    boolean required = qaQuestionDTO.isRequired();
	    int minWordsLimit = qaQuestionDTO.getMinWordsLimit();

	    if (displayOrder.equals(editableQuestionIndex)) {
		qaQuestionDTO.setQuestion(qaQuestionContentDTONew.getQuestion());
		qaQuestionDTO.setDisplayOrder(qaQuestionContentDTONew.getDisplayOrder());
		qaQuestionDTO.setFeedback(qaQuestionContentDTONew.getFeedback());
		qaQuestionDTO.setRequired(qaQuestionContentDTONew.isRequired());
		qaQuestionDTO.setMinWordsLimit(qaQuestionContentDTONew.getMinWordsLimit());

		listFinalQuestionDTO.add(qaQuestionDTO);
	    } else {
		qaQuestionDTO.setQuestion(question);
		qaQuestionDTO.setDisplayOrder(displayOrder);
		qaQuestionDTO.setFeedback(feedback);
		qaQuestionDTO.setRequired(required);
		qaQuestionDTO.setMinWordsLimit(minWordsLimit);

		listFinalQuestionDTO.add(qaQuestionDTO);

	    }
	}
	return listFinalQuestionDTO;
    }

    public static boolean checkDuplicateQuestions(List<QaQuestionDTO> questionDTOs, String newQuestion) {
	for (QaQuestionDTO questionDTO : questionDTOs) {
	    if (questionDTO.getQuestion() != null && questionDTO.getQuestion().equals(newQuestion)) {
		return true;
	    }
	}
	return false;
    }
}