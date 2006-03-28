/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package org.lamsfoundation.lams.tool.deploy.libraryActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;

import org.lamsfoundation.lams.tool.deploy.DeployException;
import org.xml.sax.SAXException;



/**
 * @author mtruong
 */
public class DeployLibraryConfigTest extends TestCase {
    
    private DeployLibraryConfig config = null;
    private DeployLibraryConfig config2 = null;
    private DeployLibraryConfig testConfigObject = null;
    
    private static String DB_USERNAME = "lams";
    private static String DB_PASSWORD = "lamsdemo";
    private static String DB_DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static String DB_DRIVER_URL = "jdbc:mysql://localhost/lams?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&autoReconnect=true";
    /* test data for first learning library */
    private static String LIBRARY_INSERT_SCRIPT1 = "D:\\deployTest\\ParallelActivity\\learninglibrary1_insert.sql";
    private static String TEMPLATE_ACTIVITY_INSERT_SCRIPT1 = "D:\\deployTest\\ParallelActivity\\libraryActivity1_insert.sql";
    private static String TOOL_SIGNATURE_1_LIB1 = "lanb11";   
    private static String TOOL_ACTIVITY_INSERT_SCRIPT_1_LIB1 = "D:\\deployTest\\ParallelActivity\\toolActivity1_insert.sql";
    private static String TOOL_SIGNATURE_2_LIB1 = "lasbmt11";
    private static String TOOL_ACTIVITY_INSERT_SCRIPT_2_LIB1 = "D:\\deployTest\\ParallelActivity\\toolActivity2_insert.sql";
   
    /* tests data for second learning library */
    private static String LIBRARY_INSERT_SCRIPT2 = "D:\\deployTest\\ParallelActivity\\learninglibrary2_insert.sql";
    private static String TEMPLATE_ACTIVITY_INSERT_SCRIPT2 = "D:\\deployTest\\ParallelActivity\\libraryActivity2_insert.sql";
    private static String TOOL_SIGNATURE_1_LIB2 = "laqa11";   
    private static String TOOL_ACTIVITY_INSERT_SCRIPT_1_LIB2 = "D:\\deployTest\\ParallelActivity\\toolActivity1_library2.sql";
    private static String TOOL_SIGNATURE_2_LIB2 = "lanb11";
    private static String TOOL_ACTIVITY_INSERT_SCRIPT_2_LIB2 = "D:\\deployTest\\ParallelActivity\\toolActivity2_library2.sql";
    
    
    public DeployLibraryConfigTest(String name)
    {
        super(name);
    }
    
    protected void setUp() throws ParserConfigurationException, IOException, SAXException
    {
        config = new DeployLibraryConfig("test/file/library_deploy/deployLibraryTest.xml");
       // testConfigObject = createTestObject();
        config2 = new DeployLibraryConfig("test/file/library_deploy/deployLibraryTest.xml");
        config2.updateConfigurationProperties("test/file/library_deploy/deployLibraryTest2.xml");
    }
    
    public void testGetDbUsername()
    {
        assertEquals(config.getDbUsername(), DB_USERNAME);
        assertEquals(config2.getDbUsername(), "root2");
    }
    
    public void testGetDbPassword()
    {
        assertEquals(config.getDbPassword(), DB_PASSWORD);
        assertEquals(config2.getDbPassword(), "dag.Quiz2");
    }
    
    public void testGetDbDriverUrl()
    {
        assertEquals(config.getDbDriverUrl(), DB_DRIVER_URL);
        assertEquals(config2.getDbDriverUrl(), "jdbc:mysql://localhost:3306/scratch2");
    }
    
    public void testGetDbDriverClass()
    {
        assertEquals(config.getDbDriverClass(), DB_DRIVER_CLASS);
        assertEquals(config2.getDbDriverClass(), "com.mysql.jdbc.Driver2");
    }
    
    public void testGetLearningLibraries()
    {
        ArrayList libraryList = config.getLearningLibraryList();
        
        assertNotNull(libraryList);
        assertEquals(libraryList.size(), 2);
        
        LearningLibrary library1 = (LearningLibrary)libraryList.get(0);
        LearningLibrary library2 = (LearningLibrary)libraryList.get(1);
        
        assertEquals(library1.getLibraryInsertScriptPath(), LIBRARY_INSERT_SCRIPT1);
        assertEquals(library2.getLibraryInsertScriptPath(), LIBRARY_INSERT_SCRIPT2);
        
        assertEquals(library1.getTemplateActivityInsertScriptPath(), TEMPLATE_ACTIVITY_INSERT_SCRIPT1);
        assertEquals(library2.getTemplateActivityInsertScriptPath(), TEMPLATE_ACTIVITY_INSERT_SCRIPT2);
        
    }
    
    public void testGetToolActivitiesFromLearningLibrary()
    {
        
       ArrayList libraryList = config.getLearningLibraryList(); 
       
       LearningLibrary library1 = (LearningLibrary)libraryList.get(0);
       LearningLibrary library2 = (LearningLibrary)libraryList.get(1);
       
       /** Tool activities that belong to the first learning library */
       ArrayList toolActivities_library1 = library1.getToolActivityList();
       assertNotNull(toolActivities_library1);
       assertTrue(toolActivities_library1.size()== 2);
       
       ToolActivity toolActivity1 = (ToolActivity)toolActivities_library1.get(0);
       assertNotNull(toolActivity1);
       assertEquals(toolActivity1.getToolActivityInsertScriptPath(), TOOL_ACTIVITY_INSERT_SCRIPT_1_LIB1);
       assertEquals(toolActivity1.getToolSignature(), TOOL_SIGNATURE_1_LIB1);
       
       ToolActivity toolActivity2 = (ToolActivity)toolActivities_library1.get(1);
       assertNotNull(toolActivity2);
       assertEquals(toolActivity2.getToolActivityInsertScriptPath(), TOOL_ACTIVITY_INSERT_SCRIPT_2_LIB1);
       assertEquals(toolActivity2.getToolSignature(), TOOL_SIGNATURE_2_LIB1);
       
       /** Tool activities that belong to the second learning library */
       ArrayList toolActivities_library2 = library2.getToolActivityList();
       assertNotNull(toolActivities_library2);
       assertTrue(toolActivities_library2.size()== 2);
       
       ToolActivity toolActivity1_lib2 = (ToolActivity)toolActivities_library2.get(0);
       assertNotNull(toolActivity1_lib2);
       assertEquals(toolActivity1_lib2.getToolActivityInsertScriptPath(), TOOL_ACTIVITY_INSERT_SCRIPT_1_LIB2);
       assertEquals(toolActivity1_lib2.getToolSignature(), TOOL_SIGNATURE_1_LIB2);
       
       ToolActivity toolActivity2_lib2 = (ToolActivity)toolActivities_library2.get(1);
       assertNotNull(toolActivity2_lib2);
       assertEquals(toolActivity2_lib2.getToolActivityInsertScriptPath(), TOOL_ACTIVITY_INSERT_SCRIPT_2_LIB2);
       assertEquals(toolActivity2_lib2.getToolSignature(), TOOL_SIGNATURE_2_LIB2);
       
    }
    
    public void testValidation()
    {
       // config2.setDbPassword(null);
        config2.setLearningLibraryList(null);
        try {
            config2.validateProperties();
            fail("Deployment exception should have been thrown as validation should have failed.");
        } catch ( DeployException e ) {
            System.out.println("Validation failed as expected. Message was "+e.getMessage());
            assertTrue("Validation failed.", true);
        }
    }
 
}
