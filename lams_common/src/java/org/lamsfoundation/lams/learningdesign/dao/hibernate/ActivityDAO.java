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

import org.hibernate.Query;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.PermissionGateActivity;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.SynchGateActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.springframework.dao.DataRetrievalFailureException;

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
		Activity act = (Activity) super.find(Activity.class,activityId);
		return getNonCGLibActivity(act);		
	}

	/** we must return the real activity, not a Hibernate proxy. So relook
	* it up. This should be quick as it should be in the cache.
	*/
	private Activity getNonCGLibActivity(Activity act) {
		if ( act != null ) {
			Integer activityType = act.getActivityTypeId();
			Long activityId = act.getActivityId();
			if ( activityType != null ) {
				switch ( activityType.intValue() ) {
					case Activity.TOOL_ACTIVITY_TYPE: 
							return getActivityByActivityId(activityId,ToolActivity.class);
					case Activity.GROUPING_ACTIVITY_TYPE: 
							return getActivityByActivityId(activityId,GroupingActivity.class);
					case Activity.SYNCH_GATE_ACTIVITY_TYPE: 
							return getActivityByActivityId(activityId,SynchGateActivity.class);
					case Activity.SCHEDULE_GATE_ACTIVITY_TYPE: 
							return getActivityByActivityId(activityId,ScheduleGateActivity.class);
					case Activity.PERMISSION_GATE_ACTIVITY_TYPE: 
							return getActivityByActivityId(activityId,PermissionGateActivity.class);
					case Activity.PARALLEL_ACTIVITY_TYPE: 
							return getActivityByActivityId(activityId,ParallelActivity.class);
					case Activity.OPTIONS_ACTIVITY_TYPE: 
							return getActivityByActivityId(activityId,OptionsActivity.class);
					case Activity.SEQUENCE_ACTIVITY_TYPE: 
							return getActivityByActivityId(activityId,SequenceActivity.class);
					default: break; 
				}
			}
			throw new DataRetrievalFailureException("Unable to get activity as the activity type is unknown or missing. Activity type is "+activityType);
		}
		return null;
	}

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IActivityDAO#getActivityById(java.lang.Long)
	 */
	public Activity getActivityByActivityId(Long activityId, Class clasz) {
		return (Activity) super.find(clasz,activityId);
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
	 * @see org.lamsfoundation.lams.learningdesign.dao.IActivityDAO#getActivityByUIID(java.lang.Integer, org.lamsfoundation.lams.learningdesign.LearningDesign)
	 */
	public Activity getActivityByUIID(Integer id, LearningDesign design) {
		if ( id != null && design != null ) {
			Long designID = design.getLearningDesignId();
			Query query = this.getSession().createQuery(FIND_BY_UI_ID);
			query.setInteger(0,id.intValue());
			query.setLong(1,designID.longValue());
			return getNonCGLibActivity((Activity) query.uniqueResult());
		}
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
