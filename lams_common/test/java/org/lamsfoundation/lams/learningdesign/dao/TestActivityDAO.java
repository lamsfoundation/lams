package org.lamsfoundation.lams.learningdesign.dao;

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
		return new String[] {"/org/lamsfoundation/lams/learningdesign/learningDesignApplicationContext.xml",
		 "applicationContext.xml"};
	}
	public void testgetActivityByLibraryID(){
		List activities = activityDAO.getActivitiesByLibraryID(new Long(1));
		assertNotNull(activities);
		System.out.println("SIZE:" + activities.size());
	}
}

