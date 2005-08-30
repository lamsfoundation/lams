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

package org.lamsfoundation.lams.contentrepository.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.CrNodeVersionProperty;
import org.lamsfoundation.lams.contentrepository.CrWorkspace;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.IValue;
import org.lamsfoundation.lams.contentrepository.IVersionDetail;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.IVersionedNodeAdmin;
import org.lamsfoundation.lams.contentrepository.ItemExistsException;
import org.lamsfoundation.lams.contentrepository.NoSuchNodeTypeException;
import org.lamsfoundation.lams.contentrepository.NodeType;
import org.lamsfoundation.lams.contentrepository.PropertyName;
import org.lamsfoundation.lams.contentrepository.PropertyType;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.ValidationException;
import org.lamsfoundation.lams.contentrepository.ValueFormatException;
import org.lamsfoundation.lams.contentrepository.dao.hibernate.WorkspaceDAO;
import org.lamsfoundation.lams.contentrepository.data.CRResources;




/**
 * @author Fiona Malikoff
 * 
 * Test the non-persistence related features of a SimpleVersionedNode.
 * Unable to test the ids as these are generated when written to the database.
 * 
 * When running tests, please make sure the FILEPATH is set to a file on your PC
 * or comment out the file tests.
 * 
 * Needs to be in the same package as SimpleVersionedNode
 * to access protected methods.
 */
public class TestSimpleVersionedNode extends BaseTestCase {

	protected Logger log = Logger.getLogger(TestSimpleVersionedNode.class);
	
	/**
	 * Constructor for TestSimpleVersionedNode. Called for each test, so the stuff
	 * is sets up is static and is only done once.
	 * @param arg0
	 * @throws RepositoryCheckedException
	 * @throws NoSuchNodeTypeException
	 * @throws ItemExistsException
	 */
	public TestSimpleVersionedNode(String name) throws ItemExistsException, NoSuchNodeTypeException, RepositoryCheckedException {
		super(name);
	}

	/*
	 * Class under test for void setProperty(String, IValue)
	 */
	public void testSetPropertyStringIValue() {
		IValue value = new CrNodeVersionProperty("IVALUE name", 
				"IVALUE value",	PropertyType.STRING, null);
	}

