package org.lamsfoundation.lams.contentrepository;

import junit.framework.TestCase;

/**
 * Test the node's transaction handling. Need to test the following scenarios:
 *
 * <UL>
 * <LI>Start to create a new node with a file. Before ending the transaction, start
 * another transaction that tries to create a node with the same file. Save the second 
 * node, then the first node. This test should successfully write two UNRELATED nodes.
 * <LI>Read an existing node. Start a transaction and make a change to the node. Save
 * the node. This test should be successful, with a new version of this node created. 
 * <LI>Read an existing node. Create a file (in the filesystem) to conflict with the 
 * next version to be written for this node. Start a transaction, make a change to the 
 * node and save the node. This test should fail, with a conflict on the added file. 
 * <LI>Read an existing node. Start a transaction and make a change to the node. 
 * Start another transaction and make a change to the same version of the node.
 * Save the node in the first transaction. Save the node in the second transaction. This
 * test should fail with the save to the node in the second transaction
 * rolling back (node out of date).
 * <LI>Read an existing node. Start a transaction and make a change to the node. 
 * Start another transaction and make a change to the same version of the node.
 * Save the node in the second transaction then the node in the first transaction. This
 * test should fail with the save to node in the first transaction rolling back 
 * (node out of date). 
 * <LI>Read an existing node. Start a transaction and make a change to the node. 
 * Start another transaction and make a change to the same version of the node.
 * Save the node in the first transaction but do not end the transaction. 
 * Save the node in the second transaction. This test should fail with the save 
 * to the node in the second transaction rolling back due to the database lock (either
 * db level or our own field lock.
 * <LI>Read an existing node. Start a transaction and create a new version 
 * of the node, with a very large file. Save the node. When it is partway through 
 * writing the file, kill the file write at an operating system level. 
 * e.g. disconnect the filesystem. This should fail but the outcome on the database
 * changes have not yet been decided. Note: it may not be possible to automate
 * this test!   
 * </UL>
 * @author Fiona Malikoff
 */
public class NodeTransactionTest extends TestCase {

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

	/**
	 * Constructor for NodeTransactionTest.
	 * @param name
	 */
	public NodeTransactionTest(String name) {
		super(name);
	}

}
