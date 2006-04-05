/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
package org.lamsfoundation.lams.contentrepository.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.CrWorkspace;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.NodeType;
import org.lamsfoundation.lams.contentrepository.dao.IWorkspaceDAO;
import org.lamsfoundation.lams.contentrepository.data.CRResources;
import org.lamsfoundation.lams.test.AbstractLamsTestCase;


public class BaseTestCase extends AbstractLamsTestCase {
	
	protected static IRepositoryAdmin repository = null;
	protected static ITicket ticket = null;
	protected static INodeFactory nodeFactory = null;
	protected static IWorkspaceDAO workspaceDAO = null;
	
	// two workspaces exist initially, and atool has access to both.
	protected static final String INITIAL_WORKSPACE = "atoolWorkspace";
	protected static final Long INITIAL_WORKSPACE_ID = new Long(1);
	protected static final String INITIAL_WORKSPACE_USER = "atool";
	protected static final char[] INITIAL_WORKSPACE_PASSWORD = {'a','t','o','o','l'};
	
	protected static final String SECONDARY_WORKSPACE = "atoolWorkspace2";

	protected static final String TEST_NODE_PATH = "/test";
	protected static final Long TEST_DATA_NODE_ID = new Long(1); // A datanode that should already be in db
	protected static final Long TEST_FILE_NODE_ID = new Long(2); // A filenode that should already be in db	

	public BaseTestCase(String name){
		super(name);
		// Uncomment the following line to get debuggging.
		// BasicConfigurator.configure();
		
	}
	
    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
    	super.setUp();
		if ( repository == null ) {
			// get repository object from bean factory
			repository =(IRepositoryAdmin)context.getBean(IRepositoryService.REPOSITORY_SERVICE_ID);
			nodeFactory = (INodeFactory)context.getBean(INodeFactory.NODE_FACTORY_ID);
			workspaceDAO = (IWorkspaceDAO)context.getBean(IWorkspaceDAO.WORKSPACE_DAO_ID);
			ICredentials cred = new SimpleCredentials(INITIAL_WORKSPACE_USER, INITIAL_WORKSPACE_PASSWORD);
			try { 
				ticket = repository.login(cred, INITIAL_WORKSPACE);
			} catch ( Exception e ) {
				failUnexpectedException(e);
			}
		}
	}

	protected String[] getContextConfigLocation() {
		return new String[] {
				IRepositoryService.CORE_LOCAL_CONTEXT_PATH,
		        IRepositoryService.REPOSITORY_CONTEXT_PATH,
		        "/org/lamsfoundation/lams/contentrepository/testApplicationContext.xml"
		        };
	}

	
    protected String getHibernateSessionFactoryName() {
    	return "crSessionFactory";
    }


	protected void failUnexpectedException(Exception e) {
		System.out.println("Unexpected exception: ");
		e.printStackTrace();
		fail("Unexpected exception thrown."+e.getMessage());
	}

	protected void failUnexpectedException(String testName, Exception e) {
		System.out.println(testName+": unexpected exception: ");
		e.printStackTrace();
		fail("Unexpected exception thrown."+e.getMessage());
	}
	
	/** Normally this functionality is handled by the ticket */
	protected SimpleVersionedNode getNode(Long workspaceId, Long nodeId) {
		try {
			return nodeFactory.getNode(workspaceId, nodeId, null);
		} catch (ItemNotFoundException e) {
			e.printStackTrace();
			fail("Latest version of test node not found, id="+nodeId);
		}
		return null;
	}

	/** Normally this functionality is handled by repository service */
	protected CrWorkspace getWorkspace(Long workspaceId) {
		// call workspace dao to get the workspace
		CrWorkspace workspace = (CrWorkspace) workspaceDAO.find(CrWorkspace.class, workspaceId);
		if ( workspace == null ) {
			fail("Workspace id "+workspaceId+" not found.");
		}
		return workspace;

	}
	protected SimpleVersionedNode getTestNode() {
		return getNode(INITIAL_WORKSPACE_ID, TEST_DATA_NODE_ID);
	}
	
	protected void checkPackage(NodeKey keys) throws AccessDeniedException, ItemNotFoundException, FileException, IOException {
		// try getting the start file - index.html
		checkFileInPackage(keys, null);
		
		// now try another file in the package
		checkFileInPackage(keys, CRResources.zipFileIncludesFilename);
		
		// check that there is the expected number of files in pacakge.
		// expect an extra node over the number of files (for the package node)
		List nodes = repository.getPackageNodes(ticket, keys.getUuid(), null);
		assertTrue("Expected number of nodes found. Expected " 
				+(CRResources.zipFileNumFiles+1)+" got "
				+(nodes != null ? nodes.size() : 0 ),
				nodes != null && nodes.size() == (CRResources.zipFileNumFiles+1));
		Iterator iter = nodes.iterator();
		if ( iter.hasNext() ) {
			SimpleVersionedNode packageNode = (SimpleVersionedNode) iter.next();
			assertTrue("First node is the package node.",
				packageNode.isNodeType(NodeType.PACKAGENODE));
		}
		while ( iter.hasNext() ) {
			SimpleVersionedNode childNode = (SimpleVersionedNode) iter.next();
			assertTrue("Child node is a file node.",
					childNode.isNodeType(NodeType.FILENODE));
		}
	}

	/**
	 * @param keys
	 * @param relPath
	 * @throws AccessDeniedException
	 * @throws ItemNotFoundException
	 * @throws FileException
	 * @throws IOException
	 */
	protected void checkFileInPackage(NodeKey keys, String relPath) throws AccessDeniedException, ItemNotFoundException, FileException, IOException {
		IVersionedNode node = repository.getFileItem(ticket, keys.getUuid(), keys.getVersion()); 
		InputStream isOut = node.getFile(); 
		assertTrue("Input stream is returned for file path "+relPath, isOut != null);
		try {
			int ch = isOut.read();
			assertTrue("Input stream can be read, first byte is "+ch, ch != -1);
		} catch ( IOException e ) {
			throw e;
		} finally {
			if (isOut != null)
				isOut.close();
		}
	}


}
