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

package org.lamsfoundation.lams.planner;

import org.lamsfoundation.lams.util.MessageService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * The base for any Pedagogical Planner Action Forms that other activities. All Java forms need to inherit from this
 * class and forms on JSP pages need to have these properties declared as hidden elements.
 *
 * @author Marcin Cieslak
 *
 */
public abstract class PedagogicalPlannerActivitySpringForm {
    /**
     * Set when opening a Learning Design.
     */
    private Integer activityOrderNumber;
    private Long toolContentID;
    /**
     * Set in {@link #validate()} method.
     */
    private Boolean valid = true;
    /**
     * Set when submitting form in base.jsp page.
     */
    private Integer callID;

    /**
     * Only to save. It is loaded straight from the tool.
     */
    private String editingAdvice;

    public Long getToolContentID() {
	return toolContentID;
    }

    public void setToolContentID(Long toolContentID) {
	this.toolContentID = toolContentID;
    }

    public Boolean getValid() {
	return valid;
    }

    public void setValid(Boolean valid) {
	this.valid = valid;
    }

    public Integer getCallID() {
	return callID;
    }

    public void setCallID(Integer callID) {
	this.callID = callID;
    }

    public void reset() {
	setValid(true);
    }

    /**
     * Validates form. Must set {@link #valid} property. Can be overriden by inheriting classes, although call to this
     * method is only in activities themselves (and not on a higher level like lams_central).
     *
     * @return
     */
    public MultiValueMap<String, String> validate(MessageService messageService) {
	setValid(true);
	return new LinkedMultiValueMap<>();
    }

    public Integer getActivityOrderNumber() {
	return activityOrderNumber;
    }

    public void setActivityOrderNumber(Integer activityOrderNumber) {
	this.activityOrderNumber = activityOrderNumber;
    }

    public String getEditingAdvice() {
	return editingAdvice;
    }

    public void setEditingAdvice(String editingAdvice) {
	this.editingAdvice = editingAdvice;
    }
}