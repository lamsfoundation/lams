/*
 * Created on Jan 11, 2005
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
