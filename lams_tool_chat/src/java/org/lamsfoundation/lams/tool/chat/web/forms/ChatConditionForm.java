package org.lamsfoundation.lams.tool.chat.web.forms;

import org.lamsfoundation.lams.tool.chat.model.ChatCondition;
import org.lamsfoundation.lams.web.form.TextSearchForm;

/**
 * A text search form with additional parameters for Chat needs.
 *
 * @author Marcin Cieslak
 *
 */
public class ChatConditionForm extends TextSearchForm {

    private Integer orderId;
    private String displayName;

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String name) {
	displayName = name;
    }

    public ChatConditionForm() {
    }

    public void populateForm(ChatCondition condition) {
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
     * Fills a new ChatCondition with data contained in this form. Note that some cruicial data is missing, so the
     * condition is NOT complete.
     *
     * @return created condition
     */
    public ChatCondition extractCondition() {
	return new ChatCondition(null, null, getOrderId(), null, getDisplayName(), getAllWords(), getPhrase(),
		getAnyWords(), getExcludedWords());
    }

    public void extractCondition(ChatCondition condition) {
	condition.setOrderId(getOrderId());
	condition.setDisplayName(getDisplayName());
	condition.setAllWords(getAllWords());
	condition.setPhrase(getPhrase());
	condition.setAnyWords(getAnyWords());
	condition.setExcludedWords(getExcludedWords());
    }
}