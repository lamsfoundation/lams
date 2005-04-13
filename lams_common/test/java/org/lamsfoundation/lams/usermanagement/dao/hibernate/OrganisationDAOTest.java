/*
 * Created on Nov 26, 2004
 *
 * Last modified on Nov 26, 2004
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.usermanagement.Organisation;

/**
 * TODO Add description here
 *
 * <p>
 * <a href="OrganisationDAOTest.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class OrganisationDAOTest extends AbstractLamsTestCase {
	
	private Organisation organisation = null;	
	private OrganisationDAO organisationDAO = null;
	
	
	public OrganisationDAOTest(String name){
		super(name);
	}
	protected void setUp() throws Exception{
		super.setUp();
		organisationDAO = (OrganisationDAO)context.getBean("organisationDAO");
	}
	
	protected void tearDown() throws Exception{
		organisationDAO = null;
	}	
	public void testGetOrganisationByWorkspaceeID(){
		organisation = organisationDAO.getOrganisationByWorkspaceID(new Integer(1));
		assertNotNull(organisation.getOrganisationId());
	}
	protected String[] getContextConfigLocation() {
		return new String[] {"WEB-INF/spring/learningDesignApplicationContext.xml",
		 "WEB-INF/spring/applicationContext.xml"};
	}	
	protected String getHibernateSessionFactoryName() {
		return "coreSessionFactory";
	}

}
