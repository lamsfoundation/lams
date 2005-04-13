package org.lamsfoundation.lams.learningdesign.dao;

import java.util.HashSet;
import java.util.List;

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
	protected LearningDesign learningDesign;

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
		learningDesign = learningDesignDAO.getLearningDesignById(new Long(1));
	}
	public void testGetActivitiesByParentActivityId() {
		List list = activityDAO.getActivitiesByParentActivityId(new Long(14));
		System.out.print("SIZE:" + list.size());
	}
	public void test(){
		activity = activityDAO.getActivityByActivityId(new Long(13));		
		Transition transition = activity.getTransitionTo();
		System.out.println("Transition TO:" + transition.getTransitionId());
		transition = activity.getTransitionFrom();
		System.out.println("Transition FROM:" + transition.getToActivity());
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
		return new String[] {"WEB-INF/spring/learningDesignApplicationContext.xml",
		 "WEB-INF/spring/applicationContext.xml"};
	}
	public void testgetActivityByLibraryID(){
		List activities = activityDAO.getActivitiesByLibraryID(new Long(1));
		assertNotNull(activities);
		System.out.println("SIZE:" + activities.size());
	}	
	public void testIsComplexActivity(){
		activity = activityDAO.getActivityByActivityId(new Long(14));
		boolean result = activity.isComplexActivity();
		assertTrue(result);		
	}
	public void testGetContributionType(){
		activity = activityDAO.getActivityByActivityId(new Long(18));
		HashSet set = activity.getContributionType();
		assertTrue(set.size()>0);
		
	}
	public void testCreateToolActivityCopy(){
		activity = activityDAO.getActivityByActivityId(new Long(20));		
		ToolActivity newToolActivity =null;
		if(activity.isToolActivity()){			
			newToolActivity = ToolActivity.createCopy((ToolActivity)activity);						
			activityDAO.insert(newToolActivity);
		}
		assertNotNull(newToolActivity.getActivityId());
	}
	public void testCreateGroupingActivityCopy(){
		activity = activityDAO.getActivityByActivityId(new Long(23));		
		GroupingActivity newGroupingActivity = null;
		if(activity.isGroupingActivity()){			
			newGroupingActivity = GroupingActivity.createCopy((GroupingActivity)activity);			
			activityDAO.insert(newGroupingActivity);
		}
		assertNotNull(newGroupingActivity.getActivityId());
	}
	public void testCreateOptionsActivityCopy(){
		activity = activityDAO.getActivityByActivityId(new Long(12));
		OptionsActivity optionsActivity =null;
		if(activity.getActivityTypeId().intValue()== Activity.OPTIONS_ACTIVITY_TYPE){
			optionsActivity = OptionsActivity.createCopy((OptionsActivity)activity);
			activityDAO.insert(optionsActivity);
		}
		assertNotNull(optionsActivity.getActivityId());		
	}
	public void testCreateParallelActivityCopy(){
		activity = activityDAO.getActivityByActivityId(new Long(13));
		ParallelActivity parallelActivity = null;
		if(activity.getActivityTypeId().intValue()==Activity.PARALLEL_ACTIVITY_TYPE){
			parallelActivity = ParallelActivity.createCopy((ParallelActivity)activity);
			activityDAO.insert(parallelActivity);
		}
		assertNotNull(parallelActivity.getActivityId());
	}	
}

