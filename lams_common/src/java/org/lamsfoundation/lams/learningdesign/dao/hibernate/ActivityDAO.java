/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;

/**
 * @author Manpreet Minhas 
 */
public class ActivityDAO extends BaseDAO implements IActivityDAO {
	
	private static final String TABLENAME ="lams_learning_activity";
		
	private static final String FIND_BY_PARENT = "from " + TABLENAME +" in class " + Activity.class.getName() + " where parent_activity_id=?" ;
	
	private static final String FIND_BY_LEARNING_DESIGN_ID = "from " + TABLENAME +
															 " in class " + Activity.class.getName() +
															 " where learning_design_id=?" ;
	private static final String FIND_BY_UI_ID ="from " + TABLENAME +
													  " in class " + Activity.class.getName() +
													  " where activity_ui_id=?" + " AND " + " learning_design_id=?" ;
	private static final String FIND_BY_LIBRARY_ID = "from " + TABLENAME +
													 " in class " + Activity.class.getName() +
													 " where learning_library_id=?" + " AND learning_design_id IS NULL";
	

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IActivityDAO#getActivityById(java.lang.Long)
	 */
	public Activity getActivityByActivityId(Long activityId) {
		return (Activity) super.find(Activity.class,activityId);		
	}

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IActivityDAO#getActivityByParentActivityId(java.lang.Long)
	 */
	public List getActivitiesByParentActivityId(Long parentActivityId) {
		List list = this.getHibernateTemplate().find(FIND_BY_PARENT,parentActivityId);
		return list;
	}

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IActivityDAO#getAllActivities()
	 */
	public List getAllActivities() {
		return super.findAll(Activity.class);						
	}

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IActivityDAO#getActivitiesByLearningDesignId(java.lang.Long)
	 */
	public List getActivitiesByLearningDesignId(Long learningDesignId) {
		return this.getHibernateTemplate().find(FIND_BY_LEARNING_DESIGN_ID,learningDesignId);
	}

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IActivityDAO#insertActivity(org.lamsfoundation.lams.learningdesign.Activity)
	 */
	public void insertActivity(Activity activity) {
		this.getHibernateTemplate().save(activity);
	}
	public void insertOptActivity(OptionsActivity activity) {
		this.getHibernateTemplate().save(activity);
	}


	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IActivityDAO#updateActivity(org.lamsfoundation.lams.learningdesign.Activity)
	 */
	public void updateActivity(Activity activity) {
		this.getHibernateTemplate().update(activity);
	}

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IActivityDAO#deleteActivity(org.lamsfoundation.lams.learningdesign.Activity)
	 */
	public void deleteActivity(Activity activity) {
		this.getHibernateTemplate().delete(activity);
	}

	/**
	 * @see org.lamsfoundation.lams.learningdesign.dao.IActivityDAO#getActivityByID(java.lang.Integer)
	 */
	public Activity getActivityByUIID(Integer id, LearningDesign design) {
		Long designID = design.getLearningDesignId();
		List list = this.getHibernateTemplate().find(FIND_BY_UI_ID,new Object[]{id,designID},new Type[]{Hibernate.INTEGER,Hibernate.LONG});
		if(list!=null && list.size()!=0)
			return (Activity) list.get(0);
		else
			return null;
	}
	/**
	 * @see org.lamsfoundation.lams.learningdesign.dao.IActivityDAO#getActivitiesByLibraryID(java.lang.Long)
	 */
	public List getActivitiesByLibraryID(Long libraryID){
		List list = this.getHibernateTemplate().find(FIND_BY_LIBRARY_ID,libraryID);
		return list;
	}
	
	/** @see org.lamsfoundation.lams.learningdesign.dao.IActivityDAO#getTemplateActivityByLibraryID(java.lang.Long) */
	public Activity getTemplateActivityByLibraryID(Long libraryID)
	{
		List list = this.getHibernateTemplate().find(FIND_BY_LIBRARY_ID,libraryID);
		return (list!= null && list.size()!=0) ? (Activity)list.get(0): null;
	}
}
