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
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * This class maps to a single category in the q&a wizard, it contains a set of
 * cognitive skills
 *
 *
 */
public class QaWizardCategory implements Serializable, Comparable<QaWizardCategory>, Cloneable {

    public static final long serialVersionUID = 1234165196523665452L;
    private static Logger logger = Logger.getLogger(QaWizardCategory.class.getName());

    private Long uid;
    private String title;
    private Set<QaWizardCognitiveSkill> cognitiveSkills;

    public QaWizardCategory() {
    }

    public QaWizardCategory(Long uid, String title, Set<QaWizardCognitiveSkill> cognitiveSkills) {
	super();
	this.uid = uid;
	this.title = title;
	this.cognitiveSkills = cognitiveSkills;
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
     */
    public Set<QaWizardCognitiveSkill> getCognitiveSkills() {
	return cognitiveSkills;
    }

    public void setCognitiveSkills(Set<QaWizardCognitiveSkill> cognitiveSkills) {
	this.cognitiveSkills = cognitiveSkills;
    }

    @Override
    public int compareTo(QaWizardCategory category) {
	if (category.getUid() != null && uid != null) {
	    return category.getUid().compareTo(uid) * -1;
	} else {
	    return 1;
	}
    }

    @Override
    public Object clone() {

	QaWizardCategory category = null;
	try {
	    category = (QaWizardCategory) super.clone();
	    category.setUid(null);
	    category.setTitle(getTitle());
	    Set<QaWizardCognitiveSkill> skills = new TreeSet<QaWizardCognitiveSkill>();

	    if (cognitiveSkills != null) {
		// create a copy of the skills
		for (QaWizardCognitiveSkill skill : cognitiveSkills) {
		    QaWizardCognitiveSkill newSkill = (QaWizardCognitiveSkill) skill.clone();
		    skills.add(newSkill);
		}
		category.setCognitiveSkills(skills);
	    }
	} catch (CloneNotSupportedException cnse) {
	    logger.error("Error cloning " + QaWizardCategory.class, cnse);
	}
	return category;
    }
}
