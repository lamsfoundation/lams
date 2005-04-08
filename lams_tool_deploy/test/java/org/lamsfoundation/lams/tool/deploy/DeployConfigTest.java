/*
 * DeployConfigTest.java
 * JUnit based test
 *
 * Created on 05 April 2005, 11:33
 */

package org.lamsfoundation.lams.tool.deploy;

import junit.framework.*;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.ConfigurationException;

/**
 *
 * @author chris
 */
public class DeployConfigTest extends TestCase
{
    private DeployConfig config = null;
    
    public DeployConfigTest(String testPath)
    {
        super(testPath);
    }
    
    protected void setUp() throws java.lang.Exception
    {
        config = new DeployConfig("test/file/deploy.properties");
    }
    
    protected void tearDown() throws java.lang.Exception
    {
    }
    
    public static junit.framework.Test suite()
    {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(DeployConfigTest.class);
        
        return suite;
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
    
    public void testGetDbUsername()
    {
        assertEquals(config.getDbUsername(), "root");
    }
    
    public void testGetDbPassword()
    {
        assertEquals(config.getDbPassword(), "dag.Quiz");
    }
    
    public void testGetDbDriverUrl()
    {
        assertEquals(config.getDbDriverUrl(), "jdbc:mysql://localhost:3306/scratch");
    }
    
    public void testGetDbDriverClass()
    {
        assertEquals(config.getDbDriverClass(), "com.mysql.jdbc.Driver");
    }
    

    
    public void testDeployFiles()
    {
        List deployFiles = config.getDeployFiles();
        assertNotNull(deployFiles);
        assertEquals(deployFiles.size(),2);
        assertEquals(deployFiles.get(0), "test/file/test-dummy.war");
        assertEquals(deployFiles.get(1), "test/file/test-dummy.jar");
        
    }

    
    public void testGetToolTablesScriptPath()
    {
        assertEquals(config.getToolTablesScriptPath(), "test/file/sql/create_tool_tables.sql");
    }
    //#Path of SQL script file to create the tool library activity
    //toolActivityInsertScriptPath=insert_imscp_activity.sql
    public void testGetToolActivityInsertScriptPath()
    {
        assertEquals(config.getToolActivityInsertScriptPath(), "test/file/sql/activity_insert.sql");
    }
    
    //#Path of SQL script to insert the library record
    //toolLibraryInsertScriptPath=insert_imscp_library.sql
    public void testGetToolLibraryInsertScriptPath()
    {
        assertEquals(config.getToolLibraryInsertScriptPath(), "test/file/sql/library_insert.sql");
    }
    //#name of SQL script that inserts the lams_tool record
    //toolInsertScriptPath=insert_imscp_tool.sql
    public void testGetToolInsertScriptPath()
    {
        assertEquals(config.getToolInsertScriptPath(), "test/file/sql/tool_insert.sql");
    }
    //#path to lams ear dir
    //lamsEarPath=/var/jboss/server/default/deploy/lams.ear
    public void testGetLamsEarPath()
    {
        assertEquals(config.getLamsEarPath(), "test/file/lams.ear");
    }
    //#Context path to deploy the tool under
    //toolContext=/lams/tool/imscp
    public void testGetToolContextRoot()
    {
        assertEquals(config.getToolContextRoot(), "/lams/tool/test");
    }
    //#URI of tool web app location
    //toolWebUri=lams_tool_imscp.war
    public void testGetToolWebUri()
    {
        assertEquals(config.getToolWebUri(), "test-dummy.war");
    }
}
