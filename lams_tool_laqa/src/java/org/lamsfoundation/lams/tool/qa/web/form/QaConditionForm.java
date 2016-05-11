/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.qa.web.form;

import org.apache.struts.util.LabelValueBean;
import org.lamsfoundation.lams.tool.qa.QaCondition;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.web.TextSearchActionForm;

/**
 * A text search form with additional parameters for Q&A needs.
 *
 * @author Marcin Cieslak
 *
 */
public class QaConditionForm extends TextSearchActionForm {
    /**
     * Names of the questions that could be selected by a user.
     */
    private LabelValueBean[] possibleItems;
    /**
     * Numbers of questions that were selected by a user.
     */
    private Integer[] selectedItems;
    private Integer orderId;
    private String displayName;

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String name) {
	displayName = name;
    }

    public QaConditionForm() {
    }

    public void populateForm(QaCondition condition) {
	super.populateForm(condition);
	setOrderId(condition.getOrderId());
	setDisplayName(condition.getDisplayName());
	Integer[] selectedItems = new Integer[condition.temporaryQuestionDTOSet.size()];
	int questionIndex = 0;
	for (QaQuestionDTO question : condition.temporaryQuestionDTOSet) {
	    selectedItems[questionIndex] = new Integer(question.getDisplayOrder());
	    questionIndex++;
	}
	setSelectedItems(selectedItems);
    }

    public LabelValueBean[] getPossibleItems() {
	return possibleItems;
    }

    public void setPossibleItems(LabelValueBean[] possibleItems) {
	this.possibleItems = possibleItems;
    }

    public Integer[] getSelectedItems() {
	return selectedItems;
    }

    public void setSelectedItems(Integer[] selectedItems) {
	this.selectedItems = selectedItems;
    }

    public Integer getOrderId() {
	return orderId;
    }

    public void setOrderId(Integer orderId) {
	this.orderId = orderId;
    }

    /**
     * Fills a new QaCondition with data contained in this form. Note that some cruicial data is missing, so the
     * condition is NOT complete.
     *
     * @return created condition
     */
    public QaCondition extractCondition() {
	return new QaCondition(null, null, getOrderId(), null, getDisplayName(), getAllWords(), getPhrase(),
		getAnyWords(), getExcludedWords(), null);
    }

    public void extractCondition(QaCondition condition) {
	condition.setOrderId(getOrderId());
	condition.setDisplayName(getDisplayName());
	condition.setAllWords(getAllWords());
	condition.setPhrase(getPhrase());
	condition.setAnyWords(getAnyWords());
	condition.setExcludedWords(getExcludedWords());
    }
}