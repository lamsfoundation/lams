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
    
    public void testLibraryDeployMain()
    {
        //DeployLibrary deployer = new DeployLibrary();
        String[] args = {"test/file/library_deploy/deployLibraryTest.xml"};
        DeployLibrary.main(args);
    }
    
    
}
