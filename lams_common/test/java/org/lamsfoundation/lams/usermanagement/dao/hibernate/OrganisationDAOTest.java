/*
 * Created on Nov 26, 2004
 *
 * Last modified on Nov 26, 2004
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.Date;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Workspace;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationTypeDAO;

import junit.framework.TestCase;

/**
 * TODO Add description here
 *
 * <p>
 * <a href="OrganisationDAOTest.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class OrganisationDAOTest extends TestCase {
	private Organisation organisation = null;
	private String errorMessage = "";
	private OrganisationTypeDAO organisationDAO = null;
	private ApplicationContext ctx;
	
	protected void setUp() throws Exception{
		ctx = new FileSystemXmlApplicationContext("applicationContext.xml");
		organisationDAO = (OrganisationTypeDAO)ctx.getBean("organisationDAO");
	}
	
	protected void tearDown() throws Exception{
		organisationDAO = null;
	}
	
	public void testSaveOrganisation(){
		/*
		IOrganisationTypeDAO organisationTypeDAO = (IOrganisationTypeDAO)ctx.getBean("organisationTypeDAO");
		organisation = new Organisation("Root","",null,new Date(), new Workspace(), organisationTypeDAO.getOrganisationTypeById(new Integer(1)),null,null);
		organisationTypeDAO = null;
		organisationDAO.saveOrganisation(organisation);
		assertTrue(organisationDAO.getAllOrganisations().size()==1);
		*/
	}

	public void testGetOrganisationById(){
	}

}
