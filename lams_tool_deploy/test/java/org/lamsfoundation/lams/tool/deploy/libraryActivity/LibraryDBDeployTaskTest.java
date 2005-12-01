/*
 * Created on 1/12/2005
 *
 */
package org.lamsfoundation.lams.tool.deploy.libraryActivity;

import org.lamsfoundation.lams.tool.deploy.ToolDBTest;
import org.lamsfoundation.lams.tool.deploy.libraryActivity.LibraryDBDeployTask;
/**
 * @author mtruong
 *
 */
public class LibraryDBDeployTaskTest extends ToolDBTest {

    private DeployLibraryConfig config = null;
    
    public LibraryDBDeployTaskTest(String name)
    {
        super(name);
    }
    
    protected void setUp() throws java.lang.Exception
    {
        super.setUp();
        insertTestRecordsForLibraryDeploy("test/file/test.properties");
        config = new DeployLibraryConfig("test/file/library_deploy/deployLibraryTest.xml");
    }
    
    protected void tearDown() throws java.lang.Exception
    {
       super.tearDown();
    }
    
      
    public void testExecute() throws Exception
    {
        LibraryDBDeployTask task = new LibraryDBDeployTask(config);
        task.execute();
        
    }
}
