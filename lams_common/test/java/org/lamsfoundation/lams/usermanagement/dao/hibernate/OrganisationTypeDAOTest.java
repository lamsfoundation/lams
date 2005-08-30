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
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import org.lamsfoundation.lams.test.AbstractCommonTestCase;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.springframework.context.ApplicationContext;



/**
 * <p>
 * <a href="OrganisationTypeTypeDAOTest.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class OrganisationTypeDAOTest extends AbstractCommonTestCase{
	private OrganisationType organisationType = null;
	private String errorMessage = "";
	private OrganisationTypeDAO organisationTypeDAO = null;
	private ApplicationContext ctx;
	
	public OrganisationTypeDAOTest(String name){
		super(name);
	}
	protected void setUp() throws Exception{
		super.setUp();		
		organisationTypeDAO = (OrganisationTypeDAO)context.getBean("organisationTypeDAO");
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
