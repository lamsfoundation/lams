package org.lamsfoundation.lams.learningdesign.dao;

import java.util.Date;

import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.*;
import org.lamsfoundation.lams.learningdesign.BaseTestCase;
/*
 * Created on Dec 3, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author MMINHAS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestActivityDAO extends BaseTestCase {

	protected ActivityDAO activityDAO;
	protected Activity activity;
	
	public void setUp() throws Exception {
		super.setUp();
		activityDAO =(ActivityDAO) context.getBean("activityDAO");	
	}

	/*protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetActivityById() {
	}

	public void testGetActivityByParentActivityId() {
	}

	public void testGetAllActivities() {
	}

	public void testGetActivitiesByLearningDesignId() {
	}*/

	/*public void testInsertActivity() {
		OptionsActivity optActivity = new OptionsActivity();		
		
		optActivity.setDefineLater(new Boolean(false));		
		
		ActivityType activityType = new ActivityType();
		activityType.setLearningActivityTypeId(new Integer(8));
		optActivity.setActivityType(activityType);
		
		optActivity.setDescription("Optional Activity Description");
		optActivity.setTitle("Optional Activity Title");
		
		LearningDesign ld = new LearningDesign();
		ld.setLearningDesignId(new Long(1));
		optActivity.setLearningDesign(ld);
		
		LearningLibrary lb = new LearningLibrary();
		lb.setLearningLibraryId(new Long(1));
		optActivity.setLearningLibrary(lb);
		
		optActivity.setCreateDateTime(new Date());
		
		activityDAO.insertActivity(optActivity);
		//activity.setOrderId(new Integer(2));
		//assertNotNull("Primary Key assigned",optActivity.getActivityId());		
	}*/
	public void testInsertActivity() {
		OptionsActivity optActivity = new OptionsActivity();		
		
		optActivity.setDefineLater(new Boolean(false));		
		
		optActivity.setDescription("Optional Activity Description");
		optActivity.setTitle("Optional Activity Title");
		
		LearningDesign ld = new LearningDesign();
		ld.setLearningDesignId(new Long(1));
		optActivity.setLearningDesign(ld);
		
		LearningLibrary lb = new LearningLibrary();
		lb.setLearningLibraryId(new Long(1));
		optActivity.setLearningLibrary(lb);
		
		optActivity.setCreateDateTime(new Date());
		
		activityDAO.insertActivity(optActivity);
		//activity.setOrderId(new Integer(2));
		//assertNotNull("Primary Key assigned",optActivity.getActivityId());		
	}

	/*public void testUpdateActivity() {
	}

	public void testDeleteActivity() {
	}*/

}