	/*
	 * Guts of tests for setProperty calls using implied types and explicit types.
	 * For implied types, past it in as its own object type (Boolean, Double, etc) and leave type null.
	 * For explicit types, past it in as a string and set type to the appropriate type.
	 */
	private void testSetProperty(String name, Object value, Integer type, String expectedString,
			Boolean expectedBoolean, Long expectedLong, Double expectedDouble, Calendar expectedCalendar)
	{
		SimpleVersionedNode testNode = getTestNode();
		try {
			if ( type != null ) {
				testNode.setProperty(name, (String)value, type.intValue());
			} else if ( String.class.isInstance(value) )
				testNode.setProperty(name, (String)value );
			else if ( Boolean.class.isInstance(value) )
				testNode.setProperty(name, ((Boolean)value).booleanValue() );
			else if ( Double.class.isInstance(value) )
				testNode.setProperty(name, ((Double)value).doubleValue() );
			else if ( Long.class.isInstance(value) )
				testNode.setProperty(name, ((Long)value).longValue() );
			else if ( Calendar.class.isInstance(value) )
				testNode.setProperty(name, (Calendar)value );
		} catch (Exception e) {
			failUnexpectedException(e);
		}
		
		try {
			testNode.save(null, null);
		} catch (Exception e) {
			failUnexpectedException(e);
		} 

		IValue iValue = null;
		IVersionedNode rereadNode = getTestNode();
		assertTrue("Able to reread node", rereadNode != null);
		iValue = rereadNode.getProperty(name);
		assertTrue("Property for name "+name+" was able to be read from db.",iValue != null);

		if ( expectedString != null ) {
			try {
				String val = iValue.getString();
				assertTrue("Property accessable as string ("+val+")",
						expectedString.equals(val));
			} catch (ValueFormatException e1) {
				e1.printStackTrace();
				fail("ValueFormatException thrown unexpectedly. "+e1.getMessage());
			}
		} else {
			try {
				String val = iValue.getString();
				fail("ValueFormatException exception expected. Value returned as "+val);
			} catch (ValueFormatException e1) {
				assertTrue("ValueFormatException thrown as expected",true);
			}
		}

		if ( expectedBoolean != null ) {
			try {
				Boolean val = iValue.getBoolean();
				assertTrue("Property accessable as boolean ("+val+")",
						expectedBoolean.equals(val));
			} catch (ValueFormatException e1) {
				failUnexpectedException(e1);
			}
		} else {
			try {
				Boolean val = iValue.getBoolean();
				fail("ValueFormatException exception expected. Value returned as "+val);
			} catch (ValueFormatException e1) {
				assertTrue("ValueFormatException thrown as expected",true);
			}
		}

		if ( expectedLong != null ) {
			try {
				long val = iValue.getLong();
				assertTrue("Property accessable as long ("+val+")",
						val == expectedLong.longValue());
			} catch (ValueFormatException e1) {
				e1.printStackTrace();
				fail("ValueFormatException thrown unexpectedly. "+e1.getMessage());
			}
		} else {
			try {
				long val = iValue.getLong();
				fail("ValueFormatException exception expected. Value returned as "+val);
			} catch (ValueFormatException e1) {
				assertTrue("ValueFormatException thrown as expected",true);
			}
		}

		if ( expectedDouble != null ) {
			try {
				double val = iValue.getDouble();
				assertTrue("Property accessable as long ("+val+")",
						val == expectedDouble.doubleValue());
			} catch (ValueFormatException e1) {
				e1.printStackTrace();
				fail("ValueFormatException thrown unexpectedly. "+e1.getMessage());
			}
		} else {
			try {
				double val = iValue.getLong();
				fail("ValueFormatException exception expected. Value returned as "+val);
			} catch (ValueFormatException e1) {
				assertTrue("ValueFormatException thrown as expected",true);
			}
		}

		if ( expectedCalendar != null ) {
			try {
				Calendar val = iValue.getDate();
				assertTrue("Property accessable as string ("+val+")",
						expectedCalendar.equals(val));
			} catch (ValueFormatException e1) {
				e1.printStackTrace();
				fail("ValueFormatException thrown unexpectedly. "+e1.getMessage());
			}
		} else {
			try {
				Calendar val = iValue.getDate();
				fail("ValueFormatException exception expected. Value returned as "+val);
			} catch (ValueFormatException e1) {
				assertTrue("ValueFormatException thrown as expected",true);
			}
		}

		testNode = null;
	}

	/*
	 * Class under test for void setProperty(String, String, int)
	 * Integer type, String expectedString,
			Boolean expectedBoolean, Long expectedLong, Double expectedDouble, Calendar expectedCalendar)
	 */
	public void testSetPropertySpecifiedType() {
		testSetProperty("NAME STRING", "VALUE STRING", new Integer(PropertyType.STRING), 
				"VALUE STRING", Boolean.FALSE, null, null, null);
		testSetProperty("NAME STRING 100.01", "100.01", new Integer(PropertyType.STRING), 
				"100.01", Boolean.FALSE, null, new Double(100.01), null);
// calendar test!		testSetPropertyImpliedType("NAME STRING 100.01", "100.01", new Integer(PropertyType.STRING), 
//		"VALUE STRING", Boolean.FALSE, new Long(100), new Double(100.01), null);
		testSetProperty("NAME BOOLEAN True", "True", new Integer(PropertyType.BOOLEAN), 
				"True", Boolean.TRUE, null, null, null);
		testSetProperty("NAME LONG 100", "100", new Integer(PropertyType.LONG), 
				"100", Boolean.FALSE, new Long(100), new Double(100), null);
		testSetProperty("NAME DOUBLE 100.02", "100.02", new Integer(PropertyType.DOUBLE), 
				"100.02", Boolean.FALSE, null, new Double(100.02), null);
//		 calendar test!		testSetPropertyImpliedType("NAME CALENDAR ???", "???", new Integer(PropertyType.CALENDAR), 
//		"???", Boolean.FALSE, null, null, value);
	}
	
