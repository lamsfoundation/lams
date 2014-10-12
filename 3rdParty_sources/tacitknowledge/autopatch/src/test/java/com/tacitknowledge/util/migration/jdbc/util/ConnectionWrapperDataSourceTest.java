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

package com.tacitknowledge.util.migration.jdbc.util;

import java.sql.Connection;

import com.mockrunner.jdbc.JDBCTestCaseAdapter;

/**
 * Test our ConnectionWrapperDataSource
 * 
 * @author Mike Hardy (mike@tacitknowledge.com)
 */
public class ConnectionWrapperDataSourceTest extends JDBCTestCaseAdapter
{
    /** A connection object we can use as a fixture */
    private Connection conn = null;
    
    /**
     * @see com.mockrunner.jdbc.JDBCTestCaseAdapter#setUp()
     */
    public void setUp() throws Exception
    {
        super.setUp();
        conn = getJDBCMockObjectFactory().getMockConnection();
    }
    
    /**
     * Test connection wrapping to make sure we get and return the same one
     * 
     * @exception Exception if anything goes wrong
     */
    public void testConnectionWrapping() throws Exception
    {
        ConnectionWrapperDataSource ds = new ConnectionWrapperDataSource(conn);
        assertEquals(conn, ds.getConnection());
        assertEquals(conn, ds.getConnection("foo", "bar"));
    }
    
    /**
     * Make sure our unsuupported operations really are unsupported
     */
    public void testUnsupportedOperations()
    {
        ConnectionWrapperDataSource ds = new ConnectionWrapperDataSource(conn);
     
        try
        {
            ds.getLogWriter();
            fail("Should have gotten an unsupported operation exception");
        }
        catch (UnsupportedOperationException uoe)
        {
            // we expect this
        }
        
        try
        {
            ds.setLogWriter(null);
            fail("Should have gotten an unsupported operation exception");
        }
        catch (UnsupportedOperationException uoe)
        {
            // we expect this
        }
        
        try
        {
            ds.getLoginTimeout();
            fail("Should have gotten an unsupported operation exception");
        }
        catch (UnsupportedOperationException uoe)
        {
            // we expect this
        }
        
        try
        {
            ds.setLoginTimeout(-1);
            fail("Should have gotten an unsupported operation exception");
        }
        catch (UnsupportedOperationException uoe)
        {
            // we expect this
        }
    }
}