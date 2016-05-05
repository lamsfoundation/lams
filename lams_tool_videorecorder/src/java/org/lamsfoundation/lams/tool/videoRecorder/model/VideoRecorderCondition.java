package org.lamsfoundation.lams.tool.videoRecorder.model;

import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.TextSearchCondition;
import org.lamsfoundation.lams.learningdesign.dto.TextSearchConditionDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderConditionDTO;

/**
 * Basically the same as {@link TextSearchCondition}. No new functionality was required.
 *
 * @author Marcin Cieslak
 *
 */
public class VideoRecorderCondition extends TextSearchCondition {

    public VideoRecorderCondition() {
	super();
    }

    public VideoRecorderCondition(TextSearchConditionDTO conditionDTO) {
	super(conditionDTO);
    }

    public VideoRecorderCondition(Long conditionId, Integer conditionUIID, Integer orderId, String name,
	    String displayName, String allWords, String phrase, String anyWords, String excludedWords) {
	super(conditionId, conditionUIID, orderId, name, displayName, BranchCondition.OUTPUT_TYPE_STRING, null, null,
		null, allWords, phrase, anyWords, excludedWords);
    }

    @Override
    public Object clone() {
	return new VideoRecorderCondition(null, null, orderId, name, displayName, allWords, phrase, anyWords,
		excludedWords);
    }

    @Override
    public VideoRecorderCondition clone(int uiidOffset) {
	Integer newConditionUIID = LearningDesign.addOffset(conditionUIID, uiidOffset);
	return new VideoRecorderCondition(null, newConditionUIID, orderId, name, displayName, allWords, phrase,
		anyWords, excludedWords);
    }

    @Override
    public VideoRecorderConditionDTO getBranchConditionDTO(Integer toolActivityUIID) {
	return new VideoRecorderConditionDTO(this, toolActivityUIID);
    }
}
