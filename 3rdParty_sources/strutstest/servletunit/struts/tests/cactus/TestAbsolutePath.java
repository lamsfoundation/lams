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

package servletunit.struts.tests.cactus;

import servletunit.struts.CactusStrutsTestCase;
import org.apache.cactus.WebRequest;

import javax.servlet.http.Cookie;

public class TestAbsolutePath extends CactusStrutsTestCase {

    static final String COOKIE_NAME = "config_file";

    public TestAbsolutePath(String testName) {
        super(testName);
    }

    public void beginSuccessfulLogin(WebRequest theRequest) {
        if (logger.isDebugEnabled())
            logger.debug("setting cookie to " + System.getProperty("basedir") + "/src/examples/WEB-INF/struts-config.xml");
        theRequest.addCookie(COOKIE_NAME, System.getProperty("basedir") + "/src/examples/WEB-INF/struts-config.xml");
    }

    public void testSuccessfulLogin() {
        String fileName = getCookieValue(request.getCookies(), COOKIE_NAME);
        setConfigFile(fileName);
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("/login");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/main/success.jsp");
        assertEquals("deryl",getSession().getAttribute("authentication"));
        verifyNoActionErrors();
    }

    private String getCookieValue (Cookie[] cookies, String name) {
        String value = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                logger.debug ("checking cookie " + cookies[i].getName());

                if (cookies[i].getName().equals(name)) {
                    value = cookies[i].getValue();
                    break;
                }
            }
        }
        return value;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestAbsolutePath.class);
    }


}

