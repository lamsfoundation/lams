package org.lamsfoundation.lams.tool.notebook.web.forms;

import org.lamsfoundation.lams.tool.notebook.model.NotebookCondition;
import org.lamsfoundation.lams.web.form.TextSearchForm;

/**
 * A text search form with additional parameters for Notebook needs.
 *
 * @author Marcin Cieslak
 *
 */
public class NotebookConditionForm extends TextSearchForm {

    private Integer orderId;
    private String displayName;

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String name) {
	displayName = name;
    }

    public NotebookConditionForm() {
    }

    public void populateForm(NotebookCondition condition) {
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
     * Fills a new NotebookCondition with data contained in this form. Note that some cruicial data is missing, so the
     * condition is NOT complete.
     *
     * @return created condition
     */
    public NotebookCondition extractCondition() {
	return new NotebookCondition(null, null, getOrderId(), null, getDisplayName(), getAllWords(), getPhrase(),
		getAnyWords(), getExcludedWords());
    }

    public void extractCondition(NotebookCondition condition) {
	condition.setOrderId(getOrderId());
	condition.setDisplayName(getDisplayName());
	condition.setAllWords(getAllWords());
	condition.setPhrase(getPhrase());
	condition.setAnyWords(getAnyWords());
	condition.setExcludedWords(getExcludedWords());
    }
}