	/*
	 * Class under test for void setProperty(String, String)
	 */
	public void testSetPropertyStringString() {
		testSetProperty("NAME STRING2", "VALUE STRING2", null, 
				"VALUE STRING2", Boolean.FALSE, null, null, null);
		testSetProperty("NAME STRING 200.01", "200.01", new Integer(PropertyType.STRING), 
				"200.01", Boolean.FALSE, null, new Double(200.01), null);
//		 calendar test!		testSetPropertyImpliedType("NAME STRING 100.01", "100.01", new Integer(PropertyType.STRING), 
//		"VALUE STRING", Boolean.FALSE, new Long(100), new Double(100.01), null);
	}

	/*
	 * Class under test for void setProperty(String, boolean)
	 */
	public void testSetPropertyStringboolean() {
		testSetProperty("NAME BOOLEAN True", Boolean.TRUE, null, 
				"true", Boolean.TRUE, null, null, null);
	}

	/*
	 * Class under test for void setProperty(String, double)
	 */
	public void testSetPropertyStringdouble() {
		testSetProperty("NAME DOUBLE 200.02", new Double(200.02), null, 
				"200.02", Boolean.FALSE, null, new Double(200.02), null);
	}

	/*
	 * Class under test for void setProperty(String, long)
	 */
	public void testSetPropertyStringlong() {
		testSetProperty("NAME LONG 200", new Long(200), null, 
				"200", Boolean.FALSE, new Long(200), new Double(200), null);
	}

	/*
	 * Class under test for void setProperty(String, Calendar)
	 */
	public void testSetPropertyStringCalendar() {
//		 calendar test!		testSetPropertyImpliedType("NAME CALENDAR ???", "???", new Integer(PropertyType.CALENDAR), 
//		"???", Boolean.FALSE, null, null, value);
	}

	/*
	 * Class under test for void setProperty(String, IValue)
	 */
	public void testClearPropertyStringIValue() {
		// set the value
		String name = "TO DELETE";
		String value = "This value is to be deleted."+System.currentTimeMillis();

		try {
			SimpleVersionedNode testNode = getTestNode();
			testNode.setProperty(name, value, PropertyType.STRING);
			testNode.save(null, null);

			IValue iValue = null;
			IVersionedNode rereadNode = getTestNode();
			assertTrue("Able to reread node", rereadNode != null);
			iValue = rereadNode.getProperty(name);
			assertTrue("Property for name "+name+" was able to be read from db.",iValue != null);
			assertTrue("Property contained expected data "+value,value.equals(iValue.getString()));
	
			// delete the value. Row in db should be deleted.
			testNode = getTestNode();
			testNode.setProperty(name, null, PropertyType.STRING);
			testNode.save(null, null);
			
			iValue = null;
			rereadNode = getTestNode();
			assertTrue("Able to reread node", rereadNode != null);
			iValue = rereadNode.getProperty(name);
			assertTrue("Property for name "+name+" was not in the db.",iValue == null);
		} catch (RepositoryCheckedException e) {
			e.printStackTrace();
			fail("RepositoryCheckedException thrown unexpectedly. "+e.getMessage());
		} 
	}

	public void testGetProperties() {
		Set properties = null;
		IVersionedNodeAdmin testNode = getNode(INITIAL_WORKSPACE_ID, TEST_FILE_NODE_ID);
		properties = testNode.getProperties();
		int num = properties != null ? properties.size() : -1;
		assertTrue("Expect that node has 3 property, finds "+num+" properties.",num==3);
		testNode = null;
	}

	public void testHasProperty() {
		IVersionedNodeAdmin testNode = getTestNode();
		assertTrue("Test node has version desc property ",testNode.hasProperty(PropertyName.VERSIONDESC));
		testNode = null;
	}

	public void testHasProperties() {
		IVersionedNodeAdmin testNode = getTestNode();
		assertTrue("Test node has properties ",testNode.hasProperties());
		testNode = null;
	}

	public void testGetNodeType() {
		IVersionedNodeAdmin testNode = getTestNode();
		String nodeType = testNode.getNodeType();
		assertTrue("Test node has node type, should be DATANODE, returned as "+nodeType,
					NodeType.DATANODE.equals(nodeType));
		testNode = null;
	}

	public void testIsNodeType() {
		IVersionedNodeAdmin testNode = getTestNode();
		assertTrue("Test node is correct DATANODE type",
					testNode.isNodeType(NodeType.DATANODE));
		testNode = null;
	}

