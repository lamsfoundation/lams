//  StrutsTestCase - a JUnit extension for testing Struts actions
//  within the context of the ActionServlet.
//  Copyright (C) 2002 Deryl Seale
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the Apache Software License as
//  published by the Apache Software Foundation; either version 1.1
//  of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  Apache Software Foundation Licens for more details.
//
//  You may view the full text here: http://www.apache.org/LICENSE.txt
package servletunit.struts.tests;

import servletunit.struts.MockStrutsTestCase;
import java.io.File;
/**
 *
 * <p>Title: TestNoRequestPathInfo</p>
 * <p>Description: Confirms correct behavior if
 * actionPerfoem() is called prior to calling
 * setRequestPathInfo().</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Sean Pritchard
 * @version 1.0
 */
public class TestNoRequestPathInfo extends MockStrutsTestCase {

    public TestNoRequestPathInfo() {
    }

    public void setUp() throws Exception {
        super.setUp();
        this.setContextDirectory(new File(System.getProperty("basedir") + "/src/examples"));
        setConfigFile("/WEB-INF/struts-config-test.xml");
    }

    /**
     * this test assumes that web.xml and struts-config.xml
     * can be found.  If these files are found,
     * but no request path is set prior to calling
     * actionPerform(), we expect an IllegalStateException
     */
    public void testNoRequestPathInfo(){
        try{
            actionPerform();
            fail("IllegalStateException expected");
        }catch(IllegalStateException e){
            //expected
        }
    }
}