package org.lamsfoundation.lams.qb;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.qb.form.QbQuestionForm;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class QbUtils {

    public static final String VSA_ANSWER_NORMALISE_JAVA_REG_EXP = "\\W";
    public static final String VSA_ANSWER_NORMALISE_SQL_REG_EXP = "[^[:alpha:][:alnum:]_]";
    public static final String VSA_ANSWER_DELIMITER = "\r\n";

    public static final Function<String, String> QB_MIGRATION_CKEDITOR_CLEANER = string -> string == null ? null
	    : string.replaceAll(">\\&nbsp;", ">").replaceAll("\\r|\\n", "").trim();

    public static final Function<String, String> QB_MIGRATION_TAG_CLEANER = string -> string == null ? null
	    : WebUtil.removeHTMLtags(string).replaceAll(">\\&nbsp;", " ").replaceAll("\\t", " ").trim();

    public static final Function<String, String> QB_MIGRATION_QUESTION_NAME_GENERATOR = description -> {
	String name = QB_MIGRATION_TAG_CLEANER.apply(description);
	return name == null ? null : name.substring(0, Math.min(80, name.length()));
    };

    public static final Function<String, String> QB_MIGRATION_TRIMMER = string -> StringUtils.isBlank(string) ? null
	    : string.trim();

    public static void fillFormWithUserCollections(IQbService qbService, QbQuestionForm form, Long qbQuestionUid) {
	QbUtils.fillFormWithUserCollections(qbService, null, form, qbQuestionUid);
    }

    public static void fillFormWithUserCollections(IQbService qbService, String toolSignature, QbQuestionForm form,
	    Long qbQuestionUid) {
	final boolean isRequestCameFromTool = StringUtils.isNotBlank(form.getSessionMapID());
	boolean isDefaultQuestion = qbQuestionUid == null ? false
		: qbService.isQuestionDefaultInTool(qbQuestionUid, toolSignature);
	Integer userId = QbUtils.getUserId();

	if (isRequestCameFromTool && !isDefaultQuestion && qbQuestionUid != null) {
	    QbQuestion qbQuestion = qbService.getQuestionByUid(qbQuestionUid);
	    boolean isQuestionInUserCollection = qbService.isQuestionInUserOwnCollection(qbQuestion.getQuestionId(),
		    userId);
	    if (!isQuestionInUserCollection) {
		// in a tool the user is editing a question which is not in one of his collections
		// do not let him change the collection, i.e. steal the question
		return;
	    }
	}

	//prepare data for displaying collections
	Collection<QbCollection> userCollections = qbService.getUserCollections(userId);
	form.setUserCollections(userCollections);

	//in case request came not from the tool, collectioUid is already supplied as parameter from collections.jsp
	if (!isRequestCameFromTool) {
	    return;
	}

	Collection<QbCollection> questionCollections = qbQuestionUid == null ? new LinkedList<>()
		: qbService.getQuestionCollectionsByUid(qbQuestionUid);
	Long collectionUid = null;
	if (isDefaultQuestion || questionCollections.isEmpty()) {
	    //set private collection as default, if question is new or doesn't have associated collection
	    for (QbCollection collection : userCollections) {
		if (collection.isPersonal()) {
		    collectionUid = collection.getUid();
		    break;
		}
	    }
	} else {
	    collectionUid = questionCollections.iterator().next().getUid();
	}
	form.setOldCollectionUid(collectionUid);
    }

    public static String normaliseVSAnswer(String answer, boolean isExactMatch) {
	if (StringUtils.isBlank(answer)) {
	    return null;
	}
	String normalisedAnswer = isExactMatch ? answer.strip()
		: answer.replaceAll(VSA_ANSWER_NORMALISE_JAVA_REG_EXP, "");
	if (StringUtils.isBlank(normalisedAnswer)) {
	    return null;
	}
	return normalisedAnswer;
    }

    public static Set<String> normaliseVSOption(String option, boolean isExactMatch) {
	return StringUtils.isBlank(option) ? Set.of()
		: Stream.of(option.split(VSA_ANSWER_DELIMITER)).filter(StringUtils::isNotBlank)
			.map(answer -> QbUtils.normaliseVSAnswer(answer, isExactMatch)).filter(StringUtils::isNotBlank)
			.collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static boolean isVSAnswerAllocated(String option, String normalisedAnswer, boolean isCaseSensitive,
	    boolean isExactMatch) {
	if (StringUtils.isBlank(option) || StringUtils.isBlank(normalisedAnswer)) {
	    return false;
	}
	return QbUtils.normaliseVSOption(option, isExactMatch).stream()
		.anyMatch(s -> isCaseSensitive ? s.equals(normalisedAnswer) : s.equalsIgnoreCase(normalisedAnswer));
    }

    /**
     * Check is given answer is present in any of VSA question's options.
     *
     * @param notAllocatedAnswers
     *            is an accumulator for unallocated answers so they do not appear in VSA allocation UI twice
     */
    public static boolean isVSAnswerAllocated(QbQuestion qbQuestion, String answer, Set<String> notAllocatedAnswers) {
	boolean isExactMatch = qbQuestion.isExactMatch();

	String normalisedAnswer = QbUtils.normaliseVSAnswer(answer, isExactMatch);
	if (StringUtils.isBlank(normalisedAnswer)) {
	    return false;
	}

	boolean isQuestionCaseSensitive = qbQuestion.isCaseSensitive();
	boolean isAnswerAllocated = false;

	for (QbOption option : qbQuestion.getQbOptions()) {
	    String name = option.getName();
	    isAnswerAllocated = QbUtils.isVSAnswerAllocated(name, normalisedAnswer, isQuestionCaseSensitive,
		    isExactMatch);
	    if (isAnswerAllocated) {
		break;
	    }
	}

	if (!isAnswerAllocated) {
	    if (!isQuestionCaseSensitive) {
		normalisedAnswer = normalisedAnswer.toLowerCase();
	    }
	    // do not add repetitive students' suggestions for teacher to assign to an option
	    if (notAllocatedAnswers.contains(normalisedAnswer)) {
		isAnswerAllocated = true;
	    } else {
		notAllocatedAnswers.add(normalisedAnswer);
	    }
	}

	return isAnswerAllocated;
    }

    private static Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }

}
