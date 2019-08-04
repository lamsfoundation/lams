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

package org.lamsfoundation.lams.tool.qa.dto;

import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.learningdesign.dto.TextSearchConditionDTO;
import org.lamsfoundation.lams.tool.qa.model.QaCondition;
import org.lamsfoundation.lams.tool.qa.model.QaQueContent;
import org.lamsfoundation.lams.tool.qa.util.QaQueContentComparator;

public class QaConditionDTO extends TextSearchConditionDTO {
    private Set<QaQueContent> questions = new TreeSet<>(new QaQueContentComparator());

    public QaConditionDTO(QaCondition condition, Integer toolActivityUIID) {
	super(condition, toolActivityUIID);
	for (QaQueContent question : condition.getQuestions()) {
	    QaQueContent questionCopy = new QaQueContent(question.getQbQuestion(), question.getDisplayOrder(), null);
	    getQuestions().add(questionCopy);
	}
    }

    public Set<QaQueContent> getQuestions() {
	return questions;
    }

    public void setQuestions(Set<QaQueContent> questions) {
	this.questions = questions;
    }

    @Override
    public QaCondition getCondition() {
	return new QaCondition(this);
    }
}