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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.assessment.util;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentResultDTO;
import org.lamsfoundation.lams.tool.assessment.dto.OptionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionSummary;
import org.lamsfoundation.lams.tool.assessment.dto.UserSummary;
import org.lamsfoundation.lams.tool.assessment.dto.UserSummaryItem;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOptionAnswer;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;

public class AssessmentEscapeUtils {

    /**
     * Escapes all characters that may brake JS code on assigning Java value to JS String variable (particularly escapes
     * all quotes in the following way \").
     */
    public static void escapeQuotes(UserSummary userSummary) {
	for (UserSummaryItem userSummaryItem : userSummary.getUserSummaryItems()) {
	    for (AssessmentQuestionResult questionResult : userSummaryItem.getQuestionResults()) {
		AssessmentEscapeUtils.escapeQuotesInQuestionResult(questionResult);
	    }
	}
    }
    
    /**
     * Escapes all characters that may brake JS code on assigning Java value to JS String variable (particularly escapes
     * all quotes in the following way \").
     */
    public static void escapeQuotes(QuestionSummary questionSummary) {
	for (List<AssessmentQuestionResult> sessionQuestionResults : questionSummary.getQuestionResultsPerSession()) {
	    for (AssessmentQuestionResult questionResult : sessionQuestionResults) {
		AssessmentEscapeUtils.escapeQuotesInQuestionResult(questionResult);
	    }
	}
    }
    
    /**
     * Escapes all characters that may brake JS code on assigning Java value to JS String variable (particularly escapes
     * all quotes in the following way \").
     */
    public static void escapeQuotes(AssessmentResultDTO assessmentResultDto) {
	for (AssessmentQuestionResult questionResult : assessmentResultDto.getQuestionResults()) {
	    AssessmentEscapeUtils.escapeQuotesInQuestionResult(questionResult);
	}
    }

    private static void escapeQuotesInQuestionResult(AssessmentQuestionResult questionResult) {
	String answerString = questionResult.getAnswerString();
	if (answerString != null) {
	    String answerStringEscaped = StringEscapeUtils.escapeJavaScript(answerString);
	    questionResult.setAnswerStringEscaped(answerStringEscaped);
	}
	
	QuestionDTO questionDto = new QuestionDTO(questionResult.getQbToolQuestion());
	questionResult.setQuestionDto(questionDto);

	String title = questionDto.getTitle();
	if (title != null) {
	    String titleEscaped = StringEscapeUtils.escapeJavaScript(title);
	    questionDto.setTitleEscaped(titleEscaped);
	}

	for (OptionDTO optionDto : questionDto.getOptionDtos()) {
	    String matchingPair = optionDto.getMatchingPair();
	    if (matchingPair != null) {
		String matchingPairEscaped = StringEscapeUtils.escapeJavaScript(matchingPair);
		optionDto.setMatchingPairEscaped(matchingPairEscaped);
	    }

	    String name = optionDto.getName();
	    if (name != null) {
		String nameEscaped = StringEscapeUtils.escapeJavaScript(name);
		optionDto.setNameEscaped(nameEscaped);
	    }
	}
    }

    public static String printResponsesForJqgrid(AssessmentQuestionResult questionResult) {
	StringBuilder responseStr = new StringBuilder();
	final String DELIMITER = "<br>";

	if (questionResult != null) {

	    List<QbOption> options = questionResult.getQbQuestion().getQbOptions();
	    Set<AssessmentOptionAnswer> optionAnswers = questionResult.getOptionAnswers();

	    switch (questionResult.getQbQuestion().getType()) {
		case QbQuestion.TYPE_MATCHING_PAIRS:
		    String str = "";
		    if (optionAnswers != null) {
			for (QbOption option : options) {
			    str += "<div>";
			    str += "	<div style='float: left;'>";
			    str += option.getMatchingPair();
			    str += "	</div>";
			    str += "	<div style=' float: right; width: 50%;'>";
			    str += " 		- ";

			    for (AssessmentOptionAnswer optionAnswer : optionAnswers) {
				if (option.getUid().equals(optionAnswer.getOptionUid())) {
				    for (QbOption option2 : options) {
					if (option2.getUid() == optionAnswer.getAnswerInt()) {
					    str += option2.getName();
					}
				    }
				}
			    }

			    str += "</div>";
			    str += "</div>";
			    str += DELIMITER;

			}
		    }
		    return str;

		case QbQuestion.TYPE_MULTIPLE_CHOICE:

		    if (optionAnswers != null) {
			for (AssessmentOptionAnswer optionAnswer : optionAnswers) {
			    if (optionAnswer.getAnswerBoolean()) {
				for (QbOption option : options) {
				    if (option.getUid().equals(optionAnswer.getOptionUid())) {
					responseStr.append(option.getName() + DELIMITER);
				    }
				}
			    }
			}
		    }
		    break;

		case QbQuestion.TYPE_NUMERICAL:
		case QbQuestion.TYPE_SHORT_ANSWER:
		case QbQuestion.TYPE_ESSAY:
		    responseStr.append(questionResult.getAnswerString());
		    break;

		case QbQuestion.TYPE_ORDERING:
		    if (optionAnswers != null) {
			for (int i = 0; i < optionAnswers.size(); i++) {
			    for (AssessmentOptionAnswer optionAnswer : optionAnswers) {
				if (optionAnswer.getAnswerInt() == i) {
				    for (QbOption option : options) {
					if (option.getUid().equals(optionAnswer.getOptionUid())) {
					    responseStr.append(option.getName());
					}
				    }
				}
			    }
			}
		    }
		    break;

		case QbQuestion.TYPE_TRUE_FALSE:
		    if (questionResult.getAnswerString() != null) {
			responseStr.append(questionResult.getAnswerBoolean());
		    }
		    break;

		case QbQuestion.TYPE_MARK_HEDGING:

		    if (optionAnswers != null) {
			for (QbOption option : options) {
			    responseStr.append("<div>");
			    responseStr.append("	<div style='float: left;'>");
			    responseStr.append(option.getName());
			    responseStr.append("	</div>");

			    responseStr.append("	<div style='float: right; width: 20%;'>");
			    responseStr.append(" - ");
			    for (AssessmentOptionAnswer optionAnswer : optionAnswers) {
				if (option.getUid().equals(optionAnswer.getOptionUid())) {
				    responseStr.append(optionAnswer.getAnswerInt());
				}
			    }
			    responseStr.append("	</div>");

			    responseStr.append("</div>");
			    responseStr.append(DELIMITER);

			}

			if (questionResult.getQbQuestion().isHedgingJustificationEnabled()) {
			    responseStr.append(questionResult.getAnswerString());
			    responseStr.append(DELIMITER);
			}
		    }
		    break;

		default:
		    return null;
	    }
	}
	return responseStr.toString();
    }

