package org.lamsfoundation.lams.learningdesign.dao;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupingDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningLibraryDAO;
import org.lamsfoundation.lams.learningdesign.*;

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
public class TestActivityDAO extends AbstractLamsTestCase {
	
	protected ActivityDAO activityDAO;
	protected Activity activity;
	protected LearningDesignDAO learningDesignDAO;
	protected LearningLibraryDAO learningLibraryDAO;
	protected GroupingDAO groupingDAO;

	/**
	 * @param name
	 */
	public TestActivityDAO(String name) {
		super(name);
	}
	
	
	public void setUp() throws Exception {
		super.setUp();
		activityDAO =(ActivityDAO) context.getBean("activityDAO");
		learningLibraryDAO =(LearningLibraryDAO)context.getBean("learningLibraryDAO");
		learningDesignDAO =(LearningDesignDAO)context.getBean("learningDesignDAO");
		groupingDAO = (GroupingDAO)context.getBean("groupingDAO");
	}
	/*public void testGetActivitiesByParentActivityId() {
		List list = activityDAO.getActivitiesByParentActivityId(new Long(14));
		System.out.print("SIZE:" + list.size());
	}*/
	public void test(){
		activity = activityDAO.getActivityByActivityId(new Long(18));
		Transition transition = activity.getTransitionTo();
		System.out.println("Transition TO:" + transition.getTransitionId());
		transition = activity.getTransitionFrom();
		System.out.println("Transition FROM:" + transition.getActivityByToActivityId());
	}
	public void testGetFirstDesignActivity(){
		Activity activity = activityDAO.getActivityByUIID(new Integer(3),learningDesignDAO.getLearningDesignById(new Long(1)));
		assertNotNull(activity.getTitle());
		System.out.println("ActivityID: " + activity.getActivityId());
	}
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
	 */
	protected String[] getContextConfigLocation() {
		return new String[] {"/org/lamsfoundation/lams/learningdesign/learningDesignApplicationContext.xml",
		 "applicationContext.xml"};
	}

	/*protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetActivityById() {
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
	/*public void testInsertActivity() {
		OptionsActivity optActivity = new OptionsActivity();		
		
		optActivity.setOrderId(new Integer(123));
		optActivity.setDefineLater(new Boolean(false));
		optActivity.setDescription("Optional Activity Description");
		optActivity.setTitle("Optional Activity Title");
		optActivity.setActivityTypeId(new Integer(8));
		
		LearningDesign design = learningDesignDAO.getLearningDesignById(new Long(1));
		optActivity.setLearningDesign(design);
		
		LearningLibrary library = learningLibraryDAO.getLearningLibraryById(new Long(1));
		optActivity.setLearningLibrary(library);
		
		optActivity.setCreateDateTime(new Date());
		optActivity.setParentActivity(activityDAO.getActivityById(new Long(1)));
		
		activityDAO.insertActivity(optActivity);
		//activity.setOrderId(new Integer(2));
		//assertNotNull("Primary Key assigned",optActivity.getActivityId());		
	}*/
	/*public void testInsertOptionsActivity(){
		OptionsActivity options = new OptionsActivity();
		
		options.setId(new Integer(26));
		//options.setTitle("Trial Options Activity");
		//options.setDescription("This is an Optional Activity");
		options.setDefineLater(new Boolean(false));
		options.setActivityTypeId(new Integer(8));
		options.setCreateDateTime(new Date());
		options.setMaxNumberOfOptions(new Integer(5));
		options.setMinNumberOfOptions(new Integer(3));
		
		/*LearningDesign design = learningDesignDAO.getLearningDesignById(new Long(1));
		options.setLearningDesign(design);
		
		LearningLibrary library = learningLibraryDAO.getLearningLibraryById(new Long(1));
		options.setLearningLibrary(library);
		
		options.setLibraryActivityUiImage("image");
		options.setOfflineInstructions("Offline Instructions for Optional Activity");
		options.setXcoord(new Integer(0));
		options.setYcoord(new Integer(0));
		options.setOrderId(new Integer(1));
		options.setParentActivity(activityDAO.getActivityById(new Long(1)));
		options.setGrouping(groupingDAO.getGroupingById(new Long(1)));
		activityDAO.insertOptActivity(options);
		//activityDAO.insertActivity(options);
		
	}*/

	/*public void testUpdateActivity() {
	}

	public void testDeleteActivity() {
	}*/

}

