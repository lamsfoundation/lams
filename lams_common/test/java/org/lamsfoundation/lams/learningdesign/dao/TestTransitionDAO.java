/*
 * Created on Feb 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao;

import java.util.List;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.TransitionDAO;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestTransitionDAO extends AbstractLamsTestCase{
	
	protected TransitionDAO transitionDAO;
	protected Transition transition;
	
	
	public TestTransitionDAO(String name) {
		super(name);	
	}
	
	public void setUp() throws Exception {
		super.setUp();
		transitionDAO =(TransitionDAO) context.getBean("transitionDAO");		
	}
	public void testGetTransitionByToActivityID(){
		List list = transitionDAO.getTransitionByToActivityID(new Long(18));
		transition = (Transition)list.get(0);
		System.out.println("SIZE: " + list.size());
		System.out.println(transition.getTitle());
		
	}
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
	 */
	protected String[] getContextConfigLocation() {
		return new String[] {"/org/lamsfoundation/lams/learningdesign/learningDesignApplicationContext.xml",
		 "applicationContext.xml"};
	}

}
