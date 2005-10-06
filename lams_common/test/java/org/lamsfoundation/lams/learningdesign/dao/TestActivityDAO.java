package org.lamsfoundation.lams.learningdesign.dao;
/* 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt 
*/

import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupingDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningLibraryDAO;
import org.lamsfoundation.lams.test.AbstractCommonTestCase;

/**
 * @author MMINHAS
 */
public class TestActivityDAO extends AbstractCommonTestCase {
	
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
	/*public void testGetContributionType(){
		activity = activityDAO.getActivityByActivityId(new Long(18));
		//activity.ge
		HashSet set = activity.getContributionType();
		assertTrue(set.size()>0);
		
	}*/
	public void testCreateToolActivityCopy(){
		ToolActivity ta = (ToolActivity) activityDAO.getActivityByActivityId(new Long(20));
		ToolActivity newToolActivity = ToolActivity.createCopy(ta);						
		activityDAO.insert(newToolActivity);
		assertNotNull(newToolActivity.getActivityId());
	}
	public void testCreateGroupingActivityCopy(){
		GroupingActivity ga = (GroupingActivity) activityDAO.getActivityByActivityId(new Long(23));
		GroupingActivity newGroupingActivity = null;
		newGroupingActivity = GroupingActivity.createCopy(ga);			
		activityDAO.insert(newGroupingActivity);
		assertNotNull(newGroupingActivity.getActivityId());
	}
	public void testCreateOptionsActivityCopy(){
		OptionsActivity oa = (OptionsActivity) activityDAO.getActivityByActivityId(new Long(12));
		OptionsActivity optionsActivity =null;
		optionsActivity = OptionsActivity.createCopy(oa);
		activityDAO.insert(optionsActivity);
		assertNotNull(optionsActivity.getActivityId());		
	}
	public void testCreateParallelActivityCopy(){
		ParallelActivity pa = (ParallelActivity) activityDAO.getActivityByActivityId(new Long(13));
		ParallelActivity parallelActivity = ParallelActivity.createCopy(pa);
		activityDAO.insert(parallelActivity);
		assertNotNull(parallelActivity.getActivityId());
	}
}

