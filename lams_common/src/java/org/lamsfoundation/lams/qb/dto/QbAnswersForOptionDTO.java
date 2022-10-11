package org.lamsfoundation.lams.qb.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class QbAnswersForOptionDTO {
    private long qbQuestionUid;
    private int displayOrder;

    private Integer correctAnswerPercent;
    private Map<Long, Integer> optionAnswerPercent;

    public QbAnswersForOptionDTO(long qbQuestionUid, int displayOrder) {
	this.qbQuestionUid = qbQuestionUid;
	this.displayOrder = displayOrder;
	this.correctAnswerPercent = -1;
	this.optionAnswerPercent = new LinkedHashMap<>();
    }

    public int getCorrectAnswerPercent() {
	return correctAnswerPercent;
    }

    public void setCorrectAnswerPercent(int correctAnswerPercent) {
	this.correctAnswerPercent = correctAnswerPercent;
    }

    public long getQbQuestionUid() {
	return qbQuestionUid;
    }

    public int getDisplayOrder() {
	return displayOrder;
    }

    public Map<Long, Integer> getOptionAnswerPercent() {
	return optionAnswerPercent;
    }
}