    /**
     * Used only for excell export (for getUserSummaryData() method).
     */
    public static Object printResponsesForExcelExport(AssessmentQuestionResult questionResult) {
	Object ret = null;

	if (questionResult != null) {
	    switch (questionResult.getQbQuestion().getType()) {
		case QbQuestion.TYPE_ESSAY:
		    String answerString = questionResult.getAnswerString();
		    return (answerString == null) ? ""
			    : answerString.replaceAll("\\<.*?>", "").replaceAll("&nbsp;", " ");
		case QbQuestion.TYPE_MATCHING_PAIRS:
		    return AssessmentEscapeUtils.getOptionResponse(questionResult,
			    QbQuestion.TYPE_MATCHING_PAIRS);
		case QbQuestion.TYPE_MULTIPLE_CHOICE:
		    return AssessmentEscapeUtils.getOptionResponse(questionResult,
			    QbQuestion.TYPE_MULTIPLE_CHOICE);
		case QbQuestion.TYPE_NUMERICAL:
		    return questionResult.getAnswerString();
		case QbQuestion.TYPE_ORDERING:
		    return AssessmentEscapeUtils.getOptionResponse(questionResult,
			    QbQuestion.TYPE_ORDERING);
		case QbQuestion.TYPE_SHORT_ANSWER:
		    return questionResult.getAnswerString();
		case QbQuestion.TYPE_TRUE_FALSE:
		    return questionResult.getAnswerBoolean();
		case QbQuestion.TYPE_MARK_HEDGING:
		    //taken care beforehand
		default:
		    return null;
	    }
	}
	return ret;
    }

    /**
     * Used only for excell export (for getUserSummaryData() method).
     */
    private static String getOptionResponse(AssessmentQuestionResult questionResult, int type) {

	StringBuilder sb = new StringBuilder();
	//whether there is a need to remove last comma
	boolean trimLastComma = false;

	List<QbOption> options = questionResult.getQbQuestion().getQbOptions();
	Set<AssessmentOptionAnswer> optionAnswers = questionResult.getOptionAnswers();
	if (optionAnswers != null) {

	    if (type == QbQuestion.TYPE_MULTIPLE_CHOICE) {
		for (AssessmentOptionAnswer optionAnswer : optionAnswers) {
		    if (optionAnswer.getAnswerBoolean()) {
			for (QbOption option : options) {
			    if (option.getUid().equals(optionAnswer.getOptionUid())) {
				sb.append(option.getName() + ", ");
				trimLastComma = true;
			    }
			}
		    }
		}

	    } else if (type == QbQuestion.TYPE_ORDERING) {
		for (int i = 0; i < optionAnswers.size(); i++) {
		    for (AssessmentOptionAnswer optionAnswer : optionAnswers) {
			if (optionAnswer.getAnswerInt() == i) {
			    for (QbOption option : options) {
				if (option.getUid().equals(optionAnswer.getOptionUid())) {
				    sb.append(option.getName() + ", ");
				    trimLastComma = true;
				}
			    }
			}
		    }
		}

	    } else if (type == QbQuestion.TYPE_MATCHING_PAIRS) {

		for (QbOption option : options) {
		    sb.append("[" + option.getName() + ", ");

		    for (AssessmentOptionAnswer optionAnswer : optionAnswers) {
			if (option.getUid().equals(optionAnswer.getOptionUid())) {
			    for (QbOption option2 : options) {
				if (option2.getUid() == optionAnswer.getAnswerInt()) {
				    sb.append(option2.getName() + "] ");
				}
			    }
			}
		    }

		}

	    } else if (type == QbQuestion.TYPE_MARK_HEDGING) {
		//taken care beforehand
	    }

	}
	String ret = sb.toString().replaceAll("\\<.*?\\>", "");

	if (trimLastComma) {
	    ret = ret.substring(0, ret.lastIndexOf(","));
	}

	return ret;
    }

}
