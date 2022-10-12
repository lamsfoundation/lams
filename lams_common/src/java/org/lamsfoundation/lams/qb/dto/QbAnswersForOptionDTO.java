package org.lamsfoundation.lams.qb.dto;

import java.util.LinkedList;
import java.util.List;

public class QbAnswersForOptionDTO {
    private long qbQuestionUid;
    private int displayOrder;

    private Integer correctAnswerPercent;
    private List<Integer> optionAnswerPercent;

    public QbAnswersForOptionDTO(long qbQuestionUid, int displayOrder) {
	this.qbQuestionUid = qbQuestionUid;
	this.displayOrder = displayOrder;
	this.correctAnswerPercent = -1;
	this.optionAnswerPercent = new LinkedList<>();
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

    public List<Integer> getOptionAnswerPercent() {
	return optionAnswerPercent;
    }
}