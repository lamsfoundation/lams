/*
 * Created on Dec 3, 2004
 */
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.List;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;

/**
 * @author MMINHAS 
 */
public class ActivityDAO extends BaseDAO implements IActivityDAO {
	
	private static final String FIND_BY_PARENT = "from lams_learning_activity activity where activity.parent_activity_id=?" ;	
	private static final String FIND_BY_LEARNING_DESIGN_ID ="from lams_learning_activity activity where activity.learning_design_id=?" ;

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IActivityDAO#getActivityById(java.lang.Long)
	 */
	public Activity getActivityById(Long activityId) {
		return (Activity) super.find(Activity.class,activityId);		
	}

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IActivityDAO#getActivityByParentActivityId(java.lang.Long)
	 */
	public Activity getActivityByParentActivityId(Long parentActivityId) {
		List list = this.getHibernateTemplate().find(FIND_BY_PARENT,parentActivityId);
		if(list.size()==0)
			return null;
		else
			return (Activity) list.get(0);
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

}