	public void testGetVersionHistory() {
		IVersionedNodeAdmin testNode = getTestNode();

		SortedSet history = testNode.getVersionHistory();
		assertTrue("history exists and is not empty ",
					history != null && ! history.isEmpty());
		Iterator iter = history.iterator();
		Long previousVersionId = null;
		
		while (iter.hasNext()) {
			
			IVersionDetail element = (IVersionDetail) iter.next();
			assertTrue("Version id is a +ve long", 
					element.getVersionId() != null && 
					element.getVersionId().longValue() > 0 );
				
			assertTrue("Version has a date", 
					element.getCreatedDateTime() != null);

			if ( previousVersionId != null && 
					previousVersionId.longValue() > element.getVersionId().longValue() ) {
				fail("Version history is in wrong order");
			}
			previousVersionId = element.getVersionId();
		}
		
		testNode = null;
	}

	public void testGetPath() {
		IVersionedNodeAdmin testNode = getTestNode();
		String val = testNode.getPath();
		assertTrue("Test node should have path "+TEST_NODE_PATH+" has path "+val,
				TEST_NODE_PATH.equals(val));
		testNode = null;
	}

	public void testGetVersion() {
		IVersionedNodeAdmin testNode = getTestNode();
		Long version = testNode.getVersion();
		assertTrue("Test node is initial version.",version != null && version.longValue()==2);
		testNode = null;
	}

	public void testGetCreatedDateTime() {
		IVersionedNodeAdmin testNode = getTestNode();
		Date dt = testNode.getCreatedDateTime();
		assertTrue("Test node has a created date time.",dt != null);
		testNode = null;
	}

	public void testAddNode() {
		Long fileNodeUUID = null;
		Long dataNodeUUID = null;
		Long packageNodeUUID = null;
		try { 
			// file nodes are tested in another test
			SimpleVersionedNode dataNode = createNode(INITIAL_WORKSPACE_ID, NodeType.DATANODE);

			SimpleVersionedNode packageNode = createNode(INITIAL_WORKSPACE_ID, NodeType.PACKAGENODE);
			packageNode.setProperty(PropertyName.INITIALPATH,"index.html");
			
			assertTrue("2 nodes created, but not saved into database ", 
					dataNode != null && dataNode.getUUID() == null
					&& packageNode != null && packageNode.getUUID() == null);

			saveCheckNode("Data", NodeType.DATANODE, dataNode); 
			saveCheckNode("Package", NodeType.PACKAGENODE, packageNode); 

		} catch ( Exception e ) {
			failUnexpectedException(e);
		}
		
	}

	public void testTextFileNode() {
		Long uuid;
        try {
            uuid = testFileNodeInternal(CRResources.getSingleFile(), CRResources.singleFileName, null);
            testFileNodeInternal(CRResources.getSingleFile(), CRResources.singleFileName, uuid);
        } catch (FileNotFoundException e) {
            fail("Unexpected exception "+e.getMessage());
        }
	}

	public void testBinaryFileNode() {
        try {
			Long uuid = testFileNodeInternal(CRResources.getZipFile(), CRResources.zipFileName, null);
			testFileNodeInternal(CRResources.getZipFile(), CRResources.zipFileName, uuid);
	    } catch (FileNotFoundException e) {
	        fail("Unexpected exception "+e.getMessage());
	    }
	}

