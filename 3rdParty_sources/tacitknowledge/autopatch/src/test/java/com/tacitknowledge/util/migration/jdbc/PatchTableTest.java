/* Copyright 2004 Tacit Knowledge
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tacitknowledge.util.migration.jdbc;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.easymock.MockControl;

import com.mockrunner.jdbc.JDBCTestCaseAdapter;
import com.mockrunner.jdbc.PreparedStatementResultSetHandler;
import com.mockrunner.mock.jdbc.MockConnection;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.tacitknowledge.util.migration.MigrationException;
import com.tacitknowledge.util.migration.jdbc.util.ConnectionWrapperDataSource;

/**
 * Out-of-container tests the <code>PatchTable</code> class using a
 * mock JDBC driver. 
 * 
 * @author  Scott Askew (scott@tacitknowledge.com)
 */
public class PatchTableTest extends JDBCTestCaseAdapter
{
    /**
     * The <code>PatchTable</code> to test
     */
    private PatchTable table = null; 

    /**
     * The mock JDBC connection to use during the tests
     */
    private MockConnection conn = null;
    
    /**
     * The <code>JDBCMigrationConteext</code> used for testing
     */
    private DataSourceMigrationContext context = new DataSourceMigrationContext(); 

    /** Used to specify different statements in the tests */
    private PreparedStatementResultSetHandler handler = null;
    private MockControl contextControl;
    private JdbcMigrationContext mockContext;


    /**
     * Constructor for PatchTableTest.
     *
     * @param name the name of the test to run
     */
    public PatchTableTest(String name)
    {
        super(name);
    }
    
    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        
        conn = getJDBCMockObjectFactory().getMockConnection();
        
        context = new DataSourceMigrationContext();
        context.setDataSource(new ConnectionWrapperDataSource(conn));
        context.setSystemName("milestone");
        context.setDatabaseType(new DatabaseType("hsqldb"));
        
