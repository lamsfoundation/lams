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

package org.lamsfoundation.lams.tool.forum.web.forms;

import java.util.Map;

import org.lamsfoundation.lams.tool.forum.model.ForumCondition;
import org.lamsfoundation.lams.tool.forum.model.Message;
import org.lamsfoundation.lams.web.form.TextSearchForm;

/**
 * A text search form with additional parameters for Forum needs.
 *
 *
 * @author Marcin Cieslak
 *
 */
public class ForumConditionForm extends TextSearchForm {
    /**
     * Names of the topics that could be selected by a user.
     */
    private Map<String, String> possibleItems;
    /**
     * Dates of creation of topics that were selected by a user.
     */
    private Long[] selectedItems;
    private Integer orderId;
    private String displayName;

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String name) {
	displayName = name;
    }

    public ForumConditionForm() {
    }

    public void populateForm(ForumCondition condition) {
	super.populateForm(condition);
	setOrderId(condition.getOrderId());
	setDisplayName(condition.getDisplayName());
	Long[] selectedItems = new Long[condition.getTopics().size()];
	int questionIndex = 0;
	for (Message topic : condition.getTopics()) {
	    selectedItems[questionIndex] = topic.getCreated().getTime();
	    questionIndex++;
	}
	setSelectedItems(selectedItems);
    }

    public Map<String, String> getPossibleItems() {
	return possibleItems;
    }

    public void setPossibleItems(Map<String, String> possibleItems) {
	this.possibleItems = possibleItems;
    }

    public Long[] getSelectedItems() {
	return selectedItems;
    }

    public void setSelectedItems(Long[] selectedItems) {
	this.selectedItems = selectedItems;
    }

    public Integer getOrderId() {
	return orderId;
    }

    public void setOrderId(Integer orderId) {
	this.orderId = orderId;
    }

    /**
     * Fills a new ForumCondition with data contained in this form. Note that some cruicial data is missing, so the
     * condition is NOT complete.
     *
     * @return created condition
     */
    public ForumCondition extractCondition() {
	return new ForumCondition(null, null, getOrderId(), null, getDisplayName(), getAllWords(), getPhrase(),
		getAnyWords(), getExcludedWords(), null);
    }

    public void extractCondition(ForumCondition condition) {
	condition.setOrderId(getOrderId());
	condition.setDisplayName(getDisplayName());
	condition.setAllWords(getAllWords());
	condition.setPhrase(getPhrase());
	condition.setAnyWords(getAnyWords());
	condition.setExcludedWords(getExcludedWords());
    }
}