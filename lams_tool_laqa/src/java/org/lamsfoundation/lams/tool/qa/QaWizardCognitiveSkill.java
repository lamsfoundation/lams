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
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * This class maps to a single cognitive skill in the q&a wizard, it contains a
 * set of questions
 *
 *
 */
public class QaWizardCognitiveSkill implements Serializable, Comparable<QaWizardCognitiveSkill>, Cloneable {

    public static final long serialVersionUID = 6732784345784895744L;
    private static Logger logger = Logger.getLogger(QaWizardCognitiveSkill.class.getName());

    private Long uid;
    private String title;
    private QaWizardCategory category;
    private Set<QaWizardQuestion> questions;

    public QaWizardCognitiveSkill() {
    }

    public QaWizardCognitiveSkill(Long uid, String title, QaWizardCategory category,
	    SortedSet<QaWizardQuestion> questions) {
	super();
	this.uid = uid;
	this.title = title;
	this.category = category;
	this.questions = questions;
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
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     *
     *
     *
     *
     *
     */
    public QaWizardCategory getCategory() {
	return category;
    }

    public void setCategory(QaWizardCategory category) {
	this.category = category;
    }

    /**
     *
     *
     *
     *
     *
     */
    public Set<QaWizardQuestion> getQuestions() {
	return questions;
    }

    public void setQuestions(Set<QaWizardQuestion> questions) {
	this.questions = questions;
    }

    @Override
    public int compareTo(QaWizardCognitiveSkill skill) {
	if (skill.getUid() != null && uid != null) {
	    return skill.getUid().compareTo(uid) * -1;
	} else {
	    return 1;
	}
    }

    @Override
    public Object clone() {

	QaWizardCognitiveSkill skill = null;
	try {
	    skill = (QaWizardCognitiveSkill) super.clone();
	    skill.setUid(null);
	    skill.setCategory(null);
	    skill.setTitle(getTitle());
	    Set<QaWizardQuestion> newQuestions = new TreeSet<QaWizardQuestion>();

	    if (questions != null) {
		// create a copy of the skills
		for (QaWizardQuestion question : questions) {
		    QaWizardQuestion newQuestion = (QaWizardQuestion) question.clone();
		    newQuestions.add(newQuestion);
		}
		skill.setQuestions(newQuestions);
	    }
	} catch (CloneNotSupportedException cnse) {
	    logger.error("Error cloning " + QaWizardCognitiveSkill.class, cnse);
	}
	return skill;
    }
}
