/*
 * DeployFilesTaskTest.java
 * JUnit based test
 *
 * Created on 05 April 2005, 15:21
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
