package org.lamsfoundation.lams.tool.notebook.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.TextSearchCondition;
import org.lamsfoundation.lams.learningdesign.dto.TextSearchConditionDTO;
import org.lamsfoundation.lams.tool.notebook.dto.NotebookConditionDTO;

/**
 * Basically the same as {@link TextSearchCondition}. No new functionality was required.
 *
 * @author Marcin Cieslak
 *
 */
@Entity
@Table(name = "tl_lantbk11_conditions")
public class NotebookCondition extends TextSearchCondition {

    public NotebookCondition() {
	super();
    }

    public NotebookCondition(TextSearchConditionDTO conditionDTO) {
	super(conditionDTO);
    }

    public NotebookCondition(Long conditionId, Integer conditionUIID, Integer orderId, String name, String displayName,
	    String allWords, String phrase, String anyWords, String excludedWords) {
	super(conditionId, conditionUIID, orderId, name, displayName, BranchCondition.OUTPUT_TYPE_STRING, null, null,
		null, allWords, phrase, anyWords, excludedWords);
    }

    @Override
    public Object clone() {
	return new NotebookCondition(null, null, orderId, name, displayName, allWords, phrase, anyWords, excludedWords);
    }

    @Override
    public NotebookCondition clone(int uiidOffset) {
	Integer newConditionUIID = LearningDesign.addOffset(conditionUIID, uiidOffset);
	return new NotebookCondition(null, newConditionUIID, orderId, name, displayName, allWords, phrase, anyWords,
		excludedWords);
    }

    @Override
    public NotebookConditionDTO getBranchConditionDTO(Integer toolActivityUIID) {
	return new NotebookConditionDTO(this, toolActivityUIID);
    }
}