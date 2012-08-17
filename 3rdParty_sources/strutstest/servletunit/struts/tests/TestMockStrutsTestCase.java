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
 * A Junit test of the MockStrutsTestCase
 * @author Sean Pritchard
 */

public class TestMockStrutsTestCase extends MockStrutsTestCase {

    public TestMockStrutsTestCase(String testCase) {
        super(testCase);
    }

    public void setUp() throws Exception {
        super.setUp();
        setServletConfigFile("/WEB-INF/web.xml");
    }

    public void testSetInitParameter() throws Exception{
        setInitParameter("testName", "testValue");
        assertEquals("testValue", getActionServlet().getInitParameter("testName"));
    }

    public void testSetContextDirectory() {
        File file = new File(System.getProperty("basedir"));
        setContextDirectory(file);
        assertEquals(new File(file,"test.html").getAbsolutePath(),getRequest().getRealPath("/test.html"));
    }

    public void testGetRealPathNotSet() {
        assertNull(getRequest().getRealPath("/test.html"));
    }

}
