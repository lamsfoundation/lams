package org.lamsfoundation.lams.tool.notebook.model;

import org.lamsfoundation.lams.learningdesign.TextSearchCondition;
import org.lamsfoundation.lams.learningdesign.dto.BranchConditionDTO;

public class NotebookCondition extends TextSearchCondition {

    public NotebookCondition() {
	super();
    }

    public NotebookCondition(BranchConditionDTO conditionDTO) {
	super(conditionDTO);
    }

    public NotebookCondition(Long conditionId, Integer conditionUIID, Integer orderId, String name, String displayName,
	    String type, String startValue, String endValue, String exactMatchValue, String allWords, String phrase,
	    String anyWords, String excludedWords) {
	super(conditionId, conditionUIID, orderId, name, displayName, type, startValue, endValue, exactMatchValue,
		allWords, phrase, anyWords, excludedWords);
    }
}