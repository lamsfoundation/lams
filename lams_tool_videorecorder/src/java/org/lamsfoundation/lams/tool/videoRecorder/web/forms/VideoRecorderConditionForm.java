package org.lamsfoundation.lams.tool.videoRecorder.web.forms;

import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderCondition;
import org.lamsfoundation.lams.web.TextSearchActionForm;

/**
 * A text search form with additional parameters for VideoRecorder needs.
 *
 * @author Marcin Cieslak
 * @struts.form name="videoRecorderConditionForm"
 */
public class VideoRecorderConditionForm extends TextSearchActionForm {

    private Integer orderId;
    private String displayName;

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String name) {
	displayName = name;
    }

    public VideoRecorderConditionForm() {
    }

    public void populateForm(VideoRecorderCondition condition) {
	super.populateForm(condition);
	setOrderId(condition.getOrderId());
	setDisplayName(condition.getDisplayName());
    }

    public Integer getOrderId() {
	return orderId;
    }

    public void setOrderId(Integer orderId) {
	this.orderId = orderId;
    }

    /**
     * Fills a new VideoRecorderCondition with data contained in this form. Note that some cruicial data is missing, so
     * the
     * condition is NOT complete.
     *
     * @return created condition
     */
    public VideoRecorderCondition extractCondition() {
	return new VideoRecorderCondition(null, null, getOrderId(), null, getDisplayName(), getAllWords(), getPhrase(),
		getAnyWords(), getExcludedWords());
    }

    public void extractCondition(VideoRecorderCondition condition) {
	condition.setOrderId(getOrderId());
	condition.setDisplayName(getDisplayName());
	condition.setAllWords(getAllWords());
	condition.setPhrase(getPhrase());
	condition.setAnyWords(getAnyWords());
	condition.setExcludedWords(getExcludedWords());
    }
}