	/*
	 * Tests a file node using the given filePath.
	 * Will also make sure it can't add the file to a data node.
	 * if repositoryEntryExists == true then assume that there should
	 * already be a file in the directory hence an error will be thrown.
	 */
	private Long testFileNodeInternal(InputStream is, String filename, Long uuid) {
		
		InputStream newIs = null;
		SimpleVersionedNode fileNode = null;
		Long newUuid = uuid;
		IVersionedNodeAdmin testNode = getTestNode();

		try {

			// first try adding the file to a data node. Should fail as that's not allowed
			try {
				testNode.setFile(is, filename, null);
			} catch (RepositoryCheckedException e) {
				assertTrue("Exception thrown as test node will not accept an input stream - it is only a data node. "
						+e.getMessage(),e.getMessage().indexOf("Node must be of type FILE_NODE") != -1);
			}
			
			// now try adding to a new filenode. Check that it enforces a file stream and the name,
			// before writing it out properly
			if ( newUuid  == null ) {
				fileNode = createNode(INITIAL_WORKSPACE_ID, NodeType.FILENODE);
			} else {
				fileNode = (SimpleVersionedNode) getNode(INITIAL_WORKSPACE_ID, newUuid);
			}

			try {
				fileNode.setFile(null, filename, null);
			} catch (RepositoryCheckedException e) {
				assertTrue("Exception thrown as input stream is missing", 
						e.getMessage().indexOf("InputStream is required.")!=-1);
			}

			try {
				fileNode.setFile(is, null, null);
			} catch (RepositoryCheckedException e) {
				assertTrue("Exception thrown as filename is missing", 
						e.getMessage().indexOf("Filename is required.")!=-1);
			}

			fileNode.setFile(is, filename, null);
			saveCheckNode("File", NodeType.FILENODE, fileNode);
			newUuid = fileNode.getUUID();
			newIs = fileNode.getFile();
			assertTrue("File node has an input stream.",newIs != null);

		} catch (FileException fe2) {
			if ( uuid != null && fe2.getMessage().indexOf("exists")!=-1) {
				assertTrue("File exists error thrown as expected.", true);
			} else {
				fe2.printStackTrace();
				fail("Exception thrown unexpectedly. "+fe2.getMessage());
			}
				
		} catch (RepositoryCheckedException e2) {
			e2.printStackTrace();
			fail("Exception thrown unexpectedly. "+e2.getMessage());
		} finally {
			if ( is != null ) {
				try {
					is.close();
				} catch (IOException e1) {
					System.err.println("Unable to close input file - was there a failure ?");
					e1.printStackTrace();
				}
			}

			if ( newIs != null ) {
				try {
					newIs.close();
				} catch (IOException e1) {
					System.err.println("Unable to close reread file");
					e1.printStackTrace();
				}
			}
		}
		testNode = null;
		return newUuid;
	}

	/**
	 * @param nodeDesc
	 * @param expectedType
	 * @param fileNode
	 * @throws ValidationException
	 * @throws RepositoryCheckedException
	 */
	private void saveCheckNode(String nodeDesc, String expectedType, SimpleVersionedNode nodeToSave) 
								throws ValidationException, RepositoryCheckedException {
		Long nodeUUID;
		nodeToSave.save(null, null);
		nodeUUID = nodeToSave.getUUID();
		assertTrue(nodeDesc+" Node saved in database, assigned a UUID",  nodeUUID != null);

		IVersionedNode retrievedNode = getNode(INITIAL_WORKSPACE_ID, nodeUUID);
		assertTrue(nodeDesc+" Node can be retrieved, has correct UUID", 
				retrievedNode != null && retrievedNode.getUUID().equals(nodeUUID));
		assertTrue(nodeDesc+" Node is of expected type. Expected "+expectedType+" found "+retrievedNode.getNodeType(), 
				retrievedNode.isNodeType(expectedType));
	}

	public void testAddNodeBadType() {
		try { 
			SimpleVersionedNode newNode = createNode(INITIAL_WORKSPACE_ID, "XYZ");
			newNode.save(null, null);
			fail("NoSuchNodeTypeException should have been thrown");
		} catch (Exception e) {
			if ( NoSuchNodeTypeException.class.isInstance(e) ) {
				assertTrue("NoSuchNodeTypeException thrown as expected ",true);
			} else {
				e.printStackTrace();
				fail("Exception thrown unexpectedly. "+e.getMessage());
			}
		}
		
	}
	
	/** Normally this functionality is handled by the repository object
	 * but this test case is trying to test the stuff below the repository
	 * object 
	 * @return Node
	 * @throws NoSuchNodeTypeException
	 * @throws AccessDeniedException
	 */
	protected SimpleVersionedNode createNode(Long workspaceId, String nodeType) throws NoSuchNodeTypeException, AccessDeniedException {

		SimpleVersionedNode newNode = (SimpleVersionedNode)context.getBean("node", SimpleVersionedNode.class);
		WorkspaceDAO workspaceDAO = (WorkspaceDAO)context.getBean("workspaceDAO");
		
		CrWorkspace workspace = (CrWorkspace) workspaceDAO.find(CrWorkspace.class, workspaceId);
		if ( workspace == null ) {
			fail("Workspace id="+workspaceId+" does not exist.");
		}

		newNode.initialiseNode(null,nodeType,workspace,null);
		return newNode;
	} 

}
