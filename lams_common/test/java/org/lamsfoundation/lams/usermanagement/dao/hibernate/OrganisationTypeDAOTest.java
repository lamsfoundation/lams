/*
 * Created on Nov 26, 2004
 *
 * Last modified on Nov 26, 2004
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import org.lamsfoundation.lams.usermanagement.OrganisationType;

import junit.framework.TestCase;

/**
 * TODO Add description here
 *
 * <p>
 * <a href="OrganisationTypeTypeDAOTest.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class OrganisationTypeDAOTest extends TestCase {
	private OrganisationType organisationType = null;
	private String errorMessage = "";
	private OrganisationTypeDAO organisationTypeDAO = null;
	private ApplicationContext ctx;
	
	protected void setUp() throws Exception{
		ctx = new FileSystemXmlApplicationContext("applicationContext.xml");
		organisationTypeDAO = (OrganisationTypeDAO)ctx.getBean("organisationTypeDAO");
	}
	
	protected void tearDown() throws Exception{
		organisationTypeDAO = null;
	}
	
	public void testSaveOrganisationType(){
		organisationType = new OrganisationType("TEST","for test purpose only", null);
		organisationTypeDAO.saveOrganisationType(organisationType);
		assertNotNull(organisationTypeDAO.getOrganisationTypeByName("TEST"));
	}

	public void testGetOrganisationTypeById(){
		errorMessage = "The name of the organisationType gotten by Id 1 is not ROOT ORGANISATION";
		organisationType = organisationTypeDAO.getOrganisationTypeById(new Integer(1));
		assertEquals(errorMessage,"ROOT ORGANISATION",organisationType.getName());
	}

	public void testGetOrganisationTypeByName(){
		errorMessage = "The id of the organisationType gotten by name ROOT ORGANISATION is not 1";
		organisationType = organisationTypeDAO.getOrganisationTypeByName("ROOT ORGANISATION");
		assertEquals(errorMessage,new Integer(1),organisationType.getOrganisationTypeId());
	}
	
	public void testDeleteOrganisationType()
	{
		organisationType = organisationTypeDAO.getOrganisationTypeByName("TEST");
		organisationTypeDAO.deleteOrganisationType(organisationType);
		assertNull(organisationTypeDAO.getOrganisationTypeByName("TEST"));
	}

}
