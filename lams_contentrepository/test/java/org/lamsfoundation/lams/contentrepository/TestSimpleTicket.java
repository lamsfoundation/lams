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

package org.lamsfoundation.lams.contentrepository;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;

/**
 * Test the SimpleTicket class. Tests the methods for
 * the ITicket interface.
 * 
 * @author Fiona Malikoff
 */
public class TestSimpleTicket extends BaseTestCase {

	protected Logger log = Logger.getLogger(TestSimpleTicket.class);

	/**
	 * Constructor for TestSimpleTicket.
	 * @throws WorkspaceNotFoundException
	 * @throws AccessDeniedException
	 * @throws LoginException
	 */
	public TestSimpleTicket() throws LoginException, AccessDeniedException, WorkspaceNotFoundException {
		super();
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetWorkspaceId() {
		Long id = ticket.getWorkspaceId();
		assertTrue("Ticket has workspaceId.", id != null );
		assertTrue("Workspace id is "+id
				+" as expected "+INITIAL_WORKSPACE_ID,
				INITIAL_WORKSPACE_ID.equals(id));
	}

	public void testGetTicketId() {
		String id = ticket.getTicketId();
		assertTrue("Ticket has a ticketId.", id != null );
	}


}
