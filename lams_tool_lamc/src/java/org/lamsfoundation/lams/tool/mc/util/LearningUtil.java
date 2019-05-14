/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.util;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.dto.AnswerDTO;
import org.lamsfoundation.lams.tool.mc.dto.McGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.mc.model.McContent;
import org.lamsfoundation.lams.tool.mc.web.form.McLearningForm;

/**
 *
 * Keeps all operations needed for Authoring mode.
 *
 * @author Ozgur Demirtas
 */
public class LearningUtil {
    static Logger logger = Logger.getLogger(LearningUtil.class.getName());

    public static void saveFormRequestData(HttpServletRequest request, McLearningForm mcLearningForm) {

	String httpSessionID = request.getParameter("httpSessionID");
	mcLearningForm.setHttpSessionID(httpSessionID);

	String passMarkApplicable = request.getParameter("passMarkApplicable");
	mcLearningForm.setPassMarkApplicable(passMarkApplicable);

	String userOverPassMark = request.getParameter("userOverPassMark");
	mcLearningForm.setUserOverPassMark(userOverPassMark);
    }

    /**
     * buildMcGeneralLearnerFlowDTO
     */
    public static McGeneralLearnerFlowDTO buildMcGeneralLearnerFlowDTO(McContent mcContent) {
	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = new McGeneralLearnerFlowDTO();
	mcGeneralLearnerFlowDTO.setRetries(new Boolean(mcContent.isRetries()).toString());
	mcGeneralLearnerFlowDTO.setActivityTitle(mcContent.getTitle());
	mcGeneralLearnerFlowDTO.setActivityInstructions(mcContent.getInstructions());
	mcGeneralLearnerFlowDTO.setPassMark(mcContent.getPassMark());
	mcGeneralLearnerFlowDTO.setReportTitleLearner("Report");

	mcGeneralLearnerFlowDTO.setTotalQuestionCount(new Integer(mcContent.getMcQueContents().size()));
	return mcGeneralLearnerFlowDTO;
    }

    /**
     * Should we show the marks for each questionDescription - we show the marks if any of the questions have a mark > 1.
     */
    public static Boolean isShowMarksOnQuestion(List<AnswerDTO> listQuestionAndCandidateAnswersDTO) {
	Iterator iter = listQuestionAndCandidateAnswersDTO.iterator();
	while (iter.hasNext()) {
	    AnswerDTO elem = (AnswerDTO) iter.next();
	    if (elem.getMark().intValue() > 1) {
		return Boolean.TRUE;
	    }
	}
	return Boolean.FALSE;
    }

    public static String formatPrefixLetter(int index) {
	return new String(Character.toChars(97 + index)) + ")";
    }
}