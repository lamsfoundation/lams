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
 
/* $Id$ */ 
package org.lamsfoundation.lams.tool.qa;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;

/**
 * This class maps to a single cognitive skill in the q&a wizard, it 
 * contains a set of questions
 * 
 * @hibernate.class table="tl_laqa11_wizard_cognitive_skill"
 */ 
public class QaWizardCognitiveSkill implements Serializable, Comparable<QaWizardCognitiveSkill> {
    
    public static final long serialVersionUID = 6732784345784895744L;
    
    private Long uid;
    private String title;
    private QaWizardCategory category;
    private Set<QaWizardQuestion> questions;
    
    public QaWizardCognitiveSkill() {
    }

    public QaWizardCognitiveSkill(Long uid, String title, QaWizardCategory category, SortedSet<QaWizardQuestion> questions) {
	super();
	this.uid = uid;
	this.title = title;
	this.category = category;
	this.questions = questions;
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * 
     */
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * @hibernate.property column="title" length="255" not-null="true"
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @hibernate.many-to-one cascade="none"
     * 		class="org.lamsfoundation.lams.tool.qa.QaWizardCategory"
     *          column="category_uid"
     * 
     */
    public QaWizardCategory getCategory() {
        return category;
    }

    public void setCategory(QaWizardCategory category) {
        this.category = category;
    }

    /**
     * @hibernate.set lazy="true" inverse="false" cascade="all-delete-orphan"
     *                order-by="uid asc"
     * @hibernate.collection-key column="cognitive_skill_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.qa.QaWizardQuestion"
     * 
     */
    public Set<QaWizardQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QaWizardQuestion> questions) {
        this.questions = questions;
    }
    
    public int compareTo(QaWizardCognitiveSkill skill) {
	if (skill.getUid()!=null && uid != null)
	{
	    return skill.getUid().compareTo(uid) * -1;
	}
	else
	{
	    return 1;
	}
    }
}
 