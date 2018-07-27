/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.tool.qa;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * This class maps to a sample question in the q&a wizard, it has a parent
 * cognitive skill which in turn has a parent category
 *
 *
 */
public class QaWizardQuestion implements Serializable, Comparable<QaWizardQuestion>, Cloneable {

    public static final long serialVersionUID = 4353787904539453783L;
    private static Logger logger = Logger.getLogger(QaWizardQuestion.class.getName());

    private Long uid;
    private QaWizardCognitiveSkill cognitiveSkill;
    private String question;

    public QaWizardQuestion() {
    }

    public QaWizardQuestion(Long uid, QaWizardCognitiveSkill cognitiveSkill, String question) {
	super();
	this.uid = uid;
	this.cognitiveSkill = cognitiveSkill;
	this.question = question;
    }

    /**
     *
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     *
     *
     *
     *
     */
    public QaWizardCognitiveSkill getCognitiveSkill() {
	return cognitiveSkill;
    }

    public void setCognitiveSkill(QaWizardCognitiveSkill cognitiveSkill) {
	this.cognitiveSkill = cognitiveSkill;
    }

    /**
     *
     */
    public String getQuestion() {
	return question;
    }

    public void setQuestion(String question) {
	this.question = question;
    }

    @Override
    public int compareTo(QaWizardQuestion question) {
	if (question.getUid() != null && uid != null) {
	    return question.getUid().compareTo(uid) * -1;
	} else {
	    return 1;
	}
    }

    @Override
    public Object clone() {
	QaWizardQuestion question = null;
	try {
	    question = (QaWizardQuestion) super.clone();
	    question.setUid(null);
	    question.setCognitiveSkill(null);
	    question.setQuestion(getQuestion());
	} catch (CloneNotSupportedException cnse) {
	    logger.error("Error cloning " + QaWizardQuestion.class, cnse);
	}
	return question;
    }

}
