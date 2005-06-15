/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */

package org.lamsfoundation.lams.tool.deploy;

import java.io.ByteArrayOutputStream;
import java.util.List;

import junit.framework.TestCase;

/**
 *
 * @author chris
 */
public class DeployConfigTest extends TestCase
{
    private DeployConfig config = null;
    private DeployConfig config2 = null;
    
    public DeployConfigTest(String testPath)
    {
        super(testPath);
    }
    
    protected void setUp() throws java.lang.Exception
    {
        config = new DeployConfig("test/file/deploy.properties", true);

        config2 = new DeployConfig("test/file/deploy.properties", false);
        config2.updateConfiguration("test/file/deploy2.properties", true);
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
        assertEquals(config2.getDbUsername(), "root2");
    }
    
    public void testGetDbPassword()
    {
        assertEquals(config.getDbPassword(), "dag.Quiz");
        assertEquals(config2.getDbPassword(), "dag.Quiz2");
    }
    
    public void testGetDbDriverUrl()
    {
        assertEquals(config.getDbDriverUrl(), "jdbc:mysql://localhost:3306/scratch");
        assertEquals(config2.getDbDriverUrl(), "jdbc:mysql://localhost:3306/scratch2");
    }
    
    public void testGetDbDriverClass()
    {
        assertEquals(config.getDbDriverClass(), "com.mysql.jdbc.Driver");
        assertEquals(config2.getDbDriverClass(), "com.mysql.jdbc.Driver2");
    }
    

    
    public void testDeployFiles()
    {
        List deployFiles = config.getDeployFiles();
        assertNotNull(deployFiles);
        assertEquals(deployFiles.size(),2);
        assertEquals(deployFiles.get(0), "test/file/test-dummy.war");
        assertEquals(deployFiles.get(1), "test/file/test-dummy.jar");
        
        deployFiles = config2.getDeployFiles();
        assertNotNull(deployFiles);
        assertEquals(deployFiles.size(),2);
        assertEquals(deployFiles.get(0), "test/file/test-dummy2.war");
        assertEquals(deployFiles.get(1), "test/file/test-dummy2.jar");
    }

    
    public void testGetToolTablesScriptPath()
    {
        assertEquals(config.getToolTablesScriptPath(), "test/file/sql/create_tool_tables.sql");
        assertEquals(config2.getToolTablesScriptPath(), "test/file/sql/create_tool_tables.sql");
    }
    //#Path of SQL script file to create the tool library activity
    //toolActivityInsertScriptPath=insert_imscp_activity.sql
    public void testGetToolActivityInsertScriptPath()
    {
        assertEquals(config.getToolActivityInsertScriptPath(), "test/file/sql/activity_insert.sql");
        assertEquals(config2.getToolActivityInsertScriptPath(), "test/file/sql/activity_insert.sql");
    }
    
    //#Path of SQL script to insert the library record
    //toolLibraryInsertScriptPath=insert_imscp_library.sql
    public void testGetToolLibraryInsertScriptPath()
    {
        assertEquals(config.getToolLibraryInsertScriptPath(), "test/file/sql/library_insert.sql");
        assertEquals(config2.getToolLibraryInsertScriptPath(), "test/file/sql/library_insert.sql");
    }
    //#name of SQL script that inserts the lams_tool record
    //toolInsertScriptPath=insert_imscp_tool.sql
    public void testGetToolInsertScriptPath()
    {
        assertEquals(config.getToolInsertScriptPath(), "test/file/sql/tool_insert.sql");
        assertEquals(config2.getToolInsertScriptPath(), "test/file/sql/tool_insert.sql");
    }
    //#path to lams ear dir
    //lamsEarPath=/var/jboss/server/default/deploy/lams.ear
    public void testGetLamsEarPath()
    {
        assertEquals(config.getLamsEarPath(), "test/file/lams.ear");
        assertEquals(config2.getLamsEarPath(), "test/file/lams.ear");
    }
    //#Context path to deploy the tool under
    //toolContext=/lams/tool/imscp
    public void testGetToolContextRoot()
    {
        assertEquals(config.getToolContextRoot(), "/lams/tool/test");
        assertEquals(config2.getToolContextRoot(), "/lams/tool/test");
    }
    //#URI of tool web app location
    //toolWebUri=lams_tool_imscp.war
    public void testGetToolWebUri()
    {
        assertEquals(config.getToolWebUri(), "test-dummy.war");
        assertEquals(config2.getToolWebUri(), "test-dummy.war");
    }

    public void testValidation()
    {
        config2.setDbPassword(null);
        try {
            config2.validateProperties();
            fail("Deployment exception should have been thrown as validation should have failed.");
        } catch ( DeployException e ) {
            System.out.println("Validation failed as expected. Message was "+e.getMessage());
            assertTrue("Validation failed.", true);
        }
    }
    
    public void testWriteProperties() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        config2.writeProperties(os);
        String output = os.toString();
        System.out.println("Properties:"+output);
        assertTrue("Properties written okay",output != null && output.length() > 0);
    }
}