        table = new PatchTable(context);
        contextControl = MockControl.createControl(JdbcMigrationContext.class);
        mockContext = (JdbcMigrationContext) contextControl.getMock();
    }
    
    /**
     * Ensures that the class throws an <code>IllegalArgumentException</code>
     * if an unknown database type is specified in the constructor. 
     */
    public void testUnknownDatabaseType()
    {
        try
        {
            context.setDatabaseType(new DatabaseType("bad-database-type"));
            new PatchTable(context);
            fail("Expected IllegalArgumentException because of unknown database type");
        }
        catch (IllegalArgumentException e)
        {
            // Expected
        }
    }
    
    /**
     * Validates the automatic creation of the patches table.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testCreatePatchesTable() throws Exception
    {
        // Test-specific setup
        PreparedStatementResultSetHandler h = conn.getPreparedStatementResultSetHandler();
        h.prepareThrowsSQLException(table.getSql("level.table.exists"));
        
        table.createPatchStoreIfNeeded();

        commonVerifications();
        verifyCommitted();
        verifyPreparedStatementParameter(0, 1, "milestone");
        verifySQLStatementExecuted(table.getSql("patches.create"));
    }
    
    /**
     * Tests when trying to get a connection to the database to create the patch table fails.
     * @throws SQLException shouldn't occur, only declared to make the code below more readable.
     */
    public void testCreatePatchesTableWithoutConnection() throws SQLException
    {

        // setup mock calls
        mockContext.getDatabaseType();
        contextControl.setReturnValue(new DatabaseType("postgres"), MockControl.ONE_OR_MORE);
        
        mockContext.getConnection();
        contextControl.setThrowable(new SQLException("An exception during getConnection"));
        contextControl.replay();
        
        table = new PatchTable(mockContext);
        try 
        {
            table.createPatchStoreIfNeeded();
            fail("Expected a MigrationException");
        } 
        catch (MigrationException e) 
        {
            contextControl.verify();
        }
    }

    /**
     * Validates that the system recognizes an existing patches table.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testVerifyPatchesTable() throws Exception
    {
        // Test-specific setup
        handler = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = handler.createResultSet();
        handler.prepareGlobalResultSet(rs);
        rs.addRow(new Integer[] {new Integer(13)});
        
        table.createPatchStoreIfNeeded();
        
        commonVerifications();
        verifyNotCommitted();
        verifyPreparedStatementParameter(0, 1, "milestone");
        verifyPreparedStatementNotPresent(table.getSql("patches.create"));
    }
    
    /**
     * Validates that <code>getPatchLevel</code> works on an existing system.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testGetPatchLevel() throws Exception
    {
        // Test-specific setup
        handler = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = handler.createResultSet();
        rs.addRow(new Integer[]{new Integer(13)});
        handler.prepareGlobalResultSet(rs);

        int i = table.getPatchLevel();

        assertEquals(13, i);
        commonVerifications();
        verifyNotCommitted();
        verifyPreparedStatementParameter(1, 1, "milestone");
        verifyPreparedStatementNotPresent(table.getSql("level.create"));
    }

    /**
     * Validates that <code>getPatchLevel</code> works on a new system.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testGetPatchLevelFirstTime() throws Exception
    {
        // Test-specific setup
        handler = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = handler.createResultSet();
        // empty result set
        handler.prepareResultSet(table.getSql("level.read"), rs);
        handler.prepareThrowsSQLException(table.getSql("level.table.exists"));

        int i = table.getPatchLevel();

        assertEquals(0, i);
        commonVerifications();
        verifyPreparedStatementPresent(table.getSql("level.create"));
    }

    /**
     * Validates that the patch level can be updated.
     *  
     * @throws Exception if an unexpected error occurs
     */
    public void testUpdatePatchLevel() throws Exception
    {
        handler = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = handler.createResultSet();
        rs.addRow(new Integer[]{new Integer(12)});
        handler.prepareResultSet(table.getSql("level.read"), rs, new String[]{"milestone"});

        table.updatePatchLevel(13);
        
        verifyPreparedStatementParameter(table.getSql("level.update"), 1, new Integer(13));
        verifyPreparedStatementParameter(table.getSql("level.update"), 2, "milestone");
        commonVerifications();
        verifyCommitted();
    }
    
    /**
     * Validates that <code>isPatchTableLocked</code> works when no lock exists.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testIsPatchTableNotLocked() throws Exception
    {
        // Test-specific setup
        // Return a non-empty set in response to the patch lock query
        handler = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = handler.createResultSet();
        rs.addRow(new String[]{"F"});
        handler.prepareResultSet(table.getSql("lock.read"), rs, new String[]{"milestone", "milestone"});
        
        assertFalse(table.isPatchStoreLocked());
        commonVerifications();
        verifyNotCommitted();
    }
    
    /**
     * Validates that <code>isPatchTableLocked</code> works when a lock already exists.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testIsPatchTableLocked() throws Exception
    {
        // Test-specific setup
        // Return a non-empty set in response to the patch lock query
        handler = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = handler.createResultSet();
        rs.addRow(new String[]{"T"});
        handler.prepareResultSet(table.getSql("lock.read"), rs, new String[]{"milestone", "milestone"});
        
        assertTrue(table.isPatchStoreLocked());
        commonVerifications();
        verifyNotCommitted();
    }
    
    /**
     * Validates that an <code>IllegalStateException</code> is thrown when trying
     * to lock an already locked patches table.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testLockPatchTableWhenAlreadyLocked() throws Exception
    {
        // Test-specific setup
        // Return a non-empty set in response to the patch lock query
        handler = conn.getPreparedStatementResultSetHandler();
        handler.prepareUpdateCount(table.getSql("lock.obtain"), 0, new String[] {"milestone", "milestone"});
        
        try
        {
            table.lockPatchStore();
            fail("Expected an IllegalStateException since a lock already exists.");
        }
        catch (IllegalStateException e)
        {
            // Expected
        }
        
        verifyPreparedStatementParameter(table.getSql("lock.obtain"), 1, "milestone");
        verifyPreparedStatementParameter(table.getSql("lock.obtain"), 2, "milestone");
        commonVerifications();
        verifyCommitted();
    }

    /**
     * Validates that the patches table can be locked as long as no other lock
     * is in place.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testLockPatchTableWhenNotAlreadyLocked() throws Exception
    {
        // Test-specific setup
        // Return an empty set in response to the patch lock query
        handler = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = handler.createResultSet();
        handler.prepareUpdateCount(table.getSql("lock.obtain"), 1, new String[] {"milestone", "milestone"});
        
        table.lockPatchStore();
        verifyPreparedStatementParameter(table.getSql("lock.obtain"), 1, "milestone");
        verifyPreparedStatementParameter(table.getSql("lock.obtain"), 2, "milestone");
        commonVerifications();
        verifyCommitted();
    }
    
    /**
     * Validates that the patches table lock can be removed.
     * 
     * @throws Exception if an unexpected error occurs
     */
    public void testUnlockPatchTable() throws Exception
    {       
        table.unlockPatchStore();

        verifyPreparedStatementParameter(table.getSql("lock.release"), 1, "milestone");
        commonVerifications();
        verifyCommitted();
    }

    public void testIsPatchApplied() throws MigrationException
    {
        handler = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = handler.createResultSet();
        rs.addRow(new Integer[]{new Integer(3)});
        handler.prepareGlobalResultSet(rs);

        assertEquals(true, table.isPatchApplied(3));
        commonVerifications();
        verifyPreparedStatementPresent(table.getSql("level.exists"));

    }

    public void testMigrationExceptionIsThrownIfSQLExceptionHappens() throws SQLException {

        mockContext.getDatabaseType();
        contextControl.setReturnValue(new DatabaseType("postgres"), MockControl.ONE_OR_MORE);

        mockContext.getConnection();
        contextControl.setThrowable(new SQLException("An exception during getConnection"));
        contextControl.replay();

        table = new PatchTable(mockContext);

        try {

            table.isPatchApplied(3);
            fail("MigrationException should have happened if SQLException");

        } catch (MigrationException e) {

            //Expected
        }

    }

    public void testIsPatchAppliedWithMissingLevel() throws MigrationException
    {
        handler = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = handler.createResultSet();
        handler.prepareGlobalResultSet(rs);

        assertEquals(false, table.isPatchApplied(3));
        commonVerifications();
        verifyPreparedStatementPresent(table.getSql("level.exists"));
    }

    public void testPatchRetrievesSetWithPatchesApplied () throws SQLException, MigrationException {
        handler = conn.getPreparedStatementResultSetHandler();
        MockResultSet rs = handler.createResultSet();
        rs.addColumn("patch_level", new Object[]{1, 2});
        handler.prepareGlobalResultSet(rs);

        Set<Integer> expected = new HashSet<Integer>();
        expected.add(1);
        expected.add(2);
        assertEquals(expected, table.getPatchesApplied());
        commonVerifications();
        verifyPreparedStatementPresent(table.getSql("patches.all"));
    }


    private void commonVerifications()
    {
        verifyAllResultSetsClosed();
        verifyAllStatementsClosed();
        verifyConnectionClosed();
    }


}
