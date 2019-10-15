/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.qa.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;

/**
 * Holds question content within a particular content
 *
 * @author Ozgur Demirtas
 */
@Entity
@Table(name = "tl_laqa11_que_content")
//in this entity's table primary key is "uid", but it references "tool_question_uid" in lams_qb_tool_question
@PrimaryKeyJoinColumn(name = "uid")
public class QaQueContent extends QbToolQuestion implements Serializable {

    private static final long serialVersionUID = -4028785701106936621L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qa_content_id")
    private QaContent qaContent;

    public QaQueContent() {
    }

    public QaQueContent(QbQuestion qbQuestion, int displayOrder, QaContent qaContent) {
	this.qbQuestion = qbQuestion;
	this.qaContent = qaContent;
	this.toolContentId = qaContent == null ? null : qaContent.getQaContentId();
	this.displayOrder = displayOrder;
    }

    public static QaQueContent newInstance(QaQueContent queContent, QaContent newQaContent) {
	QaQueContent newQueContent = new QaQueContent(queContent.getQbQuestion(), queContent.getDisplayOrder(),
		newQaContent);
	return newQueContent;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid: ", getUid()).append("name: ", getQbQuestion().getName())
		.append("displayOrder: ", getDisplayOrder()).toString();
    }

    public QaContent getQaContent() {
	return qaContent;
    }

    public void setQaContent(QaContent qaContent) {
	this.qaContent = qaContent;
	this.toolContentId = qaContent == null ? null : qaContent.getQaContentId();
    }
}