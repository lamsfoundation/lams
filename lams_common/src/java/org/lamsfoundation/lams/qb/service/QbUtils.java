package org.lamsfoundation.lams.qb.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.QbConstants;
import org.lamsfoundation.lams.qb.form.QbQuestionForm;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.model.QbQuestionUnit;
import org.lamsfoundation.lams.util.WebUtil;

public class QbUtils {
    
    private static Logger log = Logger.getLogger(QbUtils.class);

    /**
     * This method will populate assessment question information to its form for edit use.
     */
    public static void fillFormWithQbQuestion(QbQuestion qbQuestion, QbQuestionForm form,
	    HttpServletRequest request) {
	form.setTitle(qbQuestion.getName());
	form.setQuestion(qbQuestion.getDescription());
	form.setMaxMark(String.valueOf(qbQuestion.getMaxMark()));
	form.setPenaltyFactor(String.valueOf(qbQuestion.getPenaltyFactor()));
	form.setAnswerRequired(qbQuestion.isAnswerRequired());
	form.setFeedback(qbQuestion.getFeedback());
	form.setMultipleAnswersAllowed(qbQuestion.isMultipleAnswersAllowed());
	form.setIncorrectAnswerNullifiesMark(qbQuestion.isIncorrectAnswerNullifiesMark());
	form.setFeedbackOnCorrect(qbQuestion.getFeedbackOnCorrect());
	form.setFeedbackOnPartiallyCorrect(qbQuestion.getFeedbackOnPartiallyCorrect());
	form.setFeedbackOnIncorrect(qbQuestion.getFeedbackOnIncorrect());
	form.setShuffle(qbQuestion.isShuffle());
	form.setPrefixAnswersWithLetters(qbQuestion.isPrefixAnswersWithLetters());
	form.setCaseSensitive(qbQuestion.isCaseSensitive());
	form.setCorrectAnswer(qbQuestion.getCorrectAnswer());
	form.setAllowRichEditor(qbQuestion.isAllowRichEditor());
	form.setMaxWordsLimit(qbQuestion.getMaxWordsLimit());
	form.setMinWordsLimit(qbQuestion.getMinWordsLimit());
	form.setHedgingJustificationEnabled(qbQuestion.isHedgingJustificationEnabled());

	Integer questionType = qbQuestion.getType();
	if ((questionType == QbQuestion.TYPE_MULTIPLE_CHOICE)
		|| (questionType == QbQuestion.TYPE_ORDERING)
		|| (questionType == QbQuestion.TYPE_MATCHING_PAIRS)
		|| (questionType == QbQuestion.TYPE_SHORT_ANSWER)
		|| (questionType == QbQuestion.TYPE_NUMERICAL)
		|| (questionType == QbQuestion.TYPE_MARK_HEDGING)) {
	    List<QbOption> optionList = qbQuestion.getQbOptions();
	    request.setAttribute(QbConstants.ATTR_OPTION_LIST, optionList);
	}
	if (questionType == QbQuestion.TYPE_NUMERICAL) {
	    List<QbQuestionUnit> unitList = qbQuestion.getUnits();
	    request.setAttribute(QbConstants.ATTR_UNIT_LIST, unitList);
	}
    }

