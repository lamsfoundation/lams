/*
 * Created on May 28, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.AbstractLamsTestCase;

/**
 * @author Manpreet Minhas
 */
public class TestUserOrganisationDAO extends AbstractLamsTestCase {
	
	protected UserOrganisationDAO userOrganisationDAO;
	protected UserDAO userDAO;
	
	public TestUserOrganisationDAO(String name){
		super(name);
	}
	protected void setUp()throws Exception{
		super.setUp();
		userOrganisationDAO = (UserOrganisationDAO)context.getBean("userOrganisationDAO");
		userDAO =(UserDAO)context.getBean("userDAO");
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
	 */
	protected String[] getContextConfigLocation() {
		return new String[] {"org/lamsfoundation/lams/applicationContext.xml"};
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.AbstractLamsTestCase#getHibernateSessionFactoryName()
	 */
	protected String getHibernateSessionFactoryName() {
		return "coreSessionFactory";
	}
	public void testGetUserOrganisationsByUser(){
		List memberships = userOrganisationDAO.getUserOrganisationsByUser(userDAO.getUserById(new Integer(4)));
		assertEquals(memberships.size(),4);
	}

}
