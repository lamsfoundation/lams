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
import org.lamsfoundation.lams.usermanagement.Organisation;

/**
 * <p>
 * <a href="OrganisationDAOTest.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class OrganisationDAOTest extends AbstractCommonTestCase {
	
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

}
