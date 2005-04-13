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

import junit.framework.*;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author chris
 */
public class DeployFilesTaskTest extends TestCase
{
    public static final String TEST_WAR_PATH = "test/file/test-dummy.war";
    public static final String TEST_JAR_PATH = "test/file/test-dummy.jar";
    public static final String TEST_TARGET_PATH = "test/file/lams.ear";
    public static final String COPIED_TEST_WAR_PATH = TEST_TARGET_PATH+"/test-dummy.war";
    public static final String COPIED_TEST_JAR_PATH = TEST_TARGET_PATH+"/test-dummy.jar";
    
    public static final List testDeployFiles = new ArrayList();
    static 
    {
        testDeployFiles.add(TEST_WAR_PATH);
        testDeployFiles.add(TEST_JAR_PATH);
    }
    
    private DeployFilesTask deployer = null;
    
    public DeployFilesTaskTest(String testName)
    {
        super(testName);
    }

    protected void setUp() throws java.lang.Exception
    {
        deployer = new DeployFilesTask();
    }

    protected void tearDown() throws java.lang.Exception
    {
        FileUtils.forceDelete(new File(COPIED_TEST_WAR_PATH));
        FileUtils.forceDelete(new File(COPIED_TEST_JAR_PATH));
    }

    public static junit.framework.Test suite()
    {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(DeployFilesTaskTest.class);
        
        return suite;
    }
    
    public void testExecute() throws Exception
    {
        deployer.setLamsEarPath(TEST_TARGET_PATH);
        deployer.setDeployFiles(testDeployFiles);
        deployer.execute();
        assertTrue((new File(COPIED_TEST_WAR_PATH)).exists());
        assertTrue((new File(COPIED_TEST_JAR_PATH)).exists());
    }
    
    
    
}
