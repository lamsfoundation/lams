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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
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

import java.util.List;
import java.util.Set;

public class AssessmentEscapeUtils {

    public static class AssessmentExcelCell {
	public Object value;
	public boolean isHighlighted;

	private AssessmentExcelCell(Object value, boolean isHighlighted) {
	    this.value = value;
	    this.isHighlighted = isHighlighted;
	}
    }

    /**
     * Escapes all characters that may brake JS code on assigning Java value to JS String variable (particularly escapes
     * all quotes in the following way \").
     */
    public static void escapeQuotes(UserSummary userSummary) {
	for (UserSummaryItem userSummaryItem : userSummary.getUserSummaryItems()) {
	    AssessmentEscapeUtils.escapeQuotes(userSummaryItem.getQuestionDto());
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

    public static void escapeQuotesInQuestionResult(AssessmentQuestionResult questionResult) {
	String answer = questionResult.getAnswer();
	if (answer != null) {
	    String answerEscaped = StringEscapeUtils.escapeJavaScript(answer);
	    questionResult.setanswerEscaped(answerEscaped);
	}

	QuestionDTO questionDto = new QuestionDTO(questionResult.getQbToolQuestion());
	questionResult.setQuestionDto(questionDto);

	AssessmentEscapeUtils.escapeQuotes(questionDto);

	String markerCommentEscaped = "";
	if (StringUtils.isNotBlank(questionResult.getMarkerComment())) {
	    markerCommentEscaped = questionResult.getMarkerComment().replace("\n", "<br>").replace("\"", "\\\"");
	}
	questionResult.setMarkerCommentEscaped(markerCommentEscaped);
    }

    private static void escapeQuotes(QuestionDTO questionDto) {
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
			    boolean isCorrect = false;
			    String selectedOption = null;

			    for (AssessmentOptionAnswer optionAnswer : optionAnswers) {
				if (option.getUid().equals(optionAnswer.getOptionUid())) {
				    for (QbOption option2 : options) {
					if (option2.getUid() == optionAnswer.getAnswerInt()) {
					    selectedOption = option2.getName();
					    isCorrect = option.getUid()
						    .equals(Long.valueOf(optionAnswer.getAnswerInt()));
					    break;
					}
				    }
				}

				if (selectedOption != null) {
				    break;
				}
			    }

			    str += "<div";
			    if (selectedOption != null) {
				str += " class=\"" + (isCorrect ? "text-success" : "text-danger") + "\"";
			    }
			    str += ">	<div style='float: left;'>";
			    str += option.getMatchingPair();
			    str += "	</div>";
			    str += "	<div style=' float: right; width: 50%;'>";
			    str += " 		- ";
			    if (selectedOption != null) {
				str += selectedOption;
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
		case QbQuestion.TYPE_VERY_SHORT_ANSWERS:
		case QbQuestion.TYPE_ESSAY:
		    responseStr.append(
			    StringUtils.isBlank(questionResult.getAnswer()) ? "-" : questionResult.getAnswer());
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
		    if (questionResult.getAnswer() != null) {
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
			    responseStr.append(questionResult.getAnswer());
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
    public static AssessmentExcelCell addResponseCellForExcelExport(AssessmentQuestionResult questionResult,
	    boolean useLettersForMcq) {
	if (questionResult == null) {
	    return null;
	}

	switch (questionResult.getQbQuestion().getType()) {
	    case QbQuestion.TYPE_ESSAY:
		Object value = AssessmentEscapeUtils.escapeStringForExcelExport(questionResult.getAnswer());
		return new AssessmentExcelCell(value, false);
	    case QbQuestion.TYPE_MATCHING_PAIRS:
		return AssessmentEscapeUtils.getOptionResponse(questionResult, QbQuestion.TYPE_MATCHING_PAIRS, false);
	    case QbQuestion.TYPE_MULTIPLE_CHOICE:
	    case QbQuestion.TYPE_MARK_HEDGING:
		return AssessmentEscapeUtils.getOptionResponse(questionResult, questionResult.getQbQuestion().getType(),
			useLettersForMcq);
	    case QbQuestion.TYPE_NUMERICAL:
		return new AssessmentExcelCell(questionResult.getAnswer(), false);
	    case QbQuestion.TYPE_ORDERING:
		return AssessmentEscapeUtils.getOptionResponse(questionResult, QbQuestion.TYPE_ORDERING, false);
	    case QbQuestion.TYPE_VERY_SHORT_ANSWERS:
		return new AssessmentExcelCell(questionResult.getAnswer(), false);
	    case QbQuestion.TYPE_TRUE_FALSE:
		boolean isCorrect =
			questionResult.getQbQuestion().getCorrectAnswer() == questionResult.getAnswerBoolean();
		return new AssessmentExcelCell(questionResult.getAnswerBoolean(), isCorrect);
	}
	return null;
    }

    public static String escapeStringForExcelExport(String input) {
	return input == null ? "" : input.replaceAll("\\<.*?>", "").replaceAll("&nbsp;", " ");
    }

    /**
     * Used only for excell export (for getUserSummaryData() method).
     */
    private static AssessmentExcelCell getOptionResponse(AssessmentQuestionResult questionResult, int type,
	    boolean useLettersForMcq) {

	StringBuilder sb = new StringBuilder();
	//whether there is a need to remove last comma
	boolean trimLastComma = false;
	boolean highlightCell = false;

	List<QbOption> options = questionResult.getQbQuestion().getQbOptions();
	Set<AssessmentOptionAnswer> optionAnswers = questionResult.getOptionAnswers();
	if (optionAnswers != null) {
	    if (type == QbQuestion.TYPE_MULTIPLE_CHOICE || type == QbQuestion.TYPE_MARK_HEDGING) {
		highlightCell = type == QbQuestion.TYPE_MULTIPLE_CHOICE;
		int letter = 'A';
		for (QbOption option : options) {
		    for (AssessmentOptionAnswer optionAnswer : optionAnswers) {
			if (option.getUid().equals(optionAnswer.getOptionUid())) {
			    if (optionAnswer.getAnswerBoolean() || type == QbQuestion.TYPE_MARK_HEDGING) {
				// either we display full answers or just letters of chosen options
				sb.append(useLettersForMcq ? String.valueOf((char) letter) : option.getName());
				if (type == QbQuestion.TYPE_MARK_HEDGING) {
				    sb.append('=').append(optionAnswer.getAnswerInt());
				}
				sb.append(',');
				trimLastComma = true;

				// if any answer is wrong, we do not highlight correct answer
				if (option.getMaxMark() <= 0) {
				    highlightCell = false;
				}
			    }
			    break;
			}
		    }
		    letter++;
		}

		// do not highlight if we use full answers, not letters,
		// or if no answer was provided
		highlightCell &= useLettersForMcq && StringUtils.isNotBlank(sb.toString());

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
		    sb.append("[" + option.getMatchingPair() + "-");

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

	    }

	}
	String ret = sb.toString().replaceAll("\\<.*?\\>|\\r|\\n", "").strip();

	if (trimLastComma) {
	    ret = ret.substring(0, ret.lastIndexOf(","));
	}

	return new AssessmentExcelCell(StringUtils.isBlank(ret) ? null : ret, highlightCell);
    }
}