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
package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.qa.QaWizardCategory;
import org.lamsfoundation.lams.tool.qa.QaWizardCognitiveSkill;
import org.lamsfoundation.lams.tool.qa.QaWizardQuestion;
import org.lamsfoundation.lams.tool.qa.dao.IQaWizardDAO;

/**
 * 
 * @author edited by lfoxton
 * 
 */
public class QaWizardDAO extends BaseDAO implements IQaWizardDAO {

    private static final String QUERY_FIND_ALL_CATEGORIES = "from " + QaWizardCategory.class.getName();
    private static final String QUERY_GET_CATEGORY = "from " + QaWizardCategory.class.getName() + " c where c.uid=?";
    private static final String QUERY_GET_SKILL = "from " + QaWizardCognitiveSkill.class.getName() + " c where c.uid=?";
    private static final String QUERY_GET_QUESTION = "from " + QaWizardQuestion.class.getName() + " q where q.uid=?";

    public void saveOrUpdateCategories(SortedSet<QaWizardCategory> categories) {
	if (categories != null) {
	    for (QaWizardCategory category : categories) {
		getHibernateTemplate().saveOrUpdate(category);
	    }
	}
    }

    @SuppressWarnings("unchecked")
    public SortedSet<QaWizardCategory> getWizardCategories() {
	SortedSet<QaWizardCategory> ret = new TreeSet<QaWizardCategory>();
	List<QaWizardCategory> list = (List<QaWizardCategory>) getHibernateTemplate().find(QUERY_FIND_ALL_CATEGORIES);
	for (QaWizardCategory category : list) {
	    ret.add(category);
	}
	return ret;
    }

    public void deleteWizardCategoryByUID(Long uid) {
	QaWizardCategory cat = getWizardCategoryByUID(uid);
	if (cat != null)
	{
	    getHibernateTemplate().delete(cat);
	}
    }

    public void deleteWizardSkillByUID(Long uid){
	QaWizardCognitiveSkill skill = getWizardSkillByUID(uid);
	if (skill != null)
	{
	    getHibernateTemplate().delete(skill);
	}
    }
    
    public void deleteWizardQuestionByUID(Long uid){
	QaWizardQuestion question = getWizardQuestionByUID(uid);
	if (question != null)
	{
	    getHibernateTemplate().delete(question);
	}
    }

    @SuppressWarnings("unchecked")
    public QaWizardCategory getWizardCategoryByUID(Long uid) {
	List result = getHibernateTemplate().find(QUERY_GET_CATEGORY, uid);
	if (result.size() > 0)
	{
	    return (QaWizardCategory)result.get(0);
	}
	else
	{
	    return null;
	}
    }

    @SuppressWarnings("unchecked")
    public QaWizardCognitiveSkill getWizardSkillByUID(Long uid){
	List result = getHibernateTemplate().find(QUERY_GET_SKILL, uid);
	if (result.size() > 0)
	{
	    return (QaWizardCognitiveSkill)result.get(0);
	}
	else
	{
	    return null;
	}
    }
    
    @SuppressWarnings("unchecked")
    public QaWizardQuestion getWizardQuestionByUID(Long uid){
	
	List result = getHibernateTemplate().find(QUERY_GET_QUESTION, uid);
	if (result.size() > 0)
	{
	    return (QaWizardQuestion)result.get(0);
	}
	else
	{
	    return null;
	}
    }
    
    public void deleteAllWizardCategories()
    {
	this.getHibernateTemplate().deleteAll(getWizardCategories());
    }

}
