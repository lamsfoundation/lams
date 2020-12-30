package org.lamsfoundation.lams.qb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.lamsfoundation.lams.qb.service.IQbService;

/**
 * A question in Question Bank.
 *
 * @author Marcin Cieslak
 */
@Entity
@Table(name = "lams_qb_question")
public class QbQuestion implements Serializable, Cloneable {
    private static final long serialVersionUID = -6287273838239262151L;

    // questions can be of different type
    // not all tools can produce/consume all question types
    public static final int TYPE_MULTIPLE_CHOICE = 1;
    public static final int TYPE_MATCHING_PAIRS = 2;
    public static final int TYPE_VERY_SHORT_ANSWERS = 3;
    public static final int TYPE_NUMERICAL = 4;
    public static final int TYPE_TRUE_FALSE = 5;
    public static final int TYPE_ESSAY = 6;
    public static final int TYPE_ORDERING = 7;
    public static final int TYPE_MARK_HEDGING = 8;

    // primary key
    // another candidate is questionId + version, but single uid can be searched faster
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private UUID uuid;

    // one of question types
    @Column
    private Integer type;

    // "tracking ID" for a question
    // multiple versions can share the same question ID so their stats are aggregated
    @Column(name = "question_id")
    private Integer questionId;

    // the same question can have multiple versions
    @Column
    private Integer version = 1;

    @Column(name = "create_date")
    private Date createDate = new Date();

    @Column(name = "content_folder_id")
    private String contentFolderId;

    // text of the question
    @Column
    private String name;

    @Column
    private String description;

    @Column(name = "max_mark")
    private Integer maxMark = 1;

    @Column
    private String feedback;

    @Column(name = "penalty_factor")
    private float penaltyFactor;

    @Column(name = "multiple_answers_allowed")
    private boolean multipleAnswersAllowed;

    @Column(name = "incorrect_answer_nullifies_mark")
    private boolean incorrectAnswerNullifiesMark;

    @Column(name = "feedback_on_correct")
    private String feedbackOnCorrect;

    @Column(name = "feedback_on_partially_correct")
    private String feedbackOnPartiallyCorrect;

    @Column(name = "feedback_on_incorrect")
    private String feedbackOnIncorrect;

    // only one of shuffle and prefixAnswersWithLetters should be on. Both may be off
    @Column
    private boolean shuffle;

    @Column(name = "prefix_answers_with_letters")
    private boolean prefixAnswersWithLetters;

    @Column(name = "case_sensitive")
    private boolean caseSensitive;

    @Column(name = "correct_answer")
    private boolean correctAnswer;

    @Column(name = "allow_rich_editor")
    private boolean allowRichEditor;

    // only for essay type of question
    @Column(name = "max_words_limit")
    private int maxWordsLimit;

    // only for essay type of question
    @Column(name = "min_words_limit")
    private int minWordsLimit;

    /** ---- only for hedging type of question ---- */
    @Column(name = "hedging_justification_enabled")
    private boolean hedgingJustificationEnabled;

    /** ---- only for VSA type of question ---- */
    @Column(name = "autocomplete_enabled")
    private boolean autocompleteEnabled;

