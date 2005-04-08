/*
 * ToolDBDeployTaskTest.java
 * JUnit based test
 *
 * Created on 07 April 2005, 11:21
 */

package org.lamsfoundation.lams.tool.deploy;

import junit.framework.*;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author chris
 */
public class ToolDBDeployTaskTest extends ToolDBTest
{
    
    File toolInsertScript = null;
    File libraryInsertScript = null;
    File toolCreateScript = null;
    File activityInsertScript = null;
    
    public ToolDBDeployTaskTest(String testName)
    {
        super(testName);
    }
    
    protected void setUp() throws java.lang.Exception
    {
        super.setUp();
        
        
    }
    
    protected void tearDown() throws java.lang.Exception
    {
        super.tearDown();
    }
    
    
    
    public static junit.framework.Test suite()
    {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(ToolDBDeployTaskTest.class);
        
        return suite;
    }
    
    public void testExecute() throws Exception
    {
        ToolDBDeployTask task = new ToolDBDeployTask();
        task.setDbUsername(props.getString("dbUsername"));
        task.setDbPassword(props.getString("dbPassword"));
        task.setDbDriverClass(props.getString("dbDriverClass"));
        task.setDbDriverUrl(props.getString("dbDriverUrl"));
        task.setToolInsertScriptPath(props.getString("toolInsertScriptPath"));
        task.setToolLibraryInsertScriptPath(props.getString("toolLibraryInsertScriptPath"));
        task.setToolActivityInsertScriptPath(props.getString("toolActivityInsertScriptPath"));
        task.setToolTablesScriptPath(props.getString("toolTablesScriptPath"));
        task.execute();
        assertTrue(task.getToolId() > 0L);
        assertTrue(task.getLearningLibraryId() > 0L);
        assertTrue(task.getDefaultContentId() > 0L);
    }
    
    
    
}
