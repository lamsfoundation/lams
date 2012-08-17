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

package servletunit.tests;

import junit.framework.TestCase;
import servletunit.ServletConfigSimulator;

import java.util.Enumeration;

public class TestInitParameters extends TestCase {

    ServletConfigSimulator config;

    public TestInitParameters(String testName) {
        super(testName);
    }

    public void setUp() {
        config = new ServletConfigSimulator();
    }

    public void testSetInitParameter() {
        config.setInitParameter("test","testValue");
        assertEquals("testValue",config.getInitParameter("test"));
    }

    public void testNoParameter() {
        assertNull(config.getInitParameter("badValue"));
    }

    public void testGetInitParameterNames() {
        config.setInitParameter("test","testValue");
        config.setInitParameter("another","anotherValue");
        assertEquals("testValue",config.getInitParameter("test"));
        assertEquals("anotherValue",config.getInitParameter("another"));
        Enumeration names = config.getInitParameterNames();
        boolean fail = true;
        while (names.hasMoreElements()) {
            fail = true;
            String name = (String) names.nextElement();
            if ((name.equals("test")) || (name.equals("another")))
                fail = false;
        }
        if (fail)
            fail();
    }

}


