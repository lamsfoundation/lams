/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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
package org.lamsfoundation.lams.tool.deploy;

import junit.framework.*;
import org.apache.commons.io.FileUtils;
import java.io.IOException;
import java.io.File;
/**
 *
 * @author chris
 */
public class DeployTest extends ToolDBTest
{
    
    public DeployTest(String testName)
    {
        super(testName);
    }
    
    protected void setUp() throws java.lang.Exception
    {
        super.setUp();
        File testedApplicationXml = new File(props.getString("testEarPath")+"/META-INF/application.xml");
        if (testedApplicationXml.exists())
        {
            FileUtils.forceDelete(testedApplicationXml);
        }
        File testedWar = new File(props.getString("testWarPath"));
        if (testedWar.exists())
        {
            FileUtils.forceDelete(testedWar);
        }
        File testedJar =  new File(props.getString("testJarPath"));
        if (testedJar.exists())
        {
            FileUtils.forceDelete(testedJar);
        }
        FileUtils.copyFileToDirectory(new File(props.getString("testAppXmlPath")), new File(props.getString("testEarPath")+"/META-INF"));
    }
    
    protected void tearDown() throws java.lang.Exception
    {
        //super.tearDown();
        //FileUtils.forceDelete(new File(props.getString("testEarPath")+"/META-INF/application.xml"));
        super.tearDown();
        dropTestToolTable("test/file/test.properties");
    }
    
    public static junit.framework.Test suite()
    {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(DeployTest.class);
        
        return suite;
    }
    
    public void testMain() throws Exception
    {
        String[] args = {props.getString("testDeployPropertiesPath")};
        Deploy.main(args);
    }
    
}
