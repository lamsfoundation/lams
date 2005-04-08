/*
 * DeployTest.java
 * JUnit based test
 *
 * Created on 08 April 2005, 10:55
 */

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