    @OneToMany(mappedBy = "qbQuestion", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("displayOrder")
    private List<QbOption> qbOptions = new ArrayList<>();

    @OneToMany(mappedBy = "qbQuestion", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<QbQuestionUnit> units = new ArrayList<>();

    // non-persistent field, useful for displaying other versions of this question
    @Transient
    private Map<Integer, Long> versionMap;

    // checks if important parts of another question are the same as current question's.
    // And if not, determines whether another question/version be created.
    public int isQbQuestionModified(QbQuestion oldQuestion) {
	if (oldQuestion.getUid() == null) {
	    return IQbService.QUESTION_MODIFIED_ID_BUMP;
	}
	if (QbQuestion.TYPE_ESSAY == oldQuestion.getType() || QbQuestion.TYPE_MATCHING_PAIRS == oldQuestion.getType()) {
	    return IQbService.QUESTION_MODIFIED_NONE;
	}

	boolean isModificationRequiresNewVersion = false;
	// title or question is different - do nothing. Also question grade can't be changed

	//QbQuestion.TYPE_TRUE_FALSE
	if (oldQuestion.getCorrectAnswer() != getCorrectAnswer()) {
	    isModificationRequiresNewVersion = true;
	}

	// options are different
	List<QbOption> oldOptions = oldQuestion.getQbOptions();
	List<QbOption> newOptions = getQbOptions();
	for (QbOption oldOption : oldOptions) {
	    for (QbOption newOption : newOptions) {
		if (oldOption.getDisplayOrder() == newOption.getDisplayOrder()) {

		    //ordering
		    if (((oldQuestion.getType() == QbQuestion.TYPE_ORDERING)
			    && (oldOption.getDisplayOrder() != newOption.getDisplayOrder()))
			    //short answer
			    || ((oldQuestion.getType() == QbQuestion.TYPE_VERY_SHORT_ANSWERS)
				    && !StringUtils.equals(oldOption.getName(), newOption.getName()))
			    //numbering
			    || (oldOption.getNumericalOption() != newOption.getNumericalOption())
			    || (oldOption.getAcceptedError() != newOption.getAcceptedError())
			    //changed option maxMark (Assessment tool) or correctness of the option (MCQ/Scratchie/Q&A)
			    || (oldOption.getMaxMark() != newOption.getMaxMark())) {
			isModificationRequiresNewVersion = true;
			break;
		    }
		}
	    }
	}
	if (oldOptions.size() != newOptions.size()) {
	    isModificationRequiresNewVersion = true;
	}

	return isModificationRequiresNewVersion ? IQbService.QUESTION_MODIFIED_VERSION_BUMP
		: IQbService.QUESTION_MODIFIED_NONE;
    }

    @Override
    public boolean equals(Object o) {
	QbQuestion other = (QbQuestion) o;
	// options are also checked if they are equal
	return new EqualsBuilder().append(name, other.name).append(description, other.description)
		.append(feedback, other.feedback).append(maxMark, other.maxMark)
		.append(qbOptions.toArray(), other.getQbOptions().toArray())
		.append(units.toArray(), other.getUnits().toArray()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(name).append(description).append(feedback).append(maxMark).toHashCode();
    }

    @Override
    public QbQuestion clone() {
	QbQuestion clone = null;
	try {
	    clone = (QbQuestion) super.clone();
	} catch (CloneNotSupportedException e) {
	    // it should never happen
	    e.printStackTrace();
	}
	// make a deep copy of options
	List<QbOption> optionsClone = new ArrayList<>(qbOptions.size());
	clone.setQbOptions(optionsClone);
	for (QbOption option : qbOptions) {
	    QbOption optionClone = option.clone();
	    optionClone.setQbQuestion(clone);
	    optionsClone.add(optionClone);
	}
	// make a deep copy of units
	List<QbQuestionUnit> unitsClone = new ArrayList<>(units.size());
	clone.setUnits(unitsClone);
	for (QbQuestionUnit unit : units) {
	    QbQuestionUnit unitClone = unit.clone();
	    unitClone.setQbQuestion(clone);
	    unitsClone.add(unitClone);
	}
	return clone;
    }

    public void clearID() {
	this.uid = null;
	if (qbOptions != null) {
	    for (QbOption option : qbOptions) {
		option.uid = null;
	    }
	}
	if (units != null) {
	    for (QbQuestionUnit unit : units) {
		unit.uid = null;
	    }
	}
    }

    /**
     * Check if it's a TBL case, i.e. only two option groups available, one has 0%, second - 100%
     */
    public boolean isVsaAndCompatibleWithTbl() {
	boolean isVsaAndCompatibleWithTbl = false;

	if (qbOptions.size() == 2) {
	    float firstGroupMark = qbOptions.get(0).getMaxMark();
	    float secondGroupMark = qbOptions.get(1).getMaxMark();

	    isVsaAndCompatibleWithTbl = (firstGroupMark == 0 || firstGroupMark == 1)
		    && (secondGroupMark == 0 || secondGroupMark == 1) && (firstGroupMark != secondGroupMark)
		    && type == QbQuestion.TYPE_VERY_SHORT_ANSWERS;
	}

	return isVsaAndCompatibleWithTbl;
    }

    public Long getUid() {
	return uid;
    }

    public UUID getUuid() {
	return uuid;
    }

    public void setUuid(UUID uuid) {
	this.uuid = uuid;
    }

    public void setUuid(String uuid) {
	this.uuid = uuid == null ? null : UUID.fromString(uuid);
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Integer getQuestionId() {
	return questionId;
    }

    public void setQuestionId(Integer questionId) {
	this.questionId = questionId;
    }

    public Integer getVersion() {
	return version;
    }

    public void setVersion(Integer version) {
	this.version = version;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public String getContentFolderId() {
	return contentFolderId;
    }

    public void setContentFolderId(String contentFolderId) {
	this.contentFolderId = contentFolderId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = StringUtils.isBlank(name) ? null : name.trim();
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Integer getMaxMark() {
	return maxMark == null ? 1 : maxMark;
    }

    public void setMaxMark(Integer maxMark) {
	this.maxMark = maxMark;
    }

    public String getFeedback() {
	return feedback;
    }

    public void setFeedback(String feedback) {
	this.feedback = StringUtils.isBlank(feedback) ? null : feedback.trim();
    }

    public float getPenaltyFactor() {
	return penaltyFactor;
    }

    public void setPenaltyFactor(float penaltyFactor) {
	this.penaltyFactor = penaltyFactor;
    }

    public boolean isMultipleAnswersAllowed() {
	return multipleAnswersAllowed;
    }

    public void setMultipleAnswersAllowed(boolean multipleAnswersAllowed) {
	this.multipleAnswersAllowed = multipleAnswersAllowed;
    }

    public boolean isIncorrectAnswerNullifiesMark() {
	return incorrectAnswerNullifiesMark;
    }

    public void setIncorrectAnswerNullifiesMark(boolean incorrectAnswerNullifiesMark) {
	this.incorrectAnswerNullifiesMark = incorrectAnswerNullifiesMark;
    }

    public String getFeedbackOnCorrect() {
	return feedbackOnCorrect;
    }

    public void setFeedbackOnCorrect(String feedbackOnCorrect) {
	this.feedbackOnCorrect = feedbackOnCorrect;
    }

    public String getFeedbackOnPartiallyCorrect() {
	return feedbackOnPartiallyCorrect;
    }

    public void setFeedbackOnPartiallyCorrect(String feedbackOnPartiallyCorrect) {
	this.feedbackOnPartiallyCorrect = feedbackOnPartiallyCorrect;
    }

    public String getFeedbackOnIncorrect() {
	return feedbackOnIncorrect;
    }

    public void setFeedbackOnIncorrect(String feedbackOnIncorrect) {
	this.feedbackOnIncorrect = feedbackOnIncorrect;
    }

    public boolean isShuffle() {
	return shuffle;
    }

    public void setShuffle(boolean shuffle) {
	this.shuffle = shuffle;
    }

    public boolean isCaseSensitive() {
	return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
	this.caseSensitive = caseSensitive;
    }

    public boolean getCorrectAnswer() {
	return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
	this.correctAnswer = correctAnswer;
    }

    public boolean isAllowRichEditor() {
	return allowRichEditor;
    }

    public void setAllowRichEditor(boolean allowRichEditor) {
	this.allowRichEditor = allowRichEditor;
    }

    /**
     * maxWordsLimit set in author. Used only for essay type of questions
     */
    public int getMaxWordsLimit() {
	return maxWordsLimit;
    }

    /**
     * @param maxWordsLimit
     *            set in author. Used only for essay type of questions
     */
    public void setMaxWordsLimit(int maxWordsLimit) {
	this.maxWordsLimit = maxWordsLimit;
    }

    /**
     * minWordsLimit set in author. Used only for essay type of questions
     */
    public int getMinWordsLimit() {
	return minWordsLimit;
    }

    /**
     * @param minWordsLimit
     *            set in author. Used only for essay type of questions
     */
    public void setMinWordsLimit(int minWordsLimit) {
	this.minWordsLimit = minWordsLimit;
    }

    public boolean isHedgingJustificationEnabled() {
	return hedgingJustificationEnabled;
    }

    public void setHedgingJustificationEnabled(boolean hedgingJustificationEnabled) {
	this.hedgingJustificationEnabled = hedgingJustificationEnabled;
    }

    public boolean isAutocompleteEnabled() {
	return autocompleteEnabled;
    }

    public void setAutocompleteEnabled(boolean autocompleteEnabled) {
	this.autocompleteEnabled = autocompleteEnabled;
    }

    public boolean isPrefixAnswersWithLetters() {
	return prefixAnswersWithLetters;
    }

    public void setPrefixAnswersWithLetters(boolean prefixAnswersWithLetters) {
	this.prefixAnswersWithLetters = prefixAnswersWithLetters;
    }

    public List<QbOption> getQbOptions() {
	return qbOptions;
    }

    public void setQbOptions(List<QbOption> options) {
	this.qbOptions = options;
    }

    public List<QbQuestionUnit> getUnits() {
	return units;
    }

    public void setUnits(List<QbQuestionUnit> units) {
	this.units = units;
    }

    public Map<Integer, Long> getVersionMap() {
	return versionMap;
    }

    public void setVersionMap(Map<Integer, Long> otherVersions) {
	this.versionMap = otherVersions;
    }
}