    /**
     * Extract web form content to QB question.
     * 
     * BE CAREFUL: This method will copy necessary info from request form to an old or new AssessmentQuestion
     * instance. It gets all info EXCEPT AssessmentQuestion.createDate, which need be set when
     * persisting this assessment Question.
     * 
     * @return qbQuestionModified
     */
    public static int extractFormToQbQuestion(QbQuestion qbQuestion, QbQuestionForm questionForm,
	    HttpServletRequest request, IQbService qbService, boolean isAuthoringRestricted) {
	QbQuestion baseLine = qbQuestion.clone();
	// evict everything manually as we do not use DTOs, just real entities
	// without eviction changes would be saved immediately into DB
	qbService.releaseFromCache(baseLine);

	qbQuestion.setName(questionForm.getTitle());
	qbQuestion.setDescription(questionForm.getQuestion());

	if (!isAuthoringRestricted) {
	    qbQuestion.setMaxMark(Integer.parseInt(questionForm.getMaxMark()));
	}
	qbQuestion.setFeedback(questionForm.getFeedback());
	qbQuestion.setAnswerRequired(questionForm.isAnswerRequired());

	Integer type = questionForm.getQuestionType();
	if (type == QbQuestion.TYPE_MULTIPLE_CHOICE) {
	    qbQuestion.setMultipleAnswersAllowed(questionForm.isMultipleAnswersAllowed());
	    boolean incorrectAnswerNullifiesMark = questionForm.isMultipleAnswersAllowed()
		    ? questionForm.isIncorrectAnswerNullifiesMark()
		    : false;
	    qbQuestion.setIncorrectAnswerNullifiesMark(incorrectAnswerNullifiesMark);
	    qbQuestion.setPenaltyFactor(Float.parseFloat(questionForm.getPenaltyFactor()));
	    qbQuestion.setShuffle(questionForm.isShuffle());
	    qbQuestion.setPrefixAnswersWithLetters(questionForm.isPrefixAnswersWithLetters());
	    qbQuestion.setFeedbackOnCorrect(questionForm.getFeedbackOnCorrect());
	    qbQuestion.setFeedbackOnPartiallyCorrect(questionForm.getFeedbackOnPartiallyCorrect());
	    qbQuestion.setFeedbackOnIncorrect(questionForm.getFeedbackOnIncorrect());
	} else if ((type == QbQuestion.TYPE_MATCHING_PAIRS)) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(questionForm.getPenaltyFactor()));
	    qbQuestion.setShuffle(questionForm.isShuffle());
	} else if ((type == QbQuestion.TYPE_SHORT_ANSWER)) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(questionForm.getPenaltyFactor()));
	    qbQuestion.setCaseSensitive(questionForm.isCaseSensitive());
	} else if ((type == QbQuestion.TYPE_NUMERICAL)) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(questionForm.getPenaltyFactor()));
	} else if ((type == QbQuestion.TYPE_TRUE_FALSE)) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(questionForm.getPenaltyFactor()));
	    qbQuestion.setCorrectAnswer(questionForm.isCorrectAnswer());
	    qbQuestion.setFeedbackOnCorrect(questionForm.getFeedbackOnCorrect());
	    qbQuestion.setFeedbackOnIncorrect(questionForm.getFeedbackOnIncorrect());
	} else if ((type == QbQuestion.TYPE_ESSAY)) {
	    qbQuestion.setAllowRichEditor(questionForm.isAllowRichEditor());
	    qbQuestion.setMaxWordsLimit(questionForm.getMaxWordsLimit());
	    qbQuestion.setMinWordsLimit(questionForm.getMinWordsLimit());
	} else if (type == QbQuestion.TYPE_ORDERING) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(questionForm.getPenaltyFactor()));
	    qbQuestion.setFeedbackOnCorrect(questionForm.getFeedbackOnCorrect());
	    qbQuestion.setFeedbackOnIncorrect(questionForm.getFeedbackOnIncorrect());
	} else if (type == QbQuestion.TYPE_MARK_HEDGING) {
	    qbQuestion.setShuffle(questionForm.isShuffle());
	    qbQuestion.setFeedbackOnCorrect(questionForm.getFeedbackOnCorrect());
	    qbQuestion.setFeedbackOnPartiallyCorrect(questionForm.getFeedbackOnPartiallyCorrect());
	    qbQuestion.setFeedbackOnIncorrect(questionForm.getFeedbackOnIncorrect());
	    qbQuestion.setHedgingJustificationEnabled(questionForm.isHedgingJustificationEnabled());
	}

	// set options
	if ((type == QbQuestion.TYPE_MULTIPLE_CHOICE)
		|| (type == QbQuestion.TYPE_ORDERING)
		|| (type == QbQuestion.TYPE_MATCHING_PAIRS)
		|| (type == QbQuestion.TYPE_SHORT_ANSWER)
		|| (type == QbQuestion.TYPE_NUMERICAL)
		|| (type == QbQuestion.TYPE_MARK_HEDGING)) {
	    Set<QbOption> optionList = QbUtils.getOptionsFromRequest(qbService, request, true);
	    List<QbOption> options = new ArrayList<>();
	    int displayOrder = 0;
	    for (QbOption option : optionList) {
		option.setDisplayOrder(displayOrder++);
		options.add(option);
	    }
	    qbQuestion.setQbOptions(options);
	}
	// set units
	if (type == QbQuestion.TYPE_NUMERICAL) {
	    Set<QbQuestionUnit> unitList = QbUtils.getUnitsFromRequest(qbService, request, true);
	    List<QbQuestionUnit> units = new ArrayList<>();
	    int displayOrder = 0;
	    for (QbQuestionUnit unit : unitList) {
		unit.setDisplayOrder(displayOrder++);
		units.add(unit);
	    }
	    qbQuestion.setUnits(units);
	}
	
	return QbUtils.isQbQuestionModified(baseLine, qbQuestion);
    }
    

    /**
     * Get answer options from <code>HttpRequest</code>
     *
     * @param request
     * @param isForSaving
     *            whether the blank options will be preserved or not
     */
    public static TreeSet<QbOption> getOptionsFromRequest(IQbService qbService, HttpServletRequest request,
	    boolean isForSaving) {
	Map<String, String> paramMap = QbUtils.splitRequestParameter(request, QbConstants.ATTR_OPTION_LIST);

	int count = NumberUtils.toInt(paramMap.get(QbConstants.ATTR_OPTION_COUNT));
	int questionType = WebUtil.readIntParam(request, QbConstants.ATTR_QUESTION_TYPE);
	Integer correctOptionIndex = (paramMap.get(QbConstants.ATTR_OPTION_CORRECT) == null) ? null
		: NumberUtils.toInt(paramMap.get(QbConstants.ATTR_OPTION_CORRECT));
	
	TreeSet<QbOption> optionList = new TreeSet<>();
	for (int i = 0; i < count; i++) {
	    
	    String displayOrder = paramMap.get(QbConstants.ATTR_OPTION_DISPLAY_ORDER_PREFIX + i);
	    //displayOrder is null, in case this item was removed using Remove button
	    if (displayOrder == null) {
		continue;
	    }
	    
	    QbOption option = null;
	    String uidStr = paramMap.get(QbConstants.ATTR_OPTION_UID_PREFIX + i);
	    if (uidStr != null) {
		Long uid = NumberUtils.toLong(uidStr);
		option = qbService.getQbOptionByUid(uid);
		
	    } else {
		option = new QbOption();
	    }
	    option.setDisplayOrder(NumberUtils.toInt(displayOrder));
	    
	    if ((questionType == QbQuestion.TYPE_MULTIPLE_CHOICE)
		    || (questionType == QbQuestion.TYPE_SHORT_ANSWER)) {
		String name = paramMap.get(QbConstants.ATTR_OPTION_NAME_PREFIX + i);
		if ((name == null) && isForSaving) {
		    continue;
		}

		option.setName(name);
		float maxMark = Float.valueOf(paramMap.get(QbConstants.ATTR_OPTION_MAX_MARK_PREFIX + i));
		option.setMaxMark(maxMark);
		option.setFeedback(paramMap.get(QbConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));

	    } else if (questionType == QbQuestion.TYPE_MATCHING_PAIRS) {
		String matchingPair = paramMap.get(QbConstants.ATTR_MATCHING_PAIR_PREFIX + i);
		if ((matchingPair == null) && isForSaving) {
		    continue;
		}

		option.setName(paramMap.get(QbConstants.ATTR_OPTION_NAME_PREFIX + i));
		option.setMatchingPair(matchingPair);

	    } else if (questionType == QbQuestion.TYPE_NUMERICAL) {
		String numericalOptionStr = paramMap.get(QbConstants.ATTR_NUMERICAL_OPTION_PREFIX + i);
		String acceptedErrorStr = paramMap.get(QbConstants.ATTR_OPTION_ACCEPTED_ERROR_PREFIX + i);
		String maxMarkStr = paramMap.get(QbConstants.ATTR_OPTION_MAX_MARK_PREFIX + i);
		if (numericalOptionStr.equals("0.0") && numericalOptionStr.equals("0.0") && maxMarkStr.equals("0.0")
			&& isForSaving) {
		    continue;
		}

		try {
		    float numericalOption = Float.valueOf(numericalOptionStr);
		    option.setNumericalOption(numericalOption);
		} catch (Exception e) {
		    option.setNumericalOption(0);
		}
		try {
		    float acceptedError = Float.valueOf(acceptedErrorStr);
		    option.setAcceptedError(acceptedError);
		} catch (Exception e) {
		    option.setAcceptedError(0);
		}
		float maxMark = Float.valueOf(paramMap.get(QbConstants.ATTR_OPTION_MAX_MARK_PREFIX + i));
		option.setMaxMark(maxMark);
		option.setFeedback(paramMap.get(QbConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));

	    } else if (questionType == QbQuestion.TYPE_ORDERING) {
		String name = paramMap.get(QbConstants.ATTR_OPTION_NAME_PREFIX + i);
		if ((name == null) && isForSaving) {
		    continue;
		}

		option.setName(name);
		
	    } else if (questionType == QbQuestion.TYPE_MARK_HEDGING) {
		String name = paramMap.get(QbConstants.ATTR_OPTION_NAME_PREFIX + i);
		if ((name == null) && isForSaving) {
		    continue;
		}

		option.setName(name);
		if ((correctOptionIndex != null) && correctOptionIndex.equals(Integer.valueOf(displayOrder))) {
		    option.setCorrect(true);
		}
		option.setFeedback(paramMap.get(QbConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));
	    }
	    
	    optionList.add(option);
	}
	return optionList;
    }

    /**
     * Get units from <code>HttpRequest</code>
     *
     * @param request
     */
    public static TreeSet<QbQuestionUnit> getUnitsFromRequest(IQbService qbService, HttpServletRequest request,
	    boolean isForSaving) {
	Map<String, String> paramMap = QbUtils.splitRequestParameter(request, QbConstants.ATTR_UNIT_LIST);

	int count = NumberUtils.toInt(paramMap.get(QbConstants.ATTR_UNIT_COUNT));
	TreeSet<QbQuestionUnit> unitList = new TreeSet<>();
	for (int i = 0; i < count; i++) {
	    String name = paramMap.get(QbConstants.ATTR_UNIT_NAME_PREFIX + i);
	    if (StringUtils.isBlank(name) && isForSaving) {
		continue;
	    }

	    QbQuestionUnit unit = null;
	    String uidStr = paramMap.get(QbConstants.ATTR_UNIT_UID_PREFIX + i);
	    if (uidStr != null) {
		Long uid = NumberUtils.toLong(uidStr);
		unit = qbService.getQbQuestionUnitByUid(uid);
		
	    } else {
		unit = new QbQuestionUnit();
	    }
	    String displayOrder = paramMap.get(QbConstants.ATTR_UNIT_DISPLAY_ORDER_PREFIX + i);
	    unit.setDisplayOrder(NumberUtils.toInt(displayOrder));
	    unit.setName(name);
	    float multiplier = Float.valueOf(paramMap.get(QbConstants.ATTR_UNIT_MULTIPLIER_PREFIX + i));
	    unit.setMultiplier(multiplier);
	    unitList.add(unit);
	}

	return unitList;
    }
    
    /**
     * Split Request Parameter from <code>HttpRequest</code>
     *
     * @param request
     * @param parameterName
     *            parameterName
     */
    private static Map<String, String> splitRequestParameter(HttpServletRequest request, String parameterName) {
	String list = request.getParameter(parameterName);
	if (list == null) {
	    return null;
	}

	String[] params = list.split("&");
	Map<String, String> paramMap = new HashMap<>();
	String[] pair;
	for (String item : params) {
	    pair = item.split("=");
	    if ((pair == null) || (pair.length != 2)) {
		continue;
	    }
	    try {
		paramMap.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
	    } catch (UnsupportedEncodingException e) {
		log.error("Error occurs when decode instruction string:" + e.toString());
	    }
	}
	return paramMap;
    }
    
    public static int isQbQuestionModified(QbQuestion baseLine, QbQuestion modifiedQuestion) {
	if (baseLine.getUid() == null) {
	    return IQbService.QUESTION_MODIFIED_ID_BUMP;
	}

	return baseLine.isModified(modifiedQuestion) ? IQbService.QUESTION_MODIFIED_VERSION_BUMP
		: IQbService.QUESTION_MODIFIED_NONE;
    }

}
