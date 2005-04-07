/*
 * ToolDBActivateTaskTest.java
 * JUnit based test
 *
 * Created on 07 April 2005, 16:06
 */

package org.lamsfoundation.lams.tool.deploy;

import junit.framework.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.apache.commons.dbutils.DbUtils;

/**
 *
 * @author chris
 */
public class ToolDBActivateTaskTest extends ToolDBTest
{
    
    public ToolDBActivateTaskTest(String testName)
    {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception
    {
        super.setUp();
        insertTestRecords("test/file/test.properties");
    }

    protected void tearDown() throws java.lang.Exception
    {
        super.tearDown();
    }
    
    public void testExecute() throws Exception
    {
        ToolDBActivateTask task = new ToolDBActivateTask();
        task.setDbUsername(props.getString("dbUsername"));
        task.setDbPassword(props.getString("dbPassword"));
        task.setDbDriverClass(props.getString("dbDriverClass"));
        task.setDbDriverUrl(props.getString("dbDriverUrl"));
        task.setToolId(1);
        task.setLearningLibraryId(1);
        task.execute();
    }

    public static junit.framework.Test suite()
    {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(ToolDBActivateTaskTest.class);
        
        return suite;
    }
    
    
    
}
