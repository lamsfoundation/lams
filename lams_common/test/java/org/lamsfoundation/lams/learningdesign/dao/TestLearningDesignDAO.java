/*
 * Created on Dec 6, 2004
 */
package org.lamsfoundation.lams.learningdesign.dao;


import java.util.List;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.learningdesign.Activity;

import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.TransitionDAO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;

import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.UserDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.WorkspaceFolderDAO;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;


/**
 * @author MMINHAS
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestLearningDesignDAO extends AbstractLamsTestCase {
	
	protected ActivityDAO activityDAO;
	private LearningDesignDAO learningDesignDAO;
	protected TransitionDAO transitionDAO;
	protected WorkspaceFolderDAO workspaceFolderDAO;
	private UserDAO userDAO;
	private User user;
	private LearningDesign learningDesign;
	
	public TestLearningDesignDAO(String name) {
		super(name);
	}	
	public void setUp() throws Exception{
		super.setUp();
		learningDesignDAO =(LearningDesignDAO)context.getBean("learningDesignDAO");		
		transitionDAO =(TransitionDAO) context.getBean("transitionDAO");
		activityDAO =(ActivityDAO) context.getBean("activityDAO");
		userDAO = (UserDAO)context.getBean("userDAO");
	}
	public void testCalculateFirstActivity(){
		learningDesign = learningDesignDAO.getLearningDesignById(new Long(2));
		Activity activity = learningDesign.calculateFirstActivity();
		assertNotNull(activity.getActivityId());
		long x = 27;
		assertEquals(activity.getActivityId().longValue(),x);
	}
	protected String[] getContextConfigLocation() {
		return new String[] {"WEB-INF/spring/learningDesignApplicationContext.xml",
		 "WEB-INF/spring/applicationContext.xml"};
	}
	public void testGetAllValidLearningDesignsInFolder(){
		List list = learningDesignDAO.getAllValidLearningDesignsInFolder(new Integer(1));
		System.out.println("SIZE:"+list.size());
	}
	public void testGetLearningDesignDTO() throws Exception{
		learningDesign = learningDesignDAO.getLearningDesignById(new Long(2));
		LearningDesignDTO learningDesignDTO = learningDesign.getLearningDesignDTO();		
		String str = WDDXProcessor.serialize(learningDesignDTO);
		System.out.println(str);		
	}
	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getHibernateSessionFactoryName()
	 */
	protected String getHibernateSessionFactoryName() {
		// TODO Auto-generated method stub
		return null;
	}	
}
